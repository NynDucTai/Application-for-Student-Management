package com.example.doanlaptrinhhuongdoituong.model.Student;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// Lớp này chứa dữ liệu cho bảng "Lớp nguyện vọng"
public class WishlistCourse {
    private final StringProperty maLopHP; // Dùng cho cột "Lớp NV"
    private final StringProperty tenMon;
    private final IntegerProperty soTinChi;
    private final StringProperty hocKy;
    private final StringProperty namHoc;

    public WishlistCourse(String maLopHP, String tenMon, int soTinChi, int hocKy, String namHoc) {
        this.maLopHP = new SimpleStringProperty(maLopHP);
        this.tenMon = new SimpleStringProperty(tenMon);
        this.soTinChi = new SimpleIntegerProperty(soTinChi);
        this.hocKy = new SimpleStringProperty(String.valueOf(hocKy));
        this.namHoc = new SimpleStringProperty(namHoc);
    }

    // Getters (dùng cho Controller)
    public String getMaLopHP() { return maLopHP.get(); }
    public String getTenMon() { return tenMon.get(); }

    // Properties (dùng cho TreeTableView)
    public StringProperty maLopHPProperty() { return maLopHP; } // Cột 'tenLopNV_col' sẽ dùng cái này
    public StringProperty tenMonProperty() { return tenMon; }
    public IntegerProperty soTinChiProperty() { return soTinChi; }
    public StringProperty hocKyProperty() { return hocKy; }
    public StringProperty namHocProperty() { return namHoc; }
}