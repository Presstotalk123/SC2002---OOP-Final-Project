import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.BufferedReader;

public class Pharmacist extends User {

    public Pharmacist(String hospitalID, String password, String name, Date dateOfBirth, String gender, ContactInfo contactInfo) {
        super(hospitalID, password, name, dateOfBirth, gender, contactInfo);
    }

    /**
     * Views all appointment outcome records.
     *
     * @return a list of AppointmentOutcomeRecord objects.
     */
    public List<AppointmentOutcomeRecord> viewAppointmentOutcomeRecords() {
        List<AppointmentOutcomeRecord> records = new ArrayList<>();
        String filePath = "appointment_outcome_records.csv"; // Path to your CSV file

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Assuming the CSV columns are in the order: appointmentID, dateOfAppointment, serviceType, prescribedMedications, consultationNotes
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

    /**
     * Updates the status of a prescription.
     *
     * @param prescriptionID the ID of the prescription to update.
     * @param status         the new status of the prescription.
     */
    public void updatePrescriptionStatus(String prescriptionID, String status) {
        // Find the prescription by ID
        Prescription prescription = findPrescriptionByID(prescriptionID);
        if (prescription != null) {
            prescription.updateStatus(status);
            // Save the updated prescription to the database (not implemented)
        } else {
            System.out.println("Prescription not found.");
        }
    }

    /**
     * Views the current medication inventory.
     *
     * @return a list of Medication objects.
     */
    public List<Medication> viewMedicationInventory() {
        // Retrieve the inventory (placeholder implementation)
        Inventory inventory = getInventory();
        return inventory.getMedications();
    }

    /**
     * Submits a replenishment request for a medication.
     *
     * @param medicationName the name of the medication.
     * @param quantity       the quantity to request.
     */
    public void submitReplenishmentRequest(String medicationName, int quantity) {
        // Create a new replenishment request
        ReplenishmentRequest request = new ReplenishmentRequest(
                generateRequestID(), medicationName, quantity, "Pending");
        // Submit the request (e.g., save to database or notify administrator)
        request.saveToCSV("replenishment_requests.csv");
    }

    // Helper methods (placeholders for actual implementations)

    private Prescription findPrescriptionByID(String prescriptionID) {
        // Placeholder method to find a prescription by ID
        return null; // Should return a Prescription object if found
    }

    private Inventory getInventory() {
        // Placeholder method to retrieve the inventory
        Inventory inventory = new Inventory();
        inventory.loadFromCSV("inventory.csv");
        return inventory;
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

    // Implement abstract methods from User if any (e.g., login, changePassword)
    @Override
    public boolean login(String Username) {
        // Implement login logic here
        return this.password.equals(password);
    }

    @Override
    public void changePassword(String newPassword) {
        // Implement password change logic here
        this.password = newPassword;
    }
}