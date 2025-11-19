package com.example.doanlaptrinhhuongdoituong.dao.Admin;

import com.example.doanlaptrinhhuongdoituong.model.Admin.AdminStuManageModel;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Lớp DAO (Data Access Object)
 * Chứa tất cả các hàm truy vấn CSDL liên quan đến Sinh Viên.
 */
public class AdminStuManageDAO {

    /**
     * Lấy tất cả sinh viên từ CSDL để hiển thị.
     * JOIN với bảng Khoa và USER để lấy đủ thông tin.
     */
    public static ObservableList<AdminStuManageModel> getStudents() {
        ObservableList<AdminStuManageModel> studentList = FXCollections.observableArrayList();

        // Câu SQL này JOIN 3 bảng: SinhVien, Khoa, và USER
        String sql = "SELECT " +
                "    s.maSV AS maSV, " +
                "    s.hoTen AS hoTen, " +
                "    s.ngaySinh AS ngaySinh, " +
                "    s.gioiTinh AS gioiTinh, " +
                "    s.queQuan AS queQuan, " +
                "    k.tenKhoa AS tenKhoa, " +
                "    s.maKhoa AS maKhoa, " +  // <-- ĐÂY LÀ CỘT BẠN BỊ THIẾU
                "    s.lop AS lop, " +
                "    u.username AS username, " +
                "    u.password AS password " +
                "FROM SinhVien s " +
                "JOIN Khoa k ON s.maKhoa = k.maKhoa " +
                "LEFT JOIN USER u ON s.maSV = u.maSV";

        try (Connection conn = DatabaseUtil.getConnection(); // Lấy kết nối
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (conn == null) {
                System.err.println("getStudents: Kết nối CSDL thất bại.");
                return studentList; // Trả về danh sách rỗng
            }

            while (rs.next()) {
                Date sqlDate = rs.getDate("ngaySinh");
                LocalDate ngaySinh = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                // Dùng constructor rỗng và setter để dễ gán
                AdminStuManageModel student = new AdminStuManageModel();
                student.setMaSV(rs.getString("maSV"));
                student.setHoTen(rs.getString("hoTen"));
                student.setNgaySinh(ngaySinh);
                student.setGioiTinh(rs.getString("gioiTinh"));
                student.setQueQuan(rs.getString("queQuan"));
                student.setTenKhoa(rs.getString("tenKhoa"));
                student.setMaKhoa(rs.getString("maKhoa")); // <-- GÁN DỮ LIỆU maKhoa
                student.setLop(rs.getString("lop"));
                student.setUsername(rs.getString("username"));
                student.setPassword(rs.getString("password"));

                studentList.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể tải danh sách sinh viên.");
        }
        return studentList;
    }


    /**
     * Thêm sinh viên mới (bao gồm cả tạo USER).
     * Dùng Transaction để đảm bảo thêm vào cả 2 bảng.
     */
    public static boolean addStudent(AdminStuManageModel student, String maKhoa) {
        Connection conn = null;
        String sqlSinhVien = "INSERT INTO SinhVien (maSV, hoTen, ngaySinh, gioiTinh, queQuan, lop, maKhoa) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlUser = "INSERT INTO USER (username, password, maSV) VALUES (?, ?, ?)";

        try {
            conn = DatabaseUtil.getConnection();
            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");

            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Thêm vào bảng SinhVien
            try (PreparedStatement stmtSV = conn.prepareStatement(sqlSinhVien)) {
                stmtSV.setString(1, student.getMaSV());
                stmtSV.setString(2, student.getHoTen());
                stmtSV.setDate(3, student.getNgaySinh() != null ? Date.valueOf(student.getNgaySinh()) : null);
                stmtSV.setString(4, student.getGioiTinh());
                stmtSV.setString(5, student.getQueQuan());
                stmtSV.setString(6, student.getLop());
                stmtSV.setString(7, maKhoa);
                stmtSV.executeUpdate();
            }

            // 2. Thêm vào bảng USER
            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser)) {
                stmtUser.setString(1, student.getUsername());
                stmtUser.setString(2, student.getPassword()); // CẢNH BÁO: Mật khẩu nên được băm!
                stmtUser.setString(3, student.getMaSV());
                stmtUser.executeUpdate();
            }

            conn.commit(); // Hoàn tất Transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể thêm sinh viên: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    /**
     * Xóa sinh viên (bao gồm cả USER).
     * Dùng Transaction để xóa ở cả 2 bảng.
     */
    public static boolean deleteStudent(String maSV) {
        Connection conn = null;

        // ĐỊNH NGHĨA CÁC CÂU SQL (THỨ TỰ XÓA RẤT QUAN TRỌNG)
        // 1. Xóa ở bảng con 'dangki_ketqua' TRƯỚC TIÊN
        String sqlDangKi = "DELETE FROM DangKi_KetQua WHERE maSV = ?";
        // 2. Xóa ở bảng con 'USER'
        String sqlUser = "DELETE FROM USER WHERE maSV = ?";
        // 3. Xóa ở bảng cha 'SinhVien' SAU CÙNG
        String sqlSinhVien = "DELETE FROM SinhVien WHERE maSV = ?";

        try {
            conn = DatabaseUtil.getConnection();
            if (conn == null) throw new SQLException("Kết nối CSDL thất bại.");

            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Thực thi xóa ở DangKi_KetQua
            try (PreparedStatement stmt = conn.prepareStatement(sqlDangKi)) {
                stmt.setString(1, maSV);
                stmt.executeUpdate();
            }

            // 2. Thực thi xóa ở USER
            try (PreparedStatement stmt = conn.prepareStatement(sqlUser)) {
                stmt.setString(1, maSV);
                stmt.executeUpdate();
            }

            // 3. Thực thi xóa ở SinhVien (sau cùng)
            try (PreparedStatement stmt = conn.prepareStatement(sqlSinhVien)) {
                stmt.setString(1, maSV);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    // Nếu không có sinh viên nào bị xóa (ví dụ: mã SV sai)
                    throw new SQLException("Không tìm thấy sinh viên để xóa.");
                }
            }

            conn.commit(); // Hoàn tất Transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } // Hủy bỏ nếu có lỗi
            showAlert(Alert.AlertType.ERROR, "Lỗi CSDL", "Không thể xóa sinh viên: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

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

    // Hàm tiện ích
    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}