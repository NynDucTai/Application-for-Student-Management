package com.example.doanlaptrinhhuongdoituong.model.Student;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

// Lớp này chứa dữ liệu cho mỗi hàng của Bảng Lớp học phần
public class StudentCourtRecord {
    private final SimpleStringProperty maHP;
    private final SimpleStringProperty lopHP;
    private final SimpleStringProperty tenMonHoc;
    private final SimpleIntegerProperty soTinChi;
    private final SimpleStringProperty hocKy;
    private final SimpleStringProperty namHoc;

    public StudentCourtRecord(String maHP, String lopHP, String tenMonHoc, int soTinChi, int hocKy, String namHoc) {
        this.maHP = new SimpleStringProperty(maHP);
        this.lopHP = new SimpleStringProperty(lopHP);
        this.tenMonHoc = new SimpleStringProperty(tenMonHoc);
        this.soTinChi = new SimpleIntegerProperty(soTinChi);
        this.hocKy = new SimpleStringProperty(String.valueOf(hocKy));
        this.namHoc = new SimpleStringProperty(namHoc);
    }

    // Properties cho TreeTableView
    public StringProperty maHPProperty() { return maHP; }
    public StringProperty lopHPProperty() { return lopHP; }
    public StringProperty tenMonHocProperty() { return tenMonHoc; }
    public IntegerProperty soTinChiProperty() { return soTinChi; }
    public StringProperty hocKyProperty() { return hocKy; }
    public StringProperty namHocProperty() { return namHoc; }
}