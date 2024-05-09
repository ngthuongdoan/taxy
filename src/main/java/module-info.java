module com.example.testjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.apache.poi.ooxml;

    opens com.example.testjavafx to javafx.fxml;
    exports com.example.testjavafx;
}