package com.example.doanlaptrinhhuongdoituong.controller.admin;

import com.example.doanlaptrinhhuongdoituong.dao.Admin.AdminClassManageDAO;
import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminClassManageModel;
import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminSubjectManageModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller cho giao diện AdminClassManageView.fxml (Duyệt lớp)
 * (Đã cập nhật 2 Chế độ Tạo Lớp + Tích hợp tenLop_field)
 */
public class AdminClassManageController implements Initializable {

    // --- Khai báo các thành phần UI ---
    // (Bảng 1 & 2)
    @FXML private TreeTableView<AdminClassManageModel> lopNV_table;
    @FXML private TreeTableColumn<AdminClassManageModel, String> maLopNV_col;
    @FXML private TreeTableColumn<AdminClassManageModel, String> monHocNV_col;
    @FXML private TreeTableColumn<AdminClassManageModel, Number> hocKiNV_col;
    @FXML private TreeTableColumn<AdminClassManageModel, String> namHocNV_col;
    @FXML private TreeTableColumn<AdminClassManageModel, Number> soTinChiNV_col;
    @FXML private TreeTableView<AdminClassManageModel> lopHL_table;
    @FXML private TreeTableColumn<AdminClassManageModel, String> maLopHL_col;
    @FXML private TreeTableColumn<AdminClassManageModel, String> monHocHL_col;
    @FXML private TreeTableColumn<AdminClassManageModel, Number> hocKiHL_col;
    @FXML private TreeTableColumn<AdminClassManageModel, String> namHocHL_col;
    @FXML private TreeTableColumn<AdminClassManageModel, Number> soTinChiHL_col;

    // Vùng điều khiển (Giữa)
    @FXML private TextField maLopNVHL_field;
    @FXML private TextField maLopHP_field;
    @FXML private ComboBox<AdminSubjectManageModel> monHoc_comboBox;

    // --- BỔ SUNG KHAI BÁO @FXML ---
    @FXML private TextField tenLop_field;

    @FXML private TextField hocKi_field;
    @FXML private TextField namHoc_field;
    @FXML private TextField soTinChi_field;
    @FXML private TextField siSo_field;
    @FXML private Button taoLop_button;
    @FXML private Button xoaLop_button;

    // Bảng 3: Lớp Học Phần (HP)
    @FXML private TreeTableView<AdminClassManageModel> lopHocPhan_table;
    @FXML private TreeTableColumn<AdminClassManageModel, String> maLopHP_col;
    @FXML private TreeTableColumn<AdminClassManageModel, String> tenLop_col;
    @FXML private TreeTableColumn<AdminClassManageModel, String> monHoc_col;
    @FXML private TreeTableColumn<AdminClassManageModel, Number> hocKi_col;
    @FXML private TreeTableColumn<AdminClassManageModel, String> namHoc_col;
    @FXML private TreeTableColumn<AdminClassManageModel, Number> soTinChi_col;

    private TreeItem<AdminClassManageModel> rootNV, rootHL, rootHP;
    private ObservableList<AdminSubjectManageModel> monHocList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        loadMonHocComboBoxData();
        loadAllTables();
        setupListeners();

