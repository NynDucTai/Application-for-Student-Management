package com.example.doanlaptrinhhuongdoituong.controller.admin;

import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminStudentScoreModel;
import com.example.doanlaptrinhhuongdoituong.model.Student.StudentScoreRecord;
import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminStuManageModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminStudentScoreController implements Initializable {

    // --- Khai báo FXML ---
    @FXML private Label hoTen_label;
    @FXML private ComboBox<String> chonKiHoc_comboBox;
    @FXML private ComboBox<String> hconNamHoc_comboBox; // Sửa lỗi FXML: fx:id="hconNamHoc_comboBox"
    @FXML private TreeTableView<StudentScoreRecord> score_table;
    @FXML private BarChart<String, Number> bar_chart;

    // Khai báo các cột
    @FXML private TreeTableColumn<StudentScoreRecord, String> hp_id_col;
    @FXML private TreeTableColumn<StudentScoreRecord, String> hp_name_col;
    @FXML private TreeTableColumn<StudentScoreRecord, String> lophp_col;
    @FXML private TreeTableColumn<StudentScoreRecord, Number> tinchi_col;
    @FXML private TreeTableColumn<StudentScoreRecord, Float> diemqt_col;
    @FXML private TreeTableColumn<StudentScoreRecord, Float> diemkt_col;
    @FXML private TreeTableColumn<StudentScoreRecord, Float> diemtk_col;
    @FXML private TreeTableColumn<StudentScoreRecord, Float> diem4_col;
    @FXML private TreeTableColumn<StudentScoreRecord, String> diemchu_col;
    @FXML private TreeTableColumn<StudentScoreRecord, String> dat_col;

    // Trục X của biểu đồ (cần fx:id trong FXML)
    @FXML private CategoryAxis xAxis; // Giả sử BarChart/xAxis có fx:id="xAxis"

    // --- Biến logic ---
    private AdminStudentScoreModel model;
    private AdminStuManageModel selectedStudent; // Sinh viên được truyền từ giao diện trước

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.model = new AdminStudentScoreModel();

        // 1. Cài đặt bảng
        setupTableColumns();

        // 2. Gán sự kiện
        chonKiHoc_comboBox.setOnAction(event -> loadTableAndChart());
        hconNamHoc_comboBox.setOnAction(event -> loadTableAndChart());

        // 3. Cài đặt biểu đồ
        bar_chart.setAnimated(false);
        // (Nếu xAxis null, bạn cần thêm fx:id="xAxis" vào <CategoryAxis> trong FXML)
        if (xAxis != null) {
            xAxis.setTickLabelRotation(45);
        }
    }

    /**
     * HÀM NÀY ĐƯỢC AdminStuManageController GỌI
     * để truyền thông tin sinh viên qua.
     */
    public void initData(AdminStuManageModel student) {
        this.selectedStudent = student;

        // Ngay lập tức cập nhật giao diện
        if (this.selectedStudent != null) {
            hoTen_label.setText("Sinh viên: " + selectedStudent.getHoTen());
            loadComboBoxes();
        } else {
            hoTen_label.setText("Lỗi: Không có sinh viên nào được chọn.");
        }
    }

    /**
     * Tải dữ liệu cho 2 ComboBox
     */
    private void loadComboBoxes() {
        String maSV = selectedStudent.getMaSV();
        chonKiHoc_comboBox.setItems(model.getHocKy(maSV));
        hconNamHoc_comboBox.setItems(model.getNamHoc(maSV));
    }

    /**
     * Tải dữ liệu cho bảng VÀ biểu đồ
     */
    private void loadTableAndChart() {
        String hocKy = chonKiHoc_comboBox.getValue();
        String namHoc = hconNamHoc_comboBox.getValue();
        String maSV = selectedStudent.getMaSV();

        if (hocKy != null && namHoc != null) {
            ObservableList<StudentScoreRecord> scores = model.getScores(maSV, hocKy, namHoc);

            // 1. Tải bảng
            populateTable(scores);
            // 2. Tải biểu đồ
            populateChart(scores, hocKy, namHoc);

        } else {
            // Xóa bảng và biểu đồ nếu chưa chọn đủ
            score_table.setRoot(null);
            bar_chart.getData().clear();
        }
    }

    /**
     * Nạp dữ liệu vào TreeTableView
     */
    private void populateTable(ObservableList<StudentScoreRecord> scores) {
        TreeItem<StudentScoreRecord> root = new TreeItem<>();
        for (StudentScoreRecord record : scores) {
            root.getChildren().add(new TreeItem<>(record));
        }
        score_table.setRoot(root);
        score_table.setShowRoot(false);
    }

    /**
     * Nạp dữ liệu vào BarChart (thang điểm 10)
     */
    private void populateChart(ObservableList<StudentScoreRecord> scores, String hocKy, String namHoc) {
        bar_chart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Điểm hệ 10 - Kỳ " + hocKy + " - " + namHoc);

        for (StudentScoreRecord record : scores) {
            // Lấy điểm TK (hệ 10)
            Float diemTK = record.diemTKProperty().get();
            if (diemTK != null) {
                series.getData().add(new XYChart.Data<>(record.tenMonHocProperty().get(), diemTK));
            }
        }
        bar_chart.getData().add(series);
    }

    /**
     * Cài đặt CellValueFactory (chống lỗi Null)
     */
    private void setupTableColumns() {
        hp_id_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().maHPProperty());
        hp_name_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().tenMonHocProperty());
        lophp_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().lopHPProperty());
        tinchi_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().soTinChiProperty());
        diemqt_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemQTProperty());
        diemkt_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemKTProperty());
        diemtk_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemTKProperty());
        diem4_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemHe4Property());
        diemchu_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().diemChuProperty());
        dat_col.setCellValueFactory(p -> (p.getValue() == null || p.getValue().getValue() == null) ? null : p.getValue().getValue().trangThaiProperty());
    }
}