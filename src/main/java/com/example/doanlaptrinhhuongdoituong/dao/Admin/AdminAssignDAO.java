package com.example.doanlaptrinhhuongdoituong.dao.Admin;

import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminAssignModel;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Lớp DAO (Data Access Object)
 * Chứa tất cả các hàm truy vấn CSDL liên quan đến PHÂN CÔNG GIẢNG DẠY.
 */
public class AdminAssignDAO {

    // --- CÁC HÀM LẤY DỮ LIỆU CHO COMBOBOX ---

    public static ObservableList<String> getMaKhoaList() {
        // (Tái sử dụng hàm từ các DAO trước)
        ObservableList<String> khoaList = FXCollections.observableArrayList();
        String sql = "SELECT maKhoa FROM Khoa";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                khoaList.add(rs.getString("maKhoa"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return khoaList;
    }

    public static ObservableList<Integer> getHocKyList() {
        // Giả sử có 3 học kỳ (1, 2, 3)
        return FXCollections.observableArrayList(1, 2, 3);
    }

    public static ObservableList<String> getNamHocList() {
        // Tạm thời, bạn có thể thay bằng truy vấn CSDL
        return FXCollections.observableArrayList("2024-2025", "2025-2026", "2026-2027");
    }

    // --- CÁC HÀM LẤY DỮ LIỆU CHO 3 BẢNG ---

    /**
     * Lấy các Lớp Học Phần (LHP) CHƯA được phân công
     */
    public static ObservableList<AdminAssignModel> getLopHocPhanChuaPhanCong(String maKhoa, int hocKy, String namHoc) {
        ObservableList<AdminAssignModel> lopList = FXCollections.observableArrayList();
        // SQL: Lấy LHP thuộc Khoa, Kỳ, Năm HỌC...
        // ...VÀ maLopHP KHÔNG CÓ trong bảng PhanCongGiangDay
        String sql = "SELECT m.maMon, m.tenMon, l.maLopHP, m.soTinChi " +
                "FROM LopHocPhan l " +
                "JOIN MonHoc m ON l.maMon = m.maMon " +
                "WHERE m.maKhoa = ? AND l.hocKy = ? AND l.namHoc = ? " +
                "AND l.maLopHP NOT IN (SELECT DISTINCT maLopHP FROM PhanCongGiangDay)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maKhoa);
            stmt.setInt(2, hocKy);
            stmt.setString(3, namHoc);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AdminAssignModel lop = new AdminAssignModel();
                    lop.setMaMon(rs.getString("maMon"));
                    lop.setTenMon(rs.getString("tenMon"));
                    lop.setMaLopHP(rs.getString("maLopHP"));
                    lop.setSoTinChi(rs.getInt("soTinChi"));
                    lopList.add(lop);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải Lớp Học Phần: " + e.getMessage());
        }
        return lopList;
    }

    /**
     * Lấy Giảng Viên (GV) thuộc một Khoa
     */
    public static ObservableList<AdminAssignModel> getGiangVienByKhoa(String maKhoa) {
        ObservableList<AdminAssignModel> gvList = FXCollections.observableArrayList();
        String sql = "SELECT maGV, hoTen, email FROM GiangVien WHERE maKhoa = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maKhoa);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AdminAssignModel gv = new AdminAssignModel();
                    gv.setMaGV(rs.getString("maGV"));
                    gv.setHoTenGV(rs.getString("hoTen"));
                    gv.setEmailGV(rs.getString("email"));
                    gvList.add(gv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải Giảng Viên: " + e.getMessage());
        }
        return gvList;
    }

    /**
     * Lấy các LHP ĐÃ ĐƯỢC phân công
     */
    public static ObservableList<AdminAssignModel> getPhanCongHienTai(String maKhoa, int hocKy, String namHoc) {
        ObservableList<AdminAssignModel> phanCongList = FXCollections.observableArrayList();
        String sql = "SELECT l.maLopHP, m.tenMon, m.soTinChi, g.hoTen " +
                "FROM PhanCongGiangDay p " +
                "JOIN LopHocPhan l ON p.maLopHP = l.maLopHP " +
                "JOIN MonHoc m ON l.maMon = m.maMon " +
                "JOIN GiangVien g ON p.maGV = g.maGV " +
                "WHERE m.maKhoa = ? AND l.hocKy = ? AND l.namHoc = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maKhoa);
            stmt.setInt(2, hocKy);
            stmt.setString(3, namHoc);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AdminAssignModel pc = new AdminAssignModel();
                    pc.setMaLopHP(rs.getString("maLopHP"));
                    pc.setTenMon(rs.getString("tenMon"));
                    pc.setSoTinChi(rs.getInt("soTinChi"));
                    pc.setHoTenGV(rs.getString("hoTen")); // Tên GV
                    phanCongList.add(pc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải Phân Công: " + e.getMessage());
        }
        return phanCongList;
    }

    // --- HÀM THỰC THI PHÂN CÔNG ---

    /**
     * Thêm 1 dòng vào bảng PhanCongGiangDay
     */
    public static boolean assignGiangVien(String maGV, String maLopHP) {
        String sql = "INSERT INTO PhanCongGiangDay (maGV, maLopHP, trangThai) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");

            stmt.setString(1, maGV);
            stmt.setString(2, maLopHP);
            stmt.setString(3, "Đã phân công"); // Trạng thái mặc định

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể phân công: " + e.getMessage());
            return false;
        }
    }

    // Hàm tiện ích
    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}