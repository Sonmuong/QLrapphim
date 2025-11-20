package model;

public class Phim {
    private int maPhim;
    private String tenPhim;
    private String theLoai;
    private int thoiLuong;
    private String moTa;

    public Phim() {}

    public Phim(int maPhim, String tenPhim, String theLoai, int thoiLuong, String moTa) {
        this.maPhim = maPhim;
        this.tenPhim = tenPhim;
        this.theLoai = theLoai;
        this.thoiLuong = thoiLuong;
        this.moTa = moTa;
    }

    public int getMaPhim() { return maPhim; }
    public void setMaPhim(int maPhim) { this.maPhim = maPhim; }
    
    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }
    
    public String getTheLoai() { return theLoai; }
    public void setTheLoai(String theLoai) { this.theLoai = theLoai; }
    
    public int getThoiLuong() { return thoiLuong; }
    public void setThoiLuong(int thoiLuong) { this.thoiLuong = thoiLuong; }
    
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}