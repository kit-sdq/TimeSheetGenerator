package parser;

import java.util.Collection;

import checker.holiday.Holiday;

public interface IHolidayParser {
    
    public Collection<Holiday> getHolidays() throws ParseException;
    
}
