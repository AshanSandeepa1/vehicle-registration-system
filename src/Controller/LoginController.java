/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import View.vrsView;
import Model.DBConnection;
import Model.DBSearch;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class LoginController {

    public static void login(String loginEmail, String loginPass) {
        if (loginEmail == null || loginEmail.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter an email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isValidEmail(loginEmail.trim())) {
            JOptionPane.showMessageDialog(null, "Invalid Email Format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String email = null;
            String password = null;
            ResultSet rs = new DBSearch().searchLogin(loginEmail.trim());

            if (rs != null) {
                while (rs.next()) {
                    try {
                        email = rs.getString("email");
                    } catch (SQLException ex) {
                        email = rs.getString("EMAIL");
                    }
                    try {
                        password = rs.getString("password");
                    } catch (SQLException ex) {
                        password = rs.getString("PASSWORD");
                    }
                }
            }

            if (email != null && password != null) {
                if (password.equals(loginPass)) {
                    System.out.println("[LoginController] Login Successful for: " + email);

                    // Actions after successful login
                    vrsView.switchTabs(vrsView.homePanel, vrsView.logregLayeredPane);
                    vrsView.LOGIN.setVisible(false);
                    vrsView.REGISTER.setVisible(false);
                    vrsView.firstRegistration.setVisible(true);
                    vrsView.ownershipTransfer.setVisible(true);
                    vrsView.admin.setVisible(true);
                    JOptionPane.showMessageDialog(null, "Welcome! Login Successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Email not found. Please register first.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void userRegister(String regUsername, String regEmail, String regPassword, String regConfirmPassword) {
        if (regUsername == null || regUsername.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the email is valid
        if (!isValidEmail(regEmail != null ? regEmail.trim() : "")) {
            JOptionPane.showMessageDialog(null, "Invalid email format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if passwords are provided and match
        if (regPassword == null || regPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!regPassword.equals(regConfirmPassword)) {
            JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Proceed with registration
        new Model.DBSearch().addUser(regUsername.trim(), regEmail.trim(), regPassword);
        JOptionPane.showMessageDialog(null, "You are successfully registered. Please login.", "Successful", JOptionPane.INFORMATION_MESSAGE);

        // Set Login Visible
        vrsView.switchTabs(vrsView.loginPanel, vrsView.logregLayeredPane);
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
