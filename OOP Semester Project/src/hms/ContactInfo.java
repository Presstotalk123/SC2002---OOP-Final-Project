public class ContactInfo {
    private String phoneNumber;
    private String emailAddress;

    public ContactInfo(String phoneNumber, String emailAddress) {
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void updatePhoneNumber(String newNumber) {
        this.phoneNumber = newNumber;
    }

    public void updateEmailAddress(String newEmail) {
        this.emailAddress = newEmail;
    }
}