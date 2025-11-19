package com.example.doanlaptrinhhuongdoituong.controller.teacher;

import com.example.doanlaptrinhhuongdoituong.model.MainViewModel;
import com.example.doanlaptrinhhuongdoituong.view.TeacherMenuOptions; // Import enum của Teacher
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

// File này là controller cho TeacherHome.fxml (BorderPane)
public class TeacherController implements Initializable {

    // fx:id="teacher_parent" trong file TeacherHome.fxml
    @FXML
    public BorderPane teacher_parent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Lắng nghe sự thay đổi menu (giống hệt AdminController)
        MainViewModel.getInstance().getViewFactory().getTeacherSelectedMenuItem().addListener((observable, oldValue, newValue) -> {
            // Dùng switch-case với TeacherMenuOptions
            switch (newValue) {
                case QUANLYSINHVIEN -> teacher_parent.setCenter(MainViewModel.getInstance().getViewFactory().getTeacherStuManageView());
                case NHAPSUADIEM -> teacher_parent.setCenter(MainViewModel.getInstance().getViewFactory().getTeacherAddScoreView());
                case THONGKEBAOCAO -> teacher_parent.setCenter(MainViewModel.getInstance().getViewFactory().getTeacherStaticView());
                    default -> teacher_parent.setCenter(MainViewModel.getInstance().getViewFactory().getTeacherHomeView());
            }
        });
    }
}