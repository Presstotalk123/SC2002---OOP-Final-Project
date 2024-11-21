package hms.Billing;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Billing} class handles all billing-related functionalities,
 * such as adding bills, calculating bill amounts, updating billing status,
 * and verifying the integrity of the billing blockchain.
 * It implements both {@code billingAdmin} and {@code billingPatient} interfaces.
 */

public class Billing implements billingAdmin,billingPatient{
    private static final String BILLING_FILE = "C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\billing.csv";
    private static final String BLOCKCHAIN_FILE = "C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\blockchain.dat";
    private static final String PRESCRIPTION_FILE = "C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\prescription.csv";
    private static final String INVENTORY_FILE = "C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\inventory.csv";

    private static Blockchain blockchain;

    public Billing() {
        try {
            loadBlockchain();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Represents an individual bill with details such as bill ID, patient ID,
     * description, amount, and payment status.
     */

    public class Bill {
            public String id;
            public String patientId;
            public String description;
            public double amount;
            public boolean paid;
        
            /**
         * Constructs a new {@code Bill} instance with the specified details.
         *
         * @param id          The unique identifier for the bill.
         * @param patientId   The patient ID associated with the bill.
         * @param description The description of the bill.
         * @param amount      The total amount of the bill.
         * @param paid        The payment status of the bill.
         */
        
            public Bill(String id, String patientId, String description, double amount, boolean paid) {
                this.id = id;
                this.patientId = patientId;
                this.description = description;
                this.amount = amount;
                this.paid = paid;
            }

        /**
         * Returns a string representation of the bill in CSV format.
         *
         * @return A CSV-formatted string of the bill.
         */

            @Override
            public String toString() {
                return String.format("\"%s\",\"%s\",\"%s\",%.2f,%b", id, patientId, description.replace("\"", "\"\""), amount, paid);
            }

        /**
         * Displays the bill details in a formatted manner.
         *
         * @param bill The {@code Bill} object to be viewed.
         */
            public void viewBill(Bill bill) {
                System.out.printf("%-15s %-12s %-50s %-10.2f %-6b%n",
                        bill.id,
                        bill.patientId,
                        bill.description.length() > 50 ? bill.description.substring(0, 47) + "..." : bill.description,
                        bill.amount,
                        bill.paid);
            }
        }

    /**
     * Adds a new bill for a patient based on the given prescription IDs.
     *
     * @param billId         The unique identifier for the bill.
     * @param patientId      The patient ID.
     * @param prescriptionIds A list of prescription IDs associated with the bill.
     * @throws IOException If an I/O error occurs during file operations.
     */
    
    public void addBill(String billId, String patientId, List<String> prescriptionIds) throws IOException {
        if (!isValidPatient(patientId)) {
            System.out.println("Invalid patient ID: " + patientId);
            return;
        }

        double totalAmount = 0;
        for (String prescriptionId : prescriptionIds) {
            if (!isValidPrescription(prescriptionId)) {
                System.out.println("Invalid prescription ID: " + prescriptionId);
                continue;
            }

            // Check if the prescription status is "Dispensed"
            if (!isPrescriptionDispensed(prescriptionId)) {
                System.out.println("Prescription ID " + prescriptionId + " is still pending.");
                continue;
            }

            totalAmount += calculateBillAmountWithoutDoctorFee(prescriptionId); // Updated method to exclude doctor's fee
        }

        // Add the doctor's fee once for the entire bill
        totalAmount += 50;

        String description = "Billing for prescription IDs: " + String.join(", ", prescriptionIds);

        Bill bill = new Bill(billId, patientId, description, totalAmount, false);
        blockchain.addBlock(new Block(bill.toString(), blockchain.getLatestBlock().hash));
        saveBlockchain();
        updateBillingFile(bill);
    }

    /**
     * Checks if the given patient ID is valid by verifying it against the users CSV file.
     *
     * @param patientId The patient ID to validate.
     * @return {@code true} if the patient ID is valid; {@code false} otherwise.
     * @throws IOException If an I/O error occurs during file reading.
     */
    
    private boolean isValidPatient(String patientId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\users.csv"))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(patientId) && parts[3].equalsIgnoreCase("patient")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the given prescription ID has a status of "Dispensed".
     *
     * @param prescriptionId The prescription ID to check.
     * @return {@code true} if the prescription is dispensed; {@code false} otherwise.
     * @throws IOException If an I/O error occurs during file reading.
     */
    
    private boolean isPrescriptionDispensed(String prescriptionId) throws IOException {
        try (BufferedReader prescriptionReader = new BufferedReader(new FileReader(PRESCRIPTION_FILE))) {
            String line;
            prescriptionReader.readLine(); // Skip header
            while ((line = prescriptionReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(prescriptionId)) {
                    String status = parts[3]; // Assuming status is in the 4th column
                    return "Dispensed".equalsIgnoreCase(status);
                }
            }
        }
        return false;
    }

    /**
     * Calculates the total bill amount for a given prescription without including the doctor's fee.
     *
     * @param prescriptionId The prescription ID.
     * @return The total amount excluding the doctor's fee.
     * @throws IOException If an I/O error occurs during file reading.
     */
    
    public double calculateBillAmountWithoutDoctorFee(String prescriptionId) throws IOException {
        double totalAmount = 0;

        try (BufferedReader prescriptionReader = new BufferedReader(new FileReader(PRESCRIPTION_FILE))) {
            String line;
            prescriptionReader.readLine(); // Skip header
            while ((line = prescriptionReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(prescriptionId)) {
                    String medicationName = parts[1];
                    int quantity = Integer.parseInt(parts[2]);

                    double medicationPrice = getMedicationPriceFromInventory(medicationName);
                    totalAmount += quantity * medicationPrice;
                }
            }
        }

        return totalAmount;
    }

    /**
     * Calculates the total bill amount for a given prescription, including the doctor's fee.
     *
     * @param prescriptionId The prescription ID.
     * @return The total amount including the doctor's fee.
     * @throws IOException If an I/O error occurs during file reading.
     */
    
    public double calculateBillAmount(String prescriptionId) throws IOException {
        double totalAmount = 0;

        try (BufferedReader prescriptionReader = new BufferedReader(new FileReader(PRESCRIPTION_FILE))) {
            String line;
            prescriptionReader.readLine(); // Skip header
            while ((line = prescriptionReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(prescriptionId)) {
                    String medicationName = parts[1];
                    int quantity = Integer.parseInt(parts[2]);

                    double medicationPrice = getMedicationPriceFromInventory(medicationName);
                    totalAmount += quantity * medicationPrice;
                }
            }
        }

        // Adding doctor's fee
        totalAmount += 50;
        return totalAmount;
    }

    /**
     * Retrieves the price of a medication from the inventory.
     *
     * @param medicationName The name of the medication.
     * @return The price of the medication.
     * @throws IOException              If an I/O error occurs during file reading.
     * @throws IllegalArgumentException If the medication is not found in the inventory.
     */


    private double getMedicationPriceFromInventory(String medicationName) throws IOException {
        try (BufferedReader inventoryReader = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            String line;
            inventoryReader.readLine(); // Skip header
            while ((line = inventoryReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equalsIgnoreCase(medicationName)) {
                    return Double.parseDouble(parts[1]); // Assuming price is in the second column
                }
            }
        }
        throw new IllegalArgumentException("Medication not found in inventory: " + medicationName);
    }

    /**
     * Retrieves all bills associated with a specific patient ID.
     *
     * @param patientId The patient ID.
     * @return A list of bills for the patient.
     * @throws IOException If an I/O error occurs during file reading.
     */
    
    public List<Bill> getBillsByPatientId(String patientId) throws IOException {
        List<Bill> bills = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BILLING_FILE))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts[1].replace("\"", "").trim().equals(patientId)) {
                    String id = parts[0].replace("\"", "").trim();
                    String description = parts[2].replace("\"", "").trim();
                    double amount = Double.parseDouble(parts[3].trim());
                    boolean paid = Boolean.parseBoolean(parts[4].trim());
                    bills.add(new Bill(id, patientId, description, amount, paid));
                }
            }
        }
        return bills;
    }

    /**
     * Retrieves all bills from the billing file.
     *
     * @return A list of all bills.
     * @throws IOException If an I/O error occurs during file reading.
     */

    
    public List<Bill> getAllBills() throws IOException {
        List<Bill> bills = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BILLING_FILE))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                // Skip the header
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // Ensure the parts array has the correct number of elements
                if (parts.length < 5) {
                    System.out.println("Skipping malformed line: " + line);
                    continue;
                }

                try {
                    String id = parts[0].replace("\"", "").trim();
                    String patientId = parts[1].replace("\"", "").trim();
                    String description = parts[2].replace("\"", "").trim();
                    double amount = Double.parseDouble(parts[3].trim());
                    boolean paid = Boolean.parseBoolean(parts[4].trim());

                    bills.add(new Bill(id, patientId, description, amount, paid));
                }
                catch (NumberFormatException e) {
                    System.out.println("Skipping line due to number format error: " + line);
                }
            }
        }
        return bills;
    }


    /**
     * Updates the payment status of a bill by its ID.
     *
     * @param id   The bill ID.
     * @param paid The new payment status.
     * @throws IOException If an I/O error occurs during file operations.
     */

    
    public void updateBill(String id, boolean paid) throws IOException {
        List<Bill> bills = getAllBills();
        for (Bill bill : bills) {
            if (bill.id.equals(id)) {
                bill.paid = paid;
                break;
            }
        }
        blockchain = new Blockchain();
        for (Bill bill : bills) {
            blockchain.addBlock(new Block(bill.toString(), blockchain.getLatestBlock().hash));
        }
        saveBlockchain();
        updateBillingFile(bills);
    }

    /**
     * Saves the blockchain to a file.
     *
     * @throws IOException If an I/O error occurs during file writing.
     */

    
    private void saveBlockchain() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BLOCKCHAIN_FILE))) {
            oos.writeObject(blockchain);
        }
    }

    /**
     * Loads the blockchain from a file.
     *
     * @throws IOException            If an I/O error occurs during file reading.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */

    
    private void loadBlockchain() throws IOException, ClassNotFoundException {
        File file = new File(BLOCKCHAIN_FILE);
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                blockchain = (Blockchain) ois.readObject();
            } catch (EOFException e) {
                System.out.println("Blockchain file is empty or corrupted. Initializing a new blockchain.");
                blockchain = new Blockchain();
            }
        } else {
            blockchain = new Blockchain();
        }
    }

    /**
     * Appends a new bill to the billing CSV file.
     *
     * @param bill The {@code Bill} object to append.
     * @throws IOException If an I/O error occurs during file writing.
     */
    
    private void updateBillingFile(Bill bill) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BILLING_FILE, true))) {
            writer.write(bill.toString());
            writer.newLine();
        }
    }
    
     /**
     * Writes the entire list of bills to the billing CSV file, overwriting existing content.
     *
     * @param bills The list of bills to write.
     * @throws IOException If an I/O error occurs during file writing.
     */

    private void updateBillingFile(List<Bill> bills) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BILLING_FILE))) {
            writer.write("id,patientId,description,amount,paid");
            writer.newLine();
            for (Bill bill : bills) {
                writer.write(bill.toString());
                writer.newLine();
            }
        }
    }

    /**
     * Verifies the integrity of the blockchain.
     *
     * @return {@code true} if the blockchain is valid; {@code false} otherwise.
     */

    public static boolean verifyBlockchain() {
        return blockchain.isChainValid();
    }

    /**
     * Marks a bill as paid based on the given bill ID.
     *
     * @param BillId The bill ID to mark as paid.
     * @throws IOException If an I/O error occurs during file operations.
     */
    
public void paybill(String BillId) throws IOException {
        updateBill(BillId, true);
    }

    /**
     * Checks if the given prescription ID is valid by verifying it against the prescription CSV file.
     *
     * @param prescriptionId The prescription ID to validate.
     * @return {@code true} if the prescription ID is valid; {@code false} otherwise.
     * @throws IOException If an I/O error occurs during file reading.
     */
    
    private boolean isValidPrescription(String prescriptionId) throws IOException {
        try (BufferedReader prescriptionReader = new BufferedReader(new FileReader(PRESCRIPTION_FILE))) {
            String line;
            prescriptionReader.readLine(); // Skip header
            while ((line = prescriptionReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(prescriptionId)) {
                    return true;
                }
            }
        }
        return false;
    }

}
