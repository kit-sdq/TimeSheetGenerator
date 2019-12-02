package parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;

import checker.holiday.Holiday;

/**
 * A JsonHolidayParser provides the functionality to parse the
 * elements specified by {@link IHolidayParser} from an {@link JSONObject}.
 */
public class JsonHolidayParser implements IHolidayParser {
    
    private final JSONObject json;
    private final static String DATE_PATTERN = "yyyy-MM-dd";
    
    /**
     * Constructs a new {@link JsonHolidayParser} instance.
     * @param json - to parse the data from.
     */
    public JsonHolidayParser(JSONObject json) {
        this.json = json;
    }

    @Override
    public Collection<Holiday> getHolidays() throws ParseException {
        Collection<Holiday> holidays = new ArrayList<Holiday>();
        
        DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder().appendPattern(DATE_PATTERN).toFormatter();
        for (String description : json.keySet()) {
            LocalDate date;
            try {
                JSONObject holidayItem = json.getJSONObject(description);
                String dateString = holidayItem.getString("datum");
                date = LocalDate.parse(dateString, dateFormatter);
            } catch (JSONException | DateTimeParseException e) {
                throw new ParseException(e.getMessage());
            }
            holidays.add(new Holiday(date, description));
        }
        
        return holidays;
    }

}
