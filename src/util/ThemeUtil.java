package util;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.KeyboardFocusManager;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.TableModel;

/**
 * UI Theme Manager implementing the WordPress Dashboard Design System.
 * Deep Charcoal Sidebar, WordPress Blue Accents, Clean White Cards, and
 * Modern Segoe UI Typography.
 */
public final class ThemeUtil {

    private static final Logger LOG = Logger.getLogger(ThemeUtil.class.getName());

    private ThemeUtil() {
    }

    // WordPress Admin Palette
    public static final Color SIDEBAR_BG = new Color(0x1D, 0x23, 0x27);
    public static final Color SIDEBAR_HEADER_BG = new Color(0x13, 0x17, 0x1A);
    public static final Color SIDEBAR_HOVER_BG = new Color(0x2C, 0x33, 0x38);
    public static final Color SIDEBAR_ACTIVE_BG = new Color(0x22, 0x71, 0xB1);
    public static final Color SIDEBAR_TEXT = new Color(0xC3, 0xC4, 0xC7);
    public static final Color SIDEBAR_TEXT_ACTIVE = Color.WHITE;

    public static final Color CANVAS_BG = new Color(0xF0, 0xF2, 0xF5);
    public static final Color CARD_BG = Color.WHITE;
    public static final Color SUBTLE_BG = new Color(0xF8, 0xFA, 0xFC);
    public static final Color BORDER_LIGHT = new Color(0xDC, 0xDC, 0xDE);
    public static final Color BORDER_INPUT = new Color(0x8C, 0x8F, 0x94);
    public static final Color BORDER_FOCUS = new Color(0x22, 0x71, 0xB1);

    public static final Color PRIMARY_BLUE = new Color(0x22, 0x71, 0xB1);
    public static final Color PRIMARY_HOVER = new Color(0x13, 0x5E, 0x96);
    public static final Color PRIMARY_PRESSED = new Color(0x0A, 0x4B, 0x78);
    public static final Color PRIMARY_TINT = new Color(0xF0, 0xF6, 0xFC);
    public static final Color BADGE_RED = new Color(0xD6, 0x36, 0x38);
    public static final Color DANGER_HOVER = new Color(0xB3, 0x2D, 0x2E);
    public static final Color SUCCESS_GREEN = new Color(0x00, 0x8A, 0x20);
    public static final Color SUCCESS_TINT = new Color(0xED, 0xFA, 0xEF);

    public static final Color TEXT_DARK = new Color(0x1D, 0x23, 0x27);
    public static final Color TEXT_BODY = new Color(0x2C, 0x33, 0x38);
    public static final Color TEXT_SECONDARY = new Color(0x50, 0x57, 0x5E);
    public static final Color TEXT_MUTED = new Color(0x64, 0x69, 0x70);
    public static final Color DISABLED_BG = new Color(0xDC, 0xDC, 0xDE);
    public static final Color DISABLED_TEXT = new Color(0x8C, 0x8F, 0x94);

    // Modern Segoe UI Typography
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_HINT = new Font("Segoe UI", Font.PLAIN, 12);

    // Client property keys used to keep styling idempotent
    private static final String KEY_STYLED = "vrs.button.styled";
    private static final String KEY_BG = "vrs.button.bg";
    private static final String KEY_HOVER = "vrs.button.hover";
    private static final String KEY_PRESSED = "vrs.button.pressed";
    private static final String KEY_FG = "vrs.button.fg";
    private static final String KEY_EDGE = "vrs.button.edge";
    private static final String KEY_INPUT_STYLED = "vrs.input.styled";

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

            UIManager.put("Component.focusColor", PRIMARY_BLUE);
            UIManager.put("Component.focusedBorderColor", PRIMARY_BLUE);
            UIManager.put("Button.focusedBorderColor", PRIMARY_BLUE);

            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("OptionPane.messageForeground", TEXT_DARK);
            UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("Panel.background", CANVAS_BG);

