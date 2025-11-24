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

/**
 * ========== QUANLYVETHONGKEFORM - PHIÊN BẢN MỚI ==========
 * 
 * THAY ĐỔI:
 * - BỎ: Bảng "Danh sách vé đã bán" ở tab Bán Vé
 * - THÊM: Bảng "Danh sách suất chiếu" để khách chọn trực tiếp
 * 
 * FLOW MỚI:
 * 1. Chọn phim từ ComboBox
 * 2. Hiển thị TABLE các suất chiếu của phim đó
 * 3. Click vào suất chiếu trong table → Thông tin tự điền vào form
 * 4. Nhập số lượng vé
 * 5. Bán vé
 */
public class QuanLyVeThongKeForm extends JFrame {
    private VeDAO veDAO;
    private SuatChieuDAO suatChieuDAO;
    private ThongKeDAO thongKeDAO;
    private PhimDAO phimDAO;
    
    // ===== TAB BÁN VÉ =====
    private DefaultTableModel tableModelSuatChieu; // TABLE: Hiển thị suất chiếu
    private JTable tableSuatChieu;
    
    private JTextField txtSoLuong;
    private JLabel lblGiaVe, lblThanhTien, lblThongTinPhim;
    private JComboBox<String> cboPhim;
    private JButton btnBanVe, btnLamMoi;
    
    // Lưu suất chiếu đang chọn
    private SuatChieu suatChieuDangChon = null;
    
    // ===== TAB THỐNG KÊ =====
    private DefaultTableModel tableModelThongKe;
    private JTable tableThongKe;
    private JComboBox<String> cboLoaiThongKe;
    private JTextField txtNamThongKe, txtTuNgay, txtDenNgay;
    private JButton btnCapNhatTK, btnLocThongKe;
    private JLabel lblTongVe, lblTongDoanhThu;
    
