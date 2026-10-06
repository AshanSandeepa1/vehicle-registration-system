/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Ashan
 */
public class DBConnection {
    static Connection conn;
    static Statement stat = null;
    
public static Statement getStatementConnection() {
    try {
        
//Establish the connection
    String url = "jdbc:mysql://localhost:3306/vrs_db";
    conn = DriverManager.getConnection(url, "root", "");
    
//Create the connection
    stat = conn.createStatement();
 } catch (Exception e) {
e.printStackTrace();
}
return stat;
}

// Close the connection
    public static void closeCon() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// Add a method to create a PreparedStatement object
    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }
}
