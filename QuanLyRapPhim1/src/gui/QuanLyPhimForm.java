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
        setSize(1400, 800);
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
            new Color(99, 102, 241),
            new Color(79, 70, 229)
        );
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        pnlHeader.setLayout(new BorderLayout());
        
        JPanel pnlTitleArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlTitleArea.setOpaque(false);
        
        JLabel lblIcon = new JLabel("🎬");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        pnlTitleArea.add(lblIcon);
        
        JLabel lblTitle = new JLabel("QUẢN LÝ PHIM");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        pnlTitleArea.add(lblTitle);
        
        pnlHeader.add(pnlTitleArea, BorderLayout.WEST);
        
        JButton btnBack = UIUtils.createIconButton("🏠", UIUtils.PRIMARY_DARK, UIUtils.PRIMARY_COLOR);
        btnBack.setToolTipText("Quay lại trang chủ");
        btnBack.addActionListener(e -> dispose());
        pnlHeader.add(btnBack, BorderLayout.EAST);
        
        // ========== CONTENT ==========
        JPanel pnlContent = new JPanel(new BorderLayout(20, 20));
        pnlContent.setOpaque(false);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // === LEFT: FORM PANEL ===
        JPanel pnlFormWrapper = createFormPanel();
        pnlFormWrapper.setPreferredSize(new Dimension(500, 0));
        
        // === RIGHT: TABLE PANEL ===
        JPanel pnlTableWrapper = createTablePanel();
        
        pnlContent.add(pnlFormWrapper, BorderLayout.WEST);
        pnlContent.add(pnlTableWrapper, BorderLayout.CENTER);
        
        // Add to main
        mainPanel.add(pnlHeader, BorderLayout.NORTH);
        mainPanel.add(pnlContent, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel wrapper = UIUtils.createCardPanel();
        wrapper.setLayout(new BorderLayout(0, 15));
        
        // Title
        JLabel lblFormTitle = new JLabel("📝 Thông Tin Phim");
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
        
        // Mã phim
        gbc.gridy = 0;
        JLabel lblMa = new JLabel("Mã Phim");
        lblMa.setFont(UIUtils.FONT_BODY);
        lblMa.setForeground(UIUtils.TEXT_SECONDARY);
        pnlForm.add(lblMa, gbc);
        
        gbc.gridy = 1;
        txtMaPhim = new JTextField();
        txtMaPhim.setEditable(false);
        txtMaPhim.setBackground(UIUtils.GRAY_100);
        UIUtils.styleTextField(txtMaPhim);
        txtMaPhim.setPreferredSize(new Dimension(0, 36));
        pnlForm.add(txtMaPhim, gbc);
        
        // Tên phim
        gbc.gridy = 2;
        JLabel lblTen = new JLabel("Tên Phim *");
        lblTen.setFont(UIUtils.FONT_SUBHEADING);
        lblTen.setForeground(UIUtils.TEXT_PRIMARY);
        pnlForm.add(lblTen, gbc);
        
        gbc.gridy = 3;
        txtTenPhim = new JTextField();
        UIUtils.styleTextField(txtTenPhim);
        txtTenPhim.setPreferredSize(new Dimension(0, 36));
        pnlForm.add(txtTenPhim, gbc);
        
        // Thể loại
        gbc.gridy = 4;
        JLabel lblTheLoai = new JLabel("Thể Loại");
        lblTheLoai.setFont(UIUtils.FONT_BODY);
        lblTheLoai.setForeground(UIUtils.TEXT_SECONDARY);
        pnlForm.add(lblTheLoai, gbc);
        
        gbc.gridy = 5;
        txtTheLoai = new JTextField();
        UIUtils.styleTextField(txtTheLoai);
        txtTheLoai.setPreferredSize(new Dimension(0, 36));
        pnlForm.add(txtTheLoai, gbc);
        
        // Thời lượng
        gbc.gridy = 6;
        JLabel lblThoiLuong = new JLabel("Thời Lượng (phút)");
        lblThoiLuong.setFont(UIUtils.FONT_BODY);
        lblThoiLuong.setForeground(UIUtils.TEXT_SECONDARY);
        pnlForm.add(lblThoiLuong, gbc);
        
        gbc.gridy = 7;
        txtThoiLuong = new JTextField();
        UIUtils.styleTextField(txtThoiLuong);
        txtThoiLuong.setPreferredSize(new Dimension(0, 36));
        pnlForm.add(txtThoiLuong, gbc);
        
        // Mô tả
        gbc.gridy = 8;
        JLabel lblMoTa = new JLabel("Mô Tả");
        lblMoTa.setFont(UIUtils.FONT_BODY);
        lblMoTa.setForeground(UIUtils.TEXT_SECONDARY);
        pnlForm.add(lblMoTa, gbc);
        
        gbc.gridy = 9;
        txtMoTa = new JTextArea(4, 20);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setFont(UIUtils.FONT_BODY);
        txtMoTa.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        scrollMoTa.setBorder(null);
        pnlForm.add(scrollMoTa, gbc);
        
        // Buttons với BoxLayout để tránh bị GridLayout ép
        gbc.gridy = 10;
        gbc.insets = new Insets(20, 0, 0, 0);
        
        btnThem = UIUtils.createSuccessButton("Thêm");
        btnSua = UIUtils.createPrimaryButton("Sửa");
        btnXoa = UIUtils.createDangerButton("Xóa");
        btnLamMoi = UIUtils.createSecondaryButton("Làm Mới");
        
        // Row 1
        JPanel pnlRow1 = new JPanel();
        pnlRow1.setLayout(new BoxLayout(pnlRow1, BoxLayout.X_AXIS));
        pnlRow1.setBackground(Color.WHITE);
        pnlRow1.add(btnThem);
        pnlRow1.add(Box.createHorizontalStrut(12));
        pnlRow1.add(btnSua);
        
        // Row 2
        JPanel pnlRow2 = new JPanel();
        pnlRow2.setLayout(new BoxLayout(pnlRow2, BoxLayout.X_AXIS));
        pnlRow2.setBackground(Color.WHITE);
        pnlRow2.add(btnXoa);
        pnlRow2.add(Box.createHorizontalStrut(12));
        pnlRow2.add(btnLamMoi);
        
        // Wrapper
        JPanel pnlButtonsWrapper = new JPanel();
        pnlButtonsWrapper.setLayout(new BoxLayout(pnlButtonsWrapper, BoxLayout.Y_AXIS));
        pnlButtonsWrapper.setBackground(Color.WHITE);
        pnlButtonsWrapper.add(pnlRow1);
        pnlButtonsWrapper.add(Box.createVerticalStrut(12));
        pnlButtonsWrapper.add(pnlRow2);
        
        pnlForm.add(pnlButtonsWrapper, gbc);
        
        // ✅✅✅ THÊM SCROLLPANE - PHẦN SỬA CHÍNH ✅✅✅
        JScrollPane scrollForm = new JScrollPane(pnlForm);
        scrollForm.setBorder(null);
        scrollForm.getVerticalScrollBar().setUnitIncrement(16);
        wrapper.add(scrollForm, BorderLayout.CENTER);
        // ✅✅✅ HẾT PHẦN THÊM ✅✅✅
        
        // Events
        btnThem.addActionListener(e -> themPhim());
        btnSua.addActionListener(e -> suaPhim());
        btnXoa.addActionListener(e -> xoaPhim());
        btnLamMoi.addActionListener(e -> lamMoi());
        
        return wrapper;
    }
    
    private JPanel createTablePanel() {
        JPanel wrapper = UIUtils.createCardPanel();
        wrapper.setLayout(new BorderLayout(0, 15));
        
        // Search bar
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        pnlSearch.setBackground(Color.WHITE);
        
        JLabel lblSearch = new JLabel("🔍");
        lblSearch.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        pnlSearch.add(lblSearch);
        
        cboTimKiemTheo = new JComboBox<>(new String[]{"Tên phim", "Thể loại"});
        cboTimKiemTheo.setFont(UIUtils.FONT_BODY);
        UIUtils.styleComboBox(cboTimKiemTheo);
        cboTimKiemTheo.setPreferredSize(new Dimension(120, 38));
        pnlSearch.add(cboTimKiemTheo);
        
        txtTimKiem = new JTextField(30);
        UIUtils.styleTextField(txtTimKiem);
        txtTimKiem.setPreferredSize(new Dimension(300, 38));
        pnlSearch.add(txtTimKiem);
        
        btnTimKiem = UIUtils.createWarningButton("Tìm Kiếm");
        pnlSearch.add(btnTimKiem);
        
        JButton btnRefresh = UIUtils.createIconButton("🔄", UIUtils.INFO_COLOR, UIUtils.INFO_DARK);
        btnRefresh.setToolTipText("Làm mới danh sách");
        btnRefresh.addActionListener(e -> {
            txtTimKiem.setText("");
            loadDataToTable();
        });
        pnlSearch.add(btnRefresh);
        
        wrapper.add(pnlSearch, BorderLayout.NORTH);
        
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
        
        // Set column widths
        tablePhim.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablePhim.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablePhim.getColumnModel().getColumn(2).setPreferredWidth(120);
        tablePhim.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablePhim.getColumnModel().getColumn(4).setPreferredWidth(300);
        
        JScrollPane scrollTable = new JScrollPane(tablePhim);
        scrollTable.setBorder(BorderFactory.createLineBorder(UIUtils.BORDER_COLOR));
        wrapper.add(scrollTable, BorderLayout.CENTER);
        
        // Events
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
        
        return wrapper;
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
                UIUtils.showSuccessMessage(this, "Thêm phim thành công!");
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
                UIUtils.showSuccessMessage(this, "Cập nhật phim thành công!");
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
                    UIUtils.showSuccessMessage(this, "Xóa phim thành công!");
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