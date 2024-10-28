package hms.MedicalRecords;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hms.Doctor;
import hms.Patient;

enum Gender {
    Male,
    Female
}

public class MedicalRecords implements PatientView, DoctorView {

    private String id;
    private String name;
    private String dateOfBirth;
    private Gender gender;
    private String phoneNumber;
    private String emailAddress;
    private ArrayList<String> diagnosis;
    private ArrayList<String> prescriptions;
    private ArrayList<String> treatments;
    private final String bloodType; // final as this field should not be updated.

    private MedicalRecords(String id) throws IOException {

        String[] record = MedicalRecords.loadMedicalRecordFromFile(id);

        this.id = id;
        this.name = record[1];
        this.dateOfBirth = record[2];
        this.gender = record[3] == "male" ? Gender.Male : Gender.Female;
        this.phoneNumber = record[4];
        this.emailAddress = record[5];
        this.bloodType = record[6];

        // TODO: Add retrieval of diagnosis, prescriptions and treatments

    }

    public static PatientView getRecord(Patient caller, String patientId) throws IOException {
        return new MedicalRecords(patientId);
    }

    public static DoctorView getRecord(Doctor caller, String patientId) throws IOException {
        return new MedicalRecords(patientId);
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
        throw new IOException("Missing or Invalid Patient Data found in patient_record.csv for patient with ID: " + id);
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
        if (phoneNumber.length() > 0) {
            this.phoneNumber = phoneNumber.replaceAll(",", ""); // come up with better solution for escaping commas
        }
    }

    // From MedicalRecordDoctorAccess
    public void newDiagnosis(String diagnosis) {
        this.diagnosis.add(diagnosis);
    }

    // From MedicalRecordDoctorAccess
    public void newPrescription(String prescription) {
        this.prescriptions.add(prescription);
    }

    // From MedicalRecordDoctorAccess
    public void newTreatmentPlan(String treatmentPlan) {
        this.treatments.add(treatmentPlan);
    }

    public String toString() {
        return new StringBuilder()
                .append("Patient ID: " + this.id + "\n")
                .append("Name: " + this.name + "\n")
                .append("Date of Birth: " + this.dateOfBirth + "\n")
                .append("Gender: " + this.gender + "\n")
                .append("Phone Number: " + this.phoneNumber + "\n")
                .append("Email Address: " + this.emailAddress + "\n")
                .append("Blood Type: " + this.bloodType + "\n")
                .toString();
    }

    public void saveToFile() throws IOException {
        FileReader file = new FileReader("../data/medical_record.csv");

        List<String> lines = Files.readAllLines(Paths.get("../data/medical_record.csv"));
        FileOutputStream output = new FileOutputStream("../data/medical_record.csv");

        for (int i = 0; i < lines.size(); i++) {
            String[] patient = lines.get(i).split(",");

            if (patient.length == 7 && patient[0].equals(this.id)) {
                String newEntry = this.id + "," + this.name + "," + this.dateOfBirth + "," + this.gender.toString().toLowerCase() + "," + this.phoneNumber + "," + this.emailAddress + "," + this.bloodType + "\n";
                output.write(newEntry.getBytes());
            } else {
                String line = lines.get(i) + "\n";
                output.write(line.getBytes());
            }
        }

        output.close();
        file.close();
    }
}

// I use interfaces to limit what different users can do