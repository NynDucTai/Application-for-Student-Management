package com.example.doanlaptrinhhuongdoituong.model.Student;

import com.example.doanlaptrinhhuongdoituong.dao.Student.StudentHomeDAO;
import javafx.collections.ObservableList;

public class StudentHomeModel {

    private final StudentHomeDAO dao;

    public StudentHomeModel() {
        this.dao = new StudentHomeDAO();
    }

    public StudentInfo getStudentInfo(String maSV) {
        return dao.getStudentInfo(maSV);
    }

    public ObservableList<Semester> getSemesters(String maSV) {
        return dao.getSemesters(maSV);
    }

    public ObservableList<CourseRegistration> getCoursesAndScores(String maSV, int hocKy, String namHoc) {
        return dao.getCoursesAndScores(maSV, hocKy, namHoc);
    }
}