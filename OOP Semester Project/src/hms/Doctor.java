package hms;


import hms.Appointments.Appointment;
import hms.Appointments.AppointmentStatus;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Doctor extends Staff {

    public Doctor(Scanner scanner) throws IOException {
        super(scanner, "doctor");

        try {
            super.save();
        } catch (IOException error) {
            System.out.println("Unable to save user " + name + " due to IOException: " + error.getMessage());
        }
    }

    public Doctor(String id, String name, String password) throws IOException {
        super(id, name, password, "doctor");
    }

    // Basically a findDoctorById
    public Doctor(String id) throws IOException {
        super(id);
    }

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
                9. Log Out
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
                    for (String[] record : medicalRecords) {
                        System.out.println(String.join(", ", record));
                    }
                    System.out.print("Enter the index of the record to update: ");
                    int index = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter the updated record: ");
                    String updatedRecord = scanner.nextLine();
                    medicalRecords.get(index)[1] = updatedRecord;
                    updateMedicalRecord(patientId2, medicalRecords.get(index));
                } catch (IOException e) {
                    System.out.println("Error reading medical records: " + e.getMessage());
                }
                break;
            case 3:
                viewPersonalSchedule();
                break;
            case 4:
                setAvailabilityForAppointments(scanner);
                break;
            case 5:
                viewPendingRequests(scanner);  // Interactive function for pending requests
                break;
            case 6:
                System.out.print("Enter the appointment ID: ");
                String appointmentID2 = scanner.nextLine();
                System.out.print("Enter the date of the appointment: ");
                String dateOfAppointment = scanner.nextLine();
                System.out.print("Enter the service type: ");
                String serviceType = scanner.nextLine();
                System.out.print("Enter the prescribed medications (separated by commas): ");
                String[] prescribedMedications = scanner.nextLine().split(",");
                List<Prescription> prescriptions = new ArrayList<>();
                for (String medication : prescribedMedications) {
                    int prescriptionID = new Random().nextInt(9000) + 1000;
                    String prescriptionId = Integer.toString(prescriptionID);
                    prescriptions.add(new Prescription(prescriptionId,medication,PrescriptionStatus.Pending));
                }
                System.out.print("Enter the consultation notes: ");
                String consultationNotes = scanner.nextLine();
                recordAppointmentOutcome(appointmentID2, new Date(Long.parseLong(dateOfAppointment)), serviceType, prescriptions, consultationNotes);
                break;
            case 7:
                cancelAnyAppointment(scanner); // New case to cancel appointments
                break;
            case 8:
                clearAppointments();
                break;
            case 9:
                return false;
            default:
                System.out.println("Invalid choice. Please enter a number from 1 to 7.");
                break;
        }
        return true;
    }

    // TODO: Add proper Doctor formatting
    public String toString() {
        // System.out.printf("%s - %s - %s, %s, %s", this.id, this.role, this.name, this.age, this.gender);
        return this.id + " - Doctor - " + this.name;
    }


    public List<Appointment> viewAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String line;
        String separator = ",";
    
        try (BufferedReader br = new BufferedReader(new FileReader("../data/appointments.csv"))) {
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
                    accept(selectedAppointmentId);
                    System.out.println("Appointment accepted successfully.\n");
                }
                case 2 -> {
                    decline(selectedAppointmentId);
                    System.out.println("Appointment declined successfully.\n");
                }
                
                default -> System.out.println("Invalid choice. Returning to menu.\n");
            }
    
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.\n");
            scanner.nextLine(); // Clear the invalid input
        }
    }
    

    public void accept(String appointmentID) {
        try {
            List<Appointment> appointments = viewAppointments();
            boolean updated = false;
            
            for (Appointment appointment : appointments) {
                if (appointment.getId().equals(appointmentID)) {
                    appointment.setStatus(Optional.of(AppointmentStatus.confirmed));
                    appointment.save();
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
    
    public void decline(String appointmentID) {
        try {
            List<Appointment> appointments = viewAppointments();
            boolean updated = false;
    
            for (Appointment appointment : appointments) {
                if (appointment.getId().equals(appointmentID)) {
                    appointment.setStatus(Optional.of(AppointmentStatus.cancelled));
                    appointment.save();
                    updated = true;
                    System.out.printf("Appointment ID %s has been declined and set to CANCELLED.%n", appointmentID);
                    break;
                }
            }
    
            if (!updated) {
                System.out.printf("Appointment ID %s not found or already processed.%n", appointmentID);
            }
        } catch (IOException e) {
            System.out.println("Error while declining the appointment: " + e.getMessage());
        }
    }

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
    public void recordAppointmentOutcome(String appointmentID, Date dateOfAppointment, String serviceType, List<Prescription> prescribedMedications, String consultationNotes) {
        AppointmentOutcomeRecord outcomeRecord = new AppointmentOutcomeRecord(appointmentID, dateOfAppointment, serviceType, prescribedMedications, consultationNotes);

        // Save the outcome record to the CSV file
        try (PrintWriter pw = new PrintWriter(new FileWriter("../data/appointment_outcome_records.csv", true))) {
            StringBuilder sb = new StringBuilder();
            sb.append(outcomeRecord.getAppointmentID()).append(",");
            sb.append(outcomeRecord.getDateOfAppointment().getTime()).append(",");
            sb.append(outcomeRecord.getServiceType()).append(",");
            sb.append(outcomeRecord.getPrescribedMedications().stream().map(Prescription::getID).collect(Collectors.joining(";"))).append(",");
            sb.append(outcomeRecord.getConsultationNotes());
            pw.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<String[]> viewMedicalRecord(String patientId) throws IOException {
        List<String[]> medicalRecords = new ArrayList<>();
        String line;
        String separator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader("../data/medical_record.csv"))) {
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
    public void clearAppointments() {
        String filePath = "../data/appointments.csv";
    
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
    
    public void updateMedicalRecord(String patientId, String[] updatedRecord) throws IOException {
        List<String[]> medicalRecords = new ArrayList<>();
        String line;
        String separator = ",";

        try (BufferedReader br = new BufferedReader(new FileReader("../data/medical_record.csv"))) {
            // Read all lines and store them in a list
            while ((line = br.readLine()) != null) {
                String[] record = line.split(separator);
                if (record.length > 0 && record[0].equals(patientId)) {
                    medicalRecords.add(updatedRecord); // Add updated record
                } else {
                    medicalRecords.add(record); // Add existing record
                }
            }
        }

        // Write the updated records back to the CSV file
        try (PrintWriter pw = new PrintWriter(new FileWriter("../data/medical_record.csv"))) {
            for (String[] record : medicalRecords) {
                pw.println(String.join(separator, record));
            }
        }
    }
    private void viewPersonalSchedule() {
        String filePath = "../data/appointments.csv";
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
    private void setAvailabilityForAppointments(Scanner scanner) {

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

    //Making helper function to generate time slots
    private List<Appointment> generateTimeSlots(LocalDateTime start, LocalDateTime end, int intervalMinutes) {
        List<Appointment> slots = new ArrayList<>();
        LocalDateTime slotTime = start;
        Random random=new Random();
        while (slotTime.isBefore(end)) {
            int AppointmentID=random.nextInt(9000)+1000;
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



    // Placeholder method for "View Upcoming Appointments"
    private void viewUpcomingAppointments() {
        System.out.println("Viewing upcoming appointments... (placeholder)");
    }

    // Placeholder method for "Record Appointment Outcomes"
    private void recordAppointmentOutcomes() {
        System.out.println("Recording appointment outcomes... (placeholder)");
    }

}

//         } catch (IOException e) {
//             e.printStackTrace();
//         }

//         return null; // Return null if username not found
//     }

//     public String getPhoneNumber(String Username) {
//         String line;
//         String separator = ",";

//         try (BufferedReader br = new BufferedReader(new FileReader(path))) {
//             br.readLine(); // Skip header row

//             while ((line = br.readLine()) != null) {
//                 String[] values = line.split(separator);

//                 if (values.length >= 9 && values[8].equals(Username)) { // Username at index 8
//                     return values[5]; // phoneNumber at index 5
//                 }
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }

//         return null; // Return null if username not found
//     }

//     public String getEmailAddress(String Username) {
//         String line;
//         String separator = ",";

//         try (BufferedReader br = new BufferedReader(new FileReader(path))) {
//             br.readLine(); // Skip header row

//             while ((line = br.readLine()) != null) {
//                 String[] values = line.split(separator);

//                 if (values.length >= 9 && values[8].equals(Username)) { // Username at index 8
//                     return values[6]; // emailAddress at index 6
//                 }
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }

//         return null; // Return null if username not found
//     }
// }
