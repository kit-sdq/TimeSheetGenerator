package parser;

/**
 * @author Dennis Zimmermann
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ParseException extends Exception{

    
    public ParseException(String error) {

        super(error);
    }    
}
