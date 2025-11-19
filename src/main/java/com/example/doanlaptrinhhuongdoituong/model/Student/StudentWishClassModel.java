package com.example.doanlaptrinhhuongdoituong.model.Student;

import com.example.doanlaptrinhhuongdoituong.dao.Student.StudentWishClassDAO;
import javafx.collections.ObservableList;

public class StudentWishClassModel {

    private final StudentWishClassDAO dao;

    public StudentWishClassModel() {
        this.dao = new StudentWishClassDAO();
    }

    public ObservableList<WishlistCourse> getAvailableWishlistCourses(String maSV) {
        return dao.getAvailableWishlistCourses(maSV);
    }

    public boolean registerCourse(String maSV, String maLopHP) {
        return dao.registerCourse(maSV, maLopHP);
    }
}