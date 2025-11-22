package gui;

import dao.VeDAO;
import dao.SuatChieuDAO;
import dao.ThongKeDAO;
import dao.PhimDAO;
import model.Ve;
import model.SuatChieu;
import model.Phim;
import utils.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.Map;

public class QuanLyVeThongKeForm extends JFrame {
    private VeDAO veDAO;
    private SuatChieuDAO suatChieuDAO;
    private ThongKeDAO thongKeDAO;
    private PhimDAO phimDAO;
    private DefaultTableModel tableModelVe, tableModelThongKe;
    
    private JTable tableVe, tableThongKe;
    private JTextField txtSoLuong;
    private JLabel lblGiaVe, lblThanhTien, lblThongTinPhim;
    private JComboBox<String> cboPhim, cboSuatChieu, cboLoaiThongKe;
    private JTextField txtNamThongKe, txtTuNgay, txtDenNgay;
    private JButton btnBanVe, btnLamMoi, btnCapNhatTK, btnLocThongKe;
    private JLabel lblTongVe, lblTongDoanhThu;
    
    public QuanLyVeThongKeForm() {
        veDAO = new VeDAO();
        suatChieuDAO = new SuatChieuDAO();
        thongKeDAO = new ThongKeDAO();
        phimDAO = new PhimDAO();
        
        initComponents();
        loadComboBoxPhim();
        loadDataVe();
        loadThongKe();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Quản Lý Vé & Thống Kê");
        setSize(1600, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, UIUtils.GRAY_50,
                    0, getHeight(), UIUtils.GRAY_100
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setOpaque(false);
        setContentPane(mainPanel);
        
        // Header
        JPanel pnlHeader = UIUtils.createHeaderPanel(
            new Color(251, 146, 60),
            new Color(234, 88, 12)
        );
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        pnlHeader.setLayout(new BorderLayout());
        
        JPanel pnlTitleArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlTitleArea.setOpaque(false);
        
        JLabel lblIcon = new JLabel("🎫");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        pnlTitleArea.add(lblIcon);
        
        JLabel lblTitle = new JLabel("BÁN VÉ & THỐNG KÊ");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        pnlTitleArea.add(lblTitle);
        
        pnlHeader.add(pnlTitleArea, BorderLayout.WEST);
        
        JButton btnBack = UIUtils.createIconButton("🏠", UIUtils.WARNING_DARK, UIUtils.WARNING_COLOR);
        btnBack.setToolTipText("Quay lại trang chủ");
        btnBack.addActionListener(e -> dispose());
        pnlHeader.add(btnBack, BorderLayout.EAST);
        
        mainPanel.add(pnlHeader, BorderLayout.NORTH);
        
        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIUtils.FONT_SUBHEADING);
        tabbedPane.setBackground(Color.WHITE);
        
       tabbedPane.addTab(
    "  🎫 Bán Vé  ",
    // Giữ JScrollPane bao ngoài cho Tab Bán Vé
    new JScrollPane(createBanVePanel(),
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
);

tabbedPane.addTab(
    "  📊 Thống Kê  ",
    // Giữ JScrollPane bao ngoài cho Tab Thống Kê
    new JScrollPane(createThongKePanel(),
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
);

        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }
    
    // ========== TAB BÁN VÉ ==========
    private JPanel createBanVePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // LEFT: Form bán vé
        JPanel pnlFormWrapper = createFormBanVe();
        
        // SỬA ĐỔI: Bọc pnlFormWrapper vào JScrollPane
        JScrollPane scrollForm = new JScrollPane(pnlFormWrapper);
        scrollForm.setBorder(null); // Không có border cho scroll pane
        scrollForm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollForm.setPreferredSize(new Dimension(480, 0)); // Giữ chiều rộng cố định cho khu vực WEST
        
        // RIGHT: Danh sách vé
        JPanel pnlTableVe = createTableVe();
        
        // THAY THẾ: Thêm JScrollPane thay vì JPanel
        panel.add(scrollForm, BorderLayout.WEST); // <<< SỬA ĐỔI Ở ĐÂY
        panel.add(pnlTableVe, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFormBanVe() {
        JPanel wrapper = UIUtils.createCardPanel();
        wrapper.setLayout(new BorderLayout(0, 15));
        
        // Title
        JLabel lblTitle = new JLabel("🎬 Bán Vé Xem Phim");
        lblTitle.setFont(UIUtils.FONT_HEADING);
        lblTitle.setForeground(UIUtils.TEXT_PRIMARY);
        wrapper.add(lblTitle, BorderLayout.NORTH);
        
        // Form
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        
        // BƯỚC 1: Chọn phim
        gbc.gridy = 0;
        JLabel lblStep1 = new JLabel("️⃣ Chọn Phim *");
        lblStep1.setFont(UIUtils.FONT_SUBHEADING);
        lblStep1.setForeground(new Color(99, 102, 241));
        pnlForm.add(lblStep1, gbc);
        
        gbc.gridy = 1;
        cboPhim = new JComboBox<>();
        UIUtils.styleComboBox(cboPhim);
        cboPhim.setPreferredSize(new Dimension(0, 40));
        cboPhim.addActionListener(e -> loadSuatChieuTheoPhim());
        pnlForm.add(cboPhim, gbc);
        
        // Thông tin phim
        gbc.gridy = 2;
        lblThongTinPhim = new JLabel("<html><i>Chọn phim để xem thông tin</i></html>");
        lblThongTinPhim.setFont(UIUtils.FONT_SMALL);
        lblThongTinPhim.setForeground(UIUtils.TEXT_MUTED);
        pnlForm.add(lblThongTinPhim, gbc);
        
        // BƯỚC 2: Chọn suất chiếu
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 10, 0);
        JLabel lblStep2 = new JLabel("️⃣ Chọn Suất Chiếu *");
        lblStep2.setFont(UIUtils.FONT_SUBHEADING);
        lblStep2.setForeground(new Color(34, 197, 94));
        pnlForm.add(lblStep2, gbc);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 10, 0);
        cboSuatChieu = new JComboBox<>();
        UIUtils.styleComboBox(cboSuatChieu);
        cboSuatChieu.setPreferredSize(new Dimension(0, 40));
        cboSuatChieu.addActionListener(e -> hienThiGiaVe());
        pnlForm.add(cboSuatChieu, gbc);
        
        // Giá vé
        gbc.gridy = 5;
        JPanel pnlGiaVe = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlGiaVe.setOpaque(false);
        JLabel lblGiaVeText = new JLabel("💰 Giá vé:");
        lblGiaVeText.setFont(UIUtils.FONT_BODY);
        lblGiaVeText.setForeground(UIUtils.TEXT_SECONDARY);
        pnlGiaVe.add(lblGiaVeText);
        
        lblGiaVe = new JLabel("0 VNĐ");
        lblGiaVe.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblGiaVe.setForeground(new Color(34, 197, 94));
        pnlGiaVe.add(lblGiaVe);
        pnlForm.add(pnlGiaVe, gbc);
        
        // BƯỚC 3: Số lượng
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 0, 10, 0);
        JLabel lblStep3 = new JLabel("️⃣ Số Lượng Vé *");
        lblStep3.setFont(UIUtils.FONT_SUBHEADING);
        lblStep3.setForeground(new Color(251, 146, 60));
        pnlForm.add(lblStep3, gbc);
        
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 0, 10, 0);
        JPanel pnlSoLuong = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlSoLuong.setOpaque(false);
        
