package model;

import java.sql.Date;

public class ThongKe {
    private Date ngayChieu;
    private int nam;
    private int thang;
    private int tuan;
    private int soDonHang;
    private int tongSoVe;
    private long tongDoanhThu;

    public ThongKe() {}

    // Getters and Setters
    public Date getNgayChieu() { return ngayChieu; }
    public void setNgayChieu(Date ngayChieu) { this.ngayChieu = ngayChieu; }
    
    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }
    
    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }
    
    public int getTuan() { return tuan; }
    public void setTuan(int tuan) { this.tuan = tuan; }
    
    public int getSoDonHang() { return soDonHang; }
    public void setSoDonHang(int soDonHang) { this.soDonHang = soDonHang; }
    
    public int getTongSoVe() { return tongSoVe; }
    public void setTongSoVe(int tongSoVe) { this.tongSoVe = tongSoVe; }
    
    public long getTongDoanhThu() { return tongDoanhThu; }
    public void setTongDoanhThu(long tongDoanhThu) { this.tongDoanhThu = tongDoanhThu; }
}