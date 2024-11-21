package hms;


import hms.Appointments.Appointment;
import hms.Appointments.AppointmentStatus;
import hms.appointment_outcome_record.AppointmentOutcomeRecord;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * The {@code Doctor} class represents a doctor in the hospital management system.
 * It extends the {@link Staff} class and provides functionalities specific to doctors,
 * such as managing appointments, viewing and updating medical records, and recording appointment outcomes.
 */

public class Doctor extends Staff {

    /**
     * Constructs a new {@code Doctor} instance by prompting the user for input via a {@code Scanner}.
     * This constructor is typically used when creating a new doctor account.
     *
     * @param scanner The {@code Scanner} object to read user input.
     * @throws IOException If an I/O error occurs during saving the user data.
     */


    public Doctor(Scanner scanner) throws IOException {
        super(scanner, "doctor");
        try {
            super.save();
        } catch (IOException error) {
            System.out.println("Unable to save user " + name + " due to IOException: " + error.getMessage());
        }
    }

    /**
     * Saves the doctor's data to the file system.
     *
     * @throws IOException If an I/O error occurs during file operations.
     */
    // Doctor.java
    @Override
    public void save() throws IOException {
        // staff.csv: id,gender,age,role,phoneNumber,email,specialization
        List<String> lines = Files.readAllLines(Paths.get("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\Patient.csv"));
        FileOutputStream output = new FileOutputStream("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\Patient.csv");

        boolean isEntryFound = false;
        for (int i = 0; i < lines.size(); i++) {
            String[] staff = lines.get(i).split(",");

            if (staff.length == 7 && staff[0].equals(this.id)) {
                String newEntry = this.id + "," + this.gender.toString().toLowerCase() + "," + this.age + ","
                        + this.role + "," + this.phoneNumber + "," + this.emailAddress + "," + this.specialization + "\n";
                output.write(newEntry.getBytes());
                isEntryFound = true;
            } else {
                String line = lines.get(i) + "\n";
                output.write(line.getBytes());
            }
        }

        if (!isEntryFound) {
            String newEntry = this.id + "," + this.gender.toString().toLowerCase() + "," + this.age + ","
                    + this.role + "," + this.phoneNumber + "," + this.emailAddress + "," + this.specialization + "\n";
            output.write(newEntry.getBytes());
        }

        output.close();
    }

    /**
     * Constructs a new {@code Doctor} instance with the specified ID, name, and password.
     * This constructor is typically used when loading an existing doctor account.
     *
     * @param id       The unique identifier for the doctor.
     * @param name     The name of the doctor.
     * @param password The password for the doctor.
     * @throws IOException If an I/O error occurs during operations.
     */


    public Doctor(String id, String name, String password) throws IOException {
        super(id, name, password, "doctor");
    }

