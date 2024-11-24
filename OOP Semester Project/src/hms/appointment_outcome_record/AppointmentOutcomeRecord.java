package hms.appointment_outcome_record;

import hms.Prescription;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents an appointment outcome record, including appointment details,
 * prescribed medications, and consultation notes. Provides methods to save
 * and retrieve appointment records from a CSV file, and to save prescribed
 * medications to their respective CSV files.
 */

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

        /**
     * Constructs an AppointmentOutcomeRecord with the specified details.
     *
     * @param appointmentID         The unique identifier for the appointment.
     * @param dateOfAppointment     The date of the appointment.
     * @param serviceType           The type of service provided during the appointment.
     * @param prescribedMedications The list of prescriptions given during the appointment.
     * @param consultationNotes     The notes taken during the consultation.
     */

        
        createPrescriptions(); // Automatically save prescriptions when creating the record
    }

    /**
     * Saves the prescribed medications to their respective CSV files.
     * Logs any IOExceptions encountered during the process.
     */
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

    /**
     * Gets the appointment ID.
     *
     * @return The appointment ID.
     */
    
    public String getAppointmentID() {
        return appointmentID;
    }

    /**
     * Gets the date of the appointment.
     *
     * @return The date of the appointment.
     */

    public Date getDateOfAppointment() {
        return dateOfAppointment;
    }

    /**
     * Gets the service type provided during the appointment.
     *
     * @return The service type.
     */
    
    public String getServiceType() {
        return serviceType;
    }

     /**
     * Gets the list of prescribed medications.
     *
     * @return The list of prescriptions.
     */

    public List<Prescription> getPrescribedMedications() {
        return prescribedMedications;
    }

    /**
     * Gets the consultation notes.
     *
     * @return The consultation notes.
     */

    public String getConsultationNotes() {
        return consultationNotes;
    }

    /**
     * Reads all appointment outcome records from a CSV file and displays them.
     * Each record includes appointment details and associated prescriptions.
     */

    public static void getAllRecords() {
        //List<AppointmentOutcomeRecord> records = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader("../data/appointment_outcome_records.csv"))) {
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

    /**
     * Saves the appointment outcome record to a CSV file.
     *
     * @param filePath The file path of the CSV file.
     */
    
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

    /**
     * Returns a string representation of the appointment outcome record.
     *
     * @return A string containing the appointment details, prescribed medications, and consultation notes.
     */

    
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

    /**
     * Returns a string representation of the appointment outcome record using the specified prescription IDs.
     *
     * @param a A string containing prescription IDs.
     * @return A string representation of the appointment outcome record.
     */
    
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
