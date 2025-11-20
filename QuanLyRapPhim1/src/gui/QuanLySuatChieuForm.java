package gui;

import dao.SuatChieuDAO;
import dao.PhimDAO;
import model.SuatChieu;
import model.Phim;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.List;

public class QuanLySuatChieuForm extends JFrame {
    private SuatChieuDAO suatChieuDAO;
    private PhimDAO phimDAO;
    private DefaultTableModel tableModel;
    
    private JTable tableSuatChieu;
    private JTextField txtMaSuat, txtNgayChieu, txtGioChieu, txtLocNgay, txtGiaVe;  // ← THÊM txtGiaVe
    private JComboBox<String> cboPhim, cboPhong;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnLocNgay;
    
    public QuanLySuatChieuForm() {
        suatChieuDAO = new SuatChieuDAO();
        phimDAO = new PhimDAO();
        initComponents();
        loadComboBoxData();
        loadDataToTable();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Quản Lý Suất Chiếu");
        setSize(1300, 680);  // Tăng width để chứa cột Giá Vé
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Panel form (Trái)
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông Tin Suất Chiếu"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Mã suất
        gbc.gridx = 0; gbc.gridy = 0;
        pnlForm.add(new JLabel("Mã Suất:"), gbc);
        gbc.gridx = 1;
        txtMaSuat = new JTextField(20);
        txtMaSuat.setEditable(false);
        txtMaSuat.setBackground(Color.LIGHT_GRAY);
        pnlForm.add(txtMaSuat, gbc);
        
        // Phim
        gbc.gridx = 0; gbc.gridy = 1;
        pnlForm.add(new JLabel("Phim: *"), gbc);
        gbc.gridx = 1;
        cboPhim = new JComboBox<>();
        pnlForm.add(cboPhim, gbc);
        
        // Phòng chiếu
        gbc.gridx = 0; gbc.gridy = 2;
        pnlForm.add(new JLabel("Phòng: *"), gbc);
        gbc.gridx = 1;
        cboPhong = new JComboBox<>();
        // ← THÊM: Tự động điền giá vé khi chọn phòng
        cboPhong.addActionListener(e -> tuDongDienGiaVe());
        pnlForm.add(cboPhong, gbc);
        
        // Ngày chiếu
        gbc.gridx = 0; gbc.gridy = 3;
        pnlForm.add(new JLabel("Ngày Chiếu: *"), gbc);
        gbc.gridx = 1;
        JPanel pnlNgay = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        txtNgayChieu = new JTextField(15);
        JLabel lblFormat1 = new JLabel(" (yyyy-MM-dd)");
        lblFormat1.setFont(new Font("Arial", Font.ITALIC, 10));
        pnlNgay.add(txtNgayChieu);
        pnlNgay.add(lblFormat1);
        pnlForm.add(pnlNgay, gbc);
        
        // Giờ chiếu
        gbc.gridx = 0; gbc.gridy = 4;
        pnlForm.add(new JLabel("Giờ Chiếu: *"), gbc);
        gbc.gridx = 1;
        JPanel pnlGio = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        txtGioChieu = new JTextField(15);
        JLabel lblFormat2 = new JLabel(" (HH:mm:ss)");
        lblFormat2.setFont(new Font("Arial", Font.ITALIC, 10));
        pnlGio.add(txtGioChieu);
        pnlGio.add(lblFormat2);
        pnlForm.add(pnlGio, gbc);
        
        // ← THÊM: Giá vé
        gbc.gridx = 0; gbc.gridy = 5;
        pnlForm.add(new JLabel("Giá Vé (VNĐ): *"), gbc);
        gbc.gridx = 1;
        txtGiaVe = new JTextField(20);
        pnlForm.add(txtGiaVe, gbc);
        
        // Gợi ý giá
        gbc.gridx = 1; gbc.gridy = 6;
        JLabel lblGoiY = new JLabel("<html><i>Gợi ý: Phòng thường: 75000-80000<br/>Phòng VIP: 120000</i></html>");
        lblGoiY.setFont(new Font("Arial", Font.PLAIN, 10));
        lblGoiY.setForeground(Color.GRAY);
        pnlForm.add(lblGoiY, gbc);
        
        // Panel buttons
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        JPanel pnlButtons = new JPanel(new GridLayout(2, 2, 10, 10));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm Mới");
        
        btnThem.setBackground(new Color(46, 204, 113));
        btnThem.setForeground(Color.WHITE);
        btnSua.setBackground(new Color(52, 152, 219));
        btnSua.setForeground(Color.WHITE);
        btnXoa.setBackground(new Color(231, 76, 60));
        btnXoa.setForeground(Color.WHITE);
        btnLamMoi.setBackground(new Color(149, 165, 166));
        btnLamMoi.setForeground(Color.WHITE);
        
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);
        pnlForm.add(pnlButtons, gbc);
        
