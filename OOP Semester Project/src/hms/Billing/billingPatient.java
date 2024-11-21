package hms.Billing;

import java.io.IOException;

/**
 * Interface representing patient billing operations in the hospital management system.
 * Provides methods for patients to interact with their bills, such as paying a bill.
 */

public interface billingPatient {
    
    /**
     * Pays the bill with the specified bill ID.
     *
     * @param BillId The unique identifier of the bill to be paid.
     * @throws IOException If an I/O error occurs during the payment process.
     */

    void paybill(String BillId) throws IOException;
}
