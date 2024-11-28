// View
package com.example.ws7;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class View {
    @FXML
    private ComboBox<Item> itemsComboBox;
    @FXML
    private Label unitValueLabel;
    @FXML
    private Label unitPriceValueLabel;
    @FXML
    private Slider quantitySlider;
    @FXML
    private Label purchaseQuantityValueLabel;
    @FXML
    private Label purchasePriceValueLabel;

    public ComboBox<Item> getItemsComboBox() {
        return itemsComboBox;
    }

    public Label getUnitValueLabel() {
        return unitValueLabel;
    }

    public Label getUnitPriceValueLabel() {
        return unitPriceValueLabel;
    }

    public Slider getQuantitySlider() {
        return quantitySlider;
    }

    public Label getPurchaseQuantityValueLabel() {
        return purchaseQuantityValueLabel;
    }

    public Label getPurchasePriceValueLabel() {
        return purchasePriceValueLabel;
    }
}

