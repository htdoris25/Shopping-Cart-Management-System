package com.example.ws7;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Socket serverSocket; // Server socket to communicate with the server

    /**
     * Sets the server socket for communication.
     *
     * @param serverSocket the server socket
     */
    public void setServerSocket(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

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

            // Notify the server about successful login
            sendLoginToServer(username, password);

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
     * Sends login information to the server.
     *
     * @param username The entered username.
     * @param password The entered password.
     */
    private void sendLoginToServer(String username, String password) {
        if (serverSocket == null) {
            showAlert("Error", "Server connection is not available.");
            return;
        }
        try {
            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
            out.println("LOGIN " + username + " " + password);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to communicate with the server.");
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
