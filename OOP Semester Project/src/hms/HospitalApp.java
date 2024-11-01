import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Date;
class HospitalApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hospital");
        System.out.println("LOGIN");
        System.out.println("1. Login\n2. Sign up");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        if(choice==1) {
            System.out.println("Login as:\n1. Doctor\n2. Patient\n3. Admin\n4. Pharmacist");
            choice = sc.nextInt();
            switch (choice) {
                case 1: {
                    System.out.println("Enter your username:");
                    String username = sc.next();
                    System.out.println("Enter your password:");
                    String password = sc.next();
                    Login login=new Login();
                    if (login.login(username, password, "Doctor.csv")) {
                        System.out.println("Login successful");
                    } else {
                        System.out.println("Login failed");
                    }
                    break;
                }
                case 2: {
                    System.out.println("Enter your username:");
                    String username = sc.next();
                    System.out.println("Enter your password:");
                    String password = sc.next();
                    Login login=new Login();
                    if (login.login(username, password, "Patient.csv")) {
                        System.out.println("Login successful");
                    } else {
                        System.out.println("Login failed");
                    }
                    break;
                }
                case 3: {
                    System.out.println("Enter your username:");
                    String username = sc.next();
                    System.out.println("Enter your password:");
                    String password = sc.next();
                    Login login=new Login();
                    if (login.login(username, password, "Admin.csv")) {
                        System.out.println("Login successful");
                    } else {
                        System.out.println("Login failed");
                    }
                    break;
                }
                case 4: {
                    System.out.println("Enter your username:");
                    String username = sc.next();
                    System.out.println("Enter your password:");
                    String password = sc.next();
                    Login login=new Login();
                    if (login.login(username, password, "Pharmacist.csv")) {
                        System.out.println("Login successful");
                    } else {
                        System.out.println("Login failed");
                    }
                    break;
                }
            }
        }
        else{
            System.out.println("Enter your Name:");
            String name = sc.next();
            System.out.println("Enter Date of Birth (DD-MM-YYYY):");
            String DOB = sc.next();
            System.out.println("Gender:");
            String gender=sc.next();
            System.out.println("contact:");
            int Contact=sc.nextInt();
            System.out.println("Type of user:\nDoctor\nPatient\nAdmin\nPharmacist");
            String user=sc.next();
            switch(user){
                case "Doctor":
                    System.out.println("Enter your Specialty:");
                    String specialty=sc.next();
                    System.out.println("Enter your username:");
                    String username=sc.next();
                    System.out.println("Enter your password:");
                    String password=sc.next();
                   User user=new Doctor(username,password,name,DOB,)

            }
        }
    }
}
