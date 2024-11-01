import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Doctor extends User {
    private final String path="Doctor.csv";
    private String specialty;
    private List<Appointment> appointments;
    private String Username;

    public Doctor(int hospitalID, String password, String name, Date dateOfBirth, String gender, 
                  ContactInfo contactInfo, String specialty, String Username, String csvFilePath) {
        super(hospitalID, password, name, dateOfBirth, gender, contactInfo);
        this.specialty = specialty;
        this.Username = Username;
        this.appointments = loadAppointmentsFromCSV(path);  // Load initial appointments
    }
    public void saveToCSV() {
        try (FileWriter fileWriter = new FileWriter(path, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                    hospitalID,
                    password,
                    getName(), // Use the getter here instead of direct field access
                    getDateOfBirth().toString(),
                    getGender(),
                    getContactInfo().getPhoneNumber(),
                    getContactInfo().getEmailAddress(),
                    this.specialty=specialty,
                    this.Username=Username);
                    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSpeciality(String Username) {
        String line;
        String separator = ","; 
    
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            // Skip header row
            br.readLine();
    
            // Process each line in the CSV file
            while ((line = br.readLine()) != null) {
                String[] values = line.split(separator);
    
                // Ensure values array has the correct length before accessing specific indices
                if (values.length >= 9 && values[8].equals(Username)) { // Assuming 'Username' is at index 8
                    return values[7]; // Assuming 'specialty' is at index 7
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return null; // return null if username not found
    }

    public boolean setSpeciality(String Username, String newSpecialty) {
        List<String> lines = new ArrayList<>();
        String separator = ",";
        boolean isUpdated = false;
    
        // Read the file and store each line in a list
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine(); // Read the header
            if (line != null) {
                lines.add(line); // Add header to the list
            }
    
            // Process each line in the CSV file
            while ((line = br.readLine()) != null) {
                String[] values = line.split(separator);
    
                // Check if this line's Username matches the target Username
                if (values.length >= 9 && values[8].equals(Username)) { // Username is at index 8
                    values[7] = newSpecialty; // Update specialty at index 7
                    isUpdated = true;
                }
    
                // Reconstruct the line and add it to the list
                lines.add(String.join(separator, values));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Return false if there was an error reading the file
        }
    
        // Write the updated lines back to the CSV file
        if (isUpdated) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
                for (String outputLine : lines) {
                    writer.println(outputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false; // Return false if there was an error writing to the file
            }
        }
    
        return isUpdated; // Return true if the specialty was successfully updated
    }
    
    public String getHospitalID(String Username) {
        String line;
        String separator = ",";
    
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Skip header row
    
            while ((line = br.readLine()) != null) {
                String[] values = line.split(separator);
    
                if (values.length >= 9 && values[8].equals(Username)) { // Username at index 8
                    return values[0]; // hospitalID at index 0
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return null; // Return null if username not found
    }

    public String getPassword(String Username) {
        String line;
        String separator = ",";
    
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Skip header row
    
            while ((line = br.readLine()) != null) {
                String[] values = line.split(separator);
    
                if (values.length >= 9 && values[8].equals(Username)) { // Username at index 8
                    return values[1]; // password at index 1
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return null; // Return null if username not found
    }
    
    public String getName(String Username) {
        String line;
        String separator = ",";
    
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Skip header row
    
            while ((line = br.readLine()) != null) {
                String[] values = line.split(separator);
    
                if (values.length >= 9 && values[8].equals(Username)) { // Username at index 8
                    return values[2]; // name at index 2
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return null; // Return null if username not found
    }

    public String getDateOfBirth(String Username) {
        String line;
        String separator = ",";
    
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Skip header row
    
            while ((line = br.readLine()) != null) {
                String[] values = line.split(separator);
    
                if (values.length >= 9 && values[8].equals(Username)) { // Username at index 8
                    return values[3]; // dateOfBirth at index 3
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return null; // Return null if username not found
    }

    public String getGender(String Username) {
        String line;
        String separator = ",";
    
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Skip header row
    
            while ((line = br.readLine()) != null) {
                String[] values = line.split(separator);
    
                if (values.length >= 9 && values[8].equals(Username)) { // Username at index 8
                    return values[4]; // gender at index 4
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return null; // Return null if username not found
    }

    public String getPhoneNumber(String Username) {
        String line;
        String separator = ",";
    
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Skip header row
    
            while ((line = br.readLine()) != null) {
                String[] values = line.split(separator);
    
                if (values.length >= 9 && values[8].equals(Username)) { // Username at index 8
                    return values[5]; // phoneNumber at index 5
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return null; // Return null if username not found
    }

    public String getEmailAddress(String Username) {
        String line;
        String separator = ",";
    
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Skip header row
    
            while ((line = br.readLine()) != null) {
                String[] values = line.split(separator);
    
                if (values.length >= 9 && values[8].equals(Username)) { // Username at index 8
                    return values[6]; // emailAddress at index 6
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return null; // Return null if username not found
    }
}
