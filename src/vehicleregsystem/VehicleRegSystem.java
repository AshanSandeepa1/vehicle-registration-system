/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vehicleregsystem;

import Model.DBConnection;
import View.vrsView;
import javax.swing.SwingUtilities;
import util.ThemeUtil;

/**
 * Main entry point for the Vehicle Registration System (VRS).
 * 
 * @author Ashan & Refactored for Oracle Migration
 */
public class VehicleRegSystem {

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(VehicleRegSystem.class.getName());

    public static void main(String[] args) {
        LOG.info("Vehicle Registration System (VRS) starting up...");

        // Warm up and verify Database Connection (Oracle with offline fallback)
        try {
            DBConnection.getConnection();
            LOG.info(() -> "Active Database: " + DBConnection.getDatabaseType());
        } catch (Exception e) {
            LOG.log(java.util.logging.Level.WARNING, "Database initialization warning: " + e.getMessage(), e);
        }

        // Apply WordPress Dashboard Theme globally via FlatLaf
        ThemeUtil.initGlobalTheme();

        // Launch the Main Application Window
        SwingUtilities.invokeLater(() -> {
            try {
                vrsView app = new vrsView();
                app.setLocationRelativeTo(null);
                app.setVisible(true);
                LOG.info("Vehicle Registration System GUI launched successfully.");
            } catch (Exception ex) {
                LOG.log(java.util.logging.Level.SEVERE, "Error launching GUI: " + ex.getMessage(), ex);
            }
        });
    }
}
