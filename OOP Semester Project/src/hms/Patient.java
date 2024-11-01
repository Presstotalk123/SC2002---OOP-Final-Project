import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient extends User {
    private String patientUsername;

    private final String path = "Patient.csv";

    public Patient(Integer hospitalID, String password, String name, Date dateOfBirth, String gender, ContactInfo contactInfo ) {
        super(hospitalID,
                password,
                name,
                dateOfBirth,
                gender,
                contactInfo);
        this.patientUsername = patientUsername;
    }

    public void saveToCSV() {
        try (FileWriter fileWriter = new FileWriter(path, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.printf("%d,%s,%s,%s,%s,%s,%s,%s",
                    hospitalID,
                    password,
                    name,
                    dateOfBirth.toString(),
                    gender,
                    contactInfo.getPhoneNumber(),
                    contactInfo.getEmailAddress(),
                    this.patientUsername);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer gethospitalID(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[7].equals(username)) {
                    return Integer.parseInt(parts[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean changepatientUsername(String oldUsername, String newUsername) {
        List<String> lines = new ArrayList<>();
        boolean usernameChanged = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[7].equals(oldUsername)) {
                    parts[7] = newUsername;
                    usernameChanged = true;
                }
                lines.add(String.join(",", parts));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (usernameChanged) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
                for (String updatedLine : lines) {
                    writer.println(updatedLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return usernameChanged;
    }
    public String getpatientUsername(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[7].equals(username)) {
                    return parts[7];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public String getname(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[7].equals(username)) {
                    return parts[2];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getDateOfBirth(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[7].equals(username)) {
                    return parts[3];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getGender(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[7].equals(username)) {
                    return parts[4];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public String getPhoneNumber(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[7].equals(username)) {
                    return parts[5];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getEmail(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[7].equals(username)) {
                    return parts[6];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean login(String username,String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                return parts[7].equals(username) && parts[1].equals(password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }



/*public static Patient fromCSV(String csv) {
        String[] parts = csv.split(",");
        String patientID = parts[0];
        String name = parts[1];
        Date dateOfBirth = new Date(Long.parseLong(parts[2]));
        String gender = parts[3];
        ContactInfo contactInfo = new ContactInfo(parts[4], parts[5]); // Assuming a constructor that takes address, phone, and email
        String password = parts[6];
        return new Patient(patientID, name, dateOfBirth, gender, contactInfo, password);
    }*/
}
