package com.example.ws7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class GroceryApp extends Application {

    private static Socket serverSocket;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);

        // Pass the server socket to the login controller
        LoginController loginController = loader.getController();
        loginController.setServerSocket(serverSocket);

        primaryStage.show();
    }

    public static void showCartWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(GroceryApp.class.getResource("/com/example/ws7/view.fxml"));
        Scene scene = new Scene(loader.load());

        Stage stage = new Stage();
        stage.setTitle("Shopping Cart");
        stage.setScene(scene);

        // Pass the server socket to the cart controller
        Cart cartController = loader.getController();
        cartController.setServerSocket(serverSocket);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
