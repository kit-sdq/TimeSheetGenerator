package parser;

import data.FullDocumentation;
import org.json.*;

/**
 * @author Liam Wachter
 */
public class Parser implements IParser {

    private String name;
    private String personnelNumber;
    private String institute;
    private int workingHours;
    private double wage;
    private boolean gf;
    private boolean ub;

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
