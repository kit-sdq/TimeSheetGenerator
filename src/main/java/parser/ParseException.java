package parser;

/**
 * Exception thrown by parser classes to signal that an error occurred while parsing.
 */
public class ParseException extends Exception {
    
    private static final long serialVersionUID = -644519857973827281L;

    /**
     * Constructs a new {@link ParseException}.
     * @param error - message of the error that occurred.
     */
    public ParseException(String error) {
        super(error);
    }
    
}
