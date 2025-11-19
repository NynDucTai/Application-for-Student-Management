package com.example.doanlaptrinhhuongdoituong.dao.Teacher;

import com.example.doanlaptrinhhuongdoituong.model.Teacher.LopPhanCong;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil; // Giả sử đây là lớp kết nối DB của bạn
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherHomeDAO {

    // 1. Lấy tên giảng viên (từ bảng giangvien, cột hoTen)
    public String getTenGiangVien(String maGV) {
        String query = "SELECT hoTen FROM giangvien WHERE maGV = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, maGV);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("hoTen");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Giảng viên"; // Trả về mặc định nếu lỗi
    }

    // 2. Lấy danh sách kì học (từ lophocphan, cột hocKy)
    public ObservableList<String> getDanhSachKiHoc() {
        ObservableList<String> dsKiHoc = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT hocKy FROM lophocphan ORDER BY hocKy";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                dsKiHoc.add(String.valueOf(rs.getInt("hocKy"))); // Chuyển int sang String
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKiHoc;
    }

    // 3. Lấy danh sách năm học (từ lophocphan, cột namHoc)
    public ObservableList<String> getDanhSachNamHoc() {
        ObservableList<String> dsNamHoc = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT namHoc FROM lophocphan ORDER BY namHoc DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                dsNamHoc.add(rs.getString("namHoc"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsNamHoc;
    }

    // 4. Lấy danh sách Lớp được phân công (JOIN 3 bảng)
    public ObservableList<LopPhanCong> getLopPhanCong(String maGV, String kiHoc, String namHoc) {
        ObservableList<LopPhanCong> dsLop = FXCollections.observableArrayList();

        // Sử dụng tên bảng/cột từ database của bạn
        String query = "SELECT mh.maMon, mh.tenMon, lhp.maLopHP, mh.soTinChi, lhp.siSoHienTai, lhp.hocKy, lhp.namHoc " +
                "FROM phanconggiangday pcg " +
                "JOIN lophocphan lhp ON pcg.maLopHP = lhp.maLopHP " +
                "JOIN monhoc mh ON lhp.maMon = mh.maMon " +
                "WHERE pcg.maGV = ? AND lhp.hocKy = ? AND lhp.namHoc = ? " +
                "ORDER BY lhp.namHoc, lhp.hocKy, mh.tenMon";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, maGV);
            pstmt.setInt(2, Integer.parseInt(kiHoc)); // Cột hocKy của bạn là INT
            pstmt.setString(3, namHoc);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                dsLop.add(new LopPhanCong(
                        rs.getString("maMon"),
                        rs.getString("tenMon"),
                        rs.getString("maLopHP"),
                        rs.getInt("soTinChi"),
                        rs.getInt("siSoHienTai"), // Dùng siSoHienTai
                        String.valueOf(rs.getInt("hocKy")), // Chuyển hocKy (INT) sang String
                        rs.getString("namHoc")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsLop;
    }
}