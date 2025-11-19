package com.example.doanlaptrinhhuongdoituong.controller.teacher;

import com.example.doanlaptrinhhuongdoituong.model.Teacher.LopDayHoc;
import com.example.doanlaptrinhhuongdoituong.model.Teacher.SinhVienDiem;
import com.example.doanlaptrinhhuongdoituong.model.Teacher.TeacherAddScoreModel;
import com.example.doanlaptrinhhuongdoituong.util.ScoreCalculator;
import com.example.doanlaptrinhhuongdoituong.util.UserSession;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherAddScoreController implements Initializable {

    // --- Khai báo FXML ---
    @FXML private ComboBox<LopDayHoc> chonLop_comboBox;
    @FXML private TextField timSV_field;
    @FXML private Button timKiem_button;
    @FXML private TreeTableView<SinhVienDiem> stuScore_table;
    @FXML private TreeTableColumn<SinhVienDiem, Number> soThuTu_col;
    @FXML private TreeTableColumn<SinhVienDiem, String> maSV_col;
    @FXML private TreeTableColumn<SinhVienDiem, String> hoTen_col;
    @FXML private TreeTableColumn<SinhVienDiem, Float> diemqt_col;
    @FXML private TreeTableColumn<SinhVienDiem, Float> diemkt_col;

    // --- BỔ SUNG KHAI BÁO FXML CHO CỘT MỚI ---
    @FXML private TreeTableColumn<SinhVienDiem, Float> diemtk_col; // Cột Điểm TK (hệ 10)

    @FXML private TreeTableColumn<SinhVienDiem, Float> diemHe4_col;
    @FXML private TreeTableColumn<SinhVienDiem, String> diemChu_col;
    @FXML private TreeTableColumn<SinhVienDiem, String> trangThai_col;

    @FXML private Label tenSV_label;
    @FXML private TextField diemQT_field;
    @FXML private TextField diemKT_field;
    @FXML private Button capNhat_button;

    // --- Biến logic ---
    private TeacherAddScoreModel model;
    private String maGiangVien;
    private SinhVienDiem selectedStudent = null;
    private LopDayHoc selectedLop = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.model = new TeacherAddScoreModel();
        this.maGiangVien = UserSession.getInstance().getMaGiangVien();

        if (this.maGiangVien == null) {
            System.err.println("TeacherAddScoreController: maGiangVien là null!");
            return;
        }

        setupTableColumns();
        loadComboBoxLop();
        addTableSelectionListener();

        // Gán sự kiện cho các nút
        chonLop_comboBox.setOnAction(event -> loadStudentTable());
        timKiem_button.setOnAction(event -> loadStudentTable());
        timSV_field.setOnAction(event -> loadStudentTable());
        capNhat_button.setOnAction(event -> handleCapNhatDiem());
    }

    // (Các hàm loadComboBoxLop, loadStudentTable, addTableSelectionListener giữ nguyên)

    private void loadComboBoxLop() {
        ObservableList<LopDayHoc> dsLop = model.getLopGiangDay(maGiangVien);
        chonLop_comboBox.setItems(dsLop);
        chonLop_comboBox.setConverter(new StringConverter<LopDayHoc>() {
            @Override public String toString(LopDayHoc lop) { return (lop == null) ? "Chọn lớp" : lop.toString(); }
            @Override public LopDayHoc fromString(String string) { return null; }
        });
    }

    private void loadStudentTable() {
        this.selectedLop = chonLop_comboBox.getSelectionModel().getSelectedItem();
        String searchText = timSV_field.getText();
        if (this.selectedLop == null) {
            stuScore_table.setRoot(null);
            return;
        }
        String maLopHP = this.selectedLop.getMaLopHP();
        ObservableList<SinhVienDiem> dsSV = model.getDiemSinhVien(maLopHP, searchText);
        TreeItem<SinhVienDiem> root = new TreeItem<>();
        for (SinhVienDiem sv : dsSV) {
            root.getChildren().add(new TreeItem<>(sv));
        }
        stuScore_table.setRoot(root);
        stuScore_table.setShowRoot(false);
        clearSelectionFields();
    }

    private void addTableSelectionListener() {
        stuScore_table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection.getValue() != null) {
                this.selectedStudent = newSelection.getValue();
                tenSV_label.setText("Sinh viên: " + selectedStudent.getHoTen() + " (" + selectedStudent.getMaSV() + ")");
                Float diemQT = selectedStudent.getDiemQT();
                Float diemKT = selectedStudent.getDiemKT();
                diemQT_field.setText(diemQT == null ? "" : diemQT.toString());
                diemKT_field.setText(diemKT == null ? "" : diemKT.toString());
            } else {
                clearSelectionFields();
            }
        });
    }

    /**
     * Xử lý khi nhấn nút Cập nhật (ĐÃ CẬP NHẬT)
     */
    private void handleCapNhatDiem() {
        if (this.selectedStudent == null || this.selectedLop == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Bạn chưa chọn sinh viên hoặc lớp học.");
            return;
        }

        float diemQT, diemKT;
        try {
            diemQT = Float.parseFloat(diemQT_field.getText());
            diemKT = Float.parseFloat(diemKT_field.getText());
            if (diemQT < 0 || diemQT > 10 || diemKT < 0 || diemKT > 10) {
                throw new NumberFormatException("Điểm phải trong khoảng 0-10");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi Nhập Liệu", "Điểm phải là số (0-10).");
            return;
        }

        String maSV = this.selectedStudent.getMaSV();
        String maLopHP = this.selectedLop.getMaLopHP();

        // 3. Gọi Model để cập nhật DB (Model sẽ dùng ScoreCalculator đã sửa)
        ScoreCalculator.ScoreResult result = model.updateDiem(maSV, maLopHP, diemQT, diemKT);

        // 4. Cập nhật giao diện (nếu thành công)
        if (result != null) {
            // Cập nhật đối tượng POJO trong Java
            // Bảng sẽ tự động refresh vì dùng Property
            selectedStudent.setDiemQT(diemQT);
            selectedStudent.setDiemKT(diemKT);

            // --- CẬP NHẬT ĐIỂM TK (hệ 10) MỚI ---
            selectedStudent.setDiemTK(result.diemTK);

            selectedStudent.setDiemHe4(result.diemHe4);
            selectedStudent.setDiemChu(result.diemChu);
            selectedStudent.setTrangThai(result.trangThai);

            stuScore_table.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật điểm thành công.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi Database", "Cập nhật điểm thất bại.");
        }
    }

    private void clearSelectionFields() {
        this.selectedStudent = null;
        tenSV_label.setText("Sinh viên:");
        diemQT_field.clear();
        diemKT_field.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Cài đặt CellValueFactory (ĐÃ CẬP NHẬT)
     */
    private void setupTableColumns() {
        soThuTu_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().sttProperty());
        maSV_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().maSVProperty());
        hoTen_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().hoTenProperty());
        diemqt_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemQTProperty());
        diemkt_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemKTProperty());

        // --- BỔ SUNG BINDING CHO CỘT MỚI ---
        diemtk_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemTKProperty());

        diemHe4_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemHe4Property());
        diemChu_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemChuProperty());
        trangThai_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().trangThaiProperty());
    }
}