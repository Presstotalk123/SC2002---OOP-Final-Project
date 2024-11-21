package hms;

import hms.Appointments.Appointment;
import hms.Billing.Billing;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * The {@code Administrator} class represents an administrator user in the hospital management system.
 * It extends the {@link User} class and provides functionalities specific to administrators,
 * such as managing staff, appointments, inventory, billing, and viewing feedback.
 */

public class Administrator extends User {
    private Billing billing;
    private List<Staff> staffList;
    private List<Appointment> appointmentList;
    private Inventory inventory;

    /**
     * Constructs a new {@code Administrator} instance by prompting the user for input via a {@code Scanner}.
     * This constructor is typically used when creating a new administrator account.
     *
     * @param scanner The {@code Scanner} object to read user input.
     * @throws IOException If an I/O error occurs during saving the user data.
     */
    
    public Administrator(Scanner scanner) throws IOException {
        super(scanner, "administrator");
        try {
            super.save();
        }
        catch (IOException error) {
            System.out.println("Unable to save user " + name + " due to IOException: " + error.getMessage());
        }
        staffList = new ArrayList<>();
        appointmentList = new ArrayList<>();
        inventory = new Inventory();
        billing=new Billing();
        loadStaffData();
        loadAppointmentData();
        loadInventoryData();
    }

    /**
     * Constructs a new {@code Administrator} instance with the specified ID, name, and password.
     * This constructor is typically used when loading an existing administrator account.
     *
     * @param id       The unique identifier for the administrator.
     * @param name     The name of the administrator.
     * @param password The password for the administrator.
     */
    
    public Administrator(String id, String name, String password) {
        super(id, name, password, "administrator");
        staffList = new ArrayList<>();
        appointmentList = new ArrayList<>();
        inventory = new Inventory();
        billing=new Billing();
        loadStaffData();
        loadAppointmentData();
        loadInventoryData();
    }

    /**
     * The main event loop for the administrator, providing a menu of options to interact with the system.
     *
     * @param scanner The {@code Scanner} object to read user input.
     * @return {@code true} if the administrator wishes to continue; {@code false} to log out.
     * @throws IOException If an I/O error occurs during operations.
     */
    
    public boolean eventLoop(Scanner scanner) throws IOException {
        int choice;
        System.out.println("Administrator Menu:");
        System.out.println("1. View Staff");
        System.out.println("2. Manage Staff");
        System.out.println("3. View Appointments");
        System.out.println("4. Manage Inventory");
        System.out.println("5. Create Bill");
        System.out.println("6. View Bills");
        System.out.println("7. Verify Blockchain Integrity");
        System.out.println("8. View Feedback");
        System.out.println("9. Log Out");
        choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                viewStaff(scanner);
                break;
            case 2:
                manageStaff(scanner);
                break;
            case 3:
                viewAppointments(scanner);
                break;
            case 4:
                manageInventory(scanner);
                break;
            case 5:
                createBill(scanner);
                break;
            case 6:
                viewBills();
                break;
            case 7:
                verifyBlockchain();
                break;
            case 8:
                viewFeedback();
                break;
            case 9:
                // Returning false just means "I want to log out and go back to the login menu"
                System.out.println("Signed out successfully.");
                return false;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        // Returning true just means "Yes I want to continue as this user"
        return true;
    }

    /**
     * Displays the list of staff members.
     *
     * @param scanner The {@code Scanner} object to read user input.
     */
    
    private void viewStaff(Scanner scanner) {
        System.out.println("Viewing staff...");
        for (Staff staff : staffList) {
            System.out.println(staff);
        }
        System.out.println("Press Enter to return to the admin menu.");

        scanner.nextLine(); // Wait for user to press Enter
    }

     /**
     * Ensures that the billing system is initialized.
     */

    
    private void ensureBillingInitialized() {
        if (billing == null) {
            billing = new Billing();
        }
    }

    /**
     * Manages staff-related operations such as adding, updating, and removing staff.
     *
     * @param scanner The {@code Scanner} object to read user input.
     * @throws IOException If an I/O error occurs during operations.
     */


