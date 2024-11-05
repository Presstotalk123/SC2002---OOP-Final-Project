package hms;

// import java.io.FileWriter;
import java.io.IOException;
// import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
// import java.util.Date;

class Main {

  public static void main(String[] args) {
    System.out.println("Welcome to Hospital Management System!");

    Scanner scanner = new Scanner(System.in);

    while (Main.eventLoop(scanner)) {
    }

    System.out.println("Bye!");
  }

  public static boolean eventLoop(Scanner scanner) {
    // Load data on program startup
    List<User> users;
    try {
      users = User.loadFromFile("/Users/sam/programming/OOP---SC2002-Group-Project/OOP Semester Project/data/users.csv");
      System.out.println(users);
    } catch (IOException error) {
      System.out.println("Error occurred when loading data: ");
      error.printStackTrace();
      return false;
    }

    System.out.print("""
          What would you like to do?
          1. Log In
          2. Sign Up
          3. Exit
        """);

    int input = scanner.nextInt();
    scanner.nextLine();

    switch (input) {
      case 1:

        System.out.print("Please enter your Hospital ID: ");
        String hospitalId = scanner.nextLine().replace("\n", "");
        System.out.print("Please enter your Password: ");
        String password = scanner.nextLine().replace("\n", "");

        User loggedInUser = null;

        for (int i = 0; i < users.size(); i += 1) {

          User user = users.get(i);
          if (user.id.equals(hospitalId) && user.login(password)) {
            loggedInUser = user;
          }

        }

        if (loggedInUser == null) {
          System.out.println("Hospital ID or Password is incorrect! Please try again.");
        } else {
          System.out.println("> Successfully logged in as ".concat(loggedInUser.name));

          // Keep repeating the event loop of the user until eventLoop returns false
          // in other words, user logs out.
          while (loggedInUser.eventLoop(scanner)) {
          }
        }
        break;

      case 2:
        outerWhile:
        while (true) {
          System.out.print("""
                Select what kind of user you want to sign up as:
                1. Patient
                2. Administrator
                3. Doctor
                4. Pharmacist
                5. Go Back
              """);
          try {
            int selection = scanner.nextInt();
            scanner.nextLine();
            switch (selection) {
              case 1:
                new Patient(scanner);
                break outerWhile;
              case 2:
                new Administrator(scanner);
                break outerWhile;
              case 3:
                new Doctor(scanner);
                break outerWhile;
              case 4:
                new Pharmacist(scanner);
                break outerWhile;
              case 5:
                break outerWhile;
              default:
                System.out.println("Invalid input. Please enter a number from 1 to 4.");
                break;
            }
            System.out.println("Sign Up successful!. Please log in now.");
          } catch (NoSuchElementException error) {
            System.out.println("Invalid input. Please enter a number from 1 to 4.");
          }

        }
        break;

      case 3:
        return false;

      default:
        System.out.println("Invalid Selection. Please try again.");
        break;
    }

    return true;
  }

  // Below code is an antipattern. It doesn't provide an event loop and follows
  // bad design patterns like improper inheritance use.

  // public static void main(String[] args) {
  // System.out.println("Welcome to the Hospital");
  // System.out.println("LOGIN");
  // System.out.println("1. Login\n2. Sign up");
  // Scanner sc = new Scanner(System.in);
  // int choice = sc.nextInt();
  // if(choice==1) {
  // System.out.println("Login as:\n1. Doctor\n2. Patient\n3. Admin\n4.
  // Pharmacist");
  // choice = sc.nextInt();
  // switch (choice) {
  // case 1: {
  // System.out.println("Enter your username:");
  // String username = sc.next();
  // System.out.println("Enter your password:");
  // String password = sc.next();
  // Login login=new Login();
  // if (login.login(username, password, "Doctor.csv")) {
  // System.out.println("Login successful");
  // } else {
  // System.out.println("Login failed");
  // }
  // break;
  // }
  // case 2: {
  // System.out.println("Enter your username:");
  // String username = sc.next();
  // System.out.println("Enter your password:");
  // String password = sc.next();
  // Login login=new Login();
  // if (login.login(username, password, "Patient.csv")) {
  // System.out.println("Login successful");
  // } else {
  // System.out.println("Login failed");
  // }
  // break;
  // }
  // case 3: {
  // System.out.println("Enter your username:");
  // String username = sc.next();
  // System.out.println("Enter your password:");
  // String password = sc.next();
  // Login login=new Login();
  // if (login.login(username, password, "Admin.csv")) {
  // System.out.println("Login successful");
  // } else {
  // System.out.println("Login failed");
  // }
  // break;
  // }
  // case 4: {
  // System.out.println("Enter your username:");
  // String username = sc.next();
  // System.out.println("Enter your password:");
  // String password = sc.next();
  // Login login=new Login();
  // if (login.login(username, password, "Pharmacist.csv")) {
  // System.out.println("Login successful");
  // } else {
  // System.out.println("Login failed");
  // }
  // break;
  // }
  // }
  // }
  // else{
  // System.out.println("Enter your Name:");
  // String name = sc.next();
  // System.out.println("Enter Date of Birth (DD-MM-YYYY):");
  // String DOB = sc.next();
  // System.out.println("Gender:");
  // String gender=sc.next();
  // System.out.println("contact:");
  // int Contact=sc.nextInt();
  // System.out.println("Type of user:\nDoctor\nPatient\nAdmin\nPharmacist");
  // String user=sc.next();
  // switch(user){
  // case "Doctor":
  // System.out.println("Enter your Specialty:");
  // String specialty=sc.next();
  // System.out.println("Enter your username:");
  // String username=sc.next();
  // System.out.println("Enter your password:");
  // String password=sc.next();
  // User user=new Doctor(username,password,name,DOB,)

  // }
  // }
  // }
}
