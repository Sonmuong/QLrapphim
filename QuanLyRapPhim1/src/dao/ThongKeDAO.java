package dao;

import database.DatabaseConnection;
import model.ThongKe;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongKeDAO {
    private Connection conn;
    
    public ThongKeDAO() {
        conn = DatabaseConnection.getConnection();
    }
    
    // Thống kê vé theo phim (cập nhật với số lượng)
    public Map<String, Object> thongKeVeTheoPhim() {
        Map<String, Object> thongKe = new HashMap<>();
        String sql = "SELECT * FROM vw_ThongKeVeTheoPhim ORDER BY SoVeBan DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> phimData = new HashMap<>();
                phimData.put("MaPhim", rs.getInt("MaPhim"));
                phimData.put("TenPhim", rs.getString("TenPhim"));
                phimData.put("TheLoai", rs.getString("TheLoai"));
                phimData.put("SoVeBan", rs.getInt("SoVeBan"));
                phimData.put("TongDoanhThu", rs.getLong("TongDoanhThu"));
                thongKe.put(rs.getString("TenPhim"), phimData);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thống kê: " + e.getMessage());
            e.printStackTrace();
        }
        return thongKe;
    }
    
    // ← MỚI: Thống kê theo ngày
    public List<ThongKe> thongKeTheoNgay(Date tuNgay, Date denNgay) {
        List<ThongKe> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM vw_ThongKeTheoNgay " +
                     "WHERE NgayChieu BETWEEN ? AND ? " +
                     "ORDER BY NgayChieu";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, tuNgay);
            pstmt.setDate(2, denNgay);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ThongKe tk = new ThongKe();
                tk.setNgayChieu(rs.getDate("NgayChieu"));
                tk.setNam(rs.getInt("Nam"));
                tk.setThang(rs.getInt("Thang"));
                tk.setTuan(rs.getInt("Tuan"));
                tk.setSoDonHang(rs.getInt("SoDonHang"));
                tk.setTongSoVe(rs.getInt("TongSoVe"));
                tk.setTongDoanhThu(rs.getLong("TongDoanhThu"));
                danhSach.add(tk);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thống kê theo ngày: " + e.getMessage());
        }
        return danhSach;
    }
    
    // ← MỚI: Thống kê theo tuần
    public List<Map<String, Object>> thongKeTheoTuan(int nam) {
        List<Map<String, Object>> danhSach = new ArrayList<>();
        String sql = "SELECT Tuan, SUM(TongSoVe) AS TongVe, SUM(TongDoanhThu) AS DoanhThu " +
                     "FROM vw_ThongKeTheoNgay " +
                     "WHERE Nam = ? " +
                     "GROUP BY Tuan " +
                     "ORDER BY Tuan";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, nam);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("Tuan", rs.getInt("Tuan"));
                row.put("TongVe", rs.getInt("TongVe"));
                row.put("DoanhThu", rs.getLong("DoanhThu"));
                danhSach.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thống kê theo tuần: " + e.getMessage());
        }
        return danhSach;
    }
    
    // ← MỚI: Thống kê theo tháng
    public List<Map<String, Object>> thongKeTheoThang(int nam) {
        List<Map<String, Object>> danhSach = new ArrayList<>();
        String sql = "SELECT Thang, SUM(TongSoVe) AS TongVe, SUM(TongDoanhThu) AS DoanhThu " +
                     "FROM vw_ThongKeTheoNgay " +
                     "WHERE Nam = ? " +
                     "GROUP BY Thang " +
                     "ORDER BY Thang";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, nam);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("Thang", rs.getInt("Thang"));
                row.put("TongVe", rs.getInt("TongVe"));
                row.put("DoanhThu", rs.getLong("DoanhThu"));
                danhSach.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thống kê theo tháng: " + e.getMessage());
        }
        return danhSach;
    }
    
    // ← MỚI: Thống kê theo năm
    public List<Map<String, Object>> thongKeTheoNam() {
        List<Map<String, Object>> danhSach = new ArrayList<>();
        String sql = "SELECT Nam, SUM(TongSoVe) AS TongVe, SUM(TongDoanhThu) AS DoanhThu " +
                     "FROM vw_ThongKeTheoNgay " +
                     "GROUP BY Nam " +
                     "ORDER BY Nam";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("Nam", rs.getInt("Nam"));
                row.put("TongVe", rs.getInt("TongVe"));
                row.put("DoanhThu", rs.getLong("DoanhThu"));
                danhSach.add(row);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi thống kê theo năm: " + e.getMessage());
        }
        return danhSach;
    }
    
    // Tổng số vé đã bán
    public int demTongSoVe() {
        String sql = "SELECT ISNULL(SUM(SoLuong), 0) AS TongVe FROM Ve";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("TongVe");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đếm tổng vé: " + e.getMessage());
        }
        return 0;
    }
    
    // Tổng doanh thu
    public long tinhTongDoanhThu() {
        String sql = "SELECT ISNULL(SUM(GiaVe * SoLuong), 0) AS TongDoanhThu FROM Ve";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong("TongDoanhThu");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tính tổng doanh thu: " + e.getMessage());
        }
        return 0;
    }
}