        JButton btnGiam = UIUtils.createIconButton("-", UIUtils.DANGER_COLOR, UIUtils.DANGER_DARK);
        btnGiam.setPreferredSize(new Dimension(45, 45));
        btnGiam.addActionListener(e -> {
            try {
                int sl = Integer.parseInt(txtSoLuong.getText());
                if (sl > 1) {
                    txtSoLuong.setText(String.valueOf(sl - 1));
                    tinhThanhTien();
                }
            } catch (Exception ex) {}
        });
        pnlSoLuong.add(btnGiam);
        
        txtSoLuong = new JTextField("1", 8);
        txtSoLuong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtSoLuong.setHorizontalAlignment(JTextField.CENTER);
        UIUtils.styleTextField(txtSoLuong);
        txtSoLuong.setPreferredSize(new Dimension(100, 45));
        txtSoLuong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tinhThanhTien();
            }
        });
        pnlSoLuong.add(txtSoLuong);
        
        JButton btnTang = UIUtils.createIconButton("+", UIUtils.SUCCESS_COLOR, UIUtils.SUCCESS_DARK);
        btnTang.setPreferredSize(new Dimension(45, 45));
        btnTang.addActionListener(e -> {
            try {
                int sl = Integer.parseInt(txtSoLuong.getText());
                txtSoLuong.setText(String.valueOf(sl + 1));
                tinhThanhTien();
            } catch (Exception ex) {}
        });
        pnlSoLuong.add(btnTang);
        
        pnlForm.add(pnlSoLuong, gbc);
        
        // Thành tiền
        gbc.gridy = 8;
        gbc.insets = new Insets(20, 0, 20, 0);
        JPanel pnlThanhTien = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(239, 68, 68, 30),
                    getWidth(), 0, new Color(220, 38, 38, 30)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        pnlThanhTien.setOpaque(false);
        pnlThanhTien.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblThanhTienText = new JLabel("💵 TỔNG THANH TOÁN:");
        lblThanhTienText.setFont(UIUtils.FONT_BODY);
        lblThanhTienText.setForeground(UIUtils.TEXT_SECONDARY);
        pnlThanhTien.add(lblThanhTienText, BorderLayout.NORTH);
        
        lblThanhTien = new JLabel("0 VNĐ");
        lblThanhTien.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblThanhTien.setForeground(new Color(239, 68, 68));
        pnlThanhTien.add(lblThanhTien, BorderLayout.CENTER);
        
        pnlForm.add(pnlThanhTien, gbc);
        
        // Buttons
        gbc.gridy = 9;
        gbc.insets = new Insets(10, 0, 0, 0);
        JPanel pnlButtons = new JPanel(new GridLayout(1, 2, 12, 0));
        pnlButtons.setBackground(Color.WHITE);
        
        btnBanVe = UIUtils.createSuccessButton("💳 BÁN VÉ");
        btnBanVe.setPreferredSize(new Dimension(0, 50));
        
        btnLamMoi = UIUtils.createSecondaryButton("🔄 Làm Mới");
        btnLamMoi.setPreferredSize(new Dimension(0, 50));
        
        pnlButtons.add(btnBanVe);
        pnlButtons.add(btnLamMoi);
        pnlForm.add(pnlButtons, gbc);
        
        wrapper.add(pnlForm, BorderLayout.CENTER);
        
        // Events
        btnBanVe.addActionListener(e -> banVe());
        btnLamMoi.addActionListener(e -> lamMoi());
        
        return wrapper;
    }
    
    private JPanel createTableVe() {
        JPanel wrapper = UIUtils.createCardPanel();
        wrapper.setLayout(new BorderLayout(0, 15));
        
        JLabel lblTitle = new JLabel("📋 Danh Sách Vé Đã Bán");
        lblTitle.setFont(UIUtils.FONT_HEADING);
        lblTitle.setForeground(UIUtils.TEXT_PRIMARY);
        wrapper.add(lblTitle, BorderLayout.NORTH);
        
        String[] columnNames = {"Mã Vé", "Mã Suất", "Tên Phim", "Phòng", 
                                "Ngày", "Giờ", "Giá Vé", "SL", "Thành Tiền"};
        tableModelVe = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableVe = new JTable(tableModelVe);
        tableVe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UIUtils.styleTable(tableVe);
        
        tableVe.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableVe.getColumnModel().getColumn(1).setPreferredWidth(70);
        tableVe.getColumnModel().getColumn(2).setPreferredWidth(180);
        tableVe.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableVe.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableVe.getColumnModel().getColumn(5).setPreferredWidth(80);
        tableVe.getColumnModel().getColumn(6).setPreferredWidth(90);
        tableVe.getColumnModel().getColumn(7).setPreferredWidth(50);
        tableVe.getColumnModel().getColumn(8).setPreferredWidth(100);
        
        JScrollPane scrollVe = new JScrollPane(tableVe);
        scrollVe.setBorder(BorderFactory.createLineBorder(UIUtils.BORDER_COLOR));
        wrapper.add(scrollVe, BorderLayout.CENTER);
        
        return wrapper;
    }
    
    // ========== TAB THỐNG KÊ ==========
    private JPanel createThongKePanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // TOP: Stats cards
        JPanel pnlStats = createStatsPanel();
        panel.add(pnlStats, BorderLayout.NORTH);
        
        // CENTER: Table + Filter
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setOpaque(false);
        
        // Filter
        JPanel pnlFilter = createFilterPanel();
        pnlCenter.add(pnlFilter, BorderLayout.NORTH);
        
        // Table
        JPanel pnlTable = createTableThongKe();
        pnlCenter.add(pnlTable, BorderLayout.CENTER);
        
        panel.add(pnlCenter, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(0, 120));
        
        // Card 1: Tổng vé
        JPanel card1 = UIUtils.createStatsCard(
            "TỔNG SỐ VÉ ĐÃ BÁN",
            "0",
            "🎫",
            new Color(59, 130, 246)
        );
        panel.add(card1);
        
        // Card 2: Doanh thu
        JPanel card2 = UIUtils.createStatsCard(
            "TỔNG DOANH THU",
            "0 VNĐ",
            "💰",
            new Color(34, 197, 94)
        );
        panel.add(card2);
        
        // Lưu reference để update
        lblTongVe = (JLabel) ((JPanel)card1.getComponent(1)).getComponent(1);
        lblTongDoanhThu = (JLabel) ((JPanel)card2.getComponent(1)).getComponent(1);
        
        return panel;
    }
    
    private JPanel createFilterPanel() {
        JPanel wrapper = UIUtils.createCardPanel();
        wrapper.setLayout(new BorderLayout(15, 0));
        
        JLabel lblTitle = new JLabel("🔍 Bộ Lọc Thống Kê");
        lblTitle.setFont(UIUtils.FONT_SUBHEADING);
        lblTitle.setForeground(UIUtils.TEXT_PRIMARY);
        wrapper.add(lblTitle, BorderLayout.WEST);
        
        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        pnlControls.setBackground(Color.WHITE);
        
        JLabel lblLoai = new JLabel("Loại:");
        lblLoai.setFont(UIUtils.FONT_BODY);
        pnlControls.add(lblLoai);
        
        cboLoaiThongKe = new JComboBox<>(new String[]{
            "Theo Phim", "Theo Ngày", "Theo Tuần", "Theo Tháng", "Theo Năm"
        });
        UIUtils.styleComboBox(cboLoaiThongKe);
        cboLoaiThongKe.setPreferredSize(new Dimension(130, 36));
        pnlControls.add(cboLoaiThongKe);
        
        pnlControls.add(new JLabel("  "));
        
        JLabel lblTuNgay = new JLabel("Từ ngày:");
        lblTuNgay.setFont(UIUtils.FONT_BODY);
        pnlControls.add(lblTuNgay);
        
        txtTuNgay = new JTextField(10);
        UIUtils.styleTextField(txtTuNgay);
        txtTuNgay.setPreferredSize(new Dimension(120, 36));
        pnlControls.add(txtTuNgay);
        
        JLabel lblDenNgay = new JLabel("Đến:");
        lblDenNgay.setFont(UIUtils.FONT_BODY);
        pnlControls.add(lblDenNgay);
        
        txtDenNgay = new JTextField(10);
        UIUtils.styleTextField(txtDenNgay);
        txtDenNgay.setPreferredSize(new Dimension(120, 36));
        pnlControls.add(txtDenNgay);
        
        pnlControls.add(new JLabel("  "));
        
        JLabel lblNam = new JLabel("Năm:");
        lblNam.setFont(UIUtils.FONT_BODY);
        pnlControls.add(lblNam);
        
        txtNamThongKe = new JTextField("2024", 6);
        UIUtils.styleTextField(txtNamThongKe);
        txtNamThongKe.setPreferredSize(new Dimension(80, 36));
        pnlControls.add(txtNamThongKe);
        
        btnLocThongKe = UIUtils.createWarningButton("📊 Lọc");
        pnlControls.add(btnLocThongKe);
        
        btnCapNhatTK = UIUtils.createInfoButton("🔄 Cập Nhật");
        pnlControls.add(btnCapNhatTK);
        
        wrapper.add(pnlControls, BorderLayout.CENTER);
        
        // Events
        btnLocThongKe.addActionListener(e -> locThongKe());
        btnCapNhatTK.addActionListener(e -> loadThongKe());
        
        return wrapper;
    }
    
    private JPanel createTableThongKe() {
        JPanel wrapper = UIUtils.createCardPanel();
        wrapper.setLayout(new BorderLayout());
        
        String[] columnNames = {"STT", "Tiêu Chí", "Số Vé Bán", "Doanh Thu (VNĐ)"};
        tableModelThongKe = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableThongKe = new JTable(tableModelThongKe);
        UIUtils.styleTable(tableThongKe);
        
        tableThongKe.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableThongKe.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableThongKe.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableThongKe.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        JScrollPane scrollTK = new JScrollPane(tableThongKe);
        scrollTK.setBorder(BorderFactory.createLineBorder(UIUtils.BORDER_COLOR));
        wrapper.add(scrollTK, BorderLayout.CENTER);
        
        return wrapper;
    }
    
    // ========== METHODS ==========
    
    private void loadComboBoxPhim() {
        try {
            cboPhim.removeAllItems();
            List<Phim> danhSachPhim = phimDAO.layDanhSachPhim();
            
            if (danhSachPhim.isEmpty()) {
                UIUtils.showWarningMessage(this, "Chưa có phim nào!");
                return;
            }
            
            cboPhim.addItem("-- Chọn phim --");
            for (Phim phim : danhSachPhim) {
                String item = String.format("%d - %s (%s, %d phút)", 
                    phim.getMaPhim(), 
                    phim.getTenPhim(),
                    phim.getTheLoai() != null ? phim.getTheLoai() : "N/A",
                    phim.getThoiLuong());
                cboPhim.addItem(item);
            }
        } catch (Exception e) {
            UIUtils.showErrorMessage(this, "Lỗi load phim: " + e.getMessage());
        }
    }
    
    private void loadSuatChieuTheoPhim() {
        try {
            cboSuatChieu.removeAllItems();
            lblGiaVe.setText("0 VNĐ");
            lblThanhTien.setText("0 VNĐ");
            lblThongTinPhim.setText("<html><i>Chọn phim để xem thông tin</i></html>");
            
            if (cboPhim.getSelectedIndex() == 0) return;
            
            int maPhim = getMaPhimFromCombo();
            if (maPhim == 0) return;
            
            Phim phim = phimDAO.layPhimTheoMa(maPhim);
            if (phim != null) {
                lblThongTinPhim.setText(String.format(
                    "<html><b>%s</b> | %s | %d phút</html>",
                    phim.getTenPhim(),
                    phim.getTheLoai() != null ? phim.getTheLoai() : "N/A",
                    phim.getThoiLuong()
                ));
            }
            
            List<SuatChieu> danhSach = suatChieuDAO.layDanhSachSuatChieu();
            boolean coSuatChieu = false;
            
            for (SuatChieu sc : danhSach) {
                if (sc.getMaPhim() == maPhim) {
                    coSuatChieu = true;
                    int conTrong = sc.getSoGhe() - sc.getSoVeDaBan();
                    String item = String.format("%d - %s %s (%s) - %,d VNĐ | Còn %d", 
                        sc.getMaSuat(), 
                        sc.getNgayChieu(), 
                        sc.getGioChieu(),
                        sc.getTenPhong() != null ? sc.getTenPhong() : "N/A",
                        sc.getGiaVe(),
                        conTrong);
                    cboSuatChieu.addItem(item);
                }
            }
            
            if (!coSuatChieu) {
                cboSuatChieu.addItem("-- Phim này chưa có suất chiếu --");
                UIUtils.showWarningMessage(this, "Phim này chưa có suất chiếu!");
            }
        } catch (Exception e) {
            UIUtils.showErrorMessage(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void hienThiGiaVe() {
        try {
            int maSuat = getMaSuatFromCombo();
            if (maSuat > 0) {
                SuatChieu sc = suatChieuDAO.laySuatChieuTheoMa(maSuat);
                if (sc != null) {
                    lblGiaVe.setText(String.format("%,d VNĐ", sc.getGiaVe()));
                    tinhThanhTien();
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
    }
    
    private void tinhThanhTien() {
        try {
            String giaVeStr = lblGiaVe.getText().replace(",", "").replace(" VNĐ", "").trim();
            int giaVe = Integer.parseInt(giaVeStr);
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            
            long thanhTien = (long) giaVe * soLuong;
            lblThanhTien.setText(String.format("%,d VNĐ", thanhTien));
        } catch (Exception e) {
            lblThanhTien.setText("0 VNĐ");
        }
    }
    
    private int getMaPhimFromCombo() {
        try {
            String selected = (String) cboPhim.getSelectedItem();
            if (selected != null && !selected.startsWith("--") && selected.contains(" - ")) {
                return Integer.parseInt(selected.split(" - ")[0].trim());
            }
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return 0;
    }
    
    private int getMaSuatFromCombo() {
        try {
            String selected = (String) cboSuatChieu.getSelectedItem();
            if (selected != null && !selected.startsWith("--") && selected.contains(" - ")) {
                return Integer.parseInt(selected.split(" - ")[0].trim());
            }
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return 0;
    }
    
    private boolean validateInput() {
        if (cboPhim.getSelectedIndex() == 0) {
            UIUtils.showWarningMessage(this, "Vui lòng chọn phim!");
            return false;
        }
        
        if (cboSuatChieu.getSelectedItem() == null || 
            cboSuatChieu.getSelectedItem().toString().startsWith("--")) {
            UIUtils.showWarningMessage(this, "Vui lòng chọn suất chiếu!");
            return false;
        }
        
        try {
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) {
                UIUtils.showWarningMessage(this, "Số lượng phải > 0!");
                txtSoLuong.requestFocus();
                return false;
            }
            
            int maSuat = getMaSuatFromCombo();
            if (!veDAO.kiemTraConGheTrong(maSuat, soLuong)) {
                UIUtils.showWarningMessage(this, 
                    "⚠️ Không đủ ghế trống!\nVui lòng giảm số lượng.");
                return false;
            }
            
        } catch (NumberFormatException e) {
            UIUtils.showWarningMessage(this, "Số lượng phải là số nguyên!");
            txtSoLuong.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void banVe() {
        if (!validateInput()) return;
        
        try {
            int maSuat = getMaSuatFromCombo();
            SuatChieu sc = suatChieuDAO.laySuatChieuTheoMa(maSuat);
            
            Ve ve = new Ve();
            ve.setMaSuat(maSuat);
            ve.setGiaVe(sc.getGiaVe());
            ve.setSoLuong(Integer.parseInt(txtSoLuong.getText().trim()));
            
            if (veDAO.banVe(ve)) {
                int thanhTien = ve.getGiaVe() * ve.getSoLuong();
                
                String thongBao = String.format(
                    "✅ BÁN VÉ THÀNH CÔNG!\n\n" +
                    "━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                    "🎬 Phim: %s\n" +
                    "📅 Ngày: %s\n" +
                    "🕐 Giờ: %s\n" +
                    "🏠 Phòng: %s\n" +
                    "🎫 Số lượng: %d vé\n" +
                    "💰 Giá vé: %,d VNĐ\n" +
                    "━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                    "💵 TỔNG TIỀN: %,d VNĐ",
                    sc.getTenPhim(),
                    sc.getNgayChieu(),
                    sc.getGioChieu(),
                    sc.getTenPhong(),
                    ve.getSoLuong(),
                    ve.getGiaVe(),
                    thanhTien
                );
                
                JOptionPane.showMessageDialog(this, 
                    thongBao, 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                lamMoi();
            } else {
                UIUtils.showErrorMessage(this, "Bán vé thất bại!");
            }
        } catch (Exception ex) {
            UIUtils.showErrorMessage(this, "Lỗi: " + ex.getMessage());
        }
    }
    
    private void lamMoi() {
        txtSoLuong.setText("1");
        lblGiaVe.setText("0 VNĐ");
        lblThanhTien.setText("0 VNĐ");
        lblThongTinPhim.setText("<html><i>Chọn phim để xem thông tin</i></html>");
        cboPhim.setSelectedIndex(0);
        cboSuatChieu.removeAllItems();
        loadComboBoxPhim();
        loadDataVe();
        loadThongKe();
    }
    
    private void loadDataVe() {
        try {
            tableModelVe.setRowCount(0);
            List<Ve> danhSach = veDAO.layDanhSachVe();
            
            for (Ve ve : danhSach) {
                Object[] row = {
                    ve.getMaVe(),
                    ve.getMaSuat(),
                    ve.getTenPhim() != null ? ve.getTenPhim() : "N/A",
                    ve.getTenPhong() != null ? ve.getTenPhong() : "N/A",
                    ve.getNgayChieu(),
                    ve.getGioChieu(),
                    String.format("%,d", ve.getGiaVe()),
                    ve.getSoLuong(),
                    String.format("%,d", ve.getThanhTien())
                };
                tableModelVe.addRow(row);
            }
        } catch (Exception e) {
            UIUtils.showErrorMessage(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void loadThongKe() {
        try {
            cboLoaiThongKe.setSelectedIndex(0);
            locThongKe();
            
            int tongVe = thongKeDAO.demTongSoVe();
            long tongDoanhThu = thongKeDAO.tinhTongDoanhThu();
            
            lblTongVe.setText(String.format("%,d", tongVe));
            lblTongDoanhThu.setText(String.format("%,d VNĐ", tongDoanhThu));
        } catch (Exception e) {
            UIUtils.showErrorMessage(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void locThongKe() {
        try {
            tableModelThongKe.setRowCount(0);
            String loai = (String) cboLoaiThongKe.getSelectedItem();
            
            switch (loai) {
                case "Theo Phim": thongKeTheoPhim(); break;
                case "Theo Ngày": thongKeTheoNgay(); break;
                case "Theo Tuần": thongKeTheoTuan(); break;
                case "Theo Tháng": thongKeTheoThang(); break;
                case "Theo Năm": thongKeTheoNam(); break;
            }
        } catch (Exception e) {
            UIUtils.showErrorMessage(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void thongKeTheoPhim() {
        Map<String, Object> thongKe = thongKeDAO.thongKeVeTheoPhim();
        int stt = 1;
        for (Object phimData : thongKe.values()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) phimData;
            Object[] row = {
                stt++,
                data.get("TenPhim") + " (" + data.get("TheLoai") + ")",
                String.format("%,d", data.get("SoVeBan")),
                String.format("%,d", (Long) data.get("TongDoanhThu"))
            };
            tableModelThongKe.addRow(row);
        }
    }
    
    private void thongKeTheoNgay() {
        try {
            Date tuNgay = Date.valueOf(txtTuNgay.getText().trim());
            Date denNgay = Date.valueOf(txtDenNgay.getText().trim());
            
            var danhSach = thongKeDAO.thongKeTheoNgay(tuNgay, denNgay);
            int stt = 1;
            for (var tk : danhSach) {
                Object[] row = {
                    stt++,
                    tk.getNgayChieu(),
                    String.format("%,d", tk.getTongSoVe()),
                    String.format("%,d", tk.getTongDoanhThu())
                };
                tableModelThongKe.addRow(row);
            }
        } catch (Exception e) {
            UIUtils.showWarningMessage(this, "Vui lòng nhập đúng định dạng ngày (yyyy-MM-dd)");
        }
    }
    
    private void thongKeTheoTuan() {
        try {
            int nam = Integer.parseInt(txtNamThongKe.getText().trim());
            var danhSach = thongKeDAO.thongKeTheoTuan(nam);
            int stt = 1;
            for (var row : danhSach) {
                tableModelThongKe.addRow(new Object[]{
                    stt++,
                    "Tuần " + row.get("Tuan"),
                    String.format("%,d", row.get("TongVe")),
                    String.format("%,d", row.get("DoanhThu"))
                });
            }
        } catch (Exception e) {
            UIUtils.showWarningMessage(this, "Vui lòng nhập năm hợp lệ");
        }
    }
    
    private void thongKeTheoThang() {
        try {
            int nam = Integer.parseInt(txtNamThongKe.getText().trim());
            var danhSach = thongKeDAO.thongKeTheoThang(nam);
            int stt = 1;
            for (var row : danhSach) {
                tableModelThongKe.addRow(new Object[]{
                    stt++,
                    "Tháng " + row.get("Thang") + "/" + nam,
                    String.format("%,d", row.get("TongVe")),
                    String.format("%,d", row.get("DoanhThu"))
                });
            }
        } catch (Exception e) {
            UIUtils.showWarningMessage(this, "Vui lòng nhập năm hợp lệ");
        }
    }
    
    private void thongKeTheoNam() {
        var danhSach = thongKeDAO.thongKeTheoNam();
        int stt = 1;
        for (var row : danhSach) {
            tableModelThongKe.addRow(new Object[]{
                stt++,
                "Năm " + row.get("Nam"),
                String.format("%,d", row.get("TongVe")),
                String.format("%,d", row.get("DoanhThu"))
            });
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new QuanLyVeThongKeForm().setVisible(true);
        });
    }
}