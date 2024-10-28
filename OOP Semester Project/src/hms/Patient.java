package hms;

public class Patient extends User {

  public Patient(String id, String name, String password) {
    super(id, name, password);
  }

  public boolean eventLoop() {

    System.out.println("Got here!");
    return true;

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
