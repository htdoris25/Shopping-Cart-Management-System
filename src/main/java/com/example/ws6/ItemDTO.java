package com.example.ws6;

public class ItemDTO {
    private String name;
    private double quantity;

    public ItemDTO(String name, double quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }
}
