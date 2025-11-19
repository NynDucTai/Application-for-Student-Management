package com.example.doanlaptrinhhuongdoituong.model.Admin;

import com.example.doanlaptrinhhuongdoituong.dao.Admin.AdminStudentScoreDAO;
import com.example.doanlaptrinhhuongdoituong.model.Student.StudentScoreRecord;
import javafx.collections.ObservableList;

public class AdminStudentScoreModel {

    private final AdminStudentScoreDAO dao;

    public AdminStudentScoreModel() {
        this.dao = new AdminStudentScoreDAO();
    }

    public ObservableList<String> getHocKy(String maSV) {
        return dao.getHocKy(maSV);
    }

    public ObservableList<String> getNamHoc(String maSV) {
        return dao.getNamHoc(maSV);
    }

    public ObservableList<StudentScoreRecord> getScores(String maSV, String hocKy, String namHoc) {
        return dao.getScores(maSV, hocKy, namHoc);
    }
}