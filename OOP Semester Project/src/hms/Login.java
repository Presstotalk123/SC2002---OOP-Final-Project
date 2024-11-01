import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Login {
    public boolean login(String username, String password, String path) {
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
}
