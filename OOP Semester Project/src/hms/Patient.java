package hms;
import hms.Appointments.Appointment;
import hms.Appointments.AppointmentPatientView;
import hms.Appointments.AppointmentStatus;
import hms.Billing.Billing;
import hms.MedicalRecords.MedicalRecordPatientView;
import hms.MedicalRecords.MedicalRecords;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The {@code Patient} class represents a patient user in the hospital management system.
 * It extends the {@link User} class and provides functionalities specific to patients,
 * such as viewing and updating medical records, scheduling appointments, and giving feedback.
 */


public class Patient extends User {

    /**
     * The patient's medical record, viewed through a patient-specific interface.
     * This ensures that only methods intended for patients are accessible.
     */
    
    // Store MedicalRecord as a PatientView so only Patient methods are exposed.
    private MedicalRecordPatientView patientRecord;

    // Prompt for information about patient

    /**
     * Constructs a new {@code Patient} by prompting for information via a {@code Scanner}.
     * Initializes the patient record and saves it to file.
     *
     * @param scanner The {@code Scanner} object for reading user input.
     * @throws IOException If an I/O error occurs during saving operations.
     */

    
    public Patient(Scanner scanner) throws IOException {
        super(scanner, "patient"); // Creates base User
        
        try {
            super.save(); // Save to users.csv
        } catch (IOException error) {
            System.out.println("Unable to save user " + name + " due to IOException: " + error.getMessage());
        }
        
        this.patientRecord = new MedicalRecords(scanner, this.id, this.name); // Create Patient Medical Record
        
        // Save the new MedicalRecord to Patient.csv immediately
        try {
            this.patientRecord.saveToFile();
            System.out.println("Medical Record for Patient saved successfully.");
        } catch (IOException error) {
            System.out.println("Error saving Medical Record: " + error.getMessage());
        }
    }


    /**
     * Constructs a new {@code Patient} with the specified ID, name, and password.
     * Loads the patient's medical record if it exists; otherwise, creates a new one.
     *
     * @param id       The patient's ID.
     * @param name     The patient's name.
     * @param password The patient's password.
     * @throws IOException If an I/O error occurs during operations.
     */

    
    public Patient(String id, String name, String password) throws IOException {
        super(id, name, password, "patient");
    
        this.patientRecord = MedicalRecords.getRecord(this, id);
        
        if (this.patientRecord == null) {
            this.patientRecord = new MedicalRecords(new Scanner(System.in), id, name); 
            this.patientRecord.saveToFile(); // Save new MedicalRecords if not found
        }
    }

    /**
     * The main event loop for the patient, providing a menu of options to interact with the system.
     *
     * @param scanner The {@code Scanner} object for reading user input.
     * @return {@code true} to continue the loop; {@code false} to log out.
     * @throws IOException If an I/O error occurs during operations.
     */
    
    public boolean eventLoop(Scanner scanner) throws IOException {
        System.out.print("""
                Patient Menu:
                1. View Medical Records
                2. Update Email or Phone Number
                3. Schedule New Appointment
                4. Reschedule/Cancel Existing Appointment
                5. View all your Appointment Statuses
                6. View Appointment Outcome Records
                7. Add past diagnosis and treatment
                8. Add Allergy to Medical Record
                9. Pay Bill
                10. Give Feedback
                11. Log Out
                Enter your choice:""");
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.println("");
        switch (choice) {
            case 1:
             viewMedicalRecord();
             break;
            case 2:
                System.out.print("Enter a new email address (leave blank to keep existing value): ");
                String newEmail = scanner.nextLine();
                System.out.print("Enter a new phone number (leave blank to keep existing value): ");
                String newPhoneNumber = scanner.nextLine();
                this.patientRecord.updateEmailAddress(newEmail);
                this.patientRecord.updatePhoneNumber(newPhoneNumber);
                try {
                    this.patientRecord.saveToFile();
                    System.out.println("All changes successful!");
                } catch (IOException error) {
                    System.out.println("Error occurred when saving data: ");
                    error.printStackTrace();
                }
                System.out.println("");
                break;
            case 3:
                this.scheduleAppointment(scanner);
                break;
            case 4:
                this.rescheduleOrCancelAppointment(scanner);
                break;
            case 5:
                this.viewAppointmentStatuses();
                break;
            case 6:
                this.viewAppointmentOutcomeRecords(scanner);
                break;
            case 7:
                System.out.println("Select the service:\n1. Add past Diagnosis\n2. Add past Treatment");
                int choice1 = scanner.nextInt();
                Scanner scanner1 = new Scanner(System.in);
                switch (choice1){
                    case 1:
                        System.out.println("Enter the Past Diagnosis diagnosis: ");
                        String diagnosis = scanner1.nextLine();
                        this.patientRecord.addDiagnosis(diagnosis);
                        break;
                    case 2:
                        System.out.println("Enter the Past treatment: ");
                        String treatment = scanner1.nextLine();
                        this.patientRecord.addTreatments(treatment);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number from 1 to 2.");
                        break;
                }
                break;
            case 8:
                this.addAllergyToMedicalRecord(scanner);
                break;
            case 9:
                this.payBill(scanner);
                break;
            case 10:
                this.giveFeedback(scanner);
                break;
            case 11:
                System.out.println("Signed out successfully.");
                return false;
            default:
                System.out.println("Invalid choice. Please enter a number from 1 to 7.");
                break;
        }
        return true;
    }

