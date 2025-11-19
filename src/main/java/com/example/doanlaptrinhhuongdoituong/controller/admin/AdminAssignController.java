package com.example.doanlaptrinhhuongdoituong.controller.admin;

import com.example.doanlaptrinhhuongdoituong.dao.Admin.AdminAssignDAO;
import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminAssignModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller cho giao diện AdminAssignView.fxml.
 */
public class AdminAssignController implements Initializable {

    // --- Khai báo các thành phần UI (khớp 100% với fx:id) ---
    @FXML private ChoiceBox<String> chonKhoa;
    @FXML private ChoiceBox<Integer> chonKiHoc;
    @FXML private ChoiceBox<String> chonNamHoc;

    // Bảng 1: Lớp Học Phần (HP)
    @FXML private TreeTableView<AdminAssignModel> HP_table;
    @FXML private TreeTableColumn<AdminAssignModel, String> maHP_col;
    @FXML private TreeTableColumn<AdminAssignModel, String> tenMon_col; // Đã sửa fx:id
    @FXML private TreeTableColumn<AdminAssignModel, String> lopHP_col;
    @FXML private TreeTableColumn<AdminAssignModel, Number> soTinChi_col; // Đã sửa fx:id

    @FXML private TextField hocPhan_field;
    @FXML private TextField timTenGV_field;
    @FXML private Button timKiem_button;

    // Bảng 2: Giảng Viên (GV)
    @FXML private TreeTableView<AdminAssignModel> GV_table;
    @FXML private TreeTableColumn<AdminAssignModel, String> maGV_col;
    @FXML private TreeTableColumn<AdminAssignModel, String> hoTen_col;
    @FXML private TreeTableColumn<AdminAssignModel, String> email_col;

    @FXML private TextField tenGV_field;
    @FXML private Button phanCong_button;

    // Bảng 3: Đã Phân Công
    @FXML private TreeTableView<AdminAssignModel> phanCong_table;
    @FXML private TreeTableColumn<AdminAssignModel, String> maHP_col_final;
    @FXML private TreeTableColumn<AdminAssignModel, String> tenMon_col_final;
    @FXML private TreeTableColumn<AdminAssignModel, Number> soTinChi_col_final;
    @FXML private TreeTableColumn<AdminAssignModel, String> lopHP_col_final;
    @FXML private TreeTableColumn<AdminAssignModel, String> GVDuocPhanCong_col_final;

    // Danh sách đầy đủ (dùng cho tìm kiếm)
    private ObservableList<AdminAssignModel> gvListFull;
    private TreeItem<AdminAssignModel> rootHP, rootGV, rootPhanCong;

    // Biến tạm để lưu lựa chọn
    private String selectedLopHP = null;
    private String selectedGV = null;

