package model;

import java.sql.Date;
import java.sql.Time;

public class Ve {
    private int maVe;
    private int maSuat;
    private int giaVe;
    private int soLuong;  // ← THÊM MỚI
    
    // Thông tin bổ sung từ join
    private String tenPhim;
    private String tenPhong;
    private Date ngayChieu;
    private Time gioChieu;

    public Ve() {}

    public Ve(int maVe, int maSuat, int giaVe, int soLuong) {
        this.maVe = maVe;
        this.maSuat = maSuat;
        this.giaVe = giaVe;
        this.soLuong = soLuong;
    }

    // Getters and Setters
    public int getMaVe() { return maVe; }
    public void setMaVe(int maVe) { this.maVe = maVe; }
    
    public int getMaSuat() { return maSuat; }
    public void setMaSuat(int maSuat) { this.maSuat = maSuat; }
    
    public int getGiaVe() { return giaVe; }
    public void setGiaVe(int giaVe) { this.giaVe = giaVe; }
    
    public int getSoLuong() { return soLuong; }  // ← THÊM MỚI
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    
    // Tính thành tiền
    public int getThanhTien() {
        return giaVe * soLuong;
    }
    
    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }
    
    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }
    
    public Date getNgayChieu() { return ngayChieu; }
    public void setNgayChieu(Date ngayChieu) { this.ngayChieu = ngayChieu; }
    
    public Time getGioChieu() { return gioChieu; }
    public void setGioChieu(Time gioChieu) { this.gioChieu = gioChieu; }
}