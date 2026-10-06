package View;

/**
 * Transferee details view.
 * Corrected spelling alias for TranfereeDetails.
 */
public class TransfereeDetails extends TranfereeDetails {

    public TransfereeDetails() {
        super();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new TransfereeDetails().setVisible(true);
        });
    }
}

