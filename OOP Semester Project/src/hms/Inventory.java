package hms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Inventory} class manages a collection of medications in the hospital management system.
 * It provides functionalities to add, remove, update, and retrieve medications,
 * as well as loading from and saving to a CSV file.
 */


public class Inventory {
    private List<Medication> medications;

    public Inventory() {
        medications = new ArrayList<>();
    }

    /**
     * Adds a medication to the inventory.
     * Sets the stock level alert based on the current stock and low stock alert level.
     *
     * @param medication The {@code Medication} object to add.
     */

    
    public void addMedication(Medication medication) {
        medication.setStockLevelAlert(medication.getStockLevel() < medication.getLowStockAlertLevel());
        medications.add(medication);
    }
    
    /**
     * Removes a medication from the inventory based on its name.
     *
     * @param medicationName The name of the medication to remove.
     * @return {@code true} if the medication was removed; {@code false} otherwise.
     */
    
    public boolean removeMedication(String medicationName) {
        boolean removed = medications.removeIf(medication -> 
            medication.getMedicationName().equalsIgnoreCase(medicationName)
        );
        return removed; // Only return true if something was actually removed
    }
    
    /**
     * Updates the stock level of a specific medication.
     *
     * @param medicationName The name of the medication to update.
     * @param quantity       The new stock level quantity.
     */
    
    public void updateStockLevel(String medicationName, int quantity) {
        for (Medication med : medications) {
            if (med.getMedicationName().equals(medicationName)) {
                med.updateStockLevel(quantity);
                break;
            }
        }
    }

    /**
     * Retrieves a medication from the inventory based on its name.
     * Loads the inventory from the CSV file before searching.
     *
     * @param medicationName The name of the medication to retrieve.
     * @return The {@code Medication} object if found; {@code null} otherwise.
     */

    public Medication getMedication(String medicationName) {
        loadFromCSV();
        for (Medication med : medications) {
            if (med.getMedicationName().equals(medicationName)) {
                return med;
            }
        }
        return null;
    }

    /**
     * Retrieves the list of all medications in the inventory.
     *
     * @return A list of {@code Medication} objects.
     */

    
    public List<Medication> getMedications() {
        return medications;
    }


    /**
     * Saves the current inventory to a CSV file.
     *
     * @param filePath The file path where the inventory should be saved.
     */
    
    public void saveToCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Name,Price,StockLevel,LowStockAlertLevel,stocklevelalert\n"); // Write the header
            for (Medication med : medications) {
                writer.write(String.format("%s,%d,%d,%d,%s\n", med.getMedicationName(), med.getPrice(), med.getStockLevel(), med.getLowStockAlertLevel(), med.getStockLevelAlert()));
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory data: " + e.getMessage());
        }
    }
    
    /**
     * Loads the inventory data from a CSV file.
     * Clears the current list before loading.
     */
    
    public void loadFromCSV() {
        medications.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("../data/inventory.csv"))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }
                String[] values = line.split(",");
                if (values.length == 5) {
                    try {
                        String medicationName = values[0];
                        int price = Integer.parseInt(values[1]);
                        int stockLevel = Integer.parseInt(values[2]);
                        int lowStockAlertLevel = Integer.parseInt(values[3]);
                        boolean stockLevelAlert = Boolean.parseBoolean(values[4]);
    
                        Medication medication = new Medication(medicationName, price, stockLevel, lowStockAlertLevel);
                        medication.setStockLevelAlert(stockLevelAlert);
                        medications.add(medication);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid data format in inventory file: " + line);
                    }
                } else {
                    System.out.println("Invalid data format in inventory file: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Checks for medications that are below their low stock alert level and prints an alert message.
     */
    
    public void checkLowStockAlerts() {
        for (Medication med : medications) {
            if (med.getStockLevel() < med.getLowStockAlertLevel()) {
                System.out.println("Alert: Stock level for " + med.getMedicationName() + " is below the specified low stock alert level.");
            }
        }
    }
}
