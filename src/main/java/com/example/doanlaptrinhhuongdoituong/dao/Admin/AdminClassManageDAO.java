package com.example.doanlaptrinhhuongdoituong.dao.Admin;

import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminClassManageModel;
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
 * Chứa các hàm CSDL cho chức năng Quản lý Lớp Học Phần.
 * (Đã cập nhật để xử lý 'tenLopHP')
 */
public class AdminClassManageDAO {

    // (Hàm getMonHocList() giữ nguyên)
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

    // (Hàm getQualifiedClasses() giữ nguyên)
    public static ObservableList<AdminClassManageModel> getQualifiedClasses(String loaiLop) {
        ObservableList<AdminClassManageModel> classList = FXCollections.observableArrayList();
        String sql = "SELECT l.*, m.tenMon, m.soTinChi " +
                "FROM LopHocPhan l " +
                "JOIN MonHoc m ON l.maMon = m.maMon " +
                "WHERE l.loaiLop = ? " +
                "AND l.trangThai = 'DANG_CHO' " +
                "AND l.siSoHienTai >= l.siSoToiThieu";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, loaiLop);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    classList.add(mapResultSetToModel(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách lớp chờ: " + e.getMessage());
        }
        return classList;
    }

    // (Hàm getOfficialClasses() giữ nguyên)
    public static ObservableList<AdminClassManageModel> getOfficialClasses() {
        ObservableList<AdminClassManageModel> classList = FXCollections.observableArrayList();
        String sql = "SELECT l.*, m.tenMon, m.soTinChi " +
                "FROM LopHocPhan l " +
                "JOIN MonHoc m ON l.maMon = m.maMon " +
                "WHERE l.loaiLop = 'CHINH_QUY'";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                classList.add(mapResultSetToModel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách lớp học phần: " + e.getMessage());
        }
        return classList;
    }

    /**
     * (Mode 1) Chuyển đổi lớp chờ (ĐÃ CẬP NHẬT)
     * Thêm 'newTenLop' vào hàm
     */
    public static boolean createClassFromExisting(AdminClassManageModel lopCho, String newMaLopHP, String newTenLop, int newSiSoToiDa) {
        Connection conn = null;
        String sqlUpdateOld = "UPDATE LopHocPhan SET trangThai = 'DA_DUYET' WHERE maLopHP = ?";

        // SỬA SQL: Thêm cột 'tenLopHP'
        String sqlInsertNew = "INSERT INTO LopHocPhan (maLopHP, tenLopHP, maMon, hocKy, namHoc, " +
                "siSoToiThieu, siSoToiDa, trangThai, loaiLop, siSoHienTai) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 'CHINH_THUC', 'CHINH_QUY', 0)";

        try {
            conn = DatabaseUtil.getConnection();
            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");

            conn.setAutoCommit(false);

            // 1. Cập nhật lớp cũ
            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdateOld)) {
                stmtUpdate.setString(1, lopCho.getMaLopHP());
                stmtUpdate.executeUpdate();
            }

            // 2. Tạo lớp mới
            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsertNew)) {
                stmtInsert.setString(1, newMaLopHP);
                stmtInsert.setString(2, newTenLop); // <-- DÒNG MỚI
                stmtInsert.setString(3, lopCho.getMaMon());
                stmtInsert.setInt(4, lopCho.getHocKy());
                stmtInsert.setString(5, lopCho.getNamHoc());
                stmtInsert.setInt(6, lopCho.getSiSoToiThieu());
                stmtInsert.setInt(7, newSiSoToiDa);
                stmtInsert.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            // ... (xử lý lỗi)
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tạo lớp: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * (Mode 2) Thêm một Lớp Học Phần mới (độc lập) (ĐÃ CẬP NHẬT)
     */
    public static boolean addIndependentClass(AdminClassManageModel lop) {
        // SỬA SQL: Thêm cột 'tenLopHP'
        String sql = "INSERT INTO LopHocPhan (maLopHP, tenLopHP, maMon, hocKy, namHoc, " +
                "siSoToiThieu, siSoToiDa, trangThai, loaiLop, siSoHienTai) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 'CHINH_THUC', 'CHINH_QUY', 0)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");

            stmt.setString(1, lop.getMaLopHP());
            stmt.setString(2, lop.getTenLopHP()); // <-- DÒNG MỚI
            stmt.setString(3, lop.getMaMon());
            stmt.setInt(4, lop.getHocKy());
            stmt.setString(5, lop.getNamHoc());
            stmt.setInt(6, 0); // Sĩ số tối thiểu (mặc định 0)
            stmt.setInt(7, lop.getSiSoToiDa());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // ... (xử lý lỗi)
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tạo lớp độc lập: " + e.getMessage());
            return false;
        }
    }

    // (Hàm deleteOfficialClass() giữ nguyên)
    public static boolean deleteOfficialClass(String maLopHP) {
        Connection conn = null;
        String sqlDangKi = "DELETE FROM DangKi_KetQua WHERE maLopHP = ?";
        String sqlPhanCong = "DELETE FROM PhanCongGiangDay WHERE maLopHP = ?";
        String sqlLopHP = "DELETE FROM LopHocPhan WHERE maLopHP = ? AND loaiLop = 'CHINH_QUY'";
        try {
            conn = DatabaseUtil.getConnection();
            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(sqlDangKi)) {
                stmt.setString(1, maLopHP);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(sqlPhanCong)) {
                stmt.setString(1, maLopHP);
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conn.prepareStatement(sqlLopHP)) {
                stmt.setString(1, maLopHP);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Không tìm thấy lớp học phần để xóa.");
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            // ... (xử lý lỗi)
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể xóa lớp: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // (Hàm mapResultSetToModel() giữ nguyên, vì nó đã đọc 'tenLopHP')
    private static AdminClassManageModel mapResultSetToModel(ResultSet rs) throws SQLException {
        AdminClassManageModel lop = new AdminClassManageModel();
        lop.setMaLopHP(rs.getString("maLopHP"));
        lop.setMaMon(rs.getString("maMon"));
        lop.setTenMonHoc(rs.getString("tenMon"));
        lop.setHocKy(rs.getInt("hocKy"));
        lop.setNamHoc(rs.getString("namHoc"));
        lop.setSoTinChi(rs.getInt("soTinChi"));
        lop.setSiSoToiThieu(rs.getInt("siSoToiThieu"));
        lop.setSiSoToiDa(rs.getInt("siSoToiDa"));
        lop.setSiSoHienTai(rs.getInt("siSoHienTai"));
        lop.setLoaiLop(rs.getString("loaiLop"));
        lop.setTenLopHP(rs.getString("tenLopHP")); // <- Đã có sẵn
        return lop;
    }

    // (Hàm showAlert() giữ nguyên)
    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}