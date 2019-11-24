package parser;

public class ParseException extends Exception {
    
    private static final long serialVersionUID = -644519857973827281L;

    public ParseException(String error) {
        super(error);
    }
    
}
