package com.example.ws7;

import java.io.*;
import java.net.*;

public class CartClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connected to the server at " + socket.getInetAddress() + ":" + socket.getPort());

            // Create a new thread to read messages from the server
            new Thread(() -> {
                String response;
                try {
                    while ((response = in.readLine()) != null) {
                        System.out.println("Server: " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Send login details
            out.println("LOGIN Doris 1234567");

            // Launch GUI (GroceryApp)
            GroceryApp.main(new String[0]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

