package com.example.doanlaptrinhhuongdoituong.model.Admin; // (Hoặc package của bạn)

import java.time.LocalDate;

// Đây là một POJO (Plain Old Java Object) đơn giản
// Nó chứa tất cả các trường bạn cần
public class AdminStuManageModel {
    private String maSV;
    private String hoTen;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String queQuan;
    private String tenKhoa;
    private String maKhoa;
    private String lop; // Tương ứng với 'tenLopHP' trong FXML của bạn
    private String username;
    private String password;

    // Tạo một constructor rỗng
    public AdminStuManageModel() {
    }

    // Tạo constructor đầy đủ
    public AdminStuManageModel(String maSV, String hoTen, LocalDate ngaySinh, String gioiTinh,
                   String queQuan, String tenKhoa, String lop,
                   String username, String password) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.queQuan = queQuan;
        this.tenKhoa = tenKhoa;
        this.lop = lop;
        this.username = username;
        this.password = password;
    }

    // --- Thêm đầy đủ Getters và Setters cho tất cả các trường ---
    // (Bắt buộc phải có để JavaFX TreeTableView hoạt động)

    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getQueQuan() { return queQuan; }
    public void setQueQuan(String queQuan) { this.queQuan = queQuan; }

    public String getTenKhoa() { return tenKhoa; }
    public void setTenKhoa(String tenKhoa) { this.tenKhoa = tenKhoa; }

    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }

    public String getLop() { return lop; }
    public void setLop(String lop) { this.lop = lop; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}