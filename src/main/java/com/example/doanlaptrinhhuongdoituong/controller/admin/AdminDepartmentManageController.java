package com.example.doanlaptrinhhuongdoituong.controller.admin;

import com.example.doanlaptrinhhuongdoituong.dao.Admin.AdminDepartmentManageDAO;
import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminDepartmentManageModel;
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
 * Controller cho giao diện AdminDepartmentManageView.fxml.
 * Triển khai Initializable.
 */
public class AdminDepartmentManageController implements Initializable {

    // --- Khai báo các thành phần UI (khớp 100% với fx:id) ---
    @FXML private TextField maKhoa_field;
    @FXML private TextField tenKhoa_field;

    @FXML private Button themKhoa_button;
    @FXML private Button xoaKhoa_button;
    @FXML private TextField timKiemKhoa_field;
    @FXML private Button timKiem_button;

    // Đây là fx:id bạn vừa thêm vào FXML
    @FXML private TreeTableView<AdminDepartmentManageModel> department_table;

    @FXML private TreeTableColumn<AdminDepartmentManageModel, String> maKhoa_col;
    @FXML private TreeTableColumn<AdminDepartmentManageModel, String> tenKhoa_col;
    @FXML private TreeTableColumn<AdminDepartmentManageModel, Number> soLuongGV_col;
    @FXML private TreeTableColumn<AdminDepartmentManageModel, Number> soLuongSV_col;
    @FXML private TreeTableColumn<AdminDepartmentManageModel, Number> soLuongLHP_col;

    // Danh sách đầy đủ (dùng cho tìm kiếm)
    private ObservableList<AdminDepartmentManageModel> departmentList;
    private TreeItem<AdminDepartmentManageModel> root;

    /**
     * Hàm này được tự động gọi sau khi FXML được tải.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Cài đặt các cột cho bảng (Khắc phục lỗi "Bảng trống")
        setupTableColumns();

        // 2. Tải dữ liệu ban đầu cho bảng
        loadDepartmentData();

        // 3. Lắng nghe sự kiện click chuột vào hàng
        setupRowSelectionListener();

        // 4. Gán sự kiện cho các nút
        themKhoa_button.setOnAction(event -> onAddDepartment());
        xoaKhoa_button.setOnAction(event -> onDeleteDepartment());
        timKiem_button.setOnAction(event -> onSearchDepartment());
    }

    // 1. Cài đặt các cột (Dùng Lambda - Tránh lỗi "bảng trống")
    private void setupTableColumns() {
        maKhoa_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getMaKhoa()));

        tenKhoa_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getTenKhoa()));

        // Khắc phục lỗi "Bad return type" -> Không dùng ".asObject()"
        soLuongGV_col.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getValue().getSoLuongGV()));

        soLuongSV_col.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getValue().getSoLuongSV()));

        soLuongLHP_col.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getValue().getSoLuongLHP()));
    }

    // 2. Tải dữ liệu Khoa từ CSDL
    private void loadDepartmentData() {
        // Lấy danh sách từ CSDL
        departmentList = AdminDepartmentManageDAO.getDepartments();

        // Cài đặt root cho TreeTableView
        root = new TreeItem<>(new AdminDepartmentManageModel()); // Root ảo
        department_table.setShowRoot(false);
        department_table.setRoot(root);

        // Chuyển danh sách Model thành danh sách TreeItem
        root.getChildren().clear(); // Xóa dữ liệu cũ
        departmentList.forEach(department -> {
            root.getChildren().add(new TreeItem<>(department));
        });
    }

    // 3. Lắng nghe sự kiện chọn hàng
    private void setupRowSelectionListener() {
        department_table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection.getValue() != null) {
                AdminDepartmentManageModel selectedDept = newSelection.getValue();

                // Đổ dữ liệu lên các TextField
                maKhoa_field.setText(selectedDept.getMaKhoa());
                tenKhoa_field.setText(selectedDept.getTenKhoa());
            }
        });
    }

    // 4. Xử lý nút Thêm Khoa
    @FXML
    private void onAddDepartment() {
        String maKhoa = maKhoa_field.getText();
        String tenKhoa = tenKhoa_field.getText();

        if (maKhoa.isEmpty() || tenKhoa.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Mã khoa và Tên khoa là bắt buộc.");
            return;
        }

        // Gọi DAO để thêm
        boolean success = AdminDepartmentManageDAO.addDepartment(maKhoa, tenKhoa);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm khoa thành công.");
            loadDepartmentData(); // Tải lại toàn bộ bảng
            clearFields();
        }
    }

    // 5. Xử lý nút Xóa Khoa
    @FXML
    private void onDeleteDepartment() {
        TreeItem<AdminDepartmentManageModel> selectedItem = department_table.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn một khoa để xóa.");
            return;
        }

        AdminDepartmentManageModel selectedDept = selectedItem.getValue();
        String maKhoa = selectedDept.getMaKhoa();

        // Xác nhận xóa
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa khoa: " + selectedDept.getTenKhoa() + "?");
        confirmAlert.setContentText("Hành động này sẽ thất bại nếu khoa vẫn còn Sinh viên, Giảng viên hoặc Môn học.");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            boolean success = AdminDepartmentManageDAO.deleteDepartment(maKhoa);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa khoa thành công.");
                loadDepartmentData(); // Tải lại bảng
                clearFields();
            }
            // Nếu không thành công, hàm DAO đã tự hiển thị lỗi (ví dụ: lỗi F-Key)
        }
    }

    // 6. Xử lý nút Tìm kiếm
    @FXML
    private void onSearchDepartment() {
        String filter = timKiemKhoa_field.getText().toLowerCase();
        if (filter.isEmpty()) {
            loadDepartmentData(); // Nếu ô trống, tải lại tất cả
            return;
        }

        // Lọc và đưa lên đầu
        ObservableList<TreeItem<AdminDepartmentManageModel>> filteredList = FXCollections.observableArrayList();
        ObservableList<TreeItem<AdminDepartmentManageModel>> otherList = FXCollections.observableArrayList();

        for (AdminDepartmentManageModel dept : departmentList) {
            if (dept.getTenKhoa().toLowerCase().contains(filter) ||
                    dept.getMaKhoa().toLowerCase().contains(filter)) {
                filteredList.add(new TreeItem<>(dept)); // Khớp -> lên đầu
            } else {
                otherList.add(new TreeItem<>(dept)); // Không khớp -> xuống dưới
            }
        }

        filteredList.addAll(otherList);
        root.getChildren().setAll(filteredList);
    }

    // Hàm tiện ích để xóa các trường
    private void clearFields() {
        maKhoa_field.clear();
        tenKhoa_field.clear();
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