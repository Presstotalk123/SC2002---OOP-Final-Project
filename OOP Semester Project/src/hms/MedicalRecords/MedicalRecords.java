package hms.MedicalRecords;

import hms.AppointmentOutcomeRecord;
import hms.Doctor;
import hms.Gender;
import hms.Patient;
import hms.Prescription;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MedicalRecords implements MedicalRecordPatientView, MedicalRecordDoctorView {

    private String id;
    private String name;
    private String dateOfBirth;
    private Gender gender;
    private String phoneNumber;
    private String emailAddress;
    private List<String> diagnosis;
    private List<Prescription> prescriptions;
    private List<String> treatments;
    private String bloodType; // final as this field should not be updated.

    public MedicalRecords(Scanner scanner, String id, String name) {
        this.id = id;
        this.name = name;
        while (true) {
            System.out.print("Enter the Date of Birth for this user in the format (DD-MM-YYYY): ");
            String dateOfBirth = scanner.nextLine();
            if (dateOfBirth.matches("(0[1-9]|[12][0-9]|3[01])\\-(0[1-9]|1[0,1,2])\\-(19|20)\\d{2}")) {
                this.dateOfBirth = dateOfBirth;
                break;
            } else {
                System.out.println("Incorrect format! Please use the format DD-MM-YYYY.");
            }
        }

        while (true) {
            System.out.print("Enter the gender for this user (male or female): ");
            String gender = scanner.nextLine().toLowerCase();
            if (gender.equals("male")) {
                this.gender = Gender.Male;
                break;
            } else if (gender.equals("female")) {
                this.gender = Gender.Female;
                break;
            } else {
                System.out.println("Invalid entry. Please specify either male or female.");
            }
        }

        while (true) {
            System.out.print("Enter the phone number for this user (Singaporean Numbers Only, like 91234567): ");
            String number = scanner.nextLine().toLowerCase();
            if (number.matches("^([0-9]{8})$") && number.length() == 8) {
                this.phoneNumber = number;
                break;
            } else {
                System.out.println("Invalid entry. Please specify in the format 91234567.");
            }
        }

        while (true) {
            System.out.print("Enter the email address for this user: ");
            String email = scanner.nextLine().toLowerCase();
            if (email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                this.emailAddress = email;
                break;
            } else {
                System.out.println("Invalid entry. Please enter a valid email address.");
            }
        }

        System.out.print("Enter the blood type for this user (Note: This cannot be changed): ");
        String bloodType = scanner.nextLine();

        this.bloodType = bloodType;

    }

    private MedicalRecords(String id) throws IOException {

        String[] record = MedicalRecords.loadMedicalRecordFromFile(id);

        this.id = id;
        this.name = record[1];
        this.dateOfBirth = record[2];
        this.gender = record[3].equals("male") ? Gender.Male : Gender.Female;
        this.phoneNumber = record[4];
        this.emailAddress = record[5];
        this.bloodType = record[6];

        this.diagnosis = new ArrayList<>();
        this.treatments = new ArrayList<>();
        this.prescriptions = new ArrayList<>();

        for (AppointmentOutcomeRecord apptRecord : AppointmentOutcomeRecord.getAllRecords()) {

            if (apptRecord.getPatientID() == id) {
                this.diagnosis.add(apptRecord.getDiagnosis());
                this.treatments.add(apptRecord.getTreatmentPlan());
                this.prescriptions.addAll(apptRecord.getPrescribedMedications());
            }
        }

        // TODO: Add retrieval of diagnosis, prescriptions and treatments

    }

    public static MedicalRecordPatientView getRecord(Patient caller, String patientId) {
        try {
            return new MedicalRecords(patientId);
        } catch (IOException e) {
            System.out.println("Error loading medical record for patient with ID: " + patientId);
            e.printStackTrace();
            return null;
        }
    }

    public static MedicalRecordDoctorView getRecord(Doctor caller, String patientId) {
        try {
            return new MedicalRecords(patientId);
        } catch (IOException e) {
            System.out.println("Error loading medical record for patient with ID: " + patientId);
            e.printStackTrace();
            return null;
        }
    }

    private static String[] loadMedicalRecordFromFile(String id) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader("../data/medical_record.csv"));

        String nextLine = file.readLine();
        while ((nextLine = file.readLine()) != null) {
            String[] patient = nextLine.split(",");

            if (patient.length < 7) {
                continue;
            }

            String currentId = patient[0];

            if (currentId.equals(id)) {
                file.close();
                return patient;
            }
        }

        file.close();
        throw new IOException("Missing or Invalid Patient Data found in MedicalRecords.csv for patient with ID: " + id);
    }

    // From MedicalRecordPatientAccess
    public void updateEmailAddress(String email) {
        // TODO: Maybe add validation?
        if (email.length() > 0) {
            this.emailAddress = email.replaceAll(",", ""); // come up with better solution for escaping commas
        }
    }

    // From MedicalRecordPatientAccess
    public void updatePhoneNumber(String phoneNumber) {
        // TODO: Maybe add validation?
        if (phoneNumber.length() == 8) {
            this.phoneNumber = phoneNumber.replaceAll(",", ""); // come up with better solution for escaping commas
        }
    }

    public String toString() {

        StringBuilder builder = new StringBuilder();

        String tableFormatter = "| %-4s | %-16s | %-10s | %-6s | %-9s | %-24s | %-10s | %n";

        builder.append(
                "+------+------------------+------------+--------+-----------+--------------------------+------------+\n");
        builder.append(
                "|                                       Your Medical Record                                         |\n");
        builder.append(
                "+------+------------------+------------+--------+-----------+--------------------------+------------+\n");
        builder.append(
                "| ID   | Name             | Birth Date | Gender | Phone No. | Email Address            | Blood Type |\n");
        builder.append(
                "+------+------------------+------------+--------+-----------+--------------------------+------------+\n");
        builder.append(String.format(tableFormatter, this.id, this.name, this.dateOfBirth,
                this.gender.toString().toLowerCase(), this.phoneNumber, this.emailAddress, this.bloodType));
        builder.append(
                "+------+------------------+------------+--------+-----------+--------------------------+------------+\n\n\n");

        builder.append(
                "+----------------------------------------+\n");
        builder.append(
                "|                Diagnoses               |\n");
        builder.append(
                "+----------------------------------------+\n");

        tableFormatter = "| %-38s | %n";

        for (String diagnosis : this.diagnosis) {
            builder.append(String.format(tableFormatter, diagnosis));
        }

        if (this.diagnosis.size() == 0) {
            builder.append(String.format(tableFormatter, "No diagnoses found!"));
        }


        builder.append(
                "+----------------------------------------+\n\n\n");

        builder.append(
                "+----------------------------------------+\n");
        builder.append(
                "|             Treatment Plans            |\n");
        builder.append(
                "+----------------------------------------+\n");

        tableFormatter = "| %-38s | %n";

        for (String treatment : this.treatments) {
            builder.append(String.format(tableFormatter, treatment));
        }

        if (this.treatments.size() == 0) {
            builder.append(String.format(tableFormatter, "No treatments found!"));
        }

        builder.append(
                "+----------------------------------------+\n\n\n");

        builder.append(
                "+----------------------------------------+\n");
        builder.append(
                "|              Prescriptions             |\n");
        builder.append(
                "+----------------------------------------+\n");

        tableFormatter = "| %-38s | %n";

        for (Prescription prescription : this.prescriptions) {
            builder.append(String.format(tableFormatter, prescription.getMedicationName()));
        }

        if (this.prescriptions.size() == 0) {
            builder.append(String.format(tableFormatter, "No prescriptions found!"));
        }

        builder.append(
                "+----------------------------------------+\n\n\n");

        return builder.toString();
    }

    public void saveToFile() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("../data/medical_record.csv"));
        FileOutputStream output = new FileOutputStream("../data/medical_record.csv");

        boolean isEntryFound = false;

        for (int i = 0; i < lines.size(); i++) {
            String[] patient = lines.get(i).split(",");

            if (patient.length == 7 && patient[0].equals(this.id)) {
                String newEntry = this.id + "," + this.name + "," + this.dateOfBirth + "," +
                        this.gender.toString().toLowerCase() + "," + this.phoneNumber + "," +
                        this.emailAddress + "," + this.bloodType + "\n";
                output.write(newEntry.getBytes());
                isEntryFound = true;
            } else {
                String line = lines.get(i) + "\n";
                output.write(line.getBytes());
            }
        }

        // If the patient is not found, append a new entry
        if (!isEntryFound) {
            String newEntry = this.id + "," + this.name + "," + this.dateOfBirth + "," +
                    this.gender.toString().toLowerCase() + "," + this.phoneNumber + "," +
                    this.emailAddress + "," + this.bloodType + "\n";
            output.write(newEntry.getBytes());
        }

        output.close();
    }

}

// I use interfaces to limit what different users can do
