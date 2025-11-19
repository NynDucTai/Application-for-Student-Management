package com.example.doanlaptrinhhuongdoituong.model.Student;

import com.example.doanlaptrinhhuongdoituong.dao.Student.StudentCourtDAO;
import javafx.collections.ObservableList;

public class StudentCourtModel {

    private final StudentCourtDAO dao;

    public StudentCourtModel() {
        this.dao = new StudentCourtDAO();
    }

    public ObservableList<String> getHocKy(String maSV) {
        return dao.getHocKy(maSV);
    }

    public ObservableList<String> getNamHoc(String maSV) {
        return dao.getNamHoc(maSV);
    }

    public ObservableList<StudentCourtRecord> getCourses(String maSV, String hocKy, String namHoc) {
        return dao.getCourses(maSV, hocKy, namHoc);
    }
}