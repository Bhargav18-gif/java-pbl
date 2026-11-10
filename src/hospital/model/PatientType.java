package hospital.model;

public enum PatientType {
    REGULAR("Regular"),
    EMERGENCY("Emergency");

    private final String label;

    PatientType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
