package hms;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public abstract class User {
  public String id;
  public String name;
  private String password;
  public final String role;

  // When a constructor with a scanner is used, it is expected for the subclass to
  // prompt for data.
  // All subclasses should implement the scanner constructor pattern to create new
  // instance.
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


  public User(String id, String name, String password, String role) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.role = role;
  }

  public boolean login(String password) {
    return this.password.equals(password);
  }

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

protected String getPassword() {
  return this.password;
}


  // Return true to logout
  public abstract boolean eventLoop(Scanner scanner) throws IOException;
}
