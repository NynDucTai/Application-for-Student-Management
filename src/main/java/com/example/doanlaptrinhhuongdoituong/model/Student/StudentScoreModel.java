package com.example.doanlaptrinhhuongdoituong.model.Student;

import com.example.doanlaptrinhhuongdoituong.dao.Student.StudentScoreDAO;
import javafx.collections.ObservableList;

public class StudentScoreModel {

    private final StudentScoreDAO dao;

    public StudentScoreModel() {
        this.dao = new StudentScoreDAO();
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