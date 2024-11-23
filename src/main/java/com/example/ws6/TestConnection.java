package com.example.ws6;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection conn = Database.connect()) {
            if (conn != null) {
                System.out.println("Connection to SQLite database successful!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
