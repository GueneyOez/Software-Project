module com.example.timemanagementapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires org.mongodb.driver.sync.client;


    opens com.example.timemanagementapp to javafx.fxml;
    exports com.example.timemanagementapp;

    exports controller;
    opens controller to javafx.fxml;
}



