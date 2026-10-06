package Controller;

import View.vrsView;
import Model.DBSearch;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class SearchController {

    private static final Logger LOG = Logger.getLogger(SearchController.class.getName());

    public static void checkStatus(String applicationNum, String vehicleNum) {
        if (applicationNum == null || applicationNum.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter an Application Number.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Reset check boxes first so previous search results don't linger
        vrsView.jCheckBox1.setSelected(false);
        vrsView.jCheckBox2.setSelected(false);
        vrsView.jCheckBox3.setSelected(false);
        vrsView.jCheckBox4.setSelected(false);
        vrsView.jCheckBox5.setSelected(false);

        // Query status from DBSearch
        String status = new DBSearch().checkStatus(applicationNum.trim(), vehicleNum != null ? vehicleNum.trim() : "");
        LOG.fine(() -> "Check status query for " + applicationNum + " returned: " + status);

        // Result Action
        if (status == null) {
            JOptionPane.showMessageDialog(null, "No record found for Application Number: " + applicationNum.trim()
                + "\nPlease enter a valid Application Number (e.g., 1234AA, 1255AB, 1000AA, 0001AA, 0525YZ, 1687ZW).",
                "Record Not Found", JOptionPane.ERROR_MESSAGE);
        } else {
            switch (status) {
                case "1":
                    vrsView.jCheckBox1.setSelected(true);
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
                    JOptionPane.showMessageDialog(null, "Unknown status code (" + status + "). Please contact Customer Support.", "Record Status", JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        }
    }
}
