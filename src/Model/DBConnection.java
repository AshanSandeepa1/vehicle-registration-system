package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database Connection Manager.
 * Supports Oracle Database (thin driver) with automated fallback to an
 * embedded Oracle-compatible in-memory database for testing and offline execution.
 *
 * Configuration precedence (highest first):
 *   1. JVM system properties  (-Dvrs.db.url=..., -Dvrs.db.username=..., -Dvrs.db.password=..., -Dvrs.db.fallback.enabled=...)
 *   2. Environment variables  (VRS_DB_URL, VRS_DB_USERNAME, VRS_DB_PASSWORD, VRS_DB_FALLBACK_ENABLED, VRS_DB_DRIVER)
 *   3. config/db.properties
 *   4. Built-in development defaults
 *
 * @author Ashan & Refactored for Oracle Migration
 */
public final class DBConnection {

    private static final Logger LOG = Logger.getLogger(DBConnection.class.getName());
    private static final int VALIDATION_TIMEOUT_SECONDS = 2;

    private static Connection conn = null;
    private static String databaseType = "Unknown";
    private static final Properties config = new Properties();

    static {
        loadConfig();
        Runtime.getRuntime().addShutdownHook(new Thread(DBConnection::closeQuietly, "vrs-db-shutdown"));
    }

    private DBConnection() {
    }

    private static void loadConfig() {
        // Development defaults (overridable by file, env vars or system properties)
        config.setProperty("db.driver", "oracle.jdbc.OracleDriver");
        config.setProperty("db.url", "jdbc:oracle:thin:@localhost:1521:XE");
        config.setProperty("db.username", "system");
        config.setProperty("db.password", "oracle");
        config.setProperty("db.fallback.enabled", "true");

        File primary = new File("config/db.properties");
        final File configFile = primary.exists() ? primary : new File("../config/db.properties");
        if (configFile.exists()) {
            try (InputStream in = new FileInputStream(configFile)) {
                config.load(in);
                LOG.fine(() -> "Loaded database configuration from " + configFile.getAbsolutePath());
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Could not read config/db.properties; using defaults.", e);
            }
        }

        applyOverride("db.driver", "VRS_DB_DRIVER");
        applyOverride("db.url", "VRS_DB_URL");
        applyOverride("db.username", "VRS_DB_USERNAME");
        applyOverride("db.password", "VRS_DB_PASSWORD");
        applyOverride("db.fallback.enabled", "VRS_DB_FALLBACK_ENABLED");
    }

    private static void applyOverride(String key, String envName) {
        String env = System.getenv(envName);
        if (env != null && !env.isBlank()) {
            config.setProperty(key, env.trim());
        }
        String sys = System.getProperty("vrs." + key);
        if (sys != null && !sys.isBlank()) {
            config.setProperty(key, sys.trim());
        }
    }

