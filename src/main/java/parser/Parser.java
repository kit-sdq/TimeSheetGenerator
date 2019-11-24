package parser;

import java.time.YearMonth;
import data.Employee;
import data.Entry;
import data.TimeSheet;
import data.Profession;
import data.TimeSpan;
import org.json.*;

public class Parser {

    private Parser() {}
    
    public static TimeSheet parseTimeSheet(JSONObject globalJson, JSONObject monthJson) throws ParseException {
        IGlobalParser globalParser = new JsonGlobalParser(globalJson);
        
        Employee employee = globalParser.getEmployee();
        Profession profession = globalParser.getProfession();
        
        IMonthParser monthParser = new JsonMonthParser(monthJson);
        
        YearMonth yearMonth = monthParser.getYearMonth();
        Entry[] entries = monthParser.getEntries();
        TimeSpan vacation = monthParser.getVacation();
        TimeSpan succTransfer = monthParser.getSuccTransfer();
        TimeSpan predTransfer = monthParser.getPredTransfer();
        
        return new TimeSheet(employee, profession, yearMonth, entries, vacation, succTransfer, predTransfer);
    }
    
}
