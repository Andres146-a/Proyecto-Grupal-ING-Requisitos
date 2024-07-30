package com.example.persistence;
import com.example.model.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class menuDAO {

    public List<menu> getAllMenus() {
        List<menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM menu";

        try (Connection connection = DataBaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                menu m = new menu(
                    resultSet.getInt("id"),
                    resultSet.getString("nombre"),
                    resultSet.getString("descripcion"),
                    resultSet.getDouble("precio")
                );
                menus.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menus;
    }

    public void addMenu(menu m) {
        String sql = "INSERT INTO menu (nombre, descripcion, precio) VALUES (?, ?, ?)";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, m.getNombre());
            statement.setString(2, m.getDescripcion());
            statement.setDouble(3, m.getPrecio());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    m.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMenu(menu m) {
        String sql = "UPDATE menu SET nombre = ?, descripcion = ?, precio = ? WHERE id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, m.getNombre());
            statement.setString(2, m.getDescripcion());
            statement.setDouble(3, m.getPrecio());
            statement.setInt(4, m.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMenu(int id) {
        String sql = "DELETE FROM menu WHERE id = ?";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
