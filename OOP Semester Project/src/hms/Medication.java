package hms;

public class Medication {
    private String medicationName;
    private int price;
    private int stockLevel;
    private int lowStockAlertLevel;


    public Medication(String medicationName,int price, int stockLevel, int lowStockAlertLevel) {
        this.medicationName = medicationName;
        this.price = price;
        this.stockLevel = stockLevel;
        this.lowStockAlertLevel = lowStockAlertLevel;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public int getStockLevel() {
        return stockLevel;
    }
    public int getPrice() {
        return price;
    }

    public int getLowStockAlertLevel() {
        return lowStockAlertLevel;
    }

    public void updateStockLevel(int quantity) {
        this.stockLevel = quantity;
    }

    public void setLowStockAlertLevel(int lowStockAlertLevel) {
        this.lowStockAlertLevel = lowStockAlertLevel;
    }
    // Inventory.java
     @Override
    public String toString() {
        return "InventoryItem{" +
                "Name='" + medicationName + '\'' +
                ", Stock=" + stockLevel +
                ", Low Stock Level=" + lowStockAlertLevel +
                '}';
    }

    public int setPrice(int price) {
        if (price >= 0) {
            this.price = price;
        } else {
            System.out.println("Price cannot be negative.");
        }
        return price;
    }
}
