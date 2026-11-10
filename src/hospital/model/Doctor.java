package hospital.model;

/**
 * Model class for Doctor, inheriting from Person.
 */
public class Doctor extends Person {
    private static final long serialVersionUID = 1L;

    private String specialization;
    private String roomNumber;
    private String qualification;
    private int experienceYears;
    private String availableDays;
    private String shiftTimings;

    public Doctor() {
        super();
    }

    public Doctor(String id, String name, int age, String gender, String phone,
                  String email, String address, String specialization,
                  String roomNumber, String qualification, int experienceYears,
                  String availableDays, String shiftTimings) {
        super(id, name, age, gender, phone, email, address);
        this.specialization = specialization;
        this.roomNumber = roomNumber;
        this.qualification = qualification;
        this.experienceYears = experienceYears;
        this.availableDays = availableDays;
        this.shiftTimings = shiftTimings;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(String availableDays) {
        this.availableDays = availableDays;
    }

    public String getShiftTimings() {
        return shiftTimings;
    }

    public void setShiftTimings(String shiftTimings) {
        this.shiftTimings = shiftTimings;
    }

    @Override
    public String getRoleTitle() {
        return "Dr. (" + specialization + ")";
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
        return (specialization != null && specialization.toLowerCase().contains(q)) ||
               (roomNumber != null && roomNumber.toLowerCase().contains(q)) ||
               (qualification != null && qualification.toLowerCase().contains(q));
    }
}