            UIManager.put("Table.selectionBackground", PRIMARY_BLUE);
            UIManager.put("Table.selectionForeground", Color.WHITE);
            UIManager.put("Table.alternateRowColor", SUBTLE_BG);
            UIManager.put("Table.showHorizontalLines", Boolean.TRUE);
            UIManager.put("Table.gridColor", new Color(0xEB, 0xEC, 0xEE));
            UIManager.put("TableHeader.background", new Color(0xF0, 0xF0, 0xF1));
            UIManager.put("TableHeader.foreground", TEXT_DARK);
            UIManager.put("TableHeader.font", FONT_BOLD);
        } catch (RuntimeException | LinkageError t) {
            LOG.log(Level.WARNING, "FlatLaf theme could not be initialized; using default look and feel.", t);
        }
    }

    // ------------------------------------------------------------------
    // Buttons
    // ------------------------------------------------------------------

    /** Primary action buttons (e.g. Sign In, Submit, Next). */
    public static void stylePrimaryButton(JButton btn) {
        styleButton(btn, PRIMARY_BLUE, PRIMARY_HOVER, PRIMARY_PRESSED, Color.WHITE, PRIMARY_BLUE);
    }

    /** Secondary buttons (e.g. Back, Register, Clear). */
    public static void styleSecondaryButton(JButton btn) {
        styleButton(btn, Color.WHITE, PRIMARY_TINT, new Color(0xDB, 0xE9, 0xF7), PRIMARY_BLUE, PRIMARY_BLUE);
    }

    /** Destructive buttons (e.g. Delete). */
    public static void styleDangerButton(JButton btn) {
        styleButton(btn, BADGE_RED, DANGER_HOVER, new Color(0x8A, 0x22, 0x23), Color.WHITE, BADGE_RED);
    }

    /**
     * Applies colors and installs exactly one set of listeners per button.
     * Listeners read colors from client properties, so restyling a button
     * (primary to secondary, etc.) never stacks conflicting hover handlers.
     */
    private static void styleButton(JButton btn, Color bg, Color hover, Color pressed, Color fg, Color edge) {
        if (btn == null) {
            return;
        }
        btn.putClientProperty(KEY_BG, bg);
        btn.putClientProperty(KEY_HOVER, hover);
        btn.putClientProperty(KEY_PRESSED, pressed);
        btn.putClientProperty(KEY_FG, fg);
        btn.putClientProperty(KEY_EDGE, edge);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (btn.getClientProperty(KEY_STYLED) == null) {
            btn.putClientProperty(KEY_STYLED, Boolean.TRUE);
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (btn.isEnabled()) {
                        btn.setBackground((Color) btn.getClientProperty(KEY_HOVER));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    refreshButton(btn);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (btn.isEnabled()) {
                        btn.setBackground((Color) btn.getClientProperty(KEY_PRESSED));
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (btn.isEnabled()) {
                        btn.setBackground(btn.getModel().isRollover()
                                ? (Color) btn.getClientProperty(KEY_HOVER)
                                : (Color) btn.getClientProperty(KEY_BG));
                    }
                }
            });
            btn.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    refreshButton(btn);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    refreshButton(btn);
                }
            });
            btn.addPropertyChangeListener("enabled", e -> refreshButton(btn));
        }
        refreshButton(btn);
    }

    /** Repaints a styled button for its current enabled/focus state. */
    private static void refreshButton(JButton btn) {
        Color edge = (Color) btn.getClientProperty(KEY_EDGE);
        if (!btn.isEnabled()) {
            btn.setBackground(DISABLED_BG);
            btn.setForeground(DISABLED_TEXT);
            btn.setBorder(new CompoundBorder(new LineBorder(DISABLED_BG, 2, true), new EmptyBorder(6, 15, 6, 15)));
            btn.setCursor(Cursor.getDefaultCursor());
            return;
        }
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBackground((Color) btn.getClientProperty(KEY_BG));
        btn.setForeground((Color) btn.getClientProperty(KEY_FG));
        // Keyboard focus ring: thicker, darker outline. Same outer size in both states to avoid layout jumps.
        Color ring = btn.isFocusOwner() ? TEXT_DARK : edge;
        btn.setBorder(new CompoundBorder(new LineBorder(ring, 2, true), new EmptyBorder(6, 15, 6, 15)));
    }

    /**
     * Disables a button for the duration of a synchronous action to block
     * double submits. Re-enabling is queued after any clicks that arrived
     * while the action was running, so those clicks hit a disabled button.
     */
    public static void runWithSubmitGuard(JButton btn, Runnable action) {
        if (btn != null && !btn.isEnabled()) {
            return;
        }
        if (btn != null) {
            btn.setEnabled(false);
        }
        try {
            action.run();
        } catch (RuntimeException ex) {
            LOG.log(Level.SEVERE, "Unexpected error while processing action", ex);
            showError(btn, "Unexpected Error", "The operation could not be completed:\n" + ex.getMessage());
        } finally {
            if (btn != null) {
                SwingUtilities.invokeLater(() -> btn.setEnabled(true));
            }
        }
    }

    // ------------------------------------------------------------------
    // Inputs
    // ------------------------------------------------------------------

    /** Text and password fields: padded border with a blue focus ring. */
    public static void styleInput(JTextField tf) {
        if (tf == null) {
            return;
        }
        tf.setBackground(Color.WHITE);
        tf.setForeground(TEXT_DARK);
        tf.setFont(FONT_BODY);
        tf.setCaretColor(PRIMARY_BLUE);
        applyInputBorder(tf, tf.isFocusOwner());
        if (tf.getClientProperty(KEY_INPUT_STYLED) == null) {
            tf.putClientProperty(KEY_INPUT_STYLED, Boolean.TRUE);
            tf.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    applyInputBorder(tf, true);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    applyInputBorder(tf, false);
                }
            });
        }
    }

    private static void applyInputBorder(JTextField tf, boolean focused) {
        // 2px outer border in both states keeps text from shifting on focus.
        tf.setBorder(new CompoundBorder(
                new LineBorder(focused ? BORDER_FOCUS : BORDER_INPUT, focused ? 2 : 1, true),
                new EmptyBorder(focused ? 5 : 6, focused ? 9 : 10, focused ? 5 : 6, focused ? 9 : 10)));
    }

    public static void styleComboBox(JComboBox<?> cb) {
        if (cb == null) {
            return;
        }
        cb.setBackground(Color.WHITE);
        cb.setForeground(TEXT_DARK);
        cb.setFont(FONT_BODY);
    }

    // ------------------------------------------------------------------
    // Layout building blocks
    // ------------------------------------------------------------------

    public static void styleCard(JPanel panel) {
        if (panel == null) {
            return;
        }
        panel.setBackground(CARD_BG);
        panel.setBorder(new CompoundBorder(
                new LineBorder(BORDER_LIGHT, 1, true),
                new EmptyBorder(16, 20, 16, 20)));
    }

    public static JPanel createPageHeader(String title, String subtitle) {
        JPanel header = new JPanel(new BorderLayout(5, 5));
        header.setBackground(CARD_BG);
        header.setBorder(new CompoundBorder(
                new LineBorder(BORDER_LIGHT, 1, true),
                new EmptyBorder(14, 20, 14, 20)));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(FONT_TITLE);
        titleLbl.setForeground(TEXT_DARK);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(FONT_BODY);
        subLbl.setForeground(TEXT_SECONDARY);

        header.add(titleLbl, BorderLayout.NORTH);
        header.add(subLbl, BorderLayout.SOUTH);
        return header;
    }

    public static JLabel createSectionHeader(String title) {
        JLabel lbl = new JLabel(title);
        lbl.setFont(FONT_HEADER);
        lbl.setForeground(TEXT_DARK);
        lbl.setBorder(new EmptyBorder(8, 0, 4, 0));
        return lbl;
    }

    public static JLabel createFieldLabel(String labelText) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_BODY);
        return lbl;
    }

    /**
     * Wraps content in a vertical scroll pane whose content always matches the
     * viewport width. Prevents wide labels or wrapped text from forcing a
     * horizontal scrollbar, while still scrolling vertically on small screens.
     */
    public static JScrollPane createVerticalScroll(JComponent content, Color background) {
        WidthTrackingPanel wrapper = new WidthTrackingPanel();
        wrapper.setBackground(background);
        wrapper.add(content, BorderLayout.CENTER);
        JScrollPane scroll = new JScrollPane(wrapper,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        scroll.getViewport().setBackground(background);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private static final class WidthTrackingPanel extends JPanel implements Scrollable {
        WidthTrackingPanel() {
            super(new BorderLayout());
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return orientation == SwingConstants.VERTICAL ? visibleRect.height : visibleRect.width;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            // Fill the viewport when content is short; scroll when it is taller.
            Container parent = getParent();
            return parent != null && getPreferredSize().height < parent.getHeight();
        }
    }

    /**
     * Creates a read-only table that renders a centered message whenever it has
     * no rows, so empty dashboards never look broken.
     */
    public static JTable createTable(TableModel model, String emptyMessage) {
        JTable table = new JTable(model) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getRowCount() == 0 && emptyMessage != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    try {
                        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                        g2.setFont(FONT_BODY);
                        g2.setColor(TEXT_MUTED);
                        FontMetrics fm = g2.getFontMetrics();
                        Rectangle view = getVisibleRect();
                        int x = view.x + Math.max(8, (view.width - fm.stringWidth(emptyMessage)) / 2);
                        int y = view.y + Math.max(fm.getAscent() + 8, view.height / 2);
                        g2.drawString(emptyMessage, x, y);
                    } finally {
                        g2.dispose();
                    }
                }
            }
        };
        table.setFillsViewportHeight(true);
        table.setRowHeight(28);
        table.setFont(FONT_BODY);
        table.getTableHeader().setFont(FONT_BOLD);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        return table;
    }

    // ------------------------------------------------------------------
    // Sidebar
    // ------------------------------------------------------------------

    public static void setSidebarState(JPanel itemPanel, JLabel label, boolean active) {
        if (itemPanel == null || label == null) {
            return;
        }
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

    public static void addSidebarHover(JPanel itemPanel, JLabel label, java.util.function.BooleanSupplier isActiveSupplier) {
        if (itemPanel == null || label == null) {
            return;
        }
        itemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        MouseAdapter hover = new MouseAdapter() {
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
        };
        // The label covers most of the panel, so it must forward hover too.
        itemPanel.addMouseListener(hover);
        label.addMouseListener(hover);
    }

    // ------------------------------------------------------------------
    // Hierarchy sanitizer
    // ------------------------------------------------------------------

    /**
     * Recursively enforces Segoe UI typography, removes white-on-light text,
     * and applies consistent input styling across a component tree.
     */
    public static void sanitizeHierarchy(Component comp) {
        if (comp == null) {
            return;
        }

        Font currentFont = comp.getFont();
        if (currentFont != null && !"Segoe UI".equalsIgnoreCase(currentFont.getName())) {
            comp.setFont(new Font("Segoe UI", currentFont.getStyle(), currentFont.getSize()));
        }

        if (comp instanceof JLabel) {
            JLabel lbl = (JLabel) comp;
            Container parent = lbl.getParent();
            if (isNearWhite(lbl.getForeground()) && parent != null && !isDark(parent.getBackground())) {
                lbl.setForeground(TEXT_DARK);
            }
        } else if (comp instanceof JCheckBox || comp instanceof JRadioButton) {
            // Transparent so they blend with whichever card or row they sit on.
            ((JComponent) comp).setOpaque(false);
            comp.setForeground(TEXT_DARK);
        } else if (comp instanceof JTextField && !isInsideCompositeEditor(comp)) {
            styleInput((JTextField) comp);
        }

        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                sanitizeHierarchy(child);
            }
        }
    }

    /** Text fields owned by spinners, combo boxes or JCalendar keep their own look. */
    private static boolean isInsideCompositeEditor(Component comp) {
        for (Container p = comp.getParent(); p != null; p = p.getParent()) {
            if (p instanceof JSpinner || p instanceof JComboBox || p.getClass().getName().startsWith("com.toedter.")) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNearWhite(Color c) {
        return c != null && c.getRed() > 220 && c.getGreen() > 220 && c.getBlue() > 220;
    }

    private static boolean isDark(Color c) {
        if (c == null) {
            return false;
        }
        double luma = 0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue();
        return luma < 100;
    }

    // ------------------------------------------------------------------
    // Dialogs
    // ------------------------------------------------------------------

    /** Dialogs are owned by the active window so they center on the app, not the screen. */
    private static Component ownerOf(Component parent) {
        if (parent != null) {
            return parent;
        }
        Window active = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        return active;
    }

    public static void showInfo(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(ownerOf(parent), message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarning(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(ownerOf(parent), message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void showError(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(ownerOf(parent), message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirm(Component parent, String title, String message) {
        return JOptionPane.showConfirmDialog(ownerOf(parent), message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }
}
