package com.example.doanlaptrinhhuongdoituong.model.Admin;

/**
 * Model (POJO) đại diện cho một Giảng viên và các số liệu thống kê.
 */
public class AdminTeacherManageModel {

    private String maGV;
    private String hoTen;
    private String email;
    private String maKhoa;    // Dùng cho ComboBox (ví dụ: 'CNTT')
    private String tenKhoa;   // Dùng để hiển thị trong bảng (ví dụ: 'Công nghệ thông tin')
    private int soLopGiangDay;
    private String username;
    private String password;

    // Constructor rỗng
    public AdminTeacherManageModel() {
    }

    // --- Getters và Setters (Bắt buộc) ---

    public String getMaGV() { return maGV; }
    public void setMaGV(String maGV) { this.maGV = maGV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }

    public String getTenKhoa() { return tenKhoa; }
    public void setTenKhoa(String tenKhoa) { this.tenKhoa = tenKhoa; }

    public int getSoLopGiangDay() { return soLopGiangDay; }
    public void setSoLopGiangDay(int soLopGiangDay) { this.soLopGiangDay = soLopGiangDay; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}