package controller;

import com.example.timemanagementapp.HomeScreen;
import com.example.timemanagementapp.LoginScreen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;

import java.util.List;

public class LoginController {
    @FXML
    private Button loginButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField usernameField; // Stelle sicher, dass dies mit dem fx:id in der FXML-Datei übereinstimmt

    @FXML
    private PasswordField passwordField;

    private Stage stage;
    private List<Document> employees;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setEmployees(List<Document> employees) {
        this.employees = employees;
    }

    @FXML
    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Document authenticatedEmployee = validateLogin(username, password);

        if (authenticatedEmployee != null) {
            // Wenn die Anmeldedaten korrekt sind, wechsle zu HomeScreen
            HomeScreen homeScreen = new HomeScreen(stage, authenticatedEmployee);
            homeScreen.show();
        } else {
            // Zeige eine Fehlermeldung für eine falsche Anmeldung
            System.out.println("Invalid username or password!");
        }
    }

    @FXML
    private void cancel() {
        // Logik für den Cancel-Button
        stage.close();
    }

    // Eine einfache Authentifizierungslogik
    private Document validateLogin(String username, String password) {
        Document authenticatedEmployee = null;
        for (Document employee : employees) {
            if (employee.getString("Name").equals(username) && employee.getString("Password").equals(password)) {
                authenticatedEmployee = employee;
                break; // Exit the loop on successful login
            }
        }
        return authenticatedEmployee;
    }

    public void setHomeScreen(LoginScreen loginScreen) {
    }
}
