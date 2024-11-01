import java.util.Date;

public class Patient {
    private String patientUsername;
    private String name;
    private Date dateOfBirth;
    private String gender;
    private ContactInfo contactInfo;
    private String password;

    public Patient(String patientUsername, String name, Date dateOfBirth, String gender, ContactInfo contactInfo, String password) {
        this.patientUsername = patientUsername;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactInfo = contactInfo;
        this.password = password;
    }

    public String getPatientID() {
        return patientUsername;
    }

    public void setpatientUsername(String patientUsername) {
        this.patientUsername = patientUsername;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean login(String password) {
        return this.password.equals(password);
    }

/*    public static Patient fromCSV(String csv) {
        String[] parts = csv.split(",");
        String patientID = parts[0];
        String name = parts[1];
        Date dateOfBirth = new Date(Long.parseLong(parts[2]));
        String gender = parts[3];
        ContactInfo contactInfo = new ContactInfo(parts[4], parts[5], parts[6]); // Assuming a constructor that takes address, phone, and email
        String password = parts[7];
        return new Patient(patientID, name, dateOfBirth, gender, contactInfo, password);
    }*/
}