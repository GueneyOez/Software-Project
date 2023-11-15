package com.example.timemanagementapp;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class LoginScreen {
    private Stage stage;

    public LoginScreen(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // UI-Elemente
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label messageLabel = new Label(); // Neues Label für Meldungen

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button cancelButton = new Button("Cancel");

        // Layout
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        // Anordnung der UI-Elemente
        gridPane.add(usernameLabel, 0, 1);
        gridPane.add(usernameField, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(loginButton, 0, 3);
        gridPane.add(cancelButton, 1, 3);
        gridPane.add(messageLabel, 0, 4, 2, 1); // Meldungslabel in der letzten Zeile

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (validateLogin(username, password)) {
                // Wenn die Anmeldedaten korrekt sind, wechsle zu HomeScreen
                HomeScreen homeScreen = new HomeScreen(stage);
                homeScreen.show();
            } else {
                // Zeige eine Fehlermeldung für eine falsche Anmeldung
                messageLabel.setText("Invalid username or password!");
            }
        });

        cancelButton.setOnAction(e -> stage.close()); // Schließe die Anwendung beim Klicken auf "Cancel"

        Scene scene = new Scene(gridPane, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    // Eine einfache Authentifizierungslogik
    private boolean validateLogin(String username, String password) {
        return username.equals("obiwan") && password.equals("kenobi");
    }
}
