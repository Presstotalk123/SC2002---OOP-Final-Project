import java.util.Date;

public class Administrator extends User {

    public Administrator(String hospitalID, String password, String name, Date dateOfBirth, String gender, ContactInfo contactInfo) {
        super(hospitalID, password, name, dateOfBirth, gender, contactInfo);
    }

    @Override
    public boolean login(String password) {
        return this.password.equals(password);
    }

    @Override
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // Additional methods specific to Administrator
    public void manageUserAccounts() {
        // Implement logic to manage user accounts
    }

    public void generateReports() {
        // Implement logic to generate reports
    }
}