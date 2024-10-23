package hms;

public class InventoryItem {
    private String name;
    private int stock;
    private int lowStockLevel;

    public InventoryItem(String name, int stock, int lowStockLevel) {
        this.name = name;
        this.stock = stock;
        this.lowStockLevel = lowStockLevel;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getLowStockLevel() {
        return lowStockLevel;
    }

    public void setLowStockLevel(int lowStockLevel) {
        this.lowStockLevel = lowStockLevel;
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "Name='" + name + '\'' +
                ", Stock=" + stock +
                ", Low Stock Level=" + lowStockLevel +
                '}';
    }
}
