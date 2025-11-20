package model;

import java.sql.Date;
import java.sql.Time;

public class SuatChieu {
    private int maSuat;
    private int maPhim;
    private int maPhong;
    private Date ngayChieu;
    private Time gioChieu;
    private int giaVe;  // ← THÊM MỚI
    
    // Thông tin bổ sung từ join
    private String tenPhim;
    private String tenPhong;
    private int soVeDaBan;
    private int soGhe;  // Tổng số ghế của phòng

    public SuatChieu() {}

    public SuatChieu(int maSuat, int maPhim, int maPhong, Date ngayChieu, 
                     Time gioChieu, int giaVe) {
        this.maSuat = maSuat;
        this.maPhim = maPhim;
        this.maPhong = maPhong;
        this.ngayChieu = ngayChieu;
        this.gioChieu = gioChieu;
        this.giaVe = giaVe;
    }

    // Getters and Setters
    public int getMaSuat() { return maSuat; }
    public void setMaSuat(int maSuat) { this.maSuat = maSuat; }
    
    public int getMaPhim() { return maPhim; }
    public void setMaPhim(int maPhim) { this.maPhim = maPhim; }
    
    public int getMaPhong() { return maPhong; }
    public void setMaPhong(int maPhong) { this.maPhong = maPhong; }
    
    public Date getNgayChieu() { return ngayChieu; }
    public void setNgayChieu(Date ngayChieu) { this.ngayChieu = ngayChieu; }
    
    public Time getGioChieu() { return gioChieu; }
    public void setGioChieu(Time gioChieu) { this.gioChieu = gioChieu; }
    
    public int getGiaVe() { return giaVe; }  // ← THÊM MỚI
    public void setGiaVe(int giaVe) { this.giaVe = giaVe; }
    
    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }
    
    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }
    
    public int getSoVeDaBan() { return soVeDaBan; }
    public void setSoVeDaBan(int soVeDaBan) { this.soVeDaBan = soVeDaBan; }
    
    public int getSoGhe() { return soGhe; }
    public void setSoGhe(int soGhe) { this.soGhe = soGhe; }
}