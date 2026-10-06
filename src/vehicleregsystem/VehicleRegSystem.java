/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vehicleregsystem;

import Model.DBConnection;
import View.vrsView;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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

        // Apply Nimbus or system Look and Feel for modern appearance
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Default look and feel is fine
        }

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
