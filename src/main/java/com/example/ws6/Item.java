//Item
package com.example.ws6;

public class Item {
    private String name;
    private String unit;
    private double unitQuantity;
    private double unitPrice;

    public Item(String name, String unit, double unitQuantity, double unitPrice) {
        this.name = name;
        this.unit = unit;
        this.unitQuantity = unitQuantity;
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public double getUnitQuantity() {
        return unitQuantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    @Override
    public String toString() {
        return name;
    }
}
