package parser;

import data.FullDocumentation;
import org.json.*;

/**
 * @author Liam Wachter
 */
public class Parser implements IParser {
    
    String name;
    String personnelNumber;
    String institute;
    int workingHours;
    double wage;
    boolean gf;
    boolean ub;    
    
    @Override
    public FullDocumentation parse(String global, String month) throws IllegalArgumentException {
        
        parseGlobal(global);
        parseMonth(month);
        
        return null;
    }

    private void parseGlobal(String global) {
        JSONObject json = new JSONObject(global);
        //TODO do actual parsing
    }

    private void parseMonth(String month) {
        JSONObject json = new JSONObject(month);
        //TODO do actual parsing
    }
}
