package com.example.doanlaptrinhhuongdoituong.controller.teacher;

import com.example.doanlaptrinhhuongdoituong.model.Teacher.LopPhanCong;
import com.example.doanlaptrinhhuongdoituong.model.Teacher.TeacherHomeModel;
import com.example.doanlaptrinhhuongdoituong.util.UserSession;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class TeacherHomeController implements Initializable {

    // --- Khai báo FXML ---
    @FXML private Label tenGV_label;
    @FXML private ComboBox<String> chonKi_comboBox;
    @FXML private ComboBox<String> chonNam_comboBox;
    @FXML private TreeTableView<LopPhanCong> phanCong_table;

    @FXML private TreeTableColumn<LopPhanCong, String> maHP_col;
    @FXML private TreeTableColumn<LopPhanCong, String> tenMon_col;
    @FXML private TreeTableColumn<LopPhanCong, String> lopHP_col;
    @FXML private TreeTableColumn<LopPhanCong, Number> soTinChi_col;
    @FXML private TreeTableColumn<LopPhanCong, Number> siSo_col;
    @FXML private TreeTableColumn<LopPhanCong, String> kiHoc_col;
    @FXML private TreeTableColumn<LopPhanCong, String> namHoc_col;

    // --- Biến logic ---
    private TeacherHomeModel model;
    private String maGiangVien;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.model = new TeacherHomeModel();
        this.maGiangVien = UserSession.getInstance().getMaGiangVien();

        if (this.maGiangVien == null || this.maGiangVien.isEmpty()) {
            tenGV_label.setText("Xin chào (LỖI: CHƯA ĐĂNG NHẬP)");
            System.err.println("TeacherHomeController: maGiangVien là null. Hãy đảm bảo LoginController đã set UserSession.");
            return;
        }

        setupTableColumns();

        chonKi_comboBox.setOnAction(event -> handleFilterAction());
        chonNam_comboBox.setOnAction(event -> handleFilterAction());

        loadTenGiangVien();
        loadComboBoxData();
    }

    // Tải tên Giảng viên
    private void loadTenGiangVien() {
        String tenGV = model.getTenGiangVien(this.maGiangVien);
        tenGV_label.setText("Xin chào " + tenGV);
    }

    //
    // =========================================================================
    // *** PHẦN SỬA LỖI NẰM TẠI ĐÂY ***
    //
    // Đã loại bỏ .asObject() khỏi 'soTinChiProperty' và 'siSoProperty'
    // =========================================================================
    private void setupTableColumns() {
        // Cú pháp (lambda) dài hơn nhưng an toàn, chống NullPointerException
        maHP_col.setCellValueFactory(param -> {
            TreeItem<LopPhanCong> treeItem = param.getValue();
            if (treeItem == null || treeItem.getValue() == null) {
                return null;
            }
            return treeItem.getValue().maHPProperty();
        });

        tenMon_col.setCellValueFactory(param -> {
            TreeItem<LopPhanCong> treeItem = param.getValue();
            if (treeItem == null || treeItem.getValue() == null) {
                return null;
            }
            return treeItem.getValue().tenMonProperty();
        });

        lopHP_col.setCellValueFactory(param -> {
            TreeItem<LopPhanCong> treeItem = param.getValue();
            if (treeItem == null || treeItem.getValue() == null) {
                return null;
            }
            return treeItem.getValue().lopHPProperty();
        });

        // *** ĐÃ SỬA LỖI ***
        soTinChi_col.setCellValueFactory(param -> {
            TreeItem<LopPhanCong> treeItem = param.getValue();
            if (treeItem == null || treeItem.getValue() == null) {
                return null;
            }
            // Trả về trực tiếp IntegerProperty, VÌ NÓ LÀ ObservableValue<Number>
            return treeItem.getValue().soTinChiProperty();
        });

        // *** ĐÃ SỬA LỖI ***
        siSo_col.setCellValueFactory(param -> {
            TreeItem<LopPhanCong> treeItem = param.getValue();
            if (treeItem == null || treeItem.getValue() == null) {
                return null;
            }
            // Trả về trực tiếp IntegerProperty
            return treeItem.getValue().siSoProperty();
        });

        kiHoc_col.setCellValueFactory(param -> {
            TreeItem<LopPhanCong> treeItem = param.getValue();
            if (treeItem == null || treeItem.getValue() == null) {
                return null;
            }
            return treeItem.getValue().kiHocProperty();
        });

        namHoc_col.setCellValueFactory(param -> {
            TreeItem<LopPhanCong> treeItem = param.getValue();
            if (treeItem == null || treeItem.getValue() == null) {
                return null;
            }
            return treeItem.getValue().namHocProperty();
        });
    }

    // Tải dữ liệu cho 2 ComboBox
    private void loadComboBoxData() {
        chonKi_comboBox.setItems(model.loadKiHoc());
        chonNam_comboBox.setItems(model.loadNamHoc());
    }

    /**
     * Hàm này được gọi BẤT CỨ KHI NÀO 1 trong 2 ComboBox thay đổi giá trị.
     */
    private void handleFilterAction() {
        String selectedKi = chonKi_comboBox.getSelectionModel().getSelectedItem();
        String selectedNam = chonNam_comboBox.getSelectionModel().getSelectedItem();

        // Chỉ tải dữ liệu khi "chọn đồng thời cả 2"
        if (selectedKi != null && !selectedKi.isEmpty() &&
                selectedNam != null && !selectedNam.isEmpty()) {

            loadTableData(selectedKi, selectedNam);
        } else {
            // Nếu 1 trong 2 rỗng -> Xóa bảng
            phanCong_table.setRoot(null);
        }
    }

    // Lấy dữ liệu từ Model và cập nhật TreeTableView
    private void loadTableData(String kiHoc, String namHoc) {
        ObservableList<LopPhanCong> dsLop = model.loadLopPhanCong(this.maGiangVien, kiHoc, namHoc);

        TreeItem<LopPhanCong> root = new TreeItem<>();

        for (LopPhanCong lop : dsLop) {
            root.getChildren().add(new TreeItem<>(lop));
        }

        phanCong_table.setRoot(root);
        phanCong_table.setShowRoot(false);
    }
}