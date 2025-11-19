package com.example.doanlaptrinhhuongdoituong.controller.teacher;

import com.example.doanlaptrinhhuongdoituong.model.Teacher.LopDayHoc;
import com.example.doanlaptrinhhuongdoituong.model.Teacher.SinhVienDiem;
import com.example.doanlaptrinhhuongdoituong.model.Teacher.TeacherStaticModel;
import com.example.doanlaptrinhhuongdoituong.util.UserSession;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class TeacherStaticController implements Initializable {

    @FXML private ComboBox<LopDayHoc> chonLopHP_conboBox;
    @FXML private Button tangDan_button;
    @FXML private Button giamDan_button;
    @FXML private TreeTableView<SinhVienDiem> stuScore_table;
    @FXML private TreeTableColumn<SinhVienDiem, Number> soThuTu_col;
    @FXML private TreeTableColumn<SinhVienDiem, String> maSV_col;
    @FXML private TreeTableColumn<SinhVienDiem, String> hoTen_col;
    @FXML private TreeTableColumn<SinhVienDiem, Float> diemQT_col;
    @FXML private TreeTableColumn<SinhVienDiem, Float> diemKT_col;
    @FXML private TreeTableColumn<SinhVienDiem, Float> diemTK_col;
    @FXML private TreeTableColumn<SinhVienDiem, Float> diemHe4_col;
    @FXML private TreeTableColumn<SinhVienDiem, String> diemChu_col;
    @FXML private TreeTableColumn<SinhVienDiem, String> trangThai_col;

    // Các TextField thống kê
    @FXML private TextField quaMon_field;
    @FXML private TextField truotMon_field;
    @FXML private TextField datA_field;
    @FXML private TextField datB_field;
    @FXML private TextField datC_field;
    @FXML private TextField datD_field;

    private TeacherStaticModel model;
    private String maGiangVien;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.model = new TeacherStaticModel();
        this.maGiangVien = UserSession.getInstance().getMaGiangVien();

        if (this.maGiangVien == null) {
            System.err.println("TeacherStaticController: maGiangVien là null!");
            return;
        }

        // --- KHẮC PHỤC LỖI HIỂN THỊ ---
        // Ép buộc độ rộng tối thiểu cho các ô text để số không bị che khuất
        fixTextFieldWidths();

        setStatFieldsEditable(false);
        setupTableColumns();
        loadComboBoxLop();

        chonLopHP_conboBox.setOnAction(event -> loadTableAndStats(null));
        tangDan_button.setOnAction(event -> loadTableAndStats("ASC"));
        giamDan_button.setOnAction(event -> loadTableAndStats("DESC"));
    }

    // Hàm sửa lỗi độ rộng TextField
    private void fixTextFieldWidths() {
        double minW = 50.0; // Đủ rộng để hiển thị 2-3 chữ số
        if (quaMon_field != null) quaMon_field.setMinWidth(minW);
        if (truotMon_field != null) truotMon_field.setMinWidth(minW);
        if (datA_field != null) datA_field.setMinWidth(minW);
        if (datB_field != null) datB_field.setMinWidth(minW);
        if (datC_field != null) datC_field.setMinWidth(minW);
        if (datD_field != null) datD_field.setMinWidth(minW);
    }

    private void loadComboBoxLop() {
        ObservableList<LopDayHoc> dsLop = model.getLopGiangDay(maGiangVien);
        chonLopHP_conboBox.setItems(dsLop);
        chonLopHP_conboBox.setConverter(new StringConverter<LopDayHoc>() {
            @Override public String toString(LopDayHoc lop) { return (lop == null) ? "Chọn lớp" : lop.toString(); }
            @Override public LopDayHoc fromString(String string) { return null; }
        });
    }

    private void loadTableAndStats(String sortOrder) {
        LopDayHoc selectedLop = chonLopHP_conboBox.getSelectionModel().getSelectedItem();
        if (selectedLop == null) {
            stuScore_table.setRoot(null);
            clearStatFields();
            return;
        }

        String maLopHP = selectedLop.getMaLopHP();
        ObservableList<SinhVienDiem> dsSV = model.getDiemSinhVien(maLopHP, sortOrder);

        TreeItem<SinhVienDiem> root = new TreeItem<>();
        for (SinhVienDiem sv : dsSV) {
            root.getChildren().add(new TreeItem<>(sv));
        }
        stuScore_table.setRoot(root);
        stuScore_table.setShowRoot(false);

        // Tính toán và hiển thị thống kê
        Map<String, Integer> stats = model.calculateStatistics(dsSV);
        updateStatFields(stats);
    }

    private void updateStatFields(Map<String, Integer> stats) {
        // Sử dụng String.valueOf để đảm bảo chuyển số thành chuỗi
        quaMon_field.setText(String.valueOf(stats.getOrDefault("quaMon", 0)));
        truotMon_field.setText(String.valueOf(stats.getOrDefault("truotMon", 0)));
        datA_field.setText(String.valueOf(stats.getOrDefault("datA", 0)));
        datB_field.setText(String.valueOf(stats.getOrDefault("datB", 0)));
        datC_field.setText(String.valueOf(stats.getOrDefault("datC", 0)));
        datD_field.setText(String.valueOf(stats.getOrDefault("datD", 0)));
    }

    private void clearStatFields() {
        quaMon_field.clear();
        truotMon_field.clear();
        datA_field.clear();
        datB_field.clear();
        datC_field.clear();
        datD_field.clear();
    }

    private void setStatFieldsEditable(boolean editable) {
        quaMon_field.setEditable(editable);
        truotMon_field.setEditable(editable);
        datA_field.setEditable(editable);
        datB_field.setEditable(editable);
        datC_field.setEditable(editable);
        datD_field.setEditable(editable);
    }

    private void setupTableColumns() {
        soThuTu_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().sttProperty());
        maSV_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().maSVProperty());
        hoTen_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().hoTenProperty());
        diemQT_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemQTProperty());
        diemKT_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemKTProperty());
        diemTK_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemTKProperty());
        diemHe4_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemHe4Property());
        diemChu_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemChuProperty());
        trangThai_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().trangThaiProperty());
    }
}