package com.example.doanlaptrinhhuongdoituong.controller.admin;

import com.example.doanlaptrinhhuongdoituong.dao.Admin.AdminSubjectManageDAO;
import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminSubjectManageModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;

/**
 * Controller cho giao diện AdminSubjectManageView.fxml.
 */
public class AdminSubjectManageController implements Initializable {

    // --- Khai báo các thành phần UI (khớp 100% với fx:id) ---
    @FXML private TextField maMonHoc_field;
    @FXML private TextField tenMonHoc_field;
    @FXML private ComboBox<String> khoa_comboBox;
    @FXML private TextField soTinChi_field;
    @FXML private TextField hocKi_field;
    @FXML private TextField namHoc_field;
    @FXML private TextField loaiMon_field;

    @FXML private Button themMon_button;
    @FXML private Button xoaMon_button;
    @FXML private TextField timKiemMonHoc_field;
    @FXML private Button timKiem_button;

    @FXML private TreeTableView<AdminSubjectManageModel> stuManageTable; // Đổi tên fx:id bảng này
    @FXML private TreeTableColumn<AdminSubjectManageModel, String> maMon_col;
    @FXML private TreeTableColumn<AdminSubjectManageModel, String> tenMon_col;
    @FXML private TreeTableColumn<AdminSubjectManageModel, String> khoa_col;
    @FXML private TreeTableColumn<AdminSubjectManageModel, Number> soTInChi_col; // fx:id có chữ 'I' hoa
    @FXML private TreeTableColumn<AdminSubjectManageModel, Number> hocKi_col;
    @FXML private TreeTableColumn<AdminSubjectManageModel, String> namHoc_col;
    @FXML private TreeTableColumn<AdminSubjectManageModel, String> loaiMon_col;

    // Danh sách đầy đủ môn học (dùng cho tìm kiếm)
    private ObservableList<AdminSubjectManageModel> subjectList;
    private TreeItem<AdminSubjectManageModel> root;

