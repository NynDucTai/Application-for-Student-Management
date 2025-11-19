package com.example.doanlaptrinhhuongdoituong.model.Student;

// Chứa dữ liệu cho 2 ComboBox
public class Semester {
    private final int hocKy;
    private final String namHoc;

    public Semester(int hocKy, String namHoc) {
        this.hocKy = hocKy;
        this.namHoc = namHoc;
    }

    public int getHocKy() { return hocKy; }
    public String getNamHoc() { return namHoc; }

    @Override
    public String toString() {
        return "Học kỳ " + hocKy + " - " + namHoc;
    }
}