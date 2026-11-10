package hospital.model;

/**
 * Interface defining queue priority and identity behavior.
 */
public interface Queueable {
    String getTokenNumber();
    boolean isEmergency();
    long getTimestamp();
}
