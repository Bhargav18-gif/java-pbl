package hospital.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model class for Doctor Appointments.
 */
public class Appointment implements Searchable, Serializable {
    private static final long serialVersionUID = 1L;

    private String appointmentId;
    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private String doctorSpecialization;
    private LocalDate appointmentDate;
    private String timeSlot;
    private PatientType type;
    private AppointmentStatus status;
    private String notes;
    private LocalDateTime bookedAt;

    public Appointment() {
        this.bookedAt = LocalDateTime.now();
        this.status = AppointmentStatus.SCHEDULED;
        this.type = PatientType.REGULAR;
    }

    public Appointment(String appointmentId, String patientId, String patientName,
                       String doctorId, String doctorName, String doctorSpecialization,
                       LocalDate appointmentDate, String timeSlot, PatientType type,
                       AppointmentStatus status, String notes) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.doctorSpecialization = doctorSpecialization;
        this.appointmentDate = appointmentDate;
        this.timeSlot = timeSlot;
        this.type = type != null ? type : PatientType.REGULAR;
        this.status = status != null ? status : AppointmentStatus.SCHEDULED;
        this.notes = notes;
        this.bookedAt = LocalDateTime.now();
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorSpecialization() {
        return doctorSpecialization;
    }

    public void setDoctorSpecialization(String doctorSpecialization) {
        this.doctorSpecialization = doctorSpecialization;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public PatientType getType() {
        return type;
    }

    public void setType(PatientType type) {
        this.type = type;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }

    public String getFormattedDate() {
        return appointmentDate != null ? appointmentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
    }

    @Override
    public boolean matches(String query) {
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        String q = query.toLowerCase().trim();
        return (appointmentId != null && appointmentId.toLowerCase().contains(q)) ||
               (patientId != null && patientId.toLowerCase().contains(q)) ||
               (patientName != null && patientName.toLowerCase().contains(q)) ||
               (doctorId != null && doctorId.toLowerCase().contains(q)) ||
               (doctorName != null && doctorName.toLowerCase().contains(q)) ||
               (timeSlot != null && timeSlot.toLowerCase().contains(q)) ||
               (status != null && status.name().toLowerCase().contains(q));
    }
}
