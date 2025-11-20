package gui;

import dao.VeDAO;
import dao.SuatChieuDAO;
import dao.ThongKeDAO;
import dao.PhimDAO;
import model.Ve;
import model.SuatChieu;
import model.Phim;
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
    private PhimDAO phimDAO;  // ← THÊM
    private DefaultTableModel tableModelVe, tableModelThongKe;
    
    private JTable tableVe, tableThongKe;
    private JTextField txtSoLuong;
    private JLabel lblGiaVe, lblThanhTien, lblThongTinPhim;  // ← THÊM lblThongTinPhim
    private JComboBox<String> cboPhim, cboSuatChieu, cboLoaiThongKe;  // ← THÊM cboPhim
    private JTextField txtNamThongKe, txtTuNgay, txtDenNgay;
    private JButton btnBanVe, btnLamMoi, btnCapNhatTK, btnLocThongKe;
    private JLabel lblTongVe, lblTongDoanhThu;
    
    public QuanLyVeThongKeForm() {
        veDAO = new VeDAO();
        suatChieuDAO = new SuatChieuDAO();
        thongKeDAO = new ThongKeDAO();
        phimDAO = new PhimDAO();  // ← THÊM
        
        initComponents();
        loadComboBoxPhim();  // ← Load phim trước
        loadDataVe();
        loadThongKe();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Quản Lý Vé & Thống Kê");
        setSize(1500, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // ========== TAB QUẢN LÝ VÉ ==========
        JPanel pnlQuanLyVe = new JPanel(new BorderLayout(10, 10));
        pnlQuanLyVe.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel form bán vé (Trái)
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("🎬 Bán Vé"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // ← BƯỚC 1: CHỌN PHIM
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblPhim = new JLabel("1️⃣ Chọn Phim: *");
        lblPhim.setFont(new Font("Arial", Font.BOLD, 13));
        pnlForm.add(lblPhim, gbc);
        gbc.gridx = 1;
        cboPhim = new JComboBox<>();
        cboPhim.setPreferredSize(new Dimension(300, 30));
        // ← Khi chọn phim, load suất chiếu của phim đó
        cboPhim.addActionListener(e -> loadSuatChieuTheoPhim());
        pnlForm.add(cboPhim, gbc);
        
        // Thông tin phim (thời lượng, thể loại)
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        lblThongTinPhim = new JLabel("<html><i>Chọn phim để xem thông tin</i></html>");
        lblThongTinPhim.setFont(new Font("Arial", Font.PLAIN, 11));
        lblThongTinPhim.setForeground(Color.GRAY);
        pnlForm.add(lblThongTinPhim, gbc);
        
        // ← BƯỚC 2: CHỌN SUẤT CHIẾU
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblSuat = new JLabel("2️⃣ Chọn Suất Chiếu: *");
        lblSuat.setFont(new Font("Arial", Font.BOLD, 13));
        pnlForm.add(lblSuat, gbc);
        gbc.gridx = 1;
        cboSuatChieu = new JComboBox<>();
        cboSuatChieu.setPreferredSize(new Dimension(300, 30));
        // Khi chọn suất chiếu, tự động hiển thị giá vé
        cboSuatChieu.addActionListener(e -> hienThiGiaVe());
        pnlForm.add(cboSuatChieu, gbc);
        
        // ← GIÁ VÉ (tự động hiển thị)
        gbc.gridx = 0; gbc.gridy = 3;
        pnlForm.add(new JLabel("💰 Giá Vé:"), gbc);
        gbc.gridx = 1;
        lblGiaVe = new JLabel("0 VNĐ");
        lblGiaVe.setFont(new Font("Arial", Font.BOLD, 18));
        lblGiaVe.setForeground(new Color(39, 174, 96));
        pnlForm.add(lblGiaVe, gbc);
        
        // ← BƯỚC 3: NHẬP SỐ LƯỢNG
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblSoLuong = new JLabel("3️⃣ Số Lượng Vé: *");
        lblSoLuong.setFont(new Font("Arial", Font.BOLD, 13));
        pnlForm.add(lblSoLuong, gbc);
        gbc.gridx = 1;
        JPanel pnlSoLuong = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        txtSoLuong = new JTextField(10);
        txtSoLuong.setText("1");
        txtSoLuong.setFont(new Font("Arial", Font.PLAIN, 14));
        // Tự động tính thành tiền khi thay đổi số lượng
        txtSoLuong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tinhThanhTien();
            }
        });
        
        // Nút tăng/giảm số lượng
        JButton btnGiam = new JButton("-");
        btnGiam.setPreferredSize(new Dimension(45, 30));
        btnGiam.addActionListener(e -> {
            try {
                int sl = Integer.parseInt(txtSoLuong.getText());
                if (sl > 1) {
                    txtSoLuong.setText(String.valueOf(sl - 1));
                    tinhThanhTien();
                }
            } catch (Exception ex) {}
        });
        
        JButton btnTang = new JButton("+");
        btnTang.setPreferredSize(new Dimension(45, 30));
        btnTang.addActionListener(e -> {
            try {
                int sl = Integer.parseInt(txtSoLuong.getText());
                txtSoLuong.setText(String.valueOf(sl + 1));
                tinhThanhTien();
            } catch (Exception ex) {}
        });
        
        pnlSoLuong.add(btnGiam);
        pnlSoLuong.add(txtSoLuong);
        pnlSoLuong.add(btnTang);
        pnlForm.add(pnlSoLuong, gbc);
        
        // ← THÀNH TIỀN (tự động tính)
        gbc.gridx = 0; gbc.gridy = 5;
        pnlForm.add(new JLabel("💵 Thành Tiền:"), gbc);
        gbc.gridx = 1;
        lblThanhTien = new JLabel("0 VNĐ");
        lblThanhTien.setFont(new Font("Arial", Font.BOLD, 22));
        lblThanhTien.setForeground(new Color(231, 76, 60));
        pnlForm.add(lblThanhTien, gbc);
        
        // Separator
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        pnlForm.add(new JSeparator(), gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        JPanel pnlButtons = new JPanel(new GridLayout(1, 2, 15, 0));
        btnBanVe = new JButton("💳 Bán Vé");
        btnLamMoi = new JButton("🔄 Làm Mới");
        
        btnBanVe.setBackground(new Color(46, 204, 113));
        btnBanVe.setForeground(Color.WHITE);
        btnBanVe.setFont(new Font("Arial", Font.BOLD, 16));
        btnBanVe.setPreferredSize(new Dimension(140, 45));
        
        btnLamMoi.setBackground(new Color(149, 165, 166));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFont(new Font("Arial", Font.BOLD, 14));
        btnLamMoi.setPreferredSize(new Dimension(140, 45));
        
        pnlButtons.add(btnBanVe);
        pnlButtons.add(btnLamMoi);
        pnlForm.add(pnlButtons, gbc);
        
        // Panel table vé (Phải)
        JPanel pnlTableVe = new JPanel(new BorderLayout());
        pnlTableVe.setBorder(BorderFactory.createTitledBorder("📋 Danh Sách Vé Đã Bán"));
        
        String[] columnNamesVe = {"Mã Vé", "Mã Suất", "Tên Phim", "Phòng", 
                                  "Ngày Chiếu", "Giờ", "Giá Vé", "SL", "Thành Tiền"};
        tableModelVe = new DefaultTableModel(columnNamesVe, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableVe = new JTable(tableModelVe);
        tableVe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableVe.setFont(new Font("Arial", Font.PLAIN, 12));
        tableVe.setRowHeight(25);
        JScrollPane scrollVe = new JScrollPane(tableVe);
        pnlTableVe.add(scrollVe, BorderLayout.CENTER);
        
        pnlQuanLyVe.add(pnlForm, BorderLayout.WEST);
        pnlQuanLyVe.add(pnlTableVe, BorderLayout.CENTER);
        
        // ========== TAB THỐNG KÊ ==========
        JPanel pnlThongKe = new JPanel(new BorderLayout(10, 10));
        pnlThongKe.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel header với bộ lọc
        JPanel pnlHeaderTK = new JPanel(new BorderLayout());
        
        // Thống kê tổng quan
        JPanel pnlStats = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        pnlStats.setBorder(BorderFactory.createTitledBorder("📊 Tổng Quan"));
        
        lblTongVe = new JLabel("Tổng số vé: 0");
        lblTongVe.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongVe.setForeground(new Color(41, 128, 185));
        
        lblTongDoanhThu = new JLabel("Tổng doanh thu: 0 VNĐ");
        lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongDoanhThu.setForeground(new Color(39, 174, 96));
        
        btnCapNhatTK = new JButton("🔄 Cập Nhật");
        btnCapNhatTK.setBackground(new Color(52, 152, 219));
        btnCapNhatTK.setForeground(Color.WHITE);
        btnCapNhatTK.setFont(new Font("Arial", Font.BOLD, 12));
        
        pnlStats.add(lblTongVe);
        pnlStats.add(new JLabel("|"));
        pnlStats.add(lblTongDoanhThu);
        pnlStats.add(btnCapNhatTK);
        
        // Bộ lọc thống kê
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlFilter.setBorder(BorderFactory.createTitledBorder("🔍 Bộ Lọc"));
        
        pnlFilter.add(new JLabel("Loại:"));
        cboLoaiThongKe = new JComboBox<>(new String[]{
            "Theo Phim", "Theo Ngày", "Theo Tuần", "Theo Tháng", "Theo Năm"
        });
        pnlFilter.add(cboLoaiThongKe);
        
        pnlFilter.add(new JLabel("  Từ ngày:"));
        txtTuNgay = new JTextField(10);
        txtTuNgay.setToolTipText("yyyy-MM-dd");
        pnlFilter.add(txtTuNgay);
        
        pnlFilter.add(new JLabel("Đến ngày:"));
        txtDenNgay = new JTextField(10);
        txtDenNgay.setToolTipText("yyyy-MM-dd");
        pnlFilter.add(txtDenNgay);
        
        pnlFilter.add(new JLabel("  Năm:"));
        txtNamThongKe = new JTextField(6);
        txtNamThongKe.setText("2024");
        pnlFilter.add(txtNamThongKe);
        
        btnLocThongKe = new JButton("📊 Lọc");
        btnLocThongKe.setBackground(new Color(241, 196, 15));
        pnlFilter.add(btnLocThongKe);
        
        JPanel pnlTop = new JPanel(new GridLayout(2, 1));
        pnlTop.add(pnlStats);
        pnlTop.add(pnlFilter);
        pnlHeaderTK.add(pnlTop, BorderLayout.CENTER);
        pnlThongKe.add(pnlHeaderTK, BorderLayout.NORTH);
        
        // Table thống kê
        JPanel pnlTableTK = new JPanel(new BorderLayout());
        pnlTableTK.setBorder(BorderFactory.createTitledBorder("📈 Kết Quả Thống Kê"));
        
        String[] columnNamesTK = {"STT", "Tiêu Chí", "Số Vé Bán", "Doanh Thu (VNĐ)"};
        tableModelThongKe = new DefaultTableModel(columnNamesTK, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableThongKe = new JTable(tableModelThongKe);
        tableThongKe.setFont(new Font("Arial", Font.PLAIN, 12));
        tableThongKe.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableThongKe.setRowHeight(25);
        JScrollPane scrollTK = new JScrollPane(tableThongKe);
        pnlTableTK.add(scrollTK, BorderLayout.CENTER);
        pnlThongKe.add(pnlTableTK, BorderLayout.CENTER);
        
        // Add tabs
        tabbedPane.addTab("🎫 Quản Lý Vé", pnlQuanLyVe);
        tabbedPane.addTab("📊 Thống Kê", pnlThongKe);
        add(tabbedPane);
        
        // Event handlers
        btnBanVe.addActionListener(e -> banVe());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnCapNhatTK.addActionListener(e -> loadThongKe());
        btnLocThongKe.addActionListener(e -> locThongKe());
    }
    
    // ← MỚI: Load danh sách phim
    private void loadComboBoxPhim() {
        try {
            cboPhim.removeAllItems();
            List<Phim> danhSachPhim = phimDAO.layDanhSachPhim();
            
            if (danhSachPhim.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Chưa có phim nào!", 
                    "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            cboPhim.addItem("-- Chọn phim --");  // Item mặc định
            for (Phim phim : danhSachPhim) {
                // Format: MaPhim - TenPhim (TheLoai, ThoiLuong phút)
                String item = String.format("%d - %s (%s, %d phút)", 
                    phim.getMaPhim(), 
                    phim.getTenPhim(),
                    phim.getTheLoai() != null ? phim.getTheLoai() : "N/A",
                    phim.getThoiLuong());
                cboPhim.addItem(item);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi load danh sách phim: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ← MỚI: Load suất chiếu theo phim đã chọn
    private void loadSuatChieuTheoPhim() {
        try {
            cboSuatChieu.removeAllItems();
            lblGiaVe.setText("0 VNĐ");
            lblThanhTien.setText("0 VNĐ");
            lblThongTinPhim.setText("<html><i>Chọn phim để xem thông tin</i></html>");
            
            if (cboPhim.getSelectedIndex() == 0) return;  // "-- Chọn phim --"
            
            int maPhim = getMaPhimFromCombo();
            if (maPhim == 0) return;
            
            // Hiển thị thông tin phim
            Phim phim = phimDAO.layPhimTheoMa(maPhim);
            if (phim != null) {
                lblThongTinPhim.setText(String.format(
                    "<html><b>%s</b><br/>Thể loại: %s | Thời lượng: %d phút</html>",
                    phim.getTenPhim(),
                    phim.getTheLoai() != null ? phim.getTheLoai() : "N/A",
                    phim.getThoiLuong()
                ));
            }
            
            // Load suất chiếu của phim này
            List<SuatChieu> danhSach = suatChieuDAO.layDanhSachSuatChieu();
            boolean coSuatChieu = false;
            
            for (SuatChieu sc : danhSach) {
                if (sc.getMaPhim() == maPhim) {
                    coSuatChieu = true;
                    int conTrong = sc.getSoGhe() - sc.getSoVeDaBan();
                    // Format: Ngày Giờ (Phòng) - GiaVe VNĐ | Còn X ghế
                    String item = String.format("%d - %s %s (%s) - %,d VNĐ | Còn %d ghế", 
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
                JOptionPane.showMessageDialog(this, 
                    "Phim này chưa có suất chiếu nào!", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi load suất chiếu: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ← Lấy mã phim từ combo
    private int getMaPhimFromCombo() {
        try {
            String selected = (String) cboPhim.getSelectedItem();
            if (selected != null && !selected.startsWith("--") && selected.contains(" - ")) {
                return Integer.parseInt(selected.split(" - ")[0].trim());
            }
        } catch (Exception e) {
            System.err.println("Lỗi getMaPhimFromCombo: " + e.getMessage());
        }
        return 0;
    }
    
    // Hiển thị giá vé khi chọn suất chiếu
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
            System.err.println("Lỗi hiển thị giá vé: " + e.getMessage());
        }
    }
    
    // Tính thành tiền tự động
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
            JOptionPane.showMessageDialog(this, 
                "Lỗi load danh sách vé: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadThongKe() {
        try {
            cboLoaiThongKe.setSelectedIndex(0);
            locThongKe();
            
            int tongVe = thongKeDAO.demTongSoVe();
            long tongDoanhThu = thongKeDAO.tinhTongDoanhThu();
            
            lblTongVe.setText(String.format("Tổng số vé: %,d", tongVe));
            lblTongDoanhThu.setText(String.format("Tổng doanh thu: %,d VNĐ", tongDoanhThu));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi load thống kê: " + e.getMessage());
            e.printStackTrace();
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
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            e.printStackTrace();
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
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng ngày (yyyy-MM-dd)");
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
            JOptionPane.showMessageDialog(this, "Vui lòng nhập năm hợp lệ");
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
            JOptionPane.showMessageDialog(this, "Vui lòng nhập năm hợp lệ");
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
    
    private int getMaSuatFromCombo() {
        try {
            String selected = (String) cboSuatChieu.getSelectedItem();
            if (selected != null && !selected.startsWith("--") && selected.contains(" - ")) {
                return Integer.parseInt(selected.split(" - ")[0].trim());
            }
        } catch (Exception e) {
            System.err.println("Lỗi getMaSuatFromCombo: " + e.getMessage());
        }
        return 0;
    }
    
    private boolean validateInput() {
        if (cboPhim.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim!");
            return false;
        }
        
        if (cboSuatChieu.getSelectedItem() == null || 
            cboSuatChieu.getSelectedItem().toString().startsWith("--")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn suất chiếu!");
            return false;
        }
        
        try {
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải > 0!");
                txtSoLuong.requestFocus();
                return false;
            }
            
            // Kiểm tra còn ghế trống không
            int maSuat = getMaSuatFromCombo();
            if (!veDAO.kiemTraConGheTrong(maSuat, soLuong)) {
                JOptionPane.showMessageDialog(this, 
                    "⚠️ Không đủ ghế trống!\nVui lòng giảm số lượng.",
                    "Hết chỗ",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!");
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
                JOptionPane.showMessageDialog(this, "Bán vé thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
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
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuanLyVeThongKeForm().setVisible(true);
        });
    }
}