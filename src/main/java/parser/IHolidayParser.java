package parser;

import java.util.Collection;

import checker.holiday.Holiday;

/**
 * A holiday parser provides the functionality of parsing {@link Holiday holidays} from given data.
 */
public interface IHolidayParser {
    
    /**
     * Returns a {@link Collection} of {@link Holiday holidays} parsed from data.
     * @return A collection of holidays.
     * @throws ParseException if an error occurs while parsing.
     */
    public Collection<Holiday> getHolidays() throws ParseException;
    
}
