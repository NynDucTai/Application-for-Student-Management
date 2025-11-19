package com.example.doanlaptrinhhuongdoituong.controller.admin;

import com.example.doanlaptrinhhuongdoituong.dao.Admin.AdminClassVerifyDAO;
import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminClassVerifyModel;
import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminSubjectManageModel;
import javafx.util.StringConverter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller cho giao diện AdminClassVerifyView.fxml (Mở lớp nguyện vọng)
 */
public class AdminClassVerifyController implements Initializable {

    // --- Khai báo các thành phần UI ---
    // (Tên fx:id phải khớp 100% với AdminClassVerifyView.fxml)
    @FXML private TextField maLopNV_field;
    @FXML private ComboBox<AdminSubjectManageModel> monHoc_comboBox; // Giả định bạn đã sửa FXML
    @FXML private TextField hocKi_field;
    @FXML private TextField namHoc_field;
    @FXML private TextField soTinChi_field;
    @FXML private DatePicker hanDangKi_datepicker;
    @FXML private TextField siSoToiThieu_field;
    @FXML private TextField siSo_field;

    @FXML private Button taoLop_button;
    @FXML private Button xoaLop_button;
    @FXML private Button moLop_button;

    @FXML private TreeTableView<AdminClassVerifyModel> lopNguyenVong_table;
    @FXML private TreeTableColumn<AdminClassVerifyModel, String> maLop_col;
    @FXML private TreeTableColumn<AdminClassVerifyModel, String> monHoc_col;
    @FXML private TreeTableColumn<AdminClassVerifyModel, Number> hocKi_col;
    @FXML private TreeTableColumn<AdminClassVerifyModel, Number> soTinChi_col;
    @FXML private TreeTableColumn<AdminClassVerifyModel, String> siSo_col;
    @FXML private TreeTableColumn<AdminClassVerifyModel, LocalDate> hanDangKi_col;
    @FXML private TreeTableColumn<AdminClassVerifyModel, String> hetHan_col;

    @FXML private TextField lopChon_field;

