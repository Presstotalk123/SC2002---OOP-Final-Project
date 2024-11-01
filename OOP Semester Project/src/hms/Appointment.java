import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Appointment {
    private String appointmentID;
    private Patient patient;
    private Doctor doctor;
    private Date date;
    private TimeSlot timeSlot;
    private String status;
    private AppointmentOutcomeRecord appointmentOutcomeRecord;

    public Appointment(String appointmentID, Patient patient, Doctor doctor, Date date, TimeSlot timeSlot, String status) {
        this.appointmentID = appointmentID;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status;
    }

    // Getters and setters...

    public boolean schedule() {
        this.status = "Scheduled";
        return true;
    }

    public boolean reschedule(Date newDate, TimeSlot newTimeSlot) {
        this.date = newDate;
        this.timeSlot = newTimeSlot;
        this.status = "Rescheduled";
        return true;
    }

    public boolean cancel() {
        this.status = "Cancelled";
        return true;
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    public void saveToCSV(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(toCSV());
            writer.newLine();
        }
    }

    public static List<Appointment> loadFromCSV(String filePath) throws IOException {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                appointments.add(fromCSV(line));
            }
        }
        return appointments;
    }

    private String toCSV() {
        return String.join(",",
                appointmentID,
                patient.getPatientID(),
                doctor.getDoctorID(),
                String.valueOf(date.getTime()),
                timeSlot.getStartTime(),
                timeSlot.getEndTime(),
                status,
                appointmentOutcomeRecord != null ? appointmentOutcomeRecord.getAppointmentID() : ""
        );
    }

    private static Appointment fromCSV(String csv) {
        String[] parts = csv.split(",");
        String appointmentID = parts[0];
        Patient patient = new Patient(parts[1]); // Assuming a constructor that takes patientID
        Doctor doctor = new Doctor(parts[2]); // Assuming a constructor that takes doctorID
        Date date = new Date(Long.parseLong(parts[3]));
        TimeSlot timeSlot = new TimeSlot(parts[4], parts[5]);
        String status = parts[6];
        AppointmentOutcomeRecord outcomeRecord = parts.length > 7 && !parts[7].isEmpty() ? new AppointmentOutcomeRecord(parts[7]) : null; // Assuming a constructor that takes appointmentID
        return new Appointment(appointmentID, patient, doctor, date, timeSlot, status);
    }
}