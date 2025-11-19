package com.example.doanlaptrinhhuongdoituong.model.Student;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// Lớp này chứa dữ liệu cho bảng "Lớp có thể đăng ký"
public class AvailableCourse {
    private final StringProperty maHP;
    private final StringProperty tenMon;
    private final String maLopHP; // Mã lớp (ẩn, dùng để đăng ký)
    private final StringProperty tenLopHP; // Tên lớp (hiển thị)
    private final IntegerProperty soTinChi;
    private final StringProperty namHoc;

    public AvailableCourse(String maHP, String tenMon, String maLopHP, String tenLopHP, int soTinChi, String namHoc) {
        this.maHP = new SimpleStringProperty(maHP);
        this.tenMon = new SimpleStringProperty(tenMon);
        this.maLopHP = maLopHP; // Mã lớp (ẩn)
        this.tenLopHP = new SimpleStringProperty(tenLopHP); // Tên lớp
        this.soTinChi = new SimpleIntegerProperty(soTinChi);
        this.namHoc = new SimpleStringProperty(namHoc);
    }

    // Getter cho mã lớp (dùng khi nhấn Đăng ký)
    public String getMaLopHP() { return maLopHP; }

    // Properties cho TreeTableView
    public StringProperty maHPProperty() { return maHP; }
    public StringProperty tenMonProperty() { return tenMon; }
    public StringProperty tenLopHPProperty() { return tenLopHP; } // Cột "Lớp học phần" sẽ dùng cái này
    public IntegerProperty soTinChiProperty() { return soTinChi; }
    public StringProperty namHocProperty() { return namHoc; }
}