//SavedCartController
package com.example.ws6;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;
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

    @FXML
    private Button checkoutButton;


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
        System.out.println("Load Cart button clicked.");
        SavedCart selectedCart = savedCartsTable.getSelectionModel().getSelectedItem();
        if (selectedCart != null) {
            System.out.println("Selected Cart: " + selectedCart.getCartNumber());
            try {
                cartObservableList.clear();
                cartObservableList.addAll(selectedCart.getItems());
                System.out.println("Cart loaded successfully: " + selectedCart.getItems());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No cart selected.");
        }
    }


    @FXML
    private void saveCurrentCart() {
        int userId = 1; // Replace with dynamic user logic
        try {
            saveCartToDatabase(userId, new ArrayList<>(cartObservableList));
            showAlert("Success", "Cart saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save the cart.");
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
    private void deleteCartsFromDatabase(List<SavedCart> cartsToDelete) {
        String sql = "DELETE FROM Carts WHERE cart_number = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (SavedCart cart : cartsToDelete) {
                stmt.setInt(1, cart.getCartNumber());
                stmt.executeUpdate();
            }
            System.out.println("Carts deleted from database.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete carts from database.");
        }
    }

    @FXML
    private void checkoutSelectedCarts() {
        ObservableList<SavedCart> selectedCarts = savedCartsTable.getSelectionModel().getSelectedItems();

        if (selectedCarts.isEmpty()) {
            showAlert("Error", "No carts selected. Please select one or more carts to checkout.");
            return;
        }

        // Confirmation Alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Checkout");
        confirmationAlert.setHeaderText("Are you sure you want to check out?");
        confirmationAlert.setContentText("This action cannot be undone.");

        // Wait for user response
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Proceed with checkout
            StringBuilder cartDetails = new StringBuilder("You have selected the following carts for checkout:\n");
            double totalAmount = 0.0;

            for (SavedCart cart : selectedCarts) {
                cartDetails.append("Cart Number: ").append(cart.getCartNumber())
                        .append(", Total Price: $").append(cart.getPrice()).append("\n");
                totalAmount += cart.getPrice();
            }

            cartDetails.append("\nTotal Amount: $").append(totalAmount);

            // Show checkout details
            showAlert("Checkout", cartDetails.toString());

            // Remove the checked-out carts
            savedCarts.removeAll(selectedCarts); // This updates the table as well
            deleteCartsFromDatabase(new ArrayList<>(selectedCarts)); // delete the cart from dat base.
            System.out.println("Checked-out carts removed from the table.");
        } else {
            // Cancel action
            showAlert("Cancelled", "Checkout cancelled.");
        }
    }


}
