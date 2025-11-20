package utils;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * UIUtils - Class chứa các tiện ích UI hiện đại
 * Bao gồm: Colors, Fonts, Borders, Button Styles
 */
public class UIUtils {
    
    // ========== COLOR PALETTE (Flat Design) ==========
    public static final Color PRIMARY_COLOR = new Color(52, 152, 219);      // Xanh dương
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113);      // Xanh lá
    public static final Color DANGER_COLOR = new Color(231, 76, 60);        // Đỏ
    public static final Color WARNING_COLOR = new Color(241, 196, 15);      // Vàng
    public static final Color INFO_COLOR = new Color(52, 73, 94);           // Xám đậm
    public static final Color LIGHT_COLOR = new Color(236, 240, 241);       // Xám nhạt
    public static final Color DARK_COLOR = new Color(44, 62, 80);           // Xám đen
    
    // Accent Colors
    public static final Color PURPLE_COLOR = new Color(155, 89, 182);
    public static final Color ORANGE_COLOR = new Color(230, 126, 34);
    public static final Color TEAL_COLOR = new Color(26, 188, 156);
    
    // Background Colors
    public static final Color BG_PRIMARY = new Color(255, 255, 255);        // Trắng
    public static final Color BG_SECONDARY = new Color(248, 249, 250);      // Xám rất nhạt
    public static final Color BG_CARD = new Color(255, 255, 255);
    
    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    public static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    public static final Color TEXT_WHITE = new Color(255, 255, 255);
    
    // Border Colors
    public static final Color BORDER_COLOR = new Color(222, 226, 230);
    public static final Color BORDER_FOCUS = PRIMARY_COLOR;
    
    // ========== FONTS ==========
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBHEADING = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_LARGE_NUMBER = new Font("Segoe UI", Font.BOLD, 28);
    
    // ========== BORDERS ==========
    public static final Border BORDER_CARD = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_COLOR, 1),
        BorderFactory.createEmptyBorder(15, 15, 15, 15)
    );
    
    public static final Border BORDER_ROUNDED = BorderFactory.createCompoundBorder(
        new RoundedBorder(8, BORDER_COLOR),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    );
    
    public static final Border BORDER_PANEL = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_COLOR, 1),
        BorderFactory.createEmptyBorder(20, 20, 20, 20)
    );
    
    // ========== BUTTON STYLES ==========
    
    /**
     * Tạo button với style primary (xanh dương)
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, PRIMARY_COLOR, TEXT_WHITE);
        return button;
    }
    
    /**
     * Tạo button với style success (xanh lá)
     */
    public static JButton createSuccessButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, SUCCESS_COLOR, TEXT_WHITE);
        return button;
    }
    
    /**
     * Tạo button với style danger (đỏ)
     */
    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, DANGER_COLOR, TEXT_WHITE);
        return button;
    }
    
    /**
     * Tạo button với style warning (vàng)
     */
    public static JButton createWarningButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, WARNING_COLOR, TEXT_PRIMARY);
        return button;
    }
    
    /**
     * Tạo button với style secondary (xám)
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, LIGHT_COLOR, TEXT_PRIMARY);
        return button;
    }
    
    /**
     * Style chung cho button
     */
    private static void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Hover effect
        Color originalBg = bgColor;
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(originalBg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalBg);
            }
        });
    }
    
    /**
     * Tạo icon button (nhỏ gọn với icon)
     */
    public static JButton createIconButton(String icon, Color color) {
        JButton button = new JButton(icon);
        button.setBackground(color);
        button.setForeground(TEXT_WHITE);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(40, 40));
        
        Color originalBg = color;
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(originalBg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalBg);
            }
        });
        
        return button;
    }
    
    // ========== PANEL STYLES ==========
    
    /**
     * Tạo panel với card style (có shadow)
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_CARD);
        panel.setBorder(BORDER_CARD);
        return panel;
    }
    
    /**
     * Tạo panel với title
     */
    public static JPanel createTitledPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            title,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            FONT_SUBHEADING,
            TEXT_PRIMARY
        ));
        return panel;
    }
    
    // ========== TEXT FIELD STYLES ==========
    
    /**
     * Style text field hiện đại
     */
    public static void styleTextField(JTextField textField) {
        textField.setFont(FONT_BODY);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        // Focus effect
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_FOCUS, 2),
                    BorderFactory.createEmptyBorder(7, 9, 7, 9)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
            }
        });
    }
    
    /**
     * Style combo box
     */
    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(FONT_BODY);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
    }
    
    // ========== TABLE STYLES ==========
    
    /**
     * Style table hiện đại
     */
    public static void styleTable(JTable table) {
        // Header
        table.getTableHeader().setFont(FONT_SUBHEADING);
        table.getTableHeader().setBackground(BG_SECONDARY);
        table.getTableHeader().setForeground(TEXT_PRIMARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Body
        table.setFont(FONT_BODY);
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(230, 240, 255));
        table.setSelectionForeground(TEXT_PRIMARY);
        
        // Alternating row colors
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : BG_SECONDARY);
                }
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        });
    }
    
    // ========== LABEL STYLES ==========
    
    /**
     * Tạo label với style heading
     */
    public static JLabel createHeadingLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_HEADING);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    /**
     * Tạo label với icon và màu
     */
    public static JLabel createIconLabel(String icon, String text, Color color) {
        JLabel label = new JLabel(icon + " " + text);
        label.setFont(FONT_BODY);
        label.setForeground(color);
        return label;
    }
    
    /**
     * Tạo label hiển thị số tiền lớn
     */
    public static JLabel createMoneyLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_LARGE_NUMBER);
        label.setForeground(color);
        return label;
    }
    
    // ========== CUSTOM BORDER CLASS ==========
    
    /**
     * Border bo tròn góc
     */
    static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;
        
        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2, radius/2, radius/2, radius/2);
        }
    }
    
    // ========== UTILITY METHODS ==========
    
    /**
     * Format số tiền VNĐ
     */
    public static String formatMoney(long money) {
        return String.format("%,d VNĐ", money);
    }
    
    /**
     * Tạo separator ngang
     */
    public static JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER_COLOR);
        return separator;
    }
    
    /**
     * Hiển thị thông báo thành công
     */
    public static void showSuccessMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "✅ Thành công", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Hiển thị thông báo lỗi
     */
    public static void showErrorMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "❌ Lỗi", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Hiển thị thông báo cảnh báo
     */
    public static void showWarningMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "⚠️ Cảnh báo", 
            JOptionPane.WARNING_MESSAGE);
    }
}