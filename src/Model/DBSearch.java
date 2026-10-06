/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 * Data Access Layer for VRS.
 * Provides parameterized SQL queries to prevent SQL injection and ensure
 * compatibility with Oracle and other standard SQL engines.
 *
 * @author Ashan & Refactored for Oracle Migration
 */
public class DBSearch {

    private Statement stmt;

    public DBSearch() {
        this.stmt = DBConnection.getStatementConnection();
    }

    /**
     * Searches for a user by email.
     * Uses PreparedStatement to prevent SQL injection.
     */
    public ResultSet searchLogin(String loginEmail) {
        ResultSet rs = null;
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM login WHERE LOWER(email) = LOWER(?)");
                ps.setString(1, loginEmail.trim());
                rs = ps.executeQuery();
            }
        } catch (SQLException e) {
            System.err.println("[DBSearch] Error searching login for email: " + loginEmail);
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * Registers a new user account into the database.
     * Uses PreparedStatement for secure parameterized insertion.
     */
    public void addUser(String regUsername, String regEmail, String regPassword) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO login (username, email, password) VALUES (?, ?, ?)"
                );
                ps.setString(1, regUsername.trim());
                ps.setString(2, regEmail.trim());
                ps.setString(3, regPassword);
                ps.executeUpdate();
                System.out.println("[DBSearch] Successfully registered user: " + regEmail);
            }
        } catch (SQLException e) {
            System.err.println("[DBSearch] Error registering user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Checks the application status for a given application number and vehicle number.
     * Uses PreparedStatement.
     */
    public String checkStatus(String applicationNum, String vehicleNum) {
        String status = null;
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement(
                    "SELECT applicationstatus FROM checkstatus WHERE UPPER(applicationnum) = UPPER(?)"
                );
                ps.setString(1, applicationNum != null ? applicationNum.trim() : "");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        status = rs.getString("applicationstatus");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[DBSearch] Error checking status: " + e.getMessage());
            e.printStackTrace();
        }
        return status;
    }

    /**
     * Adds or updates a checkstatus record.
     */
    public boolean addCheckStatus(String applicationNum, String vehicleNum, String status) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO checkstatus (applicationnum, vehiclenum, applicationstatus) VALUES (?, ?, ?)"
                );
                ps.setString(1, applicationNum != null ? applicationNum.trim() : "");
                ps.setString(2, vehicleNum != null ? vehicleNum.trim() : "");
                ps.setString(3, status != null ? status.trim() : "1");
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[DBSearch] Error adding checkstatus: " + e.getMessage());
            e.printStackTrace();
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
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO vehicle_details (application_num, vehicle_type, vehicle_num, fuel_type) VALUES (?, ?, ?, ?)"
                );
                ps.setString(1, applicationNum != null ? applicationNum.trim() : "");
                ps.setString(2, vehicleType != null ? vehicleType.trim() : "Car");
                ps.setString(3, vehicleNum != null ? vehicleNum.trim() : "");
                ps.setString(4, fuelType != null ? fuelType.trim() : "Petrol");
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[DBSearch] Error adding vehicle_details: " + e.getMessage());
            e.printStackTrace();
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
                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE checkstatus SET applicationstatus = ? WHERE UPPER(applicationnum) = UPPER(?)"
                );
                ps.setString(1, newStatus != null ? newStatus.trim() : "1");
                ps.setString(2, applicationNum != null ? applicationNum.trim() : "");
                int updated = ps.executeUpdate();
                return updated > 0;
            }
        } catch (SQLException e) {
            System.err.println("[DBSearch] Error updating checkstatus: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes an application from checkstatus and vehicle_details.
     */
    public boolean deleteApplication(String applicationNum) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                PreparedStatement ps1 = conn.prepareStatement(
                    "DELETE FROM checkstatus WHERE UPPER(applicationnum) = UPPER(?)"
                );
                ps1.setString(1, applicationNum.trim());
                ps1.executeUpdate();

                PreparedStatement ps2 = conn.prepareStatement(
                    "DELETE FROM vehicle_details WHERE UPPER(application_num) = UPPER(?)"
                );
                ps2.setString(1, applicationNum.trim());
                ps2.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[DBSearch] Error deleting application: " + e.getMessage());
            e.printStackTrace();
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
                Statement s = conn.createStatement();
                ResultSet rs = s.executeQuery("SELECT applicationnum, vehiclenum, applicationstatus FROM checkstatus ORDER BY applicationnum ASC");
                while (rs.next()) {
                    String appNum = rs.getString(1);
                    String vehNum = rs.getString(2);
                    String status = rs.getString(3);
                    String desc = getStatusDescription(status);
                    model.addRow(new Object[]{appNum, vehNum, status, desc});
                }
            }
        } catch (SQLException e) {
            System.err.println("[DBSearch] Error loading checkstatus table: " + e.getMessage());
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
                Statement s = conn.createStatement();
                ResultSet rs = s.executeQuery("SELECT application_num, vehicle_type, vehicle_num, fuel_type FROM vehicle_details ORDER BY application_num ASC");
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
                }
            }
        } catch (SQLException e) {
            System.err.println("[DBSearch] Error loading vehicle table: " + e.getMessage());
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
                Statement s = conn.createStatement();
                ResultSet rs = s.executeQuery("SELECT indexID, username, email FROM login ORDER BY indexID ASC");
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3)});
                }
            }
        } catch (SQLException e) {
            System.err.println("[DBSearch] Error loading users table: " + e.getMessage());
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
