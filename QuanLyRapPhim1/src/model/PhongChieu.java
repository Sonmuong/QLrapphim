package model;

public class PhongChieu {
    private int maPhong;
    private String tenPhong;
    private int soGhe;

    public PhongChieu() {}

    public PhongChieu(int maPhong, String tenPhong, int soGhe) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.soGhe = soGhe;
    }

    public int getMaPhong() { return maPhong; }
    public void setMaPhong(int maPhong) { this.maPhong = maPhong; }
    
    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }
    
    public int getSoGhe() { return soGhe; }
    public void setSoGhe(int soGhe) { this.soGhe = soGhe; }
}