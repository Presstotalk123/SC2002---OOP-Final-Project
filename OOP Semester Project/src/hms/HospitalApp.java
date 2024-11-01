import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Date;
class HospitalApp {
    public static void main(String[] args) {
        List<Doctor> doctorList = new ArrayList<>();
        public Doctor getDoctorByUsername(String username) {
            for (Doctor doctor : doctorList) {
                if (doctor.getDoctorUsername().equals(username)) {
                    return doctor;
                }
            }
            return null;
        }
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
                    Doctor doctor = getDoctorByUsername(username);
                    if (doctor != null && doctor.login(password)) {
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
                    try {
                        Class<?> clazz = Class.forName("Patient");
                        Patient patient = (Patient) clazz.getDeclaredConstructor(String.class).newInstance(username);
                        if (patient.login(password)) {
                            System.out.println("Login successful");
                        } else {
                            System.out.println("Login failed");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 3: {
                    System.out.println("Enter your username:");
                    String username = sc.next();
                    System.out.println("Enter your password:");
                    String password = sc.next();
                    try {
                        Class<?> clazz = Class.forName("Administrator");
                        Administrator admin = (Administrator) clazz.getDeclaredConstructor(String.class).newInstance(username);
                        if (admin.login(password)) {
                            System.out.println("Login successful");
                        } else {
                            System.out.println("Login failed");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 4: {
                    System.out.println("Enter your username:");
                    String username = sc.next();
                    System.out.println("Enter your password:");
                    String password = sc.next();
                    try {
                        Class<?> clazz = Class.forName("Administrator");
                        Pharmacist pharma = (Pharmacist) clazz.getDeclaredConstructor(String.class).newInstance(username);
                        if (pharma.login(password)) {
                            System.out.println("Login successful");
                        } else {
                            System.out.println("Login failed");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
                    Class<?> clazz = Class.forName(className);

                    // Create a new instance of the class
                    Object obj = clazz.getDeclaredConstructor().newInstance();

            }
        }
    }
}