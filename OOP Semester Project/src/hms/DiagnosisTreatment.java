package hms;

import java.util.Date;
/**
 * The {@code DiagnosisTreatment} class represents the relationship between a diagnosis and its corresponding treatment plan.
 * It includes details such as diagnosis ID, treatment ID, dates of diagnosis and treatment, and a description.
 */
public class DiagnosisTreatment {
    private String diagnosisID;
    private String treatmentID;
    private Date dateDiagnosed;
    private Date startDate;
    private Date endDate;
    private String description;

    
    /**
     * Constructs a new {@code DiagnosisTreatment} instance with the specified details.
     *
     * @param diagnosisID   The unique identifier for the diagnosis.
     * @param treatmentID   The unique identifier for the treatment.
     * @param dateDiagnosed The date when the diagnosis was made.
     * @param startDate     The start date of the treatment.
     * @param endDate       The end date of the treatment.
     * @param description   A description of the diagnosis and treatment plan.
     */

    public DiagnosisTreatment(String diagnosisID, String treatmentID, Date dateDiagnosed, Date startDate, Date endDate, String description) {
        this.diagnosisID = diagnosisID;
        this.treatmentID = treatmentID;
        this.dateDiagnosed = dateDiagnosed;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    public String getDiagnosisID() {
        return diagnosisID;
    }

    public void setDiagnosisID(String diagnosisID) {
        this.diagnosisID = diagnosisID;
    }

    public String getTreatmentID() {
        return treatmentID;
    }

    public void setTreatmentID(String treatmentID) {
        this.treatmentID = treatmentID;
    }

    public Date getDateDiagnosed() {
        return dateDiagnosed;
    }

    public void setDateDiagnosed(Date dateDiagnosed) {
        this.dateDiagnosed = dateDiagnosed;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns a string representation of the {@code DiagnosisTreatment} object.
     *
     * @return A string containing the details of the diagnosis and treatment.
     */
    
    @Override
    public String toString() {
        return "DiagnosisTreatment{" +
                "diagnosisID='" + diagnosisID + '\'' +
                ", treatmentID='" + treatmentID + '\'' +
                ", dateDiagnosed=" + dateDiagnosed +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", description='" + description + '\'' +
                '}';
    }
}
