package utils;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * UIUtils - Modern Flat Design System
 * Version 2.0 - Enhanced with gradients, shadows, and animations
 */
public class UIUtils {
    
    // ========== MODERN COLOR PALETTE ==========
    // Primary Colors
    public static final Color PRIMARY_COLOR = new Color(99, 102, 241);        // Indigo
    public static final Color PRIMARY_DARK = new Color(79, 70, 229);
    public static final Color PRIMARY_LIGHT = new Color(165, 180, 252);
    
    // Semantic Colors
    public static final Color SUCCESS_COLOR = new Color(34, 197, 94);         // Green
    public static final Color SUCCESS_DARK = new Color(22, 163, 74);
    public static final Color DANGER_COLOR = new Color(239, 68, 68);          // Red
    public static final Color DANGER_DARK = new Color(220, 38, 38);
    public static final Color WARNING_COLOR = new Color(251, 146, 60);        // Orange
    public static final Color WARNING_DARK = new Color(234, 88, 12);
    public static final Color INFO_COLOR = new Color(59, 130, 246);           // Blue
    public static final Color INFO_DARK = new Color(37, 99, 235);
    
    // Neutral Colors
    public static final Color GRAY_50 = new Color(249, 250, 251);
    public static final Color GRAY_100 = new Color(243, 244, 246);
    public static final Color GRAY_200 = new Color(229, 231, 235);
    public static final Color GRAY_300 = new Color(209, 213, 219);
    public static final Color GRAY_400 = new Color(156, 163, 175);
    public static final Color GRAY_500 = new Color(107, 114, 128);
    public static final Color GRAY_600 = new Color(75, 85, 99);
    public static final Color GRAY_700 = new Color(55, 65, 81);
    public static final Color GRAY_800 = new Color(31, 41, 55);
    public static final Color GRAY_900 = new Color(17, 24, 39);
    
    // Background Colors
    public static final Color BG_PRIMARY = Color.WHITE;
    public static final Color BG_SECONDARY = GRAY_50;
    public static final Color BG_CARD = Color.WHITE;
    
    // Text Colors
    public static final Color TEXT_PRIMARY = GRAY_900;
    public static final Color TEXT_SECONDARY = GRAY_600;
    public static final Color TEXT_MUTED = GRAY_400;
    public static final Color TEXT_WHITE = Color.WHITE;
    
    // Border Colors
    public static final Color BORDER_COLOR = GRAY_200;
    public static final Color BORDER_FOCUS = PRIMARY_COLOR;
    
    // ========== TYPOGRAPHY ==========
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBHEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_LARGE_NUMBER = new Font("Segoe UI", Font.BOLD, 32);
    
    // ========== ENHANCED BUTTON STYLES ==========
    
    /**
     * Primary Button - Gradient Blue
     */
    public static JButton createPrimaryButton(String text) {
        return createGradientButton(text, PRIMARY_COLOR, PRIMARY_DARK, TEXT_WHITE);
    }
    
    /**
     * Success Button - Gradient Green
     */
    public static JButton createSuccessButton(String text) {
        return createGradientButton(text, SUCCESS_COLOR, SUCCESS_DARK, TEXT_WHITE);
    }
    
    /**
     * Danger Button - Gradient Red
     */
    public static JButton createDangerButton(String text) {
        return createGradientButton(text, DANGER_COLOR, DANGER_DARK, TEXT_WHITE);
    }
    
    /**
     * Warning Button - Gradient Orange
     */
    public static JButton createWarningButton(String text) {
        return createGradientButton(text, WARNING_COLOR, WARNING_DARK, TEXT_WHITE);
    }
    
    /**
     * Info Button - Gradient Blue
     */
    public static JButton createInfoButton(String text) {
        return createGradientButton(text, INFO_COLOR, INFO_DARK, TEXT_WHITE);
    }
    
    /**
     * Secondary Button - Light Gray
     */
    public static JButton createSecondaryButton(String text) {
        return createGradientButton(text, GRAY_200, GRAY_300, TEXT_PRIMARY);
    }
    
    /**
     * Create Gradient Button with Shadow
     */
    private static JButton createGradientButton(String text, Color color1, Color color2, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Shadow
                if (isEnabled()) {
                    g2d.setColor(new Color(0, 0, 0, 30));
                    g2d.fillRoundRect(2, 2, width - 4, height - 4, 12, 12);
                }
                
                // Gradient background
                GradientPaint gradient;
                if (getModel().isPressed()) {
                    gradient = new GradientPaint(0, 0, color2, 0, height, color1);
                } else if (getModel().isRollover()) {
                    Color lighter1 = brighter(color1, 1.1f);
                    Color lighter2 = brighter(color2, 1.1f);
                    gradient = new GradientPaint(0, 0, lighter1, 0, height, lighter2);
                } else {
                    gradient = new GradientPaint(0, 0, color1, 0, height, color2);
                }
                
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, width, height, 10, 10);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setForeground(fgColor);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setOpaque(false);
        
