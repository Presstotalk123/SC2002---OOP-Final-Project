package hms;


public interface IPatient extends User {

}

public class Patient implements IPatient {

    public static IPatient Patient() {
        return new Patient();
    }

}

//public class Patient extends User {
//    private String name;
//    private String dob;
//    private String gender;
//
//    public Patient(String userId, String password, String name, String dob, String gender) {
//        super(userId, password);
//        this.name = name;
//        this.dob = dob;
//        this.gender = gender;
//    }
//
//    @Override
//    public void login() {
//        // Implement patient login logic
//    }
//
//    // Getters and setters for patient-specific fields
//}
