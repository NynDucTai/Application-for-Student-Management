package com.example.doanlaptrinhhuongdoituong.controller.admin;

import com.example.doanlaptrinhhuongdoituong.dao.Admin.AdminTeacherManageDAO;
import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminTeacherManageModel;
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
 * Controller cho giao diện AdminTeacherManageView.fxml.
 * (Đã cập nhật Tên đăng nhập & Mật khẩu)
 */
public class AdminTeacherManageController implements Initializable {

    // --- Khai báo các thành phần UI (khớp 100% với fx:id) ---
    @FXML private TextField maGV_field;
    @FXML private TextField hoTen_field;
    @FXML private TextField email_field;
    @FXML private ComboBox<String> khoa_comboBox;

    // --- BỔ SUNG 2 TRƯỜNG MỚI ---
    @FXML private TextField tenDN_field;
    @FXML private TextField matKhau_field;

    @FXML private Button themGV_button;
    @FXML private Button xoaGV_button;
    @FXML private TextField timGV_field;
    @FXML private Button search_button;

    @FXML private TreeTableView<AdminTeacherManageModel> teacher_management_table;
    @FXML private TreeTableColumn<AdminTeacherManageModel, String> maGV_col;
    @FXML private TreeTableColumn<AdminTeacherManageModel, String> hoTen_col;
    @FXML private TreeTableColumn<AdminTeacherManageModel, String> khoa_col;
    @FXML private TreeTableColumn<AdminTeacherManageModel, String> email_col;
    @FXML private TreeTableColumn<AdminTeacherManageModel, Number> soLopGiangDay_col;

    // Danh sách đầy đủ (dùng cho tìm kiếm)
    private ObservableList<AdminTeacherManageModel> teacherList;
    private TreeItem<AdminTeacherManageModel> root;

