package com.example.doanlaptrinhhuongdoituong.dao.Student;

import com.example.doanlaptrinhhuongdoituong.model.Student.StudentInfo;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentInfoDAO {

    // Lấy thông tin chi tiết của sinh viên
    public StudentInfo getStudentInfo(String maSV) {
        // Câu query này JOIN 2 bảng sinhvien và khoa
        String query = "SELECT sv.hoTen, sv.ngaySinh, sv.gioiTinh, sv.queQuan, sv.lop, k.tenKhoa " +
                "FROM sinhvien sv " +
                "JOIN khoa k ON sv.maKhoa = k.maKhoa " +
                "WHERE sv.maSV = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, maSV);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Trả về đối tượng StudentInfo
                return new StudentInfo(
                        maSV,
                        rs.getString("hoTen"),
                        rs.getString("gioiTinh"),
                        rs.getString("tenKhoa"), // Ngành
                        rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                        rs.getString("lop"), // Lớp
                        rs.getString("queQuan") // Nơi sinh
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy
    }
}