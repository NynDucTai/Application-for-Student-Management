package com.example.doanlaptrinhhuongdoituong.model.Teacher;

import javafx.beans.property.*;

public class SinhVienDiem {
    private final SimpleIntegerProperty stt;
    private final SimpleStringProperty maSV;
    private final SimpleStringProperty hoTen;
    private final SimpleObjectProperty<Float> diemQT;
    private final SimpleObjectProperty<Float> diemKT;

    // --- BỔ SUNG THUỘC TÍNH MỚI ---
    private final SimpleObjectProperty<Float> diemTK; // Điểm tổng kết (hệ 10)

    private final SimpleObjectProperty<Float> diemHe4;
    private final SimpleStringProperty diemChu;
    private final SimpleStringProperty trangThai;

    public SinhVienDiem(int stt, String maSV, String hoTen, Float diemQT, Float diemKT, Float diemTK, Float diemHe4, String diemChu, String trangThai) {
        this.stt = new SimpleIntegerProperty(stt);
        this.maSV = new SimpleStringProperty(maSV);
        this.hoTen = new SimpleStringProperty(hoTen);
        this.diemQT = new SimpleObjectProperty<>(diemQT);
        this.diemKT = new SimpleObjectProperty<>(diemKT);

        // --- BỔ SUNG KHỞI TẠO ---
        this.diemTK = new SimpleObjectProperty<>(diemTK);

        this.diemHe4 = new SimpleObjectProperty<>(diemHe4);
        this.diemChu = new SimpleStringProperty(diemChu);
        this.trangThai = new SimpleStringProperty(trangThai);
    }

    // Getters
    public String getMaSV() { return maSV.get(); }
    public String getHoTen() { return hoTen.get(); }
    public Float getDiemQT() { return diemQT.get(); }
    public Float getDiemKT() { return diemKT.get(); }
    public String getDiemChu() { return diemChu.get(); }
    public String getTrangThai() { return trangThai.get(); }

    // Setters (Rất quan trọng cho việc cập nhật)
    public void setDiemQT(Float diemQT) { this.diemQT.set(diemQT); }
    public void setDiemKT(Float diemKT) { this.diemKT.set(diemKT); }

    // --- BỔ SUNG SETTER MỚI ---
    public void setDiemTK(Float diemTK) { this.diemTK.set(diemTK); }

    public void setDiemHe4(Float diemHe4) { this.diemHe4.set(diemHe4); }
    public void setDiemChu(String diemChu) { this.diemChu.set(diemChu); }
    public void setTrangThai(String trangThai) { this.trangThai.set(trangThai); }

    // Properties (Dùng cho TreeTableView)
    public SimpleIntegerProperty sttProperty() { return stt; }
    public SimpleStringProperty maSVProperty() { return maSV; }
    public SimpleStringProperty hoTenProperty() { return hoTen; }
    public SimpleObjectProperty<Float> diemQTProperty() { return diemQT; }
    public SimpleObjectProperty<Float> diemKTProperty() { return diemKT; }

    // --- BỔ SUNG PROPERTY MỚI ---
    public SimpleObjectProperty<Float> diemTKProperty() { return diemTK; }

    public SimpleObjectProperty<Float> diemHe4Property() { return diemHe4; }
    public SimpleStringProperty diemChuProperty() { return diemChu; }
    public SimpleStringProperty trangThaiProperty() { return trangThai; }
}