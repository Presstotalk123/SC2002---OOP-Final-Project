import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;


public abstract class User {
    public String hospitalID;
    protected String password;
    private String name;
    private Date dateOfBirth;
    private String gender;
    private ContactInfo contactInfo;

    public User(String hospitalID, String password, String name, Date dateOfBirth, String gender, ContactInfo contactInfo) {
        this.hospitalID = hospitalID;
        this.password = password;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactInfo = contactInfo;
    }

    // Existing methods...

    /**
     * Saves the user details to a CSV file.
     *
     * @param filePath the path of the CSV file.
     */
    public void saveToCSV(String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.printf("%s,%s,%s,%s,%s,%s,%s%n",
                    hospitalID,
                    password,
                    name,
                    dateOfBirth.toString(),
                    gender,
                    contactInfo.getPhoneNumber(),
                    contactInfo.getEmailAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters and Setters...

    public String getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
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

    // Implement abstract methods from User if any (e.g., login, changePassword)
    public abstract boolean login(String password);

    public abstract void changePassword(String newPassword);
}