import java.util.Date;
import java.util.List;

public class MedicalRecord {
    private String recordID;
    private Patient patient;
    private List<String> diagnoses;
    private List<String> treatments;
    private List<String> medications;
    private Date lastUpdated;

    public MedicalRecord(String recordID, Patient patient, List<String> diagnoses, List<String> treatments, List<String> medications, Date lastUpdated) {
        this.recordID = recordID;
        this.patient = patient;
        this.diagnoses = diagnoses;
        this.treatments = treatments;
        this.medications = medications;
        this.lastUpdated = lastUpdated;
    }

    public String getRecordID() {
        return recordID;
    }

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<String> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(List<String> diagnoses) {
        this.diagnoses = diagnoses;
    }

    public List<String> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<String> treatments) {
        this.treatments = treatments;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "recordID='" + recordID + '\'' +
                ", patient=" + patient +
                ", diagnoses=" + diagnoses +
                ", treatments=" + treatments +
                ", medications=" + medications +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}