package com.example;


import com.example.util.Utils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.Controller.LoginController;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize the database
        Utils.initializeDatabase();

        // Load the login view
        LoginController loginController = new LoginController(primaryStage);
        VBox loginView = loginController.createLoginView();
        Scene scene = new Scene(loginView, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Restaurant Management - Login");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
