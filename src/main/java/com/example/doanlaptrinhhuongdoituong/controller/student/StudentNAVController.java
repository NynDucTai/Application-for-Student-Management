package com.example.doanlaptrinhhuongdoituong.controller.student;

import com.example.doanlaptrinhhuongdoituong.model.MainViewModel;
import com.example.doanlaptrinhhuongdoituong.view.HomeViewFactory; // Import thêm
import com.example.doanlaptrinhhuongdoituong.view.StudentMenuOptions;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage; // Import thêm

import java.net.URL;
import java.util.ResourceBundle;

public class StudentNAVController implements Initializable {
    @FXML
    public Button HomeBtn;
    @FXML
    public Button DiemBtn;
    @FXML
    public Button MonBtn;
    @FXML
    public Button DKHPBtn;
    @FXML
    public Button TTCNBtn;
    @FXML
    public Button LogoutBtn; // Tên fx:id từ StudentNAVView.fxml
    @FXML
    public Button DKLNVBtn;
    @FXML
    public Button HoclaiBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners() {
        HomeBtn.setOnAction(event -> onStudentHomeView());
        DiemBtn.setOnAction(event -> onStudentScoreView());
        MonBtn.setOnAction(event -> onStudentCourtView());
        DKHPBtn.setOnAction(event -> onStudentCourtSignedView());
        TTCNBtn.setOnAction(event -> onStudentInfoView());
        DKLNVBtn.setOnAction(event -> onStudentWishClassView());
        HoclaiBtn.setOnAction(event -> onStudentRetakeClassView());

        // --- BƯỚC 1: Thêm listener cho nút đăng xuất ---
        LogoutBtn.setOnAction(event -> onLogout());
    }

    // --- BƯỚC 2: Thêm hàm onLogout() mới ---
    // (Sao chép logic từ AdminNAVController)
    private void onLogout() {
        // 1. Lấy HomeViewFactory
        HomeViewFactory viewFactory = MainViewModel.getInstance().getViewFactory();

        // 2. Lấy Stage (cửa sổ) hiện tại từ nút bấm
        Stage currentStage = (Stage) LogoutBtn.getScene().getWindow();

        // 3. Hiển thị màn hình Login
        viewFactory.showLoginWindow();

        // 4. Đóng màn hình Sinh viên (cửa sổ hiện tại)
        viewFactory.closeStage(currentStage);
    }

    // --- (Các hàm onStudent...View() khác giữ nguyên) ---

    private void onStudentHomeView() {
        MainViewModel.getInstance().getViewFactory().getStudentSelectedMenuItem().set(StudentMenuOptions.TRANGCHU);
    }

    private void onStudentScoreView() {
        MainViewModel.getInstance().getViewFactory().getStudentSelectedMenuItem().set(StudentMenuOptions.KETQUAHOCTAP);
    }

    private void onStudentCourtView() {
        MainViewModel.getInstance().getViewFactory().getStudentSelectedMenuItem().set(StudentMenuOptions.XEMLOPHOCPHAN);
    }

    private void onStudentCourtSignedView() {
        MainViewModel.getInstance().getViewFactory().getStudentSelectedMenuItem().set(StudentMenuOptions.DANGKILOPHOCPHAN);
    }

    private void onStudentInfoView() {
        MainViewModel.getInstance().getViewFactory().getStudentSelectedMenuItem().set(StudentMenuOptions.THONGTINCANHAN);
    }

    private void onStudentRetakeClassView() {
        MainViewModel.getInstance().getViewFactory().getStudentSelectedMenuItem().set(StudentMenuOptions.DANGKIHOCLAI);
    }

    private void onStudentWishClassView() {
        MainViewModel.getInstance().getViewFactory().getStudentSelectedMenuItem().set(StudentMenuOptions.DANGKILOPNGUYENVONG);
    }
}