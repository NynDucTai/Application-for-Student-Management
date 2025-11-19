package com.example.doanlaptrinhhuongdoituong.model.Admin;

/**
 * Model (POJO) đại diện cho một Khoa và các số liệu thống kê.
 */
public class AdminDepartmentManageModel {

    private String maKhoa;
    private String tenKhoa;
    private int soLuongGV;
    private int soLuongSV;
    private int soLuongLHP;

    // Constructor rỗng
    public AdminDepartmentManageModel() {
    }

    // --- Getters và Setters (Bắt buộc) ---

    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }

    public String getTenKhoa() { return tenKhoa; }
    public void setTenKhoa(String tenKhoa) { this.tenKhoa = tenKhoa; }

    public int getSoLuongGV() { return soLuongGV; }
    public void setSoLuongGV(int soLuongGV) { this.soLuongGV = soLuongGV; }

    public int getSoLuongSV() { return soLuongSV; }
    public void setSoLuongSV(int soLuongSV) { this.soLuongSV = soLuongSV; }

    public int getSoLuongLHP() { return soLuongLHP; }
    public void setSoLuongLHP(int soLuongLHP) { this.soLuongLHP = soLuongLHP; }
}