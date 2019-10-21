package parser;

import java.util.StringJoiner;
import java.util.stream.Stream;

import data.Entry;
import data.FullDocumentation;

import org.json.*;

/**
 * @author Liam Wachter
 */
public class Parser implements IParser {

    private enum Global_Required {
        NAME, PERSONNELNUMBER, INSTITUTE, WORKINGHOURS, WAGE, GF, UB;
        
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    
    private enum Month_Required {
        MONTH, YEAR, ENTRIES;
        
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    
    private String name;
    private int personnelNumber;
    private String institute;
    private int workingHours;
    private double wage;
    private boolean gf;
    private boolean ub;
    
    private int month;
    private int year;
    

    @Override
    public FullDocumentation parse(String globalConfig, String monthConfig) throws IllegalArgumentException, ParseException {
        
        parseGlobal(globalConfig);
        parseMonth(monthConfig);
        
        FullDocumentation doc = new FullDocumentation(name, institute, personnelNumber, gf, null);
        
        doc.setWage(wage);
        doc.setMonth(month);
        doc.setYear(year);
        
        return doc;
    }
    

    private void parseGlobal(String config) throws ParseException {
        JSONObject json = new JSONObject(config);
        
        // check if the global config file contains all required elements
        String[] requiredElements = Stream.of(Global_Required.values()).map(Global_Required::toString).toArray(String[]::new);
        checkRequiredElements(json, requiredElements);
        
        // read the values from the config file
        this.name = json.getString("name");
        this.personnelNumber = json.getInt("personnelnumber");
        this.institute = json.getString("institute");
        this.workingHours = json.getInt("workinghours");
        this.wage = json.getDouble("wage");
        this.gf = json.getBoolean("gf");
        this.ub = json.getBoolean("ub");
        
        if(gf == ub) {
            throw new ParseException("Error in global config: either ub or gf has to be checked");
        }
    }

    private void parseMonth(String config) throws ParseException {
        JSONObject json = new JSONObject(config);
        
        // check if month config file contains all required elements
        String[] requiredElements = Stream.of(Month_Required.values()).map(Month_Required::toString).toArray(String[]::new);
        checkRequiredElements(json, requiredElements);
        
        // read the values from the config file
        this.month = json.getInt("month");
        this.year = json.getInt("year");
        
        //TODO parse entries
    }
    
    private void checkRequiredElements(JSONObject json, String[] requiredElements) throws ParseException {
        
        StringJoiner missingEntries = new StringJoiner(",");
        for(String element: requiredElements) {
            
            if (!json.has(element)) {
                missingEntries.add(element);
            }
        }
        if (missingEntries.length() > 0) {
            throw new ParseException("Error in global config: missing required element(s): " + missingEntries.toString());
        }
    }
}
