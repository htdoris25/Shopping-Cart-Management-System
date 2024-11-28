package com.example.ws7;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class CartServer {
    private static final int PORT = 12345; // Port for the server
    private static final ConcurrentHashMap<String, Socket> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Starting the server...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                // Accept client connections
                Socket clientSocket = serverSocket.accept();
                String clientInfo = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
                System.out.println("Client connected: " + clientInfo);

                // Save client info
                clients.put(clientInfo, clientSocket);

                // Handle client in a new thread
                new Thread(new ClientHandler(clientSocket, clientInfo)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Thread to handle each client
    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final String clientInfo;

        public ClientHandler(Socket socket, String clientInfo) {
            this.socket = socket;
            this.clientInfo = clientInfo;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                out.println("Welcome to the Cart Server! You are connected as " + clientInfo);

                String input;
                while ((input = in.readLine()) != null) {
                    System.out.println("Message from " + clientInfo + ": " + input);

                    if (input.equalsIgnoreCase("exit")) {
                        out.println("Goodbye!");
                        break;
                    } else if (input.equalsIgnoreCase("cart")) {
                        out.println("Cart functionality not yet implemented.");
                    } else {
                        out.println("Unknown command: " + input);
                    }
                }
            } catch (IOException e) {
                System.err.println("Connection with client " + clientInfo + " lost.");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.remove(clientInfo);
                System.out.println("Client " + clientInfo + " disconnected.");
            }
        }
    }
}

