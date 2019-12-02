package checker;

import data.TimeSheet;

/**
 * A CheckerError is used when an expected or user induced error occurs
 * while checking a {@link TimeSheet} with an {@link IChecker} instance.
 * Therefore it is not insoluble and should not be treated like an {@link Exception},
 * especially the {@link CheckerException}.
 */
public class CheckerError {

    private final String errorMsg;
    
    /**
     * Constructs a new {@link CheckerError} instance.
     * @param errorMsg - associated with the occurred error.
     */
    public CheckerError(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    
    /**
     * Gets the error message of an {@link CheckerError}.
     * @return The error message.
     */
    public String getErrorMessage() {
        return this.errorMsg;
    }
    
}
