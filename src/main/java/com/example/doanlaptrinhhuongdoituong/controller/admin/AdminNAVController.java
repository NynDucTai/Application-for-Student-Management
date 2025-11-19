package com.example.doanlaptrinhhuongdoituong.controller.admin;

import com.example.doanlaptrinhhuongdoituong.model.MainViewModel;
import com.example.doanlaptrinhhuongdoituong.view.AdminMenuOptions;
import com.example.doanlaptrinhhuongdoituong.view.HomeViewFactory; // <-- Bổ sung import
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage; // <-- Bổ sung import

import java.net.URL;
import java.util.ResourceBundle;

public class AdminNAVController implements Initializable{
    @FXML
    public Button PCGDBtn;
    @FXML
    public Button QLSVBtn;
    @FXML
    public Button QLMHBtn;
    @FXML
    public Button QLKBtn;
    @FXML
    public Button QLGVBtn;
    @FXML
    public Button MLNVBtn;
    @FXML
    public Button MLHLBtn;
    @FXML
    public Button TKBCBtn;
    @FXML
    public Button QLLHPBtn;

    // --- BƯỚC 1: Thêm khai báo @FXML cho nút Đăng xuất ---
    @FXML
    public Button dangXuat_button;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addListeners();
    }

    private void addListeners() {
        PCGDBtn.setOnAction(event -> onAdminAssignView());
        QLSVBtn.setOnAction(event -> onAdminStuManageView());
        QLMHBtn.setOnAction(event -> onAdminSubjectManageView());
        QLGVBtn.setOnAction(event -> onAdminTeacherManageView());
        QLKBtn.setOnAction(event -> onAdminDepartmentView());
        MLNVBtn.setOnAction(event -> onAdminClassVerifyView());
        MLHLBtn.setOnAction(event -> onAdminRetakeClassView());
        TKBCBtn.setOnAction(event -> onAdminStaticView());
        QLLHPBtn.setOnAction(event -> onAdminClassManageView());

        // --- BƯỚC 2: Thêm listener cho nút đăng xuất ---
        dangXuat_button.setOnAction(event -> onLogout());
    }

    // --- BƯỚC 3: Thêm hàm onLogout() mới ---
    private void onLogout() {
        // 1. Lấy HomeViewFactory
        HomeViewFactory viewFactory = MainViewModel.getInstance().getViewFactory();

        // 2. Lấy Stage (cửa sổ) hiện tại từ nút bấm
        Stage currentStage = (Stage) dangXuat_button.getScene().getWindow();

        // 3. Hiển thị màn hình Login
        viewFactory.showLoginWindow();

        // 4. Đóng màn hình Admin (cửa sổ hiện tại)
        viewFactory.closeStage(currentStage);
    }

    // --- (Các hàm onAdmin...View() khác giữ nguyên) ---

    public void onAdminAssignView() {
        MainViewModel.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.PHANCONGGIANGDAY);
    }

    public void onAdminStuManageView() {
        MainViewModel.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.QUANLYSINHVIEN);
    }

    public void onAdminSubjectManageView() {
        MainViewModel.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.QUANLYMONHOC);
    }

    public void onAdminTeacherManageView() {
        MainViewModel.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.QUANLYGIANGVIEN);
    }

    public void onAdminDepartmentView() {
        MainViewModel.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.QUANLYKHOA);
    }

    public void onAdminClassVerifyView() {
        MainViewModel.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.MOLOPNGUYENVONG);
    }

    public void onAdminRetakeClassView() {
        MainViewModel.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.MOLOPHOCLAI);
    }

    public void onAdminStaticView() {
        MainViewModel.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.THONGKEBAOCAO);
    }

    public void onAdminClassManageView() {
        MainViewModel.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.QUANLYLOPHOCPHAN);
    }
}