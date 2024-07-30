package com.example.Controller;


import com.example.model.User;
import com.example.persistence.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminController {
    private Stage stage;
    private TableView<User> userTable;
    private ObservableList<User> userData;
    private UserDAO userDAO;

    public AdminController(Stage stage) {
        this.stage = stage;
        this.userDAO = new UserDAO();
        this.userData = FXCollections.observableArrayList(userDAO.getAllUsers());
    }

    public VBox createAdminView() {
        VBox root = new VBox(10);
        userTable = new TableView<>();
        userTable.setItems(userData);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());

        userTable.getColumns().addAll(usernameColumn, roleColumn);

        Button addButton = new Button("Agregar Usuario");
        addButton.setOnAction(event -> handleNewUser());

        Button editButton = new Button("Modificar Usuario");
        editButton.setOnAction(event -> handleEditUser());

        Button deleteButton = new Button("Eliminar Usuario");
        deleteButton.setOnAction(event -> handleDeleteUser());

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> handleBack());

        root.getChildren().addAll(userTable, addButton, editButton, deleteButton, backButton);

        return root;
    }

    private void handleNewUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Usuario");
        dialog.setHeaderText("Ingrese el nombre de usuario:");
        dialog.setContentText("Username:");
        dialog.showAndWait();
        String username = dialog.getResult();

        dialog.setHeaderText("Ingrese la contraseña:");
        dialog.setContentText("Password:");
        dialog.showAndWait();
        String password = dialog.getResult();

        dialog.setHeaderText("Ingrese el rol:");
        dialog.setContentText("Role:");
        dialog.showAndWait();
        String role = dialog.getResult();

        User user = new User(username, password, role);
        userDAO.addUser(user);

        userData.setAll(userDAO.getAllUsers());
    }

    private void handleEditUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Dialog<User> dialog = new Dialog<>();
            dialog.setTitle("Modificar Usuario");

            // Set the button types
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Create the username, password, and role fields
            TextField usernameField = new TextField();
            usernameField.setText(selectedUser.getUsername());
            PasswordField passwordField = new PasswordField();
            passwordField.setText(selectedUser.getPassword());
            TextField roleField = new TextField();
            roleField.setText(selectedUser.getRole());

            VBox vbox = new VBox();
            vbox.setSpacing(10);
            vbox.getChildren().addAll(
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                new Label("Role:"), roleField
            );
            dialog.getDialogPane().setContent(vbox);

            // Convert the result to a user object when the login button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    selectedUser.setUsername(usernameField.getText());
                    selectedUser.setPassword(passwordField.getText());
                    selectedUser.setRole(roleField.getText());
                    return selectedUser;
                }
                return null;
            });

            dialog.showAndWait().ifPresent(user -> {
                userDAO.updateUser(user);
                userData.setAll(userDAO.getAllUsers());
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(stage);
            alert.setTitle("No Selection");
            alert.setHeaderText("No User Selected");
            alert.setContentText("Please select a user in the table.");
            alert.showAndWait();
        }
    }

    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userDAO.deleteUser(selectedUser);
            userData.remove(selectedUser);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(stage);
            alert.setTitle("No Selection");
            alert.setHeaderText("No User Selected");
            alert.setContentText("Please select a user in the table.");
            alert.showAndWait();
        }
    }

    private void handleBack() {
        MainController mainController = new MainController(stage);
        VBox mainView = mainController.createMainView();
        Scene scene = new Scene(mainView, 800, 600);
        stage.setScene(scene);
    }
}
