package com.example.doanlaptrinhhuongdoituong.controller.student;

import com.example.doanlaptrinhhuongdoituong.model.Student.StudentCourtModel;
import com.example.doanlaptrinhhuongdoituong.model.Student.StudentCourtRecord;
import com.example.doanlaptrinhhuongdoituong.util.UserSession;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentCourtController implements Initializable {

    // --- Khai báo FXML ---
    @FXML private ComboBox<String> chonKiHoc_comboBox;
    @FXML private ComboBox<String> chonNamHoc_comboBox;
    @FXML private TreeTableView<StudentCourtRecord> court_table;

    // Khai báo các cột
    @FXML private TreeTableColumn<StudentCourtRecord, String> hp_id_col;
    @FXML private TreeTableColumn<StudentCourtRecord, String> lophp_col;
    @FXML private TreeTableColumn<StudentCourtRecord, String> hp_name_col;
    @FXML private TreeTableColumn<StudentCourtRecord, Number> tinchi_col;
    @FXML private TreeTableColumn<StudentCourtRecord, String> hocKi_col;
    @FXML private TreeTableColumn<StudentCourtRecord, String> namHoc_col;

    // --- Biến logic ---
    private StudentCourtModel model;
    private String maSinhVien;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.model = new StudentCourtModel();

        // 1. Lấy maSV từ Session
        this.maSinhVien = UserSession.getInstance().getMaSinhVien();

        if (this.maSinhVien == null || this.maSinhVien.isEmpty()) {
            System.err.println("StudentCourtController: maSinhVien là null!");
            return;
        }

        // 2. Cài đặt bảng (chống lỗi null của root)
        setupTableColumns();

        // 3. Tải dữ liệu cho ComboBoxes
        loadComboBoxes();

        // 4. Gán sự kiện: Khi một ComboBox thay đổi, gọi hàm loadCourseTable
        chonKiHoc_comboBox.setOnAction(event -> loadCourseTable());
        chonNamHoc_comboBox.setOnAction(event -> loadCourseTable());
    }

    /**
     * Tải dữ liệu cho 2 ComboBox
     */
    private void loadComboBoxes() {
        chonKiHoc_comboBox.setItems(model.getHocKy(maSinhVien));
        chonNamHoc_comboBox.setItems(model.getNamHoc(maSinhVien));
    }

    /**
     * Tải dữ liệu cho bảng, CHỈ KHI cả 2 ComboBox đã được chọn
     */
    private void loadCourseTable() {
        String hocKy = chonKiHoc_comboBox.getValue();
        String namHoc = chonNamHoc_comboBox.getValue();

        // Chỉ tải khi cả hai đã được chọn
        if (hocKy != null && !hocKy.isEmpty() && namHoc != null && !namHoc.isEmpty()) {
            ObservableList<StudentCourtRecord> courses = model.getCourses(maSinhVien, hocKy, namHoc);

            TreeItem<StudentCourtRecord> root = new TreeItem<>();
            for (StudentCourtRecord record : courses) {
                root.getChildren().add(new TreeItem<>(record));
            }
            court_table.setRoot(root);
            court_table.setShowRoot(false);
        } else {
            // Nếu 1 trong 2 chưa chọn, xóa bảng
            court_table.setRoot(null);
        }
    }

    /**
     * Cài đặt CellValueFactory (chống lỗi Null)
     */
    private void setupTableColumns() {
        // Cú pháp an toàn (kiểm tra null cho root node)
        hp_id_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().maHPProperty());
        lophp_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().lopHPProperty());
        hp_name_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().tenMonHocProperty());
        tinchi_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().soTinChiProperty());
        hocKi_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().hocKyProperty());
        namHoc_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().namHocProperty());
    }
}