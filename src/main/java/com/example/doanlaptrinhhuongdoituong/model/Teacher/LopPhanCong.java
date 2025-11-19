package com.example.doanlaptrinhhuongdoituong.model.Teacher;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Lớp này là một POJO (Plain Old Java Object)
 * dùng để chứa dữ liệu cho mỗi hàng trong TreeTableView.
 */
public class LopPhanCong {
    private final SimpleStringProperty maHP;
    private final SimpleStringProperty tenMon;
    private final SimpleStringProperty lopHP;
    private final SimpleIntegerProperty soTinChi;
    private final SimpleIntegerProperty siSo;
    private final SimpleStringProperty kiHoc;
    private final SimpleStringProperty namHoc;

    public LopPhanCong(String maHP, String tenMon, String lopHP, int soTinChi, int siSo, String kiHoc, String namHoc) {
        this.maHP = new SimpleStringProperty(maHP);
        this.tenMon = new SimpleStringProperty(tenMon);
        this.lopHP = new SimpleStringProperty(lopHP);
        this.soTinChi = new SimpleIntegerProperty(soTinChi);
        this.siSo = new SimpleIntegerProperty(siSo);
        this.kiHoc = new SimpleStringProperty(kiHoc);
        this.namHoc = new SimpleStringProperty(namHoc);
    }

    // Getters và Property Getters
    public String getMaHP() { return maHP.get(); }
    public SimpleStringProperty maHPProperty() { return maHP; }

    public String getTenMon() { return tenMon.get(); }
    public SimpleStringProperty tenMonProperty() { return tenMon; }

    public String getLopHP() { return lopHP.get(); }
    public SimpleStringProperty lopHPProperty() { return lopHP; }

    public int getSoTinChi() { return soTinChi.get(); }
    public SimpleIntegerProperty soTinChiProperty() { return soTinChi; }

    public int getSiSo() { return siSo.get(); }
    public SimpleIntegerProperty siSoProperty() { return siSo; }

    public String getKiHoc() { return kiHoc.get(); }
    public SimpleStringProperty kiHocProperty() { return kiHoc; }

    public String getNamHoc() { return namHoc.get(); }
    public SimpleStringProperty namHocProperty() { return namHoc; }
}