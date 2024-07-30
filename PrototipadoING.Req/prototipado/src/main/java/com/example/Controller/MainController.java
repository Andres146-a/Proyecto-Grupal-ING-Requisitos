package com.example.Controller;



import com.example.util.CurrentUser;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {
    private Stage stage;

    public MainController(Stage stage) {
        this.stage = stage;
    }

    public VBox createMainView() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label welcomeLabel = new Label("Welcome, " + CurrentUser.getRole());
        root.getChildren().add(welcomeLabel);

        String role = CurrentUser.getRole();

        if ("admin".equals(role)) {
            Button manageUsersButton = new Button("Gestionar Usuarios");
            manageUsersButton.setOnAction(event -> openAdminView());
            root.getChildren().add(manageUsersButton);
        }

        if ("admin".equals(role) || "receptionist".equals(role)) {
            Button manageOrdersButton = new Button("Gestionar Pedidos");
            manageOrdersButton.setOnAction(event -> openReceptionistView());
            root.getChildren().add(manageOrdersButton);
        }

        if ("admin".equals(role) || "chef".equals(role)) {
            Button manageMenuButton = new Button("Gestionar Menú");
            manageMenuButton.setOnAction(event -> openChefView());
            root.getChildren().add(manageMenuButton);
        }

        Button logoutButton = new Button("Cerrar Sesión");
        logoutButton.setOnAction(event -> logout());
        root.getChildren().add(logoutButton);

        return root;
    }

    private void openAdminView() {
        AdminController adminController = new AdminController(stage);
        VBox adminView = adminController.createAdminView();
        Scene scene = new Scene(adminView, 800, 600);
        stage.setScene(scene);
    }

    private void openReceptionistView() {
        ReceptionistController receptionistController = new ReceptionistController(stage);
        VBox receptionistView = receptionistController.createReceptionistView();
        Scene scene = new Scene(receptionistView, 800, 600);
        stage.setScene(scene);
    }

    private void openChefView() {
        ChefController chefController = new ChefController(stage);
        VBox chefView = chefController.createChefView();
        Scene scene = new Scene(chefView, 800, 600);
        stage.setScene(scene);
    }

    private void logout() {
        LoginController loginController = new LoginController(stage);
        VBox loginView = loginController.createLoginView();
        Scene scene = new Scene(loginView, 400, 300);
        stage.setScene(scene);
    }
}
