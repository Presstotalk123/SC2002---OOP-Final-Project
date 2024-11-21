package hms;

import java.io.*;
import java.util.*;

/* It allows patients to submit feedback, which includes comments and a rating.
 * Administrators can view all feedback submitted by patients.
 */

public class Feedback {
    private static final String FEEDBACK_FILE = "C:\\Users\\welcome\\Desktop\\OOP---SC2002-Group-Project 3\\OOP---SC2002-Group-Project\\OOP Semester Project\\data\\feedback.csv";
    private String id; // Unique feedback ID
    private String patientId;
    private String comments;
    private int rating;

    /**
     * Constructs a new {@code Feedback} instance with the specified patient ID, comments, and rating.
     * Generates a unique feedback ID upon creation.
     *
     * @param patientId The ID of the patient providing the feedback.
     * @param comments  The comments provided by the patient.
     * @param rating    The rating provided by the patient (e.g., 1-5).
     */
    
    public Feedback(String patientId, String comments, int rating) {
        this.id = "FB" + (new Random().nextInt(9000) + 1000);
        this.patientId = patientId;
        this.comments = comments;
        this.rating = rating;
    }

    /**
     * Saves the feedback to the feedback CSV file.
     * If the file does not exist, it creates a new one with a header row.
     *
     * @throws IOException If an I/O error occurs during file operations.
     */
    
    public void save() throws IOException {
        File file = new File(FEEDBACK_FILE);
        boolean isNewFile = !file.exists();

        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
            if (isNewFile) {
                pw.println("id,patientId,comments,rating"); // Header row
            }
            pw.printf("%s,%s,%s,%d%n", this.id, this.patientId, this.comments.replace(",", " "), this.rating);
        }
    }

     /**
     * Retrieves all feedback entries from the feedback CSV file.
     *
     * @return A list of strings, each representing a line from the feedback file.
     * @throws IOException If an I/O error occurs during file reading.
     */

    public static List<String> viewAllFeedback() throws IOException {
        List<String> feedbackList = new ArrayList<>();
        File file = new File(FEEDBACK_FILE);
        if (!file.exists()) {
            return feedbackList;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FEEDBACK_FILE))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                feedbackList.add(line);
            }
        }
        return feedbackList;
    }
}
