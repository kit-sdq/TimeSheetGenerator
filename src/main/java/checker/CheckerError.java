package checker;

import data.TimeSheet;

/**
 * A CheckerError is used when an expected or user induced error occurs
 * while checking a {@link TimeSheet} with an {@link IChecker} instance.
 * Therefore it is not insoluble and should not be treated like an {@link Exception},
 * especially the {@link CheckerException}.
 */
public class CheckerError {

    private final CheckerErrorMessageProvider errorMessageProvider;
    private final Object[] args;
    
    /**
     * Constructs a new {@link CheckerError} instance.
     * @param errorMessageProvider - Provider for the error message.
     * @param args - Arguments referenced by the format specifiers in the errorMsg format string. 
     */
    public CheckerError(CheckerErrorMessageProvider errorMessageProvider, Object... args) {
        this.errorMessageProvider = errorMessageProvider;
        this.args = args;
    }
    
    /**
     * Gets the error message of an {@link CheckerError}.
     * @return The error message.
     */
    public String getErrorMessage() {
        return errorMessageProvider.getErrorMessage(args);
    }
    
    /**
     * Gets the {@link CheckerErrorMessageProvider} used to create this {@link CheckerError}.
     * @return The checker error message provider.
     */
    public CheckerErrorMessageProvider getErrorMessageProvider() {
        return errorMessageProvider;
    }
    
    /**
     * Gets the arguments used to create this {@link CheckerError}.
     * @return The arguments.
     */
    public Object[] getArgs() {
        return args;
    }
    
    /**
     * A CheckerErrorMessageProvider provides a message for given arguments that can be used in a CheckerError.
     */
    public interface CheckerErrorMessageProvider {
        
        /**
         * Gets the error message.
         * 
         * @param args Arguments that will be inserted in the error message
         * @return Error message including the formatted arguments
         */
        String getErrorMessage(Object... args);
        
    }
    
}
