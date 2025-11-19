package com.example.doanlaptrinhhuongdoituong.controller.admin;

import com.example.doanlaptrinhhuongdoituong.dao.Admin.AdminStaticDAO;
import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminStaticModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller cho giao diện AdminStaticView.fxml
 */
public class AdminStaticController implements Initializable {

    // --- Khai báo các thành phần UI (Giả định đã sửa FXML) ---
    // VBox 1: Lớp Nguyện Vọng (NV)
    @FXML private ComboBox<Integer> chonKiHoc_NV_combobox; // ID gốc
    @FXML private ComboBox<String> chonKhoa_NV_combobox; // ID gốc
    @FXML private TreeTableView<AdminStaticModel> lopNV_table;
    @FXML private TreeTableColumn<AdminStaticModel, String> maLopNV_col;
    @FXML private TreeTableColumn<AdminStaticModel, String> lopNV_col;
    @FXML private TreeTableColumn<AdminStaticModel, String> monHoc_col; // ID gốc
    @FXML private TreeTableColumn<AdminStaticModel, Number> soSVDK_col;
    @FXML private TreeTableColumn<AdminStaticModel, String> trangThai_col; // ID gốc

    // VBox 2: Lớp Học Lại (HL)
    @FXML private ComboBox<Integer> chonKiHoc_HL_combobox; // ID đã sửa
    @FXML private ComboBox<String> chonKhoa_HL_combobox; // ID đã sửa
    @FXML private TreeTableView<AdminStaticModel> lopHL_table;
    @FXML private TreeTableColumn<AdminStaticModel, String> maLopHL_col;
    @FXML private TreeTableColumn<AdminStaticModel, String> lopHL_col;
    @FXML private TreeTableColumn<AdminStaticModel, String> monHoc_hl_col; // ID đã sửa
    @FXML private TreeTableColumn<AdminStaticModel, Number> soSinhVien_col;
    @FXML private TreeTableColumn<AdminStaticModel, String> trangThai_hl_col; // ID đã sửa

    // Root cho 2 bảng
    private TreeItem<AdminStaticModel> rootNV;
    private TreeItem<AdminStaticModel> rootHL;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        loadComboBoxData();
        setupListeners();
        loadAllTables(); // Tải dữ liệu lần đầu
    }

    // 1. Cài đặt các cột (Khắc phục lỗi "Bảng trống" & "Trạng thái")
    private void setupTableColumns() {
        // --- Cài đặt Bảng 1: Lớp Nguyện Vọng ---
        maLopNV_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getMaLopHP()));
        lopNV_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getTenLopHP()));
        monHoc_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getTenMonHoc()));
        soSVDK_col.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getValue().getSiSoHienTai()));

        // YÊU CẦU 3 & 4: Logic hiển thị "Trạng thái"
        trangThai_col.setCellValueFactory(cellData -> {
            AdminStaticModel lop = cellData.getValue().getValue();
            String trangThai = "Chưa đủ";
            if (lop.getSiSoHienTai() >= lop.getSiSoToiThieu()) {
                trangThai = "Đã đủ";
            }
            return new SimpleStringProperty(trangThai);
        });

        // --- Cài đặt Bảng 2: Lớp Học Lại ---
        maLopHL_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getMaLopHP()));
        lopHL_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getTenLopHP()));
        monHoc_hl_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getTenMonHoc()));
        soSinhVien_col.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getValue().getSiSoHienTai()));

        // YÊU CẦU 3 & 4: Logic hiển thị "Trạng thái" (cho bảng 2)
        trangThai_hl_col.setCellValueFactory(cellData -> {
            AdminStaticModel lop = cellData.getValue().getValue();
            String trangThai = "Chưa đủ";
            if (lop.getSiSoHienTai() >= lop.getSiSoToiThieu()) {
                trangThai = "Đã đủ";
            }
            return new SimpleStringProperty(trangThai);
        });

        // Tạo root ảo cho 2 bảng
        rootNV = new TreeItem<>(new AdminStaticModel());
        lopNV_table.setRoot(rootNV); lopNV_table.setShowRoot(false);

        rootHL = new TreeItem<>(new AdminStaticModel());
        lopHL_table.setRoot(rootHL); lopHL_table.setShowRoot(false);
    }

    // 2. Tải dữ liệu ComboBox (Cho cả 4 ComboBox)
    private void loadComboBoxData() {
        ObservableList<String> khoaList = AdminStaticDAO.getMaKhoaList();
        ObservableList<Integer> hocKyList = AdminStaticDAO.getHocKyList();

        // Gán cho 2 ComboBox đầu
        chonKhoa_NV_combobox.setItems(khoaList);
        chonKiHoc_NV_combobox.setItems(hocKyList);

        // Gán cho 2 ComboBox sau
        chonKhoa_HL_combobox.setItems(khoaList);
        chonKiHoc_HL_combobox.setItems(hocKyList);

        // Đặt giá trị mặc định
        if (!khoaList.isEmpty()) {
            chonKhoa_NV_combobox.setValue(khoaList.get(0));
            chonKhoa_HL_combobox.setValue(khoaList.get(0));
        }
        if (!hocKyList.isEmpty()) {
            chonKiHoc_NV_combobox.setValue(hocKyList.get(0));
            chonKiHoc_HL_combobox.setValue(hocKyList.get(0));
        }
    }

    // 3. Lắng nghe sự kiện (cho cả 4 ComboBox)
    private void setupListeners() {
        // Khi 1 trong 4 ComboBox thay đổi, tải lại BẢNG TƯƠNG ỨNG
        chonKhoa_NV_combobox.valueProperty().addListener((obs, oldV, newV) -> loadNVTable());
        chonKiHoc_NV_combobox.valueProperty().addListener((obs, oldV, newV) -> loadNVTable());

        chonKhoa_HL_combobox.valueProperty().addListener((obs, oldV, newV) -> loadHLTable());
        chonKiHoc_HL_combobox.valueProperty().addListener((obs, oldV, newV) -> loadHLTable());
    }

    // 4. Hàm tải dữ liệu (tách biệt)

    // Tải cho cả 2 bảng
    private void loadAllTables() {
        loadNVTable();
        loadHLTable();
    }

    // Tải Bảng Nguyện Vọng
    private void loadNVTable() {
        String maKhoa = chonKhoa_NV_combobox.getValue();
        Integer hocKy = chonKiHoc_NV_combobox.getValue();
        if (maKhoa == null || hocKy == null) return;

        ObservableList<AdminStaticModel> classList = AdminStaticDAO.getStaticClasses("NGUYEN_VONG", maKhoa, hocKy);
        rootNV.getChildren().clear();
        classList.forEach(lop -> rootNV.getChildren().add(new TreeItem<>(lop)));
    }

    // Tải Bảng Học Lại
    private void loadHLTable() {
        String maKhoa = chonKhoa_HL_combobox.getValue();
        Integer hocKy = chonKiHoc_HL_combobox.getValue();
        if (maKhoa == null || hocKy == null) return;

        ObservableList<AdminStaticModel> classList = AdminStaticDAO.getStaticClasses("HOC_LAI", maKhoa, hocKy);
        rootHL.getChildren().clear();
        classList.forEach(lop -> rootHL.getChildren().add(new TreeItem<>(lop)));
    }
}