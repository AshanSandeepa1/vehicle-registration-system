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

    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("      Vehicle Registration System (VRS) Starting         ");
        System.out.println("=========================================================");

        // Warm up and verify Database Connection (Oracle with offline fallback)
        try {
            DBConnection.getConnection();
            System.out.println("[VRS] Active Database: " + DBConnection.getDatabaseType());
        } catch (Exception e) {
            System.err.println("[VRS] Database warning: " + e.getMessage());
        }

        // Apply WordPress Dashboard Theme globally via FlatLaf
        ThemeUtil.initGlobalTheme();

        // Launch the Main Application Window
        SwingUtilities.invokeLater(() -> {
            try {
                vrsView app = new vrsView();
                app.setLocationRelativeTo(null);
                app.setVisible(true);
                System.out.println("[VRS] Vehicle Registration System GUI launched successfully.");
            } catch (Exception ex) {
                System.err.println("[VRS] Error launching GUI: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
}
