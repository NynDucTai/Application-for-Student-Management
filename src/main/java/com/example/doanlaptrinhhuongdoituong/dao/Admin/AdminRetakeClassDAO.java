package com.example.doanlaptrinhhuongdoituong.dao.Admin;

// BỔ SUNG IMPORT NÀY
import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminSubjectManageModel;
import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminRetakeClassModel;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
// Bỏ: import java.util.HashMap;
// Bỏ: import java.util.Map;

/**
 * Lớp DAO (Data Access Object)
 * Chứa các hàm CSDL cho chức năng Mở Lớp Học Lại. (ĐÃ SỬA LỖI)
 */
public class AdminRetakeClassDAO {

    /**
     * THAY ĐỔI: Lấy danh sách đối tượng Môn học (thay vì Map)
     * (Sử dụng model 'AdminSubjectManageModel' vì nó đã định nghĩa Môn học)
     */
    public static ObservableList<AdminSubjectManageModel> getMonHocList() {
        ObservableList<AdminSubjectManageModel> monHocList = FXCollections.observableArrayList();
        String sql = "SELECT maMon, tenMon, soTinChi FROM MonHoc";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                AdminSubjectManageModel mon = new AdminSubjectManageModel();
                mon.setMaMon(rs.getString("maMon"));
                mon.setTenMon(rs.getString("tenMon"));
                mon.setSoTinChi(rs.getInt("soTinChi"));
                monHocList.add(mon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monHocList;
    }

    /**
     * Lấy tất cả Lớp Học Lại (loaiLop = 'HOC_LAI') từ CSDL.
     */
    public static ObservableList<AdminRetakeClassModel> getRetakeClasses() {
        ObservableList<AdminRetakeClassModel> classList = FXCollections.observableArrayList();

        String sql = "SELECT l.*, m.tenMon, m.soTinChi " +
                "FROM LopHocPhan l " +
                "JOIN MonHoc m ON l.maMon = m.maMon " +
                "WHERE l.loaiLop = 'HOC_LAI' AND l.trangThai = 'DANG_CHO'";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (conn == null) {
                System.err.println("getRetakeClasses: Kết nối CSDL thất bại.");
                return classList;
            }

            while (rs.next()) {
                AdminRetakeClassModel lop = new AdminRetakeClassModel();
                lop.setMaLopHP(rs.getString("maLopHP"));
                lop.setMaMon(rs.getString("maMon"));
                lop.setTenMon(rs.getString("tenMon"));
                lop.setHocKy(rs.getInt("hocKy"));
                lop.setNamHoc(rs.getString("namHoc"));
                lop.setSoTinChi(rs.getInt("soTinChi"));
                lop.setSiSoToiThieu(rs.getInt("siSoToiThieu"));
                lop.setSiSoToiDa(rs.getInt("siSoToiDa"));
                lop.setSiSoHienTai(rs.getInt("siSoHienTai"));

                Date sqlDate = rs.getDate("hanDangKi");
                lop.setHanDangKi((sqlDate != null) ? sqlDate.toLocalDate() : null);
                lop.setTrangThai(rs.getString("trangThai"));

                classList.add(lop);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách lớp học lại: " + e.getMessage());
        }
        return classList;
    }

    /**
     * Thêm một Lớp Học Lại mới vào bảng LopHocPhan.
     */
    public static boolean addRetakeClass(AdminRetakeClassModel lop) {
        // SỬA SQL: Xóa "soTinChi" khỏi câu INSERT
        // (Vì 'soTinChi' không thuộc bảng 'LopHocPhan')
        String sql = "INSERT INTO LopHocPhan (maLopHP, maMon, hocKy, namHoc, " +
                "siSoToiThieu, siSoToiDa, hanDangKi, trangThai, loaiLop, siSoHienTai) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 'DANG_CHO', 'HOC_LAI', 0)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");

            stmt.setString(1, lop.getMaLopHP());
            stmt.setString(2, lop.getMaMon());
            stmt.setInt(3, lop.getHocKy());
            stmt.setString(4, lop.getNamHoc());
            // BỎ: stmt.setInt(5, lop.getSoTinChi());
            stmt.setInt(5, lop.getSiSoToiThieu()); // Sửa thành 5
            stmt.setInt(6, lop.getSiSoToiDa()); // Sửa thành 6
            stmt.setDate(7, lop.getHanDangKi() != null ? Date.valueOf(lop.getHanDangKi()) : null); // Sửa thành 7

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tạo lớp: " + e.getMessage());
            return false;
        }
    }

    // ... (Hàm deleteRetakeClass và openClass giữ nguyên) ...

    // (Bản sao 2 hàm delete và open từ code trước)
    public static boolean deleteRetakeClass(String maLopHP) {
        Connection conn = null;
        String sqlDangKi = "DELETE FROM DangKi_KetQua WHERE maLopHP = ?";
        String sqlLopHP = "DELETE FROM LopHocPhan WHERE maLopHP = ?";

        try {
            conn = DatabaseUtil.getConnection();
            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(sqlDangKi)) {
                stmt.setString(1, maLopHP);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(sqlLopHP)) {
                stmt.setString(1, maLopHP);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Không tìm thấy lớp để xóa.");
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể xóa lớp: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static boolean openClass(String maLopHP) {
        String sql = "UPDATE LopHocPhan SET trangThai = 'CHINH_THUC' WHERE maLopHP = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");
            stmt.setString(1, maLopHP);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể mở lớp: " + e.getMessage());
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