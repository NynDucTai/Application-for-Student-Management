package com.example.doanlaptrinhhuongdoituong.model.Teacher;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

public class SinhVienTrongLop {
    private final SimpleIntegerProperty stt;
    private final SimpleStringProperty maSV;
    private final SimpleStringProperty hoTen;
    private final SimpleStringProperty ngaySinh;
    private final SimpleStringProperty queQuan;
    private final SimpleStringProperty gioiTinh;
    private final SimpleStringProperty khoa;
    private final SimpleStringProperty lop;

    public SinhVienTrongLop(int stt, String maSV, String hoTen, LocalDate ngaySinh, String queQuan, String gioiTinh, String khoa, String lop) {
        this.stt = new SimpleIntegerProperty(stt);
        this.maSV = new SimpleStringProperty(maSV);
        this.hoTen = new SimpleStringProperty(hoTen);
        this.ngaySinh = new SimpleStringProperty(ngaySinh != null ? ngaySinh.toString() : "");
        this.queQuan = new SimpleStringProperty(queQuan);
        this.gioiTinh = new SimpleStringProperty(gioiTinh);
        this.khoa = new SimpleStringProperty(khoa);
        this.lop = new SimpleStringProperty(lop);
    }

    // Getters và Properties
    public int getStt() { return stt.get(); }
    public SimpleIntegerProperty sttProperty() { return stt; }

    public String getMaSV() { return maSV.get(); }
    public SimpleStringProperty maSVProperty() { return maSV; }

    public String getHoTen() { return hoTen.get(); }
    public SimpleStringProperty hoTenProperty() { return hoTen; }

    public String getNgaySinh() { return ngaySinh.get(); }
    public SimpleStringProperty ngaySinhProperty() { return ngaySinh; }

    public String getQueQuan() { return queQuan.get(); }
    public SimpleStringProperty queQuanProperty() { return queQuan; }

    public String getGioiTinh() { return gioiTinh.get(); }
    public SimpleStringProperty gioiTinhProperty() { return gioiTinh; }

    public String getKhoa() { return khoa.get(); }
    public SimpleStringProperty khoaProperty() { return khoa; }

    public String getLop() { return lop.get(); }
    public SimpleStringProperty lopProperty() { return lop; }
}