    /**
     * Hàm này được tự động gọi sau khi FXML được tải.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Cài đặt các cột cho cả 3 bảng
        setupTableColumns();

        // 2. Tải dữ liệu cho ComboBox
        loadComboBoxData();

        // 3. Lắng nghe sự kiện (ComboBox, Bảng, Nút bấm)
        setupListeners();
    }

    // 1. Cài đặt các cột (Dùng Lambda - Tránh lỗi "bảng trống")
    private void setupTableColumns() {
        // --- Bảng 1: HP_table ---
        // (Lưu ý: FXML của bạn có 2 cột Mã HP và Lớp HP, tôi sẽ gán cả 2 là maLopHP)
        maHP_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getMaMon()));
        tenMon_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getTenMon()));
        lopHP_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getMaLopHP()));
        soTinChi_col.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue().getSoTinChi()));

        // --- Bảng 2: GV_table ---
        maGV_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getMaGV()));
        hoTen_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getHoTenGV()));
        email_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getEmailGV()));

        // --- Bảng 3: phanCong_table ---
        maHP_col_final.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getMaLopHP()));
        tenMon_col_final.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getTenMon()));
        soTinChi_col_final.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue().getSoTinChi()));
        lopHP_col_final.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getMaLopHP())); // Gán lại
        GVDuocPhanCong_col_final.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getHoTenGV()));

        // Tạo root ảo cho 3 bảng
        rootHP = new TreeItem<>(new AdminAssignModel());
        HP_table.setRoot(rootHP); HP_table.setShowRoot(false);

        rootGV = new TreeItem<>(new AdminAssignModel());
        GV_table.setRoot(rootGV); GV_table.setShowRoot(false);

        rootPhanCong = new TreeItem<>(new AdminAssignModel());
        phanCong_table.setRoot(rootPhanCong); phanCong_table.setShowRoot(false);
    }

    // 2. Tải dữ liệu ComboBox
    private void loadComboBoxData() {
        chonKhoa.setItems(AdminAssignDAO.getMaKhoaList());
        chonKiHoc.setItems(AdminAssignDAO.getHocKyList());
        chonNamHoc.setItems(AdminAssignDAO.getNamHocList());

        // (Tạm thời hardcode năm học)
        // chonNamHoc.setItems(AdminAssignDAO.getNamHocList());

        // Đặt giá trị mặc định
        if (!chonKhoa.getItems().isEmpty()) chonKhoa.setValue(chonKhoa.getItems().get(0));
        if (!chonKiHoc.getItems().isEmpty()) chonKiHoc.setValue(chonKiHoc.getItems().get(0));
        if (!chonNamHoc.getItems().isEmpty()) chonNamHoc.setValue(chonNamHoc.getItems().get(0));
    }

    // 3. Cài đặt các Listener
    private void setupListeners() {
        // Khi chọn Khoa hoặc Kì học -> tải lại cả 3 bảng
        chonKhoa.valueProperty().addListener((obs, oldV, newV) -> loadAllTables());
        chonKiHoc.valueProperty().addListener((obs, oldV, newV) -> loadAllTables());
        chonNamHoc.valueProperty().addListener((obs, oldV, newV) -> loadAllTables());

        // Khi click bảng HP -> cập nhật TextField
        HP_table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null && newV.getValue() != null) {
                AdminAssignModel selectedLop = newV.getValue();
                hocPhan_field.setText(selectedLop.getTenMon() + " (" + selectedLop.getMaLopHP() + ")");
                this.selectedLopHP = selectedLop.getMaLopHP(); // Lưu lại mã
            }
        });

        // Khi click bảng GV -> cập nhật TextField
        GV_table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null && newV.getValue() != null) {
                AdminAssignModel selectedGV = newV.getValue();
                tenGV_field.setText(selectedGV.getHoTenGV() + " (" + selectedGV.getMaGV() + ")");
                this.selectedGV = selectedGV.getMaGV(); // Lưu lại mã
            }
        });

        // Nút bấm
        timKiem_button.setOnAction(event -> onSearchTeacher());
        phanCong_button.setOnAction(event -> onAssignTeacher());

        // Tải dữ liệu lần đầu
        loadAllTables();
    }

    // 4. Hàm tải dữ liệu cho cả 3 bảng (dựa trên ComboBox)
    private void loadAllTables() {
        String maKhoa = chonKhoa.getValue();
        Integer hocKy = chonKiHoc.getValue();
        String namHoc = chonNamHoc.getValue();

        if (maKhoa == null || hocKy == null || namHoc == null) return;

        // Bảng 1: Lớp chưa phân công
        ObservableList<AdminAssignModel> lopList = AdminAssignDAO.getLopHocPhanChuaPhanCong(maKhoa, hocKy, namHoc);
        rootHP.getChildren().clear();
        lopList.forEach(lop -> rootHP.getChildren().add(new TreeItem<>(lop)));

        // Bảng 2: Giảng viên
        gvListFull = AdminAssignDAO.getGiangVienByKhoa(maKhoa); // Lấy danh sách đầy đủ
        rootGV.getChildren().clear();
        gvListFull.forEach(gv -> rootGV.getChildren().add(new TreeItem<>(gv)));

        // Bảng 3: Lớp đã phân công
        ObservableList<AdminAssignModel> phanCongList = AdminAssignDAO.getPhanCongHienTai(maKhoa, hocKy, namHoc);
        rootPhanCong.getChildren().clear();
        phanCongList.forEach(pc -> rootPhanCong.getChildren().add(new TreeItem<>(pc)));
    }

    // 5. Xử lý nút Tìm kiếm GV
    @FXML
    private void onSearchTeacher() {
        String filter = timTenGV_field.getText().toLowerCase();
        if (filter.isEmpty()) {
            rootGV.getChildren().clear();
            gvListFull.forEach(gv -> rootGV.getChildren().add(new TreeItem<>(gv)));
            return;
        }

        // Lọc và đưa lên đầu
        ObservableList<TreeItem<AdminAssignModel>> filteredList = FXCollections.observableArrayList();
        ObservableList<TreeItem<AdminAssignModel>> otherList = FXCollections.observableArrayList();

        for (AdminAssignModel gv : gvListFull) {
            if (gv.getHoTenGV().toLowerCase().contains(filter) ||
                    gv.getMaGV().toLowerCase().contains(filter)) {
                filteredList.add(new TreeItem<>(gv)); // Khớp -> lên đầu
            } else {
                otherList.add(new TreeItem<>(gv)); // Không khớp -> xuống dưới
            }
        }

        filteredList.addAll(otherList);
        rootGV.getChildren().setAll(filteredList);
    }

    // 6. Xử lý nút Phân công
    @FXML
    private void onAssignTeacher() {
        // Lấy mã từ biến tạm đã lưu
        String lopHP = this.selectedLopHP;
        String gv = this.selectedGV;

        if (lopHP == null || gv == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng chọn 1 Lớp học phần VÀ 1 Giảng viên.");
            return;
        }

        // Gọi DAO để thực thi
        boolean success = AdminAssignDAO.assignGiangVien(gv, lopHP);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã phân công giảng viên " + gv + " cho lớp " + lopHP + ".");
            // Tải lại toàn bộ các bảng
            loadAllTables();

            // Xóa lựa chọn
            this.selectedLopHP = null;
            this.selectedGV = null;
            hocPhan_field.clear();
            tenGV_field.clear();
        }
        // Nếu thất bại, DAO đã tự hiển thị lỗi
    }

    // Hàm tiện ích để hiện thông báo
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}