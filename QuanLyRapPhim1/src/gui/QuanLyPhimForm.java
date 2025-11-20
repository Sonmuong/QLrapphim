package gui;

import dao.PhimDAO;
import model.Phim;
import utils.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class QuanLyPhimForm extends JFrame {
    private PhimDAO phimDAO;
    private DefaultTableModel tableModel;
    
    private JTable tablePhim;
    private JTextField txtMaPhim, txtTenPhim, txtTheLoai, txtThoiLuong, txtTimKiem;
    private JTextArea txtMoTa;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JComboBox<String> cboTimKiemTheo;
    
    public QuanLyPhimForm() {
        phimDAO = new PhimDAO();
        initComponents();
        loadDataToTable();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Quản Lý Phim");
        setSize(1300, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(UIUtils.BG_SECONDARY);
        
        // ========== HEADER ==========
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(UIUtils.PRIMARY_COLOR);
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitle = new JLabel("🎬 QUẢN LÝ PHIM");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        
        // ========== CONTENT ==========
        JPanel pnlContent = new JPanel(new BorderLayout(20, 20));
        pnlContent.setBackground(UIUtils.BG_SECONDARY);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // === FORM PANEL (Trái) ===
        JPanel pnlFormWrapper = UIUtils.createCardPanel();
        pnlFormWrapper.setLayout(new BorderLayout());
        pnlFormWrapper.setPreferredSize(new Dimension(380, 0));
        
        JLabel lblFormTitle = UIUtils.createHeadingLabel("Thông Tin Phim");
        lblFormTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        pnlFormWrapper.add(lblFormTitle, BorderLayout.NORTH);
        
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Mã phim
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblMa = new JLabel("Mã Phim:");
        lblMa.setFont(UIUtils.FONT_BODY);
        pnlForm.add(lblMa, gbc);
        
        gbc.gridy = 1;
        txtMaPhim = new JTextField();
        txtMaPhim.setEditable(false);
        txtMaPhim.setBackground(UIUtils.BG_SECONDARY);
        UIUtils.styleTextField(txtMaPhim);
        pnlForm.add(txtMaPhim, gbc);
        
        // Tên phim
        gbc.gridy = 2;
        JLabel lblTen = new JLabel("Tên Phim: *");
        lblTen.setFont(UIUtils.FONT_BODY);
        lblTen.setForeground(UIUtils.TEXT_PRIMARY);
        pnlForm.add(lblTen, gbc);
        
        gbc.gridy = 3;
        txtTenPhim = new JTextField();
        UIUtils.styleTextField(txtTenPhim);
        pnlForm.add(txtTenPhim, gbc);
        
        // Thể loại
        gbc.gridy = 4;
        pnlForm.add(new JLabel("Thể Loại:"), gbc);
        
        gbc.gridy = 5;
        txtTheLoai = new JTextField();
        UIUtils.styleTextField(txtTheLoai);
        pnlForm.add(txtTheLoai, gbc);
        
        // Thời lượng
        gbc.gridy = 6;
        pnlForm.add(new JLabel("Thời Lượng (phút):"), gbc);
        
        gbc.gridy = 7;
        txtThoiLuong = new JTextField();
        UIUtils.styleTextField(txtThoiLuong);
        pnlForm.add(txtThoiLuong, gbc);
        
        // Mô tả
        gbc.gridy = 8;
        pnlForm.add(new JLabel("Mô Tả:"), gbc);
        
        gbc.gridy = 9;
        txtMoTa = new JTextArea(4, 20);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setFont(UIUtils.FONT_BODY);
        txtMoTa.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        scrollMoTa.setBorder(null);
        pnlForm.add(scrollMoTa, gbc);
        
        // Buttons
        gbc.gridy = 10;
        JPanel pnlButtons = new JPanel(new GridLayout(2, 2, 10, 10));
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
        
        pnlFormWrapper.add(pnlForm, BorderLayout.CENTER);
        
        // === TABLE PANEL (Phải) ===
        JPanel pnlTableWrapper = UIUtils.createCardPanel();
        pnlTableWrapper.setLayout(new BorderLayout(0, 15));
        
        // Search bar
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlSearch.setBackground(Color.WHITE);
        
        JLabel lblSearch = new JLabel("🔍 Tìm kiếm:");
        lblSearch.setFont(UIUtils.FONT_SUBHEADING);
        pnlSearch.add(lblSearch);
        
        cboTimKiemTheo = new JComboBox<>(new String[]{"Tên phim", "Thể loại"});
        cboTimKiemTheo.setFont(UIUtils.FONT_BODY);
        UIUtils.styleComboBox(cboTimKiemTheo);
        pnlSearch.add(cboTimKiemTheo);
        
        txtTimKiem = new JTextField(30);
        UIUtils.styleTextField(txtTimKiem);
        pnlSearch.add(txtTimKiem);
        
        btnTimKiem = UIUtils.createWarningButton("Tìm Kiếm");
        pnlSearch.add(btnTimKiem);
        
        pnlTableWrapper.add(pnlSearch, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"Mã", "Tên Phim", "Thể Loại", "Thời Lượng (phút)", "Mô Tả"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePhim = new JTable(tableModel);
        tablePhim.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UIUtils.styleTable(tablePhim);
        
        JScrollPane scrollTable = new JScrollPane(tablePhim);
        scrollTable.setBorder(BorderFactory.createLineBorder(UIUtils.BORDER_COLOR));
        pnlTableWrapper.add(scrollTable, BorderLayout.CENTER);
        
        // Add to content
        pnlContent.add(pnlFormWrapper, BorderLayout.WEST);
        pnlContent.add(pnlTableWrapper, BorderLayout.CENTER);
        
        // Add to frame
        add(pnlHeader, BorderLayout.NORTH);
        add(pnlContent, BorderLayout.CENTER);
        
        // Event handlers
        btnThem.addActionListener(e -> themPhim());
        btnSua.addActionListener(e -> suaPhim());
        btnXoa.addActionListener(e -> xoaPhim());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnTimKiem.addActionListener(e -> timKiem());
        
        tablePhim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hienThiThongTin();
            }
        });
        
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    timKiem();
                }
            }
        });
    }
    
    private void loadDataToTable() {
        tableModel.setRowCount(0);
        List<Phim> danhSach = phimDAO.layDanhSachPhim();
        for (Phim phim : danhSach) {
            Object[] row = {
                phim.getMaPhim(),
                phim.getTenPhim(),
                phim.getTheLoai(),
                phim.getThoiLuong(),
                phim.getMoTa()
            };
            tableModel.addRow(row);
        }
    }
    
    private boolean validateInput() {
        if (txtTenPhim.getText().trim().isEmpty()) {
            UIUtils.showWarningMessage(this, "Vui lòng nhập tên phim!");
            txtTenPhim.requestFocus();
            return false;
        }
        
        try {
            if (!txtThoiLuong.getText().trim().isEmpty()) {
                int thoiLuong = Integer.parseInt(txtThoiLuong.getText().trim());
                if (thoiLuong <= 0) {
                    UIUtils.showWarningMessage(this, "Thời lượng phải lớn hơn 0!");
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            UIUtils.showWarningMessage(this, "Thời lượng phải là số!");
            txtThoiLuong.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void themPhim() {
        if (!validateInput()) return;
        
        try {
            Phim phim = new Phim();
            phim.setTenPhim(txtTenPhim.getText().trim());
            phim.setTheLoai(txtTheLoai.getText().trim());
            phim.setThoiLuong(Integer.parseInt(txtThoiLuong.getText().trim()));
            phim.setMoTa(txtMoTa.getText().trim());
            
            if (phimDAO.themPhim(phim)) {
                UIUtils.showSuccessMessage(this, "✅ Thêm phim thành công!");
                lamMoi();
            } else {
                UIUtils.showErrorMessage(this, "Thêm phim thất bại!");
            }
        } catch (Exception ex) {
            UIUtils.showErrorMessage(this, "Lỗi: " + ex.getMessage());
        }
    }
    
    private void suaPhim() {
        if (txtMaPhim.getText().isEmpty()) {
            UIUtils.showWarningMessage(this, "Vui lòng chọn phim cần sửa!");
            return;
        }
        
        if (!validateInput()) return;
        
        try {
            Phim phim = new Phim();
            phim.setMaPhim(Integer.parseInt(txtMaPhim.getText()));
            phim.setTenPhim(txtTenPhim.getText().trim());
            phim.setTheLoai(txtTheLoai.getText().trim());
            phim.setThoiLuong(Integer.parseInt(txtThoiLuong.getText().trim()));
            phim.setMoTa(txtMoTa.getText().trim());
            
            if (phimDAO.capNhatPhim(phim)) {
                UIUtils.showSuccessMessage(this, "✅ Cập nhật phim thành công!");
                lamMoi();
            } else {
                UIUtils.showErrorMessage(this, "Cập nhật phim thất bại!");
            }
        } catch (Exception ex) {
            UIUtils.showErrorMessage(this, "Lỗi: " + ex.getMessage());
        }
    }
    
    private void xoaPhim() {
        if (txtMaPhim.getText().isEmpty()) {
            UIUtils.showWarningMessage(this, "Vui lòng chọn phim cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa phim này?\n(Lưu ý: Phải xóa suất chiếu liên quan trước!)", 
            "⚠️ Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int maPhim = Integer.parseInt(txtMaPhim.getText());
                if (phimDAO.xoaPhim(maPhim)) {
                    UIUtils.showSuccessMessage(this, "✅ Xóa phim thành công!");
                    lamMoi();
                } else {
                    UIUtils.showErrorMessage(this, 
                        "Xóa phim thất bại!\nPhim có thể đang có suất chiếu!");
                }
            } catch (Exception ex) {
                UIUtils.showErrorMessage(this, "Lỗi: " + ex.getMessage());
            }
        }
    }
    
    private void lamMoi() {
        txtMaPhim.setText("");
        txtTenPhim.setText("");
        txtTheLoai.setText("");
        txtThoiLuong.setText("");
        txtMoTa.setText("");
        txtTimKiem.setText("");
        loadDataToTable();
        txtTenPhim.requestFocus();
    }
    
    private void timKiem() {
        String tuKhoa = txtTimKiem.getText().trim();
        if (tuKhoa.isEmpty()) {
            loadDataToTable();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Phim> danhSach;
        
        if (cboTimKiemTheo.getSelectedIndex() == 0) {
            danhSach = phimDAO.timPhimTheoTen(tuKhoa);
        } else {
            danhSach = phimDAO.timPhimTheoTheLoai(tuKhoa);
        }
        
        if (danhSach.isEmpty()) {
            UIUtils.showWarningMessage(this, "Không tìm thấy phim nào!");
        }
        
        for (Phim phim : danhSach) {
            Object[] row = {
                phim.getMaPhim(),
                phim.getTenPhim(),
                phim.getTheLoai(),
                phim.getThoiLuong(),
                phim.getMoTa()
            };
            tableModel.addRow(row);
        }
    }
    
    private void hienThiThongTin() {
        int selectedRow = tablePhim.getSelectedRow();
        if (selectedRow >= 0) {
            txtMaPhim.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenPhim.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtTheLoai.setText(tableModel.getValueAt(selectedRow, 2) != null ? 
                              tableModel.getValueAt(selectedRow, 2).toString() : "");
            txtThoiLuong.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtMoTa.setText(tableModel.getValueAt(selectedRow, 4) != null ? 
                           tableModel.getValueAt(selectedRow, 4).toString() : "");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new QuanLyPhimForm().setVisible(true);
        });
    }
}