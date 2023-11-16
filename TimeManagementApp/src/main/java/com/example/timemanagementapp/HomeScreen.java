package com.example.timemanagementapp;

import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.bson.Document;

public class HomeScreen {
    private Stage stage;
    private TimeTrackingScene timeTrackingScene;
    private NotificationsScene notificationsScene;
    private CalendarScene calendarScene;
    private Document authenticatedEmployee;
    private String lastStampedTime = "";

    public HomeScreen(Stage stage, Document authenticatedEmployee) {
        this.stage = stage;
        this.timeTrackingScene = new TimeTrackingScene(stage, this, authenticatedEmployee);
        this.notificationsScene = new NotificationsScene(stage, this);
        this.calendarScene = new CalendarScene(stage, this);
        this.authenticatedEmployee = authenticatedEmployee;
    }

    public void show() {
        Button calendarButton = new Button("Kalender");
        Button notificationsButton = new Button("Benachrichtigungen");
        Button timeTrackingButton = new Button("Zeiterfassung");
        Button addEmployeeButton = new Button("Mitarbeiter hinzufügen");
        Button logoutButton = new Button("Logout");

        calendarButton.setOnAction(e -> showCalendar());
        notificationsButton.setOnAction(e -> showNotifications());
        timeTrackingButton.setOnAction(e -> showTimeTracking());
        addEmployeeButton.setOnAction(e -> showAddEmployee());
        logoutButton.setOnAction(e -> showLogin());

        Label welcomeLabel = new Label("Welcome, " + authenticatedEmployee.getString("Name") + "!");

        HBox h_layout = new HBox(10);
        h_layout.getChildren().addAll(calendarButton, notificationsButton, timeTrackingButton);

        if ((authenticatedEmployee.getString("Rolle").equals("Manager"))){
            h_layout.getChildren().addAll(addEmployeeButton);
        }

        h_layout.getChildren().addAll(logoutButton);

        VBox v_layout = new VBox(10);
        v_layout.getChildren().addAll(welcomeLabel);
        v_layout.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setTop(h_layout); // Setze HBox oben
        root.setCenter(v_layout); // Setze VBox in der Mitte

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Home Screen");
        stage.show();
    }

    private void showCalendar() {
        calendarScene.start(stage);
    }

    private void showNotifications() {
        notificationsScene.show();
    }

    private void showTimeTracking() {
        timeTrackingScene = new TimeTrackingScene(stage, this, authenticatedEmployee);
        timeTrackingScene.show();
    }

    private void showLogin() {
        LoginScreen loginScreen = new LoginScreen(stage);
        loginScreen.setHomeScreen(this);
        loginScreen.show();
    }

    private void showAddEmployee() {
        AddEmployeeScene addEmployeeScene = new AddEmployeeScene(stage, this);
        addEmployeeScene.show();
    }

    // Methode für den "Zurück"-Button
    public void goBack() {
        show();
    }

    public Scene getCurrentScene() {
        return stage.getScene();
    }

    // Optional: Methode zum Abrufen des letzten Stempelzeitpunkts
    public String getLastStampedTime() {
        return lastStampedTime;
    }

    // Methode zum Aktualisieren des letzten Stempelzeitpunkts
    public void updateLastStampedTime(String stampedTime) {
        this.lastStampedTime = stampedTime;
    }
}
