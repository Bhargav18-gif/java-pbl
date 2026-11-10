package hospital.exceptions;

/**
 * Thrown when an appointment booking conflicts with an existing appointment
 * for the same doctor at the same date and time slot.
 */
public class DuplicateBookingException extends Exception {
    private static final long serialVersionUID = 1L;

    public DuplicateBookingException(String message) {
        super(message);
    }

    public DuplicateBookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
