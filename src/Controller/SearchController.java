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

public class SearchController {
    
    public static void checkStatus(String applicationNum, String vehicleNum) {
        // Call a method in DBSearch to search for the record
        String status = new Model.DBSearch().checkStatus(applicationNum, vehicleNum);
        
        // Result Action
        if (status == null) {
        JOptionPane.showMessageDialog(null, "Enter a valid Application Number", "Error", JOptionPane.ERROR_MESSAGE);
    }else{
        switch (status) {
            case "1":
                vrsView.jCheckBox1.setSelected(true);                 // Check mark jCheckbox1
                break;
                
            case "2":
                vrsView.jCheckBox1.setSelected(true);
                vrsView.jCheckBox2.setSelected(true);
                break;
                
            case "3":
                vrsView.jCheckBox1.setSelected(true);
                vrsView.jCheckBox2.setSelected(true);
                vrsView.jCheckBox3.setSelected(true);
                break;
                
            case "4":
                vrsView.jCheckBox1.setSelected(true);
                vrsView.jCheckBox2.setSelected(true);
                vrsView.jCheckBox3.setSelected(true);
                vrsView.jCheckBox4.setSelected(true);
                break;
                
            case "5":
                vrsView.jCheckBox1.setSelected(true);
                vrsView.jCheckBox2.setSelected(true);
                vrsView.jCheckBox3.setSelected(true);
                vrsView.jCheckBox4.setSelected(true);
                vrsView.jCheckBox5.setSelected(true);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Unknown status: Please contact Customer Support " + status, "Record Status", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
        }
    }

}
