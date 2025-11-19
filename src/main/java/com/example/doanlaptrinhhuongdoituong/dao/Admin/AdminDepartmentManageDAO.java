package com.example.doanlaptrinhhuongdoituong.dao.Admin;

import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminDepartmentManageModel;
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
 * Chứa tất cả các hàm truy vấn CSDL liên quan đến KHOA.
 */
public class AdminDepartmentManageDAO {

    /**
     * Lấy danh sách Khoa KÈM THEO SỐ LIỆU THỐNG KÊ (Đếm GV, SV, LHP).
     */
    public static ObservableList<AdminDepartmentManageModel> getDepartments() {
        ObservableList<AdminDepartmentManageModel> departmentList = FXCollections.observableArrayList();

        // Câu SQL phức tạp dùng LEFT JOIN và GROUP BY để đếm chính xác
        String sql = "SELECT " +
                "    k.maKhoa AS maKhoa, " +
                "    k.tenKhoa AS tenKhoa, " +
                "    COUNT(DISTINCT gv.maGV) AS soLuongGV, " +
                "    COUNT(DISTINCT sv.maSV) AS soLuongSV, " +
                "    COUNT(DISTINCT lhp.maLopHP) AS soLuongLHP " +
                "FROM Khoa k " +
                "LEFT JOIN GiangVien gv ON k.maKhoa = gv.maKhoa " +
                "LEFT JOIN SinhVien sv ON k.maKhoa = sv.maKhoa " +
                "LEFT JOIN MonHoc mh ON k.maKhoa = mh.maKhoa " +
                "LEFT JOIN LopHocPhan lhp ON mh.maMon = lhp.maMon " +
                "GROUP BY k.maKhoa, k.tenKhoa";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (conn == null) {
                System.err.println("getDepartments: Kết nối CSDL thất bại.");
                return departmentList;
            }

            while (rs.next()) {
                AdminDepartmentManageModel dept = new AdminDepartmentManageModel();
                dept.setMaKhoa(rs.getString("maKhoa"));
                dept.setTenKhoa(rs.getString("tenKhoa"));
                dept.setSoLuongGV(rs.getInt("soLuongGV"));
                dept.setSoLuongSV(rs.getInt("soLuongSV"));
                dept.setSoLuongLHP(rs.getInt("soLuongLHP"));

                departmentList.add(dept);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách khoa: " + e.getMessage());
        }
        return departmentList;
    }

    /**
     * Thêm một khoa mới vào CSDL.
     */
    public static boolean addDepartment(String maKhoa, String tenKhoa) {
        String sql = "INSERT INTO Khoa (maKhoa, tenKhoa) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");

            stmt.setString(1, maKhoa);
            stmt.setString(2, tenKhoa);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể thêm khoa: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa một khoa khỏi CSDL.
     * Sẽ thất bại nếu có ràng buộc khóa ngoại (ví dụ: còn SV, GV, Môn học).
     */
    public static boolean deleteDepartment(String maKhoa) {
        String sql = "DELETE FROM Khoa WHERE maKhoa = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");

            stmt.setString(1, maKhoa);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                showAlert(Alert.AlertType.ERROR, "Lỗi Xóa", "Không tìm thấy khoa để xóa.");
                return false;
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            // KHẮC PHỤC LỖI FOREIGN KEY (1451)
            if (e.getErrorCode() == 1451) {
                showAlert(Alert.AlertType.ERROR, "Lỗi Ràng Buộc",
                        "Không thể xóa Khoa này! Khoa vẫn còn Sinh viên, Giảng viên, hoặc Môn học.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể xóa khoa: " + e.getMessage());
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