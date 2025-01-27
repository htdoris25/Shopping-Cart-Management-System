# Grocery Store Management Application


## Overview
The Grocery Store Management Application is a JavaFX-based desktop application designed to simulate a grocery store's cart and checkout system. This project demonstrates the use of JavaFX for building user interfaces, SQLite for database management, and JSON (via Gson) for data serialization and deserialization. The application provides a seamless user experience for managing grocery carts, saving and loading cart data, and handling user authentication.

## Features
User Authentication: 
A simple login and registration system using SQLite for user data storage.
User credentials (username and password) are securely stored in a local SQLite database. 

* **Cart Management**:
Users can add items to their cart, specify quantities, and view the total price dynamically.
Saved carts are automatically loaded from the database upon selection and saved after modifications.

* **Database Integration**:
SQLite is used to store user credentials and saved cart data persistently.
Automatic serialization of cart data to JSON format for efficient database storage.

* **Intuitive User Interface**: 
Designed with JavaFX and SceneBuilder for a responsive and user-friendly interface.
Custom styling using a CSS file for a professional and modern look.

* **Data Serialization**:
Uses Gson for converting cart items between Java objects and JSON for efficient storage.

* **Error Handling**:
Comprehensive error handling and alerts to ensure smooth user interaction and debugging.

## Technology Stack
* JavaFX: For building the graphical user interface (GUI). 
* SQLite: For persistent data storage. 
* Gson: For JSON serialization and deserialization. 
* Java 17+: To leverage modern Java features. 
* CSS: For styling the JavaFX application. 
