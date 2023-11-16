package com.example.timemanagementapp;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class NotificationsScene {
    private Stage stage;
    private HomeScreen homeScreen;

    public NotificationsScene(Stage stage, HomeScreen homeScreen) {
        this.stage = stage;
        this.homeScreen = homeScreen;
    }

    public void showNotification(String message) {
        Label label = new Label(message);
        Button backButton = new Button("Go Back");

        backButton.setOnAction(e -> homeScreen.goBack());

        StackPane layout = new StackPane();
        layout.getChildren().addAll(backButton, label); // Zurück-Button zuerst

        StackPane.setAlignment(backButton, javafx.geometry.Pos.TOP_LEFT); // Button oben links

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Benachrichtigungen");
        stage.show();
    }

    public void show() {
    }
}
