package com.example.doanlaptrinhhuongdoituong.controller.student;

import com.example.doanlaptrinhhuongdoituong.model.Student.RetakeCourse;
import com.example.doanlaptrinhhuongdoituong.model.Student.StudentRetakeClassModel;
import com.example.doanlaptrinhhuongdoituong.util.UserSession;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentRetakeClassController implements Initializable {

    // --- Khai báo FXML ---
    // Đảm bảo bạn đã thêm fx:id="retake_table" vào FXML
    @FXML private TreeTableView<RetakeCourse> retake_table;

    @FXML private TreeTableColumn<RetakeCourse, String> maMon_col;
    @FXML private TreeTableColumn<RetakeCourse, String> maLopHL_col;
    @FXML private TreeTableColumn<RetakeCourse, String> tenMonHoc_col;
    @FXML private TreeTableColumn<RetakeCourse, Number> soTinChi_col;
    @FXML private TreeTableColumn<RetakeCourse, String> hocKi_col;
    @FXML private TreeTableColumn<RetakeCourse, String> namHoc_col;

    @FXML private TextField monDuocChon_field;
    @FXML private Button dangKi_button;

    // --- Biến logic ---
    private StudentRetakeClassModel model;
    private String maSinhVien;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.model = new StudentRetakeClassModel();

        // 1. Lấy maSV từ Session
        this.maSinhVien = UserSession.getInstance().getMaSinhVien();

        if (this.maSinhVien == null || this.maSinhVien.isEmpty()) {
            System.err.println("StudentRetakeClassController: maSinhVien là null!");
            return;
        }

        // 2. Cài đặt bảng
        setupTableColumns();

        // 3. Tải dữ liệu ban đầu
        loadRetakeTable();

        // 4. Gán sự kiện
        // Khi click vào 1 hàng -> cập nhật TextField
        retake_table.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> updateTextField(newSelection)
        );

        // Khi nhấn nút Đăng Ký
        dangKi_button.setOnAction(event -> handleRegisterButton());

        // 5. Làm cho TextField không thể sửa
        monDuocChon_field.setEditable(false);
    }

    /**
     * Tải dữ liệu cho Bảng (Lớp Học Lại CÓ THỂ đăng ký)
     */
    private void loadRetakeTable() {
        ObservableList<RetakeCourse> courses = model.getAvailableRetakeCourses(maSinhVien);

        TreeItem<RetakeCourse> root = new TreeItem<>();
        for (RetakeCourse record : courses) {
            root.getChildren().add(new TreeItem<>(record));
        }
        retake_table.setRoot(root);
        retake_table.setShowRoot(false);

        // Xóa text field
        monDuocChon_field.clear();
    }

    /**
     * Cập nhật TextField khi 1 hàng được chọn
     */
    private void updateTextField(TreeItem<RetakeCourse> selectedItem) {
        if (selectedItem != null && selectedItem.getValue() != null) {
            RetakeCourse course = selectedItem.getValue();
            // Yêu cầu: "hiển thị lớp HL đi cùng với tên môn học"
            monDuocChon_field.setText(course.getMaLopHL() + " - " + course.getTenMonHoc());
        } else {
            monDuocChon_field.clear();
        }
    }

    /**
     * Xử lý khi nhấn nút Đăng ký
     */
    private void handleRegisterButton() {
        TreeItem<RetakeCourse> selectedItem = retake_table.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showAlert(Alert.AlertType.ERROR, "Chưa chọn lớp", "Vui lòng chọn một lớp học lại để đăng ký.");
            return;
        }

        RetakeCourse courseToRegister = selectedItem.getValue();
        String maLopHP = courseToRegister.getMaLopHL(); // Lấy mã lớp HL
        String tenMon = courseToRegister.getTenMonHoc();

        // Xác nhận
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận đăng ký học lại");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn đăng ký lớp " + maLopHP + " (" + tenMon + ")?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Gọi Model để đăng ký
            boolean success = model.registerCourse(maSinhVien, maLopHP);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đăng ký thành công.");
                // Làm mới lại bảng (lớp vừa đăng ký sẽ biến mất)
                loadRetakeTable();
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
        maMon_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().maMonProperty());
        maLopHL_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().maLopHLProperty());
        tenMonHoc_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().tenMonHocProperty());
        soTinChi_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().soTinChiProperty());
        hocKi_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().hocKyProperty());
        namHoc_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().namHocProperty());
    }
}