package hms.Appointments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Interface representing the doctor's view of appointments.
 * Provides methods for doctors to accept, decline, and save appointments.
 */

public interface AppointmentDoctorView {

  void accept() throws IOException;
  void decline() throws IOException;
  void save() throws IOException;

  // Casts all appointsments into a DoctorView

  /**
     * Loads all appointments and casts them into a list of AppointmentDoctorView.
     *
     * @return A list of appointments as viewed by the doctor.
     * @throws IOException If an I/O error occurs during the operation.
     */

  
  static List<AppointmentDoctorView> loadAllAppointments() throws IOException {
    List<AppointmentDoctorView> appts = new ArrayList<AppointmentDoctorView>();
    Iterator<Appointment> it = Appointment.loadAllAppointments().iterator();
    while (it.hasNext()) {
      AppointmentDoctorView appt = it.next();
      appts.add(appt);
    }
    return appts;
  }
}
