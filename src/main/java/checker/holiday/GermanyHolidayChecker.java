package checker.holiday;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Year;
import java.util.Collection;

import javax.net.ssl.SSLHandshakeException;

import io.FileController;
import parser.IHolidayParser;
import parser.ParseException;
import parser.json.JsonHolidayParser;

/**
 * A GermanyHolidayChecker is a holiday checker implementing {@link IHolidayChecker} that
 * is able to check for legal {@link Holiday holidays} of all different {@link GermanState GermanStates}.
 */
public class GermanyHolidayChecker implements IHolidayChecker {

    private final Year year;
    private final GermanState state;
    private Collection<Holiday> holidays;
    private static final String HOLIDAY_FETCH_ADDRESS_HTTPS = "https://feiertage-api.de/api/?jahr=$year$&nur_land=$state$";
    private static final String HOLIDAY_FETCH_ADDRESS_HTTP = "http://feiertage-api.de/api/?jahr=$year$&nur_land=$state$";
    
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
        String stringHolidays;
        try {
            stringHolidays = fetchHolidaysJSONString();
        } catch (IOException e) {
            throw new HolidayFetchException(e.getMessage());
        }
        
        try {
            IHolidayParser holidayParser = new JsonHolidayParser(stringHolidays);
            
            holidays = holidayParser.getHolidays();
        } catch (ParseException e) {
            throw new HolidayFetchException(e.getMessage());
        }
        
    }
    
    /**
     * Reads holidays formatted as JSON string and retries with fallback http address if https is not available.
     * @return Holidays formatted as JSON string
     * @throws IOException if an I/O error occurs.
     */
    private String fetchHolidaysJSONString() throws IOException {
        try {
            return readHolidayJSONStringFromAddress(HOLIDAY_FETCH_ADDRESS_HTTPS);
        } catch (SSLHandshakeException e) {
            return readHolidayJSONStringFromAddress(HOLIDAY_FETCH_ADDRESS_HTTP);
        }
    }
    
    /**
     * Reads holidays formatted as JSON string from address given.
     * @param address - to fetch holidays from
     * @return Holidays formatted as JSON string
     * @throws SSLHandshakeException if an SSL handshake error occurs.
     * @throws IOException if an I/O error occurs.
     */
    private String readHolidayJSONStringFromAddress(String address) throws SSLHandshakeException, IOException {
        String filledAddress = address
                .replace("$year$", Integer.toString(year.getValue()))
                .replace("$state$", state.name());
        
        return FileController.readURLToString(new URL(filledAddress));
    }
    
    /**
     * Checks whether the {@link Holiday holidays} are already fetched.
     * @return {@code True} if the holidays are already fetched, {@code False} otherwise.
     */
    private boolean hasHolidays() {
        return holidays != null && !holidays.isEmpty();
    }
}
