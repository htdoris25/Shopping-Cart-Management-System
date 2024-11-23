package com.example.ws6;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;

public class Cart {
    private final Model model = new Model();

    @FXML
    private ComboBox<Item> itemsComboBox;
    @FXML
    private Label unitValueLabel;
    @FXML
    private Label unitPriceValueLabel;
    @FXML
    private Slider quantitySlider;
    @FXML
    private Label purchasedUnitsLabel;
    @FXML
    private Label purchasePriceValueLabel;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button saveCartButton;
    @FXML
    private Button checkoutButton;
    @FXML
    private Button viewSavedCartsButton;
    @FXML
    private TableView<ItemInCart> cartTableView;
    @FXML
    private TableColumn<ItemInCart, String> nameColumn;
    @FXML
    private TableColumn<ItemInCart, Double> purchasedUnitsColumn;
    @FXML
    private TableColumn<ItemInCart, Double> purchasePriceColumn;
    @FXML
    private Label totalAmountLabel;

    private ObservableList<ItemInCart> cartObservableList = FXCollections.observableArrayList();
    private ObservableList<ObservableList<ItemInCart>> savedCarts = FXCollections.observableArrayList();

    public void initialize() {
        model.loadData();
        itemsComboBox.setItems(model.getItemsObservableList());
        itemsComboBox.getSelectionModel().clearSelection();

        itemsComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                unitValueLabel.setText(String.format("%.2f %s", newValue.getUnitQuantity(), newValue.getUnit()));
                unitPriceValueLabel.setText(String.format("$ %.2f", newValue.getUnitPrice()));
                updateTotalPrice();
            }
        });

        quantitySlider.setMin(1);
        quantitySlider.setMax(10);
        quantitySlider.setMajorTickUnit(1);
        quantitySlider.setMinorTickCount(0);
        quantitySlider.setSnapToTicks(true);

        quantitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            purchasedUnitsLabel.setText(String.valueOf(newValue.intValue()));
            updateTotalPrice();
        });

        setupTableView();
        setupButtons();
        setupTotalAmountBinding();
    }

    private void updateTotalPrice() {
        Item selectedItem = itemsComboBox.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            double totalPrice = selectedItem.getUnitPrice() * quantitySlider.getValue();
            purchasePriceValueLabel.setText(String.format("$ %.2f", totalPrice));
        }
    }

    private void setupTableView() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        purchasedUnitsColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        purchasePriceColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(
                () -> cellData.getValue().getQuantity() * model.getUnitPriceByName(cellData.getValue().getName()),
                cellData.getValue().quantityProperty()
        ));

        cartTableView.setItems(cartObservableList);
    }

    private void setupButtons() {
        addButton.setOnAction(event -> {
            Item selectedItem = itemsComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                double quantity = quantitySlider.getValue();
                cartObservableList.add(new ItemInCart(selectedItem.getName(), quantity));
                updateTotalPrice();
            }
        });

        removeButton.setOnAction(event -> {
            int selectedIndex = cartTableView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                cartObservableList.remove(selectedIndex);
            }
        });

        saveCartButton.setOnAction(event -> saveCart());
        checkoutButton.setOnAction(event -> checkoutCart());
        viewSavedCartsButton.setOnAction(event -> openSavedCartsWindow());
    }

    private void saveCart() {
        if (!cartObservableList.isEmpty()) {
            ObservableList<ItemInCart> cartCopy = FXCollections.observableArrayList(cartObservableList);
            savedCarts.add(cartCopy);
            saveCartToFile(cartCopy);
            cartObservableList.clear();
            updateTotalPrice();
            System.out.println("Cart saved.");
        }
    }

    private void saveCartToFile(ObservableList<ItemInCart> cart) {
        try (FileOutputStream fileOut = new FileOutputStream("cart" + savedCarts.size() + ".ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(new SavedCart(savedCarts.size(), cart, calculateCartTotal(cart)));
            System.out.println("Cart saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double calculateCartTotal(ObservableList<ItemInCart> cart) {
        return cart.stream()
                .mapToDouble(item -> item.getQuantity() * model.getUnitPriceByName(item.getName()))
                .sum();
    }

    private void checkoutCart() {
        if (!cartObservableList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Checkout Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you done with your grocery shopping?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                double total = cartObservableList.stream()
                        .mapToDouble(item -> item.getQuantity() * model.getUnitPriceByName(item.getName()))
                        .sum();
                System.out.println("Checked out with total: $" + total);
                cartObservableList.clear();
                updateTotalPrice();
            }
        }
    }

    private void openSavedCartsWindow() {
        System.out.println("Opening Saved Carts Window"); // Debug message
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("savedCarts.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Saved Carts");
            stage.setScene(new Scene(loader.load()));

            SavedCartsController controller = loader.getController();
            controller.setSavedCarts(convertToSavedCarts());
            controller.setCartObservableList(cartObservableList);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private ObservableList<SavedCart> convertToSavedCarts() {
        ObservableList<SavedCart> savedCartsList = FXCollections.observableArrayList();
        int cartNumber = 1;
        for (ObservableList<ItemInCart> cart : savedCarts) {
            double total = calculateCartTotal(cart);
            savedCartsList.add(new SavedCart(cartNumber++, cart, total));
        }
        return savedCartsList;
    }

    private void setupTotalAmountBinding() {
        ObjectBinding<Double> totalBinding = Bindings.createObjectBinding(() ->
                cartObservableList.stream()
                        .mapToDouble(item -> item.getQuantity() * model.getUnitPriceByName(item.getName()))
                        .sum(), cartObservableList);

        totalAmountLabel.textProperty().bind(totalBinding.asString("Total: $%.2f"));
    }
}
