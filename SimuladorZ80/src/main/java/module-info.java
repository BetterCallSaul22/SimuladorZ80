module com.example.xdddd {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.xdddd to javafx.fxml;
    exports com.example.xdddd;
}