    // TODO: Add EventLoop for all Doctor Menu items
    public boolean eventLoop(Scanner scanner) {
        System.out.print("""
                Doctor Menu:
                1. View Patient Medical Records
                2. Update Patient Medical Records
                3. View Personal Schedule
                4. Set Availability for Appointments
                5. Accept or Decline Appointment Requests
                6. Record Appointment Outcome\s
                7. Cancel Appointments
                8. Clear Appointments
                9. Add diagnosis/treatments
                10. Log Out
                Enter your choice:""");

        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                System.out.print("Enter the patient ID: ");
                String patientId = scanner.nextLine();
                try {
                    List<String[]> medicalRecords = viewMedicalRecord(patientId);
                    for (String[] record : medicalRecords) {
                        System.out.println(String.join(", ", record));
                    }
                } catch (IOException e) {
                    System.out.println("Error reading medical records: " + e.getMessage());
                }
                break;
            case 2:
                System.out.print("Enter the patient ID: ");
                String patientId2 = scanner.nextLine();
                try {
                    List<String[]> medicalRecords = viewMedicalRecord(patientId2);
                    if (medicalRecords.isEmpty()) {
                        System.out.println("No records found for the given patient ID.");
                    } else {
                        String[] recordToUpdate = medicalRecords.get(0); // Assuming one record per patient ID
                        
                        System.out.println("Current Record:");
                        System.out.println(String.join(", ", recordToUpdate));
            
                        // Display fields with their indices for easy selection
                        for (int i = 1; i < recordToUpdate.length; i++) { // Skipping Patient ID (index 0)
                            System.out.printf("%d. %s: %s%n", i, getFieldName(i), recordToUpdate[i]);
                        }
            
                        System.out.print("Enter the number corresponding to the field you want to update (or 0 to cancel): ");
                        int fieldIndex = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
            
                        if (fieldIndex == 0) {
                            System.out.println("Update cancelled.");
                            break;
                        }
            
                        if (fieldIndex < 1 || fieldIndex >= recordToUpdate.length) {
                            System.out.println("Invalid field number. Please try again.");
                        } else {
                            System.out.printf("Current %s: %s. Enter new value: ", getFieldName(fieldIndex), recordToUpdate[fieldIndex]);
                            String newValue = scanner.nextLine();
                            recordToUpdate[fieldIndex] = newValue.trim();
            
                            updateMedicalRecord(patientId2, recordToUpdate);
                            System.out.println("Medical record updated successfully.");
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error updating medical records: " + e.getMessage());
                }
                break;
            
            
            case 3:
                viewPersonalSchedule();
                break;
            case 4:
                setAvailabilityForAppointments();
                break;
            case 5:
                viewPendingRequests(scanner);  // Interactive function for pending requests
                break;
            case 6:
                recordAppointmentOutcome(scanner);
                break;
            case 7:
                cancelAnyAppointment(scanner); // New case to cancel appointments
                break;
            case 8:
                clearAppointments();
                break;
            case 9:
                addDiagnosisTreatments();
                break;
            case 10:
                System.out.println("Signed out successfully.");
                return false;
            default:
                System.out.println("Invalid choice. Please enter a number from 1 to 7.");
                break;
        }
        return true;
    }

    /**
     * Displays the doctor's information.
     *
     * @return A string representation of the doctor.
     */

    // TODO: Add proper Doctor formatting
    public String toString() {
        // System.out.printf("%s - %s - %s, %s, %s", this.id, this.role, this.name, this.age, this.gender);
        return this.id + " - Doctor - " + this.name;
    }

     /**
     * Retrieves the list of appointments assigned to the doctor.
     *
     * @return A list of {@link Appointment} objects.
     */


    public List<Appointment> viewAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String line;
        String separator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\appointments.csv"))) {
            br.readLine(); // Skip header row

            while ((line = br.readLine()) != null) {
                String[] values = line.split(separator);
                if (values.length >= 6 && values[2].equals(this.id)) {
                    String status = values[4].toLowerCase(); // Case-insensitive comparison

                    Optional<AppointmentStatus> apptStatus = switch (status) {
                        case "confirmed" -> Optional.of(AppointmentStatus.confirmed);
                        case "cancelled" -> Optional.of(AppointmentStatus.cancelled);
                        case "completed" -> Optional.of(AppointmentStatus.completed);
                        case "pending" -> Optional.of(AppointmentStatus.pending);
                        default -> Optional.empty();
                    };

                    appointments.add(new Appointment(
                            values[0],
                            Optional.ofNullable(values[1].isEmpty() ? null : values[1]),
                            values[2],
                            apptStatus,
                            LocalDateTime.parse(values[5]),
                            Optional.ofNullable(values[3].isEmpty() ? null : values[3])
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    /**
     * Interactively views and manages pending appointment requests.
     *
     * @param scanner The {@code Scanner} object to read user input.
     */

    
    private void viewPendingRequests(Scanner scanner) {
        try {
            System.out.println("\nPending Appointment Requests:\n");
            List<Appointment> appointments = viewAppointments();
            boolean foundPending = false;

            for (Appointment appt : appointments) {
                if (appt.getStatus().isPresent() && appt.getStatus().get() == AppointmentStatus.pending) {
                    System.out.printf("Appointment ID: %s | Patient ID: %s | Date: %s | Status: PENDING%n",
                            appt.getId(),
                            appt.getPatientId().orElse("Unassigned"),
                            appt.getDateTime());
                    foundPending = true;
                }
            }

            if (!foundPending) {
                System.out.println("No pending appointment requests.\n");
                return;  // Exit if no pending requests
            }

            System.out.println("\nSelect an appointment to manage:");
            System.out.print("Enter Appointment ID or type 'back' to return: ");
            String selectedAppointmentId = scanner.nextLine();

            if (selectedAppointmentId.equalsIgnoreCase("back")) {
                return;  // Exit if user wants to go back
            }

            // Search for the selected appointment
            Appointment selectedAppt = null;
            for (Appointment appt : appointments) {
                if (appt.getId().equals(selectedAppointmentId) && appt.getStatus().isPresent() &&
                        appt.getStatus().get() == AppointmentStatus.pending) {
                    selectedAppt = appt;
                    break;
                }
            }

            if (selectedAppt == null) {
                System.out.println("Invalid Appointment ID or the appointment is not pending.");
                return;
            }

            // Provide action choices
            System.out.print("""
                    What would you like to do with this appointment?
                    1. Accept
                    2. Decline
                    Enter your choice: """);

            int actionChoice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (actionChoice) {
                case 1 -> {
                    acceptappointment(selectedAppointmentId);
                    System.out.println("Appointment accepted successfully.\n");
                }
                case 2 -> {
                    declineappointment(selectedAppointmentId);
                    System.out.println("Appointment declined successfully.\n");
                }

                default -> System.out.println("Invalid choice. Returning to menu.\n");
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.\n");
            scanner.nextLine(); // Clear the invalid input
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFieldName(int index) {
        return switch (index) {
            case 1 -> "Name";
            case 2 -> "Date of Birth";
            case 3 -> "Gender";
            case 4 -> "Phone Number";
            case 5 -> "Email Address";
            case 6 -> "Blood Type";
            case 7 -> "Diagnosis";
            case 8 -> "Treatments";
            case 9 -> "Allergies";
            default -> "Unknown Field";
        };
    }
    
    /**
     * Accepts a pending appointment.
     *
     * @param appointmentID The ID of the appointment to accept.
     */

    public void acceptappointment(String appointmentID) {
        try {
            List<Appointment> appointments = viewAppointments();
            boolean updated = false;

            for (Appointment appointment : appointments) {
                if (appointment.getId().equals(appointmentID)) {
                    appointment.accept();
                    updated = true;
                    System.out.printf("Appointment ID %s has been accepted and set to CONFIRMED.%n", appointmentID);
                    break;
                }
            }

            if (!updated) {
                System.out.printf("Appointment ID %s not found or already processed.%n", appointmentID);
            }
        } catch (IOException e) {
            System.out.println("Error while accepting the appointment: " + e.getMessage());
        }
    }

    /**
     * Declines a pending appointment.
     *
     * @param appointmentID The ID of the appointment to decline.
     * @throws IOException If an I/O error occurs during operations.
     */
    
    public void declineappointment(String appointmentID) throws IOException {
        List<Appointment> appointments = viewAppointments();
        boolean updated = false;

        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(appointmentID)) {
                appointment.decline();
                updated = true;
                System.out.printf("Appointment ID %s has been declined and set to CANCELLED.%n", appointmentID);
                break;
            }
        }

        if (!updated) {
            System.out.printf("Appointment ID %s not found or already processed.%n", appointmentID);
        }
    }

    /**
     * Cancels any appointment selected by the doctor.
     *
     * @param scanner The {@code Scanner} object to read user input.
     */

    private void cancelAnyAppointment(Scanner scanner) {
        List<Appointment> appointments = viewAppointments();

        System.out.println("\nAll Appointments:\n");

        for (Appointment appt : appointments) {
            System.out.printf("Appointment ID: %s | Patient ID: %s | Date: %s | Status: %s%n",
                    appt.getId(),
                    appt.getPatientId().orElse("Unassigned"),
                    appt.getDateTime(),
                    appt.getStatus().map(Enum::name).orElse("None"));
        }

        System.out.print("\nEnter Appointment ID to cancel or type 'back' to return: ");
        String selectedAppointmentId = scanner.nextLine();

        if (selectedAppointmentId.equalsIgnoreCase("back")) {
            return;
        }

        Appointment selectedAppt = appointments.stream()
                .filter(appt -> appt.getId().equals(selectedAppointmentId))
                .findFirst()
                .orElse(null);

        if (selectedAppt == null) {
            System.out.println("Invalid Appointment ID.");
            return;
        }

        cancel(selectedAppointmentId);
        System.out.println("Appointment cancelled successfully.\n");
    }

    /**
     * Cancels an appointment by setting its status to cancelled.
     *
     * @param appointmentID The ID of the appointment to cancel.
     */
    
    public void cancel(String appointmentID) {
        try {
            List<Appointment> appointments = viewAppointments();
            boolean updated = false;

            for (Appointment appointment : appointments) {
                if (appointment.getId().equals(appointmentID)) {
                    appointment.setStatus(Optional.of(AppointmentStatus.cancelled));
                    appointment.save();
                    updated = true;
                    System.out.printf("Appointment ID %s has been cancelled and set to CANCELLED.%n", appointmentID);
                    break;
                }
            }

            if (!updated) {
                System.out.printf("Appointment ID %s not found or already processed.%n", appointmentID);
            }
        } catch (IOException e) {
            System.out.println("Error while cancelling the appointment: " + e.getMessage());
        }
    }

    public void complete(String appointmentID) {
        List<Appointment> appointments = viewAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(appointmentID)) {
                appointment.setStatus(Optional.of(AppointmentStatus.completed));
                try {
                    appointment.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * Records the outcome of an appointment.
     *
     * @param scanner The {@code Scanner} object to read user input.
     */
    
    public void recordAppointmentOutcome(Scanner scanner) {
        // Step 1: Ask for Appointment ID directly
        System.out.print("Enter the appointment ID: ");
        String appointmentID = scanner.nextLine();

        // Step 2: Ask for Appointment Date directly
        System.out.print("Enter the date of the appointment (yyyy-MM-dd): ");
        String dateOfAppointmentStr = scanner.nextLine();
        Date dateOfAppointment;
        try {
            dateOfAppointment = Date.from(LocalDate.parse(dateOfAppointmentStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    .atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return;
        }

        // Step 3: Gather other details for the appointment outcome
        System.out.print("Enter the service type: ");
        String serviceType = scanner.nextLine();

        System.out.println("Enter prescribed medications in the format 'name:quantity' separated by commas:");
        String[] medicationEntries = scanner.nextLine().split(",");

        List<Prescription> prescriptions = new ArrayList<>();
        for (String entry : medicationEntries) {
            String[] parts = entry.trim().split(":");
            if (parts.length != 2) {
                System.out.println("Invalid format for medication entry: " + entry);
                continue;  // Skip invalid entries
            }
            String medicationName = parts[0].trim();
            int quantity = Integer.parseInt(parts[1].trim());
            prescriptions.add(new Prescription(
                    Prescription.generateRandomPrescriptionID(),
                    medicationName,
                    quantity,
                    PrescriptionStatus.Pending
            ));
        }

        System.out.print("Enter consultation notes: ");
        String consultationNotes = scanner.nextLine();

        // Step 4: Create and save the appointment outcome record
        AppointmentOutcomeRecord record = new AppointmentOutcomeRecord(
                appointmentID,
                dateOfAppointment,
                serviceType,
                prescriptions,
                consultationNotes
        );

        record.saveToCSV("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\appointment_outcome_records.csv");
        System.out.println("Appointment outcome recorded successfully.");

        try {
            List<Appointment> appointments = viewAppointments();
            for (Appointment appt : appointments) {
                if (appt.getId().equals(appointmentID)) {
                    appt.setStatus(Optional.of(AppointmentStatus.completed));  // Set status to completed
                    appt.save();  // Save the updated appointment
                    System.out.println("Appointment status updated to COMPLETED.");  // Print only one line
                    return;  // Exit after updating
                }
            }
            System.out.println("No matching appointment found to update.");
        } catch (IOException e) {
            System.out.println("Error updating appointment status: " + e.getMessage());
        }

    }


    public String getName() {
        return this.name; // or appropriate field
    }


    /**
     * Views the medical records of a patient.
     *
     * @param patientId The ID of the patient whose records are to be viewed.
     * @return A list of string arrays representing the medical records.
     * @throws IOException If an I/O error occurs during file reading.
     */
    
    public List<String[]> viewMedicalRecord(String patientId) throws IOException {
        List<String[]> medicalRecords = new ArrayList<>();
        String line;
        String separator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\Patient.csv"))) {
            // Skip header row
            br.readLine();

            // Process each line in the CSV file
            while ((line = br.readLine()) != null) {
                String[] record = line.split(separator);
                if (record.length > 0 && record[0].equals(patientId)) {
                    medicalRecords.add(record);
                }
            }
        }

        return medicalRecords;
    }

    /**
     * Clears all appointments by resetting the appointments file.
     */
    
    public void clearAppointments() {
        String filePath = "C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\appointments.csv";

        try {
            // Read all lines, retaining only the header
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            if (lines.isEmpty()) {
                System.out.println("No data found in the appointments file.");
                return;
            }

            // Get the header (assuming the first line is the header)
            String header = lines.get(0);

            // Overwrite the file with only the header
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(header);
                writer.newLine();
            }

            System.out.println("All appointments have been cleared successfully, retaining the header.");
        } catch (IOException e) {
            System.out.println("Error clearing appointments: " + e.getMessage());
        }
    }

     /**
     * Updates a patient's medical record.
     *
     * @param patientId     The ID of the patient whose record is to be updated.
     * @param updatedRecord An array of strings representing the updated record.
     * @throws IOException If an I/O error occurs during file operations.
     */
    
    public void updateMedicalRecord(String patientId, String[] updatedRecord) throws IOException {
        String filePath = "C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\Patient.csv";
        String separator = ",";
        List<String> lines = new ArrayList<>();
    
        // Read all lines from the file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
    
        // Update the patient record
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                String[] record = line.split(separator);
                if (record.length > 0 && record[0].equals(patientId)) {
                    pw.println(String.join(separator, updatedRecord)); // Write updated record
                } else {
                    pw.println(line); // Write unchanged lines
                }
            }
        }
    }
    
    /**
     * Views the doctor's personal schedule of confirmed appointments.
     */
    
    private void viewPersonalSchedule() {
        String filePath = "C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\appointments.csv";
        List<Appointment> personalSchedule = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header row

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 6 && values[2].equals(this.id) && values[4].equalsIgnoreCase("Confirmed")) {
                    String appointmentID = values[0];
                    String patientID = values[1].isEmpty() ? "Unassigned" : values[1];
                    LocalDateTime dateTime = LocalDateTime.parse(values[5], DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

                    personalSchedule.add(new Appointment(
                            appointmentID,
                            Optional.ofNullable(patientID.equals("Unassigned") ? null : patientID),
                            this.id,
                            Optional.of(AppointmentStatus.confirmed),
                            dateTime,
                            Optional.empty()
                    ));
                }
            }

            // Sort appointments by date/time for display
            personalSchedule.sort(Comparator.comparing(Appointment::getDateTime));

            // Display the personal schedule
            System.out.println("\nYour Personal Schedule (Confirmed Appointments):");
            if (personalSchedule.isEmpty()) {
                System.out.println("No confirmed appointments scheduled.");
            } else {
                for (Appointment appointment : personalSchedule) {
                    System.out.printf("Appointment ID: %s | Patient ID: %s | Date: %s%n",
                            appointment.getId(),
                            appointment.getPatientId().orElse("Unassigned"),
                            appointment.getDateTime());
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading personal schedule: " + e.getMessage());
        }
    }


    // Placeholder method for "Set Availability for Appointments"
    private void setAvailabilityForAppointments() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter start date and time (e.g., 2024-12-01 09:00): ");
            String startInput = scanner.nextLine();
            LocalDateTime startDateTime = LocalDateTime.parse(startInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            System.out.print("Enter end time (e.g., 2024-12-01 17:00): ");
            String endInput = scanner.nextLine();
            LocalDateTime endDateTime = LocalDateTime.parse(endInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            System.out.print("Enter slot duration in minutes (e.g., 30 for 30-minute slots): ");
            int slotDuration = scanner.nextInt();
            scanner.nextLine(); // consume newline

            List<Appointment> newSlots = generateTimeSlots(startDateTime, endDateTime, slotDuration);
            saveTimeSlots(newSlots);

            System.out.println("Availability has been set successfully!");
        } catch (Exception e) {
            System.out.println("Error parsing date/time. Please try again.");
        }
    }    

    /**
     * Generates time slots for appointments based on the provided parameters.
     *
     * @param start           The start date and time.
     * @param end             The end date and time.
     * @param intervalMinutes The duration of each slot in minutes.
     * @return A list of generated {@link Appointment} slots.
     */


    //Making helper function to generate time slots
    private List<Appointment> generateTimeSlots(LocalDateTime start, LocalDateTime end, int intervalMinutes) {
        List<Appointment> slots = new ArrayList<>();
        LocalDateTime slotTime = start;
        Random random = new Random();
        while (slotTime.isBefore(end)) {
            int AppointmentID = random.nextInt(9000) + 1000;
            String appointmentID = Integer.toString(AppointmentID);
            Appointment slot = new Appointment(
                    appointmentID,          // appointmentId
                    Optional.empty(),       // patientId (no patient yet)
                    this.id,                // doctor's ID
                    Optional.empty(),       // status
                    slotTime,
                    Optional.empty()        // outcomeRecordId
            );
            slots.add(slot);
            slotTime = slotTime.plusMinutes(intervalMinutes);
        }
        return slots;
    }    

    /**
     * Saves the generated appointment time slots to the file system.
     *
     * @param slots The list of {@link Appointment} slots to save.
     */

    //Making helper function to save time slots
    private void saveTimeSlots(List<Appointment> slots) {
        for (Appointment slot : slots) {
            try {
                slot.save();
            } catch (IOException e) {
                System.out.println("Error saving appointment slot: " + e.getMessage());
            }
        }
    }
    public void addDiagnosisTreatments() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the patient ID: ");
        String patientId3 = scanner.nextLine();
    
        try {
            List<String[]> medicalRecords = viewMedicalRecord(patientId3);
            if (medicalRecords.isEmpty()) {
                System.out.println("No medical records found for patient ID: " + patientId3);
            } else {
                String[] record = medicalRecords.get(0);
                System.out.println(String.join(", ", record));
    
                // Simplified diagnosis handling
                System.out.print("Enter the diagnosis (press enter to skip): ");
                String diagnosis = scanner.nextLine();
                if (!diagnosis.trim().isEmpty()) {
                    record[7] = (record[7].equalsIgnoreCase("Not Available") 
                                ? diagnosis.trim() : record[7] + ";" + diagnosis.trim());
                }
    
                // Simplified treatments handling
                System.out.print("Enter the treatments (press enter to skip): ");
                String treatments = scanner.nextLine();
                if (!treatments.trim().isEmpty()) {
                    record[8] = (record[8].equalsIgnoreCase("Not Available") 
                                ? treatments.trim() : record[8] + ";" + treatments.trim());
                }
    
                // Allergy handling
                System.out.print("Enter a new allergy (press enter to skip): ");
                String allergy = scanner.nextLine();
                if (!allergy.trim().isEmpty()) {
                    List<String> allergies = record.length > 9 
                        ? new ArrayList<>(Arrays.asList(record[9].split(";"))) : new ArrayList<>();
                    if (!allergies.contains(allergy)) {
                        allergies.add(allergy);
                        record[9] = String.join(";", allergies);
                    }
                }
    
                updateMedicalRecord(patientId3, record);
                System.out.println("Medical record updated successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error reading medical records: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
    

    // Placeholder method for "View Upcoming Appointments"
    private void viewUpcomingAppointments() {
        System.out.println("Viewing upcoming appointments... (placeholder)");
    }

    // Placeholder method for "Record Appointment Outcomes"
    private void recordAppointmentOutcomes() {
        System.out.println("Recording appointment outcomes... (placeholder)");
    }

}
