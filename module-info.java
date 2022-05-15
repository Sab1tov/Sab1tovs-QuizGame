module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires java.xml.crypto;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}