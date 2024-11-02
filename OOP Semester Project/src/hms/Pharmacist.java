import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.BufferedReader;
import java.util.Scanner;
import.java.io.BufferedWriter;

public class Pharmacist extends User {

    public Pharmacist(String hospitalID, String password, String name, Date dateOfBirth, String gender, ContactInfo contactInfo) {
        super(hospitalID, password, name, dateOfBirth, gender, contactInfo);
    }

    // Main menu method for Pharmacist
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
    System.out.println("\nPharmacist Menu:");
    System.out.println("1. View Appointment Outcome Record");
    System.out.println("2. Update Prescription Status");
    System.out.println("3. View Medication Inventory");
    System.out.println("4. Submit Replenishment Request");
    System.out.println("5. Logout");
    System.out.print("Select an option: ");

    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    switch (choice) {
        case 1:
            viewAppointmentOutcomeRecords();
            break;
        case 2:
            System.out.print("Enter Appointment ID to update prescriptions: ");
            String appointmentID = scanner.nextLine();
            System.out.print("Enter new status (e.g., Dispensed): ");
            String status = scanner.nextLine();
            updatePrescriptionStatus(appointmentID, status);
            break;
        case 3:
            displayMedicationInventory(); 
            break;
        case 4:
            System.out.print("Enter Medication Name: ");
            String medicationName = scanner.nextLine();
            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();
            submitReplenishmentRequest(medicationName, quantity);
            break;
        case 5:
            exit = true;
            System.out.println("Logging out...");
            break;
        default:
            System.out.println("Invalid choice. Please try again.");
    }
}

        scanner.close();
    }

    // Existing methods...
public List<AppointmentOutcomeRecord> getAllAppointmentOutcomeRecords() {
    List<AppointmentOutcomeRecord> records = new ArrayList<>();
    String filePath = "appointment_outcome_records.csv"; // Path to your CSV file

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            String appointmentID = values[0];
            Date dateOfAppointment = new Date(values[1]); // You may need to parse the date string appropriately
            String serviceType = values[2];
            List<Prescription> prescribedMedications = parsePrescriptions(values[3]); // Implement parsePrescriptions method
            String consultationNotes = values[4];

            AppointmentOutcomeRecord record = new AppointmentOutcomeRecord(appointmentID, dateOfAppointment, serviceType, prescribedMedications, consultationNotes);
            records.add(record);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return records;
}

    // New method to update prescription status based on appointment ID

public void updatePrescriptionStatus(String appointmentID, String medicationName, String status) {
    // Fetch all appointment outcome records
    List<AppointmentOutcomeRecord> records = getAllAppointmentOutcomeRecords();
    boolean found = false;

    for (AppointmentOutcomeRecord record : records) {
        if (record.getAppointmentID().equals(appointmentID)) {
            // Update the status of the prescribed medication
            List<Prescription> prescriptions = record.getPrescribedMedications();
            for (Prescription prescription : prescriptions) {
                if (prescription.getMedicationName().equals(medicationName)) {
                    prescription.updateStatus(status); // Update the status
                    found = true;
                    break; // Exit the loop as we found the prescription
                }
            }
            break; // Exit the outer loop as well
        }
    }

    // If a prescription was found and updated, save the records
    if (found) {
        saveUpdatedRecordsToCSV(records);
        System.out.println("Prescription status updated successfully.");
    } else {
        System.out.println("Prescription not found for the given appointment ID and medication name.");
    }
}

private void saveUpdatedRecordsToCSV(List<AppointmentOutcomeRecord> records) {
    String filePath = "appointment_outcome_records.csv";

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
        for (AppointmentOutcomeRecord record : records) {
            StringBuilder sb = new StringBuilder();
            sb.append(record.getAppointmentID()).append(",")
              .append(record.getDateOfAppointment().getTime()).append(",") // Convert date to long
              .append(record.getServiceType()).append(",")
              .append(serializePrescriptions(record.getPrescribedMedications())).append(",")
              .append(record.getConsultationNotes());
            bw.write(sb.toString());
            bw.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private String serializePrescriptions(List<Prescription> prescriptions) {
    StringBuilder sb = new StringBuilder();
    for (Prescription prescription : prescriptions) {
        sb.append(prescription.getMedicationName()).append(":").append(prescription.getStatus()).append(";");
    }
    return sb.toString();
}



public void displayMedicationInventory() {
    Inventory inventory = new Inventory();
    inventory.loadFromCSV("inventory.csv"); // Load the inventory from the CSV file

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
    request.saveToCSV("replenishment_requests.csv");
}



    private String generateRequestID() {
        // Generates a unique request ID
        return "REQ" + System.currentTimeMillis();
    }

    private List<Prescription> parsePrescriptions(String csv) {
        List<Prescription> prescriptions = new ArrayList<>();
        String[] items = csv.split(";");
        for (String item : items) {
            String[] parts = item.split(":");
            String prescriptionID = parts[0];
            String medicationName = parts[1];
            String status = parts[2];
            prescriptions.add(new Prescription(prescriptionID, medicationName, status));
        }
        return prescriptions;
    }
}