package Controller;

import View.vrsView;
import Model.DBSearch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class LoginController {

    private static final Logger LOG = Logger.getLogger(LoginController.class.getName());

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
            DBSearch.UserAccount user = new DBSearch().findUserByEmail(loginEmail.trim());

            if (user != null) {
                if (user.password().equals(loginPass)) {
                    LOG.info(() -> "Login successful for: " + user.email());

                    // Actions after successful login
                    vrsView.switchTabs(vrsView.homePanel, vrsView.logregLayeredPane);
                    vrsView.LOGIN.setVisible(false);
                    vrsView.REGISTER.setVisible(false);
                    vrsView.firstRegistration.setVisible(true);
                    vrsView.ownershipTransfer.setVisible(true);
                    vrsView.admin.setVisible(true);
                    JOptionPane.showMessageDialog(null, "Welcome, " + user.username() + "! Login Successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Email not found. Please register first.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Unexpected error during login", ex);
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
        boolean ok = new Model.DBSearch().addUser(regUsername.trim(), regEmail.trim(), regPassword);
        if (ok) {
            JOptionPane.showMessageDialog(null, "You are successfully registered. Please login.", "Successful", JOptionPane.INFORMATION_MESSAGE);
            vrsView.switchTabs(vrsView.loginPanel, vrsView.logregLayeredPane);
        } else {
            JOptionPane.showMessageDialog(null, "Registration failed. An account with this email may already exist.", "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }
}
