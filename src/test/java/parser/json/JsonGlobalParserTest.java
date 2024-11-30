/* Licensed under MIT 2023-2024. */
package parser.json;

import data.Employee;
import data.Profession;
import data.TimeSpan;
import data.WorkingArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.IGlobalParser;
import parser.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonGlobalParserTest {

	private static final String JSON_EMPTY = "{}";
	private static final String JSON_EXAMPLE = "{" + "\"name\": \"Max Mustermann\"," + "\"staffId\": 1234567," + "\"department\": \"Fakultät für Informatik\","
			+ "\"workingTime\": \"40:00\"," + "\"wage\": 10.31," + "\"workingArea\": \"ub\"" + "}";

	@Test
	public void testCreate() {
		IGlobalParser parser = new JsonGlobalParser(JSON_EXAMPLE);

		assertNotNull(parser);
	}

	@Test
	public void testParseEmployeeEmptyJson() throws ParseException {
		// data
		IGlobalParser parser = new JsonGlobalParser(JSON_EMPTY);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEmployee);
	}

	@Test
	public void testParseEmployeeMissingName() throws ParseException {
		// data
		String json = "{" + "\"staffId\": 1234567," + "\"department\": \"Fakultät für Informatik\"," + "\"workingTime\": \"40:00\"," + "\"wage\": 10.31,"
				+ "\"workingArea\": \"ub\"" + "}";
		IGlobalParser parser = new JsonGlobalParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEmployee);
	}

	@Test
	public void testParseEmployeeMissingStaffId() throws ParseException {
		// data
		String json = "{" + "\"name\": \"Max Mustermann\"," + "\"department\": \"Fakultät für Informatik\"," + "\"workingTime\": \"40:00\","
				+ "\"wage\": 10.31," + "\"workingArea\": \"ub\"" + "}";
		IGlobalParser parser = new JsonGlobalParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEmployee);
	}

	@Test
	public void testParseEmployeeAdditionalProperty() throws ParseException {
		// data
		String json = "{" + "\"name\": \"Max Mustermann\"," + "\"staffId\": 1234567," + "\"department\": \"Fakultät für Informatik\","
				+ "\"workingTime\": \"40:00\"," + "\"wage\": 10.31," + "\"workingArea\": \"ub\"," + "\"something\": \"else\"" + "}";
		IGlobalParser parser = new JsonGlobalParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEmployee);
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

	@Test
	public void testParseProfessionEmptyJson() throws ParseException {
		// data
		IGlobalParser parser = new JsonGlobalParser(JSON_EMPTY);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getProfession);
	}

	@Test
	public void testParseProfessionMissingDepartment() throws ParseException {
		// data
		String json = "{" + "\"name\": \"Max Mustermann\"," + "\"staffId\": 1234567," + "\"workingTime\": \"40:00\"," + "\"wage\": 10.31,"
				+ "\"workingArea\": \"ub\"" + "}";
		IGlobalParser parser = new JsonGlobalParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getProfession);
	}

	@Test
	public void testParseProfessionMissingWorkingTime() throws ParseException {
		// data
		String json = "{" + "\"name\": \"Max Mustermann\"," + "\"staffId\": 1234567," + "\"department\": \"Fakultät für Informatik\"," + "\"wage\": 10.31,"
				+ "\"workingArea\": \"ub\"" + "}";
		IGlobalParser parser = new JsonGlobalParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getProfession);
	}

	@Test
	public void testParseProfessionMissingWage() throws ParseException {
		// data
		String json = "{" + "\"name\": \"Max Mustermann\"," + "\"staffId\": 1234567," + "\"department\": \"Fakultät für Informatik\","
				+ "\"workingTime\": \"40:00\"," + "\"workingArea\": \"ub\"" + "}";
		IGlobalParser parser = new JsonGlobalParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getProfession);
	}

	@Test
	public void testParseProfessionMissingWorkingArea() throws ParseException {
		// data
		String json = "{" + "\"name\": \"Max Mustermann\"," + "\"staffId\": 1234567," + "\"department\": \"Fakultät für Informatik\","
				+ "\"workingTime\": \"40:00\"," + "\"wage\": 10.31" + "}";
		IGlobalParser parser = new JsonGlobalParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getProfession);
	}

	@Test
	public void testParseProfessionAdditionalProperty() throws ParseException {
		// data
		String json = "{" + "\"name\": \"Max Mustermann\"," + "\"staffId\": 1234567," + "\"department\": \"Fakultät für Informatik\","
				+ "\"workingTime\": \"40:00\"," + "\"wage\": 10.31," + "\"workingArea\": \"ub\"," + "\"something\": \"else\"" + "}";
		IGlobalParser parser = new JsonGlobalParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getProfession);
	}

	@Test
	public void testParseProfessionWrongWorkingTimeFormat() throws ParseException {
		// data
		String json = "{" + "\"name\": \"Max Mustermann\"," + "\"staffId\": 1234567," + "\"department\": \"Fakultät für Informatik\","
				+ "\"workingTime\": \"40 Stunden 0 Minuten\"," + "\"wage\": 10.31," + "\"workingArea\": \"ub\"" + "}";
		IGlobalParser parser = new JsonGlobalParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getProfession);
	}

	@Test
	public void testParseProfessionWrongWorkingAreaFormat() throws ParseException {
		// data
		String json = "{" + "\"name\": \"Max Mustermann\"," + "\"staffId\": 1234567," + "\"department\": \"Fakultät für Informatik\","
				+ "\"workingTime\": \"40:00\"," + "\"wage\": 10.31," + "\"workingArea\": \"Univ.-Be.\"" + "}";
		IGlobalParser parser = new JsonGlobalParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getProfession);
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
