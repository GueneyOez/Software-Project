package com.example.timemanagementapp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CalendarScene {
    private static final int MAX_VACATION_DAYS = 24;
    private static final int MAX_SICKNESS_DAYS = 10;

    private int remainingVacationDays = MAX_VACATION_DAYS;
    private int remainingSicknessDays = MAX_SICKNESS_DAYS;

    private Month currentMonth = LocalDate.now().getMonth();
    private int currentYear = LocalDate.now().getYear();

    private VBox root;
    private GridPane gridPane;
    private final Map<LocalDate, Entry> entries = new HashMap<>();

    private Stage primaryStage;
    private HomeScreen homeScreen;

    public CalendarScene(Stage primaryStage, HomeScreen homeScreen) {
        this.primaryStage = primaryStage;
        this.homeScreen = homeScreen;
    }

    public void start(Stage primaryStage) {

        primaryStage.setTitle("Kalender");

        root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);

        root.setStyle("-fx-background-color: #808080;");

        HBox header = createHeader();
        gridPane = createCalendarGrid();

        content.getChildren().addAll(header, gridPane);
        root.getChildren().add(content);

        Scene scene = new Scene(root, 600, 440);
        primaryStage.setScene(scene);

        root.setPadding(new Insets(0, 20, 0, 20));

        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();

        primaryStage.show();
    }

    private HBox createHeader() {

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER);

        Button backButton = new Button("Zurück");
        Button prevMonthButton = new Button("<<");
        Button nextMonthButton = new Button(">>");

        backButton.setStyle("-fx-background-color: white;");
        prevMonthButton.setStyle("-fx-background-color: white;");
        nextMonthButton.setStyle("-fx-background-color: white;");

        Label monthLabel = new Label(currentMonth.toString() + " " + currentYear);
        Label remainingDaysLabel = new Label("Offene Urlaubstage: " + remainingVacationDays +
                " | Offene Krankheitstage: " + remainingSicknessDays);

        prevMonthButton.setOnAction(event -> {
            currentMonth = currentMonth.minus(1);
            if (currentMonth == Month.DECEMBER) {
                currentYear--;
            }
            updateCalendar();
        });

        nextMonthButton.setOnAction(event -> {
            currentMonth = currentMonth.plus(1);
            if (currentMonth == Month.JANUARY) {
                currentYear++;
            }
            updateCalendar();
        });

        backButton.setOnAction(e -> homeScreen.goBack());

        header.getChildren().addAll(backButton, prevMonthButton, monthLabel, remainingDaysLabel, nextMonthButton);

        HBox.setHgrow(monthLabel, Priority.ALWAYS);

        return header;
    }

    private GridPane createCalendarGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Wochentagslabels hinzufügen
        String[] weekdays = {"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"};
        for (int i = 0; i < weekdays.length; i++) {
            Label weekdayLabel = new Label(weekdays[i]);
            gridPane.add(weekdayLabel, i, 0);
        }

        int daysInMonth = currentMonth.length(LocalDate.now().isLeapYear());
        DayOfWeek firstDayOfWeek = getFirstDayOfMonth(currentMonth, currentYear);

        int dayOfMonth = 1;
        LocalDate currentDate = LocalDate.of(currentYear, currentMonth, dayOfMonth);
        int columnIndex = (firstDayOfWeek.getValue() - 1 + 7) % 7; // Starte in der richtigen Spalte
        int rowIndex = 1;

        while (dayOfMonth <= daysInMonth) {
            Button dayButton = createDayButton(dayOfMonth, currentDate);
            gridPane.add(dayButton, columnIndex, rowIndex);

            dayOfMonth++;
            currentDate = currentDate.plusDays(1);

            columnIndex = (columnIndex + 1) % 7;
            if (columnIndex == 0) {
                rowIndex++;
            }
        }

        return gridPane;
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private Button createDayButton(int dayOfMonth, LocalDate date) {
        Button dayButton = new Button(Integer.toString(dayOfMonth));
        dayButton.setMinSize(70, 50);
        dayButton.setStyle("-fx-background-color: white;");

        // Prüfe, ob der Tag ein Wochenendtag ist (Samstag oder Sonntag)
        if (isWeekend(date)) {
            dayButton.setDisable(true); // Deaktiviere den Button für Wochenendtage
        } else {
            dayButton.setOnAction(event -> {
                if (entries.containsKey(date)) {
                    handleExistingEntry(date);
                } else {
                    handleNewEntry(date);
                }
            });
        }

        // Ändere die Farbe des Buttons je nach Eintragstyp
        if (entries.containsKey(date)) {
            Entry entry = entries.get(date);
            if (entry.getType() == EntryType.Urlaub) {
                dayButton.setStyle("-fx-background-color: lightblue;");
            } else if (entry.getType() == EntryType.Krankheit) {
                dayButton.setStyle("-fx-background-color: lightcoral;");
            }
        }

        return dayButton;
    }

    private void handleNewEntry(LocalDate date) {
        Dialog<EntryType> dialog = new ChoiceDialog<>(EntryType.Urlaub, EntryType.Urlaub, EntryType.Krankheit);
        dialog.setTitle("Neuer Eintrag");
        dialog.setHeaderText("Wählen Sie den Grund für Ihre Abwesenheit:");
        dialog.setContentText("Abwesenheitsgrund:");

        // Setze das Standard-Ikonen-Set auf null, um die Standard-Icons zu entfernen
        dialog.setGraphic(null);

        Optional<EntryType> result = dialog.showAndWait();
        result.ifPresent(entryType -> {
            if (entryType == EntryType.Urlaub && remainingVacationDays > 0) {
                entries.put(date, new Entry(EntryType.Urlaub));
                remainingVacationDays--;
            } else if (entryType == EntryType.Krankheit && remainingSicknessDays > 0) {
                entries.put(date, new Entry(EntryType.Krankheit));
                remainingSicknessDays--;
            } else {
                showAlert("Keine verbleibenden Tage", "Sie haben alle verbleibenden Tage dieses Typs verbraucht.");
            }
            updateCalendar();
        });
    }

    private void handleExistingEntry(LocalDate date) {
        Entry entry = entries.get(date);
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Bestehender Grund");
        dialog.setHeaderText("Aktion für den ausgewählten Tag wählen:");

        ButtonType showInfoButton = new ButtonType("Information anzeigen", ButtonBar.ButtonData.OK_DONE);
        ButtonType deleteButton = new ButtonType("Löschen", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().setAll(showInfoButton, deleteButton, cancelButton);

        // Setze das Standard-Ikonen-Set auf null, um die Standard-Icons zu entfernen
        dialog.setGraphic(null);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get() == showInfoButton) {
                showAlert("Grund Anzeigen", entry.getInfo());
            } else if (result.get() == deleteButton) {
                entries.remove(date);
                if (entry.getType() == EntryType.Urlaub) {
                    remainingVacationDays++;
                } else if (entry.getType() == EntryType.Krankheit) {
                    remainingSicknessDays++;
                }
                updateCalendar();
            }
        }
    }

    private DayOfWeek getFirstDayOfMonth(Month month, int year) {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        return firstDayOfMonth.getDayOfWeek();
    }

    private void updateCalendar() {
        Stage stage = (Stage) root.getScene().getWindow();
        root.getChildren().clear();

        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);

        HBox header = createHeader();
        gridPane = createCalendarGrid();

        content.getChildren().addAll(header, gridPane);
        root.getChildren().add(content);
    }

    private void showAlert(String title, String content) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(content);

        // Setze das Standard-Ikonen-Set auf null, um die Standard-Icons zu entfernen
        dialog.setGraphic(null);

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().setAll(okButton);

        dialog.showAndWait();
    }

    private record Entry(EntryType type) {
        public String getInfo() {
            return switch (type) {
                case Urlaub -> "Sie haben sich für diesen Tag beurlaubt.";
                case Krankheit -> "Sie sind für diesen Tag krank gemeldet.";
            };
        }

        public EntryType getType() {
            return type;
        }
    }

    private enum EntryType {
        Urlaub,
        Krankheit
    }
}
