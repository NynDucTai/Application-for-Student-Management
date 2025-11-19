package com.example.doanlaptrinhhuongdoituong.model.Admin;

/**
 * Model (POJO) đại diện cho một Lớp (NV hoặc HL)
 * và các số liệu thống kê của nó.
 */
public class AdminStaticModel {

    private String maLopHP;
    private String tenLopHP; // Tên lớp (nếu có)
    private String tenMonHoc;
    private int siSoHienTai;  // Đếm được
    private int siSoToiThieu; // Lấy từ CSDL

    // Constructor rỗng
    public AdminStaticModel() {
    }

    // --- Getters và Setters (Bắt buộc) ---

    public String getMaLopHP() { return maLopHP; }
    public void setMaLopHP(String maLopHP) { this.maLopHP = maLopHP; }

    public String getTenLopHP() { return tenLopHP; }
    public void setTenLopHP(String tenLopHP) { this.tenLopHP = tenLopHP; }

    public String getTenMonHoc() { return tenMonHoc; }
    public void setTenMonHoc(String tenMonHoc) { this.tenMonHoc = tenMonHoc; }

    public int getSiSoHienTai() { return siSoHienTai; }
    public void setSiSoHienTai(int siSoHienTai) { this.siSoHienTai = siSoHienTai; }

    public int getSiSoToiThieu() { return siSoToiThieu; }
    public void setSiSoToiThieu(int siSoToiThieu) { this.siSoToiThieu = siSoToiThieu; }
}