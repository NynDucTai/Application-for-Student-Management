package com.example.doanlaptrinhhuongdoituong.dao.Admin;

import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminClassVerifyModel;
import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminSubjectManageModel;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

/**
 * Lớp DAO (Data Access Object)
 * Chứa các hàm CSDL cho chức năng Mở Lớp Nguyện Vọng.
 */
public class AdminClassVerifyDAO {

    /**
     * Lấy danh sách Môn học (để dùng cho ComboBox)
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
     * Lấy tất cả Lớp Nguyện Vọng (loaiLop = 'NGUYEN_VONG')
     * và đang ở trạng thái "CHƯA DUYỆT"
     */
    public static ObservableList<AdminClassVerifyModel> getVerifyClasses() {
        ObservableList<AdminClassVerifyModel> classList = FXCollections.observableArrayList();

        // Chỉ lấy các lớp NGUYEN_VONG và DANG_CHO
        String sql = "SELECT l.*, m.tenMon, m.soTinChi " +
                "FROM LopHocPhan l " +
                "JOIN MonHoc m ON l.maMon = m.maMon " +
                "WHERE l.loaiLop = 'NGUYEN_VONG' AND l.trangThai = 'DANG_CHO'";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (conn == null) {
                System.err.println("getVerifyClasses: Kết nối CSDL thất bại.");
                return classList;
            }

            while (rs.next()) {
                AdminClassVerifyModel lop = new AdminClassVerifyModel();
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
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách lớp nguyện vọng: " + e.getMessage());
        }
        return classList;
    }

    /**
     * Thêm một Lớp Nguyện Vọng mới vào bảng LopHocPhan.
     */
    public static boolean addVerifyClass(AdminClassVerifyModel lop) {
        // Thêm lớp với loaiLop = 'NGUYEN_VONG'
        String sql = "INSERT INTO LopHocPhan (maLopHP, maMon, hocKy, namHoc, " +
                "siSoToiThieu, siSoToiDa, hanDangKi, trangThai, loaiLop, siSoHienTai) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 'DANG_CHO', 'NGUYEN_VONG', 0)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");

            stmt.setString(1, lop.getMaLopHP());
            stmt.setString(2, lop.getMaMon());
            stmt.setInt(3, lop.getHocKy());
            stmt.setString(4, lop.getNamHoc());
            stmt.setInt(5, lop.getSiSoToiThieu());
            stmt.setInt(6, lop.getSiSoToiDa());
            stmt.setDate(7, lop.getHanDangKi() != null ? Date.valueOf(lop.getHanDangKi()) : null);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tạo lớp: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa một Lớp Nguyện Vọng.
     * Cần xóa các đăng ký liên quan trong DangKi_KetQua trước.
     */
    public static boolean deleteVerifyClass(String maLopHP) {
        Connection conn = null;
        String sqlDangKi = "DELETE FROM DangKi_KetQua WHERE maLopHP = ?";
        String sqlLopHP = "DELETE FROM LopHocPhan WHERE maLopHP = ?";

        try {
            conn = DatabaseUtil.getConnection();
            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");

            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Xóa các đăng ký (bảng con)
            try (PreparedStatement stmt = conn.prepareStatement(sqlDangKi)) {
                stmt.setString(1, maLopHP);
                stmt.executeUpdate();
            }

            // 2. Xóa lớp học phần (bảng cha)
            try (PreparedStatement stmt = conn.prepareStatement(sqlLopHP)) {
                stmt.setString(1, maLopHP);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Không tìm thấy lớp để xóa.");
                }
            }

            conn.commit(); // Hoàn tất Transaction
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

    /**
     * Mở lớp chính thức (thay đổi trạng thái).
     */
    public static boolean openClass(String maLopHP) {
        // Hàm này y hệt AdminRetakeClassDAO
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