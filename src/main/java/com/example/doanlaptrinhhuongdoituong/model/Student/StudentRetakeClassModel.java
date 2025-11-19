package com.example.doanlaptrinhhuongdoituong.model.Student;

import com.example.doanlaptrinhhuongdoituong.dao.Student.StudentRetakeClassDAO;
import javafx.collections.ObservableList;

public class StudentRetakeClassModel {

    private final StudentRetakeClassDAO dao;

    public StudentRetakeClassModel() {
        this.dao = new StudentRetakeClassDAO();
    }

    public ObservableList<RetakeCourse> getAvailableRetakeCourses(String maSV) {
        return dao.getAvailableRetakeCourses(maSV);
    }

    public boolean registerCourse(String maSV, String maLopHP) {
        return dao.registerCourse(maSV, maLopHP);
    }
}