package hms.Appointments;
import hms.MedicalRecords.MedicalRecordDoctorView;
import hms.MedicalRecords.MedicalRecordPatientView;
import hms.MedicalRecords.MedicalRecords;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents an appointment in the hospital management system.
 * An appointment can be viewed by both patients and doctors.
 * It includes details such as appointment ID, doctor ID, patient ID,
 * appointment date and time, status, and outcome record ID.
 */


public class Appointment implements AppointmentPatientView, AppointmentDoctorView {
    private String id;
    private String doctorId;
    private LocalDateTime dateTime;

    // I used Optionals to denote that these items COULD potentially be empty.
    // This forces you to check if the values exists or not.
    private Optional<String> patientId;
    private Optional<String> outcomeRecordId; // points to a AppointmentOutcomeRecord id
    private Optional<AppointmentStatus> status; // e.g., confirmed, canceled, completed, this can be empty if doctor
    // hasn't accepted or rejected

    /**
     * Constructs an Appointment with the specified details.
     *
     * @param id              The unique identifier of the appointment.
     * @param patientId       The optional identifier of the patient.
     * @param doctorId        The identifier of the doctor.
     * @param status          The optional status of the appointment.
     * @param dateTime        The date and time of the appointment.
     * @param outcomeRecordId The optional identifier of the outcome record.
     */
    
    public Appointment(String id, Optional<String> patientId, String doctorId, Optional<AppointmentStatus> status,
                       LocalDateTime dateTime, Optional<String> outcomeRecordId) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.outcomeRecordId = outcomeRecordId;
        this.status = status.or(() -> patientId.isPresent() ? Optional.of(AppointmentStatus.pending) : Optional.empty());
    }

    /**
     * Loads all appointments from the data source.
     *
     * @return A list of all appointments.
     * @throws IOException If an I/O error occurs.
     */
    
    protected static List<Appointment> loadAllAppointments() throws IOException {
        List<Appointment> appointments = new ArrayList<Appointment>();

        BufferedReader file = new BufferedReader(new FileReader("MedicalRecordDoctorView.java\n" +
                "MedicalRecordPatientView.java\n" +
                "MedicalRecords.java"));

        String nextLine = file.readLine();
        while ((nextLine = file.readLine()) != null) {
            String[] appt = nextLine.split(",");
            if (appt.length < 6) {
                continue;
            }

            String status = appt[4];
            Optional<AppointmentStatus> apptStatus;
            if (status.toLowerCase().equals("confirmed"))
                apptStatus = Optional.of(AppointmentStatus.confirmed);
            else if (status.toLowerCase().equals("cancelled"))
                apptStatus = Optional.of(AppointmentStatus.cancelled);
            else if (status.toLowerCase().equals("completed"))
                apptStatus = Optional.of(AppointmentStatus.completed);
            else
                apptStatus = Optional.empty();

            appointments.add(
                    new Appointment(
                            appt[0],
                            Optional.ofNullable(appt[1].equals("") ? null : appt[1]),
                            appt[2],
                            apptStatus,
                            LocalDateTime.parse(appt[5]),
                            Optional.ofNullable(appt[3].equals("") ? null : appt[3])));
        }

        file.close();
        // throw new IOException("Missing or Invalid Patient Data found in
        // patient_record.csv for patient with ID: " + id);

        return appointments;
    }

    /**
     * Schedules the appointment by assigning a patient ID.
     *
     * @param patientId The identifier of the patient scheduling the appointment.
     */


    public void schedule(String patientId) {
        this.patientId = Optional.of(patientId);
    }

    /**
     * Cancels the appointment by removing the patient ID and status.
     */

    
    public void cancel() {
        this.patientId = Optional.empty();
        this.status = Optional.empty();
    }

    /**
     * Saves the appointment details to the data source.
     *
     * @throws IOException If an I/O error occurs.
     */


    public void save() throws IOException {

        List<String> lines = Files.readAllLines(Paths.get("../data/appointments.csv"));
        FileOutputStream output = new FileOutputStream("../data/appointments.csv");

        boolean isEntryFound = false;
        for (int i = 0; i < lines.size(); i++) {
            String[] appt = lines.get(i).split(",");

            if (appt.length == 6 && appt[0].equals(this.id)) {
                String newEntry = this.id + "," + (this.patientId.isEmpty() ? "" : this.patientId.get()) + ","
                        + this.doctorId + "," + (this.outcomeRecordId.isEmpty() ? "" : this.outcomeRecordId.get()) + ","
                        + (this.status.isEmpty() ? "" : this.status.get().toString().toLowerCase()) + ","
                        + this.dateTime + "\n";
                output.write(newEntry.getBytes());
                isEntryFound = true;
            } else {
                String line = lines.get(i) + "\n";
                output.write(line.getBytes());
            }
        }

        if (!isEntryFound) {
            String newEntry = this.id + "," + (this.patientId.isEmpty() ? "" : this.patientId.get()) + ","
                    + this.doctorId + "," + (this.outcomeRecordId.isEmpty() ? "" : this.outcomeRecordId.get()) + ","
                    + (this.status.isEmpty() ? "" : this.status.get().toString().toLowerCase()) + ","
                    + this.dateTime + "\n";
            output.write(newEntry.getBytes());
        }

        output.close();
    }

    /**
     * Gets the status of the appointment.
     *
     * @return An Optional containing the status of the appointment.
     */

    
    public Optional<AppointmentStatus> getStatus() {
        return this.status;
    }

    public String getId() {
        return this.id;
    }

    public Optional<String> getPatientId() {
        return this.patientId;
    }

    public String getDateTime() {
        DateTimeFormatter patternDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter patternTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        return this.dateTime.format(patternDate) + " at " + this.dateTime.format(patternTime);
    }

    /**
     * Checks if the appointment is bookable.
     *
     * @return True if the appointment can be booked; false otherwise.
     */
    
    public boolean isBookable() {
        if (this.status.isEmpty())
            return this.patientId.isEmpty();
        return this.patientId.isEmpty() && !this.status.get().equals(AppointmentStatus.cancelled);
    }

   /**
     * Accepts the appointment, setting its status to confirmed.
     *
     * @throws IOException If an I/O error occurs.
     */

    public void accept() throws IOException {
        this.setStatus(Optional.of(AppointmentStatus.confirmed));
        this.save();
    }

    /**
     * Declines the appointment, setting its status to cancelled.
     *
     * @throws IOException If an I/O error occurs.
     */

    public void decline() throws IOException {
        this.setStatus(Optional.of(AppointmentStatus.cancelled));
        this.save();
    }

    /**
     * Completes the appointment by setting the outcome record ID.
     *
     * @param outcome_record_id The identifier of the outcome record.
     */
    public void complete(String outcome_record_id) {

    }

    /**
     * Returns a string representation of the appointment.
     *
     * @return A string representing the appointment details.
     */
    
    @Override
    public String toString() {
        // return "Appointment{" +
        // "Patient ID='" + patientId + '\'' +
        // ", Doctor ID='" + doctorId + '\'' +
        // ", Status='" + status + '\'' +
        // ", DateTime=" + dateTime +
        // ", Outcome Record='" + outcomeRecord + '\'' +
        // '}';
        return "";
    }

    /**
     * Sets the status of the appointment.
     *
     * @param status An Optional containing the new status.
     */
    
    public void setStatus(Optional<AppointmentStatus> status) {
        this.status = status;
    }
}
