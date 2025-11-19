package com.example.doanlaptrinhhuongdoituong.dao.Student;

import com.example.doanlaptrinhhuongdoituong.model.Student.StudentScoreRecord;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentScoreDAO {

    // 1. Lấy danh sách HỌC KỲ (ví dụ: 1, 2)
    public ObservableList<String> getHocKy(String maSV) {
        ObservableList<String> hocKyList = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT lhp.hocKy " +
                "FROM dangki_ketqua dkk " +
                "JOIN lophocphan lhp ON dkk.maLopHP = lhp.maLopHP " +
                "WHERE dkk.maSV = ? ORDER BY lhp.hocKy";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, maSV);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                hocKyList.add(String.valueOf(rs.getInt("hocKy")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return hocKyList;
    }

    // 2. Lấy danh sách NĂM HỌC (ví dụ: 2024-2025)
    public ObservableList<String> getNamHoc(String maSV) {
        ObservableList<String> namHocList = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT lhp.namHoc " +
                "FROM dangki_ketqua dkk " +
                "JOIN lophocphan lhp ON dkk.maLopHP = lhp.maLopHP " +
                "WHERE dkk.maSV = ? ORDER BY lhp.namHoc DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, maSV);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                namHocList.add(rs.getString("namHoc"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return namHocList;
    }

    // 3. Lấy bảng điểm chi tiết
    public ObservableList<StudentScoreRecord> getScores(String maSV, String hocKy, String namHoc) {
        ObservableList<StudentScoreRecord> scores = FXCollections.observableArrayList();
        String query = "SELECT mh.maMon, mh.tenMon, lhp.maLopHP, mh.soTinChi, " +
                "dkk.diemQuaTrinh, dkk.diemKetThuc, dkk.diemHe4, dkk.diemChu, dkk.trangThaiHoc " +
                "FROM dangki_ketqua dkk " +
                "JOIN lophocphan lhp ON dkk.maLopHP = lhp.maLopHP " +
                "JOIN monhoc mh ON lhp.maMon = mh.maMon " +
                "WHERE dkk.maSV = ? AND lhp.hocKy = ? AND lhp.namHoc = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, maSV);
            pstmt.setInt(2, Integer.parseInt(hocKy)); // hocKy là INT trong DB
            pstmt.setString(3, namHoc);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                scores.add(new StudentScoreRecord(
                        rs.getString("maMon"),
                        rs.getString("tenMon"),
                        rs.getString("maLopHP"),
                        rs.getInt("soTinChi"),
                        (Float) rs.getObject("diemQuaTrinh"), // Lấy Float (có thể null)
                        (Float) rs.getObject("diemKetThuc"), // Lấy Float (có thể null)
                        (Float) rs.getObject("diemHe4"),
                        rs.getString("diemChu"),
                        rs.getString("trangThaiHoc")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return scores;
    }
}