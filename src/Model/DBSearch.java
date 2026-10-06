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
}
