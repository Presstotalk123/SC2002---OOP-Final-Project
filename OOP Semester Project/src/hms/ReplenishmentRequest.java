import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class ReplenishmentRequest {
    private String requestID;
    private String medicationName;
    private int quantity;
    private String status;

    public ReplenishmentRequest(String requestID, String medicationName, int quantity, String status) {
        this.requestID = requestID;
        this.medicationName = medicationName;
        this.quantity = quantity;
        this.status = status;
    }

    public String getRequestID() {
        return requestID;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void saveToCSV(String filePath) {
    // Create a File object for the CSV file
    File file = new File(filePath);

    // Check if the file exists; if not, create it
    try {
        if (file.createNewFile()) {
            System.out.println("CSV file created: " + file.getName());
        } else {
            System.out.println("CSV file already exists.");
        }
    } catch (IOException e) {
        System.out.println("An error occurred while creating the file.");
        e.printStackTrace();
    }

    // Append the replenishment request to the CSV file
    try (FileWriter fileWriter = new FileWriter(filePath, true);
         PrintWriter printWriter = new PrintWriter(fileWriter)) {
        printWriter.printf("%s,%s,%d,%s%n",
                requestID,
                medicationName,
                quantity,
                status);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    /**
     * Loads replenishment requests from a CSV file.
     *
     * @param filePath the path of the CSV file.
     * @return a list of ReplenishmentRequest objects.
     */
    public static List<ReplenishmentRequest> loadFromCSV(String filePath) {
        List<ReplenishmentRequest> requests = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String requestID = values[0];
                String medicationName = values[1];
                int quantity = Integer.parseInt(values[2]);
                String status = values[3];
                requests.add(new ReplenishmentRequest(requestID, medicationName, quantity, status));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requests;
    }
}