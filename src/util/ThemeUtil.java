package util;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * UI Theme Manager implementing the WordPress Dashboard Design System.
 * Deep Charcoal Sidebar, WordPress Blue Accents, Clean White Cards, and
 * Modern Typography and Popups.
 */
public class ThemeUtil {

    // WordPress Admin Palette
    public static final Color SIDEBAR_BG = new Color(0x1D, 0x23, 0x27);
    public static final Color SIDEBAR_HEADER_BG = new Color(0x13, 0x17, 0x1A);
    public static final Color SIDEBAR_HOVER_BG = new Color(0x2C, 0x33, 0x38);
    public static final Color SIDEBAR_ACTIVE_BG = new Color(0x22, 0x71, 0xB1);
    public static final Color SIDEBAR_TEXT = new Color(0xC3, 0xC4, 0xC7);
    public static final Color SIDEBAR_TEXT_ACTIVE = Color.WHITE;

    public static final Color CANVAS_BG = new Color(0xF0, 0xF2, 0xF5);
    public static final Color CARD_BG = Color.WHITE;
    public static final Color BORDER_LIGHT = new Color(0xDC, 0xDC, 0xDE);
    public static final Color BORDER_INPUT = new Color(0x8C, 0x8F, 0x94);

    public static final Color PRIMARY_BLUE = new Color(0x22, 0x71, 0xB1);
    public static final Color PRIMARY_HOVER = new Color(0x13, 0x5E, 0x96);
    public static final Color BADGE_RED = new Color(0xD6, 0x36, 0x38);
    public static final Color SUCCESS_GREEN = new Color(0x00, 0x8A, 0x20);

    public static final Color TEXT_DARK = new Color(0x1D, 0x23, 0x27);
    public static final Color TEXT_MUTED = new Color(0x64, 0x69, 0x70);

    // Modern Typography
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    /**
     * Initializes global Swing Look and Feel with FlatLaf and WordPress palette.
     */
    public static void initGlobalTheme() {
        try {
            FlatLightLaf.setup();

            UIManager.put("Component.arc", 6);
            UIManager.put("Button.arc", 6);
            UIManager.put("TextComponent.arc", 4);
            UIManager.put("ScrollBar.thumbArc", 6);
            UIManager.put("ScrollBar.width", 10);

            // Focus and Accent
            UIManager.put("Component.focusColor", PRIMARY_BLUE);
            UIManager.put("Component.focusedBorderColor", PRIMARY_BLUE);
            UIManager.put("Button.focusedBorderColor", PRIMARY_BLUE);

            // Dialogs / Popups (JOptionPane)
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("OptionPane.messageForeground", TEXT_DARK);
            UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Panel.background", CANVAS_BG);

            // Tables
            UIManager.put("Table.selectionBackground", PRIMARY_BLUE);
            UIManager.put("Table.selectionForeground", Color.WHITE);
            UIManager.put("TableHeader.background", new Color(0xF0, 0xF0, 0xF1));
            UIManager.put("TableHeader.foreground", TEXT_DARK);
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));

            System.out.println("[ThemeUtil] WordPress Dashboard theme initialized via FlatLaf.");
        } catch (Throwable t) {
            System.err.println("[ThemeUtil] Theme setup notice: " + t.getMessage());
        }
    }

    /**
     * Styles primary WordPress action buttons (e.g. Login, Submit, Next).
     */
    public static void stylePrimaryButton(JButton btn) {
        if (btn == null) return;
        btn.setBackground(PRIMARY_BLUE);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(
            new LineBorder(PRIMARY_BLUE, 1, true),
            new EmptyBorder(6, 14, 6, 14)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(PRIMARY_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(PRIMARY_BLUE);
            }
        });
    }

    /**
     * Styles secondary buttons (e.g. Back, Register, Clear).
     */
    public static void styleSecondaryButton(JButton btn) {
        if (btn == null) return;
        btn.setBackground(Color.WHITE);
        btn.setForeground(PRIMARY_BLUE);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(
            new LineBorder(PRIMARY_BLUE, 1, true),
            new EmptyBorder(6, 14, 6, 14)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0xF6, 0xF7, 0xF7));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE);
            }
        });
    }

    /**
     * Styles input fields with modern clean borders and padding.
     */
    public static void styleInput(JTextField tf) {
        if (tf == null) return;
        tf.setBackground(Color.WHITE);
        tf.setForeground(TEXT_DARK);
        tf.setFont(FONT_BODY);
        tf.setBorder(new CompoundBorder(
            new LineBorder(BORDER_INPUT, 1, true),
            new EmptyBorder(5, 8, 5, 8)
        ));
    }

    /**
     * Styles cards/containers to look like WordPress admin panels.
     */
    public static void styleCard(JPanel panel) {
        if (panel == null) return;
        panel.setBackground(CARD_BG);
        panel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_LIGHT, 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
    }

    /**
     * Styles sidebar items with WordPress hover and active state colors.
     */
    public static void setSidebarState(JPanel itemPanel, JLabel label, boolean active) {
        if (itemPanel == null || label == null) return;
        if (active) {
            itemPanel.setBackground(SIDEBAR_ACTIVE_BG);
            label.setForeground(SIDEBAR_TEXT_ACTIVE);
            label.setFont(FONT_BOLD);
        } else {
            itemPanel.setBackground(SIDEBAR_BG);
            label.setForeground(SIDEBAR_TEXT);
            label.setFont(FONT_BODY);
        }
    }

    /**
     * Adds hover effect to a sidebar navigation panel.
     */
    public static void addSidebarHover(JPanel itemPanel, JLabel label, java.util.function.BooleanSupplier isActiveSupplier) {
        if (itemPanel == null || label == null) return;
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isActiveSupplier.getAsBoolean()) {
                    itemPanel.setBackground(SIDEBAR_HOVER_BG);
                    label.setForeground(Color.WHITE);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!isActiveSupplier.getAsBoolean()) {
                    itemPanel.setBackground(SIDEBAR_BG);
                    label.setForeground(SIDEBAR_TEXT);
                }
            }
        });
    }

    /**
     * Formats a showMessageDialog popup with styled modern looks.
     */
    public static void showInfo(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
}

