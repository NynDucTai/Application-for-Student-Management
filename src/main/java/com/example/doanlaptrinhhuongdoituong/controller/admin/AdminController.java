package com.example.doanlaptrinhhuongdoituong.controller.admin;

import com.example.doanlaptrinhhuongdoituong.model.MainViewModel;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML
    public BorderPane admin_parent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainViewModel.getInstance().getViewFactory().getAdminSelectedMenuItem().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case QUANLYSINHVIEN -> admin_parent.setCenter(MainViewModel.getInstance().getViewFactory().getAdminStuManageView());
                case QUANLYMONHOC ->  admin_parent.setCenter(MainViewModel.getInstance().getViewFactory().getAdminSubjectManageView());
                case QUANLYKHOA ->  admin_parent.setCenter(MainViewModel.getInstance().getViewFactory().getAdminDepartmentView());
                case QUANLYGIANGVIEN -> admin_parent.setCenter(MainViewModel.getInstance().getViewFactory().getAdminTeacherManageView());
                case QUANLYLOPHOCPHAN -> admin_parent.setCenter(MainViewModel.getInstance().getViewFactory().getAdminClassManageView());
                case MOLOPNGUYENVONG ->  admin_parent.setCenter(MainViewModel.getInstance().getViewFactory().getAdminClassVerifyView());
                case MOLOPHOCLAI ->  admin_parent.setCenter(MainViewModel.getInstance().getViewFactory().getAdminRetakeClassView());
                case THONGKEBAOCAO ->  admin_parent.setCenter(MainViewModel.getInstance().getViewFactory().getAdminStaticView());
                default -> admin_parent.setCenter(MainViewModel.getInstance().getViewFactory().getAdminAssignView());
            }
        });
    }
}