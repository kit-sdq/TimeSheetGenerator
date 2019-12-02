package parser;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.Entry;
import data.TimeSpan;

/**
 * A JsonMonthParser provides the functionality to parse the
 * elements specified by {@link IMonthParser} from an {@link JSONObject}.
 */
public class JsonMonthParser implements IMonthParser {

    private final JSONObject json;
    
    // Caching attributes to avoid redundant json parsing.
    private YearMonth yearMonth;
    private Entry[] entries;
    
    /**
     * Constructs a new {@link JsonMonthParser} instance.
     * @param json - to parse the data from.
     */
    public JsonMonthParser(JSONObject json) {
        this.json = json;
    }

    @Override
    public YearMonth getYearMonth() throws ParseException {
        // If the year and month were already parsed, they are simply returned
        if (hasYearMonth()) {
            return this.yearMonth;
        }
        
        // Otherwise get them out of the JSONObject
        YearMonth yearMonth;
        try {
            int month = json.getInt("month");
            int year = json.getInt("year");
            yearMonth = YearMonth.of(year, month);
        } catch (DateTimeException | JSONException e) {
            throw new ParseException(e.getMessage());
        }
        
        this.yearMonth = yearMonth;
        return yearMonth;
    }

    @Override
    public Entry[] getEntries() throws ParseException {
        // If the entries were already parsed, they are simply returned
        if (hasEntries()) {
            return this.entries;
        }
        
        //Otherwise 
        Entry[] entries;
        try {
            JSONArray entriesJSONArray = json.getJSONArray("entries");
            entries = new Entry[entriesJSONArray.length()];
            
            for (int i = 0; i < entries.length; i++) {
                entries[i] = parseEntry(entriesJSONArray.getJSONObject(i));
            }
        } catch (DateTimeException | JSONException e) {
            throw new ParseException(e.getMessage());
        }
        
        this.entries = entries;
        return entries;
    }

    @Override
    public TimeSpan getVacation() throws ParseException {
        String vacation = json.optString("vacation", "0:00");
        return TimeSpan.parse(vacation);
    }

    @Override
    public TimeSpan getSuccTransfer() throws ParseException {
        String vacation = json.optString("succ_transfer", "0:00");
        return TimeSpan.parse(vacation);
    }

    @Override
    public TimeSpan getPredTransfer() throws ParseException {
        String vacation = json.optString("pred_transfer", "0:00");
        return TimeSpan.parse(vacation);
    }
    
    /**
     * Parses an {@link Entry} from an {@link JSONObject}.
     * @param json - to parse {@link Entry} from
     * @return The entry parsed from the {@link JSONObject}.
     * @throws JSONException if an error occurs while parsing the {@link JSONObject}.
     * @throws DateTimeException if an error occurs while parsing the {@link LocalDate}.
     * @throws ParseException if an error occurs while fetching the {@link YearMonth}.
     */
    private Entry parseEntry(JSONObject json) throws JSONException, DateTimeException, ParseException {        
        // Json "getters" 
        String action = json.getString("action");
        int day = json.getInt("day");
        String startString = json.getString("start");
        String endString = json.getString("end");
        String pauseString = json.optString("pause", "00:00");
        
        // TimeSpan parsing
        TimeSpan start = TimeSpan.parse(startString);
        TimeSpan end = TimeSpan.parse(endString);
        TimeSpan pause = TimeSpan.parse(pauseString);
        
        // LocalDate construction
        YearMonth yearMonth = getYearMonth();
        LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), day);
        
        return new Entry(action, date, start, end, pause);
    }
    
    /**
     * Returns whether the {@link YearMonth} caching attribute contains something or not. 
     * @return False if the {@link YearMonth} attribute equals {@code NULL}.
     */
    private boolean hasYearMonth() {
        return !(yearMonth == null);
    }
    
    /**
     * Returns whether the {@link Entry entries} caching attribute contains something or not. 
     * @return False if the entries array equals {@code NULL}.
     */
    private boolean hasEntries() {
        return !(entries == null);
    }
}
