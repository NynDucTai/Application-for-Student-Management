package com.example.doanlaptrinhhuongdoituong.controller.admin;

import com.example.doanlaptrinhhuongdoituong.dao.Admin.AdminStuManageDAO;
import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminStuManageModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminStuManageController implements Initializable {

    // --- Khai báo các thành phần UI (Giữ nguyên) ---
    @FXML private TextField maSV_field;
    @FXML private TextField hoTen_field;
    @FXML private DatePicker ngaySinh_datepicker;
    @FXML private TextField gioiTinh_field;
    @FXML private TextField queQuan_field;
    @FXML private ComboBox<String> khoa_combobox;
    @FXML private TextField tenLopHP_field;
    @FXML private TextField stuUsername_field;
    @FXML private TextField StuPassword_field;

    @FXML private Button addStu_button;
    @FXML private Button deleteStu_button;
    @FXML private TextField textFildSearchStu;
    @FXML private Button searchStu_button;

    // --- THÊM KHAI BÁO NÚT "XEM ĐIỂM" ---
    @FXML private Button viewScore_button;

    @FXML private TreeTableView<AdminStuManageModel> stuManageTable;
    // ... (các cột giữ nguyên) ...
    @FXML private TreeTableColumn<AdminStuManageModel, String> maSV_col;
    @FXML private TreeTableColumn<AdminStuManageModel, String> hoTen_col;
    @FXML private TreeTableColumn<AdminStuManageModel, LocalDate> ngaySinh_col;
    @FXML private TreeTableColumn<AdminStuManageModel, String> gioiTinh_col;
    @FXML private TreeTableColumn<AdminStuManageModel, String> queQuan_col;
    @FXML private TreeTableColumn<AdminStuManageModel, String> tenKhoa_col;
    @FXML private TreeTableColumn<AdminStuManageModel, String> tenLopHP_col;


    private ObservableList<AdminStuManageModel> studentList;
    private TreeItem<AdminStuManageModel> root;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Cài đặt các cột cho bảng
        setupTableColumns();

        // 2. Tải dữ liệu ban đầu
        loadStudentData();

        // 3. Lắng nghe sự kiện click chuột vào hàng
        setupRowSelectionListener();

        // 4. Gán sự kiện cho các nút
        addStu_button.setOnAction(event -> onAddStudent());
        deleteStu_button.setOnAction(event -> onDeleteStudent());
        searchStu_button.setOnAction(event -> onSearchStudent());

        // --- GÁN SỰ KIỆN CHO NÚT "XEM ĐIỂM" ---
        viewScore_button.setOnAction(event -> onViewScore());

        // 5. Tải ComboBox Khoa
        ObservableList<String> khoaList = AdminStuManageDAO.getMaKhoaList();
        khoa_combobox.setItems(khoaList);
        if (!khoaList.isEmpty()) {
            khoa_combobox.setValue(khoaList.get(0));
        }
    }

    // --- (Các hàm setupTableColumns, loadStudentData, setupRowSelectionListener, onAddStudent, onDeleteStudent, onSearchStudent, clearFields, showAlert giữ nguyên y hệt file bạn đã upload) ---

    // (Bao gồm các hàm này từ file bạn đã upload)
    private void setupTableColumns() {
        maSV_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : new SimpleStringProperty(p.getValue().getValue().getMaSV()));
        hoTen_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : new SimpleStringProperty(p.getValue().getValue().getHoTen()));
        ngaySinh_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : new SimpleObjectProperty<>(p.getValue().getValue().getNgaySinh()));
        gioiTinh_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : new SimpleStringProperty(p.getValue().getValue().getGioiTinh()));
        queQuan_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : new SimpleStringProperty(p.getValue().getValue().getQueQuan()));
        tenKhoa_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : new SimpleStringProperty(p.getValue().getValue().getTenKhoa()));
        tenLopHP_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : new SimpleStringProperty(p.getValue().getValue().getLop()));
    }

    private void loadStudentData() {
        studentList = AdminStuManageDAO.getStudents();
        root = new TreeItem<>(new AdminStuManageModel());
        stuManageTable.setShowRoot(false);
        stuManageTable.setRoot(root);
        studentList.forEach(student -> root.getChildren().add(new TreeItem<>(student)));
    }

    private void setupRowSelectionListener() {
        stuManageTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection.getValue() != null) {
                AdminStuManageModel selectedStudent = newSelection.getValue();
                maSV_field.setText(selectedStudent.getMaSV());
                hoTen_field.setText(selectedStudent.getHoTen());
                ngaySinh_datepicker.setValue(selectedStudent.getNgaySinh());
                gioiTinh_field.setText(selectedStudent.getGioiTinh());
                queQuan_field.setText(selectedStudent.getQueQuan());
                khoa_combobox.setValue(selectedStudent.getMaKhoa());
                tenLopHP_field.setText(selectedStudent.getLop());
                stuUsername_field.setText(selectedStudent.getUsername());
                StuPassword_field.setText(selectedStudent.getPassword());
            }
        });
    }

    @FXML
    private void onAddStudent() {
        String maSV = maSV_field.getText();
        String hoTen = hoTen_field.getText();
        String username = stuUsername_field.getText();
        if (maSV.isEmpty() || hoTen.isEmpty() || username.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Mã SV, Họ tên, và Tên đăng nhập là bắt buộc.");
            return;
        }
        String maKhoa = khoa_combobox.getValue();
        if (maKhoa == null || maKhoa.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Bạn phải chọn một Khoa.");
            return;
        }
        AdminStuManageModel newStudent = new AdminStuManageModel();
        newStudent.setMaSV(maSV);
        newStudent.setHoTen(hoTen);
        newStudent.setNgaySinh(ngaySinh_datepicker.getValue());
        newStudent.setGioiTinh(gioiTinh_field.getText());
        newStudent.setQueQuan(queQuan_field.getText());
        newStudent.setLop(tenLopHP_field.getText());
        newStudent.setUsername(username);
        newStudent.setPassword(StuPassword_field.getText());
        boolean success = AdminStuManageDAO.addStudent(newStudent, maKhoa);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm sinh viên thành công.");
            loadStudentData();
            clearFields();
        }
    }

    @FXML
    private void onDeleteStudent() {
        TreeItem<AdminStuManageModel> selectedItem = stuManageTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn một sinh viên để xóa.");
            return;
        }
        AdminStuManageModel selectedStudent = selectedItem.getValue();
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa sinh viên: " + selectedStudent.getHoTen() + "?");
        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            boolean success = AdminStuManageDAO.deleteStudent(selectedStudent.getMaSV());
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa sinh viên thành công.");
                loadStudentData();
                clearFields();
            }
        }
    }

    @FXML
    private void onSearchStudent() {
        String filter = textFildSearchStu.getText().toLowerCase();
        if (filter.isEmpty()) {
            loadStudentData();
            return;
        }
        ObservableList<TreeItem<AdminStuManageModel>> filteredList = FXCollections.observableArrayList();
        ObservableList<TreeItem<AdminStuManageModel>> otherList = FXCollections.observableArrayList();
        for (AdminStuManageModel student : studentList) {
            if (student.getHoTen().toLowerCase().contains(filter) ||
                    student.getMaSV().toLowerCase().contains(filter)) {
                filteredList.add(new TreeItem<>(student));
            } else {
                otherList.add(new TreeItem<>(student));
            }
        }
        filteredList.addAll(otherList);
        root.getChildren().setAll(filteredList);
    }

    //
    // =========================================================================
    // *** HÀM MỚI ĐỂ MỞ CỬA SỔ XEM ĐIỂM ***
    // =========================================================================
    @FXML
    private void onViewScore() {
        // 1. Lấy sinh viên đang được chọn
        TreeItem<AdminStuManageModel> selectedItem = stuManageTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn một sinh viên để xem điểm.");
            return;
        }
        AdminStuManageModel selectedStudent = selectedItem.getValue();

        try {
            // 2. Tải FXML của cửa sổ xem điểm
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Admin/AdminStudentScoreView.fxml"));
            Parent root = loader.load();

            // 3. Lấy Controller của cửa sổ đó
            AdminStudentScoreController scoreController = loader.getController();

            // 4. TRUYỀN DỮ LIỆU (quan trọng nhất)
            scoreController.initData(selectedStudent);

            // 5. Hiển thị cửa sổ mới
            Stage stage = new Stage();
            stage.setTitle("Bảng điểm chi tiết - " + selectedStudent.getHoTen());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Khóa cửa sổ chính
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi FXML", "Không thể tải giao diện xem điểm.");
        }
    }


    private void clearFields() {
        maSV_field.clear();
        hoTen_field.clear();
        ngaySinh_datepicker.setValue(null);
        gioiTinh_field.clear();
        queQuan_field.clear();
        khoa_combobox.getSelectionModel().clearSelection();
        tenLopHP_field.clear();
        stuUsername_field.clear();
        StuPassword_field.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}