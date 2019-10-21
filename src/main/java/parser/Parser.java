package parser;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

import data.Entry;
import data.FullDocumentation;
import data.TimeSpan;

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
    private Entry[] entries;
    

    @Override
    public FullDocumentation parse(String globalConfig, String monthConfig) throws IllegalArgumentException, ParseException {
        
        parseGlobal(globalConfig);
        parseMonth(monthConfig);
        
        FullDocumentation doc = new FullDocumentation(name, institute, personnelNumber, gf, entries);
        
        //TODO übertrag?? we don't need to check it, but it would be good to have the option to create documents with values in these fields
        doc.setWage(wage);
        doc.setMonth(month);
        doc.setYear(year);
        
        return doc;
    }
    

    private void parseGlobal(String config) throws ParseException {
        JSONObject json = new JSONObject(config);
        
        // check if the global config file contains all required elements
        String[] requiredElements = Stream.of(Global_Required.values()).map(Global_Required::toString).toArray(String[]::new);
        checkRequiredElements("global", json, requiredElements);
        
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
        checkRequiredElements("month", json, requiredElements);
        
        // read the values from the config file
        this.month = json.getInt("month");
        this.year = json.getInt("year");
        
        // extract/parse entries and store them in a list 
        JSONArray entriesJSONArray = json.getJSONArray("entries");
        List<Entry> entriesList = new LinkedList<Entry>();
        
        for (int i = 0; i < entriesJSONArray.length(); i++) {
            entriesList.add(parseEntry(entriesJSONArray.getJSONObject(i), i + 1));
        }

        entries = entriesList.toArray(new Entry[entriesJSONArray.length()]);
    }
    
    private Entry parseEntry(JSONObject json, int entryNumber) throws ParseException {
        
        if (    !json.has("date") ||
                !json.has("action") ||
                !json.has("start") ||
                !json.has("end")) {
            throw new ParseException("Error in line definition: reqired element is missing in entry number " + entryNumber);
        }
        
        String dateString = json.getString("date");
        String action = json.getString("action");
        String startString = json.getString("start");
        String endString = json.getString("end");
        String pauseString;
        
        // pause is optional
        if (json.has("pause")) {
            pauseString = json.getString("pause");
        } else {
            pauseString = "00:00";
        }
        
        TimeSpan start, end, pause;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date date;
        try {
            date = sdf.parse(dateString);
        } catch (java.text.ParseException e) {
            throw new ParseException("Error in line definition: Date format error im entry number " + entryNumber + ". Usage: dd.MM.YYYY");
        }
        
        //LocalDate date2 = LocalDate.of(arg0, arg1, arg2)

        // TODO the solution above does not work with short year, like 01.11.19. can be tested with prints below
        
        //System.out.println(dateString);
        //System.out.println(date);
        //System.out.println(sdf.format(date));
        
        start = parseTimeSpan(startString, entryNumber);
        end = parseTimeSpan(endString, entryNumber);
        pause = parseTimeSpan(pauseString, entryNumber);
        
        return new Entry(action, date, start, end, pause);
    }
    
    private TimeSpan parseTimeSpan (String s, int entryNumber) throws ParseException {
        
        String[] split = s.split(":");
        if (split.length != 2) throw new ParseException("Error in line definition: Time format error in entry number " + entryNumber + ". Usage: (X)X:XX");
        
        int hour, minute;
        
        try {
            hour = Integer.parseInt(split[0]);
            minute = Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            throw new ParseException("Error in line definition: Time format error, not a Number in entry number " + entryNumber);
        }
        
        // Info: validity check of hour and minutes is done by the TimeSpan class
        return new TimeSpan(hour, minute);
    }
    
    private void checkRequiredElements(String configName, JSONObject json, String[] requiredElements) throws ParseException {
        
        StringJoiner missingEntries = new StringJoiner(",");
        for(String element: requiredElements) {
            
            if (!json.has(element)) {
                missingEntries.add(element);
            }
        }
        if (missingEntries.length() > 0) {
            throw new ParseException("Error in " + configName + " config: missing required element(s): " + missingEntries.toString());
        }
    }
}
