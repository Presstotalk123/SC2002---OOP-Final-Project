package hms.MedicalRecords;

import java.io.IOException;

public interface MedicalRecordPatientView {
  String toString();

  void updateEmailAddress(String email);

  void updatePhoneNumber(String phoneNumber);

  void saveToFile() throws IOException;

  void addAllergy(String allergy); 

  void addDiagnosis(String diagnosis) throws IOException;

  void addTreatments(String treatment) throws IOException;
}