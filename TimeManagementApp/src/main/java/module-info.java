module com.example.timemanagementapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.timemanagementapp to javafx.fxml;
    exports com.example.timemanagementapp;
}