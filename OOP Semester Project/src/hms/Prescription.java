public class Prescription {
    private String prescriptionID;
    private String medicationName;
    private String status;

    public Prescription(String prescriptionID, String medicationName, String status) {
        this.prescriptionID = prescriptionID;
        this.medicationName = medicationName;
        this.status = status;
    }

    public String getPrescriptionID() {
        return prescriptionID;
    }

    public void setPrescriptionID(String prescriptionID) {
        this.prescriptionID = prescriptionID;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
}