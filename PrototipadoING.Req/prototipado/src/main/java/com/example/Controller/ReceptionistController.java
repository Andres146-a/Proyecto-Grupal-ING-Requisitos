package com.example.Controller;
 
import com.example.model.User;
import com.example.model.pedido;
import com.example.persistence.pedidoDAO;
import com.example.persistence.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReceptionistController {
    private Stage stage;
    private TableView<pedido> orderTable;
    private ObservableList<pedido> orderData;
    private pedidoDAO pedidoDAO;

    public ReceptionistController(Stage stage) {
        this.stage = stage;
        this.pedidoDAO = new pedidoDAO();
        this.orderData = FXCollections.observableArrayList(pedidoDAO.getAllOrders());
    }

    public VBox createReceptionistView() {
        VBox root = new VBox(10);
        orderTable = new TableView<>();
        orderTable.setItems(orderData);
        orderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<pedido, String> clientColumn = new TableColumn<>("Cliente");
        clientColumn.setCellValueFactory(cellData -> cellData.getValue().clienteProperty());

        TableColumn<pedido, String> dateColumn = new TableColumn<>("Fecha");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());

        TableColumn<pedido, String> statusColumn = new TableColumn<>("Estado");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().estadoProperty());

        orderTable.getColumns().addAll(clientColumn, dateColumn, statusColumn);

        Button addButton = new Button("Agregar Pedido");
        addButton.setOnAction(event -> handleNewOrder());

        Button editButton = new Button("Modificar Pedido");
        editButton.setOnAction(event -> handleEditOrder());

        Button deleteButton = new Button("Eliminar Pedido");
        deleteButton.setOnAction(event -> handleDeleteOrder());

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> handleBack());

        root.getChildren().addAll(orderTable, addButton, editButton, deleteButton, backButton);

        return root;
    }

    private void handleNewOrder() {
        PedidoDialog dialog = new PedidoDialog();
        dialog.showAndWait();
        if (dialog.isSaveClicked()) {
            pedido newPedido = dialog.getPedido();
            pedidoDAO.addPedido(newPedido);
            orderData.add(newPedido);
        }
    }

    private void handleEditOrder() {
        pedido selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            PedidoDialog dialog = new PedidoDialog();
            dialog.showAndWait();
            if (dialog.isSaveClicked()) {
                pedido updatedPedido = dialog.getPedido();
                pedidoDAO.updatePedido(updatedPedido);
                orderData.set(orderTable.getSelectionModel().getSelectedIndex(), updatedPedido);
            }
        } else {
            showAlert("No Selection", "No Order Selected", "Please select an order in the table.");
        }
    }

    private void handleDeleteOrder() {
        pedido selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            pedidoDAO.deletePedido(selectedOrder.getId());
            orderData.remove(selectedOrder);
        } else {
            showAlert("No Selection", "No Order Selected", "Please select an order in the table.");
        }
    }

    private void handleBack() {
        MainController mainController = new MainController(stage);
        VBox mainView = mainController.createMainView();
        Scene scene = new Scene(mainView, 800, 600);
        stage.setScene(scene);
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
