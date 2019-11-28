package checker.holiday;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Year;
import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;

import io.FileController;
import parser.IHolidayParser;
import parser.JsonHolidayParser;
import parser.ParseException;

public class GermanyHolidayChecker implements IHolidayChecker {

    private final Year year;
    private final GermanState state;
    private Collection<Holiday> holidays;
    private static final String HOLIDAY_FETCH_ADDRESS = "https://feiertage-api.de/api/?jahr=$year$&nur_land=$state$";
    
    public GermanyHolidayChecker(Year year, GermanState state) {
        this.year = year;
        this.state = state;
    }

    @Override
    public boolean isHoliday(LocalDate date) throws HolidayFetchException {
        if (!hasHolidays()) {
            fetchHolidays();
        }
        
        for (Holiday holiday : holidays) {
            if (holiday.equalsDate(date)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<Holiday> getHolidays() throws HolidayFetchException {
        if (!hasHolidays()) {
            fetchHolidays();
        }
        return holidays;
    }

    private void fetchHolidays() throws HolidayFetchException {
        String filledAddress = HOLIDAY_FETCH_ADDRESS
                .replace("$year$", Integer.toString(year.getValue()))
                .replace("$state$", state.name());
        
        try {
            String stringHolidays = FileController.readURLToString(new URL(filledAddress));
            JSONObject jsonHolidays = new JSONObject(stringHolidays);
            IHolidayParser holidayParser = new JsonHolidayParser(jsonHolidays);
            
            holidays = holidayParser.getHolidays();
        } catch (IOException | JSONException | ParseException e) {
            throw new HolidayFetchException(e.getMessage());
        }
        
    }
    
    private boolean hasHolidays() {
        if (holidays == null || holidays.isEmpty()) {
            return false;
        }
        return true;
    }
}