    /**
     * Allows the patient to reschedule or cancel an existing appointment.
     * Prompts the patient to select an appointment to cancel and optionally schedule a new one.
     *
     * @param scanner The {@code Scanner} object for reading user input.
     */

    public void rescheduleOrCancelAppointment(Scanner scanner) {
        try {
            System.out.println("To reschedule an appointment, you need to cancel an existing appointment.");
            System.out.println("After cancelling, you'll be asked if you want to schedule a new appointment.");
            System.out.println(
                    "Please choose an appointment to cancel:\n");
            List<Appointment> appts = AppointmentPatientView.loadAllAppointments();
            Iterator<Appointment> it = appts.iterator();
            boolean foundAnyAppts = false;
            while (it.hasNext()) {
                AppointmentPatientView appt = it.next();
                if (appt.getPatientId().isPresent() && appt.getPatientId().get().equals(this.id)) {
                    System.out.println("(" + appt.getId() + ") - " + appt.getDateTime().toString());
                    foundAnyAppts = true;
                }
            }
            if (!foundAnyAppts) {
                System.out.println("You have no booked appointments!\n");
                return;
            }
            System.out.println("");
            System.out.print("Enter the ID of the appointment you want to cancel: ");
            String selectedAppointmentId = scanner.nextLine();
            boolean wasCancellationSuccessful = false;
            it = appts.iterator();
            while (it.hasNext()) {
                AppointmentPatientView appt = it.next();
                if (appt.getId().equals(selectedAppointmentId) && appt.getPatientId().isPresent()
                        && appt.getPatientId().get().equals(this.id)) {
                    appt.cancel();
                    appt.save();
                    wasCancellationSuccessful = true;
                    break;
                }
            }
            if (!wasCancellationSuccessful) {
                System.out.println("Invalid Appointment ID! Returning to main menu...");
            } else {
                System.out.println("Cancellation was successful!\n");
            }
            while (true) {
                System.out.print("""
                        Choose an option to continue:
                        1. Schedule a new appointment
                        2. Back to Patient Menu
                        Enter your choice: """);
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1) {
                    this.scheduleAppointment(scanner);
                    return;
                } else if (choice == 2) {
                    return;
                } else {
                    System.out.println("Invalid option! Please enter a value between 1-2.");
                }
            }
        } catch (IOException error) {
            System.out.println("Error occurred cancelling new appointment: ");
            error.printStackTrace();
        }
    }

     /**
     * Allows the patient to schedule a new appointment by selecting from available slots.
     *
     * @param scanner The {@code Scanner} object for reading user input.
     */

    public void scheduleAppointment(Scanner scanner) {
    try {
        List<Appointment> appts = AppointmentPatientView.loadAllAppointments();
        System.out.println("Here are all the available appointments:");
        Iterator<Appointment> it = appts.iterator();
        boolean foundAnyAppts = false;
        
        while (it.hasNext()) {
            AppointmentPatientView appt = it.next();
            if (appt.isBookable()) {
                System.out.println("(" + appt.getId() + ") - " + appt.getDateTime().toString());
                foundAnyAppts = true;
            }
        }
        
        if (!foundAnyAppts) {
            System.out.println("No more available appointments!\n");
            return;
        }
        
        System.out.print("Enter the ID of the appointment you want to book: ");
        String selectedAppointmentId = scanner.nextLine();
        boolean wasBookingSuccessful = false;
        
        it = appts.iterator();
        while (it.hasNext()) {
            AppointmentPatientView appt = it.next();
            if (appt.getId().equals(selectedAppointmentId)) {
                // Set patient ID and status to Pending
                appt.schedule(this.id);  // Assign patient ID
                if (appt instanceof Appointment) {  // Ensure it's a modifiable instance
                    ((Appointment) appt).setStatus(Optional.of(AppointmentStatus.pending));
                }
                
                appt.save();  // Save the updated appointment
                wasBookingSuccessful = true;
                break;
            }
        }
        
        if (!wasBookingSuccessful) {
            System.out.println("Invalid Appointment ID! Returning to main menu...");
        } else {
            System.out.println("Booking was successful! Appointment status is now PENDING.");
        }
        
    } catch (IOException error) {
        System.out.println("Error occurred scheduling new appointment: ");
        error.printStackTrace();
    }
}


    /**
     * Views the outcome records of an appointment specified by appointment ID.
     *
     * @param scanner The {@code Scanner} object for reading user input.
     */
    
    private void viewAppointmentOutcomeRecords(Scanner scanner) {
    System.out.print("Enter the appointment ID to search: ");
    String searchAppointmentId = scanner.nextLine();
    String filePath = "../data/appointment_outcome_records.csv";

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        int recordFound = 0;

        while ((line = br.readLine()) != null) {
            String[] record = line.split(",");

            if (record.length < 5) {
                System.err.println("Skipping malformed record: " + line);
                continue;  // Skip malformed records
            }

            if (record[0].equals(searchAppointmentId)) {
                System.out.println("Appointment ID: " + record[0]);
                System.out.println("Date of Appointment: " + new Date(Long.parseLong(record[1])));
                System.out.println("Service Type: " + record[2]);
                System.out.println("Prescribed Medications: " + record[3].replace(";", ", "));
                System.out.println("Consultation Notes: " + record[4]);
                System.out.println();  // Blank line between records
                recordFound +=1;
            }
        }

        if (recordFound==0) {
            System.out.println("No record found for Appointment ID: " + searchAppointmentId);
        }

    } catch (IOException e) {
        System.out.println("Error reading appointment outcome records: " + e.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("Error parsing date in record. Record might be malformed.");
    }
}

