package com.example.doanlaptrinhhuongdoituong.dao.Admin;

import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminTeacherManageModel;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Lớp DAO (Data Access Object) cho Giảng Viên
 * (Đã cập nhật để lấy Username/Password)
 */
public class AdminTeacherManageDAO {

    // (Hàm getMaKhoaList giữ nguyên)
    public static ObservableList<String> getMaKhoaList() {
        ObservableList<String> khoaList = FXCollections.observableArrayList();
        String sql = "SELECT maKhoa FROM Khoa";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                khoaList.add(rs.getString("maKhoa"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return khoaList;
    }

    /**
     * Lấy danh sách Giảng viên (ĐÃ CẬP NHẬT SQL)
     * Kèm theo số lớp, tên khoa, và thông tin USER
     */
    public static ObservableList<AdminTeacherManageModel> getTeachers() {
        ObservableList<AdminTeacherManageModel> teacherList = FXCollections.observableArrayList();

        // SỬA SQL: LEFT JOIN bảng USER và lấy username, password
        String sql = "SELECT " +
                "    g.maGV AS maGV, " +
                "    g.hoTen AS hoTen, " +
                "    g.email AS email, " +
                "    g.maKhoa AS maKhoa, " +
                "    k.tenKhoa AS tenKhoa, " +
                "    u.username AS username, " +  // <-- DÒNG MỚI
                "    u.password AS password, " +  // <-- DÒNG MỚI
                "    COUNT(DISTINCT pcgd.maGiangDay) AS soLopGiangDay " +
                "FROM GiangVien g " +
                "JOIN Khoa k ON g.maKhoa = k.maKhoa " +
                "LEFT JOIN USER u ON g.maGV = u.maGV " + // <-- DÒNG MỚI
                "LEFT JOIN PhanCongGiangDay pcgd ON g.maGV = pcgd.maGV " +
                "GROUP BY g.maGV, g.hoTen, g.email, g.maKhoa, k.tenKhoa, u.username, u.password"; // <-- BỔ SUNG

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (conn == null) {
                System.err.println("getTeachers: Kết nối CSDL thất bại.");
                return teacherList;
            }

            while (rs.next()) {
                AdminTeacherManageModel teacher = new AdminTeacherManageModel();
                teacher.setMaGV(rs.getString("maGV"));
                teacher.setHoTen(rs.getString("hoTen"));
                teacher.setEmail(rs.getString("email"));
                teacher.setMaKhoa(rs.getString("maKhoa"));
                teacher.setTenKhoa(rs.getString("tenKhoa"));
                teacher.setSoLopGiangDay(rs.getInt("soLopGiangDay"));
                teacher.setUsername(rs.getString("username")); // <-- DÒNG MỚI
                teacher.setPassword(rs.getString("password")); // <-- DÒNG MỚI

                teacherList.add(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách giảng viên: " + e.getMessage());
        }
        return teacherList;
    }

    /**
     * Thêm giảng viên mới (bao gồm cả tạo USER).
     * (Hàm này đã đúng, nhận username/password từ Controller)
     */
    public static boolean addTeacher(AdminTeacherManageModel teacher, String username, String password) {
        Connection conn = null;
        String sqlGiangVien = "INSERT INTO GiangVien (maGV, hoTen, email, maKhoa) VALUES (?, ?, ?, ?)";
        String sqlUser = "INSERT INTO USER (username, password, maGV) VALUES (?, ?, ?)";

        try {
            conn = DatabaseUtil.getConnection();
            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");

            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Thêm vào bảng GiangVien
            try (PreparedStatement stmtGV = conn.prepareStatement(sqlGiangVien)) {
                stmtGV.setString(1, teacher.getMaGV());
                stmtGV.setString(2, teacher.getHoTen());
                stmtGV.setString(3, teacher.getEmail());
                stmtGV.setString(4, teacher.getMaKhoa());
                stmtGV.executeUpdate();
            }

            // 2. Thêm vào bảng USER (dùng username/password từ textfield)
            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser)) {
                stmtUser.setString(1, username);
                stmtUser.setString(2, password); // CẢNH BÁO: Mật khẩu nên được băm!
                stmtUser.setString(3, teacher.getMaGV());
                stmtUser.executeUpdate();
            }

            conn.commit(); // Hoàn tất Transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể thêm giảng viên: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * Xóa giảng viên (bao gồm cả USER và các Phân công).
     * (Hàm này đã đúng, không cần sửa)
     */
    public static boolean deleteTeacher(String maGV) {
        Connection conn = null;
        String sqlPhanCong = "DELETE FROM PhanCongGiangDay WHERE maGV = ?";
        String sqlUser = "DELETE FROM USER WHERE maGV = ?";
        String sqlGiangVien = "DELETE FROM GiangVien WHERE maGV = ?";

        try {
            conn = DatabaseUtil.getConnection();
            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");

            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Xóa ở bảng PhanCongGiangDay trước
            try (PreparedStatement stmt = conn.prepareStatement(sqlPhanCong)) {
                stmt.setString(1, maGV);
                stmt.executeUpdate();
            }

            // 2. Xóa ở bảng USER
            try (PreparedStatement stmt = conn.prepareStatement(sqlUser)) {
                stmt.setString(1, maGV);
                stmt.executeUpdate();
            }

            // 3. Xóa ở bảng GiangVien sau cùng
            try (PreparedStatement stmt = conn.prepareStatement(sqlGiangVien)) {
                stmt.setString(1, maGV);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Không tìm thấy giảng viên để xóa.");
                }
            }

            conn.commit(); // Hoàn tất Transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể xóa giảng viên: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
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