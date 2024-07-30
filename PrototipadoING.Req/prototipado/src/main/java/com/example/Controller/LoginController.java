package com.example.Controller;




import com.example.util.Utils;
import com.example.util.CurrentUser;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController {
    private Stage stage;

    public LoginController(Stage stage) {
        this.stage = stage;
    }

    public VBox createLoginView() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Label errorLabel = new Label();

        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (Utils.authenticateUser(username, password)) {
                String role = CurrentUser.getRole();
                switch (role) {
                    case "admin":
                        AdminController adminController = new AdminController(stage);
                        VBox adminView = adminController.createAdminView();
                        Scene adminScene = new Scene(adminView, 800, 600);
                        stage.setScene(adminScene);
                        break;
                    case "receptionist":
                        ReceptionistController receptionistController = new ReceptionistController(stage);
                        VBox receptionistView = receptionistController.createReceptionistView();
                        Scene receptionistScene = new Scene(receptionistView, 800, 600);
                        stage.setScene(receptionistScene);
                        break;
                    case "chef":
                        ChefController chefController = new ChefController(stage);
                        VBox chefView = chefController.createChefView();
                        Scene chefScene = new Scene(chefView, 800, 600);
                        stage.setScene(chefScene);
                        break;
                    default:
                        errorLabel.setText("Access Denied. Invalid role.");
                        break;
                }
            } else {
                errorLabel.setText("Authentication Failed. Invalid username or password.");
            }
        });

        root.getChildren().addAll(
                new Label("Username"), usernameField,
                new Label("Password"), passwordField,
                loginButton, errorLabel
        );

        return root;
    }
}
