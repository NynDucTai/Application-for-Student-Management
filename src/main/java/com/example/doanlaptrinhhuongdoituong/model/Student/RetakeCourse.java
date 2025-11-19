package com.example.doanlaptrinhhuongdoituong.model.Student;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// Lớp này chứa dữ liệu cho bảng "Lớp học lại"
public class RetakeCourse {
    private final StringProperty maMon;
    private final StringProperty maLopHL; // Mã lớp học lại (maLopHP)
    private final StringProperty tenMonHoc;
    private final IntegerProperty soTinChi;
    private final StringProperty hocKy;
    private final StringProperty namHoc;

    public RetakeCourse(String maMon, String maLopHL, String tenMonHoc, int soTinChi, int hocKy, String namHoc) {
        this.maMon = new SimpleStringProperty(maMon);
        this.maLopHL = new SimpleStringProperty(maLopHL);
        this.tenMonHoc = new SimpleStringProperty(tenMonHoc);
        this.soTinChi = new SimpleIntegerProperty(soTinChi);
        this.hocKy = new SimpleStringProperty(String.valueOf(hocKy));
        this.namHoc = new SimpleStringProperty(namHoc);
    }

    // Getters (dùng cho Controller)
    public String getMaLopHL() { return maLopHL.get(); }
    public String getTenMonHoc() { return tenMonHoc.get(); }

    // Properties (dùng cho TreeTableView)
    public StringProperty maMonProperty() { return maMon; }
    public StringProperty maLopHLProperty() { return maLopHL; }
    public StringProperty tenMonHocProperty() { return tenMonHoc; }
    public IntegerProperty soTinChiProperty() { return soTinChi; }
    public StringProperty hocKyProperty() { return hocKy; }
    public StringProperty namHocProperty() { return namHoc; }
}