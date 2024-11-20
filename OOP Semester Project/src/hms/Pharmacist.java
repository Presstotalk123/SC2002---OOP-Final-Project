package hms;

import hms.appointment_outcome_record.AppointmentOutcomeRecord;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Pharmacist extends Staff {

    public Pharmacist(Scanner scanner) throws IOException {
        super(scanner, "pharmacist");
        try {
            super.save();
        } catch (IOException error) {
            System.out.println("Unable to save user " + name + " due to IOException: " + error.getMessage());
        }
    }

    public Pharmacist(String id, String name, String password) throws IOException {
        super(id, name, password, "pharmacist");
    }

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
                        inventory.saveToCSV("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\inventory.csv");
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






    private boolean checkAppointmentStatus(String appointmentId) throws IOException {
        String filePath = "C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\appointments.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > 0 && values[0].equals(appointmentId)) {
                    String status = values[4].trim(); // Assuming index 4 is status
                    return "completed".equalsIgnoreCase(status);
                }
            }
        }
        return false; // Return false if appointment not found or not completed
    }


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

    // In the submitReplenishmentRequest method
    public void submitReplenishmentRequest(String medicationName, int quantity) {
        // Create a new replenishment request
        ReplenishmentRequest request = new ReplenishmentRequest(
                generateRequestID(), medicationName, quantity, "Pending");
        // Submit the request (this will also create the file if it doesn't exist)
        request.saveToCSV("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\replenishment_requests.csv");
    }

    private String generateRequestID() {
        // Generates a unique request ID
        return "REQ" + System.currentTimeMillis();
    }

   
    

    @Override
        public String toString() {
        return this.id + " - Pharmacist - " + this.name;
}

}