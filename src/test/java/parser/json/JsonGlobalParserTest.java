package parser.json;

import static org.junit.Assert.*;

import org.junit.Test;

import data.Employee;
import data.Profession;
import data.TimeSpan;
import data.WorkingArea;
import parser.IGlobalParser;
import parser.ParseException;

public class JsonGlobalParserTest {

    private static final String JSON_EMPTY = "{}";
    private static final String JSON_EXAMPLE = "{" +
        "\"name\": \"Max Mustermann\"," +
        "\"staffId\": 1234567," +
        "\"department\": \"Fakultät für Informatik\"," +
        "\"workingTime\": \"40:00\"," +
        "\"wage\": 10.31," +
        "\"workingArea\": \"ub\"" +
    "}";

    @Test
    public void testCreate() {
        IGlobalParser parser = new JsonGlobalParser(JSON_EXAMPLE);

        assertNotNull(parser);
    }

    @Test(expected = ParseException.class)
    public void testParseEmployeeEmptyJson() throws ParseException {
        // data
        IGlobalParser parser = new JsonGlobalParser(JSON_EMPTY);

        // execute
        parser.getEmployee();
    }

    @Test(expected = ParseException.class)
    public void testParseEmployeeMissingName() throws ParseException {
        // data
        String json = "{" +
            "\"staffId\": 1234567," +
            "\"department\": \"Fakultät für Informatik\"," +
            "\"workingTime\": \"40:00\"," +
            "\"wage\": 10.31," +
            "\"workingArea\": \"ub\"" +
        "}";
        IGlobalParser parser = new JsonGlobalParser(json);

        // execute
        parser.getEmployee();
    }

    @Test(expected = ParseException.class)
    public void testParseEmployeeMissingStaffId() throws ParseException {
        // data
        String json = "{" +
            "\"name\": \"Max Mustermann\"," +
            "\"department\": \"Fakultät für Informatik\"," +
            "\"workingTime\": \"40:00\"," +
            "\"wage\": 10.31," +
            "\"workingArea\": \"ub\"" +
        "}";
        IGlobalParser parser = new JsonGlobalParser(json);

        // execute
        parser.getEmployee();
    }

    @Test(expected = ParseException.class)
    public void testParseEmployeeAdditionalProperty() throws ParseException {
        // data
        String json = "{" +
            "\"name\": \"Max Mustermann\"," +
            "\"staffId\": 1234567," +
            "\"department\": \"Fakultät für Informatik\"," +
            "\"workingTime\": \"40:00\"," +
            "\"wage\": 10.31," +
            "\"workingArea\": \"ub\"," +
            "\"something\": \"else\"" +
        "}";
        IGlobalParser parser = new JsonGlobalParser(json);

        // execute
        parser.getEmployee();
    }

    @Test
    public void testParseEmployee() throws ParseException {
        // data
        IGlobalParser parser = new JsonGlobalParser(JSON_EXAMPLE);

        // execute
        Employee employee = parser.getEmployee();

        // assert
        assertEquals(new Employee("Max Mustermann", 1234567), employee);
    }

    @Test(expected = ParseException.class)
    public void testParseProfessionEmptyJson() throws ParseException {
        // data
        IGlobalParser parser = new JsonGlobalParser(JSON_EMPTY);

        // execute
        parser.getProfession();
    }

    @Test(expected = ParseException.class)
    public void testParseProfessionMissingDepartment() throws ParseException {
        // data
        String json = "{" +
            "\"name\": \"Max Mustermann\"," +
            "\"staffId\": 1234567," +
            "\"workingTime\": \"40:00\"," +
            "\"wage\": 10.31," +
            "\"workingArea\": \"ub\"" +
        "}";
        IGlobalParser parser = new JsonGlobalParser(json);

        // execute
        parser.getProfession();
    }

    @Test(expected = ParseException.class)
    public void testParseProfessionMissingWorkingTime() throws ParseException {
        // data
        String json = "{" +
            "\"name\": \"Max Mustermann\"," +
            "\"staffId\": 1234567," +
            "\"department\": \"Fakultät für Informatik\"," +
            "\"wage\": 10.31," +
            "\"workingArea\": \"ub\"" +
        "}";
        IGlobalParser parser = new JsonGlobalParser(json);

        // execute
        parser.getProfession();
    }

    @Test(expected = ParseException.class)
    public void testParseProfessionMissingWage() throws ParseException {
        // data
        String json = "{" +
            "\"name\": \"Max Mustermann\"," +
            "\"staffId\": 1234567," +
            "\"department\": \"Fakultät für Informatik\"," +
            "\"workingTime\": \"40:00\"," +
            "\"workingArea\": \"ub\"" +
        "}";
        IGlobalParser parser = new JsonGlobalParser(json);

        // execute
        parser.getProfession();
    }

    @Test(expected = ParseException.class)
    public void testParseProfessionMissingWorkingArea() throws ParseException {
        // data
        String json = "{" +
            "\"name\": \"Max Mustermann\"," +
            "\"staffId\": 1234567," +
            "\"department\": \"Fakultät für Informatik\"," +
            "\"workingTime\": \"40:00\"," +
            "\"wage\": 10.31" +
        "}";
        IGlobalParser parser = new JsonGlobalParser(json);

        // execute
        parser.getProfession();
    }

    @Test(expected = ParseException.class)
    public void testParseProfessionAdditionalProperty() throws ParseException {
        // data
        String json = "{" +
            "\"name\": \"Max Mustermann\"," +
            "\"staffId\": 1234567," +
            "\"department\": \"Fakultät für Informatik\"," +
            "\"workingTime\": \"40:00\"," +
            "\"wage\": 10.31," +
            "\"workingArea\": \"ub\"," +
            "\"something\": \"else\"" +
        "}";
        IGlobalParser parser = new JsonGlobalParser(json);

        // execute
        parser.getProfession();
    }

    @Test(expected = ParseException.class)
    public void testParseProfessionWrongWorkingTimeFormat() throws ParseException {
        // data
        String json = "{" +
            "\"name\": \"Max Mustermann\"," +
            "\"staffId\": 1234567," +
            "\"department\": \"Fakultät für Informatik\"," +
            "\"workingTime\": \"40 Stunden 0 Minuten\"," +
            "\"wage\": 10.31," +
            "\"workingArea\": \"ub\"" +
        "}";
        IGlobalParser parser = new JsonGlobalParser(json);

        // execute
        parser.getProfession();
    }

    @Test(expected = ParseException.class)
    public void testParseProfessionWrongWorkingAreaFormat() throws ParseException {
        // data
        String json = "{" +
            "\"name\": \"Max Mustermann\"," +
            "\"staffId\": 1234567," +
            "\"department\": \"Fakultät für Informatik\"," +
            "\"workingTime\": \"40:00\"," +
            "\"wage\": 10.31," +
            "\"workingArea\": \"Univ.-Be.\"" +
        "}";
        IGlobalParser parser = new JsonGlobalParser(json);

        // execute
        parser.getProfession();
    }

    @Test
    public void testParseProfession() throws ParseException {
        // data
        IGlobalParser parser = new JsonGlobalParser(JSON_EXAMPLE);

        // execute
        Profession profession = parser.getProfession();

        // assert
        assertEquals(new Profession("Fakultät für Informatik", WorkingArea.UB, new TimeSpan(40, 0), 10.31), profession);
    }

}
