package com.example.Controller;


import com.example.model.menu;
import com.example.persistence.menuDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChefController {
    private Stage stage;
    private TableView<menu> menuTable;
    private ObservableList<menu> menuData;
    private menuDAO menuDAO;

    public ChefController(Stage stage) {
        this.stage = stage;
        this.menuDAO = new menuDAO();
        this.menuData = FXCollections.observableArrayList(menuDAO.getAllMenus());
    }

    public VBox createChefView() {
        VBox root = new VBox(10);
        menuTable = new TableView<>();
        menuTable.setItems(menuData);
        menuTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<menu, String> nameColumn = new TableColumn<>("Nombre");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());

        TableColumn<menu, String> descriptionColumn = new TableColumn<>("Descripción");
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());

        TableColumn<menu, Number> priceColumn = new TableColumn<>("Precio");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().precioProperty());

        menuTable.getColumns().addAll(nameColumn, descriptionColumn, priceColumn);

        Button addButton = new Button("Agregar Menú");
        addButton.setOnAction(event -> handleNewMenu());

        Button editButton = new Button("Modificar Menú");
        editButton.setOnAction(event -> handleEditMenu());

        Button deleteButton = new Button("Eliminar Menú");
        deleteButton.setOnAction(event -> handleDeleteMenu());

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> handleBack());

        root.getChildren().addAll(menuTable, addButton, editButton, deleteButton, backButton);

        return root;
    }

    private void handleNewMenu() {
        menu newMenu = new menu();
        boolean okClicked = showMenuEditDialog(newMenu);
        if (okClicked) {
            menuDAO.addMenu(newMenu);
            menuData.add(newMenu);
        }
    }

    private void handleEditMenu() {
        menu selectedMenu = menuTable.getSelectionModel().getSelectedItem();
        if (selectedMenu != null) {
            boolean okClicked = showMenuEditDialog(selectedMenu);
            if (okClicked) {
                menuDAO.updateMenu(selectedMenu);
                menuData.set(menuTable.getSelectionModel().getSelectedIndex(), selectedMenu);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(stage);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Menu Selected");
            alert.setContentText("Please select a menu item in the table.");
            alert.showAndWait();
        }
    }

    private void handleDeleteMenu() {
        menu selectedMenu = menuTable.getSelectionModel().getSelectedItem();
        if (selectedMenu != null) {
            menuDAO.deleteMenu(selectedMenu.getId());
            menuData.remove(selectedMenu);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(stage);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Menu Selected");
            alert.setContentText("Please select a menu in the table.");
            alert.showAndWait();
        }
    }

    private void handleBack() {
        MainController mainController = new MainController(stage);
        VBox mainView = mainController.createMainView();
        Scene scene = new Scene(mainView, 800, 600);
        stage.setScene(scene);
    }

    private boolean showMenuEditDialog(menu m) {
        // Implementar la lógica para mostrar el cuadro de diálogo de edición de menú
        // Devolver true si se hizo clic en OK, false si se hizo clic en Cancelar
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(stage);
        dialog.setTitle("Editar Menú");
        dialog.setHeaderText("Modifique los detalles del menú:");

        ButtonType saveButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        nameField.setText(m.getNombre());
        TextField descriptionField = new TextField();
        descriptionField.setText(m.getDescripcion());
        TextField priceField = new TextField();
        priceField.setText(Double.toString(m.getPrecio()));

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().addAll(
                new Label("Nombre:"), nameField,
                new Label("Descripción:"), descriptionField,
                new Label("Precio:"), priceField
        );
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                m.setNombre(nameField.getText());
                m.setDescripcion(descriptionField.getText());
                m.setPrecio(Double.parseDouble(priceField.getText()));
                return ButtonType.OK;
            }
            return null;
        });

        return dialog.showAndWait().isPresent();
    }
}
