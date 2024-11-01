import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Doctor extends User {
    private String specialty;
    private List<Appointment> appointments;

    private String Username;

    public Doctor(String hospitalID, String password, String name, Date dateOfBirth, String gender, ContactInfo contactInfo, String specialty,String Username) {
        super(hospitalID, password, name, dateOfBirth, gender, contactInfo);
        this.specialty = specialty;
        this.appointments = new ArrayList<>();
        this.Username=Username;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    public void removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
    }

    public void viewSchedule() {
        for (Appointment appointment : appointments) {
            System.out.println(appointment);
        }
    }

    @Override
    public boolean login(String password) {
        return this.password.equals(password);
    }

    @Override
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
    public String getDoctorUsername() {
        return this.Username;
    }

    public List<Patient> loadPatientsFromCSV(String filePath) throws IOException {
        List<Patient> patients = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                patients.add(Patient.fromCSV(line));
            }
        }
        return patients;
    }

    public List<Appointment> loadAppointmentsFromCSV(String filePath) throws IOException {
        return Appointment.loadFromCSV(filePath);
    }
}