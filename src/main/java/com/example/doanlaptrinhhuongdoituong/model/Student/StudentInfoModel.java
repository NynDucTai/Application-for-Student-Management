package com.example.doanlaptrinhhuongdoituong.model.Student;

import com.example.doanlaptrinhhuongdoituong.dao.Student.StudentInfoDAO;

public class StudentInfoModel {

    private final StudentInfoDAO dao;

    public StudentInfoModel() {
        this.dao = new StudentInfoDAO();
    }

    public StudentInfo getStudentInfo(String maSV) {
        return dao.getStudentInfo(maSV);
    }
}