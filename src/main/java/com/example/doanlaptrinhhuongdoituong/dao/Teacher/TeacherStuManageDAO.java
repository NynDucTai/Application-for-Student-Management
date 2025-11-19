package com.example.doanlaptrinhhuongdoituong.dao.Teacher;

import com.example.doanlaptrinhhuongdoituong.model.Teacher.LopDayHoc;
import com.example.doanlaptrinhhuongdoituong.model.Teacher.SinhVienTrongLop;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherStuManageDAO {

    /**
     * Lấy các lớp mà giảng viên (maGV) đang dạy
     */
    public ObservableList<LopDayHoc> getLopGiangDay(String maGV) {
        ObservableList<LopDayHoc> dsLop = FXCollections.observableArrayList();
        String query = "SELECT lhp.maLopHP, mh.tenMon, lhp.tenLopHP " +
                "FROM phanconggiangday pcg " +
                "JOIN lophocphan lhp ON pcg.maLopHP = lhp.maLopHP " +
                "JOIN monhoc mh ON lhp.maMon = mh.maMon " +
                "WHERE pcg.maGV = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, maGV);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                dsLop.add(new LopDayHoc(
                        rs.getString("maLopHP"),
                        rs.getString("tenMon"),
                        rs.getString("tenLopHP")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsLop;
    }

    /**
     * Lấy sinh viên trong lớp (maLopHP) có tìm kiếm
     */
    public ObservableList<SinhVienTrongLop> getSinhVienTrongLop(String maLopHP, String searchText) {
        ObservableList<SinhVienTrongLop> dsSV = FXCollections.observableArrayList();
        String searchPattern = "%" + (searchText != null ? searchText : "") + "%";

        // Câu query này JOIN 3 bảng và sắp xếp theo yêu cầu tìm kiếm
        String query = "SELECT sv.maSV, sv.hoTen, sv.ngaySinh, sv.gioiTinh, sv.queQuan, sv.lop, k.tenKhoa " +
                "FROM dangki_ketqua dkk " +
                "JOIN sinhvien sv ON dkk.maSV = sv.maSV " +
                "JOIN khoa k ON sv.maKhoa = k.maKhoa " +
                "WHERE dkk.maLopHP = ? AND (sv.maSV LIKE ? OR sv.hoTen LIKE ?) " +
                "ORDER BY " +
                // Yêu cầu: "hiển thị lên đầu bảng"
                "  CASE WHEN (sv.maSV LIKE ? OR sv.hoTen LIKE ?) THEN 0 ELSE 1 END, " +
                "  sv.hoTen"; // Sắp xếp theo tên

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, maLopHP);
            pstmt.setString(2, searchPattern); // Cho điều kiện WHERE
            pstmt.setString(3, searchPattern); // Cho điều kiện WHERE

            // Tham số cho ORDER BY
            pstmt.setString(4, searchPattern); // Cho điều kiện CASE
            pstmt.setString(5, searchPattern); // Cho điều kiện CASE

            ResultSet rs = pstmt.executeQuery();
            int stt = 1;
            while (rs.next()) {
                dsSV.add(new SinhVienTrongLop(
                        stt++,
                        rs.getString("maSV"),
                        rs.getString("hoTen"),
                        rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null,
                        rs.getString("queQuan"),
                        rs.getString("gioiTinh"),
                        rs.getString("tenKhoa"),
                        rs.getString("lop")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsSV;
    }
}