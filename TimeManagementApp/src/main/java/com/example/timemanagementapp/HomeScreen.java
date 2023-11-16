package com.example.timemanagementapp;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class HomeScreen {
    private Stage stage;

    public HomeScreen(Stage stage) {
        this.stage = stage;
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

        HBox layout = new HBox(10);
        layout.getChildren().addAll(calendarButton, notificationsButton, timeTrackingButton, addEmployeeButton,logoutButton);

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Home Screen");
        stage.show();
    }

    private void showCalendar() {
        CalendarScene calendarScene = new CalendarScene(stage, this);
        calendarScene.show();
    }

    private void showNotifications() {
        NotificationsScene notificationsScene = new NotificationsScene(stage, this);
        notificationsScene.show();
    }

    private void showTimeTracking() {
        TimeTrackingScene timeTrackingScene = new TimeTrackingScene(stage, this);
        timeTrackingScene.show();
    }

    private void showLogin() {
        LoginScreen loginScreen = new LoginScreen(stage);
        loginScreen.show();
    }

    private void showAddEmployee() {
        AddEmployeeScene addEmployeeScene = new AddEmployeeScene(stage, this);
        addEmployeeScene.show();
    }

    // Methode für den "Zurück"-Button
    public void goBack() {
        show(); // Einfach den HomeScreen erneut anzeigen
    }
}
