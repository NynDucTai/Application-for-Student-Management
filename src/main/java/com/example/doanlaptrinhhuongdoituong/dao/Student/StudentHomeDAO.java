package com.example.doanlaptrinhhuongdoituong.dao.Student;

import com.example.doanlaptrinhhuongdoituong.model.Student.CourseRegistration;
import com.example.doanlaptrinhhuongdoituong.model.Student.Semester;
import com.example.doanlaptrinhhuongdoituong.model.Student.StudentInfo;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentHomeDAO {

    // (Hàm getStudentInfo giữ nguyên)
    public StudentInfo getStudentInfo(String maSV) {
        String query = "SELECT sv.hoTen, sv.ngaySinh, sv.gioiTinh, sv.queQuan, sv.lop, k.tenKhoa " +
                "FROM sinhvien sv " +
                "JOIN khoa k ON sv.maKhoa = k.maKhoa " +
                "WHERE sv.maSV = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, maSV);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new StudentInfo(
                        maSV,
                        rs.getString("hoTen"),
                        rs.getString("gioiTinh"),
                        rs.getString("tenKhoa"),
                        rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                        rs.getString("lop"),
                        rs.getString("queQuan")
                );
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // (Hàm getSemesters giữ nguyên)
    public ObservableList<Semester> getSemesters(String maSV) {
        ObservableList<Semester> semesters = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT lhp.hocKy, lhp.namHoc " +
                "FROM dangki_ketqua dkk " +
                "JOIN lophocphan lhp ON dkk.maLopHP = lhp.maLopHP " +
                "WHERE dkk.maSV = ? " +
                "ORDER BY lhp.namHoc DESC, lhp.hocKy DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, maSV);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                semesters.add(new Semester(rs.getInt("hocKy"), rs.getString("namHoc")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return semesters;
    }

    //
    // =========================================================================
    // *** PHẦN SỬA LỖI NẰM TẠI ĐÂY (HÀM getCoursesAndScores) ***
    // =========================================================================
    public ObservableList<CourseRegistration> getCoursesAndScores(String maSV, int hocKy, String namHoc) {
        ObservableList<CourseRegistration> courses = FXCollections.observableArrayList();

        // Sửa query: Lấy thêm diemQuaTrinh và diemKetThuc
        String query = "SELECT mh.tenMon, mh.soTinChi, dkk.diemHe4, dkk.diemQuaTrinh, dkk.diemKetThuc " +
                "FROM dangki_ketqua dkk " +
                "JOIN lophocphan lhp ON dkk.maLopHP = lhp.maLopHP " +
                "JOIN monhoc mh ON lhp.maMon = mh.maMon " +
                "WHERE dkk.maSV = ? AND lhp.hocKy = ? AND lhp.namHoc = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, maSV);
            pstmt.setInt(2, hocKy);
            pstmt.setString(3, namHoc);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Float diemQT = (Float) rs.getObject("diemQuaTrinh");
                Float diemKT = (Float) rs.getObject("diemKetThuc");
                Float diemHe4 = (Float) rs.getObject("diemHe4");

                // Tính toán diemTK (hệ 10) theo công thức (QT+KT)/2
                Float diemTK = null;
                if (diemQT != null && diemKT != null) {
                    diemTK = (diemQT + diemKT) / 2.0f;
                }

                courses.add(new CourseRegistration(
                        rs.getString("tenMon"),
                        rs.getInt("soTinChi"),
                        diemHe4,
                        diemTK // Truyền điểm hệ 10 mới vào
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return courses;
    }
}