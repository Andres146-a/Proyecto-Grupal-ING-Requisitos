package com.example.util;



import com.example.persistence.DataBaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Utils {

    public static void initializeDatabase() {
        try (Connection connection = DataBaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            // Crear tablas si no existen
            statement.execute("CREATE TABLE IF NOT EXISTS menu (" +
                    "id SERIAL PRIMARY KEY," +
                    "nombre VARCHAR(100)," +
                    "descripcion TEXT," +
                    "precio DECIMAL" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS pedido (" +
                    "id SERIAL PRIMARY KEY," +
                    "cliente VARCHAR(100)," +
                    "fecha TIMESTAMP," +
                    "estado VARCHAR(50)" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY," +
                    "username VARCHAR(50) UNIQUE," +
                    "password VARCHAR(50)," +
                    "role VARCHAR(20)" + // 'admin', 'receptionist', 'chef', 'client'
                    ")");

            // Crear tabla para almacenar IDs eliminados
            statement.execute("CREATE TABLE IF NOT EXISTS deleted_ids (" +
                    "id INT PRIMARY KEY" +
                    ")");

            // Agregar usuarios con diferentes roles
            String[] users = {
                "INSERT INTO users (username, password, role) VALUES ('admin', 'admin', 'admin')",
                "INSERT INTO users (username, password, role) VALUES ('receptionist', 'receptionist', 'receptionist')",
                "INSERT INTO users (username, password, role) VALUES ('chef', 'chef', 'chef')",
                "INSERT INTO users (username, password, role) VALUES ('client', 'client', 'client')"
            };

            for (String user : users) {
                try {
                    statement.execute(user);
                } catch (SQLException e) {
                    // Ignorar errores de inserción (por ejemplo, si el usuario ya existe)
                }
            }

            // Resetear la secuencia después de agregar los usuarios
            resetSequence();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void resetSequence() {
        try (Connection connection = DataBaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            // Set the sequence to the maximum current ID in the users table
            statement.execute("SELECT setval('users_id_seq', (SELECT COALESCE(MAX(id), 1) FROM users))");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void adjustSequenceAfterDeletion() {
        try (Connection connection = DataBaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            // Adjust the sequence to the maximum current ID in the users table
            statement.execute("SELECT setval('users_id_seq', (SELECT COALESCE(MAX(id), 1) FROM users))");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void adjustSequenceAfterInsertion() {
        try (Connection connection = DataBaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            // Adjust the sequence to the maximum current ID in the users table
            statement.execute("SELECT setval('users_id_seq', (SELECT MAX(id) FROM users) + 1)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean authenticateUser(String username, String password) {
        try (Connection connection = DataBaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'");
            if (rs.next()) {
                String role = rs.getString("role");
                CurrentUser.setRole(role); // Set the role of the authenticated user
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
