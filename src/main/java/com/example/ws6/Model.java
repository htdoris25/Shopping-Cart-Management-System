// Model
package com.example.ws6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

public class Model {
    private final ObservableList<Item> itemsObservableList = FXCollections.observableArrayList();

    public void loadData() {
        try (InputStream inputStream = getClass().getResourceAsStream("/com/example/ws6/ItemsMaster.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            boolean isFirstLine = true; // Skip the header if it exists
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length == 4) {
                    String name = data[0];
                    String unit = data[1];
                    double unitQuantity = Double.parseDouble(data[2]);
                    double unitPrice = Double.parseDouble(data[3]);
                    itemsObservableList.add(new Item(name, unit, unitQuantity, unitPrice));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Item> getItemsObservableList() {
        return itemsObservableList;
    }

    // New method to get the unit price by item name
    public double getUnitPriceByName(String itemName) {
        Optional<Item> item = itemsObservableList.stream()
                .filter(i -> i.getName().equals(itemName))
                .findFirst();
        return item.map(Item::getUnitPrice).orElse(0.0); // Returns 0.0 if the item is not found
    }
}
