package hms;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The {@code Prescription} class represents a prescription in the hospital management system.
 * It contains information about the prescribed medication, quantity, status, and provides methods
 * to save and retrieve prescriptions from a CSV file.
 */

public class Prescription {
    private String id; // Prescription ID
    private String medicationName;
    private int quantity; // New field for quantity
    private PrescriptionStatus status;

    /**
     * Constructs a new {@code Prescription} with the specified details.
     *
     * @param id             The unique prescription ID.
     * @param medicationName The name of the medication.
     * @param quantity       The quantity prescribed.
     * @param status         The current status of the prescription.
     */
    
    public Prescription(String id, String medicationName, int quantity, PrescriptionStatus status) {
        this.id = id;
        this.medicationName = medicationName;
        this.quantity = quantity;
        this.status = status;
    }

    public String getID() {
        return this.id;
    }

    public String getMedicationName() {
        return this.medicationName;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public PrescriptionStatus getStatus() {
        return this.status;
    }

    public void updateStatus(PrescriptionStatus newStatus) {
        this.status = newStatus;
    }

    /**
     * Returns a string representation of the prescription.
     *
     * @return A formatted string containing prescription details.
     */


    @Override
    public String toString() {
        return "Prescription ID: " + this.id +
                ", Medication Name: " + this.medicationName +
                ", Quantity: " + this.quantity +
                ", Status: " + this.status;
    }

    /**
     * Saves the prescription to a CSV file. If the prescription already exists,
     * it updates the existing entry; otherwise, it adds a new entry.
     *
     * @throws IOException If an I/O error occurs during file operations.
     */
    
    public void save() throws IOException {
        String filePath = "../data/prescription.csv";
        File file = new File(filePath);
        String header = "Prescription ID,Medication Name,Quantity,Status";

        if (!file.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
                pw.println(header);
            }
        }

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        boolean isEntryFound = false;
        List<String> updatedLines = new ArrayList<>();
        updatedLines.add(header); // Preserve header

        for (String line : lines) {
            if (line.trim().isEmpty() || line.equals(header)) continue;

            String[] parts = line.split(",");
            if (parts[0].trim().equals(this.id)) {
                isEntryFound = true;
                updatedLines.add(this.id + "," + this.medicationName + "," + this.quantity + "," + this.status.toString().toLowerCase());
            } else {
                updatedLines.add(line);
            }
        }

        if (!isEntryFound) {
            System.out.println("Saving new prescription: " + this); // Debugging
            updatedLines.add(this.id + "," + this.medicationName + "," + this.quantity + "," + this.status.toString().toLowerCase());
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, false))) {
            for (String line : updatedLines) {
                pw.println(line);
            }
        }
    }

    /**
     * Retrieves all prescriptions from the CSV file.
     *
     * @return A list of {@code Prescription} objects.
     * @throws IOException If an I/O error occurs during file reading.
     */

    
    public static List<Prescription> getAll() throws IOException {
        List<Prescription> prescriptions = new ArrayList<>();
        String filePath = "../data/prescription.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String header = br.readLine();
            if (header == null || !header.trim().equals("Prescription ID,Medication Name,Quantity,Status")) {
                throw new IOException("Invalid or missing header in prescription file.");
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) {
                    System.err.println("Skipping malformed record: " + line);
                    continue;
                }

                int quantity = Integer.parseInt(parts[2]);
                PrescriptionStatus status = parts[3].equalsIgnoreCase("dispensed") ?
                        PrescriptionStatus.Dispensed : PrescriptionStatus.Pending;

                prescriptions.add(new Prescription(parts[0], parts[1], quantity, status));
            }
        }

        return prescriptions;
    }
     /**
     * Generates a random prescription ID consisting of a 4-digit number.
     *
     * @return A random prescription ID as a {@code String}.
     */

    public static String generateRandomPrescriptionID() {
        int randomId = new Random().nextInt(9000) + 1000; // Generate a 4-digit ID
        return Integer.toString(randomId);
    }
}
