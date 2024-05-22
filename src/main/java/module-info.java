module com.example.taxy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.datatransfer;
    requires org.apache.poi.poi;

    opens com.example.taxy to javafx.fxml;
    exports com.example.taxy;
}