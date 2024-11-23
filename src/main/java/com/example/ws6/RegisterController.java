package com.example.ws6;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username or password cannot be empty.");
            return;
        }

        if (registerUser(username, password)) {
            showAlert("Success", "Registration successful!");
            ((Stage) usernameField.getScene().getWindow()).close();
        } else {
            showAlert("Error", "Registration failed. Username might already exist.");
        }
    }

    // Method to register a user in the database
    public static boolean registerUser(String username, String password) {
        String sql = "INSERT INTO Users (username, password) VALUES (?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true; // Registration successful
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Registration failed: " + e.getMessage());
            return false; // Registration failed
        }
    }



    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
