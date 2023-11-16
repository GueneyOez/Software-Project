package com.example.timemanagementapp;

        import javafx.collections.FXCollections;
        import javafx.geometry.Insets;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.control.ComboBox;
        import javafx.scene.control.Label;
        import javafx.scene.control.PasswordField;
        import javafx.scene.control.TextField;
        import javafx.scene.layout.GridPane;
        import javafx.stage.Stage;

        import static com.example.timemanagementapp.MongoConnection.createEmployee;
        import static com.example.timemanagementapp.MongoConnection.insertEmployee;

public class AddEmployeeScene {
    private Stage stage;
    private HomeScreen homeScreen;
    public AddEmployeeScene(Stage stage, HomeScreen homeScreen) {
        this.stage = stage;
        this.homeScreen = homeScreen;
    }

    public void show() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        // Label und Felder für die Eingabe
        Label roleLabel = new Label("Rolle:");
        ComboBox<String> roleComboBox = new ComboBox<>(FXCollections.observableArrayList("Mitarbeiter", "Manager", "Freiberufler"));
        roleComboBox.setValue("Mitarbeiter");

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label passwordLabel = new Label("Passwort:");
        PasswordField passwordField = new PasswordField();

        // Button zum Hinzufügen des Mitarbeiters
        Button addButton = new Button("Mitarbeiter hinzufügen");
        addButton.setOnAction(e -> {
            insertEmployee(createEmployee(roleComboBox.getValue(), nameField.getText(), passwordField.getText()));
            System.out.println("Rolle: " + roleComboBox.getValue());
            System.out.println("Name: " + nameField.getText());
            System.out.println("Passwort: " + passwordField.getText());
        });

        // Zurück-Button
        Button backButton = new Button("Zurück");
        backButton.setOnAction(e -> homeScreen.goBack());

        // Hinzufügen von Elementen zum GridPane
        gridPane.add(roleLabel, 0, 0);
        gridPane.add(roleComboBox, 1, 0);
        gridPane.add(nameLabel, 0, 1);
        gridPane.add(nameField, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(addButton, 0, 3);
        gridPane.add(backButton, 1, 3);

        Scene scene = new Scene(gridPane, 400, 200);
        stage.setScene(scene);
        stage.setTitle("Mitarbeiter hinzufügen");
        stage.show();
    }
}