    private void manageStaff(Scanner scanner) throws IOException {
        int choice;
        do {
            System.out.println("Manage Staff Menu:");
            System.out.println("1. Add Staff");
            System.out.println("2. Update Staff");
            System.out.println("3. Remove Staff");
            System.out.println("4. Filter Staff");
            System.out.println("5. Back to Admin Menu");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addStaff(scanner);
                    break;
                case 2:
                    updateStaff(scanner);
                    break;
                case 3:
                    removeStaff(scanner);
                    break;
                case 4:
                    filterStaff(scanner);
                    break;
                case 5:
                    System.out.println("Returning to Admin Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    /**
     * Adds a new staff member to the system.
     *
     * @param scanner The {@code Scanner} object to read user input.
     * @throws IOException If an I/O error occurs during saving staff data.
     */

    
    private void addStaff(Scanner scanner) throws IOException {
        System.out.println("Enter Staff Role:");
        String role = scanner.nextLine();

        if (role.equals("doctor")) {
            staffList.add(new Doctor(scanner));
        } else if (role.equals("pharmacist")) {
            staffList.add(new Pharmacist(scanner));
        } else {
            System.out.println("Invalid staff role.");
            return;
        }

        saveStaffData(); // Save changes to the file
        System.out.println("Staff member added successfully.");
    }

    /**
     * Updates an existing staff member's information.
     *
     * @param scanner The {@code Scanner} object to read user input.
     */

    
    private void updateStaff(Scanner scanner) {
        System.out.println("Enter Staff ID to update:");
        String id = scanner.nextLine();
        for (Staff staff : staffList) {
            if (staff.id.equals(id)) {
                System.out.println("Updating Staff: " + staff);
    
                // Update Name
                System.out.print("Enter new Staff Name (or leave blank to keep current): ");
                String newName = scanner.nextLine();
                if (!newName.isEmpty()) {
                    staff.setName(newName);
                }
    
                // Update Phone Number
                while (true) {
                    System.out.print("Enter new Staff Phone Number (or leave blank to keep current): ");
                    String phoneNumber = scanner.nextLine();
                    if (phoneNumber.isEmpty()) break; // Skip updating if blank
                    try {
                        staff.updatePhoneNumber(phoneNumber);
                        break; // Exit loop if successful
                    } catch (Exception error) {
                        System.out.println(error.getMessage());
                    }
                }
    
                // Update Email Address
                while (true) {
                    System.out.print("Enter new Staff Email Address (or leave blank to keep current): ");
                    String email = scanner.nextLine();
                    if (email.isEmpty()) break; // Skip updating if blank
                    try {
                        staff.updateEmailAddress(email);
                        break; // Exit loop if successful
                    } catch (Exception error) {
                        System.out.println(error.getMessage());
                    }
                }
    
                // Save changes in both files
                saveStaffData(); 
                saveUsersData();
    
                System.out.println("Staff member updated successfully.");
                return;
            }
        }
        System.out.println("Staff member not found.");
    }
    
    /**
     * Removes a staff member from the system.
     *
     * @param scanner The {@code Scanner} object to read user input.
     */


    private void removeStaff(Scanner scanner) {
        System.out.println("Enter Staff ID to remove:");
        String id = scanner.nextLine();
    
        System.out.println("Staff list size before removal: " + staffList.size());
    
        // Remove from the in-memory list
        boolean staffFound = staffList.removeIf(staff -> staff.id.equals(id));
    
        if (staffFound) {
            saveStaffData();  // Update staff.csv to reflect changes
            removeUserFromCSV(id);  // Remove user from users.csv
            System.out.println("Staff member removed successfully.");
        } else {
            System.out.println("Staff member not found.");
        }
    
        System.out.println("Staff list size after removal: " + staffList.size());
    }
    
    /**
     * Saves user data to the users.csv file, including administrator and staff data.
     */

    private void saveUsersData() {
        String filePath = "C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\users.csv";
    
        try {
            List<String> updatedLines = Files.lines(Paths.get(filePath))
                    .map(line -> {
                        String[] parts = line.split(",");
                        if (parts[0].equals("id")) return line; // Skip header row
    
                        // Update current admin's details if the ID matches
                        if (this.id.equals(parts[0])) {
                            return this.id + "," + this.name + "," + this.getPassword() + "," + this.role;
                        }
    
                        // Check if the ID matches any staff
                        Staff matchedStaff = staffList.stream()
                                .filter(staff -> staff.id.equals(parts[0]))
                                .findFirst()
                                .orElse(null);
    
                        if (matchedStaff != null) {
                            return matchedStaff.id + "," + matchedStaff.name + "," + matchedStaff.getPassword() + "," + matchedStaff.role;
                        }
    
                        return line; // Preserve non-staff entries as is
                    })
                    .collect(Collectors.toList());
    
            // Ensure admin is always present in the updated lines
            boolean adminExists = updatedLines.stream().anyMatch(line -> line.startsWith(this.id + ","));
            if (!adminExists) {
                updatedLines.add(this.id + "," + this.name + "," + this.getPassword() + "," + this.role);
            }
    
            // Write back all updated lines
            Files.write(Paths.get(filePath), updatedLines);
            System.out.println("User data updated successfully, including administrator.");
        } catch (IOException e) {
            System.out.println("Error updating user data: " + e.getMessage());
        }
    }
    
    
     /**
     * Filters staff members based on role or gender.
     *
     * @param scanner The {@code Scanner} object to read user input.
     */

    

    private void filterStaff(Scanner scanner) {
        System.out.println("Filter Staff Menu:");
        System.out.println("1. Filter by Role");
        System.out.println("2. Filter by Gender");
        System.out.println("3. Back to Manage Staff Menu");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.println("Enter Role to filter:");
                String role = scanner.nextLine();
                staffList.stream()
                        .filter(staff -> staff.getRole().equalsIgnoreCase(role))
                        .forEach(System.out::println);
                break;
            case 2:
                System.out.println("Enter Gender to filter:");
                String gender = scanner.nextLine();
                staffList.stream()
                        .filter(staff -> staff.getGender().equalsIgnoreCase(gender))
                        .forEach(System.out::println);
                break;
            case 3:
                System.out.println("Returning to Manage Staff Menu...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        System.out.println("Press Enter to return to the Manage Staff menu.");
        scanner.nextLine(); // Wait for user to press Enter
    }

    /**
     * Displays all appointments.
     *
     * @param scanner The {@code Scanner} object to read user input.
     */

    private void viewAppointments(Scanner scanner) {
        viewAllAppointments();
        System.out.println("Press Enter to return to the admin menu.");
        scanner.nextLine(); // Wait for user input
    }

    /**
     * Reads and displays all appointments from the appointments.csv file.
     */
    
    private void viewAllAppointments() {
        String filePath = "C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\appointments.csv";
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
    
            // Print a header for the table
            System.out.printf("%-10s %-10s %-10s %-10s %-20s%n", 
                    "ID", "Patient ID", "Doctor ID", "Status", "Date & Time");
            System.out.println("---------------------------------------------------------------");
    
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the header row
                    continue;
                }
    
                String[] values = line.split(",");
                String id = values[0].trim();
                String patientId = values[1].isEmpty() ? "N/A" : values[1].trim();
                String doctorId = values[2].isEmpty() ? "N/A" : values[2].trim();
                String status = values[4].isEmpty() ? "N/A" : values[4].trim();
                String dateAndTime = values[5].trim();
    
                // Print appointment details in a structured format
                System.out.printf("%-10s %-10s %-10s %-10s %-20s%n", 
                        id, patientId, doctorId, status, dateAndTime);
            }
    
        } catch (IOException e) {
            System.out.println("Error reading appointments: " + e.getMessage());
        }
    }
    
    /**
     * Manages inventory-related operations such as viewing, adding, updating, and removing inventory items.
     *
     * @param scanner The {@code Scanner} object to read user input.
     */

    
    private void manageInventory(Scanner scanner) {
        int choice;
        do {
            System.out.println("Manage Inventory Menu:");
            System.out.println("1. View Inventory");
            System.out.println("2. Add Inventory Item");
            System.out.println("3. Update Inventory Item");
            System.out.println("4. Remove Inventory Item");
            System.out.println("5. Approve Replenishment Request");
            System.out.println("6. Back to Admin Menu");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewInventory(scanner);
                    break;
                case 2:
                    addInventoryItem(scanner);
                    break;
                case 3:
                    updateInventoryItem(scanner);
                    break;
                case 4:
                    removeInventoryItem(scanner);
                    break;
                case 5:
                    approveReplenishmentRequest(scanner);
                    break;
                case 6:
                    System.out.println("Returning to Admin Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }

    /**
     * Removes a user from the users.csv file based on user ID.
     *
     * @param userId The unique identifier of the user to remove.
     */
    
    private void removeUserFromCSV(String userId) {
        String filePath = "C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\users.csv";
    
        try {
            List<String> updatedLines = Files.lines(Paths.get(filePath))
                    .filter(line -> !line.startsWith(userId + ","))  // Exclude the user with the given ID
                    .collect(Collectors.toList());
    
            // Write back the updated list
            Files.write(Paths.get(filePath), updatedLines);
            System.out.println("User with ID " + userId + " removed from users.csv successfully.");
        } catch (IOException e) {
            System.out.println("Error removing user from users.csv: " + e.getMessage());
        }
    }
    
     /**
     * Displays the current inventory.
     *
     * @param scanner The {@code Scanner} object to read user input.
     */

    private void viewInventory(Scanner scanner) {
        System.out.println("Current Inventory:");
        inventory.loadFromCSV();
    
        System.out.printf("%-20s %-10s %-12s %-20s %-15s%n", "Medication Name", "Price", "Stock Level", "Low Stock Alert", "Stock Level Alert");
        System.out.println("--------------------------------------------------------------------------------------------");
    
        for (Medication item : inventory.getMedications()) {
            System.out.printf("%-20s %-10d %-12d %-20d %-15s%n",
                    item.getMedicationName(),
                    item.getPrice(),
                    item.getStockLevel(),
                    item.getLowStockAlertLevel(),
                    item.getStockLevelAlert());
        }
    
        System.out.println("Press Enter to return to the Manage Inventory menu.");
        scanner.nextLine(); // Pause for user input
    }
    

    /**
     * Adds a new inventory item.
     *
     * @param scanner The {@code Scanner} object to read user input.
     */

    
    private void addInventoryItem(Scanner scanner) {
        System.out.println("Enter Medicine Name:");
        String name = scanner.nextLine();
        System.out.println("Enter Price:");
        int price = scanner.nextInt();
        System.out.println("Enter Initial Stock Level:");
        int stock = scanner.nextInt();
        System.out.println("Enter Low Stock Alert Level:");
        int lowStockLevel = scanner.nextInt();
    
        Medication newMedication = new Medication(name, price, stock, lowStockLevel);
        newMedication.setStockLevelAlert(stock < lowStockLevel); // Calculate stock alert
        inventory.addMedication(newMedication);
    
        inventory.saveToCSV("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\inventory.csv");
        System.out.println("Inventory item added successfully.");
    }
    
    /**
     * Updates an existing inventory item.
     *
     * @param scanner The {@code Scanner} object to read user input.
     */

    private void updateInventoryItem(Scanner scanner) {
        System.out.println("Enter Medicine Name to update:");
        String name = scanner.nextLine();
        Medication item = inventory.getMedication(name);
    
        if (item != null) {
            System.out.println("Updating Inventory Item: " + item);
    
            System.out.println("Enter new Price (or leave blank to keep current):");
            String newPrice = scanner.nextLine();
    
            System.out.println("Enter new Stock Level (or leave blank to keep current):");
            String newStock = scanner.nextLine();
    
            System.out.println("Enter new Low Stock Alert Level (or leave blank to keep current):");
            String newLowStock = scanner.nextLine();
    
            if (!newPrice.isEmpty()) {
                item.setPrice(Integer.parseInt(newPrice));
            }
            if (!newStock.isEmpty()) {
                int updatedStock = Integer.parseInt(newStock);
                item.updateStockLevel(updatedStock);
                item.setStockLevelAlert(updatedStock < item.getLowStockAlertLevel());
            }
            if (!newLowStock.isEmpty()) {
                item.setLowStockAlertLevel(Integer.parseInt(newLowStock));
            }
    
            inventory.saveToCSV("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\inventory.csv");
            System.out.println("Inventory item updated successfully.");
        } else {
            System.out.println("Inventory item not found.");
        }
    }

    /**
     * Removes an inventory item based on its medication name.
     *
     * @param scanner The {@code Scanner} object to read user input.
     */



    private void removeInventoryItem(Scanner scanner) {
        System.out.println("Enter Medicine Name to remove:");
        String name = scanner.nextLine();
        
        if (inventory.removeMedication(name)) {
            inventory.saveToCSV("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\inventory.csv");
            System.out.println("Inventory item removed successfully.");
        } else {
            System.out.println("Inventory item not found.");
        }
    }
    

    /**
     * Approves a replenishment request for inventory items.
     *
     * @param scanner The {@code Scanner} object to read user input.
     */

    private void approveReplenishmentRequest(Scanner scanner) {
        System.out.println("Enter Replenishment Request ID to approve:");
        String requestID = scanner.nextLine();
        List<ReplenishmentRequest> requests = ReplenishmentRequest.loadFromCSV("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\replenishment_requests.csv");

        for (ReplenishmentRequest request : requests) {
            if (request.getRequestID().equals(requestID)) {
                Medication item = inventory.getMedication(request.getMedicationName());
                if (item != null) {
                    item.updateStockLevel(item.getStockLevel() + request.getQuantity());
                    request.setStatus("Approved");
                    saveInventoryData(); // Save changes to the inventory file
                    saveReplenishmentRequests(requests); // Save changes to the replenishment requests file
                    System.out.println("Replenishment request approved for " + request.getMedicationName());
                } else {
                    System.out.println("Inventory item not found.");
                }
                return;
            }
        }
        System.out.println("Replenishment request not found.");
    }


    /**
     * Saves the list of replenishment requests to the CSV file.
     *
     * @param requests The list of {@link ReplenishmentRequest} objects to save.
     */

    
    private void saveReplenishmentRequests(List<ReplenishmentRequest> requests) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\replenishment_requests.csv"))) {
            writer.println("requestID,medicationName,quantity,status");
            for (ReplenishmentRequest request : requests) {
                writer.printf("%s,%s,%d,%s%n", request.getRequestID(), request.getMedicationName(), request.getQuantity(), request.getStatus());
            }
        } catch (IOException e) {
            System.out.println("Error saving replenishment requests: " + e.getMessage());
        }
    }
    
    /**
     * Creates a bill for a patient based on prescription IDs.
     *
     * @param scanner The {@code Scanner} object to read user input.
     * @throws IOException If an I/O error occurs during billing operations.
     */

    public void createBill(Scanner scanner) throws IOException {
        ensureBillingInitialized();  // Add this line to make sure billing is initialized
int count=0;
        System.out.print("Enter patient ID: ");
        String patientId = scanner.nextLine();

        List<String> prescriptionIds = new ArrayList<>();
        while (true) {
            System.out.print("Enter prescription ID: ");
            String prescriptionId = scanner.nextLine();
            if (!prescriptionId.isEmpty()) {
                prescriptionIds.add(prescriptionId);
                count+=1;
            }

            System.out.print("Do you want to add another prescription? (yes/no): ");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("no")) {
                break;
            }
        }
if(count!=0) {
    String billId = generateId(); // Generate a unique bill ID

    try {
        billing.addBill(billId, patientId, prescriptionIds);
        System.out.println("Bill created successfully with ID: " + billId);
    } catch (Exception e) {
        System.out.println("Failed to create bill: " + e.getMessage());
    }
}
    }


    /**
     * Views bills for a specific patient ID.
     *
     * @throws IOException If an I/O error occurs during billing operations.
     */

    
    public void viewBills() throws IOException {
        ensureBillingInitialized(); // Ensure that the billing is initialized

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();

        List<Billing.Bill> bills = billing.getBillsByPatientId(patientId);
        if (bills.isEmpty()) {
            System.out.println("No bills found for patient ID: " + patientId);
            return;
        }

        // Print the header for better readability
        System.out.printf("%-15s %-12s %-50s %-10s %-6s%n", "Bill ID", "Patient ID", "Description", "Amount", "Is Paid");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        // Print each bill
        for (Billing.Bill bill : bills) {
            bill.viewBill(bill);
        }

        System.out.println("---------------------------------------------------------------------------------------------------------");
    }

    /**
     * Generates a unique identifier for a bill.
     *
     * @return A unique bill ID.
     */
    
    private String generateId() {
        return "BILL" + System.currentTimeMillis();
    }
    
     /**
     * Views feedback from patients.
     */
    
    private void viewFeedback() {
        try {
            List<String> feedbackList = Feedback.viewAllFeedback();
            if (feedbackList.isEmpty()) {
                System.out.println("No feedback available.");
            } else {
                System.out.println("Feedback List:");
                System.out.printf("| %-12s | %-12s | %-50s | %-6s |%n", "Feedback ID", "Patient ID", "Comments", "Rating");
                System.out.println("+--------------+--------------+----------------------------------------------------+--------+");

                for (String feedback : feedbackList) {
                    String[] feedbackDetails = feedback.split(",");

                    if (feedbackDetails.length == 4) { // Ensure data integrity
                        String feedbackId = feedbackDetails[0].trim();
                        String patientId = feedbackDetails[1].trim();
                        String comments = feedbackDetails[2].trim();
                        String rating = feedbackDetails[3].trim();

                        // Break comments into multiple lines if too long
                        List<String> wrappedComments = wrapText(comments, 50);
                        for (int i = 0; i < wrappedComments.size(); i++) {
                            if (i == 0) {
                                System.out.printf("| %-12s | %-12s | %-50s | %-6s |%n",
                                        feedbackId, patientId, wrappedComments.get(i), rating);
                            } else {
                                System.out.printf("| %-12s | %-12s | %-50s | %-6s |%n",
                                        "", "", wrappedComments.get(i), "");
                            }
                        }
                    }
                }
                System.out.println("+--------------+--------------+----------------------------------------------------+--------+");
            }
        } catch (IOException e) {
            System.out.println("Error loading feedback: " + e.getMessage());
        }
    }

    /**
     * Helper method to wrap text to a specified width.
     *
     * @param text  The text to wrap.
     * @param width The maximum width per line.
     * @return A list of wrapped text lines.
     */

    // Helper method to wrap text to a specified width
    private List<String> wrapText(String text, int width) {
        List<String> wrappedLines = new ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + width, text.length());
            wrappedLines.add(text.substring(start, end));
            start = end;
        }
        return wrappedLines;
    }


    /**
     * Verifies the integrity of the billing blockchain.
     */
    
    private void verifyBlockchain() {
        if (Billing.verifyBlockchain()) {
            System.out.println("Blockchain is valid.");
        } else {
            System.out.println("Blockchain integrity compromised!");
        }
    }
    
    /**
     * Loads staff data from the users.csv file.
     */

    private void loadStaffData() {

        try {
            this.staffList.clear();

            BufferedReader file = new BufferedReader(new FileReader("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\users.csv"));
            String nextLine = file.readLine();
            while ((nextLine = file.readLine()) != null) {
                String[] user = nextLine.split(",");
                String role = user[3];

                // Ignore all other types of users. Only handle Doctor and Pharmacist
                if (role.equals("doctor")) {
                    this.staffList.add(new Doctor(user[0], user[1], user[2]));
                } else if (role.equals("pharmacist")) {
                    this.staffList.add(new Pharmacist(user[0], user[1], user[2]));
                }
            }
            file.close();
        } catch (IOException error) {
            System.out.println("Error loading staff data: " + error.getMessage());
        }

    }

    private void loadAppointmentData() {
        // Implement file loading logic for appointment data
        // You can follow a similar structure to loadStaffData
    }
    private void loadInventoryData() {
        inventory.loadFromCSV();
    }

    /**
     * Saves staff data to the staff.csv file.
     */

    private void saveStaffData() {
        String staffFilePath = "C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\staff.csv";
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(staffFilePath))) {
            // Write the header
            writer.write("id,gender,age,role,phoneNumber,email,specialization");
            writer.newLine();
    
            // Write only the current in-memory staff data to the file
            for (Staff staff : staffList) {
                writer.write(String.format("%s,%s,%d,%s,%s,%s,%s",
                        staff.id,
                        staff.getGender(),
                        staff.age,
                        staff.getRole(),
                        staff.phoneNumber,
                        staff.emailAddress,
                        staff.specialization));
                writer.newLine();
            }
            System.out.println("Staff data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving staff data: " + e.getMessage());
        }
    }
    

    private void saveInventoryData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\inventory.csv"))) {
            // Write the header with 5 fields
            bw.write("medicationName,price,stockLevel,lowStockAlertLevel,stockLevelAlert");
            bw.newLine();
    
            // Write each medication's data
            for (Medication item : inventory.getMedications()) {
                bw.write(String.format("%s,%d,%d,%d,%b%n",
                        item.getMedicationName(),
                        item.getPrice(),
                        item.getStockLevel(),
                        item.getLowStockAlertLevel(),
                        item.getStockLevelAlert())); // Save alert as true/false
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory data: " + e.getMessage());
        }
    }
    

}
