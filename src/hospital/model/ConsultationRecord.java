package hospital.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Record representing a completed or in-progress patient consultation.
 */
public class ConsultationRecord implements Searchable, Serializable {
    private static final long serialVersionUID = 1L;

    private String recordId;
    private String tokenNumber;
    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private String timestamp;
    private String symptoms;
    private String diagnosis;
    private String prescription;
    private String vitalSigns;
    private String doctorNotes;
    private String status; // IN_PROGRESS, COMPLETED

    public ConsultationRecord() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.status = "COMPLETED";
    }

    public ConsultationRecord(String recordId, String tokenNumber, String patientId,
                              String patientName, String doctorId, String doctorName,
                              String symptoms, String diagnosis, String prescription,
                              String vitalSigns, String doctorNotes) {
        this.recordId = recordId;
        this.tokenNumber = tokenNumber;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.vitalSigns = vitalSigns;
        this.doctorNotes = doctorNotes;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.status = "COMPLETED";
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(String vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean matches(String query) {
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        String q = query.toLowerCase().trim();
        return (recordId != null && recordId.toLowerCase().contains(q)) ||
               (tokenNumber != null && tokenNumber.toLowerCase().contains(q)) ||
               (patientName != null && patientName.toLowerCase().contains(q)) ||
               (doctorName != null && doctorName.toLowerCase().contains(q)) ||
               (diagnosis != null && diagnosis.toLowerCase().contains(q));
    }
}
