package checker;

/**
 * A CheckerException is thrown by a {@link IChecker} instance when an unexpected
 * or insoluble error occurs. 
 */
public class CheckerException extends Exception {

    /**
     * Auto-generated serial version UID
     */
    private static final long serialVersionUID = 4362647380313599066L;

    /**
     * Constructs a new {@link CheckerException}.
     * @param error - message of the error that occurred.
     */
    public CheckerException(String message) {
        super(message);
    }
}
