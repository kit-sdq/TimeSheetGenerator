package parser.json;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import checker.holiday.Holiday;
import parser.IHolidayParser;
import parser.ParseException;

public class JsonHolidayParserTest {
    
    private static final String JSON_EMPTY = "{}";
    private static final String JSON_EXAMPLE = "{" +
        "\"1. Weihnachtstag\": {" +
            "\"datum\": \"2019-12-25\"," +
            "\"hinweis\": \"Weihnachten\"" +
        "}, \"2. Weihnachtstag\": {" +
            "\"datum\": \"2019-12-26\"" +
        "}" +
    "}";

    @Test
    public void testCreate() {
        IHolidayParser parser = new JsonHolidayParser(JSON_EXAMPLE);

        assertNotNull(parser);
    }

    @Test
    public void testGetHolidaysEmptyJson() throws ParseException {
        // data
        IHolidayParser parser = new JsonHolidayParser(JSON_EMPTY);

        // execute
        Collection<Holiday> holidays = parser.getHolidays();

        // assert
        assertEquals(0, holidays.size());
    }

    @Test(expected = ParseException.class)
    public void testGetHolidaysMissingDate() throws ParseException {
        // data
        String json = "{" +
            "\"1. Weihnachtstag\": {" +
                "\"hinweis\": \"Weihnachten\"" +
            "}" +
        "}";
        IHolidayParser parser = new JsonHolidayParser(json);

        // execute
        parser.getHolidays();
    }

    @Test(expected = ParseException.class)
    public void testGetHolidaysAdditionalProperty() throws ParseException {
        // data
        String json = "{" +
            "\"2. Weihnachtstag\": {" +
                "\"datum\": \"2019-12-26\"," +
                "\"something\": \"else\"" +
            "}" +
        "}";
        IHolidayParser parser = new JsonHolidayParser(json);

        // execute
        parser.getHolidays();
    }

    @Test(expected = ParseException.class)
    public void testGetHolidaysWrongDateFormat() throws ParseException {
        // data
        String json = "{" +
            "\"2. Weihnachtstag\": {" +
                "\"datum\": \"26.12.2019\"" +
            "}" +
        "}";
        IHolidayParser parser = new JsonHolidayParser(json);

        // execute
        parser.getHolidays();
    }

    @Test
    public void testGetHolidays() throws ParseException {
        // data
        Collection<Holiday> expectedHolidays = new ArrayList<Holiday>();
        expectedHolidays.add(new Holiday(LocalDate.of(2019, 12, 25), "1. Weihnachtstag"));
        expectedHolidays.add(new Holiday(LocalDate.of(2019, 12, 26), "2. Weihnachtstag"));

        IHolidayParser parser = new JsonHolidayParser(JSON_EXAMPLE);

        // execute
        Collection<Holiday> holidays = parser.getHolidays();

        // assert
        assertEquals(expectedHolidays.size(), holidays.size());

        for (Holiday expectedHoliday : expectedHolidays) {
            assert(holidays.contains(expectedHoliday));
        }
    }

}
