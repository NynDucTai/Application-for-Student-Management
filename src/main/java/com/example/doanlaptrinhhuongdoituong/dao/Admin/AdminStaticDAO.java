package com.example.doanlaptrinhhuongdoituong.dao.Admin;

import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminStaticModel;
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
 * Chứa các hàm CSDL cho chức năng Thống Kê.
 */
public class AdminStaticDAO {

    /**
     * Lấy danh sách MÃ KHOA để đổ vào ComboBox.
     */
    public static ObservableList<String> getMaKhoaList() {
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

    /**
     * Lấy danh sách KỲ HỌC để đổ vào ComboBox.
     */
    public static ObservableList<Integer> getHocKyList() {
        // Giả sử có 3 học kỳ (1, 2, 3)
        return FXCollections.observableArrayList(1, 2, 3);
    }

    /**
     * Lấy danh sách thống kê các lớp (NV hoặc HL)
     * Kèm theo số lượng sinh viên đã đăng ký
     */
    public static ObservableList<AdminStaticModel> getStaticClasses(String loaiLop, String maKhoa, int hocKy) {
        ObservableList<AdminStaticModel> classList = FXCollections.observableArrayList();

        // Câu SQL này đếm số SV đăng ký (dk.maSV) cho mỗi Lớp (l.maLopHP)
        String sql = "SELECT " +
                "    l.maLopHP AS maLopHP, " +
                "    l.tenLopHP AS tenLopHP, " + // Giả sử bạn có cột 'tenLopHP'
                "    m.tenMon AS tenMonHoc, " +
                "    l.siSoToiThieu AS siSoToiThieu, " +
                "    COUNT(dk.maSV) AS siSoHienTai " + // Đếm số SV
                "FROM LopHocPhan l " +
                "JOIN MonHoc m ON l.maMon = m.maMon " +
                "LEFT JOIN DangKi_KetQua dk ON l.maLopHP = dk.maLopHP " +
                "WHERE l.loaiLop = ? AND m.maKhoa = ? AND l.hocKy = ? " +
                "GROUP BY l.maLopHP, l.tenLopHP, m.tenMon, l.siSoToiThieu";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loaiLop);
            stmt.setString(2, maKhoa);
            stmt.setInt(3, hocKy);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AdminStaticModel lop = new AdminStaticModel();
                    lop.setMaLopHP(rs.getString("maLopHP"));
                    lop.setTenLopHP(rs.getString("tenLopHP"));
                    lop.setTenMonHoc(rs.getString("tenMonHoc"));
                    lop.setSiSoToiThieu(rs.getInt("siSoToiThieu"));
                    lop.setSiSoHienTai(rs.getInt("siSoHienTai"));

                    classList.add(lop);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách thống kê: " + e.getMessage());
        }
        return classList;
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