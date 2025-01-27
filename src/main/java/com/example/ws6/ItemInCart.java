package com.example.ws6;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ItemInCart {
    private final StringProperty name;
    private final DoubleProperty quantity;
    private final DoubleProperty unitPrice;

    public ItemInCart(String name, double quantity) {
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleDoubleProperty(quantity);
        this.unitPrice = new SimpleDoubleProperty(10.0); // Default price, update as needed
    }
    public static ItemInCart fromDTO(ItemDTO dto) {
        return new ItemInCart(dto.getName(), dto.getQuantity());
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public double getQuantity() {
        return quantity.get();
    }

    public DoubleProperty quantityProperty() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice.get();
    }

    public DoubleProperty unitPriceProperty() {
        return unitPrice;
    }

    public void setUnitPrice(double price) {
        this.unitPrice.set(price);
    }

    // Add this method to convert ItemInCart to ItemDTO
    public ItemDTO toDTO() {
        return new ItemDTO(getName(), getQuantity());
    }
}
