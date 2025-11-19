package com.example.doanlaptrinhhuongdoituong.controller.student;

import com.example.doanlaptrinhhuongdoituong.model.Student.WishlistCourse;
import com.example.doanlaptrinhhuongdoituong.model.Student.StudentWishClassModel;
import com.example.doanlaptrinhhuongdoituong.util.UserSession;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentWishClassController implements Initializable {

    // --- Khai báo FXML ---
    @FXML private TreeTableView<WishlistCourse> lopNV_table;
    @FXML private TreeTableColumn<WishlistCourse, String> tenLopNV_col;
    @FXML private TreeTableColumn<WishlistCourse, String> tenMon_col;
    @FXML private TreeTableColumn<WishlistCourse, Number> soTInChi_col;
    @FXML private TreeTableColumn<WishlistCourse, String> hocKi_col;
    @FXML private TreeTableColumn<WishlistCourse, String> namHoc_col;
    @FXML private TextField lopDuocChon_field;
    @FXML private Button dangKi_button;

    // --- Biến logic ---
    private StudentWishClassModel model;
    private String maSinhVien;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.model = new StudentWishClassModel();

        // 1. Lấy maSV từ Session
        this.maSinhVien = UserSession.getInstance().getMaSinhVien();

        if (this.maSinhVien == null || this.maSinhVien.isEmpty()) {
            System.err.println("StudentWishClassController: maSinhVien là null!");
            return;
        }

        // 2. Cài đặt bảng
        setupTableColumns();

        // 3. Tải dữ liệu ban đầu
        loadWishlistTable();

        // 4. Gán sự kiện
        // Khi click vào 1 hàng -> cập nhật TextField
        lopNV_table.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> updateTextField(newSelection)
        );

        // Khi nhấn nút Đăng Ký
        dangKi_button.setOnAction(event -> handleRegisterButton());

        // 5. Làm cho TextField không thể sửa
        lopDuocChon_field.setEditable(false);
    }

    /**
     * Tải dữ liệu cho Bảng (Lớp Nguyện Vọng CÓ THỂ đăng ký)
     */
    private void loadWishlistTable() {
        ObservableList<WishlistCourse> courses = model.getAvailableWishlistCourses(maSinhVien);

        TreeItem<WishlistCourse> root = new TreeItem<>();
        for (WishlistCourse record : courses) {
            root.getChildren().add(new TreeItem<>(record));
        }
        lopNV_table.setRoot(root);
        lopNV_table.setShowRoot(false);

        // Xóa text field
        lopDuocChon_field.clear();
    }

    /**
     * Cập nhật TextField khi 1 hàng được chọn
     */
    private void updateTextField(TreeItem<WishlistCourse> selectedItem) {
        if (selectedItem != null && selectedItem.getValue() != null) {
            WishlistCourse course = selectedItem.getValue();
            // Yêu cầu: "hiển thị lớp NV đi cùng với tên môn học"
            lopDuocChon_field.setText(course.getMaLopHP() + " - " + course.getTenMon());
        } else {
            lopDuocChon_field.clear();
        }
    }

    /**
     * Xử lý khi nhấn nút Đăng ký
     */
    private void handleRegisterButton() {
        TreeItem<WishlistCourse> selectedItem = lopNV_table.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showAlert(Alert.AlertType.ERROR, "Chưa chọn lớp", "Vui lòng chọn một lớp nguyện vọng để đăng ký.");
            return;
        }

        WishlistCourse courseToRegister = selectedItem.getValue();
        String maLopHP = courseToRegister.getMaLopHP();
        String tenMon = courseToRegister.getTenMon();

        // Xác nhận
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận đăng ký");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn đăng ký lớp " + maLopHP + " (" + tenMon + ")?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Gọi Model để đăng ký
            boolean success = model.registerCourse(maSinhVien, maLopHP);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đăng ký thành công.");
                // Làm mới lại bảng
                loadWishlistTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Thất bại", "Đăng ký thất bại. Lớp có thể đã đủ hoặc có lỗi xảy ra.");
            }
        }
    }

    /**
     * Hiển thị thông báo
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Cài đặt CellValueFactory (chống lỗi Null)
     */
    private void setupTableColumns() {
        // Cột "Lớp NV" [tenLopNV_col] sẽ hiển thị Mã Lớp [maLopHPProperty]
        tenLopNV_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().maLopHPProperty());

        tenMon_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().tenMonProperty());
        soTInChi_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().soTinChiProperty());
        hocKi_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().hocKyProperty());
        namHoc_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().namHocProperty());
    }
}