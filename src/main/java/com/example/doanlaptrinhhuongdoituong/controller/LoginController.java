package com.example.doanlaptrinhhuongdoituong.controller;

import com.example.doanlaptrinhhuongdoituong.model.MainViewModel;
import com.example.doanlaptrinhhuongdoituong.view.HomeViewFactory;
import com.example.doanlaptrinhhuongdoituong.util.DatabaseUtil;
import com.example.doanlaptrinhhuongdoituong.util.UserSession;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

// Thêm "implements Initializable" để điền dữ liệu cho ComboBox
public class LoginController implements Initializable {

    @FXML
    private TextField username_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private ComboBox<String> role_combobox;

    @FXML
    private Button login_button;

    // Đảm bảo FXML của bạn có Label này với fx:id="error_message_label"
    @FXML
    private Label error_message_label;

    private HomeViewFactory viewFactory;

    /**
     * Hàm này tự động chạy khi FXML được tải
     * Dùng để điền dữ liệu cho ComboBox vai trò
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Giả sử MainViewModel và ViewFactory được khởi tạo ở đâu đó
        // Tạm thời khởi tạo ở đây nếu chưa có
        if (MainViewModel.getInstance() == null) {
            // Cần một cách khởi tạo MainViewModel
            // System.err.println("MainViewModel chưa được khởi tạo!");
            // return;
        }
        // Tạm thời giả định MainViewModel đã sẵn sàng
        this.viewFactory = MainViewModel.getInstance().getViewFactory();

        role_combobox.setItems(FXCollections.observableArrayList(
                "Giảng viên",
                "Sinh viên",
                "Admin"
        ));
        role_combobox.getSelectionModel().select("Giảng viên");
    }

    /**
     * Hàm này được gọi khi nhấn nút Đăng nhập.
     */
    @FXML
    private void handleLoginButtonAction() {
        String username = username_field.getText();
        String password = password_field.getText();
        String selectedRole = role_combobox.getSelectionModel().getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            error_message_label.setText("Vui lòng nhập đầy đủ Tên đăng nhập và Mật khẩu.");
            return;
        }

        if (selectedRole == null) {
            error_message_label.setText("Vui lòng chọn tư cách đăng nhập.");
            return;
        }

        // 1. Xác thực đăng nhập
        String[] loginInfo = validateLogin(username, password, selectedRole);

        String role = loginInfo[0]; // "TEACHER", "ADMIN", "STUDENT", "FAILED"
        String id = loginInfo[1];   // maGV, maSV, "admin", or null

        // 2. Điều hướng dựa trên vai trò
        switch (role) {
            case "TEACHER":
                UserSession.getInstance().setMaGiangVien(id); // id chính là maGV
                viewFactory.showTeacherHomeView();
                closeLoginWindow();
                break;

            case "ADMIN":
                viewFactory.showAdminHomeView();
                closeLoginWindow();
                break;

            case "STUDENT":
                // UserSession.getInstance().setMaSinhVien(id); // Nên thêm session cho SV
                UserSession.getInstance().setMaSinhVien(id);
                viewFactory.showStudentHomeView();
                closeLoginWindow();
                break;

            case "FAILED":
            default:
                // Thông báo lỗi đã được set bên trong validateLogin
                break;
        }
    }

    /**
     * Xác thực thông tin đăng nhập với Database
     * Dựa trên vai trò đã chọn.
     */
    private String[] validateLogin(String username, String password, String selectedRole) {
        String query = "SELECT password, maGV, maSV FROM user WHERE username = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");

                // 1. Kiểm tra mật khẩu
                if (!dbPassword.equals(password)) {
                    error_message_label.setText("Tên đăng nhập hoặc Mật khẩu không đúng.");
                    return new String[]{"FAILED", null}; // Sai mật khẩu
                }

                // 2. Mật khẩu đúng, kiểm tra vai trò
                String maGV = rs.getString("maGV");
                String maSV = rs.getString("maSV");

                switch (selectedRole) {
                    case "Giảng viên":
                        if (maGV != null) {
                            return new String[]{"TEACHER", maGV}; // OK
                        }
                        break;
                    case "Sinh viên":
                        if (maSV != null) {
                            return new String[]{"STUDENT", maSV}; // OK
                        }
                        break;
                    case "Admin":
                        if (maGV == null && maSV == null) {
                            return new String[]{"ADMIN", username}; // OK
                        }
                        break;
                }

                // Nếu đến đây: Mật khẩu đúng, nhưng vai trò chọn không khớp
                error_message_label.setText("Tài khoản này không có vai trò: " + selectedRole);
                return new String[]{"FAILED", null};

            } else {
                error_message_label.setText("Tên đăng nhập hoặc Mật khẩu không đúng.");
                return new String[]{"FAILED", null}; // Sai username
            }

        } catch (SQLException e) {
            e.printStackTrace();
            error_message_label.setText("Lỗi kết nối cơ sở dữ liệu.");
            return new String[]{"FAILED", null};
        }
    }

    /**
     * HÀM ĐÃ ĐƯỢC CẬP NHẬT THEO YÊU CẦU
     * Hàm này được gọi khi nhấn vào "Quên mật khẩu".
     */
    @FXML
    private void handleForgotPasswordAction() {
        error_message_label.setText("Liên hệ với Admin để lấy lại mật khẩu");
    }

    /**
     * Mở một giao diện FXML mới.
     */
    private void openDashboard(String fxmlPath, String title) {
        // Tạm thời comment vì viewFactory sẽ xử lý
        // (Code này không còn được dùng nữa nếu bạn dùng viewFactory)
    }

    /**
     * Đóng cửa sổ Đăng nhập.
     */
    private void closeLoginWindow() {
        Stage stage = (Stage) login_button.getScene().getWindow();
        viewFactory.closeStage(stage);
    }
}