package com.example.doanlaptrinhhuongdoituong.dao.Admin;

import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminSubjectManageModel;
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
 * Chứa tất cả các hàm truy vấn CSDL liên quan đến Môn Học.
 */
public class AdminSubjectManageDAO {

    /**
     * Lấy danh sách MÃ KHOA để đổ vào ComboBox.
     * (Hàm này giống hệt hàm trong AdminStuManageDAO)
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return khoaList;
    }

    /**
     * Lấy tất cả môn học từ CSDL.
     * Dùng JOIN để lấy Tên Khoa, và dùng ALIAS (AS) để tránh mọi lỗi "Column not found".
     */
    public static ObservableList<AdminSubjectManageModel> getSubjects() {
        ObservableList<AdminSubjectManageModel> subjectList = FXCollections.observableArrayList();

        String sql = "SELECT " +
                "    m.maMon AS maMon, " +
                "    m.tenMon AS tenMon, " +
                "    m.maKhoa AS maKhoa, " +
                "    k.tenKhoa AS tenKhoa, " +
                "    m.soTinChi AS soTinChi, " +
                "    m.hocKy AS hocKi, " +
                "    m.namHoc AS namHoc, " +
                "    m.loaiMon AS loaiMon " +
                "FROM MonHoc m " +
                "JOIN Khoa k ON m.maKhoa = k.maKhoa";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (conn == null) { /* ... (lỗi) ... */ }

            while (rs.next()) {
                AdminSubjectManageModel subject = new AdminSubjectManageModel();
                subject.setMaMon(rs.getString("maMon"));
                subject.setTenMon(rs.getString("tenMon"));
                subject.setMaKhoa(rs.getString("maKhoa"));
                subject.setTenKhoa(rs.getString("tenKhoa"));
                subject.setSoTinChi(rs.getInt("soTinChi"));
                subject.setHocKi(rs.getInt("hocKi"));
                subject.setNamHoc(rs.getString("namHoc"));
                subject.setLoaiMon(rs.getString("loaiMon"));

                subjectList.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách môn học: " + e.getMessage());
        }
        return subjectList;
    }

    /**
     * Thêm một môn học mới vào CSDL.
     */
    public static boolean addSubject(AdminSubjectManageModel subject) {
        // Cột 'monTienQuyet' của bạn đang là NULL, ta sẽ bỏ qua
        String sql = "INSERT INTO MonHoc (maMon, tenMon, maKhoa, soTinChi, hocKy, namHoc, loaiMon) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");

            stmt.setString(1, subject.getMaMon());
            stmt.setString(2, subject.getTenMon());
            stmt.setString(3, subject.getMaKhoa());
            stmt.setInt(4, subject.getSoTinChi());
            stmt.setInt(5, subject.getHocKi());
            stmt.setString(6, subject.getNamHoc());
            stmt.setString(7, subject.getLoaiMon());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể thêm môn học: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa một môn học khỏi CSDL.
     */
    public static boolean deleteSubject(String maMon) {
        String sql = "DELETE FROM MonHoc WHERE maMon = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");

            stmt.setString(1, maMon);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                showAlert(Alert.AlertType.ERROR, "Lỗi Xóa", "Không tìm thấy môn học để xóa.");
                return false;
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            // Khắc phục lỗi FOREIGN KEY:
            // Bắt lỗi constraint và thông báo cho người dùng một cách thân thiện
            if (e.getErrorCode() == 1451) { // 1451 là mã lỗi "FOREIGN KEY constraint fails" của MySQL
                showAlert(Alert.AlertType.ERROR, "Lỗi Ràng Buộc",
                        "Không thể xóa môn học này! Đã có Lớp Học Phần hoặc Môn Tiên Quyết tham chiếu đến nó.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể xóa môn học: " + e.getMessage());
            }
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