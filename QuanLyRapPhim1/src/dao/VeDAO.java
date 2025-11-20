package dao;

import database.DatabaseConnection;
import model.Ve;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeDAO {
    private Connection conn;
    
    public VeDAO() {
        conn = DatabaseConnection.getConnection();
    }
    
    // CREATE - Bán vé (có số lượng)
    public boolean banVe(Ve ve) {
        String sql = "INSERT INTO Ve (MaSuat, GiaVe, SoLuong) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ve.getMaSuat());
            pstmt.setInt(2, ve.getGiaVe());
            pstmt.setInt(3, ve.getSoLuong());  // ← THÊM SoLuong
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi bán vé: " + e.getMessage());
            return false;
        }
    }
    
    // READ - Lấy tất cả vé (có số lượng)
    public List<Ve> layDanhSachVe() {
        List<Ve> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM vw_ChiTietVe ORDER BY MaVe DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Ve ve = new Ve();
                ve.setMaVe(rs.getInt("MaVe"));
                ve.setMaSuat(rs.getInt("MaSuat"));
                ve.setGiaVe(rs.getInt("GiaVe"));
                ve.setSoLuong(rs.getInt("SoLuong"));  // ← THÊM SoLuong
                ve.setTenPhim(rs.getString("TenPhim"));
                ve.setTenPhong(rs.getString("TenPhong"));
                ve.setNgayChieu(rs.getDate("NgayChieu"));
                ve.setGioChieu(rs.getTime("GioChieu"));
                danhSach.add(ve);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách vé: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSach;
    }
    
    // Kiểm tra còn ghế trống không
    public boolean kiemTraConGheTrong(int maSuat, int soLuongMua) {
        String sql = "SELECT pc.SoGhe, ISNULL(SUM(v.SoLuong), 0) AS DaBan " +
                     "FROM SuatChieu sc " +
                     "JOIN PhongChieu pc ON sc.MaPhong = pc.MaPhong " +
                     "LEFT JOIN Ve v ON sc.MaSuat = v.MaSuat " +
                     "WHERE sc.MaSuat = ? " +
                     "GROUP BY pc.SoGhe";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maSuat);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int tongGhe = rs.getInt("SoGhe");
                int daBan = rs.getInt("DaBan");
                int conLai = tongGhe - daBan;
                return conLai >= soLuongMua;  // Còn đủ ghế không?
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra ghế trống: " + e.getMessage());
        }
        return false;
    }
}