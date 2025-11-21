package gui;

import utils.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class MainMenuForm extends JFrame {
    
    public MainMenuForm() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Rạp Phim");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // ========== GRADIENT BACKGROUND ==========
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Multi-stop gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(249, 250, 251),
                    getWidth(), getHeight(), new Color(237, 242, 247)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Decorative circles
                g2d.setColor(new Color(99, 102, 241, 20));
                g2d.fillOval(-100, -100, 400, 400);
                g2d.fillOval(getWidth() - 300, getHeight() - 300, 400, 400);
            }
        };
        backgroundPanel.setOpaque(false);
        setContentPane(backgroundPanel);
        
        // ========== MODERN HEADER ==========
        JPanel pnlHeader = UIUtils.createHeaderPanel(
            new Color(99, 102, 241),
            new Color(79, 70, 229)
        );
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        pnlHeader.setLayout(new BorderLayout());
        
        // Logo & Title
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlTitle.setOpaque(false);
        
        JLabel lblIcon = new JLabel("🎬");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        pnlTitle.add(lblIcon);
        
        JPanel pnlText = new JPanel(new GridLayout(2, 1, 0, 5));
        pnlText.setOpaque(false);
        
        JLabel lblTitle = new JLabel("CINEMA MANAGER");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        
        JLabel lblSubtitle = new JLabel("Hệ Thống Quản Lý Rạp Chiếu Phim Chuyên Nghiệp");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSubtitle.setForeground(new Color(224, 231, 255));
        
        pnlText.add(lblTitle);
        pnlText.add(lblSubtitle);
        pnlTitle.add(pnlText);
        
        pnlHeader.add(pnlTitle, BorderLayout.CENTER);
        
        // Current time
        JLabel lblTime = new JLabel("🕐 " + java.time.LocalDate.now().toString());
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTime.setForeground(new Color(224, 231, 255));
        pnlHeader.add(lblTime, BorderLayout.EAST);
        
        // ========== MAIN CONTENT ==========
        JPanel pnlMain = new JPanel(new GridBagLayout());
        pnlMain.setOpaque(false);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Card 1: Quản Lý Phim
        gbc.gridx = 0; gbc.gridy = 0;
        pnlMain.add(createModernCard(
            "QUẢN LÝ PHIM",
            "Thêm, sửa, xóa và tìm kiếm phim",
            "🎬",
            new Color(99, 102, 241),
            new Color(79, 70, 229),
            e -> new QuanLyPhimForm().setVisible(true)
        ), gbc);
        
        // Card 2: Quản Lý Suất Chiếu
        gbc.gridx = 1;
        pnlMain.add(createModernCard(
            "SUẤT CHIẾU",
            "Lập lịch chiếu và quản lý phòng",
            "📅",
            new Color(34, 197, 94),
            new Color(22, 163, 74),
            e -> new QuanLySuatChieuForm().setVisible(true)
        ), gbc);
        
        // Card 3: Bán Vé
        gbc.gridx = 0; gbc.gridy = 1;
        pnlMain.add(createModernCard(
            "BÁN VÉ & THỐNG KÊ",
            "Bán vé và xem báo cáo doanh thu",
            "🎫",
            new Color(251, 146, 60),
            new Color(234, 88, 12),
            e -> new QuanLyVeThongKeForm().setVisible(true)
        ), gbc);
        
        // Card 4: Thoát
        gbc.gridx = 1;
        pnlMain.add(createModernCard(
            "THOÁT",
            "Đóng chương trình",
            "🚪",
            new Color(239, 68, 68),
            new Color(220, 38, 38),
            e -> {
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn thoát?",
                    "⚠️ Xác nhận",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        ), gbc);
        
        // ========== FOOTER ==========
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        pnlFooter.setOpaque(false);
        
        JLabel lblCopyright = new JLabel("© 2024 Cinema Management System");
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCopyright.setForeground(UIUtils.TEXT_SECONDARY);
        
        JLabel lblVersion = new JLabel("• Version 2.0");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblVersion.setForeground(UIUtils.TEXT_SECONDARY);
        
        JLabel lblDatabase = new JLabel("• Database: QLRapPhim");
        lblDatabase.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDatabase.setForeground(UIUtils.TEXT_SECONDARY);
        
        pnlFooter.add(lblCopyright);
        pnlFooter.add(lblVersion);
        pnlFooter.add(lblDatabase);
        
        // Add all panels
        backgroundPanel.add(pnlHeader, BorderLayout.NORTH);
        backgroundPanel.add(pnlMain, BorderLayout.CENTER);
        backgroundPanel.add(pnlFooter, BorderLayout.SOUTH);
    }
    
    /**
     * Create Modern Card with gradient and hover effects
     */
    private JPanel createModernCard(String title, String description, String icon,
                                   Color color1, Color color2,
                                   java.awt.event.ActionListener action) {
        JPanel card = new JPanel(new BorderLayout(0, 15)) {
            private boolean isHovered = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Shadow
                if (isHovered) {
                    g2d.setColor(new Color(0, 0, 0, 30));
                    g2d.fillRoundRect(4, 4, width - 8, height - 8, 20, 20);
                } else {
                    g2d.setColor(new Color(0, 0, 0, 15));
                    g2d.fillRoundRect(3, 3, width - 6, height - 6, 20, 20);
                }
                
                // Card background
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, width, height, 16, 16);
                
                // Accent gradient bar at top
                GradientPaint gradient = new GradientPaint(0, 0, color1, width, 0, color2);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, width, 6, 16, 16);
            }
            
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                    
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        if (action != null) action.actionPerformed(null);
                    }
                });
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(500, 240));
        
        // Container chính với layout vertical
        JPanel mainContainer = new JPanel(new GridBagLayout());
        mainContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        // Icon panel với background circle
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int size = 100;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                
                // Gradient circle
                RadialGradientPaint gradient = new RadialGradientPaint(
                    getWidth() / 2f, getHeight() / 2f, size / 2f,
                    new float[]{0.0f, 1.0f},
                    new Color[]{color1, color2}
                );
                g2d.setPaint(gradient);
                g2d.fillOval(x, y, size, size);
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(120, 120));
        iconPanel.setLayout(new GridBagLayout());
        
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconPanel.add(lblIcon);
        
        gbc.insets = new Insets(10, 0, 10, 0);
        mainContainer.add(iconPanel, gbc);
        
        // Title
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(UIUtils.TEXT_PRIMARY);
        gbc.insets = new Insets(5, 0, 5, 0);
        mainContainer.add(lblTitle, gbc);
        
        // Description
        JLabel lblDesc = new JLabel(description, SwingConstants.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(UIUtils.TEXT_SECONDARY);
        gbc.insets = new Insets(0, 0, 10, 0);
        mainContainer.add(lblDesc, gbc);
        
        card.add(mainContainer, BorderLayout.CENTER);
        
        return card;
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            JWindow splash = createModernSplash();
            splash.setVisible(true);
            
            Timer timer = new Timer(2500, e -> {
                splash.dispose();
                MainMenuForm form = new MainMenuForm();
                form.setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        });
    }
    
    private static JWindow createModernSplash() {
        JWindow splash = new JWindow();
        splash.setSize(600, 400);
        splash.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(99, 102, 241),
                    getWidth(), getHeight(), new Color(79, 70, 229)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Decorative circles
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillOval(-50, -50, 200, 200);
                g2d.fillOval(getWidth() - 150, getHeight() - 150, 200, 200);
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(15, 0, 15, 0);
        
        // Logo
        JLabel lblLogo = new JLabel("🎬");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 96));
        contentPanel.add(lblLogo, gbc);
        
        // Title
        JLabel lblTitle = new JLabel("CINEMA MANAGER");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        contentPanel.add(lblTitle, gbc);
        
        // Subtitle
        JLabel lblSubtitle = new JLabel("Hệ Thống Quản Lý Rạp Phim");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitle.setForeground(new Color(224, 231, 255));
        contentPanel.add(lblSubtitle, gbc);
        
        // Loading
        gbc.insets = new Insets(40, 0, 10, 0);
        JLabel lblLoading = new JLabel("Đang khởi động hệ thống...");
        lblLoading.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLoading.setForeground(new Color(200, 220, 255));
        contentPanel.add(lblLoading, gbc);
        
        // Progress bar
        gbc.insets = new Insets(0, 60, 0, 60);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setForeground(Color.WHITE);
        progressBar.setBackground(new Color(79, 70, 229));
        progressBar.setPreferredSize(new Dimension(480, 6));
        progressBar.setBorderPainted(false);
        contentPanel.add(progressBar, gbc);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        splash.setContentPane(panel);
        
        return splash;
    }
}