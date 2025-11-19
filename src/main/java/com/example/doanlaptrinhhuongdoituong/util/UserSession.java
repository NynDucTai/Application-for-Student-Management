package com.example.doanlaptrinhhuongdoituong.util;

// Lớp này dùng để lưu trữ thông tin giảng viên đăng nhập
public class UserSession {

    private static UserSession instance;
    private String maGiangVien;
    private String tenGiangVien; // Có thể mở rộng sau
    private String maSinhVien;

    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setMaGiangVien(String maGV) {
        this.maGiangVien = maGV;
    }

    public String getMaGiangVien() {
        return this.maGiangVien;
    }

    public void setMaSinhVien(String maSV) {
        this.maSinhVien = maSV;
    }

    public String getMaSinhVien() {
        return this.maSinhVien;
    }

    // (Tùy chọn) Thêm các hàm set/get Tên, Email...
    // public void setTenGiangVien(String tenGV) { ... }
    // public String getTenGiangVien() { ... }

    public void cleanUserSession() {
        this.maGiangVien = null;
        this.tenGiangVien = null;
        // instance = null; // Cân nhắc nếu muốn logout hoàn toàn
    }
}