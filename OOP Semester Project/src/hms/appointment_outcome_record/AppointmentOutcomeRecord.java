package hms.appointment_outcome_record;

import hms.Prescription;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class AppointmentOutcomeRecord {
    private String appointmentID;
    private Date dateOfAppointment;
    private String serviceType;
    private List<Prescription> prescribedMedications;
    private String consultationNotes;

    public AppointmentOutcomeRecord(String appointmentID, Date dateOfAppointment, String serviceType, List<Prescription> prescribedMedications, String consultationNotes) {
        this.appointmentID = appointmentID;
        this.dateOfAppointment = dateOfAppointment;
        this.serviceType = serviceType;
        this.prescribedMedications = prescribedMedications;
        this.consultationNotes = consultationNotes;
        createPrescriptions(); // Automatically save prescriptions when creating the record
    }

    // Save prescribed medications to their respective CSV
    private void createPrescriptions() {
        for (Prescription prescription : prescribedMedications) {
            try {
                System.out.println("Attempting to save prescription: " + prescription);
                System.out.println("Saving prescription: " + this);
                prescription.save();
            } catch (IOException e) {
                e.printStackTrace();  // Log errors to avoid silent failures
            }
        }
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public Date getDateOfAppointment() {
        return dateOfAppointment;
    }

    public String getServiceType() {
        return serviceType;
    }

    public List<Prescription> getPrescribedMedications() {
        return prescribedMedications;
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }

    public static void getAllRecords() {
        //List<AppointmentOutcomeRecord> records = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\appointment_outcome_records.csv"))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;// Skip the header row
                    if (!line.equals("appointmentID,dateOfAppointment,serviceType,prescribedMedications,consultationNotes")) {
                        throw new IOException("Invalid or missing header in prescription file.");
                    }
                    continue;
                }
                String[] values = line.split(",");
                if (values.length < 5) {
                    System.err.println("Skipping malformed record: " + line);
                    continue;
                }

                String appointmentID = values[0];
                Date dateOfAppointment;
                try {
                    dateOfAppointment = dateFormat.parse(values[1]); // Try to parse as formatted date
                } catch (ParseException e) {
                    dateOfAppointment = new Date(Long.parseLong(values[1])); // Parse as timestamp
                }
                String serviceType = values[2];
                List<String> prescriptionIds = Arrays.asList(values[3].split(";"));

                List<Prescription> allPrescriptions = Prescription.getAll();
                String result = String.join(", ", prescriptionIds);

                List<Prescription> prescribedMedications = allPrescriptions.stream()
                        .filter(p -> prescriptionIds.contains(p.getID()))
                        .collect(Collectors.toList());

                String consultationNotes = values[4];
               AppointmentOutcomeRecord a=new AppointmentOutcomeRecord(appointmentID, dateOfAppointment, serviceType, prescribedMedications, consultationNotes);
               System.out.println(a.toString2(result));
            }
        } catch (IOException e) {
            e.printStackTrace();
}

}

    public void saveToCSV(String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
                String medicationsFormatted = prescribedMedications.stream()
                .map(prescription -> prescription.getMedicationName() + ":" + prescription.getQuantity())
                .collect(Collectors.joining(";"));
            printWriter.printf("%s,%d,%s,%s,%s%n",
                    appointmentID,
                    dateOfAppointment.getTime(), // Store date as a timestamp
                    serviceType,
                    medicationsFormatted,
                    consultationNotes
            );
        } catch (IOException e) {
            System.out.println("Error saving to CSV: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Appointment ID: ").append(appointmentID).append("\n");
        sb.append("Date of Appointment: ").append(dateOfAppointment).append("\n");
        sb.append("Service Type: ").append(serviceType).append("\n");
        sb.append("Prescribed Medications: ");
        for (Prescription prescription : prescribedMedications) {
            sb.append(prescription.toString()).append("; ");
        }
        sb.append("\nConsultation Notes: ").append(consultationNotes);
        return sb.toString();
    }
    public String toString2(String a) {
        StringBuilder sb = new StringBuilder();
        sb.append("Appointment ID: ").append(appointmentID).append("\n");
        sb.append("Date of Appointment: ").append(dateOfAppointment).append("\n");
        sb.append("Service Type: ").append(serviceType).append("\n");
        sb.append("Prescribed Medications: ").append(a);
        sb.append("\nConsultation Notes:").append(consultationNotes);
        sb.append("\n");
        return sb.toString();
}
}
