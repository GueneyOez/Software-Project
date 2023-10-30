package com.example.zeitmanagement_login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;


public class Controller {

    public Button loginButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;

    public void loginButtonAction() {

        if (!usernameTextField.getText().isBlank() && !passwordPasswordField.getText().isBlank()) {
            loginMessageLabel.setText("Invalid username or password!");
        } else {
            loginMessageLabel.setText("Please enter username and password.");
        }
    }

    public void cancelButtonAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}