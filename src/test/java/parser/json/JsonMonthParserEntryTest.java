package parser.json;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import data.Entry;
import data.TimeSpan;
import parser.IMonthParser;
import parser.ParseException;

public class JsonMonthParserEntryTest {

    private static final String JSON_EXAMPLE_EMPTY = "{" +
        "\"year\": 2019," +
        "\"month\": 11," +
        "\"entries\": [" +
            "{}" +
        "]" +
    "}";
    private static final String JSON_EXAMPLE_NECESSARY = "{" +
        "\"year\": 2019," +
        "\"month\": 11," +
        "\"entries\": [" +
            "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\"}" +
        "]" +
    "}";
    private static final String JSON_EXAMPLE_FULL = "{" +
        "\"year\": 2019," +
        "\"month\": 11," +
        "\"entries\": [" +
            "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"00:30\"}" +
        "]" +
    "}";

    @Test(expected = ParseException.class)
    public void testParseEntryEmptyJsonObject() throws ParseException {
        // data
        IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_EMPTY);

        // execute
        parser.getEntries();
    }

    @Test(expected = ParseException.class)
    public void testParseEntryMissingAction() throws ParseException {
        // data
        String json = "{" +
            "\"year\": 2019," +
            "\"month\": 11," +
            "\"entries\": [" +
                "{\"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"00:30\"}" +
            "]" +
        "}";
        IMonthParser parser = new JsonMonthParser(json);

        // execute
        parser.getEntries();
    }

    @Test(expected = ParseException.class)
    public void testParseEntryMissingDay() throws ParseException {
        // data
        String json = "{" +
            "\"year\": 2019," +
            "\"month\": 11," +
            "\"entries\": [" +
                "{\"action\": \"Fragen beantworten\", \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"00:30\"}" +
            "]" +
        "}";
        IMonthParser parser = new JsonMonthParser(json);

        // execute
        parser.getEntries();
    }

    @Test(expected = ParseException.class)
    public void testParseEntryMissingStart() throws ParseException {
        // data
        String json = "{" +
            "\"year\": 2019," +
            "\"month\": 11," +
            "\"entries\": [" +
                "{\"action\": \"Fragen beantworten\", \"day\": 4, \"end\": \"15:11\", \"pause\": \"00:30\"}" +
            "]" +
        "}";
        IMonthParser parser = new JsonMonthParser(json);

        // execute
        parser.getEntries();
    }

    @Test(expected = ParseException.class)
    public void testParseEntryMissingEnd() throws ParseException {
        // data
        String json = "{" +
            "\"year\": 2019," +
            "\"month\": 11," +
            "\"entries\": [" +
                "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"pause\": \"00:30\"}" +
            "]" +
        "}";
        IMonthParser parser = new JsonMonthParser(json);

        // execute
        parser.getEntries();
    }

    @Test(expected = ParseException.class)
    public void testParseEntryAdditionalProperty() throws ParseException {
        // data
        String json = "{" +
            "\"year\": 2019," +
            "\"month\": 11," +
            "\"entries\": [" +
                "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"something\": \"else\"}" +
            "]" +
        "}";
        IMonthParser parser = new JsonMonthParser(json);

        // execute
        parser.getEntries();
    }

    @Test(expected = ParseException.class)
    public void testParseEntryWrongStartFormat() throws ParseException {
        // data
        String json = "{" +
            "\"year\": 2019," +
            "\"month\": 11," +
            "\"entries\": [" +
                "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"kurz nach halb elf\", \"end\": \"15:11\", \"pause\": \"00:30\"}" +
            "]" +
        "}";
        IMonthParser parser = new JsonMonthParser(json);

        // execute
        parser.getEntries();
    }

    @Test(expected = ParseException.class)
    public void testParseEntryWrongEndFormat() throws ParseException {
        // data
        String json = "{" +
            "\"year\": 2019," +
            "\"month\": 11," +
            "\"entries\": [" +
                "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"kurz nach zehn nach drei\", \"pause\": \"00:30\"}" +
            "]" +
        "}";
        IMonthParser parser = new JsonMonthParser(json);

        // execute
        parser.getEntries();
    }

    @Test(expected = ParseException.class)
    public void testParseEntryWrongPauseFormat() throws ParseException {
        // data
        String json = "{" +
            "\"year\": 2019," +
            "\"month\": 11," +
            "\"entries\": [" +
                "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"halbe Stunde\"}" +
            "]" +
        "}";
        IMonthParser parser = new JsonMonthParser(json);

        // execute
        parser.getEntries();
    }

    @Test
    public void testParseEntry() throws ParseException {
        // data
        IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_FULL);

        // execute
        Entry[] entries = parser.getEntries();

        // assert
        assertEquals(1, entries.length);

        assertEquals(new Entry("Fragen beantworten", LocalDate.of(2019, 11, 4), new TimeSpan(11, 31), new TimeSpan(15, 11), new TimeSpan(0, 30)), entries[0]);
    }

    @Test
    public void testParseEntryDefault() throws ParseException {
        // data
        IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_NECESSARY);

        // execute
        Entry[] entries = parser.getEntries();

        // assert
        assertEquals(1, entries.length);

        assertEquals(new Entry("Fragen beantworten", LocalDate.of(2019, 11, 4), new TimeSpan(11, 31), new TimeSpan(15, 11), new TimeSpan(0, 0)), entries[0]);
    }

}
