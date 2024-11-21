package hms;

/**
 * The {@code Medication} class represents a medication item in the hospital management system.
 * It includes details such as medication name, price, stock level, low stock alert level,
 * and whether a low stock alert is active.
 */


public class Medication {
    private String medicationName;
    private int price;
    private int stockLevel;
    private int lowStockAlertLevel;
    private boolean stocklevelalert; // Correct field name casing to align with standard practices

    /**
     * Constructs a new {@code Medication} instance with the specified details.
     * Initializes the stock level alert based on the stock level and low stock alert level.
     *
     * @param medicationName     The name of the medication.
     * @param price              The price of the medication.
     * @param stockLevel         The current stock level.
     * @param lowStockAlertLevel The stock level at which to trigger a low stock alert.
     */

    
    public Medication(String medicationName, int price, int stockLevel, int lowStockAlertLevel) {
        this.medicationName = medicationName;
        this.price = price;
        this.stockLevel = stockLevel;
        this.lowStockAlertLevel = lowStockAlertLevel;
        this.stocklevelalert = stockLevel < lowStockAlertLevel; // Initialize alert status
    }

    public String getMedicationName() {
        return medicationName;
    }

    public boolean getStockLevelAlert() {
        return stocklevelalert;
    }

    public void setStockLevelAlert(boolean alertStatus) { // Add this method
        this.stocklevelalert = alertStatus;
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
        if (quantity >= 0) {
            this.stockLevel = quantity;
            this.stocklevelalert = stockLevel < lowStockAlertLevel; // Recalculate alert status
        } else {
            System.out.println("Stock level cannot be negative.");
        }
    }

    public void setLowStockAlertLevel(int lowStockAlertLevel) {
        if (lowStockAlertLevel >= 0) {
            this.lowStockAlertLevel = lowStockAlertLevel;
            this.stocklevelalert = stockLevel < lowStockAlertLevel; // Recalculate alert status
        } else {
            System.out.println("Low stock alert level cannot be negative.");
        }
    }

    public int setPrice(int price) {
        if (price >= 0) {
            this.price = price;
        } else {
            System.out.println("Price cannot be negative.");
        }
        return price;
    }

    /**
     * Returns a string representation of the {@code Medication} object.
     *
     * @return A formatted string containing the medication details.
     */
    
    @Override
    public String toString() {
        return String.format(
            "Medication{name='%s', price=%d, stockLevel=%d, lowStockAlertLevel=%d, stockLevelAlert=%b}",
            medicationName, price, stockLevel, lowStockAlertLevel, stocklevelalert
        );
    }
}
