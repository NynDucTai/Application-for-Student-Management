module com.example.doanlaptrinhhuongdoituong {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires transitive mysql.connector.j;

    opens com.example.doanlaptrinhhuongdoituong.controller to javafx.fxml;
    opens com.example.doanlaptrinhhuongdoituong.controller.student to javafx.fxml;
    opens com.example.doanlaptrinhhuongdoituong.controller.teacher to javafx.fxml;
    opens com.example.doanlaptrinhhuongdoituong.controller.admin to javafx.fxml;
    exports com.example.doanlaptrinhhuongdoituong;
}