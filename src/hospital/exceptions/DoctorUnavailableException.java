package hospital.exceptions;

/**
 * Thrown when attempting an action with a doctor who is off-duty or unavailable.
 */
public class DoctorUnavailableException extends Exception {
    private static final long serialVersionUID = 1L;

    public DoctorUnavailableException(String message) {
        super(message);
    }

    public DoctorUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
