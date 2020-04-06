package checker;

import data.TimeSheet;

/**
 * A CheckerError is used when an expected or user induced error occurs
 * while checking a {@link TimeSheet} with an {@link IChecker} instance.
 * Therefore it is not insoluble and should not be treated like an {@link Exception},
 * especially the {@link CheckerException}.
 */
public class CheckerError {

    private final CheckerErrorMessage errorMsg;
    private final Object[] args;
    
    /**
     * Constructs a new {@link CheckerError} instance.
     * @param errorMsg - associated with the occurred error.
     * @param args - Arguments referenced by the format specifiers in the errorMsg format string. 
     */
    public CheckerError(CheckerErrorMessage errorMsg, Object... args) {
        this.errorMsg = errorMsg;
        this.args = args;
    }
    
    /**
     * Gets the error message of an {@link CheckerError}.
     * @return The error message.
     */
    public String getErrorMessage() {
        return errorMsg.getErrorMessage(args);
    }
    
    /**
     * Gets the {@link CheckerErrorMessage} used to create this {@link CheckerError}.
     * @return The checker error message.
     */
    public CheckerErrorMessage getCheckerErrorMessage() {
        return errorMsg;
    }
    
    /**
     * Gets the arguments used to create this {@link CheckerError}.
     * @return The arguments.
     */
    public Object[] getArgs() {
        return args;
    }
    
    /**
     * A CheckerErrorMessage represents a message that can be used in a CheckerError.
     */
    public interface CheckerErrorMessage {
        
        /**
         * Gets the error message.
         * 
         * @param args Arguments that will be inserted in the error message
         * @return Error message including the formatted arguments
         */
        String getErrorMessage(Object... args);
        
    }
    
}
