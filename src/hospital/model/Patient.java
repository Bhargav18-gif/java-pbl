package hospital.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Model class for Patient, inheriting from Person.
 */
public class Patient extends Person {
    private static final long serialVersionUID = 1L;

    private String bloodGroup;
    private String medicalHistory;
    private String emergencyContact;
    private String registrationDate;

    public Patient() {
        super();
        this.registrationDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public Patient(String id, String name, int age, String gender, String phone,
                   String email, String address, String bloodGroup,
                   String medicalHistory, String emergencyContact) {
        super(id, name, age, gender, phone, email, address);
        this.bloodGroup = bloodGroup;
        this.medicalHistory = medicalHistory;
        this.emergencyContact = emergencyContact;
        this.registrationDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String getRoleTitle() {
        return "Patient";
    }

    @Override
    public boolean matches(String query) {
        if (super.matches(query)) {
            return true;
        }
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        String q = query.toLowerCase().trim();
        return (bloodGroup != null && bloodGroup.toLowerCase().contains(q)) ||
               (medicalHistory != null && medicalHistory.toLowerCase().contains(q));
    }
}
