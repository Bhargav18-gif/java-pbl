package hospital.exceptions;

/**
 * Thrown when trying to call or process patients when no tokens are waiting in queue.
 */
public class QueueEmptyException extends Exception {
    private static final long serialVersionUID = 1L;

    public QueueEmptyException(String message) {
        super(message);
    }
}