        // Mặc định: Cho phép "Tạo mới" (Mode 2), khóa "Xóa"
        setControlFields(false, true);
        maLopNVHL_field.setEditable(false);
        soTinChi_field.setEditable(false);
    }

    // 1. Cài đặt các cột (Giữ nguyên)
    private void setupTableColumns() {
        // (Bảng 1, 2, 3... code không đổi)
        maLopNV_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getMaLopHP()));
        monHocNV_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getTenMonHoc()));
        hocKiNV_col.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue().getHocKy()));
        namHocNV_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getNamHoc()));
        soTinChiNV_col.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue().getSoTinChi()));

        maLopHL_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getMaLopHP()));
        monHocHL_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getTenMonHoc()));
        hocKiHL_col.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue().getHocKy()));
        namHocHL_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getNamHoc()));
        soTinChiHL_col.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue().getSoTinChi()));

        maLopHP_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getMaLopHP()));
        tenLop_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getTenLopHP()));
        monHoc_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getTenMonHoc()));
        hocKi_col.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue().getHocKy()));
        namHoc_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().getNamHoc()));
        soTinChi_col.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue().getSoTinChi()));

        // Tạo root ảo cho 3 bảng
        rootNV = new TreeItem<>(new AdminClassManageModel());
        lopNV_table.setRoot(rootNV); lopNV_table.setShowRoot(false);
        rootHL = new TreeItem<>(new AdminClassManageModel());
        lopHL_table.setRoot(rootHL); lopHL_table.setShowRoot(false);
        rootHP = new TreeItem<>(new AdminClassManageModel());
        lopHocPhan_table.setRoot(rootHP); lopHocPhan_table.setShowRoot(false);
    }

    // 2. Tải dữ liệu ComboBox Môn Học (Giữ nguyên)
    private void loadMonHocComboBoxData() {
        monHocList = AdminClassManageDAO.getMonHocList();
        monHoc_comboBox.setItems(monHocList);

        monHoc_comboBox.setConverter(new StringConverter<AdminSubjectManageModel>() {
            @Override
            public String toString(AdminSubjectManageModel object) {
                return object != null ? object.getMaMon() + " - " + object.getTenMon() : "";
            }
            @Override
            public AdminSubjectManageModel fromString(String string) { return null; }
        });

        monHoc_comboBox.setButtonCell(new ListCell<AdminSubjectManageModel>() {
            @Override
            protected void updateItem(AdminSubjectManageModel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getMaMon() + " - " + item.getTenMon());
            }
        });

        monHoc_comboBox.valueProperty().addListener((obs, oldV, newV) -> {
            // Chỉ điền tín chỉ nếu người dùng đang ở Chế độ 2 (Tạo mới)
            if (maLopNVHL_field.getText().isEmpty() && newV != null) {
                soTinChi_field.setText(String.valueOf(newV.getSoTinChi()));
            }
        });
    }

    // 3. Tải dữ liệu cho cả 3 bảng (Giữ nguyên)
    private void loadAllTables() {
        // (Code này không thay đổi)
        lopNV_table.getSelectionModel().clearSelection();
        lopHL_table.getSelectionModel().clearSelection();
        lopHocPhan_table.getSelectionModel().clearSelection();

        ObservableList<AdminClassManageModel> listNV = AdminClassManageDAO.getQualifiedClasses("NGUYEN_VONG");
        rootNV.getChildren().clear();
        listNV.forEach(lop -> rootNV.getChildren().add(new TreeItem<>(lop)));

        ObservableList<AdminClassManageModel> listHL = AdminClassManageDAO.getQualifiedClasses("HOC_LAI");
        rootHL.getChildren().clear();
        listHL.forEach(lop -> rootHL.getChildren().add(new TreeItem<>(lop)));

        ObservableList<AdminClassManageModel> listHP = AdminClassManageDAO.getOfficialClasses();
        rootHP.getChildren().clear();
        listHP.forEach(lop -> rootHP.getChildren().add(new TreeItem<>(lop)));
    }

    // 4. Lắng nghe sự kiện (Giữ nguyên)
    private void setupListeners() {
        lopNV_table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) { populateFields(newV.getValue(), "TAO"); }
        });
        lopHL_table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) { populateFields(newV.getValue(), "TAO"); }
        });
        lopHocPhan_table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) { populateFields(newV.getValue(), "XOA"); }
        });

        taoLop_button.setOnAction(event -> onTaoLop());
        xoaLop_button.setOnAction(event -> onXoaLop());
    }

    // 5. Điền dữ liệu vào TextFields (ĐÃ CẬP NHẬT)
    private void populateFields(AdminClassManageModel lop, String mode) {
        if (lop == null) return;

        if (mode.equals("TAO")) {
            lopHocPhan_table.getSelectionModel().clearSelection();
        } else {
            lopNV_table.getSelectionModel().clearSelection();
            lopHL_table.getSelectionModel().clearSelection();
        }

        // Tìm và chọn Môn học trong ComboBox
        String maMonCanChon = lop.getMaMon();
        for (AdminSubjectManageModel mon : monHocList) {
            if (mon.getMaMon().equals(maMonCanChon)) {
                monHoc_comboBox.setValue(mon);
                break;
            }
        }

        hocKi_field.setText(String.valueOf(lop.getHocKy()));
        namHoc_field.setText(lop.getNamHoc());
        soTinChi_field.setText(String.valueOf(lop.getSoTinChi()));

        if (mode.equals("TAO")) { // Click từ bảng trên (NV/HL)
            maLopNVHL_field.setText(lop.getMaLopHP());
            maLopHP_field.clear();
            tenLop_field.clear(); // <-- BỔ SUNG
            siSo_field.clear();
            setControlFields(false, true); // Mở tạo, khóa xóa

        } else if (mode.equals("XOA")) { // Click từ bảng dưới (HP)
            maLopNVHL_field.clear();
            maLopHP_field.setText(lop.getMaLopHP());
            tenLop_field.setText(lop.getTenLopHP()); // <-- BỔ SUNG
            siSo_field.setText(String.valueOf(lop.getSiSoToiDa()));
            setControlFields(true, false); // Khóa tạo, mở xóa
        }
    }

    // 6. Xử lý nút Tạo Lớp (ĐÃ CẬP NHẬT 2 CHẾ ĐỘ)
    @FXML
    private void onTaoLop() {
        String maLopGoc = maLopNVHL_field.getText();

        if (maLopGoc.isEmpty()) {
            onTaoLopMoiDocLap();
        } else {
            onDuyetLopTuLopCho();
        }
    }

    private void onDuyetLopTuLopCho() {
        String maLopMoi = maLopHP_field.getText();
        String tenLopMoi = tenLop_field.getText(); // <-- BỔ SUNG

        if (maLopMoi.isEmpty() || tenLopMoi.isEmpty() || siSo_field.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Vui lòng nhập Mã LHP mới, Tên lớp và Sĩ số tối đa mới.");
            return;
        }

        try {
            int siSoToiDaMoi = Integer.parseInt(siSo_field.getText());
            AdminClassManageModel lopGoc = lopNV_table.getSelectionModel().getSelectedItem() != null ?
                    lopNV_table.getSelectionModel().getSelectedItem().getValue() :
                    lopHL_table.getSelectionModel().getSelectedItem().getValue();

            if (lopGoc == null) { /* ... (lỗi) ... */ return; }

            // SỬA LẠI: Truyền thêm 'tenLopMoi'
            boolean success = AdminClassManageDAO.createClassFromExisting(lopGoc, maLopMoi, tenLopMoi, siSoToiDaMoi);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Duyệt và tạo lớp học phần thành công.");
                loadAllTables();
                clearFields();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi định dạng", "Sĩ số phải là một con số.");
        }
    }

    private void onTaoLopMoiDocLap() {
        try {
            AdminSubjectManageModel selectedMon = monHoc_comboBox.getValue();
            String maLopHP = maLopHP_field.getText();
            String tenLop = tenLop_field.getText(); // <-- BỔ SUNG

            if (maLopHP.isEmpty() || tenLop.isEmpty() || selectedMon == null) {
                showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Vui lòng nhập Mã LHP, Tên lớp và chọn Môn học.");
                return;
            }

            AdminClassManageModel lopMoi = new AdminClassManageModel();
            lopMoi.setMaLopHP(maLopHP);
            lopMoi.setTenLopHP(tenLop); // <-- BỔ SUNG
            lopMoi.setMaMon(selectedMon.getMaMon());
            lopMoi.setHocKy(Integer.parseInt(hocKi_field.getText()));
            lopMoi.setNamHoc(namHoc_field.getText());
            lopMoi.setSoTinChi(selectedMon.getSoTinChi());
            lopMoi.setSiSoToiDa(Integer.parseInt(siSo_field.getText()));

            boolean success = AdminClassManageDAO.addIndependentClass(lopMoi);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Tạo lớp học phần mới thành công.");
                loadAllTables();
                clearFields();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi định dạng", "Học kì, Sĩ số phải là SỐ.");
        }
    }

    // 7. Xử lý nút Xóa Lớp (Giữ nguyên)
    @FXML
    private void onXoaLop() {
        String maLopHP = maLopHP_field.getText();
        if (maLopHP.isEmpty()) { /* ... (lỗi) ... */ return; }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa lớp học phần: " + maLopHP + "?");
        confirmAlert.setContentText("Hành động này sẽ xóa tất cả phân công và đăng ký của sinh viên liên quan đến lớp này.");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            boolean success = AdminClassManageDAO.deleteOfficialClass(maLopHP);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa lớp học phần thành công.");
                loadAllTables();
                clearFields();
            }
        }
    }

    // Hàm tiện ích để khóa/mở khóa các trường (ĐÃ CẬP NHẬT)
    private void setControlFields(boolean taoDisabled, boolean xoaDisabled) {
        // Các trường này luôn mở khi ở chế độ tạo
        maLopHP_field.setEditable(!taoDisabled);
        tenLop_field.setEditable(!taoDisabled); // <-- BỔ SUNG
        siSo_field.setEditable(!taoDisabled);
        monHoc_comboBox.setDisable(taoDisabled);
        hocKi_field.setEditable(!taoDisabled);
        namHoc_field.setEditable(!taoDisabled);

        // Các trường này luôn khóa
        maLopNVHL_field.setEditable(false);
        soTinChi_field.setEditable(false);

        taoLop_button.setDisable(taoDisabled);
        xoaLop_button.setDisable(xoaDisabled);
    }

    // Hàm tiện ích để xóa các trường (ĐÃ CẬP NHẬT)
    @FXML
    private void clearFields() {
        maLopNVHL_field.clear();
        maLopHP_field.clear();
        tenLop_field.clear(); // <-- BỔ SUNG
        monHoc_comboBox.getSelectionModel().clearSelection();
        hocKi_field.clear();
        namHoc_field.clear();
        soTinChi_field.clear();
        siSo_field.clear();

        lopNV_table.getSelectionModel().clearSelection();
        lopHL_table.getSelectionModel().clearSelection();
        lopHocPhan_table.getSelectionModel().clearSelection();

        // Mặc định: Cho phép "Tạo mới" (Mode 2)
        setControlFields(false, true);
    }

    // Hàm tiện ích
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}