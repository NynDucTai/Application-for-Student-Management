package com.example.doanlaptrinhhuongdoituong.view;

import com.example.doanlaptrinhhuongdoituong.controller.admin.AdminController;
import com.example.doanlaptrinhhuongdoituong.controller.student.StudentController;
import com.example.doanlaptrinhhuongdoituong.controller.teacher.TeacherController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomeViewFactory {

    private AccountType loginAccountType;

    // Sudent View //
    private final ObjectProperty<StudentMenuOptions> StudentSelectedMenuItem;

    // Teacher View //
    private final ObjectProperty<TeacherMenuOptions> TeacherSelectedMenuItem;

    // Admin View //
    private final ObjectProperty<AdminMenuOptions>  AdminSelectedMenuItem;

    // --- CÁC BIẾN AnchorPane ĐÃ BỊ XÓA ---
    // (Xóa bỏ các biến như 'private AnchorPane StudentHomeView;'
    // để đảm bảo giao diện luôn được tải mới)


    public HomeViewFactory() {
        this.StudentSelectedMenuItem = new SimpleObjectProperty<>();
        this.TeacherSelectedMenuItem = new SimpleObjectProperty<>();
        this.AdminSelectedMenuItem = new SimpleObjectProperty<>();
        this.loginAccountType = AccountType.SINHVIEN;
    }


    public ObjectProperty<StudentMenuOptions> getStudentSelectedMenuItem() {
        return StudentSelectedMenuItem;
    }

    public ObjectProperty<TeacherMenuOptions> getTeacherSelectedMenuItem() {
        return TeacherSelectedMenuItem;
    }

    public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem() {
        return AdminSelectedMenuItem;
    }

    public AccountType getLoginAccountType() {
        return loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }

    // --- Sudent Main Screen View (ĐÃ SỬA) ---
    // (Các hàm này giờ sẽ luôn tải FXML mới)

    public AnchorPane getStudentHomeView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Student/StudentHomeView.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getStudentScoreView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Student/StudentScoreView.fxml")).load();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getStudentCourtView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Student/StudentCourtView.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getStudentCourtSignedView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Student/StudentCourtSignedView.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getStudentInfoView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Student/StudentInfoView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getStudentRetakeClassView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Student/StudentRetakeClassView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getStudentWishClassView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Student/StudentWishClassView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    // --- Teacher Main Screen View (ĐÃ SỬA) ---

    public AnchorPane getTeacherHomeView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Teacher/TeacherHomeView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getTeacherStuManageView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Teacher/TeacherStuManageView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getTeacherAddScoreView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Teacher/TeacherAddScoreView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getTeacherStaticView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Teacher/TeacherStaticView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    // --- Admin Main Screen View (ĐÃ SỬA) ---

    public AnchorPane getAdminAssignView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Admin/AdminAssignView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getAdminDepartmentView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Admin/AdminDepartmentManageView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getAdminClassVerifyView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Admin/AdminClassVerifyView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getAdminRetakeClassView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Admin/AdminRetakeClassView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getAdminStuManageView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Admin/AdminStuManageView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getAdminTeacherManageView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Admin/AdminTeacherManageView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getAdminStaticView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Admin/AdminStaticView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getAdminSubjectManageView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Admin/AdminSubjectManageView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public AnchorPane getAdminClassManageView() {
        AnchorPane view = null;
        try {
            view = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Admin/AdminClassManageView.fxml")).load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    // --- CÁC HÀM "SHOW..." KHÔNG THAY ĐỔI ---

    // Show Student Home Screen //

    public void showStudentHomeView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Student/StudentHome.fxml"));
        StudentController studentController = new StudentController();
        loader.setController(studentController);
        createStage(loader);
    }

    // Show Teacher Home Screen //

    public void showTeacherHomeView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Teacher/TeacherHome.fxml"));
        TeacherController teacherController = new TeacherController();
        loader.setController(teacherController);
        createStage(loader);
    }

    // Show Admin Home View //

    public void showAdminHomeView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/Admin/AdminHome.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
        createStage(loader);
    }

    // Show Login View //

    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/doanlaptrinhhuongdoituong/FXML/LoginView.fxml"));
        createStage(loader);
    }

    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Hệ thống quản lý điểm");
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }
}