package com.example.persistence;

import com.example.model.pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class pedidoDAO {

    public List<pedido> getAllOrders() {
        List<pedido> orders = new ArrayList<>();
        try (Connection connection = DataBaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM pedido")) {

            while (resultSet.next()) {
                pedido p = new pedido(
                    resultSet.getInt("id"),
                    resultSet.getString("cliente"),
                    resultSet.getString("fecha"),
                    resultSet.getString("estado")
                );
                orders.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void addPedido(pedido p) {
        String sql = "INSERT INTO pedido (cliente, fecha, estado) VALUES (?, ?, ?)";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, p.getCliente());
            statement.setString(2, p.getFecha());
            statement.setString(3, p.getEstado());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    p.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePedido(pedido p) {
        String sql = "UPDATE pedido SET cliente = ?, fecha = ?, estado = ? WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, p.getCliente());
            statement.setString(2, p.getFecha());
            statement.setString(3, p.getEstado());
            statement.setInt(4, p.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePedido(int id) {
        String sql = "DELETE FROM pedido WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
