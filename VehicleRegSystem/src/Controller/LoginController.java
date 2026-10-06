/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import View.vrsView;
import Model.DBConnection;
import Model.DBSearch;
import java.awt.Frame;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class LoginController {
    
    public static void login(String loginEmail, String loginPass) {
 if (!isValidEmail(loginEmail)) {
            JOptionPane.showMessageDialog(null, "Invalid Email Format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String email = null; // initial value of the email
            String password = null; // initial value of the password
            ResultSet rs = new DBSearch().searchLogin(loginEmail);
            
            // Process the query
            while (rs.next()) {
                email = rs.getString("email"); // assign database email to the variable
                password = rs.getString("password"); // assign database password to the variable
            }
            
            if (email != null && password != null) {
                if (password.equals(loginPass)) {
                    System.out.println("Login Successful");
                    
                    // Actions after successful login
                    vrsView.switchTabs(vrsView.homePanel, vrsView.logregLayeredPane);
                    vrsView.LOGIN.setVisible(false);
                    vrsView.REGISTER.setVisible(false);
                    vrsView.firstRegistration.setVisible(true);
                    vrsView.ownershipTransfer.setVisible(true);
                    vrsView.admin.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Email not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            // Close the database connection
            DBConnection.closeCon();
        } catch (SQLException ex) { 
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public static void userRegister(String regUsername, String regEmail, String regPassword, String regConfirmPassword) {
    // Check if the email is valid
            if (!isValidEmail(regEmail)) {
                JOptionPane.showMessageDialog(null, "Invalid email format", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if the passwords match
            if (!regPassword.equals(regConfirmPassword)) {
                JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // If email is valid and passwords match, proceed with registration
            new Model.DBSearch().addUser(regUsername, regEmail, regPassword);
            JOptionPane.showMessageDialog(null, "You are successfully registered. Please login.", "Successful", JOptionPane.INFORMATION_MESSAGE);
            //Set Login Visible
            vrsView.switchTabs(vrsView.loginPanel, vrsView.logregLayeredPane);
        }
    
    
    private static boolean isValidEmail(String email) {
        // Regular expression for validating email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
 }


