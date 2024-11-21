package hms.Billing;

import java.io.IOException;
import java.util.List;

/**
 * Interface representing administrative billing operations in the hospital management system.
 * Provides methods for administrators to add new bills for patients based on prescription IDs.
 */

public interface billingAdmin {

    /**
     * Adds a new bill for a patient using the provided bill ID, patient ID, and a list of prescription IDs.
     *
     * @param billId          The unique identifier for the new bill.
     * @param patientId       The unique identifier of the patient to whom the bill belongs.
     * @param prescriptionIds A list of prescription IDs associated with the bill.
     * @throws IOException If an I/O error occurs during the operation.
     */
    
    void addBill(String billId, String patientId, List<String> prescriptionIds) throws IOException;

}
