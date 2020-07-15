package parser;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.Test;

import data.Employee;
import data.Entry;
import data.Profession;
import data.TimeSheet;
import data.TimeSpan;
import data.WorkingArea;

public class ParserJsonTest {

    private static final String JSON_EMPTY = "{}";
    private static final String JSON_GLOBAL_EXAMPLE = "{" +
        "\"name\": \"Max Mustermann\"," +
        "\"staffId\": 1234567," +
        "\"department\": \"Fakultät für Informatik\"," +
        "\"workingTime\": \"40:00\"," +
        "\"wage\": 10.31," +
        "\"workingArea\": \"ub\"" +
    "}";
    private static final String JSON_MONTH_EXAMPLE = "{" +
        "\"year\": 2019," +
        "\"month\": 11," +
        "\"pred_transfer\": \"2:00\"," +
        "\"succ_transfer\": \"1:00\"," +
        "\"entries\": [" +
            "{\"action\": \"Korrektur\", \"day\": 2, \"start\": \"10:00\", \"end\": \"11:00\"}," +
            "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"00:30\"}," +
            "{\"action\": \"Urlaub in Italien\", \"day\": 11, \"start\": \"09:00\", \"end\": \"12:00\", \"vacation\": true}" +
        "]" +
    "}";

    @Test(expected = ParseException.class)
    public void testParseTimeSheetJsonBothJsonEmpty() throws ParseException {
        // execute
        Parser.parseTimeSheetJson(JSON_EMPTY, JSON_EMPTY);
    }

    @Test(expected = ParseException.class)
    public void testParseTimeSheetJsonGlobalJsonEmpty() throws ParseException {
        // execute
        Parser.parseTimeSheetJson(JSON_EMPTY, JSON_MONTH_EXAMPLE);
    }

    @Test(expected = ParseException.class)
    public void testParseTimeSheetJsonMonthJsonEmpty() throws ParseException {
        // execute
        Parser.parseTimeSheetJson(JSON_GLOBAL_EXAMPLE, JSON_EMPTY);
    }

    @Test
    public void testParseTimeSheetJson() throws ParseException {
        // data
        Employee expectedEmployee = new Employee("Max Mustermann", 1234567);
        Profession expectedProfession = new Profession("Fakultät für Informatik", WorkingArea.UB, new TimeSpan(40, 0), 10.31);
        Entry[] expectedEntries = new Entry[] {
            new Entry("Korrektur", LocalDate.of(2019, 11, 2), new TimeSpan(10, 0), new TimeSpan(11, 0), new TimeSpan(0, 0), false),
            new Entry("Fragen beantworten", LocalDate.of(2019, 11, 4), new TimeSpan(11, 31), new TimeSpan(15, 11), new TimeSpan(0, 30), false),
            new Entry("Urlaub in Italien", LocalDate.of(2019, 11, 11), new TimeSpan(9, 0), new TimeSpan(12, 0), new TimeSpan(0, 0), true)
        };
        TimeSheet expectedTimeSheet = new TimeSheet(
            expectedEmployee,
            expectedProfession,
            YearMonth.of(2019, 11),
            expectedEntries,
            new TimeSpan(1, 0),
            new TimeSpan(2, 0)
        );
        
        // execute
        TimeSheet timeSheet = Parser.parseTimeSheetJson(JSON_GLOBAL_EXAMPLE, JSON_MONTH_EXAMPLE);

        // assert
        assertEquals(expectedTimeSheet, timeSheet);
    }

}
