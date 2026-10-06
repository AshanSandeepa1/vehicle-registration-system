/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 *
 * @author Ashan
 */
public class DBSearch {
    Statement stmt;
    ResultSet rs;
    
public ResultSet searchLogin(String loginEmail) {
 try {
 stmt = DBConnection.getStatementConnection();
 String userEmail = loginEmail;
 
//Execute the Query
    rs = stmt.executeQuery("SELECT * FROM login where email='" + userEmail + "'");
 } catch (Exception e) {
 e.printStackTrace();
 }
 return rs;
 }
   
// Add new user login to system
public void addUser(String regUsername, String regEmail, String regPassword) {
    
 try {
stmt = DBConnection.getStatementConnection();
stmt.executeUpdate("INSERT INTO login (username, email, password) VALUES ('" + regUsername + "','" + regEmail + "','" + regPassword + "')");
 } catch (Exception e) {
 e.printStackTrace();
 }
}

public DBSearch() {
        this.stmt = DBConnection.getStatementConnection(); // Initialize the statement when creating a new DBSearch object
    }
    
public String checkStatus(String applicationNum, String vehicleNum) {
        String status = null;
        try {
            ResultSet rs = stmt.executeQuery("SELECT applicationstatus FROM checkstatus WHERE applicationnum = '" + applicationNum + "'");
            if (rs.next()) {
                status = rs.getString("applicationstatus");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }
}
