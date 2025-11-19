package com.example.doanlaptrinhhuongdoituong.model.Student;

import com.example.doanlaptrinhhuongdoituong.dao.Student.StudentCourtSignedDAO;
import javafx.collections.ObservableList;

public class StudentCourtSignedModel {

    private final StudentCourtSignedDAO dao;

    public StudentCourtSignedModel() {
        this.dao = new StudentCourtSignedDAO();
    }

    // Lấy tất cả các kỳ/năm
    public ObservableList<String> getAllHocKy() {
        return dao.getAllHocKy();
    }
    public ObservableList<String> getAllNamHoc() {
        return dao.getAllNamHoc();
    }

    // Lấy danh sách lớp
    public ObservableList<AvailableCourse> getAvailableCourses(String maSV, String hocKy, String namHoc) {
        return dao.getAvailableCourses(maSV, hocKy, namHoc);
    }

    // Đăng ký
    public boolean registerCourse(String maSV, String maLopHP) {
        return dao.registerCourse(maSV, maLopHP);
    }
}