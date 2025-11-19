package com.example.doanlaptrinhhuongdoituong.controller.teacher;

import com.example.doanlaptrinhhuongdoituong.model.MainViewModel;
import com.example.doanlaptrinhhuongdoituong.view.HomeViewFactory;
import com.example.doanlaptrinhhuongdoituong.view.TeacherMenuOptions;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherNAVController implements Initializable {

    // Khai báo FXML từ file TeacherNAVView.fxml
    @FXML private Button trang_chu_btn;
    @FXML private Button qly_sv_btn;
    @FXML private Button nhap_diem_btn;
    @FXML private Button tke_bcao_btn;
    @FXML private Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addListeners();
    }

    private void addListeners() {
        // Gán hàm cho các nút
        trang_chu_btn.setOnAction(event -> onTrangChu());
        qly_sv_btn.setOnAction(event -> onQLSV());
        nhap_diem_btn.setOnAction(event -> onNhapDiem());
        tke_bcao_btn.setOnAction(event -> onThongKe());
        logout_btn.setOnAction(event -> onLogout());
    }

    // Các hàm này CHỈ CẬP NHẬT TRẠNG THÁI (Enum)
    // Giống hệt cách AdminNAVController hoạt động
    private void onTrangChu() {
        MainViewModel.getInstance().getViewFactory().getTeacherSelectedMenuItem().set(TeacherMenuOptions.TRANGCHU);
    }

    private void onQLSV() {
        MainViewModel.getInstance().getViewFactory().getTeacherSelectedMenuItem().set(TeacherMenuOptions.QUANLYSINHVIEN);
    }

    private void onNhapDiem() {
        MainViewModel.getInstance().getViewFactory().getTeacherSelectedMenuItem().set(TeacherMenuOptions.NHAPSUADIEM);
    }

    private void onThongKe() {
        MainViewModel.getInstance().getViewFactory().getTeacherSelectedMenuItem().set(TeacherMenuOptions.THONGKEBAOCAO);
    }

    private void onLogout() {
        // Sao chép logic từ AdminNAVController
        HomeViewFactory viewFactory = MainViewModel.getInstance().getViewFactory();
        Stage currentStage = (Stage) logout_btn.getScene().getWindow();
        viewFactory.showLoginWindow();
        viewFactory.closeStage(currentStage);
    }
}