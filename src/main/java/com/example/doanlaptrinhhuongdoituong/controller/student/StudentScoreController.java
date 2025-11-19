package com.example.doanlaptrinhhuongdoituong.controller.student;

import com.example.doanlaptrinhhuongdoituong.model.Student.StudentScoreModel;
import com.example.doanlaptrinhhuongdoituong.model.Student.StudentScoreRecord;
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

public class StudentScoreController implements Initializable {

    // --- Khai báo FXML ---
    @FXML private ComboBox<String> chomKiHoc_comboBox;
    @FXML private ComboBox<String> chonNamHoc_comboBox;
    @FXML private TreeTableView<StudentScoreRecord> score_table;

    // Khai báo các cột với kiểu dữ liệu chính xác
    @FXML private TreeTableColumn<StudentScoreRecord, String> hp_id_col;
    @FXML private TreeTableColumn<StudentScoreRecord, String> hp_name_col;
    @FXML private TreeTableColumn<StudentScoreRecord, String> lophp_col;
    @FXML private TreeTableColumn<StudentScoreRecord, Number> tinchi_col;
    @FXML private TreeTableColumn<StudentScoreRecord, Float> diemqt_col;
    @FXML private TreeTableColumn<StudentScoreRecord, Float> diemkt_col;
    @FXML private TreeTableColumn<StudentScoreRecord, Float> diemtk_col;
    @FXML private TreeTableColumn<StudentScoreRecord, Float> diem4_col;
    @FXML private TreeTableColumn<StudentScoreRecord, String> diemchu_col;
    @FXML private TreeTableColumn<StudentScoreRecord, String> dat_col;

    // --- Biến logic ---
    private StudentScoreModel model;
    private String maSinhVien;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.model = new StudentScoreModel();

        // 1. Lấy maSV từ Session
        this.maSinhVien = UserSession.getInstance().getMaSinhVien();

        if (this.maSinhVien == null || this.maSinhVien.isEmpty()) {
            System.err.println("StudentScoreController: maSinhVien là null!");
            return;
        }

        // 2. Cài đặt bảng (chống lỗi null của root)
        setupTableColumns();

        // 3. Tải dữ liệu cho ComboBoxes
        loadComboBoxes();

        // 4. Gán sự kiện: Khi một ComboBox thay đổi, gọi hàm loadScoreTable
        chomKiHoc_comboBox.setOnAction(event -> loadScoreTable());
        chonNamHoc_comboBox.setOnAction(event -> loadScoreTable());
    }

    /**
     * Tải dữ liệu cho 2 ComboBox
     */
    private void loadComboBoxes() {
        chomKiHoc_comboBox.setItems(model.getHocKy(maSinhVien));
        chonNamHoc_comboBox.setItems(model.getNamHoc(maSinhVien));
    }

    /**
     * Tải dữ liệu cho bảng, CHỈ KHI cả 2 ComboBox đã được chọn
     */
    private void loadScoreTable() {
        String hocKy = chomKiHoc_comboBox.getValue();
        String namHoc = chonNamHoc_comboBox.getValue();

        // Chỉ tải khi cả hai đã được chọn
        if (hocKy != null && !hocKy.isEmpty() && namHoc != null && !namHoc.isEmpty()) {
            ObservableList<StudentScoreRecord> scores = model.getScores(maSinhVien, hocKy, namHoc);

            TreeItem<StudentScoreRecord> root = new TreeItem<>();
            for (StudentScoreRecord record : scores) {
                root.getChildren().add(new TreeItem<>(record));
            }
            score_table.setRoot(root);
            score_table.setShowRoot(false);
        } else {
            // Nếu 1 trong 2 chưa chọn, xóa bảng
            score_table.setRoot(null);
        }
    }

    /**
     * Cài đặt CellValueFactory (chống lỗi Null)
     */
    private void setupTableColumns() {
        // Cú pháp an toàn (kiểm tra null cho root node)
        hp_id_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().maHPProperty());
        hp_name_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().tenMonHocProperty());
        lophp_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().lopHPProperty());
        tinchi_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().soTinChiProperty());
        diemqt_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemQTProperty());
        diemkt_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemKTProperty());
        diemtk_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemTKProperty());
        diem4_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemHe4Property());
        diemchu_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemChuProperty());
        dat_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().trangThaiProperty());
    }
}