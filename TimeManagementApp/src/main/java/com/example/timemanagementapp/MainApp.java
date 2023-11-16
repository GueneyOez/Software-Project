package com.example.timemanagementapp;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Hier wird der Login-Bildschirm initialisiert und angezeigt
        LoginScreen loginScreen = new LoginScreen(primaryStage);
        HomeScreen homeScreen = new HomeScreen(primaryStage);

        loginScreen.setHomeScreen(homeScreen);

        loginScreen.show();
    }
}
