package com.example.doanlaptrinhhuongdoituong;

import com.example.doanlaptrinhhuongdoituong.model.MainViewModel;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Lớp Application chính để khởi chạy ứng dụng JavaFX.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainViewModel.getInstance().getViewFactory().showLoginWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
