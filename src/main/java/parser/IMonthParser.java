package parser;

import java.time.YearMonth;

import data.Entry;
import data.TimeSpan;

public interface IMonthParser {
    
    public YearMonth getYearMonth() throws ParseException;
    
    public Entry[] getEntries() throws ParseException;
    
    public TimeSpan getVacation() throws ParseException;
    
    public TimeSpan getSuccTransfer() throws ParseException;
    
    public TimeSpan getPredTransfer() throws ParseException;

}
