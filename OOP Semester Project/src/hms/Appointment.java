package hms;

import java.time.LocalDateTime;

public class Appointment {
    private String patientId;
    private String doctorId;
    private String status; // e.g., confirmed, canceled, completed
    private LocalDateTime dateTime;
    private String outcomeRecord; // For completed appointments

    public Appointment(String patientId, String doctorId, String status, LocalDateTime dateTime, String outcomeRecord) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.status = status;
        this.dateTime = dateTime;
        this.outcomeRecord = outcomeRecord;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "Patient ID='" + patientId + '\'' +
                ", Doctor ID='" + doctorId + '\'' +
                ", Status='" + status + '\'' +
                ", DateTime=" + dateTime +
                ", Outcome Record='" + outcomeRecord + '\'' +
                '}';
    }
}
