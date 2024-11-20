package hms.MedicalRecords;

import java.io.IOException;

public interface MedicalRecordDoctorView {
  void addDiagnosis(String diagnosis) throws IOException;


void addTreatments(String treatmentPlan) throws IOException;
}
