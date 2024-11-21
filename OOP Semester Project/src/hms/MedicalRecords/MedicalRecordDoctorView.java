package hms.MedicalRecords;

import java.io.IOException;

/**
 * Interface representing the doctor's view of a patient's medical records.
 * Provides methods for doctors to interact with a patient's medical records,
 * such as adding diagnoses and treatment plans.
 */

public interface MedicalRecordDoctorView {
  void addDiagnosis(String diagnosis) throws IOException;


void addTreatments(String treatmentPlan) throws IOException;
}