        // Panel table (Phải)
        JPanel pnlTable = new JPanel(new BorderLayout(5, 5));
        pnlTable.setBorder(BorderFactory.createTitledBorder("Danh Sách Suất Chiếu"));
        
        // Panel lọc
        JPanel pnlLoc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlLoc.add(new JLabel("Lọc theo ngày:"));
        txtLocNgay = new JTextField(15);
        pnlLoc.add(txtLocNgay);
        JLabel lblFormatLoc = new JLabel("(yyyy-MM-dd)");
        lblFormatLoc.setFont(new Font("Arial", Font.ITALIC, 10));
        pnlLoc.add(lblFormatLoc);
        btnLocNgay = new JButton("Lọc");
        btnLocNgay.setBackground(new Color(241, 196, 15));
        pnlLoc.add(btnLocNgay);
        JButton btnHuyLoc = new JButton("Hủy Lọc");
        pnlLoc.add(btnHuyLoc);
        pnlTable.add(pnlLoc, BorderLayout.NORTH);
        
        // Table - ← THÊM cột Giá Vé
        String[] columnNames = {"Mã Suất", "Tên Phim", "Phòng", "Ngày Chiếu", 
                                "Giờ Chiếu", "Giá Vé", "Vé Đã Bán", "Còn Trống"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSuatChieu = new JTable(tableModel);
        tableSuatChieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTable = new JScrollPane(tableSuatChieu);
        pnlTable.add(scrollTable, BorderLayout.CENTER);
        
        // Add panels
        add(pnlForm, BorderLayout.WEST);
        add(pnlTable, BorderLayout.CENTER);
        
        // Event handlers
        btnThem.addActionListener(e -> themSuatChieu());
        btnSua.addActionListener(e -> suaSuatChieu());
        btnXoa.addActionListener(e -> xoaSuatChieu());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnLocNgay.addActionListener(e -> locTheoNgay());
        btnHuyLoc.addActionListener(e -> {
            txtLocNgay.setText("");
            loadDataToTable();
        });
        
        tableSuatChieu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hienThiThongTin();
            }
        });
        
        txtLocNgay.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    locTheoNgay();
                }
            }
        });
    }
    
    // ← MỚI: Tự động điền giá vé khi chọn phòng
    private void tuDongDienGiaVe() {
        int maPhong = getMaPhongFromCombo();
        if (maPhong == 4) {  // Phòng VIP
            txtGiaVe.setText("120000");
        } else {  // Phòng thường
            txtGiaVe.setText("80000");
        }
    }
    
    private void loadComboBoxData() {
        try {
            // Load phim
            cboPhim.removeAllItems();
            List<Phim> danhSachPhim = phimDAO.layDanhSachPhim();
            if (danhSachPhim.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Chưa có phim nào!\nVui lòng thêm phim trước.", 
                    "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
            }
            for (Phim phim : danhSachPhim) {
                cboPhim.addItem(phim.getMaPhim() + " - " + phim.getTenPhim());
            }
            
            // Load phòng
            cboPhong.removeAllItems();
            cboPhong.addItem("1 - Phòng 1");
            cboPhong.addItem("2 - Phòng 2");
            cboPhong.addItem("3 - Phòng 3");
            cboPhong.addItem("4 - Phòng VIP");
            cboPhong.addItem("5 - Phòng 4");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi load dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadDataToTable() {
        try {
            tableModel.setRowCount(0);
            List<SuatChieu> danhSach = suatChieuDAO.layDanhSachSuatChieu();
            for (SuatChieu sc : danhSach) {
                int conTrong = sc.getSoGhe() - sc.getSoVeDaBan();
                Object[] row = {
                    sc.getMaSuat(),
                    sc.getTenPhim() != null ? sc.getTenPhim() : "N/A",
                    sc.getTenPhong() != null ? sc.getTenPhong() : "N/A",
                    sc.getNgayChieu(),
                    sc.getGioChieu(),
                    String.format("%,d VNĐ", sc.getGiaVe()),  // ← Hiển thị giá vé
                    sc.getSoVeDaBan(),
                    conTrong
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi load dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private int getMaPhimFromCombo() {
        try {
            String selected = (String) cboPhim.getSelectedItem();
            if (selected != null && selected.contains(" - ")) {
                return Integer.parseInt(selected.split(" - ")[0].trim());
            }
        } catch (Exception e) {
            System.err.println("Lỗi getMaPhimFromCombo: " + e.getMessage());
        }
        return 0;
    }
    
    private int getMaPhongFromCombo() {
        try {
            String selected = (String) cboPhong.getSelectedItem();
            if (selected != null && selected.contains(" - ")) {
                return Integer.parseInt(selected.split(" - ")[0].trim());
            }
        } catch (Exception e) {
            System.err.println("Lỗi getMaPhongFromCombo: " + e.getMessage());
        }
        return 0;
    }
    
    private boolean validateInput() {
        if (cboPhim.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim!");
            return false;
        }
        
        if (txtNgayChieu.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày chiếu!");
            txtNgayChieu.requestFocus();
            return false;
        }
        
        if (txtGioChieu.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giờ chiếu!");
            txtGioChieu.requestFocus();
            return false;
        }
        
        if (txtGiaVe.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá vé!");
            txtGiaVe.requestFocus();
            return false;
        }
        
        try {
            Date.valueOf(txtNgayChieu.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Ngày chiếu không đúng định dạng!\nVD: 2024-12-25");
            txtNgayChieu.requestFocus();
            return false;
        }
        
        try {
            Time.valueOf(txtGioChieu.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Giờ chiếu không đúng định dạng!\nVD: 19:30:00");
            txtGioChieu.requestFocus();
            return false;
        }
        
        try {
            int giaVe = Integer.parseInt(txtGiaVe.getText().trim());
            if (giaVe <= 0) {
                JOptionPane.showMessageDialog(this, "Giá vé phải > 0!");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá vé phải là số!");
            txtGiaVe.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void themSuatChieu() {
        if (!validateInput()) return;
        
        try {
            SuatChieu sc = new SuatChieu();
            sc.setMaPhim(getMaPhimFromCombo());
            sc.setMaPhong(getMaPhongFromCombo());
            sc.setNgayChieu(Date.valueOf(txtNgayChieu.getText().trim()));
            sc.setGioChieu(Time.valueOf(txtGioChieu.getText().trim()));
            sc.setGiaVe(Integer.parseInt(txtGiaVe.getText().trim()));  // ← Lưu giá vé
            
            if (suatChieuDAO.themSuatChieu(sc)) {
                JOptionPane.showMessageDialog(this, "Thêm suất chiếu thành công!");
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm suất chiếu thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void suaSuatChieu() {
        if (txtMaSuat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn suất chiếu cần sửa!");
            return;
        }
        
        if (!validateInput()) return;
        
        try {
            SuatChieu sc = new SuatChieu();
            sc.setMaSuat(Integer.parseInt(txtMaSuat.getText()));
            sc.setMaPhim(getMaPhimFromCombo());
            sc.setMaPhong(getMaPhongFromCombo());
            sc.setNgayChieu(Date.valueOf(txtNgayChieu.getText().trim()));
            sc.setGioChieu(Time.valueOf(txtGioChieu.getText().trim()));
            sc.setGiaVe(Integer.parseInt(txtGiaVe.getText().trim()));  // ← Lưu giá vé
            
            if (suatChieuDAO.capNhatSuatChieu(sc)) {
                JOptionPane.showMessageDialog(this, "Cập nhật suất chiếu thành công!");
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật suất chiếu thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void xoaSuatChieu() {
        if (txtMaSuat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn suất chiếu cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa suất chiếu này?\n(Lưu ý: Phải xóa vé liên quan trước!)", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int maSuat = Integer.parseInt(txtMaSuat.getText());
                if (suatChieuDAO.xoaSuatChieu(maSuat)) {
                    JOptionPane.showMessageDialog(this, "Xóa suất chiếu thành công!");
                    lamMoi();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Xóa suất chiếu thất bại!\nSuất chiếu có thể đã có vé!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    private void lamMoi() {
        txtMaSuat.setText("");
        txtNgayChieu.setText("");
        txtGioChieu.setText("");
        txtGiaVe.setText("");
        txtLocNgay.setText("");
        loadDataToTable();
    }
    
    private void locTheoNgay() {
        String ngay = txtLocNgay.getText().trim();
        if (ngay.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày cần lọc!");
            return;
        }
        
        try {
            tableModel.setRowCount(0);
            List<SuatChieu> danhSach = suatChieuDAO.locSuatChieuTheoNgay(Date.valueOf(ngay));
            
            if (danhSach.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có suất chiếu nào trong ngày này!");
            }
            
            for (SuatChieu sc : danhSach) {
                int conTrong = sc.getSoGhe() - sc.getSoVeDaBan();
                Object[] row = {
                    sc.getMaSuat(),
                    sc.getTenPhim() != null ? sc.getTenPhim() : "N/A",
                    sc.getTenPhong() != null ? sc.getTenPhong() : "N/A",
                    sc.getNgayChieu(),
                    sc.getGioChieu(),
                    String.format("%,d VNĐ", sc.getGiaVe()),
                    sc.getSoVeDaBan(),
                    conTrong
                };
                tableModel.addRow(row);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                "Ngày không đúng định dạng!\nVD: 2024-12-25");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void hienThiThongTin() {
        int selectedRow = tableSuatChieu.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                txtMaSuat.setText(tableModel.getValueAt(selectedRow, 0).toString());
                
                String tenPhim = tableModel.getValueAt(selectedRow, 1).toString();
                for (int i = 0; i < cboPhim.getItemCount(); i++) {
                    if (cboPhim.getItemAt(i).contains(tenPhim)) {
                        cboPhim.setSelectedIndex(i);
                        break;
                    }
                }
                
                String tenPhong = tableModel.getValueAt(selectedRow, 2).toString();
                for (int i = 0; i < cboPhong.getItemCount(); i++) {
                    if (cboPhong.getItemAt(i).contains(tenPhong)) {
                        cboPhong.setSelectedIndex(i);
                        break;
                    }
                }
                
                txtNgayChieu.setText(tableModel.getValueAt(selectedRow, 3).toString());
                txtGioChieu.setText(tableModel.getValueAt(selectedRow, 4).toString());
                
                // ← Lấy giá vé
                String giaVe = tableModel.getValueAt(selectedRow, 5).toString()
                               .replace(",", "").replace(".", "").replace(" VNĐ", "").trim();
                txtGiaVe.setText(giaVe);
            } catch (Exception e) {
                System.err.println("Lỗi hiển thị thông tin: " + e.getMessage());
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuanLySuatChieuForm().setVisible(true);
        });
    }
}