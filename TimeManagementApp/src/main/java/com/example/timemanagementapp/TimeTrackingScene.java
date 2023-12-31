package com.example.timemanagementapp;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.bson.Document;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.timemanagementapp.MongoConnection.addStempelzeitEintrag;

public class TimeTrackingScene {
    private Stage stage;
    private HomeScreen homeScreen;
    private List<LocalTime> startTimes;
    private LocalTime endTime;
    private Timer timer;
    private boolean timerRunning;
    private Label timeLabel; // Label für die Zeiterfassung

    private Document authenticatedEmployee;

    public TimeTrackingScene(Stage stage, HomeScreen homeScreen, Document authenticatedEmployee) {
        this.stage = stage;
        this.homeScreen = homeScreen;
        this.startTimes = new ArrayList<>();
        this.timer = new Timer();
        this.timerRunning = false;
        this.authenticatedEmployee = authenticatedEmployee;
    }

    public void show() {
            Label label = new Label("Zeiterfassung-Szene");
            Button backButton = new Button("Zurück");
            Button startButton = new Button("Einstempeln");
            Button stopButton = new Button("Ausstempeln");
            timeLabel = new Label(homeScreen.getLastStampedTime()); // Setzen Sie den letzten Stempelzeitpunkt

            // Setze die Hintergrundfarbe der Buttons auf Weiß
            backButton.setStyle("-fx-background-color: white;");
            startButton.setStyle("-fx-background-color: white;");
            stopButton.setStyle("-fx-background-color: white;");

            startButton.setOnAction(e -> startTracking());
            stopButton.setOnAction(e -> stopTracking());
            backButton.setOnAction(e -> homeScreen.goBack());

            VBox layout = new VBox(10);
            layout.getChildren().addAll(backButton, label, startButton, stopButton, timeLabel);

            // Setze Padding für die VBox, um den Abstand zum Fensterrand zu vergrößern
            Insets vboxInsets = new Insets(20); // Hier den Wert anpassen
            layout.setPadding(vboxInsets);

            layout.setStyle("-fx-background-color: #808080");

            Scene scene = new Scene(layout, 600, 440);
            stage.setScene(scene);
            stage.setTitle("Zeiterfassung");
            stage.show();
    }

    private void startTracking() {
        if (!timerRunning) {
            LocalTime startTime = LocalTime.now();
            startTimes.add(startTime);
            timer = new Timer(true);
            //Einstempelzeit in die DB eintragen
            addStempelzeitEintrag(authenticatedEmployee.getInteger("EmployeeId"), String.valueOf(startTime), "Eintritt");

                // Benachrichtigung nach 1 Minute
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            if (authenticatedEmployee.getString("Rolle").equals("Mitarbeiter")) {
                                if (timerRunning) {
                                    NotificationsScene notificationsScene = new NotificationsScene(stage, homeScreen);
                                    notificationsScene.showNotification("Es ist Zeit zum Ausstempeln");
                                }
                            }
                        });
                    }
                }, 60 * 1000); // 1 Minute in Millisekunden


                // Benachrichtigung nach 2 Minuten
                timer.schedule(new TimerTask() {
                    @Override

                    public void run() {
                        Platform.runLater(() -> {
                            if (authenticatedEmployee.getString("Rolle").equals("Mitarbeiter")) {
                                if (timerRunning) {
                                    NotificationsScene notificationsScene = new NotificationsScene(stage, homeScreen);
                                    notificationsScene.showNotification("Stempeln Sie bitte aus, dies ist Ihre erste und letzte Warnung!");
                                }
                            }
                        });
                    }
                }, 2 * 60 * 1000); // 2 Minuten in Millisekunden

            homeScreen.updateLastStampedTime("Eingestempelt um " + formatTime(startTime));
            timeLabel.setText(homeScreen.getLastStampedTime());
            timerRunning = true;
        }
    }

    private void stopTracking() {
        if (timerRunning) {
            endTime = LocalTime.now();
            timer.cancel();

            addStempelzeitEintrag(authenticatedEmployee.getInteger("EmployeeId"), String.valueOf(endTime), "Austritt");

            // Berechne Gesamtzeit
            Duration totalDuration = Duration.between(startTimes.get(startTimes.size() - 1), endTime);
            timeLabel.setText(
                    "Eingestempelt um: " + formatTime(startTimes.get(startTimes.size() - 1)) +
                            "\n\nAusgestempelt um: " + formatTime(endTime) +
                            "\n\nGearbeitete Stunden: " + formatDuration(totalDuration));
            homeScreen.updateLastStampedTime(""); // Zurücksetzen des Stempelzeitpunkts
            timerRunning = false;
        }
    }

    private String formatTime(LocalTime time) {
        return String.format("%02d:%02d:%02d", time.getHour(), time.getMinute(), time.getSecond());
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
