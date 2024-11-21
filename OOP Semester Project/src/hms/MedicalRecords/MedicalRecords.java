package hms.MedicalRecords;

import hms.Doctor;
import hms.Gender;
import hms.Patient;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * The {@code MedicalRecords} class represents the medical records of a patient.
 * It implements both {@code MedicalRecordPatientView} and {@code MedicalRecordDoctorView} interfaces,
 * allowing both patients and doctors to interact with the medical records.
 */

public class MedicalRecords implements MedicalRecordPatientView, MedicalRecordDoctorView {

    private String id;
    private String name;
    private String dateOfBirth;
    private Gender gender;
    private String phoneNumber;
    private String emailAddress;
    private String diagnosis;
    private String treatments;
    private String bloodType; // final as this field should not be updated.
    private List<String> allergies;

     /**
     * Constructs a new {@code MedicalRecords} object by prompting the user for input via a {@code Scanner}.
     * This constructor is typically used when creating a new patient record.
     *
     * @param scanner The {@code Scanner} object to read user input.
     * @param id      The unique identifier for the patient.
     * @param name    The name of the patient.
     */

    
    public MedicalRecords(Scanner scanner, String id, String name) {
        this.id = id;
        this.name = name;
        this.allergies = new ArrayList<>();
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

        System.out.print("Enter the blood type for this user: ");
        String bloodType = scanner.nextLine();

        this.bloodType = bloodType;

    }

    /**
     * Constructs a new {@code MedicalRecords} object by loading data from a file.
     * This constructor is typically used when retrieving an existing patient record.
     *
     * @param id The unique identifier for the patient.
     * @throws IOException If an I/O error occurs during file reading.
     */

    private MedicalRecords(String id) throws IOException {

        String[] record = MedicalRecords.loadMedicalRecordFromFile(id);

        this.id = id;
        this.name = record[1];
        this.dateOfBirth = record[2];
        this.gender = record[3].equals("male") ? Gender.Male : Gender.Female;
        this.phoneNumber = record[4];
        this.emailAddress = record[5];
        this.bloodType = record[6];
        this.diagnosis = record[7];
        this.treatments = record[8];
        this.allergies = record.length > 9 && !record[9].equals("None") 
                 ? new ArrayList<>(Arrays.asList(record[9].split(";"))) 
                 : new ArrayList<>();


        // TODO: Add retrieval of diagnosis, prescriptions and treatments

    }

    /**
     * Retrieves the medical record for a patient from the perspective of a patient user.
     *
     * @param caller    The patient user requesting the record.
     * @param patientId The ID of the patient whose record is requested.
     * @return A {@code MedicalRecordPatientView} of the patient's medical record.
     */
    
    public static MedicalRecordPatientView getRecord(Patient caller, String patientId) {
        try {
            return new MedicalRecords(patientId);
        } catch (IOException e) {
            System.out.println("Error loading medical record for patient with ID: " + patientId);
            e.printStackTrace();
            return null;
        }
    }

