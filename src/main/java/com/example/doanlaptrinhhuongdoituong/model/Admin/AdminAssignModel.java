package com.example.doanlaptrinhhuongdoituong.model.Admin;

/**
 * Model (POJO) linh hoạt
 * Dùng để chứa dữ liệu cho cả 3 bảng trong giao diện Phân công
 */
public class AdminAssignModel {

    // Thuộc tính chung
    private String maKhoa;

    // Thuộc tính của Lớp Học Phần (Bảng 1)
    private String maLopHP;
    private String maMon;
    private String tenMon;
    private int soTinChi;

    // Thuộc tính của Giảng Viên (Bảng 2)
    private String maGV;
    private String hoTenGV;
    private String emailGV;

    // Constructor rỗng
    public AdminAssignModel() {
    }

    // --- Getters và Setters (Bắt buộc) ---

    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }

    public String getMaLopHP() { return maLopHP; }
    public void setMaLopHP(String maLopHP) { this.maLopHP = maLopHP; }

    public String getMaMon() { return maMon; }
    public void setMaMon(String maMon) { this.maMon = maMon; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public int getSoTinChi() { return soTinChi; }
    public void setSoTinChi(int soTinChi) { this.soTinChi = soTinChi; }

    public String getMaGV() { return maGV; }
    public void setMaGV(String maGV) { this.maGV = maGV; }

    public String getHoTenGV() { return hoTenGV; }
    public void setHoTenGV(String hoTenGV) { this.hoTenGV = hoTenGV; }

    public String getEmailGV() { return emailGV; }
    public void setEmailGV(String emailGV) { this.emailGV = emailGV; }
}