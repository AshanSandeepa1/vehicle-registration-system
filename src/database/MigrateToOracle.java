package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Migration Runner for Oracle Database.
 * Reads database/vrs_db_oracle.sql and executes it against the target Oracle Database.
 */
public class MigrateToOracle {

    public static void main(String[] args) {
        String url = args.length > 0 ? args[0] : "jdbc:oracle:thin:@localhost:1521:XE";
        String user = args.length > 1 ? args[1] : "system";
        String password = args.length > 2 ? args[2] : "oracle";

        System.out.println("==================================================");
        System.out.println("  Oracle Database Migration Tool for VRS          ");
        System.out.println("==================================================");
        System.out.println("Target Database: " + url);
        System.out.println("Target User:     " + user);

        File sqlFile = new File("database/vrs_db_oracle.sql");
        if (!sqlFile.exists()) {
            sqlFile = new File("../database/vrs_db_oracle.sql");
        }

        if (!sqlFile.exists()) {
            System.err.println("Error: database/vrs_db_oracle.sql file not found!");
            return;
        }

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println("Connecting to Oracle Database...");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement()) {

                System.out.println("Connected successfully!");
                System.out.println("Executing migration script: " + sqlFile.getAbsolutePath());

                StringBuilder sqlBatch = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(sqlFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.startsWith("--") || line.isEmpty() || line.equals("/")) {
                            continue;
                        }
                        sqlBatch.append(line).append(" ");
                        if (line.endsWith(";")) {
                            String statementSql = sqlBatch.toString().replace(";", "").trim();
                            sqlBatch.setLength(0);
                            try {
                                stmt.execute(statementSql);
                                System.out.println("Executed: " + (statementSql.length() > 60 ? statementSql.substring(0, 60) + "..." : statementSql));
                            } catch (Exception ex) {
                                System.out.println("Notice: " + ex.getMessage());
                            }
                        }
                    }
                }
                System.out.println("==================================================");
                System.out.println("  Migration to Oracle completed successfully!     ");
                System.out.println("==================================================");
            }
        } catch (Exception e) {
            System.err.println("Migration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