    /**
     * Hàm này được tự động gọi sau khi FXML được tải.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        loadKhoaData();
        loadTeacherData();
        setupRowSelectionListener();
        themGV_button.setOnAction(event -> onAddTeacher());
        xoaGV_button.setOnAction(event -> onDeleteTeacher());
        search_button.setOnAction(event -> onSearchTeacher());
    }

    // 1. Cài đặt các cột (Giữ nguyên)
    private void setupTableColumns() {
        maGV_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getMaGV()));

        hoTen_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getHoTen()));

        email_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getEmail()));

        khoa_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getTenKhoa()));

        soLopGiangDay_col.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getValue().getSoLopGiangDay()));
    }

    // 2. Tải dữ liệu Khoa vào ComboBox (Giữ nguyên)
    private void loadKhoaData() {
        ObservableList<String> khoaList = AdminTeacherManageDAO.getMaKhoaList();
        khoa_comboBox.setItems(khoaList);
        if (!khoaList.isEmpty()) {
            khoa_comboBox.setValue(khoaList.get(0)); // Chọn 'CNTT' làm mặc định
        }
    }

    // 3. Tải dữ liệu Giảng viên từ CSDL (Giữ nguyên)
    private void loadTeacherData() {
        teacherList = AdminTeacherManageDAO.getTeachers();

        root = new TreeItem<>(new AdminTeacherManageModel());
        teacher_management_table.setShowRoot(false);
        teacher_management_table.setRoot(root);

        root.getChildren().clear();
        teacherList.forEach(teacher -> {
            root.getChildren().add(new TreeItem<>(teacher));
        });
    }

    // 4. Lắng nghe sự kiện chọn hàng (ĐÃ CẬP NHẬT)
    private void setupRowSelectionListener() {
        teacher_management_table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection.getValue() != null) {
                AdminTeacherManageModel selectedTeacher = newSelection.getValue();

                // Đổ dữ liệu lên các TextField/ComboBox
                maGV_field.setText(selectedTeacher.getMaGV());
                hoTen_field.setText(selectedTeacher.getHoTen());
                email_field.setText(selectedTeacher.getEmail());
                khoa_comboBox.setValue(selectedTeacher.getMaKhoa()); // Gán MÃ KHOA

                // --- BỔ SUNG HIỂN THỊ 2 TRƯỜNG MỚI ---
                tenDN_field.setText(selectedTeacher.getUsername());
                matKhau_field.setText(selectedTeacher.getPassword()); // CẢNH BÁO BẢO MẬT!
            }
        });
    }

    // 5. Xử lý nút Thêm Giảng viên (ĐÃ CẬP NHẬT)
    @FXML
    private void onAddTeacher() {
        // Lấy dữ liệu từ các trường
        String maGV = maGV_field.getText();
        String hoTen = hoTen_field.getText();
        String email = email_field.getText();
        String maKhoa = khoa_comboBox.getValue();

        // --- BỔ SUNG LẤY DỮ LIỆU 2 TRƯỜNG MỚI ---
        String username = tenDN_field.getText();
        String password = matKhau_field.getText();

        if (maGV.isEmpty() || hoTen.isEmpty() || maKhoa == null || username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Tất cả các trường (Mã GV, Họ tên, Khoa, Tên ĐN, Mật khẩu) là bắt buộc.");
            return;
        }

        // Tạo đối tượng Model mới
        AdminTeacherManageModel newTeacher = new AdminTeacherManageModel();
        newTeacher.setMaGV(maGV);
        newTeacher.setHoTen(hoTen);
        newTeacher.setEmail(email);
        newTeacher.setMaKhoa(maKhoa);

        // Gọi DAO để thêm
        boolean success = AdminTeacherManageDAO.addTeacher(newTeacher, username, password);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm giảng viên thành công.");
            loadTeacherData(); // Tải lại toàn bộ bảng
            clearFields();
        }
    }

    // 6. Xử lý nút Xóa Giảng viên (Giữ nguyên)
    @FXML
    private void onDeleteTeacher() {
        TreeItem<AdminTeacherManageModel> selectedItem = teacher_management_table.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn một giảng viên để xóa.");
            return;
        }

        AdminTeacherManageModel selectedTeacher = selectedItem.getValue();
        String maGV = selectedTeacher.getMaGV();

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa giảng viên: " + selectedTeacher.getHoTen() + "?");
        confirmAlert.setContentText("Hành động này sẽ xóa cả tài khoản đăng nhập và phân công giảng dạy của họ.");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            boolean success = AdminTeacherManageDAO.deleteTeacher(maGV);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa giảng viên thành công.");
                loadTeacherData();
                clearFields();
            }
        }
    }

    // 7. Xử lý nút Tìm kiếm (Giữ nguyên)
    @FXML
    private void onSearchTeacher() {
        String filter = timGV_field.getText().toLowerCase();
        if (filter.isEmpty()) {
            loadTeacherData();
            return;
        }

        ObservableList<TreeItem<AdminTeacherManageModel>> filteredList = FXCollections.observableArrayList();
        ObservableList<TreeItem<AdminTeacherManageModel>> otherList = FXCollections.observableArrayList();

        for (AdminTeacherManageModel teacher : teacherList) {
            if (teacher.getHoTen().toLowerCase().contains(filter) ||
                    teacher.getMaGV().toLowerCase().contains(filter)) {
                filteredList.add(new TreeItem<>(teacher));
            } else {
                otherList.add(new TreeItem<>(teacher));
            }
        }

        filteredList.addAll(otherList);
        root.getChildren().setAll(filteredList);
    }

    // Hàm tiện ích để xóa các trường (ĐÃ CẬP NHẬT)
    private void clearFields() {
        maGV_field.clear();
        hoTen_field.clear();
        email_field.clear();
        tenDN_field.clear(); // <-- BỔ SUNG
        matKhau_field.clear(); // <-- BỔ SUNG

        if (!khoa_comboBox.getItems().isEmpty()) {
            khoa_comboBox.setValue(khoa_comboBox.getItems().get(0));
        }
    }

    // Hàm tiện ích (Giữ nguyên)
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}