package com.example.doanlaptrinhhuongdoituong.dao.Student;

import com.example.doanlaptrinhhuongdoituong.model.Student.RetakeCourse;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRetakeClassDAO {

    // 1. Lấy các lớp HỌC LẠI mà sinh viên CÓ THỂ ĐĂNG KÝ
    public ObservableList<RetakeCourse> getAvailableRetakeCourses(String maSV) {
        ObservableList<RetakeCourse> courses = FXCollections.observableArrayList();

        // Query này lọc CÁC LỚP HỌC LẠI:
        // 1. Dựa trên các môn mà sinh viên này đã "Trượt"
        // 2. Và sinh viên chưa đăng ký lớp học lại này
        String query = "SELECT lhp.maLopHP, mh.maMon, mh.tenMon, mh.soTinChi, lhp.hocKy, lhp.namHoc " +
                "FROM lophocphan lhp " +
                "JOIN monhoc mh ON lhp.maMon = mh.maMon " +
                "WHERE lhp.loaiLop = 'HOC_LAI' " + // Yêu cầu: Chỉ lớp học lại
                "  AND lhp.trangThai = 'CHINH_THUC' " +
                "  AND (lhp.hanDangKi >= CURDATE() OR lhp.hanDangKi IS NULL) " +
                "  AND lhp.siSoHienTai < lhp.siSoToiDa " +
                "  AND lhp.maLopHP NOT IN ( " + // Sinh viên chưa đăng ký lớp này
                "      SELECT dkk.maLopHP FROM dangki_ketqua dkk WHERE dkk.maSV = ? " +
                "  ) " +
                "  AND mh.maMon IN ( " + // Môn học này phải là môn sinh viên đã trượt
                "      SELECT lhp_inner.maMon " +
                "      FROM dangki_ketqua dkk_inner " +
                "      JOIN lophocphan lhp_inner ON dkk_inner.maLopHP = lhp_inner.maLopHP " +
                "      WHERE dkk_inner.maSV = ? AND dkk_inner.trangThaiHoc = 'Trượt' " +
                "  )";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, maSV); // Cho điều kiện NOT IN
            pstmt.setString(2, maSV); // Cho điều kiện IN (môn trượt)

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                courses.add(new RetakeCourse(
                        rs.getString("maMon"),
                        rs.getString("maLopHP"),
                        rs.getString("tenMon"),
                        rs.getInt("soTinChi"),
                        rs.getInt("hocKy"),
                        rs.getString("namHoc")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return courses;
    }

    // 2. Thực hiện đăng ký (Sử dụng Transaction)
    // Giống hệt các file DAO đăng ký trước
    public boolean registerCourse(String maSV, String maLopHP) {
        String queryInsert = "INSERT INTO dangki_ketqua (maSV, maLopHP, trangThaiHoc) VALUES (?, ?, 'Đang học')";
        String queryUpdate = "UPDATE lophocphan SET siSoHienTai = siSoHienTai + 1 WHERE maLopHP = ?";

        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Thêm vào bảng dangki_ketqua
            try (PreparedStatement pstmtInsert = conn.prepareStatement(queryInsert)) {
                pstmtInsert.setString(1, maSV);
                pstmtInsert.setString(2, maLopHP);
                pstmtInsert.executeUpdate();
            }

            // 2. Cập nhật sĩ số
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(queryUpdate)) {
                pstmtUpdate.setString(1, maLopHP);
                pstmtUpdate.executeUpdate();
            }

            conn.commit(); // Hoàn tất Transaction
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Hoàn tác nếu lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}