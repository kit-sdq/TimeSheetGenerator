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

/**
 * A GermanyHolidayChecker is a holiday checker implementing {@link IHolidayChecker} that
 * is able to check for legal {@link Holiday holidays} of all different {@link GermanState GermanStates}.
 */
public class GermanyHolidayChecker implements IHolidayChecker {

    private final Year year;
    private final GermanState state;
    private Collection<Holiday> holidays;
    private static final String HOLIDAY_FETCH_ADDRESS = "https://feiertage-api.de/api/?jahr=$year$&nur_land=$state$";
    
    /**
     * Constructs a new {@link GermanyHolidayChecker} instance.
     * @param year - in which the {@link Holiday holidays} take place.
     * @param state - of Germany to check for possible {@link Holiday holidays}.
     */
    public GermanyHolidayChecker(int year, GermanState state) {
        this.year = Year.of(year);
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

    /**
     * Fetches the occurring holidays from a specific source.
     * @throws HolidayFetchException if an error occurs fetching the holidays.
     */
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
    
    /**
     * Checks whether the {@link Holiday holidays} are already fetched.
     * @return {@code True} if the holidays are already fetched, {@code False} otherwise.
     */
    private boolean hasHolidays() {
        return holidays != null && !holidays.isEmpty();
    }
}
