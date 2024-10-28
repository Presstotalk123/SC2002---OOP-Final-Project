package hms.MedicalRecords;

import java.io.IOException;

public interface PatientView {
  String toString();

  void updateEmailAddress(String email);

  void updatePhoneNumber(String phoneNumber);

  void saveToFile() throws IOException;

}