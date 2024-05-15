module com.example.testjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.apache.poi.ooxml;
    requires java.datatransfer;

    opens com.example.taxy to javafx.fxml;
    exports com.example.taxy;
}