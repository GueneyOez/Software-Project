package com.example.timemanagementapp;

import controller.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.util.List;

import static com.example.timemanagementapp.MongoConnection.getEmployeesFromDatabase;

public class LoginScreen {
    private Stage stage;
    private List<Document> employees;

    public LoginScreen(Stage stage) {
        this.stage = stage;
        this.employees = getEmployeesFromDatabase();
    }

    public void show() {
        try {
            // Laden der FXML-Datei
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timemanagementapp/LoginScreenSample.fxml"));
            Scene scene = new Scene(loader.load(), 600, 440);

            // Erstellung des Controllers und Setzen der Stage
            LoginController loginController = loader.getController();
            loginController.setStage(stage);
            loginController.setEmployees(employees); // Setze die Mitarbeiterdaten

            // Setze HomeScreen im LoginController
            loginController.setHomeScreen(this);

            // Anpassung der Szene
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setHomeScreen(HomeScreen homeScreen) {
    }
}
