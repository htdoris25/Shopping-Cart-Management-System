package com.example.ws7;

import javafx.collections.ObservableList;

public class SavedCart {
    private final int cartNumber;
    private final ObservableList<ItemInCart> items;
    private final double price;

    public SavedCart(int cartNumber, ObservableList<ItemInCart> items, double price) {
        this.cartNumber = cartNumber;
        this.items = items;
        this.price = price;
    }

    public int getCartNumber() {
        return cartNumber;
    }

    public ObservableList<ItemInCart> getItems() {
        return items;
    }

    public double getPrice() {
        return price;
    }
}
