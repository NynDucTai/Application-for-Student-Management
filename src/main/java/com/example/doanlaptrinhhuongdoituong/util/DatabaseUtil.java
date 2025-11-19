package com.example.doanlaptrinhhuongdoituong.util;

import com.example.doanlaptrinhhuongdoituong.view.AccountType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Lớp tiện ích CSDL - Phiên bản kết nối CSDL MySQL thật.
 */
public class DatabaseUtil {

    // Thông tin kết nối CSDL của bạn
    private static final String URL = "jdbc:mysql://localhost:3306/quanlysinhvien?serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "123456"; // THAY MẬT KHẨU CỦA BẠN VÀO ĐÂY

    /**
     * Lấy kết nối đến CSDL.
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi kết nối CSDL: " + e.getMessage());
        }
        return conn;
    }

    /**
     * Xác thực người dùng từ CSDL MySQL thật.
     *
     * @param username    Tài khoản (từ emailField)
     * @param password    Mật khẩu (từ passwordField)
     * @param accountType Loại tài khoản (từ acc_selector)
     * @return true nếu hợp lệ, false nếu không.
     */
    public static boolean validateLogin(String username, String password, AccountType accountType) {
        // 1. Kiểm tra đầu vào
        if (username == null || password == null || accountType == null ||
                username.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }

        String sql = "";

        // 2. Xây dựng câu SQL dựa trên loại tài khoản
        // Logic này dựa trên CSDL chúng ta đã thiết kế (bảng USER)
        switch (accountType) {
            case SINHVIEN:
                // Phải là user có username, password, VÀ maSV không rỗng
                sql = "SELECT 1 FROM USER WHERE username = ? AND password = ? AND maSV IS NOT NULL";
                break;
            case GIANGVIEN:
                // Phải là user có username, password, VÀ maGV không rỗng
                sql = "SELECT 1 FROM USER WHERE username = ? AND password = ? AND maGV IS NOT NULL";
                break;
            case ADMIN:
                // Giả định: ADMIN là user đặc biệt (ví dụ: 'admin')
                // và không có maSV hoặc maGV (hoặc một logic riêng của bạn)
                // Ở đây tôi giả định admin là user có maSV VÀ maGV đều rỗng
                sql = "SELECT 1 FROM USER WHERE username = ? AND password = ? AND maSV IS NULL AND maGV IS NULL";
                break;
            default:
                return false; // Loại tài khoản không xác định
        }

        // 3. Thực thi truy vấn
        // Sử dụng try-with-resources để tự động đóng kết nối
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // CẢNH BÁO: Mật khẩu đang ở dạng văn bản thuần!

            // ResultSet rs chứa kết quả
            try (ResultSet rs = stmt.executeQuery()) {
                // Nếu rs.next() là true, nghĩa là tìm thấy 1 dòng khớp
                // -> Đăng nhập thành công
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Lỗi truy vấn CSDL: " + e.getMessage());
            return false;
        }
    }
}