package com.example.doanlaptrinhhuongdoituong.dao.Student;

import com.example.doanlaptrinhhuongdoituong.model.Student.WishlistCourse;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentWishClassDAO {

    // 1. Lấy các lớp NGUYỆN VỌNG CÓ THỂ ĐĂNG KÝ
    public ObservableList<WishlistCourse> getAvailableWishlistCourses(String maSV) {
        ObservableList<WishlistCourse> courses = FXCollections.observableArrayList();

        // Query này lọc CÁC LỚP NGUYỆN VỌNG mà sinh viên CHƯA ĐĂNG KÝ
        String query = "SELECT lhp.maLopHP, mh.tenMon, mh.soTinChi, lhp.hocKy, lhp.namHoc " +
                "FROM lophocphan lhp " +
                "JOIN monhoc mh ON lhp.maMon = mh.maMon " +
                "WHERE lhp.loaiLop = 'NGUYEN_VONG' " + // Yêu cầu: Chỉ lớp nguyện vọng
                "  AND lhp.trangThai = 'CHINH_THUC' " + // Phải được mở (Không phải 'DANG_CHO')
                "  AND (lhp.hanDangKi >= CURDATE() OR lhp.hanDangKi IS NULL) " + // Fix lỗi NULL
                "  AND lhp.siSoHienTai < lhp.siSoToiDa " +
                "  AND lhp.maLopHP NOT IN ( " +
                "      SELECT dkk.maLopHP FROM dangki_ketqua dkk WHERE dkk.maSV = ? " +
                "  )";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, maSV);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                courses.add(new WishlistCourse(
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
    // Giống hệt file StudentCourtSignedDAO
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