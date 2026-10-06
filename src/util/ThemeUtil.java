package util;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
import javax.swing.border.TitledBorder;

/**
 * UI Theme Manager implementing the WordPress Dashboard Design System.
 * Deep Charcoal Sidebar, WordPress Blue Accents, Clean White Cards, and
 * Modern Segoe UI Typography.
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
    public static final Color BORDER_FOCUS = new Color(0x22, 0x71, 0xB1);

    public static final Color PRIMARY_BLUE = new Color(0x22, 0x71, 0xB1);
    public static final Color PRIMARY_HOVER = new Color(0x13, 0x5E, 0x96);
    public static final Color BADGE_RED = new Color(0xD6, 0x36, 0x38);
    public static final Color SUCCESS_GREEN = new Color(0x00, 0x8A, 0x20);
    public static final Color WARNING_ORANGE = new Color(0xD9, 0x77, 0x06);

    public static final Color TEXT_DARK = new Color(0x1D, 0x23, 0x27);
    public static final Color TEXT_BODY = new Color(0x2C, 0x33, 0x38);
    public static final Color TEXT_SECONDARY = new Color(0x50, 0x57, 0x5E);
    public static final Color TEXT_MUTED = new Color(0x64, 0x69, 0x70);

    // Modern Segoe UI Typography
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_HINT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BADGE = new Font("Segoe UI", Font.BOLD, 11);

    /**
     * Initializes global Swing Look and Feel with FlatLaf and WordPress palette.
     */
    public static void initGlobalTheme() {
        try {
            FlatLightLaf.setup();

            UIManager.put("defaultFont", FONT_BODY);
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
            UIManager.put("TableHeader.font", FONT_BOLD);

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
            new EmptyBorder(7, 16, 7, 16)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Remove old listeners that might interfere
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
            new EmptyBorder(7, 16, 7, 16)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0xF0, 0xF6, 0xFC));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE);
            }
        });
    }

    /**
     * Styles danger/cancel buttons (e.g. Delete, Exit).
     */
    public static void styleDangerButton(JButton btn) {
        if (btn == null) return;
        btn.setBackground(BADGE_RED);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(
            new LineBorder(BADGE_RED, 1, true),
            new EmptyBorder(7, 16, 7, 16)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0xB3, 0x2D, 0x2E));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(BADGE_RED);
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
        tf.setCaretColor(PRIMARY_BLUE);
        tf.setBorder(new CompoundBorder(
            new LineBorder(BORDER_INPUT, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
    }

    /**
     * Styles password fields with modern clean borders and padding.
     */
    public static void styleInput(JPasswordField pf) {
        if (pf == null) return;
        pf.setBackground(Color.WHITE);
        pf.setForeground(TEXT_DARK);
        pf.setFont(FONT_BODY);
        pf.setCaretColor(PRIMARY_BLUE);
        pf.setBorder(new CompoundBorder(
            new LineBorder(BORDER_INPUT, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
    }

    /**
     * Styles combo boxes.
     */
    public static void styleComboBox(JComboBox<?> cb) {
        if (cb == null) return;
        cb.setBackground(Color.WHITE);
        cb.setForeground(TEXT_DARK);
        cb.setFont(FONT_BODY);
    }

    /**
     * Styles cards/containers to look like WordPress admin panels.
     */
    public static void styleCard(JPanel panel) {
        if (panel == null) return;
        panel.setBackground(CARD_BG);
        panel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_LIGHT, 1, true),
            new EmptyBorder(16, 20, 16, 20)
        ));
    }

    /**
     * Creates a standard styled page header panel.
     */
    public static JPanel createPageHeader(String title, String subtitle) {
        JPanel header = new JPanel(new java.awt.BorderLayout(5, 5));
        header.setBackground(CARD_BG);
        header.setBorder(new CompoundBorder(
            new LineBorder(BORDER_LIGHT, 1, true),
            new EmptyBorder(14, 20, 14, 20)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(FONT_TITLE);
        titleLbl.setForeground(TEXT_DARK);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(FONT_BODY);
        subLbl.setForeground(TEXT_SECONDARY);

        header.add(titleLbl, java.awt.BorderLayout.NORTH);
        header.add(subLbl, java.awt.BorderLayout.SOUTH);
        return header;
    }

    /**
     * Creates a styled form section header label.
     */
    public static JLabel createSectionHeader(String title) {
        JLabel lbl = new JLabel(title);
        lbl.setFont(FONT_HEADER);
        lbl.setForeground(TEXT_DARK);
        lbl.setBorder(new EmptyBorder(8, 0, 4, 0));
        return lbl;
    }

    /**
     * Creates a styled form field label.
     */
    public static JLabel createFieldLabel(String labelText) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_BODY);
        return lbl;
    }

    /**
     * Creates a styled badge label.
     */
    public static JLabel createBadge(String text, Color bg, Color fg) {
        JLabel badge = new JLabel(" " + text + " ");
        badge.setOpaque(true);
        badge.setBackground(bg);
        badge.setForeground(fg);
        badge.setFont(FONT_BADGE);
        badge.setBorder(new CompoundBorder(
            new LineBorder(bg.darker(), 1, true),
            new EmptyBorder(2, 6, 2, 6)
        ));
        return badge;
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
     * Recursively traverses a component hierarchy and sanitizes all components:
     * - Fixes any white text on light backgrounds (changes to TEXT_DARK).
     * - Enforces Segoe UI typography.
     * - Ensures inputs, radio buttons, checkboxes have clean styling.
     */
    public static void sanitizeHierarchy(Component comp) {
        if (comp == null) return;

        // Modernize font if it is not Segoe UI
        Font currentFont = comp.getFont();
        if (currentFont != null && !"Segoe UI".equalsIgnoreCase(currentFont.getName())) {
            comp.setFont(new Font("Segoe UI", currentFont.getStyle(), currentFont.getSize()));
        }

        // Fix white text on light components
        if (comp instanceof JLabel) {
            JLabel lbl = (JLabel) comp;
            Color fg = lbl.getForeground();
            if (isWhiteOrNearWhite(fg)) {
                // If label is not inside a dark sidebar or dark header, change to TEXT_DARK
                Container parent = lbl.getParent();
                if (parent != null && !isDark(parent.getBackground())) {
                    lbl.setForeground(TEXT_DARK);
                }
            }
        } else if (comp instanceof JCheckBox) {
            JCheckBox cb = (JCheckBox) comp;
            cb.setBackground(Color.WHITE);
            cb.setForeground(TEXT_DARK);
            cb.setFont(FONT_BOLD);
        } else if (comp instanceof JRadioButton) {
            JRadioButton rb = (JRadioButton) comp;
            rb.setBackground(Color.WHITE);
            rb.setForeground(TEXT_DARK);
            rb.setFont(FONT_BODY);
        } else if (comp instanceof JTextField && !(comp instanceof JPasswordField)) {
            styleInput((JTextField) comp);
        } else if (comp instanceof JPasswordField) {
            styleInput((JPasswordField) comp);
        }

        // Recurse into children
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                sanitizeHierarchy(child);
            }
        }
    }

    private static boolean isWhiteOrNearWhite(Color c) {
        if (c == null) return false;
        return c.getRed() > 220 && c.getGreen() > 220 && c.getBlue() > 220;
    }

    private static boolean isDark(Color c) {
        if (c == null) return false;
        double luma = 0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue();
        return luma < 100;
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
