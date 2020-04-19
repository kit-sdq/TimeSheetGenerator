package parser;

import java.time.YearMonth;

import data.Entry;
import data.TimeSpan;

/**
 * A month parser provides the functionality of parsing month-related
 * {@link TimeSheet} information from given data.
 */
public interface IMonthParser {
    
    /**
     * Returns a {@link YearMonth} parsed from data.
     * @return A YearMonth.
     * @throws ParseException if an error occurs while parsing.
     */
    public YearMonth getYearMonth() throws ParseException;
    
    /**
     * Returns an array of {@link Entry entries} parsed from data.
     * @return An array of entries.
     * @throws ParseException if an error occurs while parsing.
     */
    public Entry[] getEntries() throws ParseException;
    
    /**
     * Returns {@link TimeSpan} representing the transfered time
     * from the successor month parsed from data.
     * @return A transfered time {@link TimeSpan}.
     * @throws ParseException if an error occurs while parsing.
     */
    public TimeSpan getSuccTransfer() throws ParseException;
    
    /**
     * Returns {@link TimeSpan} representing the transfered time
     * from the predecessor month parsed from data.
     * @return A transfered time {@link TimeSpan}.
     * @throws ParseException if an error occurs while parsing.
     */
    public TimeSpan getPredTransfer() throws ParseException;

}
