package com.example.Controller;


import com.example.model.User;
import com.example.model.pedido;
import com.example.persistence.UserDAO;
import com.example.persistence.pedidoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class PedidoDialog {

    private Stage dialogStage;
    private boolean saveClicked = false;

    private ComboBox<User> clienteComboBox;
    private DatePicker fechaDatePicker;
    private ComboBox<String> estadoComboBox;

    private pedido pedido;

    public PedidoDialog() {
        dialogStage = new Stage();
        dialogStage.setTitle("Nuevo Pedido");
        dialogStage.initModality(Modality.WINDOW_MODAL);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        Label clienteLabel = new Label("Cliente:");
        grid.add(clienteLabel, 0, 0);
        clienteComboBox = new ComboBox<>();
        grid.add(clienteComboBox, 1, 0);

        Label fechaLabel = new Label("Fecha:");
        grid.add(fechaLabel, 0, 1);
        fechaDatePicker = new DatePicker();
        grid.add(fechaDatePicker, 1, 1);

        Label estadoLabel = new Label("Estado:");
        grid.add(estadoLabel, 0, 2);
        estadoComboBox = new ComboBox<>();
        estadoComboBox.setItems(FXCollections.observableArrayList("Activo", "Inactivo"));
        grid.add(estadoComboBox, 1, 2);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> handleSave());
        grid.add(saveButton, 0, 3);

        Button cancelButton = new Button("Cancelar");
        cancelButton.setOnAction(e -> handleCancel());
        grid.add(cancelButton, 1, 3);

        Scene scene = new Scene(grid);
        dialogStage.setScene(scene);

        UserDAO userDAO = new UserDAO();
        List<User> clientes = userDAO.getAllUsers();
        ObservableList<User> clienteList = FXCollections.observableArrayList(clientes);
        clienteComboBox.setItems(clienteList);

        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

    public void showAndWait() {
        dialogStage.showAndWait();
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public pedido getPedido() {
        return pedido;
    }

    private void handleSave() {
        if (isInputValid()) {
            pedido = new pedido();
            pedido.setCliente(clienteComboBox.getValue().getUsername());
            pedido.setFecha(fechaDatePicker.getValue().toString());
            pedido.setEstado(estadoComboBox.getValue());

            pedidoDAO pedidoDAO = new pedidoDAO();
            pedidoDAO.addPedido(pedido);

            saveClicked = true;
            dialogStage.close();
        }
    }

    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (clienteComboBox.getValue() == null) {
            errorMessage += "No valid client!\n";
        }
        if (fechaDatePicker.getValue() == null) {
            errorMessage += "No valid date!\n";
        }
        if (estadoComboBox.getValue() == null) {
            errorMessage += "No valid status!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
