package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 * Data Access Layer for VRS.
 * Provides parameterized SQL queries to prevent SQL injection and ensures
 * strict resource cleanup with try-with-resources.
 *
 * @author Ashan & Refactored for Oracle Migration & Production Hardening
 */
public class DBSearch {

    private static final Logger LOG = Logger.getLogger(DBSearch.class.getName());

    public DBSearch() {
    }

    /**
     * User account representation.
     */
    public record UserAccount(long id, String username, String email, String password) {}

    /**
     * Finds a user account by email address.
     * Safely executes with try-with-resources to avoid cursor leaks.
     */
    public UserAccount findUserByEmail(String loginEmail) {
        if (loginEmail == null || loginEmail.isBlank()) {
            return null;
        }
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT indexID, username, email, password FROM login WHERE LOWER(email) = LOWER(?)")) {
                    ps.setString(1, loginEmail.trim());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return new UserAccount(
                                rs.getLong(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4)
                            );
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error searching login for email: " + loginEmail, e);
        }
        return null;
    }

    /**
     * Searches for a user by email (backward-compatible).
     * Uses PreparedStatement to prevent SQL injection.
     */
    public ResultSet searchLogin(String loginEmail) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM login WHERE LOWER(email) = LOWER(?)");
                ps.setString(1, loginEmail != null ? loginEmail.trim() : "");
                return ps.executeQuery();
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error searching login for email: " + loginEmail, e);
        }
        return null;
    }

    /**
     * Registers a new user account into the database.
     * Uses PreparedStatement for secure parameterized insertion.
     *
     * @return true if insertion was successful, false otherwise.
     */
    public boolean addUser(String regUsername, String regEmail, String regPassword) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO login (username, email, password) VALUES (?, ?, ?)"
                )) {
                    ps.setString(1, regUsername != null ? regUsername.trim() : "");
                    ps.setString(2, regEmail != null ? regEmail.trim() : "");
                    ps.setString(3, regPassword != null ? regPassword : "");
                    int rows = ps.executeUpdate();
                    LOG.info(() -> "Successfully registered user: " + regEmail);
                    return rows > 0;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error registering user: " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * Checks the application status for a given application number and vehicle number.
     */
    public String checkStatus(String applicationNum, String vehicleNum) {
        if (applicationNum == null || applicationNum.isBlank()) {
            return null;
        }
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                boolean hasVehNum = vehicleNum != null && !vehicleNum.isBlank();
                String sql = hasVehNum
                    ? "SELECT applicationstatus FROM checkstatus WHERE UPPER(applicationnum) = UPPER(?) AND UPPER(vehiclenum) = UPPER(?)"
                    : "SELECT applicationstatus FROM checkstatus WHERE UPPER(applicationnum) = UPPER(?)";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, applicationNum.trim());
                    if (hasVehNum) {
                        ps.setString(2, vehicleNum.trim());
                    }
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return rs.getString("applicationstatus");
                        }
                    }
                }

                // If queried with vehicle number and not found, check if application number alone matches
                if (hasVehNum) {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "SELECT applicationstatus FROM checkstatus WHERE UPPER(applicationnum) = UPPER(?)")) {
                        ps.setString(1, applicationNum.trim());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                return rs.getString("applicationstatus");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error checking status for app: " + applicationNum, e);
        }
        return null;
    }

    /**
     * Submits a new vehicle application atomically, generating a unique application ID
     * and verifying uniqueness before inserting both checkstatus and vehicle_details records.
     * Retries up to 5 times if an ID collision is encountered.
     *
     * @return the generated unique application reference number, or null if submission failed
     */
    public String submitNewApplication(String vehicleType, String vehicleNum, String fuelType) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            LOG.severe("Database connection unavailable for submitNewApplication.");
            return null;
        }

        for (int attempt = 0; attempt < 5; attempt++) {
            String appNum = util.ApplicationIdGenerator.generateFormattedString();
            if (applicationExists(conn, appNum)) {
                continue;
            }

            try {
                boolean prevAutoCommit = conn.getAutoCommit();
                conn.setAutoCommit(false);
                try (PreparedStatement psCheck = conn.prepareStatement(
                        "INSERT INTO checkstatus (applicationnum, vehiclenum, applicationstatus) VALUES (?, ?, ?)");
                     PreparedStatement psVeh = conn.prepareStatement(
                        "INSERT INTO vehicle_details (application_num, vehicle_type, vehicle_num, fuel_type) VALUES (?, ?, ?, ?)")) {

                    String sanitizedVeh = sanitizeVehNum(vehicleNum);
                    psCheck.setString(1, appNum);
                    psCheck.setString(2, sanitizedVeh);
                    psCheck.setString(3, "1");
                    psCheck.executeUpdate();

                    psVeh.setString(1, appNum);
                    psVeh.setString(2, vehicleType != null ? vehicleType.trim() : "Car");
                    psVeh.setString(3, sanitizedVeh);
                    psVeh.setString(4, fuelType != null ? fuelType.trim() : "Petrol");
                    psVeh.executeUpdate();

                    conn.commit();
                    return appNum;
                } catch (SQLException ex) {
                    conn.rollback();
                    LOG.log(Level.WARNING, "Error inserting application on attempt " + attempt + ", retrying: " + ex.getMessage());
                } finally {
                    conn.setAutoCommit(prevAutoCommit);
                }
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Transaction error in submitNewApplication", ex);
            }
        }
        return null;
    }

    private static String sanitizeVehNum(String vehNum) {
        if (vehNum == null) return "";
        String trimmed = vehNum.trim();
        return trimmed.length() > 20 ? trimmed.substring(0, 20) : trimmed;
    }

    private boolean applicationExists(Connection conn, String appNum) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM checkstatus WHERE UPPER(applicationnum) = UPPER(?)")) {
            ps.setString(1, appNum);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Adds a checkstatus record.
     */
    public boolean addCheckStatus(String applicationNum, String vehicleNum, String status) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO checkstatus (applicationnum, vehiclenum, applicationstatus) VALUES (?, ?, ?)"
                )) {
                    ps.setString(1, applicationNum != null ? applicationNum.trim() : "");
                    ps.setString(2, sanitizeVehNum(vehicleNum));
                    ps.setString(3, status != null ? status.trim() : "1");
                    return ps.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error adding checkstatus: " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * Adds vehicle details to vehicle_details table.
     */
    public boolean addVehicleDetails(String applicationNum, String vehicleType, String vehicleNum, String fuelType) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO vehicle_details (application_num, vehicle_type, vehicle_num, fuel_type) VALUES (?, ?, ?, ?)"
                )) {
                    ps.setString(1, applicationNum != null ? applicationNum.trim() : "");
                    ps.setString(2, vehicleType != null ? vehicleType.trim() : "Car");
                    ps.setString(3, sanitizeVehNum(vehicleNum));
                    ps.setString(4, fuelType != null ? fuelType.trim() : "Petrol");
                    return ps.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error adding vehicle_details: " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * Updates an existing application's status in checkstatus.
     */
    public boolean updateCheckStatus(String applicationNum, String newStatus) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE checkstatus SET applicationstatus = ? WHERE UPPER(applicationnum) = UPPER(?)"
                )) {
                    ps.setString(1, newStatus != null ? newStatus.trim() : "1");
                    ps.setString(2, applicationNum != null ? applicationNum.trim() : "");
                    int updated = ps.executeUpdate();
                    return updated > 0;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error updating checkstatus: " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * Deletes an application from checkstatus and vehicle_details.
     */
    public boolean deleteApplication(String applicationNum) {
        if (applicationNum == null || applicationNum.isBlank()) {
            return false;
        }
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                try (PreparedStatement ps1 = conn.prepareStatement(
                        "DELETE FROM checkstatus WHERE UPPER(applicationnum) = UPPER(?)");
                     PreparedStatement ps2 = conn.prepareStatement(
                        "DELETE FROM vehicle_details WHERE UPPER(application_num) = UPPER(?)")) {
                    ps1.setString(1, applicationNum.trim());
                    ps1.executeUpdate();
                    ps2.setString(1, applicationNum.trim());
                    ps2.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error deleting application: " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * Populates table model with all applications from checkstatus.
     */
    public void loadCheckStatusTable(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                try (Statement s = conn.createStatement();
                     ResultSet rs = s.executeQuery("SELECT applicationnum, vehiclenum, applicationstatus FROM checkstatus ORDER BY applicationnum ASC")) {
                    while (rs.next()) {
                        String appNum = rs.getString(1);
                        String vehNum = rs.getString(2);
                        String status = rs.getString(3);
                        String desc = getStatusDescription(status);
                        model.addRow(new Object[]{appNum, vehNum, status, desc});
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error loading checkstatus table: " + e.getMessage(), e);
        }
    }

    /**
     * Populates table model with all vehicle records from vehicle_details.
     */
    public void loadVehicleTable(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                try (Statement s = conn.createStatement();
                     ResultSet rs = s.executeQuery("SELECT application_num, vehicle_type, vehicle_num, fuel_type FROM vehicle_details ORDER BY application_num ASC")) {
                    while (rs.next()) {
                        model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error loading vehicle table: " + e.getMessage(), e);
        }
    }

    /**
     * Populates table model with all registered users from login.
     */
    public void loadUsersTable(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                try (Statement s = conn.createStatement();
                     ResultSet rs = s.executeQuery("SELECT indexID, username, email FROM login ORDER BY indexID ASC")) {
                    while (rs.next()) {
                        model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3)});
                    }
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Error loading users table: " + e.getMessage(), e);
        }
    }

    public static String getStatusDescription(String status) {
        if (status == null) return "Unknown";
        return switch (status.trim()) {
            case "1" -> "1 - Application Submitted";
            case "2" -> "2 - Transferor Approved";
            case "3" -> "3 - DMT Validator Approved";
            case "4" -> "4 - Document Released (PDF)";
            case "5" -> "5 - Document Released (Post)";
            default -> "Unknown (" + status + ")";
        };
    }
}
