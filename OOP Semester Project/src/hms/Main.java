package hms;

import java.util.List;
import java.io.IOException;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {

    // Load data on program startup
    List<User> users;
    try {
      users = User.load_from_file("../data/users.csv");
      System.out.println(users);
    } catch (IOException error) {
      System.out.println("Error occurred when loading data: ");
      error.printStackTrace();
      return;
    }

    System.out.println("Welcome to Hospital Management System!");

    while (true) {
      Scanner input = new Scanner(System.in);

      System.out.print("Please enter your Hospital ID: ");
      String hospitalId = input.nextLine().replace("\n", "");
      System.out.print("Please enter your Password: ");
      String password = input.nextLine().replace("\n", "");

      input.close();

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
        while (loggedInUser.eventLoop()) {}
      }

    }


  }

}