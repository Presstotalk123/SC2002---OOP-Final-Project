package hms;

public class Doctor extends User {
    private String specialty;

    public Doctor(String userId, String password, String specialty) {
        super(userId, password);
        this.specialty = specialty;
    }

    @Override
    public void login() {
        // Implement doctor login logic
    }

    // Getters and setters for doctor-specific fields
}