    public QuanLyVeThongKeForm() {
        veDAO = new VeDAO();
        suatChieuDAO = new SuatChieuDAO();
        thongKeDAO = new ThongKeDAO();
        phimDAO = new PhimDAO();
        
        initComponents();
        loadComboBoxPhim();
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
        
        // ===== TAB 1: BÁN VÉ (ĐÃ SỬA) =====
        tabbedPane.addTab("  🎫 Bán Vé  ", createBanVePanel());
        
        // ===== TAB 2: THỐNG KÊ =====
        tabbedPane.addTab("  📊 Thống Kê  ", 
            new JScrollPane(createThongKePanel(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
        );
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }
    
    // ========== TAB BÁN VÉ (PHIÊN BẢN MỚI) ==========
    /**
     * Tạo panel bán vé với layout mới:
     * - TRÁI: Form nhập liệu (chọn phim, số lượng)
     * - PHẢI: Bảng danh sách suất chiếu (thay vì bảng vé đã bán)
     */
    private JPanel createBanVePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // ===== LEFT: Form bán vé =====
        JPanel pnlFormWrapper = createFormBanVe();
        JScrollPane scrollForm = new JScrollPane(pnlFormWrapper);
        scrollForm.setBorder(null);
        scrollForm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollForm.setPreferredSize(new Dimension(400, 0)); // Thu nhỏ form lại
        
        // ===== CENTER: Bảng suất chiếu =====
        JPanel pnlTableSuatChieu = createTableSuatChieu();
        
        panel.add(scrollForm, BorderLayout.WEST);
        panel.add(pnlTableSuatChieu, BorderLayout.CENTER); // Bảng chiếm phần lớn
        
        return panel;
    }
    
    /**
     * ===== FORM BÁN VÉ (SIMPLIFIED) =====
     * Bỏ ComboBox suất chiếu, thay bằng chọn từ table
     */
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
        
        // ===== BƯỚC 1: Chọn phim =====
        gbc.gridy = 0;
        JLabel lblStep1 = new JLabel("1️⃣ Chọn Phim *");
        lblStep1.setFont(UIUtils.FONT_SUBHEADING);
        lblStep1.setForeground(new Color(99, 102, 241));
        pnlForm.add(lblStep1, gbc);
        
        gbc.gridy = 1;
        cboPhim = new JComboBox<>();
        UIUtils.styleComboBox(cboPhim);
        cboPhim.setPreferredSize(new Dimension(0, 40));
        cboPhim.addActionListener(e -> loadSuatChieuTheoPhim()); // Load table khi chọn phim
        pnlForm.add(cboPhim, gbc);
        
        // Thông tin phim
        gbc.gridy = 2;
        lblThongTinPhim = new JLabel("<html><i>Chọn phim để xem suất chiếu</i></html>");
        lblThongTinPhim.setFont(UIUtils.FONT_SMALL);
        lblThongTinPhim.setForeground(UIUtils.TEXT_MUTED);
        pnlForm.add(lblThongTinPhim, gbc);
        
        // ===== BƯỚC 2: Chọn suất chiếu từ table =====
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 10, 0);
        JLabel lblStep2 = new JLabel("2️⃣ Chọn Suất Từ Bảng Bên Phải →");
        lblStep2.setFont(UIUtils.FONT_SUBHEADING);
        lblStep2.setForeground(new Color(34, 197, 94));
        pnlForm.add(lblStep2, gbc);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 10, 0);
        JLabel lblHint = new JLabel("<html><i>💡 Click vào dòng suất chiếu muốn mua</i></html>");
        lblHint.setFont(UIUtils.FONT_SMALL);
        lblHint.setForeground(UIUtils.TEXT_MUTED);
        pnlForm.add(lblHint, gbc);
        
        // Giá vé (hiển thị khi chọn suất)
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
        
        // ===== BƯỚC 3: Số lượng =====
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 0, 10, 0);
        JLabel lblStep3 = new JLabel("3️⃣ Số Lượng Vé *");
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
        JPanel pnlButtons = new JPanel(new GridLayout(2, 1, 0, 12));
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
    
    /**
     * ===== TẠO BẢNG SUẤT CHIẾU =====
     * Đây là thay đổi CHÍNH: Thay bảng vé đã bán bằng bảng suất chiếu
     */
    private JPanel createTableSuatChieu() {
        JPanel wrapper = UIUtils.createCardPanel();
        wrapper.setLayout(new BorderLayout(0, 15));
        
        // Title
        JLabel lblTitle = new JLabel("📋 Danh Sách Suất Chiếu");
        lblTitle.setFont(UIUtils.FONT_HEADING);
        lblTitle.setForeground(UIUtils.TEXT_PRIMARY);
        wrapper.add(lblTitle, BorderLayout.NORTH);
        
        // Table với các cột: Mã, Ngày, Giờ, Phòng, Giá vé, Còn trống
        String[] columnNames = {"Mã Suất", "Ngày Chiếu", "Giờ Chiếu", 
                                "Phòng", "Giá Vé (VNĐ)", "Ghế Trống"};
        
        tableModelSuatChieu = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableSuatChieu = new JTable(tableModelSuatChieu);
        tableSuatChieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UIUtils.styleTable(tableSuatChieu);
        
        // Set column widths
        tableSuatChieu.getColumnModel().getColumn(0).setPreferredWidth(80);   // Mã
        tableSuatChieu.getColumnModel().getColumn(1).setPreferredWidth(120);  // Ngày
        tableSuatChieu.getColumnModel().getColumn(2).setPreferredWidth(100);  // Giờ
        tableSuatChieu.getColumnModel().getColumn(3).setPreferredWidth(120);  // Phòng
        tableSuatChieu.getColumnModel().getColumn(4).setPreferredWidth(120);  // Giá
        tableSuatChieu.getColumnModel().getColumn(5).setPreferredWidth(100);  // Ghế trống
        
        JScrollPane scrollTable = new JScrollPane(tableSuatChieu);
        scrollTable.setBorder(BorderFactory.createLineBorder(UIUtils.BORDER_COLOR));
        wrapper.add(scrollTable, BorderLayout.CENTER);
        
        // ===== SỰ KIỆN: Click vào dòng → Chọn suất chiếu =====
        tableSuatChieu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                chonSuatChieuTuTable();
            }
        });
        
        return wrapper;
    }
    
    // ========== METHODS XỬ LÝ ==========
    
    /**
     * Load danh sách phim vào ComboBox
     */
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
    
    /**
     * ===== LOAD SUẤT CHIẾU THEO PHIM =====
     * Khi user chọn phim → Hiển thị các suất chiếu lên TABLE
     */
    private void loadSuatChieuTheoPhim() {
        try {
            // Xóa table
            tableModelSuatChieu.setRowCount(0);
            
            // Reset form
            lblGiaVe.setText("0 VNĐ");
            lblThanhTien.setText("0 VNĐ");
            suatChieuDangChon = null;
            
            // Kiểm tra đã chọn phim chưa
            if (cboPhim.getSelectedIndex() == 0) {
                lblThongTinPhim.setText("<html><i>Chọn phim để xem suất chiếu</i></html>");
                return;
            }
            
            int maPhim = getMaPhimFromCombo();
            if (maPhim == 0) return;
            
            // Hiển thị thông tin phim
            Phim phim = phimDAO.layPhimTheoMa(maPhim);
            if (phim != null) {
                lblThongTinPhim.setText(String.format(
                    "<html><b>%s</b> | %s | %d phút</html>",
                    phim.getTenPhim(),
                    phim.getTheLoai() != null ? phim.getTheLoai() : "N/A",
                    phim.getThoiLuong()
                ));
            }
            
            // Lấy danh sách TOÀN BỘ suất chiếu
            List<SuatChieu> danhSach = suatChieuDAO.layDanhSachSuatChieu();
            boolean coSuatChieu = false;
            
            // Lọc các suất chiếu của phim này
            for (SuatChieu sc : danhSach) {
                if (sc.getMaPhim() == maPhim) {
                    coSuatChieu = true;
                    int conTrong = sc.getSoGhe() - sc.getSoVeDaBan();
                    
                    // Thêm vào table
                    Object[] row = {
                        sc.getMaSuat(),                      // Mã suất
                        sc.getNgayChieu(),                   // Ngày
                        sc.getGioChieu(),                    // Giờ
                        sc.getTenPhong(),                    // Phòng
                        String.format("%,d", sc.getGiaVe()), // Giá vé
                        conTrong + " ghế"                    // Ghế trống
                    };
                    tableModelSuatChieu.addRow(row);
                }
            }
            
            if (!coSuatChieu) {
                UIUtils.showWarningMessage(this, "Phim này chưa có suất chiếu!");
            }
        } catch (Exception e) {
            UIUtils.showErrorMessage(this, "Lỗi: " + e.getMessage());
        }
    }
    
    /**
     * ===== CHỌN SUẤT CHIẾU TỪ TABLE =====
     * Khi user click vào dòng trong table
     */
    private void chonSuatChieuTuTable() {
        int selectedRow = tableSuatChieu.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                // Lấy mã suất từ cột 0
                int maSuat = (int) tableModelSuatChieu.getValueAt(selectedRow, 0);
                
                // Lấy thông tin đầy đủ suất chiếu từ DAO
                suatChieuDangChon = suatChieuDAO.laySuatChieuTheoMa(maSuat);
                
                if (suatChieuDangChon != null) {
                    // Hiển thị giá vé
                    lblGiaVe.setText(String.format("%,d VNĐ", suatChieuDangChon.getGiaVe()));
                    
                    // Tính thành tiền
                    tinhThanhTien();
                    
                    // Thông báo đã chọn (optional)
                    System.out.println("Đã chọn suất chiếu: " + maSuat);
                }
            } catch (Exception e) {
                System.err.println("Lỗi chọn suất chiếu: " + e.getMessage());
            }
        }
    }
    
    /**
     * Tính thành tiền
     */
    private void tinhThanhTien() {
        try {
            if (suatChieuDangChon == null) {
                lblThanhTien.setText("0 VNĐ");
                return;
            }
            
            int giaVe = suatChieuDangChon.getGiaVe();
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            
            long thanhTien = (long) giaVe * soLuong;
            lblThanhTien.setText(String.format("%,d VNĐ", thanhTien));
        } catch (Exception e) {
            lblThanhTien.setText("0 VNĐ");
        }
    }
    
    /**
     * Lấy mã phim từ ComboBox
     */
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
    
    /**
     * Validate input
     */
    private boolean validateInput() {
        if (cboPhim.getSelectedIndex() == 0) {
            UIUtils.showWarningMessage(this, "Vui lòng chọn phim!");
            return false;
        }
        
        if (suatChieuDangChon == null) {
            UIUtils.showWarningMessage(this, "Vui lòng chọn suất chiếu từ bảng!");
            return false;
        }
        
        try {
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) {
                UIUtils.showWarningMessage(this, "Số lượng phải > 0!");
                txtSoLuong.requestFocus();
                return false;
            }
            
            // Kiểm tra ghế trống
            if (!veDAO.kiemTraConGheTrong(suatChieuDangChon.getMaSuat(), soLuong)) {
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
    
    /**
     * ===== BÁN VÉ =====
     */
    private void banVe() {
        if (!validateInput()) return;
        
        try {
            // Lấy thông tin từ suất chiếu đang chọn
            if (suatChieuDangChon == null) {
                UIUtils.showWarningMessage(this, "Vui lòng chọn suất chiếu từ bảng!");
                return;
            }
            
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            
            // Tạo vé mới
            Ve ve = new Ve();
            ve.setMaSuat(suatChieuDangChon.getMaSuat());
            ve.setGiaVe(suatChieuDangChon.getGiaVe());
            ve.setSoLuong(soLuong);
            
            // Lưu vào database
            if (veDAO.banVe(ve)) {
                int thanhTien = ve.getGiaVe() * ve.getSoLuong();
                
                // Hiển thị thông báo chi tiết
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
                    suatChieuDangChon.getTenPhim(),
                    suatChieuDangChon.getNgayChieu(),
                    suatChieuDangChon.getGioChieu(),
                    suatChieuDangChon.getTenPhong(),
                    soLuong,
                    ve.getGiaVe(),
                    thanhTien
                );
                
                JOptionPane.showMessageDialog(this, 
                    thongBao, 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh lại dữ liệu
                lamMoi();
                loadThongKe(); // Cập nhật thống kê
            } else {
                UIUtils.showErrorMessage(this, "Bán vé thất bại!");
            }
        } catch (Exception ex) {
            UIUtils.showErrorMessage(this, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * ===== LÀM MỚI FORM =====
     */
    private void lamMoi() {
        // Reset form
        txtSoLuong.setText("1");
        lblGiaVe.setText("0 VNĐ");
        lblThanhTien.setText("0 VNĐ");
        lblThongTinPhim.setText("<html><i>Chọn phim để xem suất chiếu</i></html>");
        
        // Reset combo và table
        cboPhim.setSelectedIndex(0);
        tableModelSuatChieu.setRowCount(0);
        suatChieuDangChon = null;
        
        // Reload data
        loadComboBoxPhim();
        loadThongKe();
    }
    
    // ========== TAB THỐNG KÊ ==========
    
    /**
     * Tạo panel thống kê
     */
    private JPanel createThongKePanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // TOP: Stats cards (Tổng vé, Tổng doanh thu)
        JPanel pnlStats = createStatsPanel();
        panel.add(pnlStats, BorderLayout.NORTH);
        
        // CENTER: Table + Filter
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setOpaque(false);
        
        // Filter panel
        JPanel pnlFilter = createFilterPanel();
        pnlCenter.add(pnlFilter, BorderLayout.NORTH);
        
        // Table thống kê
        JPanel pnlTable = createTableThongKe();
        pnlCenter.add(pnlTable, BorderLayout.CENTER);
        
        panel.add(pnlCenter, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Panel hiển thị tổng số liệu
     */
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(0, 120));
        
        // Card 1: Tổng vé đã bán
        JPanel card1 = UIUtils.createStatsCard(
            "TỔNG SỐ VÉ ĐÃ BÁN",
            "0",
            "🎫",
            new Color(59, 130, 246)
        );
        panel.add(card1);
        
        // Card 2: Tổng doanh thu
        JPanel card2 = UIUtils.createStatsCard(
            "TỔNG DOANH THU",
            "0 VNĐ",
            "💰",
            new Color(34, 197, 94)
        );
        panel.add(card2);
        
        // Lưu reference để có thể update sau
        lblTongVe = (JLabel) ((JPanel)card1.getComponent(1)).getComponent(1);
        lblTongDoanhThu = (JLabel) ((JPanel)card2.getComponent(1)).getComponent(1);
        
        return panel;
    }
    
    /**
     * Panel bộ lọc thống kê
     */
    private JPanel createFilterPanel() {
        JPanel wrapper = UIUtils.createCardPanel();
        wrapper.setLayout(new BorderLayout(15, 0));
        
        JLabel lblTitle = new JLabel("🔍 Bộ Lọc Thống Kê");
        lblTitle.setFont(UIUtils.FONT_SUBHEADING);
        lblTitle.setForeground(UIUtils.TEXT_PRIMARY);
        wrapper.add(lblTitle, BorderLayout.WEST);
        
        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        pnlControls.setBackground(Color.WHITE);
        
        // Loại thống kê
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
        
        // Từ ngày
        JLabel lblTuNgay = new JLabel("Từ ngày:");
        lblTuNgay.setFont(UIUtils.FONT_BODY);
        pnlControls.add(lblTuNgay);
        
        txtTuNgay = new JTextField(10);
        UIUtils.styleTextField(txtTuNgay);
        txtTuNgay.setPreferredSize(new Dimension(120, 36));
        pnlControls.add(txtTuNgay);
        
        // Đến ngày
        JLabel lblDenNgay = new JLabel("Đến:");
        lblDenNgay.setFont(UIUtils.FONT_BODY);
        pnlControls.add(lblDenNgay);
        
        txtDenNgay = new JTextField(10);
        UIUtils.styleTextField(txtDenNgay);
        txtDenNgay.setPreferredSize(new Dimension(120, 36));
        pnlControls.add(txtDenNgay);
        
        pnlControls.add(new JLabel("  "));
        
        // Năm
        JLabel lblNam = new JLabel("Năm:");
        lblNam.setFont(UIUtils.FONT_BODY);
        pnlControls.add(lblNam);
        
        txtNamThongKe = new JTextField("2024", 6);
        UIUtils.styleTextField(txtNamThongKe);
        txtNamThongKe.setPreferredSize(new Dimension(80, 36));
        pnlControls.add(txtNamThongKe);
        
        // Buttons
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
    
    /**
     * Bảng hiển thị thống kê
     */
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
        
        // Set column widths
        tableThongKe.getColumnModel().getColumn(0).setPreferredWidth(60);
        tableThongKe.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableThongKe.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableThongKe.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        JScrollPane scrollTK = new JScrollPane(tableThongKe);
        scrollTK.setBorder(BorderFactory.createLineBorder(UIUtils.BORDER_COLOR));
        wrapper.add(scrollTK, BorderLayout.CENTER);
        
        return wrapper;
    }
    
    /**
     * Load dữ liệu thống kê
     */
    private void loadThongKe() {
        try {
            // Mặc định hiển thị thống kê theo phim
            cboLoaiThongKe.setSelectedIndex(0);
            locThongKe();
            
            // Cập nhật tổng số
            int tongVe = thongKeDAO.demTongSoVe();
            long tongDoanhThu = thongKeDAO.tinhTongDoanhThu();
            
            lblTongVe.setText(String.format("%,d", tongVe));
            lblTongDoanhThu.setText(String.format("%,d VNĐ", tongDoanhThu));
        } catch (Exception e) {
            UIUtils.showErrorMessage(this, "Lỗi load thống kê: " + e.getMessage());
        }
    }
    
    /**
     * Lọc thống kê theo loại
     */
    private void locThongKe() {
        try {
            tableModelThongKe.setRowCount(0);
            String loai = (String) cboLoaiThongKe.getSelectedItem();
            
            switch (loai) {
                case "Theo Phim":
                    thongKeTheoPhim();
                    break;
                case "Theo Ngày":
                    thongKeTheoNgay();
                    break;
                case "Theo Tuần":
                    thongKeTheoTuan();
                    break;
                case "Theo Tháng":
                    thongKeTheoThang();
                    break;
                case "Theo Năm":
                    thongKeTheoNam();
                    break;
            }
        } catch (Exception e) {
            UIUtils.showErrorMessage(this, "Lỗi lọc thống kê: " + e.getMessage());
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
            
            List<model.ThongKe> danhSach = thongKeDAO.thongKeTheoNgay(tuNgay, denNgay);
            int stt = 1;
            for (model.ThongKe tk : danhSach) {
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
            List<Map<String, Object>> danhSach = thongKeDAO.thongKeTheoTuan(nam);
            int stt = 1;
            for (Map<String, Object> row : danhSach) {
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
            List<Map<String, Object>> danhSach = thongKeDAO.thongKeTheoThang(nam);
            int stt = 1;
            for (Map<String, Object> row : danhSach) {
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
        List<Map<String, Object>> danhSach = thongKeDAO.thongKeTheoNam();
        int stt = 1;
        for (Map<String, Object> row : danhSach) {
            tableModelThongKe.addRow(new Object[]{
                stt++,
                "Năm " + row.get("Nam"),
                String.format("%,d", row.get("TongVe")),
                String.format("%,d", row.get("DoanhThu"))
            });
        }
    }
    
    // ========== MAIN ==========
    
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