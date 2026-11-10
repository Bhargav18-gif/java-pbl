package hospital.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a token in the hospital queue.
 * Implements Queueable and Comparable to support Priority-based processing.
 */
public class QueueToken implements Queueable, Comparable<QueueToken>, Serializable {
    private static final long serialVersionUID = 1L;

    private String tokenNumber;
    private String patientId;
    private String patientName;
    private int patientAge;
    private String patientGender;
    private String doctorId;
    private String doctorName;
    private String appointmentId;
    private PatientType type;
    private String issueDescription;
    private long timestamp;
    private String status; // WAITING, IN_CONSULTATION, COMPLETED, CANCELLED
    private String createdAtFormatted;

    public QueueToken() {
        this.timestamp = System.currentTimeMillis();
        this.status = "WAITING";
        this.type = PatientType.REGULAR;
        this.createdAtFormatted = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public QueueToken(String tokenNumber, String patientId, String patientName,
                      int patientAge, String patientGender, String doctorId,
                      String doctorName, String appointmentId, PatientType type,
                      String issueDescription) {
        this.tokenNumber = tokenNumber;
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.appointmentId = appointmentId;
        this.type = type != null ? type : PatientType.REGULAR;
        this.issueDescription = issueDescription;
        this.timestamp = System.currentTimeMillis();
        this.status = "WAITING";
        this.createdAtFormatted = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Override
    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
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

    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
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

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public PatientType getType() {
        return type;
    }

    public void setType(PatientType type) {
        this.type = type;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAtFormatted() {
        return createdAtFormatted;
    }

    public void setCreatedAtFormatted(String createdAtFormatted) {
        this.createdAtFormatted = createdAtFormatted;
    }

    @Override
    public boolean isEmergency() {
        return type == PatientType.EMERGENCY;
    }

    @Override
    public int compareTo(QueueToken other) {
        // Emergency priority over Regular
        if (this.isEmergency() && !other.isEmergency()) {
            return -1; // this comes first
        }
        if (!this.isEmergency() && other.isEmergency()) {
            return 1;  // other comes first
        }
        // Both same priority level -> FIFO based on timestamp
        return Long.compare(this.timestamp, other.timestamp);
    }

    @Override
    public String toString() {
        return tokenNumber + " - " + patientName + " (" + type + ")";
    }
}
