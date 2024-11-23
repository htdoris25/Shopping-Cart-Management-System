//SavedCartController
package com.example.ws6;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class SavedCartsController {
    @FXML
    private TableView<SavedCart> savedCartsTable;

    @FXML
    private TableColumn<SavedCart, Integer> cartNumberColumn;


    @FXML
    private TableColumn<SavedCart, Double> priceColumn;

    @FXML
    private Button loadCartButton;


    private final ObservableList<SavedCart> savedCarts = FXCollections.observableArrayList();
    private ObservableList<ItemInCart> cartObservableList; // Reference to main cart's observable list

    public void initialize() {
        cartNumberColumn.setCellValueFactory(new PropertyValueFactory<>("cartNumber"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        savedCartsTable.setItems(savedCarts);

        loadCartButton.setOnAction(event -> loadSelectedCartFromDatabase());
    }

    public void setCartObservableList(ObservableList<ItemInCart> cartObservableList) {
        this.cartObservableList = cartObservableList;
    }

    //Save the cart to the database.
    public static void saveCartToDatabase(int userId, List<ItemInCart> cartItems) {
        String sql = "INSERT INTO Carts (user_id, cart_data) VALUES (?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String serializedData = serializeCartItems(cartItems);
            stmt.setInt(1, userId);
            stmt.setString(2, serializedData);
            stmt.executeUpdate();
            showAlert("Success", "Cart saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save cart: " + e.getMessage());
        }
    }
    //Load a selected cart from the database and populate the main cart.
    @FXML
    private void loadSelectedCartFromDatabase() {
        SavedCart selectedCart = savedCartsTable.getSelectionModel().getSelectedItem();
        if (selectedCart != null) {
            System.out.println("Selected Cart: " + selectedCart.getCartNumber());
            try {
                // Load the selected cart into the observable list
                cartObservableList.setAll(selectedCart.getItems());

                // Automatically save the cart to the database
                int userId = 1; // Replace with your logic to retrieve the current user ID
                List<ItemInCart> cartItems = new ArrayList<>(cartObservableList);
                saveCartToDatabase(userId, cartItems);

                showAlert("Success", "Cart loaded and saved successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load and save the cart.");
            }
        } else {
            System.out.println("No cart selected.");
            showAlert("Error", "No cart selected. Please select a cart to load.");
        }
    }

    /**
     * Serialize cart items into a JSON string.
     */
    private static String serializeCartItems(List<ItemInCart> cartItems) {
        Gson gson = new Gson();
        List<ItemDTO> dtoList = cartItems.stream()
                .map(ItemInCart::toDTO)
                .toList();
        return gson.toJson(dtoList);
    }


    /**
     * Deserialize cart items from a JSON string.
     */
    private static ObservableList<ItemInCart> deserializeCartItems(String cartData) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ItemDTO>>() {}.getType();
        List<ItemDTO> dtoList = gson.fromJson(cartData, listType);

        // Convert ItemDTO back to ItemInCart
        return FXCollections.observableArrayList(dtoList.stream().map(ItemInCart::fromDTO).toList());
    }

    /**
     * Calculate total price of items in the cart.
     */
    private double calculateTotalPrice(List<ItemInCart> cartItems) {
        return cartItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();
    }


    /**
     * Show an alert message.
     */
    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void setSavedCarts(ObservableList<SavedCart> carts) {
        savedCarts.setAll(carts);
    }
}
