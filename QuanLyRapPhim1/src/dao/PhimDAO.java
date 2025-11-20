package dao;

import database.DatabaseConnection;
import model.Phim;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhimDAO {
    private Connection conn;
    
    public PhimDAO() {
        conn = DatabaseConnection.getConnection();
    }
    
    // CREATE - Thêm phim
    public boolean themPhim(Phim phim) {
        String sql = "INSERT INTO Phim (TenPhim, TheLoai, ThoiLuong, MoTa) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phim.getTenPhim());
            pstmt.setString(2, phim.getTheLoai());
            pstmt.setInt(3, phim.getThoiLuong());
            pstmt.setString(4, phim.getMoTa());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm phim: " + e.getMessage());
            return false;
        }
    }
    
    // READ - Lấy tất cả phim
    public List<Phim> layDanhSachPhim() {
        List<Phim> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM Phim ORDER BY MaPhim DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Phim phim = new Phim();
                phim.setMaPhim(rs.getInt("MaPhim"));
                phim.setTenPhim(rs.getString("TenPhim"));
                phim.setTheLoai(rs.getString("TheLoai"));
                phim.setThoiLuong(rs.getInt("ThoiLuong"));
                phim.setMoTa(rs.getString("MoTa"));
                danhSach.add(phim);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách phim: " + e.getMessage());
        }
        return danhSach;
    }
    
    // READ - Lấy phim theo mã
    public Phim layPhimTheoMa(int maPhim) {
        String sql = "SELECT * FROM Phim WHERE MaPhim = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhim);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Phim phim = new Phim();
                phim.setMaPhim(rs.getInt("MaPhim"));
                phim.setTenPhim(rs.getString("TenPhim"));
                phim.setTheLoai(rs.getString("TheLoai"));
                phim.setThoiLuong(rs.getInt("ThoiLuong"));
                phim.setMoTa(rs.getString("MoTa"));
                return phim;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy phim theo mã: " + e.getMessage());
        }
        return null;
    }
    
    // UPDATE - Cập nhật phim
    public boolean capNhatPhim(Phim phim) {
        String sql = "UPDATE Phim SET TenPhim=?, TheLoai=?, ThoiLuong=?, MoTa=? WHERE MaPhim=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phim.getTenPhim());
            pstmt.setString(2, phim.getTheLoai());
            pstmt.setInt(3, phim.getThoiLuong());
            pstmt.setString(4, phim.getMoTa());
            pstmt.setInt(5, phim.getMaPhim());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật phim: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE - Xóa phim
    public boolean xoaPhim(int maPhim) {
        String sql = "DELETE FROM Phim WHERE MaPhim = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhim);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa phim: " + e.getMessage());
            System.err.println("Có thể phim đang có suất chiếu, vui lòng xóa suất chiếu trước!");
            return false;
        }
    }
    
    // TÌM KIẾM - Tìm phim theo tên
    public List<Phim> timPhimTheoTen(String tenPhim) {
        List<Phim> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM Phim WHERE TenPhim LIKE ? ORDER BY TenPhim";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + tenPhim + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Phim phim = new Phim();
                phim.setMaPhim(rs.getInt("MaPhim"));
                phim.setTenPhim(rs.getString("TenPhim"));
                phim.setTheLoai(rs.getString("TheLoai"));
                phim.setThoiLuong(rs.getInt("ThoiLuong"));
                phim.setMoTa(rs.getString("MoTa"));
                danhSach.add(phim);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm phim theo tên: " + e.getMessage());
        }
        return danhSach;
    }
    
    // TÌM KIẾM - Tìm phim theo thể loại
    public List<Phim> timPhimTheoTheLoai(String theLoai) {
        List<Phim> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM Phim WHERE TheLoai LIKE ? ORDER BY TenPhim";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + theLoai + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Phim phim = new Phim();
                phim.setMaPhim(rs.getInt("MaPhim"));
                phim.setTenPhim(rs.getString("TenPhim"));
                phim.setTheLoai(rs.getString("TheLoai"));
                phim.setThoiLuong(rs.getInt("ThoiLuong"));
                phim.setMoTa(rs.getString("MoTa"));
                danhSach.add(phim);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm phim theo thể loại: " + e.getMessage());
        }
        return danhSach;
    }
    
    // Lấy danh sách thể loại không trùng
    public List<String> layDanhSachTheLoai() {
        List<String> danhSach = new ArrayList<>();
        String sql = "SELECT DISTINCT TheLoai FROM Phim WHERE TheLoai IS NOT NULL ORDER BY TheLoai";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                danhSach.add(rs.getString("TheLoai"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách thể loại: " + e.getMessage());
        }
        return danhSach;
    }
}