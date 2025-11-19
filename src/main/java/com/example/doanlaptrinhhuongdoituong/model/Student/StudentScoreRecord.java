package com.example.doanlaptrinhhuongdoituong.model.Student;

import javafx.beans.property.*;

// Lớp này chứa dữ liệu cho mỗi hàng của Bảng điểm
public class StudentScoreRecord {
    private final SimpleStringProperty maHP;
    private final SimpleStringProperty tenMonHoc;
    private final SimpleStringProperty lopHP;
    private final SimpleIntegerProperty soTinChi;
    private final SimpleObjectProperty<Float> diemQT;
    private final SimpleObjectProperty<Float> diemKT;
    private final SimpleObjectProperty<Float> diemTK; // Hệ 10
    private final SimpleObjectProperty<Float> diemHe4;
    private final SimpleStringProperty diemChu;
    private final SimpleStringProperty trangThai; // Đạt/Trượt

    public StudentScoreRecord(String maHP, String tenMonHoc, String lopHP, int soTinChi, Float diemQT, Float diemKT, Float diemHe4, String diemChu, String trangThai) {
        this.maHP = new SimpleStringProperty(maHP);
        this.tenMonHoc = new SimpleStringProperty(tenMonHoc);
        this.lopHP = new SimpleStringProperty(lopHP);
        this.soTinChi = new SimpleIntegerProperty(soTinChi);
        this.diemQT = new SimpleObjectProperty<>(diemQT);
        this.diemKT = new SimpleObjectProperty<>(diemKT);

        // Tự động tính điểm TK (hệ 10) theo công thức (QT+KT)/2
        Float diemTK_calc = null;
        if (diemQT != null && diemKT != null) {
            diemTK_calc = (diemQT + diemKT) / 2.0f;
        }
        this.diemTK = new SimpleObjectProperty<>(diemTK_calc);

        this.diemHe4 = new SimpleObjectProperty<>(diemHe4);
        this.diemChu = new SimpleStringProperty(diemChu);
        this.trangThai = new SimpleStringProperty(trangThai);
    }

    // Properties cho TreeTableView
    public StringProperty maHPProperty() { return maHP; }
    public StringProperty tenMonHocProperty() { return tenMonHoc; }
    public StringProperty lopHPProperty() { return lopHP; }
    public IntegerProperty soTinChiProperty() { return soTinChi; }
    public ObjectProperty<Float> diemQTProperty() { return diemQT; }
    public ObjectProperty<Float> diemKTProperty() { return diemKT; }
    public ObjectProperty<Float> diemTKProperty() { return diemTK; }
    public ObjectProperty<Float> diemHe4Property() { return diemHe4; }
    public StringProperty diemChuProperty() { return diemChu; }
    public StringProperty trangThaiProperty() { return trangThai; }
}   