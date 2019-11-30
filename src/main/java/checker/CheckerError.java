package checker;

public class CheckerError {

    private final String errorMsg;
    
    public CheckerError(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    
    public String getErrorMessage() {
        return this.errorMsg;
    }
    
}