    /**
     * Obtains the shared connection, reconnecting if it was closed or dropped.
     * Tries Oracle first; if unreachable and fallback is enabled, initializes
     * an embedded Oracle-mode database.
     *
     * @return an open connection, or null if no database is reachable
     */
    public static synchronized Connection getConnection() {
        if (isUsable(conn)) {
            return conn;
        }
        closeQuietly();

        String driver = config.getProperty("db.driver");
        String url = config.getProperty("db.url");
        String user = config.getProperty("db.username");
        String pass = config.getProperty("db.password");
        boolean fallbackEnabled = Boolean.parseBoolean(config.getProperty("db.fallback.enabled", "true"));

        try {
            Class.forName(driver);
            // Bound the wait so the UI never hangs on an unreachable server.
            DriverManager.setLoginTimeout(3);
            conn = DriverManager.getConnection(url, user, pass);
            databaseType = "Oracle Database (" + url + ")";
            LOG.info(() -> "Connected to " + databaseType);
            return conn;
        } catch (Exception primaryEx) {
            LOG.log(Level.WARNING, "Primary database unavailable: {0}", primaryEx.getMessage());
            if (!fallbackEnabled) {
                LOG.severe("Fallback database disabled; no database connection available.");
                return null;
            }
        }

        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:mem:vrs_db;MODE=Oracle;DB_CLOSE_DELAY=-1", "sa", "");
            databaseType = "Oracle-mode Embedded Fallback (Offline Mode)";
            initFallbackDatabase(conn);
            LOG.info("Embedded fallback database initialized with seed data.");
        } catch (Exception fallbackEx) {
            LOG.log(Level.SEVERE, "Fallback database could not be started.", fallbackEx);
            conn = null;
        }
        return conn;
    }

    private static boolean isUsable(Connection c) {
        if (c == null) {
            return false;
        }
        try {
            return !c.isClosed() && c.isValid(VALIDATION_TIMEOUT_SECONDS);
        } catch (SQLException e) {
            return false;
        }
    }

    private static synchronized void closeQuietly() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) {
                // Closing a broken connection is best-effort.
            }
            conn = null;
        }
    }

    /**
     * Creates schema, indexes and seed data in the fallback database.
     */
    private static void initFallbackDatabase(Connection c) throws SQLException {
        try (Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS login ("
                    + "indexID NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, "
                    + "username VARCHAR2(50) NOT NULL, "
                    + "email VARCHAR2(50) NOT NULL UNIQUE, "
                    + "password VARCHAR2(50) NOT NULL)");

            s.execute("CREATE TABLE IF NOT EXISTS checkstatus ("
                    + "applicationnum VARCHAR2(8) NOT NULL PRIMARY KEY, "
                    + "vehiclenum VARCHAR2(20) NOT NULL, "
                    + "applicationstatus VARCHAR2(1) NOT NULL)");

            s.execute("CREATE TABLE IF NOT EXISTS vehicle_details ("
                    + "application_num VARCHAR2(10) UNIQUE, "
                    + "vehicle_type VARCHAR2(25), "
                    + "vehicle_num VARCHAR2(20), "
                    + "fuel_type VARCHAR2(20), "
                    + "reg_certificate_pdf BLOB, "
                    + "revenue_license_pdf BLOB)");

            s.execute("CREATE INDEX IF NOT EXISTS idx_checkstatus_vehiclenum ON checkstatus (vehiclenum)");
            s.execute("CREATE INDEX IF NOT EXISTS idx_vehdetails_vehnum ON vehicle_details (vehicle_num)");

            if (isEmpty(s, "login")) {
                s.execute("INSERT INTO login (indexID, username, email, password) VALUES (1, 'Shamil Suraweera', 'shamil@vrs.com', '123')");
                s.execute("INSERT INTO login (indexID, username, email, password) VALUES (2, 'Ashan Suraweera', 'ashan@vrs.com', '123')");
                s.execute("INSERT INTO login (indexID, username, email, password) VALUES (3, 'John Doe', 'john@vrs.com', '123')");
                // Explicit IDs were used above; move the identity past them so new registrations don't collide.
                s.execute("ALTER TABLE login ALTER COLUMN indexID RESTART WITH 4");
            }

            if (isEmpty(s, "checkstatus")) {
                s.execute("INSERT INTO checkstatus VALUES ('1234AA', 'BX-7878', '1')");
                s.execute("INSERT INTO checkstatus VALUES ('1255AB', 'BO-7888', '3')");
                s.execute("INSERT INTO checkstatus VALUES ('1000AA', 'AS-2323', '5')");
                s.execute("INSERT INTO checkstatus VALUES ('0001AA', 'AB-2323', '4')");
                s.execute("INSERT INTO checkstatus VALUES ('0525YZ', 'AS-9090', '3')");
                s.execute("INSERT INTO checkstatus VALUES ('1687ZW', 'ASU-3434', '2')");
            }

            if (isEmpty(s, "vehicle_details")) {
                s.execute("INSERT INTO vehicle_details (application_num, vehicle_type, vehicle_num, fuel_type) VALUES ('0001AA', 'Car', 'AB-2323', 'Petrol')");
                s.execute("INSERT INTO vehicle_details (application_num, vehicle_type, vehicle_num, fuel_type) VALUES ('1687ZW', 'Car', 'ASU-3434', 'Petrol')");
                s.execute("INSERT INTO vehicle_details (application_num, vehicle_type, vehicle_num, fuel_type) VALUES ('0525YZ', 'Motor Bike', 'AS-9090', 'Diesel')");
                s.execute("INSERT INTO vehicle_details (application_num, vehicle_type, vehicle_num, fuel_type) VALUES ('5224VD', 'Car', 'WW-9090', 'Petrol')");
            }
        }
    }

    private static boolean isEmpty(Statement s, String table) throws SQLException {
        try (ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM " + table)) {
            return rs.next() && rs.getInt(1) == 0;
        }
    }

    /**
     * Creates a PreparedStatement from the shared connection.
     * Callers own the returned statement and must close it.
     */
    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        Connection c = getConnection();
        if (c == null) {
            throw new SQLException("No database connection is available. Check that Oracle is running or enable the offline fallback.");
        }
        return c.prepareStatement(sql);
    }

    public static String getDatabaseType() {
        return databaseType;
    }
}
