module com.example.ws7 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;

    exports com.example.ws7;

    opens com.example.ws7 to javafx.fxml, com.google.gson;
}
