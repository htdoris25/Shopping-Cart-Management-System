package com.example.ws6;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    /**
     * Handles the login button action.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
            return;
        }

        if (validateLogin(username, password)) {
            showAlert("Success", "Login successful!");
            ((Stage) usernameField.getScene().getWindow()).close();

            // Transition to the shopping cart window
            try {
                GroceryApp.showCartWindow();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load the shopping cart window.");
            }
        } else {
            showAlert("Error", "Invalid username or password!");
        }
    }

    /**
     * Validates the user's credentials against the database.
     *
     * @param username The entered username.
     * @param password The entered password.
     * @return True if credentials are valid, false otherwise.
     */
    public static boolean validateLogin(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If a result exists, login is valid
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Shows an alert dialog with a specified title and message.
     *
     * @param title   The title of the alert dialog.
     * @param message The message to display in the alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Opens the registration window.
     */
    @FXML
    private void openRegisterWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Register");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the registration window.");
        }
    }
}
