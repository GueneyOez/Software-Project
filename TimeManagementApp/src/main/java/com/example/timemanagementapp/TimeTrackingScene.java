package com.example.timemanagementapp;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TimeTrackingScene {
    private Stage stage;
    private HomeScreen homeScreen;
    private long startTime;

    public TimeTrackingScene(Stage stage, HomeScreen homeScreen) {
        this.stage = stage;
        this.homeScreen = homeScreen;
    }

    public void show() {
        Label label = new Label("Zeiterfassung-Szene");
        Button backButton = new Button("Go Back");
        Button startButton = new Button("Einstempeln");
        Button stopButton = new Button("Ausstempeln");
        Label timeLabel = new Label(); // Neues Label für die Zeiterfassung

        startButton.setOnAction(e -> {
            startTracking();
            timeLabel.setText("Arbeitszeit gestartet.");
        });

        stopButton.setOnAction(e -> {
            stopTracking();
            timeLabel.setText("Arbeitszeit gestoppt. Gesamtstunden: " + calculateHoursWorked());
        });

        backButton.setOnAction(e -> homeScreen.goBack());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(backButton, label, startButton, stopButton, timeLabel); // Zurück-Button zuerst

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Zeiterfassung");
        stage.show();
    }

    private void startTracking() {
        startTime = System.currentTimeMillis();
    }

    private void stopTracking() {
        // Arbeitszeit in Stunden umrechnen
        double hoursWorked = calculateHoursWorked();
        System.out.println("Arbeitszeit: " + hoursWorked + " Stunden");
    }

    private double calculateHoursWorked() {
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // Umrechnung in Stunden (1 Stunde = 3600000 Millisekunden)
        return (double) elapsedTime / 3600000;
    }
}
