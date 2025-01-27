module com.example.ws6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;

    exports com.example.ws6;

    opens com.example.ws6 to javafx.fxml, com.google.gson;
}
