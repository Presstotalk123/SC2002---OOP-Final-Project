package hms;

import java.io.IOException;
import java.util.Scanner;

import hms.MedicalRecords.MedicalRecords;

public class Patient extends User {

    // Store MedicalRecord as a PatientView so only Patient methods are exposed.
    private hms.MedicalRecords.PatientView patientRecord;

    public Patient(String id, String name, String password) throws IOException {
        super(id, name, password);

        hms.MedicalRecords.PatientView record = MedicalRecords.getRecord(this, id);
        this.patientRecord = record;
    }

    public boolean eventLoop(Scanner scanner) {

        System.out.print("""
                Patient Menu:
                1. View Medical Records
                2. Update Email or Phone Number
                3. Schedule New Appointment
                4. Reschedule/Cancel Existing Appointment
                5. View All Appointment Statuses
                6. View Appointment Outcome Records
                7. Log Out
                Enter your choice:""");

        int choice = scanner.nextInt();
        scanner.nextLine();

        System.out.println("");

        switch (choice) {
            case 1:
                System.out.println(this.patientRecord);
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
            case 7:
                return false;

            default:
                System.out.println("Invalid choice. Please enter a number from 1 to 7.");
                break;
        }

        return true;

    }

}
