module com.example.trying_to_push_to_git {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.trying_to_push_to_git to javafx.fxml;
    exports com.example.trying_to_push_to_git;
}