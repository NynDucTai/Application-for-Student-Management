package com.example.doanlaptrinhhuongdoituong.controller.student;

import com.example.doanlaptrinhhuongdoituong.model.Student.AvailableCourse;
import com.example.doanlaptrinhhuongdoituong.model.Student.StudentCourtSignedModel;
import com.example.doanlaptrinhhuongdoituong.util.UserSession;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentCourtSignedController implements Initializable {

    // --- Khai báo FXML ---
    @FXML private ComboBox<String> chonKiHoc1_comboBox;
    @FXML private ComboBox<String> chonNamHoc1_comboBox;
    @FXML private TreeTableView<AvailableCourse> sign_court_table;
    @FXML private TreeTableColumn<AvailableCourse, String> maHP_col;
    @FXML private TreeTableColumn<AvailableCourse, String> tenMon_col;
    @FXML private TreeTableColumn<AvailableCourse, String> lopHP_col; // Sẽ hiển thị TÊN lớp
    @FXML private TreeTableColumn<AvailableCourse, Number> soTinChi_col;
    @FXML private TreeTableColumn<AvailableCourse, String> namHoc_col;
    @FXML private Button dangKi_button;

    // --- Biến logic ---
    private StudentCourtSignedModel model;
    private String maSinhVien;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.model = new StudentCourtSignedModel();

        // 1. Lấy maSV từ Session
        this.maSinhVien = UserSession.getInstance().getMaSinhVien();

        if (this.maSinhVien == null || this.maSinhVien.isEmpty()) {
            System.err.println("StudentCourtSignedController: maSinhVien là null!");
            return;
        }

        // 2. Cài đặt bảng
        setupTableColumns();

        // 3. Tải dữ liệu cho ComboBoxes
        loadComboBoxes();

        // 4. Gán sự kiện
        chonKiHoc1_comboBox.setOnAction(event -> loadAvailableCourseTable());
        chonNamHoc1_comboBox.setOnAction(event -> loadAvailableCourseTable());

        // Gán sự kiện cho nút Đăng Ký
        dangKi_button.setOnAction(event -> handleRegisterButton());
    }

    /**
     * Tải dữ liệu cho ComboBoxes
     */
    private void loadComboBoxes() {
        ObservableList<String> allHocKy = model.getAllHocKy();
        ObservableList<String> allNamHoc = model.getAllNamHoc();

        chonKiHoc1_comboBox.setItems(allHocKy);
        chonNamHoc1_comboBox.setItems(allNamHoc);
    }

    /**
     * Tải dữ liệu cho Bảng (Lớp CÓ THỂ đăng ký)
     */
    private void loadAvailableCourseTable() {
        String hocKy = chonKiHoc1_comboBox.getValue();
        String namHoc = chonNamHoc1_comboBox.getValue();

        if (hocKy != null && namHoc != null) {
            ObservableList<AvailableCourse> courses = model.getAvailableCourses(maSinhVien, hocKy, namHoc);
            populateTable(sign_court_table, courses);
        } else {
            sign_court_table.setRoot(null);
        }
    }

    /**
     * Xử lý khi nhấn nút Đăng ký
     */
    private void handleRegisterButton() {
        // 1. Lấy dòng đang được chọn
        TreeItem<AvailableCourse> selectedItem = sign_court_table.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showAlert(Alert.AlertType.ERROR, "Chưa chọn lớp", "Vui lòng chọn một lớp học phần để đăng ký.");
            return;
        }

        // 2. Lấy thông tin lớp
        AvailableCourse courseToRegister = selectedItem.getValue();
        String maLopHP = courseToRegister.getMaLopHP(); // Lấy mã lớp (ẩn)
        String tenLop = courseToRegister.tenLopHPProperty().get(); // Lấy tên lớp (hiển thị)

        // 3. Xác nhận
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận đăng ký");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn đăng ký lớp: " + tenLop + "?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // 4. Gọi Model để đăng ký
            boolean success = model.registerCourse(maSinhVien, maLopHP);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đăng ký lớp " + tenLop + " thành công.");
                // 5. Làm mới lại bảng (lớp vừa đăng ký sẽ biến mất khỏi danh sách)
                loadAvailableCourseTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Thất bại", "Đăng ký thất bại. Lớp có thể đã đủ hoặc có lỗi xảy ra.");
            }
        }
    }

    /**
     * Hàm chung để nạp dữ liệu vào TreeTableView
     */
    private void populateTable(TreeTableView<AvailableCourse> table, ObservableList<AvailableCourse> data) {
        TreeItem<AvailableCourse> root = new TreeItem<>();
        for (AvailableCourse record : data) {
            root.getChildren().add(new TreeItem<>(record));
        }
        table.setRoot(root);
        table.setShowRoot(false);
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
        // Cài đặt Bảng (sign_court_table)
        maHP_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().maHPProperty());
        tenMon_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().tenMonProperty());

        // Yêu cầu: Cột "lớp học phần" hiển thị TÊN LỚP
        lopHP_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().tenLopHPProperty());

        soTinChi_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().soTinChiProperty());
        namHoc_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().namHocProperty());
    }
}