    /**
     * Hàm này được tự động gọi sau khi FXML được tải.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Cài đặt các cột cho bảng (Khắc phục lỗi "Bảng trống")
        setupTableColumns();

        // 2. Tải dữ liệu cho ComboBox Khoa
        loadKhoaData();

        // 3. Tải dữ liệu ban đầu cho bảng
        loadSubjectData();

        // 4. Lắng nghe sự kiện click chuột vào hàng
        setupRowSelectionListener();

        // 5. Gán sự kiện cho các nút
        themMon_button.setOnAction(event -> onAddSubject());
        xoaMon_button.setOnAction(event -> onDeleteSubject());
        timKiem_button.setOnAction(event -> onSearchSubject());
    }

    // 1. Cài đặt các cột (Dùng Lambda - Tránh lỗi "bảng trống")
    private void setupTableColumns() {
        maMon_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getMaMon()));

        tenMon_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getTenMon()));

        // Cột Khoa sẽ hiển thị TÊN KHOA
        khoa_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getTenKhoa()));

        soTInChi_col.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getValue().getSoTinChi()));

        hocKi_col.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getValue().getHocKi()));

        namHoc_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getNamHoc()));

        loaiMon_col.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValue().getLoaiMon()));
    }

    // 2. Tải dữ liệu Khoa vào ComboBox
    private void loadKhoaData() {
        ObservableList<String> khoaList = AdminSubjectManageDAO.getMaKhoaList();
        khoa_comboBox.setItems(khoaList);
        if (!khoaList.isEmpty()) {
            khoa_comboBox.setValue(khoaList.get(0)); // Chọn 'CNTT' làm mặc định
        }
    }

    // 3. Tải dữ liệu Môn học từ CSDL
    private void loadSubjectData() {
        // Lấy danh sách từ CSDL
        subjectList = AdminSubjectManageDAO.getSubjects();

        // Cài đặt root cho TreeTableView
        root = new TreeItem<>(new AdminSubjectManageModel()); // Một model rỗng (làm root ảo)
        stuManageTable.setShowRoot(false);
        stuManageTable.setRoot(root);

        // Chuyển danh sách Model thành danh sách TreeItem
        root.getChildren().clear(); // Xóa dữ liệu cũ
        subjectList.forEach(subject -> {
            root.getChildren().add(new TreeItem<>(subject));
        });
    }

    // 4. Lắng nghe sự kiện chọn hàng
    private void setupRowSelectionListener() {
        stuManageTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection.getValue() != null) {
                AdminSubjectManageModel selectedSubject = newSelection.getValue();

                // ... (các hàm setText cho maMon, tenMon, khoa, soTinChi)
                soTinChi_field.setText(String.valueOf(selectedSubject.getSoTinChi()));
                hocKi_field.setText(String.valueOf(selectedSubject.getHocKi()));

                // --- BỔ SUNG DÒNG NÀY ---
                namHoc_field.setText(selectedSubject.getNamHoc());
                // ------------------------

                loaiMon_field.setText(selectedSubject.getLoaiMon());
            }
        });
    }

    // 5. Xử lý nút Thêm Môn
    @FXML
    private void onAddSubject() {
        // Lấy dữ liệu từ các trường
        String maMon = maMonHoc_field.getText();
        String tenMon = tenMonHoc_field.getText();
        String maKhoa = khoa_comboBox.getValue();

        if (maMon.isEmpty() || tenMon.isEmpty() || maKhoa == null) {
            showAlert(Alert.AlertType.ERROR, "Thiếu thông tin", "Mã môn, Tên môn, và Khoa là bắt buộc.");
            return;
        }

        try {
            // Tạo đối tượng Model mới
            AdminSubjectManageModel newSubject = new AdminSubjectManageModel();
            newSubject.setMaMon(maMon);
            newSubject.setTenMon(tenMon);
            newSubject.setMaKhoa(maKhoa);
            // Chuyển đổi String sang int (có kiểm tra lỗi)
            newSubject.setSoTinChi(Integer.parseInt(soTinChi_field.getText()));
            newSubject.setHocKi(Integer.parseInt(hocKi_field.getText()));
            newSubject.setNamHoc(namHoc_field.getText());
            newSubject.setLoaiMon(loaiMon_field.getText());

            // Gọi DAO để thêm
            boolean success = AdminSubjectManageDAO.addSubject(newSubject);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm môn học thành công.");
                loadSubjectData(); // Tải lại toàn bộ bảng
                clearFields();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi định dạng", "Số tín chỉ và Học kì phải là SỐ.");
        }
    }

    // 6. Xử lý nút Xóa Môn
    @FXML
    private void onDeleteSubject() {
        TreeItem<AdminSubjectManageModel> selectedItem = stuManageTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn một môn học để xóa.");
            return;
        }

        AdminSubjectManageModel selectedSubject = selectedItem.getValue();
        String maMon = selectedSubject.getMaMon();

        // Xác nhận xóa
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa môn học: " + selectedSubject.getTenMon() + "?");
        confirmAlert.setContentText("Hành động này sẽ thất bại nếu môn học đã được sử dụng (ví dụ: trong Lớp Học Phần).");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            boolean success = AdminSubjectManageDAO.deleteSubject(maMon);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa môn học thành công.");
                loadSubjectData(); // Tải lại bảng
                clearFields();
            }
            // Nếu không thành công, hàm DAO đã tự hiển thị lỗi (ví dụ: lỗi F-Key)
        }
    }

    // 7. Xử lý nút Tìm kiếm
    @FXML
    private void onSearchSubject() {
        String filter = timKiemMonHoc_field.getText().toLowerCase();
        if (filter.isEmpty()) {
            loadSubjectData(); // Nếu ô trống, tải lại tất cả
            return;
        }

        // Lọc và đưa lên đầu
        ObservableList<TreeItem<AdminSubjectManageModel>> filteredList = FXCollections.observableArrayList();
        ObservableList<TreeItem<AdminSubjectManageModel>> otherList = FXCollections.observableArrayList();

        for (AdminSubjectManageModel subject : subjectList) {
            if (subject.getTenMon().toLowerCase().contains(filter) ||
                    subject.getMaMon().toLowerCase().contains(filter)) {
                filteredList.add(new TreeItem<>(subject)); // Khớp -> lên đầu
            } else {
                otherList.add(new TreeItem<>(subject)); // Không khớp -> xuống dưới
            }
        }

        filteredList.addAll(otherList);
        root.getChildren().setAll(filteredList);
    }

    // Hàm tiện ích để xóa các trường
    private void clearFields() {
        maMonHoc_field.clear();
        tenMonHoc_field.clear();
        soTinChi_field.clear();
        hocKi_field.clear();
        namHoc_field.clear();
        loaiMon_field.clear();
        // Giữ lại lựa chọn của ComboBox Khoa
        if (!khoa_comboBox.getItems().isEmpty()) {
            khoa_comboBox.setValue(khoa_comboBox.getItems().get(0));
        }
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