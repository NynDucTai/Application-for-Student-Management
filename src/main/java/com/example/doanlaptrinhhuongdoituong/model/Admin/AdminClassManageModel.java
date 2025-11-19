package com.example.doanlaptrinhhuongdoituong.model.Admin;

/**
 * Model (POJO) đại diện cho một Lớp Học Phần (cho cả 3 bảng)
 */
public class AdminClassManageModel {

    // Dữ liệu từ bảng LopHocPhan
    private String maLopHP;
    private String maMon;
    private int hocKy;
    private String namHoc;
    private int siSoHienTai;
    private int siSoToiThieu;
    private int siSoToiDa;
    private String loaiLop;
    private String tenLopHP;

    // Dữ liệu JOIN từ bảng MonHoc
    private String tenMonHoc;
    private int soTinChi;

    // Constructor rỗng
    public AdminClassManageModel() {
    }

    // --- Getters và Setters (Bắt buộc) ---

    public String getMaLopHP() { return maLopHP; }
    public void setMaLopHP(String maLopHP) { this.maLopHP = maLopHP; }

    public String getMaMon() { return maMon; }
    public void setMaMon(String maMon) { this.maMon = maMon; }

    public int getHocKy() { return hocKy; }
    public void setHocKy(int hocKy) { this.hocKy = hocKy; }

    public String getNamHoc() { return namHoc; }
    public void setNamHoc(String namHoc) { this.namHoc = namHoc; }

    public int getSiSoHienTai() { return siSoHienTai; }
    public void setSiSoHienTai(int siSoHienTai) { this.siSoHienTai = siSoHienTai; }

    public int getSiSoToiThieu() { return siSoToiThieu; }
    public void setSiSoToiThieu(int siSoToiThieu) { this.siSoToiThieu = siSoToiThieu; }

    public int getSiSoToiDa() { return siSoToiDa; }
    public void setSiSoToiDa(int siSoToiDa) { this.siSoToiDa = siSoToiDa; }

    public String getLoaiLop() { return loaiLop; }
    public void setLoaiLop(String loaiLop) { this.loaiLop = loaiLop; }

    public String getTenLopHP() { return tenLopHP; }
    public void setTenLopHP(String tenLopHP) { this.tenLopHP = tenLopHP; }

    public String getTenMonHoc() { return tenMonHoc; }
    public void setTenMonHoc(String tenMonHoc) { this.tenMonHoc = tenMonHoc; }

    public int getSoTinChi() { return soTinChi; }
    public void setSoTinChi(int soTinChi) { this.soTinChi = soTinChi; }
}