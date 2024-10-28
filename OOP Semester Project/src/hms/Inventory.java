package hms;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<InventoryItem> items;

    public Inventory() {
        items = new ArrayList<>();
    }

    public void addItem(InventoryItem item) {
        items.add(item);
    }

    public boolean removeItem(String name) {
        return items.removeIf(item -> item.getName().equals(name));
    }

    public InventoryItem findItem(String name) {
        return items.stream().filter(item -> item.getName().equals(name)).findFirst().orElse(null);
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    public void updateStockLevel(String name, int quantity) {
        for (InventoryItem item : items) {
            if (item.getName().equals(name)) {
                // this is broken, nothing called updateStockLevel() in InventoryItem.java
                System.out.println("hey fix this, item.updateStockLevel doesn't exist on InventoryItem");
                // item.updateStockLevel(quantity);
                break;
            }
        }
    }

    public InventoryItem getItem(String name) {
        for (InventoryItem item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }
}