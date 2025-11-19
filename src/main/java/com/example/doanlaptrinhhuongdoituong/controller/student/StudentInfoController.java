package com.example.doanlaptrinhhuongdoituong.controller.student;

import com.example.doanlaptrinhhuongdoituong.model.Student.StudentInfo;
import com.example.doanlaptrinhhuongdoituong.model.Student.StudentInfoModel;
import com.example.doanlaptrinhhuongdoituong.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentInfoController implements Initializable {

    // --- Khai báo FXML từ StudentInfoView.fxml ---
    @FXML private ImageView avatar_img;

    // Các Label trong GridPane
    @FXML private Label student_id;
    @FXML private Label student_name;
    @FXML private Label student_gender;
    @FXML private Label student_major;
    @FXML private Label student_birthdate;
    @FXML private Label student_class;
    @FXML private Label student_address;
    @FXML private Label student_year;

    // --- Biến logic ---
    private StudentInfoModel model;
    private String maSinhVien;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.model = new StudentInfoModel();

        // 1. Lấy maSV từ Session (BẮT BUỘC)
        this.maSinhVien = UserSession.getInstance().getMaSinhVien();

        if (this.maSinhVien == null || this.maSinhVien.isEmpty()) {
            System.err.println("StudentInfoController: maSinhVien là null!");
            // Hiển thị lỗi trên các label
            student_id.setText("Mã số SV: (LỖI)");
            student_name.setText("Họ tên: (CHƯA ĐĂNG NHẬP)");
            return;
        }

        // 2. Tải và hiển thị thông tin
        loadStudentInfo();
    }

    /**
     * Tải thông tin sinh viên và điền vào các Label
     */
    private void loadStudentInfo() {
        StudentInfo info = model.getStudentInfo(maSinhVien);

        if (info == null) {
            System.err.println("Không thể tải thông tin sinh viên với mã: " + maSinhVien);
            return;
        }

        // 3. Điền dữ liệu vào các Label
        student_id.setText("Mã số SV: " + info.getMaSV());
        student_name.setText("Họ tên: " + info.getHoTen());
        student_gender.setText("Giới tính: " + info.getGioiTinh());
        student_major.setText("Ngành: " + info.getTenKhoa());
        student_birthdate.setText("Ngày sinh: " + info.getNgaySinh());
        student_class.setText("Lớp học: " + info.getLop());
        student_address.setText("Nơi sinh: " + info.getQueQuan());
        student_year.setText("Khóa học: " + info.getKhoaHoc());

        // (Phần Avatar bạn có thể bổ sung logic tải ảnh nếu có)
    }
}