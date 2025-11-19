package com.example.doanlaptrinhhuongdoituong.model.Admin;

import java.time.LocalDate;

/**
 * Model (POJO) đại diện cho một Lớp Nguyện Vọng.
 * Tương ứng với 1 dòng trong bảng LopHocPhan.
 */
public class AdminClassVerifyModel {

    private String maLopHP;
    private String maMon;
    private String tenMon; // Lấy từ bảng MonHoc
    private int hocKy;
    private String namHoc;
    private int soTinChi;
    private int siSoToiThieu;
    private int siSoToiDa;
    private int siSoHienTai;
    private LocalDate hanDangKi;
    private String trangThai;

    // Constructor rỗng
    public AdminClassVerifyModel() {
    }

    // --- Getters và Setters (Bắt buộc) ---

    public String getMaLopHP() { return maLopHP; }
    public void setMaLopHP(String maLopHP) { this.maLopHP = maLopHP; }

    public String getMaMon() { return maMon; }
    public void setMaMon(String maMon) { this.maMon = maMon; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public int getHocKy() { return hocKy; }
    public void setHocKy(int hocKy) { this.hocKy = hocKy; }

    public String getNamHoc() { return namHoc; }
    public void setNamHoc(String namHoc) { this.namHoc = namHoc; }

    public int getSoTinChi() { return soTinChi; }
    public void setSoTinChi(int soTinChi) { this.soTinChi = soTinChi; }

    public int getSiSoToiThieu() { return siSoToiThieu; }
    public void setSiSoToiThieu(int siSoToiThieu) { this.siSoToiThieu = siSoToiThieu; }

    public int getSiSoToiDa() { return siSoToiDa; }
    public void setSiSoToiDa(int siSoToiDa) { this.siSoToiDa = siSoToiDa; }

    public int getSiSoHienTai() { return siSoHienTai; }
    public void setSiSoHienTai(int siSoHienTai) { this.siSoHienTai = siSoHienTai; }

    public LocalDate getHanDangKi() { return hanDangKi; }
    public void setHanDangKi(LocalDate hanDangKi) { this.hanDangKi = hanDangKi; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}