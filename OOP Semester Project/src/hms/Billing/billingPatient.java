package hms.Billing;

import java.io.IOException;

public interface billingPatient {
    void paybill(String BillId) throws IOException;
}
