import java.util.Date;
import java.util.List;

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
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public Date getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(Date dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public List<Prescription> getPrescribedMedications() {
        return prescribedMedications;
    }

    public void setPrescribedMedications(List<Prescription> prescribedMedications) {
        this.prescribedMedications = prescribedMedications;
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }

    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
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

}