import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public abstract class User {
    public Integer hospitalID;
    protected String password;
    public String name;
    public Date dateOfBirth;
    public String gender;
    public ContactInfo contactInfo;

    public User(Integer hospitalID, String password, String name, Date dateOfBirth, String gender, ContactInfo contactInfo) {
        hospitalID=new Random().nextInt(900000)+100000;
        this.hospitalID = hospitalID;
        this.password = password;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactInfo = contactInfo;
    }

    // Existing methods...

    /**
     * Saves the user details to a CSV file.
     *
     * @param filePath the path of the CSV file.
     */


    // Getters and Setters...

    public String getHospitalID() {
        return hospitalID;
    }
    

    public String getName() {
        return name;
    }

    public boolean changename(String username,String newname, String filePath) {
        List<String> lines = new ArrayList<>();
        boolean nameChanged = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[7].equals(username)) {
                    parts[2] = newname;
                    nameChanged = true;
                }
                lines.add(String.join(",", parts));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (nameChanged) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                for (String updatedLine : lines) {
                    writer.println(updatedLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return nameChanged;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public boolean changeGender(String username, String newGender, String filePath) {
        List<String> lines = new ArrayList<>();
        boolean GenderChanged = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[7].equals(username)) {
                    parts[4] = newGender;
                    GenderChanged = true;
                }
                lines.add(String.join(",", parts));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (GenderChanged) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                for (String updatedLine : lines) {
                    writer.println(updatedLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return GenderChanged;
    }

    public String getEmail() {

        return contactInfo.getEmailAddress();
    }
    public String getNumber() {
        return contactInfo.getPhoneNumber();
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

    public boolean changeEmail(String username, String newEmailID, String filePath) {
        List<String> lines = new ArrayList<>();
        boolean EmailChanged = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[7].equals(username)) {
                    parts[6] = newEmailID;
                    EmailChanged = true;
                }
                lines.add(String.join(",", parts));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (EmailChanged) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                for (String updatedLine : lines) {
                    writer.println(updatedLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return EmailChanged;
    }
    public boolean changePhoneNumber(String username, String newPhoneNumber, String filePath) {
        List<String> lines = new ArrayList<>();
        boolean PhoneNumberChanged = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[7].equals(username)) {
                    parts[5] = newPhoneNumber;
                    PhoneNumberChanged = true;
                }
                lines.add(String.join(",", parts));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (PhoneNumberChanged) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                for (String updatedLine : lines) {
                    writer.println(updatedLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return PhoneNumberChanged;
    }

    // Implement abstract methods from User if any (e.g., login, changePassword)


    public boolean changePassword(String username,String oldpassword, String newPassword, String filePath) {
        List<String> lines = new ArrayList<>();
        boolean PasswordChanged = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[7].equals(username) && parts[1].equals(oldpassword)) {
                    parts[6] = newPassword;
                    PasswordChanged = true;
                }
                lines.add(String.join(",", parts));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (PasswordChanged) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                for (String updatedLine : lines) {
                    writer.println(updatedLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return PasswordChanged;
    }
