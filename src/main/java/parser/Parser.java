package parser;

import java.time.YearMonth;
import data.Employee;
import data.Entry;
import data.TimeSheet;
import data.Profession;
import data.TimeSpan;
import parser.json.JsonGlobalParser;
import parser.json.JsonMonthParser;

/**
 * A Parser provides the functionality to construct a {@link TimeSheet} with the data
 * coming from different types of files getting parsed by {@link IGlobalParser} and 
 * {@link IMonthParser} instances.
 */
public class Parser {

    private Parser() {}
    
    /**
     * Returns a new {@link TimeSheet} constructed out of data coming from two json strings.
     * @param globalJson - json to get global data from.
     * @param monthJson - json to get month data from.
     * @return A new {@link TimeSheet} instances.
     * @throws ParseException if an error occurs while parsing the json strings.
     */
    public static TimeSheet parseTimeSheetJson(String globalJson, String monthJson) throws ParseException {
        IGlobalParser globalParser = new JsonGlobalParser(globalJson);
        
        Employee employee = globalParser.getEmployee();
        Profession profession = globalParser.getProfession();
        
        IMonthParser monthParser = new JsonMonthParser(monthJson);
        
        YearMonth yearMonth = monthParser.getYearMonth();
        Entry[] entries = monthParser.getEntries();
        TimeSpan succTransfer = monthParser.getSuccTransfer();
        TimeSpan predTransfer = monthParser.getPredTransfer();
        
        return new TimeSheet(employee, profession, yearMonth, entries, succTransfer, predTransfer);
    }
    
}
