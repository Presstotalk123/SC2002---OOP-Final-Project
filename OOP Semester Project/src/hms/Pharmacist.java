package hms;

import hms.appointment_outcome_record.AppointmentOutcomeRecord;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * The {@code Pharmacist} class represents a pharmacist in the hospital management system.
 * It extends the {@link Staff} class and provides functionalities specific to pharmacists,
 * such as viewing appointment outcome records, updating prescription statuses,
 * managing medication inventory, and submitting replenishment requests.
 */

public class Pharmacist extends Staff {

    /**
     * Constructs a new {@code Pharmacist} instance by prompting the user for input via a {@code Scanner}.
     * This constructor is typically used when creating a new pharmacist account.
     *
     * @param scanner The {@code Scanner} object to read user input.
     * @throws IOException If an I/O error occurs during saving the user data.
     */
    
    public Pharmacist(Scanner scanner) throws IOException {
        super(scanner, "pharmacist");
        try {
            super.save();
        } catch (IOException error) {
            System.out.println("Unable to save user " + name + " due to IOException: " + error.getMessage());
        }
    }

    /**
     * Constructs a new {@code Pharmacist} instance with the specified ID, name, and password.
     * This constructor is typically used when loading an existing pharmacist account.
     *
     * @param id       The unique identifier for the pharmacist.
     * @param name     The name of the pharmacist.
     * @param password The password for the pharmacist.
     * @throws IOException If an I/O error occurs during operations.
     */
    
    public Pharmacist(String id, String name, String password) throws IOException {
        super(id, name, password, "pharmacist");
    }

    /**
     * The main event loop for the pharmacist, providing a menu of options to interact with the system.
     *
     * @param scanner The {@code Scanner} object to read user input.
     * @return {@code true} if the pharmacist wishes to continue; {@code false} to log out.
     */

    public boolean eventLoop(Scanner scanner) {
        System.out.print("""
                Pharmacist Menu:
                1. View Appointment Outcome Record
                2. Update Prescription Status
                3. View Medication Inventory
                4. Submit Replenishment Request
                5. Log Out
                Enter your choice:""");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                AppointmentOutcomeRecord.getAllRecords();
                break;
            case 2:
                System.out.print("Enter Prescription ID to update: ");
                String prescriptionId = scanner.nextLine();
                
                try {
                    while (true) {
                        System.out.print("Enter new status (e.g., Pending/Dispensed): ");
                        String status = scanner.nextLine();
                        
                        if (status.equalsIgnoreCase("pending")) {
                            updatePrescriptionStatus(prescriptionId, PrescriptionStatus.Pending);
                            break; // Exit after successful update
                        } else if (status.equalsIgnoreCase("dispensed")) {
                            updatePrescriptionStatus(prescriptionId, PrescriptionStatus.Dispensed);
                            break; // Exit after successful update
                        } else {
                            System.out.println("Invalid choice! Please choose between Pending or Dispensed.");
                        }
                    }
                } catch (IOException error) {
                    System.out.println("Failed to update Prescription Status: " + error.getMessage());
                }
                break;
            
            case 3:
                displayMedicationInventory();
                break;
            case 4:
                System.out.print("Enter Medication Name: ");
                String medicationName = scanner.nextLine();
                int quantity = 0;

                while (true) {
                    System.out.print("Enter Quantity: ");
                    try {
                        quantity = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        break; // Exit loop if input is valid
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input! Please enter a numeric value for quantity.");
                        scanner.nextLine(); // Clear the invalid input
                    }
                }

                submitReplenishmentRequest(medicationName, quantity);
                break;

            case 5:
                System.out.println("Signed out successfully.");
                return false;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }

        return true;
    }


    /**
     * Updates the status of a prescription and adjusts inventory if necessary.
     *
     * @param prescriptionId The ID of the prescription to update.
     * @param status         The new status to set for the prescription.
     * @throws IOException If an I/O error occurs during file operations.
     */
    
    public void updatePrescriptionStatus(String prescriptionId, PrescriptionStatus status) throws IOException {
        List<Prescription> records = Prescription.getAll();
        Inventory inventory = new Inventory();
        inventory.loadFromCSV(); // Load the current inventory
        boolean found = false;

        for (Prescription record : records) {
            if (record.getID().equals(prescriptionId)) {
                record.updateStatus(status);
                record.save();
                found = true;

                // Update inventory if the status is set to dispensed
                if (status == PrescriptionStatus.Dispensed) {
                    Medication med = inventory.getMedication(record.getMedicationName());
                    if (med != null) {
                        int quantityDispensed = record.getQuantity(); // Get the quantity from the prescription
                        int newStockLevel = med.getStockLevel() - quantityDispensed; // Subtract the dispensed quantity
                        inventory.updateStockLevel(record.getMedicationName(), newStockLevel);
                        inventory.saveToCSV("../data/inventory.csv");
                    }
                }
                break; // Exit the loop as we found the prescription
            }
        }

        if (found) {
            System.out.println("Prescription status updated successfully.");
        } else {
            System.out.println("Prescription not found for the given prescription ID.");
        }
        
}
    
/**
     * Displays the current medication inventory.
     */



    public void displayMedicationInventory() {
        Inventory inventory = new Inventory();
        inventory.loadFromCSV(); // Load the inventory from the CSV file

        List<Medication> medications = inventory.getMedications();

        if (medications.isEmpty()) {
            System.out.println("No medications in inventory.");
        } else {
            System.out.println("Medication Inventory:");
            for (Medication med : medications) {
                System.out.printf("Name: %s, Quantity: %d%n", med.getMedicationName(), med.getStockLevel());
            }
        }
    }

    /**
     * Submits a replenishment request for a medication.
     *
     * @param medicationName The name of the medication to replenish.
     * @param quantity       The quantity to replenish.
     */

    
    // In the submitReplenishmentRequest method
    public void submitReplenishmentRequest(String medicationName, int quantity) {
        // Create a new replenishment request
        ReplenishmentRequest request = new ReplenishmentRequest(
                generateRequestID(), medicationName, quantity, "Pending");
        // Submit the request (this will also create the file if it doesn't exist)
        request.saveToCSV("../data/replenishment_requests.csv");
    }

    private String generateRequestID() {
        // Generates a unique request ID
        return "REQ" + System.currentTimeMillis();
    }

   /**
     * Returns a string representation of the pharmacist.
     *
     * @return A string containing the pharmacist's ID, role, and name.
     */

    @Override
        public String toString() {
        return this.id + " - Pharmacist - " + this.name;
}

}
