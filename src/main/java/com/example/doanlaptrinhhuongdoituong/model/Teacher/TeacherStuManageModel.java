package com.example.doanlaptrinhhuongdoituong.model.Teacher;

import com.example.doanlaptrinhhuongdoituong.dao.Teacher.TeacherStuManageDAO;
import javafx.collections.ObservableList;

public class TeacherStuManageModel {

    private final TeacherStuManageDAO dao;

    public TeacherStuManageModel() {
        this.dao = new TeacherStuManageDAO();
    }

    public ObservableList<LopDayHoc> getLopGiangDay(String maGV) {
        return dao.getLopGiangDay(maGV);
    }

    public ObservableList<SinhVienTrongLop> getSinhVien(String maLopHP, String searchText) {
        return dao.getSinhVienTrongLop(maLopHP, searchText);
    }
}