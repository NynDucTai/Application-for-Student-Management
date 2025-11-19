package com.example.doanlaptrinhhuongdoituong.controller.student;

import com.example.doanlaptrinhhuongdoituong.model.MainViewModel;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentController implements Initializable {
    @FXML
    public BorderPane student_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainViewModel.getInstance().getViewFactory().getStudentSelectedMenuItem().addListener((observableValue, oldValue, newValue) -> {
            switch (newValue) {
                case KETQUAHOCTAP -> student_parent.setCenter(MainViewModel.getInstance().getViewFactory().getStudentScoreView());
                case XEMLOPHOCPHAN -> student_parent.setCenter(MainViewModel.getInstance().getViewFactory().getStudentCourtView());
                case DANGKILOPHOCPHAN -> student_parent.setCenter(MainViewModel.getInstance().getViewFactory().getStudentCourtSignedView());
                case THONGTINCANHAN -> student_parent.setCenter(MainViewModel.getInstance().getViewFactory().getStudentInfoView());
                case DANGKIHOCLAI -> student_parent.setCenter(MainViewModel.getInstance().getViewFactory().getStudentRetakeClassView());
                case DANGKILOPNGUYENVONG -> student_parent.setCenter(MainViewModel.getInstance().getViewFactory().getStudentWishClassView());
                default -> student_parent.setCenter(MainViewModel.getInstance().getViewFactory().getStudentHomeView());
            }
        });
    }
}
