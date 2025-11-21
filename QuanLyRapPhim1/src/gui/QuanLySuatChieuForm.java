package gui;

import dao.SuatChieuDAO;
import dao.PhimDAO;
import model.SuatChieu;
import model.Phim;
import utils.UIUtils;
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
    private JTextField txtMaSuat, txtNgayChieu, txtGioChieu, txtLocNgay, txtGiaVe;
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
        setSize(1500, 820);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        
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
        
        // ========== HEADER ==========
        JPanel pnlHeader = UIUtils.createHeaderPanel(
            new Color(34, 197, 94),
            new Color(22, 163, 74)
        );
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        pnlHeader.setLayout(new BorderLayout());
        
        JPanel pnlTitleArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlTitleArea.setOpaque(false);
        
        JLabel lblIcon = new JLabel("📅");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        pnlTitleArea.add(lblIcon);
        
        JLabel lblTitle = new JLabel("QUẢN LÝ SUẤT CHIẾU");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        pnlTitleArea.add(lblTitle);
        
        pnlHeader.add(pnlTitleArea, BorderLayout.WEST);
        
        JButton btnBack = UIUtils.createIconButton("🏠", UIUtils.SUCCESS_DARK, UIUtils.SUCCESS_COLOR);
        btnBack.setToolTipText("Quay lại trang chủ");
        btnBack.addActionListener(e -> dispose());
        pnlHeader.add(btnBack, BorderLayout.EAST);
        
        // ========== CONTENT ==========
        JPanel pnlContent = new JPanel(new BorderLayout(20, 20));
        pnlContent.setOpaque(false);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // LEFT: FORM
        JPanel pnlFormWrapper = createFormPanel();
        pnlFormWrapper.setPreferredSize(new Dimension(450, 0));
        
        // RIGHT: TABLE
        JPanel pnlTableWrapper = createTablePanel();
        
        pnlContent.add(pnlFormWrapper, BorderLayout.WEST);
        pnlContent.add(pnlTableWrapper, BorderLayout.CENTER);
        
        mainPanel.add(pnlHeader, BorderLayout.NORTH);
        mainPanel.add(pnlContent, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel wrapper = UIUtils.createCardPanel();
        wrapper.setLayout(new BorderLayout(0, 15));
        
        // Title
        JLabel lblFormTitle = new JLabel("🎞️ Thông Tin Suất Chiếu");
        lblFormTitle.setFont(UIUtils.FONT_HEADING);
        lblFormTitle.setForeground(UIUtils.TEXT_PRIMARY);
        wrapper.add(lblFormTitle, BorderLayout.NORTH);
        
        // Form
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        
        // Mã suất
        gbc.gridy = 0;
        JLabel lblMa = new JLabel("Mã Suất");
        lblMa.setFont(UIUtils.FONT_BODY);
        lblMa.setForeground(UIUtils.TEXT_SECONDARY);
        pnlForm.add(lblMa, gbc);
        
        gbc.gridy = 1;
        txtMaSuat = new JTextField();
        txtMaSuat.setEditable(false);
        txtMaSuat.setBackground(UIUtils.GRAY_100);
        UIUtils.styleTextField(txtMaSuat);
        txtMaSuat.setPreferredSize(new Dimension(0, 40));
        pnlForm.add(txtMaSuat, gbc);
        
        // Phim
        gbc.gridy = 2;
        JLabel lblPhim = new JLabel("Phim *");
        lblPhim.setFont(UIUtils.FONT_SUBHEADING);
        lblPhim.setForeground(UIUtils.TEXT_PRIMARY);
        pnlForm.add(lblPhim, gbc);
        
        gbc.gridy = 3;
        cboPhim = new JComboBox<>();
        UIUtils.styleComboBox(cboPhim);
        cboPhim.setPreferredSize(new Dimension(0, 40));
        pnlForm.add(cboPhim, gbc);
        
        // Phòng
        gbc.gridy = 4;
        JLabel lblPhong = new JLabel("Phòng Chiếu *");
        lblPhong.setFont(UIUtils.FONT_SUBHEADING);
        lblPhong.setForeground(UIUtils.TEXT_PRIMARY);
        pnlForm.add(lblPhong, gbc);
        
        gbc.gridy = 5;
        cboPhong = new JComboBox<>();
        UIUtils.styleComboBox(cboPhong);
        cboPhong.setPreferredSize(new Dimension(0, 40));
        cboPhong.addActionListener(e -> tuDongDienGiaVe());
        pnlForm.add(cboPhong, gbc);
        
        // Ngày chiếu
        gbc.gridy = 6;
        JLabel lblNgay = new JLabel("Ngày Chiếu *");
        lblNgay.setFont(UIUtils.FONT_BODY);
        lblNgay.setForeground(UIUtils.TEXT_SECONDARY);
        pnlForm.add(lblNgay, gbc);
        
        gbc.gridy = 7;
        JPanel pnlNgay = new JPanel(new BorderLayout(8, 0));
        pnlNgay.setOpaque(false);
        txtNgayChieu = new JTextField();
        UIUtils.styleTextField(txtNgayChieu);
        txtNgayChieu.setPreferredSize(new Dimension(0, 40));
        JLabel lblFormatNgay = new JLabel("yyyy-MM-dd");
        lblFormatNgay.setFont(UIUtils.FONT_SMALL);
        lblFormatNgay.setForeground(UIUtils.TEXT_MUTED);
        pnlNgay.add(txtNgayChieu, BorderLayout.CENTER);
        pnlNgay.add(lblFormatNgay, BorderLayout.EAST);
        pnlForm.add(pnlNgay, gbc);
        
        // Giờ chiếu
        gbc.gridy = 8;
        JLabel lblGio = new JLabel("Giờ Chiếu *");
        lblGio.setFont(UIUtils.FONT_BODY);
        lblGio.setForeground(UIUtils.TEXT_SECONDARY);
        pnlForm.add(lblGio, gbc);
        
        gbc.gridy = 9;
        JPanel pnlGio = new JPanel(new BorderLayout(8, 0));
        pnlGio.setOpaque(false);
        txtGioChieu = new JTextField();
        UIUtils.styleTextField(txtGioChieu);
        txtGioChieu.setPreferredSize(new Dimension(0, 40));
        JLabel lblFormatGio = new JLabel("HH:mm:ss");
        lblFormatGio.setFont(UIUtils.FONT_SMALL);
        lblFormatGio.setForeground(UIUtils.TEXT_MUTED);
        pnlGio.add(txtGioChieu, BorderLayout.CENTER);
        pnlGio.add(lblFormatGio, BorderLayout.EAST);
        pnlForm.add(pnlGio, gbc);
        
        // Giá vé
        gbc.gridy = 10;
        JLabel lblGiaVe = new JLabel("Giá Vé (VNĐ) *");
        lblGiaVe.setFont(UIUtils.FONT_SUBHEADING);
        lblGiaVe.setForeground(new Color(34, 197, 94));
        pnlForm.add(lblGiaVe, gbc);
        
        gbc.gridy = 11;
        txtGiaVe = new JTextField();
        UIUtils.styleTextField(txtGiaVe);
        txtGiaVe.setPreferredSize(new Dimension(0, 40));
        txtGiaVe.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlForm.add(txtGiaVe, gbc);
        
        // Hint
        gbc.gridy = 12;
        JLabel lblHint = new JLabel("<html><i>💡 Phòng thường: 75k-80k | Phòng VIP: 120k</i></html>");
        lblHint.setFont(UIUtils.FONT_SMALL);
        lblHint.setForeground(UIUtils.TEXT_MUTED);
        pnlForm.add(lblHint, gbc);
        
        // Buttons
        gbc.gridy = 13;
        gbc.insets = new Insets(20, 0, 0, 0);
        JPanel pnlButtons = new JPanel(new GridLayout(2, 2, 12, 12));
        pnlButtons.setBackground(Color.WHITE);
        
        btnThem = UIUtils.createSuccessButton("➕ Thêm");
        btnSua = UIUtils.createPrimaryButton("✏️ Sửa");
        btnXoa = UIUtils.createDangerButton("🗑️ Xóa");
        btnLamMoi = UIUtils.createSecondaryButton("🔄 Làm Mới");
        
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);
        pnlForm.add(pnlButtons, gbc);
        
        wrapper.add(pnlForm, BorderLayout.CENTER);
        
        // Events
        btnThem.addActionListener(e -> themSuatChieu());
        btnSua.addActionListener(e -> suaSuatChieu());
        btnXoa.addActionListener(e -> xoaSuatChieu());
        btnLamMoi.addActionListener(e -> lamMoi());
        
        return wrapper;
    }
    
    private JPanel createTablePanel() {
        JPanel wrapper = UIUtils.createCardPanel();
        wrapper.setLayout(new BorderLayout(0, 15));
        
        // Filter bar
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        pnlFilter.setBackground(Color.WHITE);
        
        JLabel lblFilter = new JLabel("📆");
        lblFilter.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        pnlFilter.add(lblFilter);
        
        JLabel lblLocText = new JLabel("Lọc theo ngày:");
        lblLocText.setFont(UIUtils.FONT_BODY);
        lblLocText.setForeground(UIUtils.TEXT_SECONDARY);
        pnlFilter.add(lblLocText);
        
        txtLocNgay = new JTextField(15);
        UIUtils.styleTextField(txtLocNgay);
        txtLocNgay.setPreferredSize(new Dimension(180, 38));
        pnlFilter.add(txtLocNgay);
        
        JLabel lblFormat = new JLabel("yyyy-MM-dd");
        lblFormat.setFont(UIUtils.FONT_SMALL);
        lblFormat.setForeground(UIUtils.TEXT_MUTED);
        pnlFilter.add(lblFormat);
        
        btnLocNgay = UIUtils.createWarningButton("Lọc");
        pnlFilter.add(btnLocNgay);
        
        JButton btnHuyLoc = UIUtils.createSecondaryButton("Hủy");
        btnHuyLoc.addActionListener(e -> {
            txtLocNgay.setText("");
            loadDataToTable();
        });
        pnlFilter.add(btnHuyLoc);
        
        wrapper.add(pnlFilter, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"Mã", "Tên Phim", "Phòng", "Ngày Chiếu", 
                                "Giờ", "Giá Vé", "Vé Bán", "Còn Trống"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSuatChieu = new JTable(tableModel);
        tableSuatChieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UIUtils.styleTable(tableSuatChieu);
        
        tableSuatChieu.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableSuatChieu.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableSuatChieu.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableSuatChieu.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableSuatChieu.getColumnModel().getColumn(4).setPreferredWidth(80);
        tableSuatChieu.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableSuatChieu.getColumnModel().getColumn(6).setPreferredWidth(80);
        tableSuatChieu.getColumnModel().getColumn(7).setPreferredWidth(100);
        
        JScrollPane scrollTable = new JScrollPane(tableSuatChieu);
        scrollTable.setBorder(BorderFactory.createLineBorder(UIUtils.BORDER_COLOR));
        wrapper.add(scrollTable, BorderLayout.CENTER);
        
        // Events
        btnLocNgay.addActionListener(e -> locTheoNgay());
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
        
        return wrapper;
    }
    
    private void tuDongDienGiaVe() {
        int maPhong = getMaPhongFromCombo();
        if (maPhong == 4) {
            txtGiaVe.setText("120000");
        } else {
            txtGiaVe.setText("80000");
        }
    }
    
    private void loadComboBoxData() {
        try {
            cboPhim.removeAllItems();
            List<Phim> danhSachPhim = phimDAO.layDanhSachPhim();
            if (danhSachPhim.isEmpty()) {
                UIUtils.showWarningMessage(this, 
                    "Chưa có phim nào!\nVui lòng thêm phim trước.");
            }
            for (Phim phim : danhSachPhim) {
                cboPhim.addItem(phim.getMaPhim() + " - " + phim.getTenPhim());
            }
            
            cboPhong.removeAllItems();
            cboPhong.addItem("1 - Phòng 1");
            cboPhong.addItem("2 - Phòng 2");
            cboPhong.addItem("3 - Phòng 3");
            cboPhong.addItem("4 - Phòng VIP");
            cboPhong.addItem("5 - Phòng 4");
        } catch (Exception e) {
            UIUtils.showErrorMessage(this, "Lỗi load dữ liệu: " + e.getMessage());
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
                    String.format("%,d", sc.getGiaVe()),
                    sc.getSoVeDaBan(),
                    conTrong
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            UIUtils.showErrorMessage(this, "Lỗi load dữ liệu: " + e.getMessage());
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
            UIUtils.showWarningMessage(this, "Vui lòng chọn phim!");
            return false;
        }
        
        if (txtNgayChieu.getText().trim().isEmpty()) {
            UIUtils.showWarningMessage(this, "Vui lòng nhập ngày chiếu!");
            txtNgayChieu.requestFocus();
            return false;
        }
        
        if (txtGioChieu.getText().trim().isEmpty()) {
            UIUtils.showWarningMessage(this, "Vui lòng nhập giờ chiếu!");
            txtGioChieu.requestFocus();
            return false;
        }
        
        if (txtGiaVe.getText().trim().isEmpty()) {
            UIUtils.showWarningMessage(this, "Vui lòng nhập giá vé!");
            txtGiaVe.requestFocus();
            return false;
        }
        
        try {
            Date.valueOf(txtNgayChieu.getText().trim());
        } catch (Exception e) {
            UIUtils.showWarningMessage(this, 
                "Ngày chiếu không đúng định dạng!\nVD: 2024-12-25");
            return false;
        }
        
        try {
            Time.valueOf(txtGioChieu.getText().trim());
        } catch (Exception e) {
            UIUtils.showWarningMessage(this, 
                "Giờ chiếu không đúng định dạng!\nVD: 19:30:00");
            return false;
        }
        
        try {
            int giaVe = Integer.parseInt(txtGiaVe.getText().trim());
            if (giaVe <= 0) {
                UIUtils.showWarningMessage(this, "Giá vé phải > 0!");
                return false;
            }
        } catch (NumberFormatException e) {
            UIUtils.showWarningMessage(this, "Giá vé phải là số!");
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
            sc.setGiaVe(Integer.parseInt(txtGiaVe.getText().trim()));
            
            if (suatChieuDAO.themSuatChieu(sc)) {
                UIUtils.showSuccessMessage(this, "Thêm suất chiếu thành công!");
                lamMoi();
            } else {
                UIUtils.showErrorMessage(this, "Thêm suất chiếu thất bại!");
            }
        } catch (Exception ex) {
            UIUtils.showErrorMessage(this, "Lỗi: " + ex.getMessage());
        }
    }
    
    private void suaSuatChieu() {
        if (txtMaSuat.getText().isEmpty()) {
            UIUtils.showWarningMessage(this, "Vui lòng chọn suất chiếu cần sửa!");
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
            sc.setGiaVe(Integer.parseInt(txtGiaVe.getText().trim()));
            
            if (suatChieuDAO.capNhatSuatChieu(sc)) {
                UIUtils.showSuccessMessage(this, "Cập nhật thành công!");
                lamMoi();
            } else {
                UIUtils.showErrorMessage(this, "Cập nhật thất bại!");
            }
        } catch (Exception ex) {
            UIUtils.showErrorMessage(this, "Lỗi: " + ex.getMessage());
        }
    }
    
    private void xoaSuatChieu() {
        if (txtMaSuat.getText().isEmpty()) {
            UIUtils.showWarningMessage(this, "Vui lòng chọn suất chiếu cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa suất chiếu này?", 
            "⚠️ Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int maSuat = Integer.parseInt(txtMaSuat.getText());
                if (suatChieuDAO.xoaSuatChieu(maSuat)) {
                    UIUtils.showSuccessMessage(this, "Xóa thành công!");
                    lamMoi();
                } else {
                    UIUtils.showErrorMessage(this, "Xóa thất bại!");
                }
            } catch (Exception ex) {
                UIUtils.showErrorMessage(this, "Lỗi: " + ex.getMessage());
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
            UIUtils.showWarningMessage(this, "Vui lòng nhập ngày cần lọc!");
            return;
        }
        
        try {
            tableModel.setRowCount(0);
            List<SuatChieu> danhSach = suatChieuDAO.locSuatChieuTheoNgay(Date.valueOf(ngay));
            
            if (danhSach.isEmpty()) {
                UIUtils.showWarningMessage(this, "Không có suất chiếu nào trong ngày này!");
            }
            
            for (SuatChieu sc : danhSach) {
                int conTrong = sc.getSoGhe() - sc.getSoVeDaBan();
                Object[] row = {
                    sc.getMaSuat(),
                    sc.getTenPhim(),
                    sc.getTenPhong(),
                    sc.getNgayChieu(),
                    sc.getGioChieu(),
                    String.format("%,d", sc.getGiaVe()),
                    sc.getSoVeDaBan(),
                    conTrong
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            UIUtils.showErrorMessage(this, "Lỗi: " + ex.getMessage());
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
                
                String giaVe = tableModel.getValueAt(selectedRow, 5).toString()
                               .replace(",", "").trim();
                txtGiaVe.setText(giaVe);
            } catch (Exception e) {
                System.err.println("Lỗi hiển thị: " + e.getMessage());
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuanLySuatChieuForm().setVisible(true);
        });
    }
}