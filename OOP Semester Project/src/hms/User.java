package hms;

import java.io.BufferedReader;  
import java.io.FileReader;  
import java.io.IOException;  
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class User {

  public String id;
  public String name;
  private String password;

  public User(String id, String name, String password) {
    this.id = id;
    this.name = name;
    this.password = password;
  }

  public boolean login(String password) {
    return this.password.equals(password);
  }

  public static List<User> loadFromFile(String path) throws IOException {

    List<User> userArray = new ArrayList<User>();
    BufferedReader file = new BufferedReader(new FileReader(path));

    String nextLine = file.readLine();
    while ((nextLine = file.readLine()) != null) {
      String[] user = nextLine.split(",");

      String role = user[3];
      // TOOD: Add support for all kinds of users
      if (role.equals("patient")) {
        userArray.add(new Patient(user[0], user[1], user[2]));
      } else if (role.equals("admin")) {
        userArray.add(new HMSAdmin(user[0], user[1], user[2]));
      }
    }

    file.close();

    return userArray;
  }

  // Return true to logout
  public abstract boolean eventLoop(Scanner scanner);

}