package hms;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * The {@code Staff} class is an abstract class that extends {@link User} and represents staff members in the hospital management system.
 * It includes additional attributes specific to staff, such as gender, age, phone number, email address, and specialization.
 */


public abstract class Staff extends User {
    public Gender gender;
    public int age;
    public String phoneNumber;
    public String emailAddress;

    public String specialization = "NA";

    /**
     * Constructs a new {@code Staff} object by prompting the user for input via a {@code Scanner}.
     * This constructor is typically used when creating a new staff member.
     *
     * @param scanner The {@code Scanner} object to read user input.
     * @param role    The role of the staff member (e.g., "doctor", "pharmacist").
     * @throws IOException If an I/O error occurs during saving.
     */
    
    public Staff(Scanner scanner, String role) throws IOException {
        super(scanner, role);
        try {
            super.save();
        } catch (IOException error) {
            System.out.println("Unable to save user " + name + " due to IOException: " + error.getMessage());
        }
        while (true) {
            System.out.print("Enter the gender for this user (male or female): ");
            String gender = scanner.nextLine().toLowerCase();
            if (gender.equals("male")) {
                this.gender = Gender.Male;
                break;
            } else if (gender.equals("female")) {
                this.gender = Gender.Female;
                break;
            } else {
                System.out.println("Invalid entry. Please specify either male or female.");
            }
        }

        while (true) {
            System.out.print("Enter the age for this user: ");
            String age = scanner.nextLine().toLowerCase();
            if (age.matches("^([0-9]+)$")) {
                this.age = Integer.parseInt(age);
                break;
            } else {
                System.out.println("Invalid entry. Please specify in the format 91234567.");
            }
        }

        while (true) {
            System.out.print("Enter the phone number for this user (Singaporean Numbers Only, like 91234567): ");
            String number = scanner.nextLine().toLowerCase();
            if (number.matches("^([0-9]{8})$") && number.length() == 8) {
                this.phoneNumber = number;
                break;
            } else {
                System.out.println("Invalid entry. Please specify in the format 91234567.");
            }
        }

        while (true) {
            System.out.print("Enter the email address for this user: ");
            String email = scanner.nextLine().toLowerCase();
            if (email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                this.emailAddress = email;
                break;
            } else {
                System.out.println("Invalid entry. Please enter a valid email address.");
            }
        }
        if (role == "doctor") {
            while (true) {
                System.out.print("Enter the specialization for this doctor: ");
                String specialization = scanner.nextLine().toLowerCase();
                if (specialization.matches("^[a-zA-Z]+$")) {
                    this.specialization = specialization;
                    break;
                } else {
                    System.out.println("Invalid entry. Please enter a valid specialization.");
                }
            }
        }

    }

    /**
     * Constructs a new {@code Staff} object by loading staff data from a file.
     * This constructor is typically used when loading an existing staff member.
     *
     * @param id       The ID of the staff member.
     * @param name     The name of the staff member.
     * @param password The password of the staff member.
     * @param role     The role of the staff member.
     * @throws IOException If an I/O error occurs during loading staff data.
     */


    public Staff(String id, String name, String password, String role) throws IOException {
        super(id, name, password, role);
        String[] staffData = Staff.loadStaffDataFromFile(id);
        this.gender = staffData[1].equals("male") ? Gender.Male : Gender.Female;
        this.age = Integer.parseInt(staffData[2]);
        this.phoneNumber = staffData[4];
        this.emailAddress = staffData[5];
    }

    public String getRole() {
        return this.role;
    }
    public String getName() {
        return this.name;
    }
    
    public String getGender() {
        return this.gender.toString().toLowerCase();
    }

    /**
     * Updates the phone number of the staff member after validating it.
     *
     * @param phoneNumber The new phone number to set (must be an 8-digit number).
     * @throws Exception If the phone number is invalid.
     */

    public void updatePhoneNumber(String phoneNumber) throws Exception {
        if (phoneNumber.matches("^([0-9]{8})$")) { // Validate 8-digit phone number
            this.phoneNumber = phoneNumber;
        } else {
            throw new Exception("Invalid entry. Please specify in the format 91234567.");
        }
    }

    public void updateEmailAddress(String email) throws Exception {
        if (email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) { // Validate email
            this.emailAddress = email;
        } else {
            throw new Exception("Invalid entry. Please enter a valid email address.");
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Saves the staff member's data to the staff CSV file.
     * If the staff member already exists, their data is updated; otherwise, a new entry is added.
     *
     * @throws IOException If an I/O error occurs during saving.
     */

    
    public void save() throws IOException {
        // staff.csv: id,gender,age,role,phoneNumber,email
        List<String> lines = Files.readAllLines(Paths.get("../data/staff.csv"));
        FileOutputStream output = new FileOutputStream("../data/staff.csv");

        boolean isEntryFound = false;
        for (int i = 0; i < lines.size(); i++) {
            String[] appt = lines.get(i).split(",");

            if (appt.length == 6 && appt[0].equals(this.id)) {
                String newEntry = this.id + "," + this.gender.toString().toLowerCase() + "," + this.age + ","
                        + this.role + "," + this.phoneNumber + "," + this.emailAddress + "," + this.specialization + "\n";
                output.write(newEntry.getBytes());
                isEntryFound = true;
            } else {
                String line = lines.get(i) + "\n";
                output.write(line.getBytes());
            }
        }

        if (!isEntryFound) {
            String newEntry = this.id + "," + this.gender.toString().toLowerCase() + "," + this.age + ","
                    + this.role + "," + this.phoneNumber + "," + this.emailAddress + "," + this.specialization + "\n";
            output.write(newEntry.getBytes());
        }

        output.close();
    }

    /**
     * Loads staff data from the staff CSV file based on the given staff ID.
     *
     * @param id The ID of the staff member.
     * @return An array of strings containing the staff data.
     * @throws IOException If an I/O error occurs or if the staff data is missing or invalid.
     */

    private static String[] loadStaffDataFromFile(String id) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader("../data/staff.csv"));

        String nextLine = file.readLine();
        while ((nextLine = file.readLine()) != null) {
            String[] staff = nextLine.split(",");

            if (staff.length < 6) {
                continue;
            }

            String currentId = staff[0];

            if (currentId.equals(id)) {
                file.close();
                return staff;
            }
        }

        file.close();
        throw new IOException("Missing or Invalid Patient Data found in staff.csv for patient with ID: " + id);
    }

}