    private ObservableList<AdminSubjectManageModel> monHocList;
    private TreeItem<AdminClassVerifyModel> root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        loadComboBoxData();
        loadVerifyClassData();
        setupRowSelectionListener();
        taoLop_button.setOnAction(event -> onTaoLop());
        xoaLop_button.setOnAction(event -> onXoaLop());
        moLop_button.setOnAction(event -> onMoLop());
    }

    // 1. Cài đặt các cột (Áp dụng logic "Đủ/Không đủ", "Hết hạn")
    private void setupTableColumns() {
        maLop_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getMaLopHP()));

        monHoc_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getTenMon()));

        hocKi_col.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getValue().getHocKy()));

        soTinChi_col.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getValue().getSoTinChi()));

        // (Yêu cầu 1: Chỉ hiển thị Sĩ số hiện tại)
        siSo_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValue().getSiSoHienTai())));

        hanDangKi_col.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getValue().getHanDangKi()));


        // (Yêu cầu 2: Hiển thị "Hết hạn/Chưa hết")
        hetHan_col.setCellValueFactory(cellData -> {
            LocalDate hanDangKi = cellData.getValue().getValue().getHanDangKi();
            String trangThai = "Chưa hết";
            if (hanDangKi != null && hanDangKi.isBefore(LocalDate.now())) {
                trangThai = "Đã hết";
            }
            return new SimpleStringProperty(trangThai);
        });

        root = new TreeItem<>(new AdminClassVerifyModel());
        lopNguyenVong_table.setRoot(root);
        lopNguyenVong_table.setShowRoot(false);
    }

    // 2. Tải dữ liệu ComboBox (Giống hệt RetakeClass)
    private void loadComboBoxData() {
        monHocList = AdminClassVerifyDAO.getMonHocList();
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
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMaMon() + " - " + item.getTenMon());
                }
            }
        });

        monHoc_comboBox.valueProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                soTinChi_field.setText(String.valueOf(newV.getSoTinChi()));
            } else {
                soTinChi_field.clear();
            }
        });
    }

    // 3. Tải dữ liệu Bảng
    private void loadVerifyClassData() {
        ObservableList<AdminClassVerifyModel> classList = AdminClassVerifyDAO.getVerifyClasses();
        root.getChildren().clear();
        classList.forEach(lop -> root.getChildren().add(new TreeItem<>(lop)));
    }

    // 4. Lắng nghe sự kiện chọn hàng
    private void setupRowSelectionListener() {
        lopNguyenVong_table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection.getValue() != null) {
                AdminClassVerifyModel selectedLop = newSelection.getValue();

                maLopNV_field.setText(selectedLop.getMaLopHP());

                String maMonCanChon = selectedLop.getMaMon();
                for (AdminSubjectManageModel mon : monHocList) {
                    if (mon.getMaMon().equals(maMonCanChon)) {
                        monHoc_comboBox.setValue(mon);
                        break;
                    }
                }

                hocKi_field.setText(String.valueOf(selectedLop.getHocKy()));
                namHoc_field.setText(selectedLop.getNamHoc());
                soTinChi_field.setText(String.valueOf(selectedLop.getSoTinChi()));
                hanDangKi_datepicker.setValue(selectedLop.getHanDangKi());
                siSoToiThieu_field.setText(String.valueOf(selectedLop.getSiSoToiThieu()));
                siSo_field.setText(String.valueOf(selectedLop.getSiSoToiDa()));
                lopChon_field.setText(selectedLop.getTenMon() + " (" + selectedLop.getMaLopHP() + ")");
            }
        });
    }

    // 5. Xử lý nút Tạo Lớp
    @FXML
    private void onTaoLop() {
        try {
            AdminSubjectManageModel selectedMon = monHoc_comboBox.getValue();

            if (maLopNV_field.getText().isEmpty() || selectedMon == null) {
                showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Mã lớp và Môn học là bắt buộc.");
                return;
            }

            AdminClassVerifyModel lop = new AdminClassVerifyModel();
            lop.setMaLopHP(maLopNV_field.getText());
            lop.setMaMon(selectedMon.getMaMon());
            lop.setHocKy(Integer.parseInt(hocKi_field.getText()));
            lop.setNamHoc(namHoc_field.getText());
            lop.setSoTinChi(selectedMon.getSoTinChi());
            lop.setSiSoToiThieu(Integer.parseInt(siSoToiThieu_field.getText()));
            lop.setSiSoToiDa(Integer.parseInt(siSo_field.getText()));
            lop.setHanDangKi(hanDangKi_datepicker.getValue());

            boolean success = AdminClassVerifyDAO.addVerifyClass(lop);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Tạo lớp nguyện vọng thành công.");
                loadVerifyClassData();
                clearFields();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi định dạng", "Học kì, Sĩ số, Tín chỉ phải là SỐ.");
        }
    }

    // 6. Xử lý nút Xóa Lớp
    @FXML
    private void onXoaLop() {
        TreeItem<AdminClassVerifyModel> selectedItem = lopNguyenVong_table.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn một lớp để xóa.");
            return;
        }
        String maLopHP = selectedItem.getValue().getMaLopHP();
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa lớp: " + maLopHP + "?");
        confirmAlert.setContentText("Tất cả sinh viên đã đăng ký lớp này (nếu có) cũng sẽ bị hủy đăng ký.");
        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            boolean success = AdminClassVerifyDAO.deleteVerifyClass(maLopHP);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa lớp thành công.");
                loadVerifyClassData();
                clearFields();
            }
        }
    }

    // 7. Xử lý nút Mở Lớp (Duyệt)
    @FXML
    private void onMoLop() {
        TreeItem<AdminClassVerifyModel> selectedItem = lopNguyenVong_table.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn một lớp để MỞ.");
            return;
        }

        AdminClassVerifyModel lop = selectedItem.getValue();

        // (Yêu cầu MỚI: Kiểm tra Hết hạn TRƯỚC)
        LocalDate hanDangKi = lop.getHanDangKi();
        boolean isExpired = (hanDangKi != null && hanDangKi.isBefore(LocalDate.now()));

        if (isExpired) {
            showAlert(Alert.AlertType.ERROR, "Lớp đã hết hạn",
                    "Lớp này đã quá hạn đăng ký. Bạn không thể mở lớp, vui lòng xóa lớp đã hết hạn.");
            return;
        }

        // (Yêu cầu MỚI: Bỏ qua kiểm tra sĩ số)

        // (Kiểm tra này vẫn cần thiết để tránh mở lớp 2 lần)
        if (lop.getTrangThai().equals("CHINH_THUC")) {
            showAlert(Alert.AlertType.INFORMATION, "Đã mở", "Lớp này đã ở trạng thái CHINH_THUC.");
            return;
        }

        boolean success = AdminClassVerifyDAO.openClass(lop.getMaLopHP());
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã duyệt và mở lớp " + lop.getMaLopHP() + ".");

            // (Lớp này sẽ tự động biến mất vì hàm DAO đã được sửa)
            loadVerifyClassData();
            clearFields();
        }
    }

    // Hàm tiện ích
    private void clearFields() {
        maLopNV_field.clear();
        monHoc_comboBox.getSelectionModel().clearSelection();
        hocKi_field.clear();
        namHoc_field.clear();
        soTinChi_field.clear();
        hanDangKi_datepicker.setValue(null);
        siSoToiThieu_field.clear();
        siSo_field.clear();
        lopChon_field.clear();
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