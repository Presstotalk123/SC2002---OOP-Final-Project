package hms.MedicalRecords;

import java.io.IOException;

/**
 * Interface representing the patient's view of their medical records.
 * Provides methods for patients to interact with their medical records,
 * such as updating contact information, adding allergies, diagnoses, and treatments.
 */

public interface MedicalRecordPatientView {
  String toString();

  void updateEmailAddress(String email);

  void updatePhoneNumber(String phoneNumber);

  void saveToFile() throws IOException;

  void addAllergy(String allergy); 

  void addDiagnosis(String diagnosis) throws IOException;

  void addTreatments(String treatment) throws IOException;
}
