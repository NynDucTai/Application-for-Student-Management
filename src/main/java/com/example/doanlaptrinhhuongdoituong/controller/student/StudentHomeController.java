package com.example.doanlaptrinhhuongdoituong.controller.student;

import com.example.doanlaptrinhhuongdoituong.model.Student.CourseRegistration;
import com.example.doanlaptrinhhuongdoituong.model.Student.Semester;
import com.example.doanlaptrinhhuongdoituong.model.Student.StudentHomeModel;
import com.example.doanlaptrinhhuongdoituong.model.Student.StudentInfo;
import com.example.doanlaptrinhhuongdoituong.util.UserSession;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis; // Import thêm
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentHomeController implements Initializable {

    // --- Khai báo FXML ---
    @FXML private Label userName_label;
    @FXML private ImageView avatar_img;
    @FXML private Label student_id_label;
    @FXML private Label student_name_label;
    @FXML private Label student_gender_label;
    @FXML private Label student_major_label;
    @FXML private Label student_birthdate_label;
    @FXML private Label student_class_label;
    @FXML private Label student_address_label;
    @FXML private Label student_year_label;
    @FXML private ComboBox<Semester> chonKi_comboBox;
    @FXML private ListView<CourseRegistration> court_list;
    @FXML private ComboBox<Semester> chonKiHoc_comboBox;
    @FXML private BarChart<String, Number> score_chart;

    // --- KHAI BÁO MỚI ĐỂ SỬA LỖI BIỂU ĐỒ ---
    @FXML private CategoryAxis xAxis;

    // --- Biến logic ---
    private StudentHomeModel model;
    private String maSinhVien;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.model = new StudentHomeModel();
        this.maSinhVien = UserSession.getInstance().getMaSinhVien();

        if (this.maSinhVien == null || this.maSinhVien.isEmpty()) {
            userName_label.setText("Xin chào (LỖI: CHƯA ĐĂNG NHẬP)");
            return;
        }

        // --- SỬA LỖI BIỂU ĐỒ DÍNH NHAU ---
        // Tắt animation và xoay nhãn 45 độ
        if (xAxis != null) {
        }
        score_chart.setAnimated(false);
        // --- KẾT THÚC SỬA LỖI ---

        // Tải thông tin
        loadStudentInfo(); // Gọi hàm đã sửa
        loadSemesters();
        setupCourseListView();

        // Gán sự kiện
        chonKiHoc_comboBox.setOnAction(event -> {
            Semester selected = chonKiHoc_comboBox.getSelectionModel().getSelectedItem();
            if (selected != null) {
                loadScoreChart(selected);
            }
        });

        chonKi_comboBox.setOnAction(event -> {
            Semester selected = chonKi_comboBox.getSelectionModel().getSelectedItem();
            if (selected != null) {
                loadCourseList(selected);
            }
        });
    }

    //
    // =========================================================================
    // *** PHẦN SỬA LỖI THIẾU THÔNG TIN NẰM TẠI ĐÂY ***
    // =========================================================================
    /**
     * Tải thông tin sinh viên lên GridPane (Đã bổ sung đầy đủ)
     */
    private void loadStudentInfo() {
        StudentInfo info = model.getStudentInfo(maSinhVien);
        if (info == null) {
            System.err.println("Không thể tải thông tin sinh viên với mã: " + maSinhVien);
            return;
        }

        // Cập nhật tất cả các Label
        userName_label.setText("Xin chào " + info.getHoTen());
        student_id_label.setText("Mã số SV: " + info.getMaSV());
        student_name_label.setText("Họ tên: " + info.getHoTen());
        student_gender_label.setText("Giới tính: " + info.getGioiTinh());
        student_major_label.setText("Ngành: " + info.getTenKhoa());
        student_birthdate_label.setText("Ngày sinh: " + info.getNgaySinh());
        student_class_label.setText("Lớp học: " + info.getLop());
        student_address_label.setText("Nơi sinh: " + info.getQueQuan());
        student_year_label.setText("Khóa học: " + info.getKhoaHoc());
    }

    // (Hàm loadSemesters giữ nguyên)
    private void loadSemesters() {
        ObservableList<Semester> semesters = model.getSemesters(maSinhVien);
        StringConverter<Semester> converter = new StringConverter<>() {
            @Override public String toString(Semester semester) {
                return (semester == null) ? "Chọn kỳ học" : semester.toString();
            }
            @Override public Semester fromString(String s) { return null; }
        };
        chonKiHoc_comboBox.setConverter(converter);
        chonKi_comboBox.setConverter(converter);
        chonKiHoc_comboBox.setItems(semesters);
        chonKi_comboBox.setItems(semesters);
    }

    // (Hàm loadScoreChart giữ nguyên - dùng điểm 10)
    private void loadScoreChart(Semester semester) {
        ObservableList<CourseRegistration> courses = model.getCoursesAndScores(
                maSinhVien, semester.getHocKy(), semester.getNamHoc());

        score_chart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Điểm hệ 10 - " + semester.toString());

        for (CourseRegistration course : courses) {
            Float diemTK = course.getDiemTK();
            if (diemTK != null) {
                series.getData().add(new XYChart.Data<>(course.getTenMonHoc(), diemTK));
            }
        }
        score_chart.getData().add(series);
    }

    // (Các hàm loadCourseList và setupCourseListView giữ nguyên)
    private void loadCourseList(Semester semester) {
        ObservableList<CourseRegistration> courses = model.getCoursesAndScores(
                maSinhVien, semester.getHocKy(), semester.getNamHoc());
        court_list.setItems(courses);
    }

    private void setupCourseListView() {
        court_list.setCellFactory(param -> new ListCell<CourseRegistration>() {
            @Override
            protected void updateItem(CourseRegistration item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTenMonHoc() + " (Số tín chỉ: " + item.getSoTinChi() + ")");
                }
            }
        });
    }
}