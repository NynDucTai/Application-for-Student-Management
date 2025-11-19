package com.example.doanlaptrinhhuongdoituong.dao.Student;

import com.example.doanlaptrinhhuongdoituong.model.Student.AvailableCourse;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentCourtSignedDAO {

    // (Hàm getAllHocKy và getAllNamHoc giữ nguyên)
    public ObservableList<String> getAllHocKy() {
        ObservableList<String> hocKyList = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT hocKy FROM lophocphan WHERE loaiLop = 'CHINH_QUY' ORDER BY hocKy";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                hocKyList.add(String.valueOf(rs.getInt("hocKy")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return hocKyList;
    }

    public ObservableList<String> getAllNamHoc() {
        ObservableList<String> namHocList = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT namHoc FROM lophocphan WHERE loaiLop = 'CHINH_QUY' ORDER BY namHoc DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                namHocList.add(rs.getString("namHoc"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return namHocList;
    }


    //
    // =========================================================================
    // *** PHẦN SỬA LỖI NẰM TẠI ĐÂY (HÀM getAvailableCourses) ***
    //
    // Đã sửa lại điều kiện 'hanDangKi'
    // =========================================================================
    public ObservableList<AvailableCourse> getAvailableCourses(String maSV, String hocKy, String namHoc) {
        ObservableList<AvailableCourse> courses = FXCollections.observableArrayList();

        String query = "SELECT mh.maMon, lhp.maLopHP, lhp.tenLopHP, mh.tenMon, mh.soTinChi, lhp.namHoc " +
                "FROM lophocphan lhp " +
                "JOIN monhoc mh ON lhp.maMon = mh.maMon " +
                "WHERE lhp.hocKy = ? AND lhp.namHoc = ? " +
                "  AND lhp.trangThai = 'CHINH_THUC' " +
                "  AND lhp.loaiLop = 'CHINH_QUY' " +

                // SỬA LỖI Ở ĐÂY:
                // Cho phép đăng ký nếu (Hạn >= hôm nay) HOẶC (Hạn là NULL)
                "  AND (lhp.hanDangKi >= CURDATE() OR lhp.hanDangKi IS NULL) " +

                "  AND lhp.siSoHienTai < lhp.siSoToiDa " +
                "  AND lhp.maLopHP NOT IN ( " +
                "      SELECT dkk.maLopHP FROM dangki_ketqua dkk WHERE dkk.maSV = ? " +
                "  )";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, Integer.parseInt(hocKy));
            pstmt.setString(2, namHoc);
            pstmt.setString(3, maSV);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                courses.add(new AvailableCourse(
                        rs.getString("maMon"),
                        rs.getString("tenMon"),
                        rs.getString("maLopHP"),
                        rs.getString("tenLopHP"),
                        rs.getInt("soTinChi"),
                        rs.getString("namHoc")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return courses;
    }

    // (Hàm registerCourse giữ nguyên)
    public boolean registerCourse(String maSV, String maLopHP) {
        String queryInsert = "INSERT INTO dangki_ketqua (maSV, maLopHP, trangThaiHoc) VALUES (?, ?, 'Đang học')";
        String queryUpdate = "UPDATE lophocphan SET siSoHienTai = siSoHienTai + 1 WHERE maLopHP = ?";

        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtInsert = conn.prepareStatement(queryInsert)) {
                pstmtInsert.setString(1, maSV);
                pstmtInsert.setString(2, maLopHP);
                pstmtInsert.executeUpdate();
            }

            try (PreparedStatement pstmtUpdate = conn.prepareStatement(queryUpdate)) {
                pstmtUpdate.setString(1, maLopHP);
                pstmtUpdate.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
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