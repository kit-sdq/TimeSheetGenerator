/* Licensed under MIT 2023-2024. */
package parser.json;

import data.Entry;
import data.TimeSpan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.IMonthParser;
import parser.ParseException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonMonthParserEntryTest {

	private static final String JSON_EXAMPLE_EMPTY = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": [" + "{}" + "]" + "}";
	private static final String JSON_EXAMPLE_NECESSARY = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": ["
			+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\"}" + "]" + "}";
	private static final String JSON_EXAMPLE_FULL_PAUSE = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": ["
			+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"00:30\"}" + "]" + "}";
	private static final String JSON_EXAMPLE_FULL_VACATION = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": ["
			+ "{\"action\": \"Urlaub in Italien\", \"day\": 11, \"start\": \"09:00\", \"end\": \"12:00\", \"vacation\": true}" + "]" + "}";

	@Test
	public void testParseEntryEmptyJsonObject() throws ParseException {
		// data
		IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_EMPTY);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEntries);
	}

	@Test
	public void testParseEntryMissingAction() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": [" + "{\"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\"}" + "]" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEntries);
	}

	@Test
	public void testParseEntryMissingDay() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": ["
				+ "{\"action\": \"Fragen beantworten\", \"start\": \"11:31\", \"end\": \"15:11\"}" + "]" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEntries);
	}

	@Test
	public void testParseEntryMissingStart() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": [" + "{\"action\": \"Fragen beantworten\", \"day\": 4, \"end\": \"15:11\"}"
				+ "]" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEntries);
	}

	@Test
	public void testParseEntryMissingEnd() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": [" + "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\"}"
				+ "]" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEntries);
	}

	@Test
	public void testParseEntryAdditionalProperty() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": ["
				+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"something\": \"else\"}" + "]" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEntries);
	}

	@Test
	public void testParseEntryWrongStartFormat() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": ["
				+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"kurz nach halb elf\", \"end\": \"15:11\"}" + "]" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEntries);
	}

	@Test
	public void testParseEntryWrongEndFormat() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": ["
				+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"kurz nach zehn nach drei\"}" + "]" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEntries);
	}

	@Test
	public void testParseEntryWrongPauseFormat() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": ["
				+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"halbe Stunde\"}" + "]" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEntries);
	}

	@Test
	public void testParseEntryWrongVacationFormat() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": ["
				+ "{\"action\": \"Urlaub in Italien\", \"day\": 11, \"start\": \"09:00\", \"end\": \"12:00\", \"vacation\": \"ja, in Italien\"}" + "]" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEntries);
	}

	@Test
	public void testParseEntryPauseAndVacation() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": ["
				+ "{\"action\": \"Urlaub in Italien\", \"day\": 11, \"start\": \"09:00\", \"end\": \"12:00\", \"pause\": \"00:30\", \"vacation\": true}" + "]"
				+ "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEntries);
	}

	@Test
	public void testParseEntryDefault() throws ParseException {
		// data
		IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_NECESSARY);

		// execute
		Entry[] entries = parser.getEntries();

		// assert
		assertEquals(1, entries.length);

		assertEquals(new Entry("Fragen beantworten", LocalDate.of(2019, 11, 4), new TimeSpan(11, 31), new TimeSpan(15, 11), new TimeSpan(0, 0), false),
				entries[0]);
	}

	@Test
	public void testParseEntryWithPause() throws ParseException {
		// data
		IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_FULL_PAUSE);

		// execute
		Entry[] entries = parser.getEntries();

		// assert
		assertEquals(1, entries.length);

		assertEquals(new Entry("Fragen beantworten", LocalDate.of(2019, 11, 4), new TimeSpan(11, 31), new TimeSpan(15, 11), new TimeSpan(0, 30), false),
				entries[0]);
	}

	@Test
	public void testParseVacationEntry() throws ParseException {
		// data
		IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_FULL_VACATION);

		// execute
		Entry[] entries = parser.getEntries();

		// assert
		assertEquals(1, entries.length);

		assertEquals(new Entry("Urlaub in Italien", LocalDate.of(2019, 11, 11), new TimeSpan(9, 0), new TimeSpan(12, 0), new TimeSpan(0, 0), true), entries[0]);
	}

}
