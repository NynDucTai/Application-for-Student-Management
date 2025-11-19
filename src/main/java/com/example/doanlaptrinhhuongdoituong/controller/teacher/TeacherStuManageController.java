package com.example.doanlaptrinhhuongdoituong.controller.teacher;

import com.example.doanlaptrinhhuongdoituong.model.Teacher.LopDayHoc;
import com.example.doanlaptrinhhuongdoituong.model.Teacher.SinhVienTrongLop;
import com.example.doanlaptrinhhuongdoituong.model.Teacher.TeacherStuManageModel;
import com.example.doanlaptrinhhuongdoituong.util.UserSession;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherStuManageController implements Initializable {

    // --- Khai báo FXML ---
    @FXML private ComboBox<LopDayHoc> chonLop_comboBox;
    @FXML private TextField timSV_field;
    @FXML private Button timKiem_button;
    @FXML private TreeTableView<SinhVienTrongLop> stu_manage_table;
    @FXML private TreeTableColumn<SinhVienTrongLop, Number> soThuTu_col;
    @FXML private TreeTableColumn<SinhVienTrongLop, String> maSV_col;
    @FXML private TreeTableColumn<SinhVienTrongLop, String> hoTen_col;
    @FXML private TreeTableColumn<SinhVienTrongLop, String> ngaySinh_col;
    @FXML private TreeTableColumn<SinhVienTrongLop, String> queQuan_col;
    @FXML private TreeTableColumn<SinhVienTrongLop, String> gioiTinh_col;
    @FXML private TreeTableColumn<SinhVienTrongLop, String> khoa_col;
    @FXML private TreeTableColumn<SinhVienTrongLop, String> lop_col;

    // --- Biến logic ---
    private TeacherStuManageModel model;
    private String maGiangVien;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.model = new TeacherStuManageModel();

        // Lấy maGV từ Session (giống như TeacherHomeController)
        this.maGiangVien = UserSession.getInstance().getMaGiangVien();

        if (this.maGiangVien == null || this.maGiangVien.isEmpty()) {
            System.err.println("TeacherStuManageController: maGiangVien là null!");
            return;
        }

        // Cài đặt bảng (để chống lỗi Null của root)
        setupTableColumns();

        // Tải danh sách lớp cho ComboBox
        loadComboBoxLop();

        // Thêm hành động cho các nút
        // 1. Khi chọn ComboBox
        chonLop_comboBox.setOnAction(event -> loadStudentTable());

        // 2. Khi nhấn nút Tìm kiếm
        timKiem_button.setOnAction(event -> loadStudentTable());

        // 3. Khi nhấn Enter trong ô tìm kiếm
        timSV_field.setOnAction(event -> loadStudentTable());
    }

    /**
     * Tải danh sách lớp giảng dạy lên ComboBox
     */
    private void loadComboBoxLop() {
        ObservableList<LopDayHoc> dsLop = model.getLopGiangDay(maGiangVien);
        chonLop_comboBox.setItems(dsLop);

        // Hiển thị tên lớp đẹp (dùng hàm toString() của LopDayHoc)
        chonLop_comboBox.setConverter(new StringConverter<LopDayHoc>() {
            @Override
            public String toString(LopDayHoc lop) {
                return (lop == null) ? "Chọn lớp" : lop.toString();
            }

            @Override
            public LopDayHoc fromString(String string) {
                return null; // Không cần
            }
        });
    }

    /**
     * Tải danh sách sinh viên lên Bảng
     */
    private void loadStudentTable() {
        LopDayHoc selectedLop = chonLop_comboBox.getSelectionModel().getSelectedItem();
        String searchText = timSV_field.getText();

        // Chỉ tải khi đã chọn lớp
        if (selectedLop == null) {
            stu_manage_table.setRoot(null); // Xóa bảng nếu chưa chọn lớp
            return;
        }

        String maLopHP = selectedLop.getMaLopHP();
        ObservableList<SinhVienTrongLop> dsSV = model.getSinhVien(maLopHP, searchText);

        // Nạp dữ liệu vào TreeTableView
        TreeItem<SinhVienTrongLop> root = new TreeItem<>();
        for (SinhVienTrongLop sv : dsSV) {
            root.getChildren().add(new TreeItem<>(sv));
        }
        stu_manage_table.setRoot(root);
        stu_manage_table.setShowRoot(false);
    }

    /**
     * Cài đặt CellValueFactory (chống lỗi Null)
     */
    private void setupTableColumns() {
        soThuTu_col.setCellValueFactory(param -> {
            TreeItem<SinhVienTrongLop> item = param.getValue();
            return (item == null || item.getValue() == null) ? null : item.getValue().sttProperty();
        });
        maSV_col.setCellValueFactory(param -> {
            TreeItem<SinhVienTrongLop> item = param.getValue();
            return (item == null || item.getValue() == null) ? null : item.getValue().maSVProperty();
        });
        hoTen_col.setCellValueFactory(param -> {
            TreeItem<SinhVienTrongLop> item = param.getValue();
            return (item == null || item.getValue() == null) ? null : item.getValue().hoTenProperty();
        });
        ngaySinh_col.setCellValueFactory(param -> {
            TreeItem<SinhVienTrongLop> item = param.getValue();
            return (item == null || item.getValue() == null) ? null : item.getValue().ngaySinhProperty();
        });
        queQuan_col.setCellValueFactory(param -> {
            TreeItem<SinhVienTrongLop> item = param.getValue();
            return (item == null || item.getValue() == null) ? null : item.getValue().queQuanProperty();
        });
        gioiTinh_col.setCellValueFactory(param -> {
            TreeItem<SinhVienTrongLop> item = param.getValue();
            return (item == null || item.getValue() == null) ? null : item.getValue().gioiTinhProperty();
        });
        khoa_col.setCellValueFactory(param -> {
            TreeItem<SinhVienTrongLop> item = param.getValue();
            return (item == null || item.getValue() == null) ? null : item.getValue().khoaProperty();
        });
        lop_col.setCellValueFactory(param -> {
            TreeItem<SinhVienTrongLop> item = param.getValue();
            return (item == null || item.getValue() == null) ? null : item.getValue().lopProperty();
        });
    }
}