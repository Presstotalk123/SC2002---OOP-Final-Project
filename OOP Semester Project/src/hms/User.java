package hms;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The {@code User} class is an abstract base class that represents a user in the hospital management system.
 * It includes common attributes and methods shared by all types of users, such as patients, doctors, administrators, and pharmacists.
 */

public abstract class User {
  public String id;
  public String name;
  private String password;
  public final String role;

    /**
     * Constructs a new {@code User} by prompting for necessary information via a {@code Scanner}.
     * This constructor is intended to be called from subclasses when creating a new user.
     *
     * @param scanner The {@code Scanner} object to read user input.
     * @param role    The role of the user.
     * @throws IOException If an I/O error occurs during file operations.
     */
  
  public User(Scanner scanner, String role) throws IOException {
    System.out.print("Enter a name for this new user: ");
    String name = scanner.nextLine();
    while(true){
    System.out.print("Enter a password for this new user: ");
    String password = scanner.nextLine();
    System.out.print("Re-enter the password: ");
    String password2 = scanner.nextLine();
    if(password.equals(password2)){
      this.password = password;
      break;
    } else {
      System.out.println("Passwords do not match. Please try again.");
    }
    }

    Random rand = new Random();
    List<String> existingIds = Files.readAllLines(Paths.get("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\users.csv"))
            .stream()
            .map(line -> line.split(",")[0])
            .collect(Collectors.toList());
    if(Objects.equals(role, "patient")){
      while (true) {
        int ID = rand.nextInt(9000) + 1000;
        String id="P"+ID;
        if (!existingIds.contains(id)) {
          System.out.println("Your ID is: " + id);
          this.id = id;
          break;
        }
      }
    }
    else if(Objects.equals(role, "doctor")){
      while (true) {
        int ID = rand.nextInt(900) + 100;
        String id="D"+ID;
        if (!existingIds.contains(id)) {
          System.out.println("Your ID is: " + id);
          this.id = id;
          break;
        }
      }
    }
    else if(Objects.equals(role, "administrator")){
      while (true) {
        int ID = rand.nextInt(900) + 100;
        String id="A"+ID;
        if (!existingIds.contains(id)) {
          System.out.println("Your ID is: " + id);
          this.id = id;
          break;
        }
      }
    }
    else if(Objects.equals(role, "pharmacist")){
      while (true) {
        int ID = rand.nextInt(900) + 100;
        String id="P"+ID;
        if (!existingIds.contains(id)) {
          System.out.println("Your ID is: " + id);
          this.id = id;
          break;
        }
      }
    }
    this.name = name;
    this.role = role;

  }

   /**
     * Constructs a new {@code User} with the specified attributes.
     * This constructor is intended to be called when loading user data from a file.
     *
     * @param id       The unique identifier for the user.
     * @param name     The name of the user.
     * @param password The password for the user's account.
     * @param role     The role of the user.
     */

  public User(String id, String name, String password, String role) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.role = role;
  }

   /**
     * Validates the user's login by comparing the provided password with the stored password.
     *
     * @param password The password entered by the user.
     * @return {@code true} if the password matches; {@code false} otherwise.
     */
  
  public boolean login(String password) {
    return this.password.equals(password);
  }

   /**
     * Loads all users from the specified CSV file.
     *
     * @param path The file path to load users from.
     * @return A list of {@code User} objects.
     * @throws IOException If an I/O error occurs during file reading.
     */
  
  public static List<User> loadFromFile(String path) throws IOException {
    List<User> userArray = new ArrayList<User>();
    BufferedReader file = new BufferedReader(new FileReader("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\users.csv"));
    String nextLine = file.readLine();
    while ((nextLine = file.readLine()) != null) {
      String[] user = nextLine.split(",");
      String role = user[3];
      // TOOD: Add support for all kinds of users
      if (role.equals("patient")) {
        userArray.add(new Patient(user[0], user[1], user[2]));
      } else if (role.equals("administrator")) {
        userArray.add(new Administrator(user[0], user[1], user[2]));
      } else if (role.equals("doctor")) {
        userArray.add(new Doctor(user[0], user[1], user[2]));
      } else if (role.equals("pharmacist")) {
        userArray.add(new Pharmacist(user[0], user[1], user[2]));
      }
    }
    file.close();
    return userArray;
  }

  /**
     * Saves the user's information to the CSV file. If the user already exists, their information is updated.
     *
     * @throws IOException If an I/O error occurs during file operations.
     */
  
  public void save() throws IOException {
    String filePath = "C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\users.csv";

    // Read all lines from the file
    List<String> lines = Files.readAllLines(Paths.get(filePath));
    boolean isUpdated = false;

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        for (String line : lines) {
            String[] parts = line.split(",");
            
            // If the ID matches, update the user details
            if (parts[0].equals(this.id)) {
                writer.write(this.id + "," + this.name + "," + this.password + "," + this.role + "\n");
                isUpdated = true;
            } else {
                writer.write(line + "\n"); // Write the existing line back if it doesn't match
            }
        }

        // If the user is not found in the file, add a new entry
        if (!isUpdated) {
            writer.write(this.id + "," + this.name + "," + this.password + "," + this.role + "\n");
        }
    } catch (IOException e) {
        System.out.println("Error saving user data: " + e.getMessage());
    }
}

/**
     * Allows a user to reset their password if they have forgotten it.
     * Validates the user's identity based on their hospital ID and name.
     *
     * @param hospitalId The hospital ID of the user.
     * @param name       The name of the user.
     * @param scanner    The {@code Scanner} object to read user input.
     * @throws IOException If an I/O error occurs during file operations.
     */

  
public static void forgotPassword(String hospitalId, String name, Scanner scanner) throws IOException {
  List<User> users = loadFromFile("C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\users.csv");
  User userToReset = null;

  for (User user : users) {
      if (user.id.equals(hospitalId) && user.name.equals(name)) {
          userToReset = user;
          break;
      }
  }

  if (userToReset == null) {
      System.out.println("Incorrect Hospital ID or Name. Please contact the administrator for password reset!");
      return;
  }

  while (true) {
      System.out.print("Enter a new password for this user: ");
      String newPassword = scanner.nextLine();
      System.out.print("Re-enter the new password: ");
      String newPassword2 = scanner.nextLine();
      if (newPassword.equals(newPassword2)) {
          userToReset.password = newPassword;
          userToReset.save(); // This will now correctly update the existing entry
          System.out.println("Password reset successfully.");
          break;
      } else {
          System.out.println("Passwords do not match. Please try again.");
      }
  }
}

  /**
     * Retrieves the user's password.
     * This method is protected to allow subclasses access if necessary.
     *
     * @return The user's password.
     */
  
protected String getPassword() {
  return this.password;
}
  /**
     * The event loop that handles user-specific interactions.
     * Subclasses must implement this method to provide functionality.
     *
     * @param scanner The {@code Scanner} object to read user input.
     * @return {@code true} to continue; {@code false} to log out.
     * @throws IOException If an I/O error occurs during operations.
     */
  // Return true to logout
  public abstract boolean eventLoop(Scanner scanner) throws IOException;
}
