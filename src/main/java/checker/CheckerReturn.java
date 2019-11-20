package checker;

/**
 * Represents the possible return values of the checker class.
 */
public enum CheckerReturn {
    VALID("No error occurred."),
    
    TIME_EXCEEDANCE("Maximum legal working time exceeded."),
    TIME_OUTOFBOUNDS("Working time is out of bounds."),
    TIME_SUNDAY("Sunday is not a valid working day."),
    TIME_HOLIDAY("Official holiday is not a valid working day."),
    TIME_PAUSE("Maximum working time without pause exceeded."),
    
    ROWNUM_EXCEEDENCE("Exceeded the maximum number of rows for the document."),
    NAME_MISSING("Name of the departement is missing.");
    
    private final String errorMsg;
    
    private CheckerReturn(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    
    public String getErrorMsg() {
        return this.errorMsg;
    }
}