public void viewMedicalRecord() {
    try {
        // Fetch the medical record for the currently logged-in patient using an exact ID match
        MedicalRecordPatientView record = MedicalRecords.getRecord(this, this.id);
        if (record != null) {
            System.out.println("Medical Record for Patient ID: " + this.id);
            System.out.println(record.toString());
        } else {
            System.out.println("No medical record found for Patient ID: " + this.id);
        }
    } catch (Exception e) {
        System.out.println("Error accessing medical record: " + e.getMessage());
    }
}

/**
     * Allows the patient to provide feedback about the hospital management system.
     *
     * @param scanner The {@code Scanner} object for reading user input.
     */


private void giveFeedback(Scanner scanner) {
    System.out.println("What was your experience with our hospital management system? How can we improve?");
    String comments = scanner.nextLine().trim();

    // Validate non-empty feedback.
    while (comments.isEmpty()) {
        System.out.println("Feedback cannot be empty. Please provide your feedback:");
        comments = scanner.nextLine().trim();
    }

    int rating = 0;
    while (true) {
        System.out.print("Please rate your experience from 1 to 5: ");
        try {
            rating = Integer.parseInt(scanner.nextLine().trim());
            if (rating < 1 || rating > 5) {
                throw new NumberFormatException();
            }
            break;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number between 1 and 5.");
        }
    }

    // Create a Feedback object and save it.
    Feedback feedback = new Feedback(this.id, comments, rating);
    try {
        feedback.save();
        System.out.println("Thank you for your feedback!");
    } catch (IOException e) {
        System.out.println("An error occurred while saving your feedback: " + e.getMessage());
    }
}


    public void viewAppointmentStatuses() {
        try {
            System.out.println("Here are all your booked appointments and their statuses:\n");
            List<Appointment> appts = AppointmentPatientView.loadAllAppointments();
            Iterator<Appointment> it = appts.iterator();
            boolean foundAnyAppts = false;
            while (it.hasNext()) {
                AppointmentPatientView appt = it.next();
                if (appt.getPatientId().isPresent() && appt.getPatientId().get().equals(this.id)) {
                    Optional<AppointmentStatus> status = appt.getStatus();
                    System.out.println(
                            "(" + appt.getId() + ") - " + appt.getDateTime().toString() + " - " + (status.isEmpty()
                                    ? "PENDING"
                                    : status.get().toString().toUpperCase()));
                    foundAnyAppts = true;
                }
            }
            if (!foundAnyAppts) {
                System.out.println("You have no booked appointments!\n");
                return;
            }
            System.out.println("");
            
        } catch (IOException error) {
            System.out.println("Error occurred retrieving your appointment: ");
            error.printStackTrace();
        }
    }
    
    /**
     * Allows the patient to pay a bill by entering the bill ID.
     *
     * @param scanner The {@code Scanner} object for reading user input.
     */
    
    private void payBill(Scanner scanner) {
        System.out.print("Enter the bill ID to pay: ");
        String billId = scanner.nextLine();
        try {
            Billing bill = new Billing();
            bill.paybill(billId);
            System.out.println("Bill paid successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while paying the bill: " + e.getMessage());
        }
    }

    /**
     * Allows the patient to add a new allergy to their medical record.
     *
     * @param scanner The {@code Scanner} object for reading user input.
     */
    
    private void addAllergyToMedicalRecord(Scanner scanner) {
        System.out.print("Enter a new allergy to add: ");
        String allergy = scanner.nextLine().trim();
    
        if (allergy.isEmpty()) {
            System.out.println("Allergy cannot be empty. Try again.");
            return;
        }
    
        // Add allergy to the medical record.
        patientRecord.addAllergy(allergy);
    
        try {
            patientRecord.saveToFile();  // Save updated medical record.
            System.out.println("Allergy added and saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving updated medical record: " + e.getMessage());
        }
    }
    
}
