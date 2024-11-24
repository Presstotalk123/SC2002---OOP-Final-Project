package hms.Appointments;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Interface representing the patient's view of appointments.
 * Provides methods for patients to interact with their appointments,
 * such as scheduling, canceling, and retrieving appointment details.
 */

public interface AppointmentPatientView {

  String getId();

  String getDateTime();

  Optional<String> getPatientId();

  Optional<AppointmentStatus> getStatus();

  void schedule(String patient_id);

  void cancel();

  void save() throws IOException;

  boolean isBookable();

  /**
     * Loads all appointments from the data source.
     *
     * @return A list of all appointments.
     * @throws IOException If an I/O error occurs during the load operation.
     */
  
  static List<Appointment> loadAllAppointments() throws IOException {
    List<Appointment> appointments = new ArrayList<>();

    try (BufferedReader file = new BufferedReader(new FileReader("../data/appointments.csv"))) {
      String nextLine = file.readLine();
      while ((nextLine = file.readLine()) != null) {
        String[] appt = nextLine.split(",");
        if (appt.length < 6) {
          continue;
        }

        String status = appt[4];
        Optional<AppointmentStatus> apptStatus;
        if (status.toLowerCase().equals("confirmed"))
          apptStatus = Optional.of(AppointmentStatus.confirmed);
        else if (status.toLowerCase().equals("cancelled"))
          apptStatus = Optional.of(AppointmentStatus.cancelled);
        else if (status.toLowerCase().equals("completed"))
          apptStatus = Optional.of(AppointmentStatus.completed);
        else
          apptStatus = Optional.empty();

        appointments.add(
                new Appointment(
                        appt[0],
                        Optional.ofNullable(appt[1].equals("") ? null : appt[1]),
                        appt[2],
                        apptStatus,
                        LocalDateTime.parse(appt[5]),
                        Optional.ofNullable(appt[3].equals("") ? null : appt[3])));
      }
    }

    return appointments;
  }
  // A reschedule is just a cancel then schedule.
}
