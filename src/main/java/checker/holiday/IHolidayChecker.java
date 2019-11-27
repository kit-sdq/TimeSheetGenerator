package checker.holiday;

import java.time.LocalDate;
import java.util.Collection;

public interface IHolidayChecker {
    
    public boolean isHoliday(LocalDate date) throws HolidayFetchException;
    
    public Collection<Holiday> getHolidays() throws HolidayFetchException;
    
}
