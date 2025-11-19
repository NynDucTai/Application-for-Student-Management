package com.example.doanlaptrinhhuongdoituong.model.Student;

// Chứa dữ liệu cho ListView và BarChart
public class CourseRegistration {
    private final String tenMonHoc;
    private final int soTinChi;
    private final Float diemHe4; // Vẫn giữ lại

    // --- BỔ SUNG ĐIỂM HỆ 10 ---
    private final Float diemTK;

    public CourseRegistration(String tenMonHoc, int soTinChi, Float diemHe4, Float diemTK) {
        this.tenMonHoc = tenMonHoc;
        this.soTinChi = soTinChi;
        this.diemHe4 = diemHe4;
        this.diemTK = diemTK; // Thêm vào constructor
    }

    public String getTenMonHoc() { return tenMonHoc; }
    public int getSoTinChi() { return soTinChi; }
    public Float getDiemHe4() { return diemHe4; }

    // --- BỔ SUNG GETTER MỚI ---
    public Float getDiemTK() { return diemTK; }
}