     /**
     * Retrieves the medical record for a patient from the perspective of a doctor user.
     *
     * @param caller    The doctor user requesting the record.
     * @param patientId The ID of the patient whose record is requested.
     * @return A {@code MedicalRecordDoctorView} of the patient's medical record.
     */

    
    public static MedicalRecordDoctorView getRecord(Doctor caller, String patientId) {
        try {
            return new MedicalRecords(patientId);
        } catch (IOException e) {
            System.out.println("Error loading medical record for patient with ID: " + patientId);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads the medical record data for a patient from the file system.
     *
     * @param id The unique identifier for the patient.
     * @return An array of strings containing the patient's medical record data.
     * @throws IOException If an I/O error occurs during file reading.
     */

    private static String[] loadMedicalRecordFromFile(String id) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\Patient.csv"));

        String nextLine;
        while ((nextLine = file.readLine()) != null) {
            String[] patient = nextLine.split(",");

            if (patient.length < 10) {
                String[] updatedPatient = Arrays.copyOf(patient, 10);
                updatedPatient[9] = "None"; // Default allergy
                return updatedPatient;
            }

            if (patient[0].equals(id)) {
                return patient;
            }
        }

        file.close();
        throw new IOException("Missing or Invalid Patient Data found in MedicalRecords.csv for patient with ID: " + id);
    }

    /**
     * Updates the email address of the patient.
     *
     * @param email The new email address.
     */

    // From MedicalRecordPatientAccess
    public void updateEmailAddress(String email) {
        // TODO: Maybe add validation?
        if (email.length() > 0) {
            this.emailAddress = email.replaceAll(",", ""); // come up with better solution for escaping commas
        }
    }

    /**
     * Updates the phone number of the patient.
     *
     * @param phoneNumber The new phone number.
     */
    
    // From MedicalRecordPatientAccess
    public void updatePhoneNumber(String phoneNumber) {
        // TODO: Maybe add validation?
        if (phoneNumber.length() == 8) {
            this.phoneNumber = phoneNumber.replaceAll(",", ""); // come up with better solution for escaping commas
        }
    }

    /**
     * Returns a string representation of the medical record.
     *
     * @return A string containing the patient's medical record information.
     */
    
    public String toString() {
        return new StringBuilder()
                .append("Patient ID: " + this.id + "\n")
                .append("Name: " + this.name + "\n")
                .append("Date of Birth: " + this.dateOfBirth + "\n")
                .append("Gender: " + this.gender + "\n")
                .append("Phone Number: " + this.phoneNumber + "\n")
                .append("Email Address: " + this.emailAddress + "\n")
                .append("Blood Type: " + this.bloodType + "\n")
                .append("Diagnosis: " + this.diagnosis + "\n")
                .append("Treatments: " + this.treatments + "\n")
                .append("Allergies: " + (this.allergies.isEmpty() ? "None" : String.join(", ", this.allergies)) + "\n")
                .toString();
    }

    /**
     * Saves the medical record to the file system.
     *
     * @throws IOException If an I/O error occurs during file writing.
     */
    
    public void saveToFile() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\Patient.csv"));
        FileOutputStream output = new FileOutputStream("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\Patient.csv");

        boolean isEntryFound = false;

        for (int i = 0; i < lines.size(); i++) {
            String[] patient = lines.get(i).split(",");

            if (patient.length >= 9 && patient[0].equals(this.id)) {
                //String diagnosisString = (this.diagnosis == null || this.diagnosis.isEmpty() || Objects.equals(this.diagnosis, "Not Available")) ? "Not Available" : this.diagnosis;
                //String treatmentsString = (this.treatments == null || this.treatments.isEmpty() || Objects.equals(this.treatments, "Not Available")) ? "Not Available" : this.treatments;
                String newEntry = this.id + "," + this.name + "," + this.dateOfBirth + "," +
                        this.gender.toString().toLowerCase() + "," + this.phoneNumber + "," +
                        this.emailAddress + "," + this.bloodType + "," + this.diagnosis + "," + this.treatments + "," + String.join(";", this.allergies) + "\n";
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
                    this.emailAddress + "," + this.bloodType + "," + "Not Available" + "," +
                    "Not Available" + "," + "None" + "\n";
            output.write(newEntry.getBytes());
        }
        

        output.close();
    }

    /**
     * Adds a diagnosis to the patient's medical record.
     *
     * @param diagnosis The diagnosis to add.
     * @throws IOException If an I/O error occurs during file writing.
     */

    public void addDiagnosis(String diagnosis) throws IOException {
        if (diagnosis == null || diagnosis.trim().isEmpty()) {
            diagnosis = "Not Available";
        }
        this.diagnosis = (Objects.equals(this.diagnosis, "Not Available") || this.diagnosis.isEmpty()) ? diagnosis.trim() : this.diagnosis + ";" + diagnosis.trim();
        this.saveToFile();
        System.out.println("Diagnosis added successfully!");
    }

    /**
     * Adds a treatment to the patient's medical record.
     *
     * @param treatments The treatments to add.
     * @throws IOException If an I/O error occurs during file writing.
     */

    
    public void addTreatments(String treatments) throws IOException {
        if (treatments == null || treatments.trim().isEmpty()) {
            treatments = "Not Available";
        }
        this.treatments = (Objects.equals(this.treatments, "Not Available") || this.treatments.isEmpty()) ? treatments.trim() : this.treatments + ";" + treatments.trim();
        this.saveToFile();
        System.out.println("Treatments added successfully!");
    }

    /**
     * Adds an allergy to the patient's medical record.
     *
     * @param allergy The allergy to add.
     */
    
    @Override
    public void addAllergy(String allergy) {
        if (!allergies.contains(allergy)) {
            allergies.add(allergy);
            System.out.println("Allergy added: " + allergy);
            try {
                saveToFile(); // Persist the update to file
            } catch (IOException e) {
                System.out.println("Error saving updated allergies: " + e.getMessage());
            }
        } else {
            System.out.println("Allergy already exists.");
        }
    }
    


}
