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
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // ========== BACKGROUND WITH PATTERN ==========
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(240, 242, 245),
                    getWidth(), getHeight(), new Color(220, 225, 235)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw pattern (dots)
                g2d.setColor(new Color(200, 210, 220, 30));
                for (int x = 0; x < getWidth(); x += 30) {
                    for (int y = 0; y < getHeight(); y += 30) {
                        g2d.fillOval(x, y, 3, 3);
                    }
                }
            }
        };
        backgroundPanel.setOpaque(false);
        setContentPane(backgroundPanel);
        
        // ========== HEADER ==========
        JPanel pnlHeader = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient header
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(52, 152, 219),
                    getWidth(), 0, new Color(41, 128, 185)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JPanel pnlTitle = new JPanel(new GridLayout(2, 1, 0, 5));
        pnlTitle.setOpaque(false);
        
        JLabel lblLogo = new JLabel("🎬 CINEMA MANAGER");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblLogo.setForeground(Color.WHITE);
        
        JLabel lblSubtitle = new JLabel("Hệ Thống Quản Lý Rạp Chiếu Phim Chuyên Nghiệp");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(230, 240, 255));
        
        pnlTitle.add(lblLogo);
        pnlTitle.add(lblSubtitle);
        pnlHeader.add(pnlTitle, BorderLayout.CENTER);
        
        // ========== MAIN CONTENT ==========
        JPanel pnlMain = new JPanel(new GridBagLayout());
        pnlMain.setOpaque(false);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Card 1: Quản Lý Phim
        gbc.gridx = 0; gbc.gridy = 0;
        JPanel cardPhim = createIconCard(
            "QUẢN LÝ PHIM",
            "Thêm, sửa, xóa và tìm kiếm phim",
            new Color(52, 152, 219),
            new Color(41, 128, 185),
            "FILM",
            e -> new QuanLyPhimForm().setVisible(true)
        );
        pnlMain.add(cardPhim, gbc);
        
        // Card 2: Quản Lý Suất Chiếu
        gbc.gridx = 1;
        JPanel cardSuatChieu = createIconCard(
            "QUẢN LÝ SUẤT CHIẾU",
            "Lập lịch chiếu và quản lý phòng",
            new Color(46, 204, 113),
            new Color(39, 174, 96),
            "CALENDAR",
            e -> new QuanLySuatChieuForm().setVisible(true)
        );
        pnlMain.add(cardSuatChieu, gbc);
        
        // Card 3: Bán Vé
        gbc.gridx = 0; gbc.gridy = 1;
        JPanel cardVe = createIconCard(
            "BÁN VÉ & THỐNG KÊ",
            "Bán vé và xem báo cáo doanh thu",
            new Color(155, 89, 182),
            new Color(142, 68, 173),
            "TICKET",
            e -> new QuanLyVeThongKeForm().setVisible(true)
        );
        pnlMain.add(cardVe, gbc);
        
        // Card 4: Thoát
        gbc.gridx = 1;
        JPanel cardThoat = createIconCard(
            "THOÁT",
            "Đóng chương trình",
            new Color(231, 76, 60),
            new Color(192, 57, 43),
            "EXIT",
            e -> {
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn thoát?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        );
        pnlMain.add(cardThoat, gbc);
        
        // ========== FOOTER ==========
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlFooter.setOpaque(false);
        pnlFooter.setPreferredSize(new Dimension(0, 60));
        
        JLabel lblFooter = new JLabel("© 2024 Cinema Management System | Database: QLRapPhim");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooter.setForeground(new Color(100, 100, 100));
        pnlFooter.add(lblFooter);
        
        // Add all panels
        backgroundPanel.add(pnlHeader, BorderLayout.NORTH);
        backgroundPanel.add(pnlMain, BorderLayout.CENTER);
        backgroundPanel.add(pnlFooter, BorderLayout.SOUTH);
    }
    
    /**
     * Tạo card với icon vẽ bằng Graphics2D
     */
    private JPanel createIconCard(String title, String description, 
                                   Color color1, Color color2, String iconType,
                                   java.awt.event.ActionListener action) {
        JPanel card = new JPanel(new BorderLayout(0, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Rounded rectangle background
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Shadow effect (optional)
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.fillRoundRect(2, 2, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(35, 30, 35, 30));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(450, 240));
        
        // Icon panel với gradient
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                
                // Gradient circle background
                GradientPaint gradient = new GradientPaint(
                    centerX - 40, centerY - 40, color1,
                    centerX + 40, centerY + 40, color2
                );
                g2d.setPaint(gradient);
                g2d.fillOval(centerX - 50, centerY - 50, 100, 100);
                
                // Draw icon
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                switch (iconType) {
                    case "FILM":
                        drawFilmIcon(g2d, centerX, centerY);
                        break;
                    case "CALENDAR":
                        drawCalendarIcon(g2d, centerX, centerY);
                        break;
                    case "TICKET":
                        drawTicketIcon(g2d, centerX, centerY);
                        break;
                    case "EXIT":
                        drawExitIcon(g2d, centerX, centerY);
                        break;
                }
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(0, 120));
        
        // Text panel
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        textPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(40, 40, 40));
        
        JLabel lblDesc = new JLabel(description, SwingConstants.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(new Color(120, 120, 120));
        
        textPanel.add(lblTitle);
        textPanel.add(lblDesc);
        
        card.add(iconPanel, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        
        // Hover effect với animation
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color1, 3),
                    BorderFactory.createEmptyBorder(32, 27, 32, 27)
                ));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createEmptyBorder(35, 30, 35, 30));
            }
            
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (action != null) {
                    action.actionPerformed(null);
                }
            }
        });
        
        return card;
    }
    
    // ========== ICON DRAWING METHODS ==========
    
    private void drawFilmIcon(Graphics2D g2d, int x, int y) {
        // Film reel
        g2d.drawRect(x - 25, y - 20, 50, 40);
        g2d.drawLine(x - 25, y - 10, x + 25, y - 10);
        g2d.drawLine(x - 25, y, x + 25, y);
        g2d.drawLine(x - 25, y + 10, x + 25, y + 10);
        
        // Holes
        int[] holeX = {x - 20, x - 10, x, x + 10, x + 20};
        for (int hx : holeX) {
            g2d.fillRect(hx - 2, y - 18, 4, 4);
            g2d.fillRect(hx - 2, y + 14, 4, 4);
        }
    }
    
    private void drawCalendarIcon(Graphics2D g2d, int x, int y) {
        // Calendar body
        g2d.drawRoundRect(x - 25, y - 20, 50, 45, 5, 5);
        
        // Header
        g2d.fillRoundRect(x - 25, y - 20, 50, 12, 5, 5);
        
        // Rings
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(x - 15, y - 23, x - 15, y - 15);
        g2d.drawLine(x + 15, y - 23, x + 15, y - 15);
        
        // Grid
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(x - 15, y - 3, x + 15, y - 3);
        g2d.drawLine(x - 15, y + 7, x + 15, y + 7);
        g2d.drawLine(x - 5, y - 3, x - 5, y + 17);
        g2d.drawLine(x + 5, y - 3, x + 5, y + 17);
    }
    
    private void drawTicketIcon(Graphics2D g2d, int x, int y) {
        // Ticket outline
        g2d.setStroke(new BasicStroke(4));
        
        // Main body
        GeneralPath ticket = new GeneralPath();
        ticket.moveTo(x - 30, y - 15);
        ticket.lineTo(x + 10, y - 15);
        ticket.curveTo(x + 15, y - 15, x + 15, y - 10, x + 15, y - 5);
        ticket.curveTo(x + 15, y, x + 15, y + 5, x + 10, y + 5);
        ticket.lineTo(x + 10, y + 15);
        ticket.lineTo(x - 30, y + 15);
        ticket.closePath();
        g2d.draw(ticket);
        
        // Perforation
        g2d.setStroke(new BasicStroke(2));
        for (int i = -10; i <= 10; i += 5) {
            g2d.drawLine(x, y + i, x + 3, y + i);
        }
    }
    
    private void drawExitIcon(Graphics2D g2d, int x, int y) {
        // Door
        g2d.drawRoundRect(x - 15, y - 20, 30, 40, 5, 5);
        
        // Arrow pointing right
        g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(x - 25, y, x - 5, y);
        g2d.drawLine(x - 15, y - 10, x - 5, y);
        g2d.drawLine(x - 15, y + 10, x - 5, y);
        
        // Door handle
        g2d.fillOval(x + 5, y - 3, 6, 6);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.setProperty("awt.useSystemAAFontSettings", "on");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            JWindow splash = createModernSplash();
            splash.setVisible(true);
            
            Timer timer = new Timer(2000, e -> {
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
        splash.setSize(500, 350);
        splash.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(52, 152, 219),
                    0, getHeight(), new Color(41, 128, 185)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(15, 0, 15, 0);
        
        // Logo text
        JLabel lblLogo = new JLabel("🎬");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        contentPanel.add(lblLogo, gbc);
        
        JLabel lblTitle = new JLabel("CINEMA MANAGER");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        contentPanel.add(lblTitle, gbc);
        
        JLabel lblSubtitle = new JLabel("Hệ Thống Quản Lý Rạp Phim");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(230, 240, 255));
        contentPanel.add(lblSubtitle, gbc);
        
        gbc.insets = new Insets(30, 0, 10, 0);
        JLabel lblLoading = new JLabel("Đang khởi động...");
        lblLoading.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLoading.setForeground(new Color(200, 220, 240));
        contentPanel.add(lblLoading, gbc);
        
        // Progress bar
        gbc.insets = new Insets(0, 50, 0, 50);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setForeground(Color.WHITE);
        progressBar.setBackground(new Color(41, 128, 185));
        progressBar.setPreferredSize(new Dimension(400, 4));
        progressBar.setBorderPainted(false);
        contentPanel.add(progressBar, gbc);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        splash.setContentPane(panel);
        
        return splash;
    }
}