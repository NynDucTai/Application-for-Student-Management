package com.example.doanlaptrinhhuongdoituong.model.Admin;

/**
 * Model (POJO) đại diện cho một Môn học.
 * Chứa tất cả dữ liệu cần thiết cho giao diện.
 */
public class AdminSubjectManageModel {

    private String maMon;
    private String tenMon;
    private String maKhoa;    // Dùng cho ComboBox (ví dụ: 'CNTT')
    private String tenKhoa;   // Dùng để hiển thị trong bảng (ví dụ: 'Công nghệ thông tin')
    private int soTinChi;
    private int hocKi;
    private String namHoc;
    private String loaiMon;

    // Constructor rỗng
    public AdminSubjectManageModel() {
    }

    // --- Getters và Setters (Bắt buộc) ---
    // (JavaFX TableView sẽ dùng các hàm này để lấy dữ liệu)

    public String getMaMon() { return maMon; }
    public void setMaMon(String maMon) { this.maMon = maMon; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }

    public String getTenKhoa() { return tenKhoa; }
    public void setTenKhoa(String tenKhoa) { this.tenKhoa = tenKhoa; }

    public int getSoTinChi() { return soTinChi; }
    public void setSoTinChi(int soTinChi) { this.soTinChi = soTinChi; }

    public int getHocKi() { return hocKi; }
    public void setHocKi(int hocKi) { this.hocKi = hocKi; }

    public String getNamHoc() { return namHoc; }
    public void setNamHoc(String namHoc) { this.namHoc = namHoc; }

    public String getLoaiMon() { return loaiMon; }
    public void setLoaiMon(String loaiMon) { this.loaiMon = loaiMon; }
}