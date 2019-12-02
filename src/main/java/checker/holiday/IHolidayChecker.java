package checker.holiday;

import java.time.LocalDate;
import java.util.Collection;

/**
 * A HolidayChecker instance is able to check whether a given date is a holiday.
 */
public interface IHolidayChecker {
    
    /**
     * Checks whether a given date is a {@link Holiday holiday}.
     * @param date - to be checked.
     * @return True if the date is a holiday, false otherwise.
     * @throws HolidayFetchException if an error occurs while fetching possible holidays.
     */
    public boolean isHoliday(LocalDate date) throws HolidayFetchException;
    
    /**
     * Returns a {@link Collection} of all {@link Holiday holidays} associated with a
     * specific implementation of {@link IHolidayChecker}.
     * @return A {@link Collection} of all holidays.
     * @throws HolidayFetchException if an error occurs while fetching possible holidays.
     */
    public Collection<Holiday> getHolidays() throws HolidayFetchException;
    
}
