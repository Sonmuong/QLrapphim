package dao;

import database.DatabaseConnection;
import model.SuatChieu;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SuatChieuDAO {
    private Connection conn;
    
    public SuatChieuDAO() {
        conn = DatabaseConnection.getConnection();
    }
    
    // CREATE - Thêm suất chiếu (có giá vé)
    public boolean themSuatChieu(SuatChieu suatChieu) {
        String sql = "INSERT INTO SuatChieu (MaPhim, MaPhong, NgayChieu, GioChieu, GiaVe) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, suatChieu.getMaPhim());
            pstmt.setInt(2, suatChieu.getMaPhong());
            pstmt.setDate(3, suatChieu.getNgayChieu());
            pstmt.setTime(4, suatChieu.getGioChieu());
            pstmt.setInt(5, suatChieu.getGiaVe());  // ← THÊM GiaVe
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm suất chiếu: " + e.getMessage());
            return false;
        }
    }
    
    // READ - Lấy tất cả (có GiaVe)
    public List<SuatChieu> layDanhSachSuatChieu() {
        List<SuatChieu> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM vw_ChiTietSuatChieu ORDER BY NgayChieu DESC, GioChieu DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                SuatChieu sc = new SuatChieu();
                sc.setMaSuat(rs.getInt("MaSuat"));
                sc.setMaPhim(rs.getInt("MaPhim"));
                sc.setTenPhim(rs.getString("TenPhim"));
                sc.setMaPhong(rs.getInt("MaPhong"));
                sc.setTenPhong(rs.getString("TenPhong"));
                sc.setNgayChieu(rs.getDate("NgayChieu"));
                sc.setGioChieu(rs.getTime("GioChieu"));
                sc.setGiaVe(rs.getInt("GiaVe"));  // ← THÊM GiaVe
                sc.setSoVeDaBan(rs.getInt("SoVeDaBan"));
                sc.setSoGhe(rs.getInt("SoGhe"));
                danhSach.add(sc);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách suất chiếu: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSach;
    }
    
    // READ - Lấy theo mã
    public SuatChieu laySuatChieuTheoMa(int maSuat) {
        String sql = "SELECT * FROM vw_ChiTietSuatChieu WHERE MaSuat = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maSuat);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                SuatChieu sc = new SuatChieu();
                sc.setMaSuat(rs.getInt("MaSuat"));
                sc.setMaPhim(rs.getInt("MaPhim"));
                sc.setTenPhim(rs.getString("TenPhim"));
                sc.setMaPhong(rs.getInt("MaPhong"));
                sc.setTenPhong(rs.getString("TenPhong"));
                sc.setNgayChieu(rs.getDate("NgayChieu"));
                sc.setGioChieu(rs.getTime("GioChieu"));
                sc.setGiaVe(rs.getInt("GiaVe"));  // ← THÊM GiaVe
                sc.setSoVeDaBan(rs.getInt("SoVeDaBan"));
                sc.setSoGhe(rs.getInt("SoGhe"));
                return sc;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy suất chiếu: " + e.getMessage());
        }
        return null;
    }
    
    // UPDATE - Cập nhật (có giá vé)
    public boolean capNhatSuatChieu(SuatChieu suatChieu) {
        String sql = "UPDATE SuatChieu SET MaPhim=?, MaPhong=?, NgayChieu=?, GioChieu=?, GiaVe=? WHERE MaSuat=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, suatChieu.getMaPhim());
            pstmt.setInt(2, suatChieu.getMaPhong());
            pstmt.setDate(3, suatChieu.getNgayChieu());
            pstmt.setTime(4, suatChieu.getGioChieu());
            pstmt.setInt(5, suatChieu.getGiaVe());  // ← THÊM GiaVe
            pstmt.setInt(6, suatChieu.getMaSuat());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật suất chiếu: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE
    public boolean xoaSuatChieu(int maSuat) {
        String sql = "DELETE FROM SuatChieu WHERE MaSuat = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maSuat);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa suất chiếu: " + e.getMessage());
            return false;
        }
    }
    
    // LỌC THEO NGÀY
    public List<SuatChieu> locSuatChieuTheoNgay(Date ngayChieu) {
        List<SuatChieu> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM vw_ChiTietSuatChieu WHERE NgayChieu = ? ORDER BY GioChieu";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, ngayChieu);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                SuatChieu sc = new SuatChieu();
                sc.setMaSuat(rs.getInt("MaSuat"));
                sc.setMaPhim(rs.getInt("MaPhim"));
                sc.setTenPhim(rs.getString("TenPhim"));
                sc.setMaPhong(rs.getInt("MaPhong"));
                sc.setTenPhong(rs.getString("TenPhong"));
                sc.setNgayChieu(rs.getDate("NgayChieu"));
                sc.setGioChieu(rs.getTime("GioChieu"));
                sc.setGiaVe(rs.getInt("GiaVe"));
                sc.setSoVeDaBan(rs.getInt("SoVeDaBan"));
                sc.setSoGhe(rs.getInt("SoGhe"));
                danhSach.add(sc);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lọc suất chiếu theo ngày: " + e.getMessage());
        }
        return danhSach;
    }
}