        return button;
    }
    
    /**
     * Icon Button - Circular with gradient
     */
    public static JButton createIconButton(String icon, Color color1, Color color2) {
        JButton button = new JButton(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Gradient
                GradientPaint gradient = new GradientPaint(0, 0, color1, width, height, color2);
                g2d.setPaint(gradient);
                g2d.fillOval(2, 2, width - 4, height - 4);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setForeground(TEXT_WHITE);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(45, 45));
        button.setOpaque(false);
        
        return button;
    }
    
    // ========== PANEL STYLES ==========
    
    /**
     * Modern Card Panel with shadow
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 16, 16);
                
                // Card background
                g2d.setColor(BG_CARD);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }
    
    /**
     * Header Panel with gradient
     */
    public static JPanel createHeaderPanel(Color color1, Color color2) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, color1,
                    getWidth(), 0, color2
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }
    
    /**
     * Stats Card - for displaying numbers
     */
    public static JPanel createStatsCard(String title, String value, String icon, Color color) {
        JPanel panel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2d.setColor(new Color(0, 0, 0, 8));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 12, 12);
                
                // Background
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Accent bar
                g2d.setColor(color);
                g2d.fillRoundRect(0, 0, 5, getHeight(), 10, 10);
                
                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 15));
        
        // Icon
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        lblIcon.setForeground(color);
        panel.add(lblIcon, BorderLayout.WEST);
        
        // Text
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_SMALL);
        lblTitle.setForeground(TEXT_SECONDARY);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValue.setForeground(TEXT_PRIMARY);
        
        textPanel.add(lblTitle);
        textPanel.add(lblValue);
        panel.add(textPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ========== TEXT FIELD STYLES ==========
    
    /**
     * Modern TextField with focus effect
     */
    public static void styleTextField(JTextField textField) {
        textField.setFont(FONT_BODY);
        textField.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(8, BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(8, BORDER_FOCUS, 2),
                    BorderFactory.createEmptyBorder(9, 11, 9, 11)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(8, BORDER_COLOR),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
        });
    }
    
    /**
     * Style ComboBox
     */
    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(FONT_BODY);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(new RoundedBorder(8, BORDER_COLOR));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                if (isSelected) {
                    setBackground(PRIMARY_LIGHT);
                    setForeground(TEXT_PRIMARY);
                }
                return c;
            }
        });
    }
    
    // ========== TABLE STYLES ==========
    
    /**
     * Modern Table Design
     */
    public static void styleTable(JTable table) {
        // Header
        table.getTableHeader().setFont(FONT_SUBHEADING);
        table.getTableHeader().setBackground(GRAY_100);
        table.getTableHeader().setForeground(TEXT_PRIMARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR));
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        
        // Body
        table.setFont(FONT_BODY);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(TEXT_PRIMARY);
        
        // Alternating rows
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : GRAY_50);
                }
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                setHorizontalAlignment(column == 0 ? CENTER : LEFT);
                return c;
            }
        });
    }
    
    // ========== LABEL STYLES ==========
    
    public static JLabel createHeadingLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_HEADING);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    public static JLabel createIconLabel(String icon, String text, Color color) {
        JLabel label = new JLabel(icon + " " + text);
        label.setFont(FONT_BODY);
        label.setForeground(color);
        return label;
    }
    
    // ========== CUSTOM BORDERS ==========
    
    static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;
        private int thickness;
        
        RoundedBorder(int radius, Color color) {
            this(radius, color, 1);
        }
        
        RoundedBorder(int radius, Color color, int thickness) {
            this.radius = radius;
            this.color = color;
            this.thickness = thickness;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x + thickness/2, y + thickness/2, 
                           width - thickness, height - thickness, radius, radius);
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            int inset = radius/2 + thickness;
            return new Insets(inset, inset, inset, inset);
        }
    }
    
    // ========== UTILITIES ==========
    
    public static Color brighter(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() * factor));
        int g = Math.min(255, (int)(color.getGreen() * factor));
        int b = Math.min(255, (int)(color.getBlue() * factor));
        return new Color(r, g, b);
    }
    
    public static String formatMoney(long money) {
        return String.format("%,d VNĐ", money);
    }
    
    public static void showSuccessMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "✅ Thành công", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showErrorMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "❌ Lỗi", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showWarningMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "⚠️ Cảnh báo", 
            JOptionPane.WARNING_MESSAGE);
    }
}