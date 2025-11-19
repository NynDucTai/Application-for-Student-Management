package com.example.doanlaptrinhhuongdoituong.model.Teacher;

import com.example.doanlaptrinhhuongdoituong.dao.Teacher.TeacherHomeDAO;
import javafx.collections.ObservableList;

// Import lớp POJO vừa tạo
import com.example.doanlaptrinhhuongdoituong.model.Teacher.LopPhanCong;

public class TeacherHomeModel {

    private final TeacherHomeDAO dao;

    public TeacherHomeModel() {
        this.dao = new TeacherHomeDAO(); // Khởi tạo DAO
    }

    public String getTenGiangVien(String maGV) {
        return dao.getTenGiangVien(maGV);
    }

    public ObservableList<String> loadKiHoc() {
        return dao.getDanhSachKiHoc();
    }

    public ObservableList<String> loadNamHoc() {
        return dao.getDanhSachNamHoc();
    }

    public ObservableList<LopPhanCong> loadLopPhanCong(String maGV, String kiHoc, String namHoc) {
        // Chỉ gọi DAO nếu cả 3 tham số đều hợp lệ
        if (maGV == null || kiHoc == null || namHoc == null) {
            return null; // Hoặc trả về danh sách rỗng
        }
        return dao.getLopPhanCong(maGV, kiHoc, namHoc);
    }
}