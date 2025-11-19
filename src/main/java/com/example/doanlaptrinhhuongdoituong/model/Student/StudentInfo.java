package com.example.doanlaptrinhhuongdoituong.model.Student;

import java.time.LocalDate;

// Chứa thông tin cho GridPane
public class StudentInfo {
    private final String maSV;
    private final String hoTen;
    private final String gioiTinh;
    private final String tenKhoa; // Ngành
    private final LocalDate ngaySinh;
    private final String lop; // Lớp (ví dụ: CNTT-K18A)
    private final String queQuan;
    // (Khóa học không có trong DB của bạn, sẽ lấy từ 'lop')

    public StudentInfo(String maSV, String hoTen, String gioiTinh, String tenKhoa, LocalDate ngaySinh, String lop, String queQuan) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.tenKhoa = tenKhoa;
        this.ngaySinh = ngaySinh;
        this.lop = lop;
        this.queQuan = queQuan;
    }

    // Getters
    public String getMaSV() { return maSV; }
    public String getHoTen() { return hoTen; }
    public String getGioiTinh() { return gioiTinh; }
    public String getTenKhoa() { return tenKhoa; }
    public String getNgaySinh() { return ngaySinh != null ? ngaySinh.toString() : ""; }
    public String getLop() { return lop; }
    public String getQueQuan() { return queQuan; }
    public String getKhoaHoc() {
        // Suy ra khóa học từ tên lớp
        if (lop != null && lop.contains("-K")) {
            return lop.substring(lop.indexOf("-K") + 1);
        }
        return "Không rõ";
    }
}