package hms.Billing;

import java.io.IOException;
import java.util.List;

public interface billingAdmin {
    void addBill(String billId, String patientId, List<String> prescriptionIds) throws IOException;

}
