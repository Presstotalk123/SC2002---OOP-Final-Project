package hms.Appointments;

/**
 * Represents the status of an appointment in the hospital management system.
 * An appointment can have one of the following statuses:
 * <ul>
 *   <li>{@link #CONFIRMED}</li>
 *   <li>{@link #CANCELLED}</li>
 *   <li>{@link #COMPLETED}</li>
 *   <li>{@link #PENDING}</li>
 * </ul>
 */

public enum AppointmentStatus {
  /**
    * Indicates that the appointment has been confirmed by the doctor.
    */
  confirmed,
  /**
    * Indicates that the appointment has been cancelled.
    */
  cancelled,
  /**
    * Indicates that the appointment has been completed.
    */

  completed,
  /**
     * Indicates that the appointment is pending approval.
     */
  pending
}
