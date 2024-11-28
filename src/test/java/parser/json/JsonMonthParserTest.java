/* Licensed under MIT 2023-2024. */
package parser.json;

import data.Entry;
import data.TimeSpan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.IMonthParser;
import parser.ParseException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonMonthParserTest {

	private static final String JSON_EMPTY = "{}";
	private static final String JSON_EXAMPLE_MINIMAL = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": []" + "}";
	private static final String JSON_EXAMPLE_NECESSARY = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": ["
			+ "{\"action\": \"Korrektur\", \"day\": 2, \"start\": \"10:00\", \"end\": \"11:00\"},"
			+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"00:30\"},"
			+ "{\"action\": \"Urlaub in Italien\", \"day\": 11, \"start\": \"09:00\", \"end\": \"12:00\", \"vacation\": true}" + "]" + "}";
	private static final String JSON_EXAMPLE_FULL = "{" + "\"year\": 2019," + "\"month\": 11," + "\"pred_transfer\": \"2:00\"," + "\"succ_transfer\": \"1:00\","
			+ "\"entries\": [" + "{\"action\": \"Korrektur\", \"day\": 2, \"start\": \"10:00\", \"end\": \"11:00\"},"
			+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"00:30\"},"
			+ "{\"action\": \"Urlaub in Italien\", \"day\": 11, \"start\": \"09:00\", \"end\": \"12:00\", \"vacation\": true}" + "]" + "}";

	@Test
	public void testCreate() {
		IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_NECESSARY);

		assertNotNull(parser);
	}

	@Test
	public void testCreateWithOptional() {
		IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_FULL);

		assertNotNull(parser);
	}

	@Test
	public void testParseYearMonthEmptyJson() throws ParseException {
		// data
		IMonthParser parser = new JsonMonthParser(JSON_EMPTY);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getYearMonth);
	}

	@Test
	public void testParseYearMonthMissingYear() throws ParseException {
		// data
		String json = "{" + "\"month\": 11," + "\"entries\": [" + "{\"action\": \"Korrektur\", \"day\": 2, \"start\": \"10:00\", \"end\": \"11:00\"},"
				+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"00:30\"},"
				+ "{\"action\": \"Urlaub in Italien\", \"day\": 11, \"start\": \"09:00\", \"end\": \"12:00\", \"vacation\": true}" + "]" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getYearMonth);
	}

	@Test
	public void testParseYearMonthMissingMonth() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"entries\": [" + "{\"action\": \"Korrektur\", \"day\": 2, \"start\": \"10:00\", \"end\": \"11:00\"},"
				+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"00:30\"},"
				+ "{\"action\": \"Urlaub in Italien\", \"day\": 11, \"start\": \"09:00\", \"end\": \"12:00\", \"vacation\": true}" + "]" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getYearMonth);
	}

	@Test
	public void testParseYearMonthAdditionalProperty() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": ["
				+ "{\"action\": \"Korrektur\", \"day\": 2, \"start\": \"10:00\", \"end\": \"11:00\"},"
				+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"00:30\"},"
				+ "{\"action\": \"Urlaub in Italien\", \"day\": 11, \"start\": \"09:00\", \"end\": \"12:00\", \"vacation\": true}" + "],"
				+ "\"something\": \"else\"" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getYearMonth);
	}

	@Test
	public void testParseYearMonth() throws ParseException {
		// data
		IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_NECESSARY);

		// execute
		YearMonth yearmonth = parser.getYearMonth();

		// assert
		assertEquals(YearMonth.of(2019, 11), yearmonth);
	}

	@Test
	public void testParseEntriesEmptyJson() throws ParseException {
		// data
		IMonthParser parser = new JsonMonthParser(JSON_EMPTY);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEntries);
	}

	@Test
	public void testParseEntriesMissingEntries() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEntries);
	}

	@Test
	public void testParseEntriesAdditionalProperty() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"entries\": ["
				+ "{\"action\": \"Korrektur\", \"day\": 2, \"start\": \"10:00\", \"end\": \"11:00\"},"
				+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"00:30\"},"
				+ "{\"action\": \"Urlaub in Italien\", \"day\": 11, \"start\": \"09:00\", \"end\": \"12:00\", \"vacation\": true}" + "],"
				+ "\"something\": \"else\"" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getEntries);
	}

	@Test
	public void testParseEntriesEmpty() throws ParseException {
		// data
		IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_MINIMAL);

		// execute
		Entry[] entries = parser.getEntries();

		// assert
		assertEquals(0, entries.length);
	}

	@Test
	public void testParseEntries() throws ParseException {
		// data
		IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_NECESSARY);

		// execute
		Entry[] entries = parser.getEntries();

		// assert
		List<Entry> entryList = Arrays.asList(entries);

		assertEquals(3, entryList.size());

		assert (entryList.contains(new Entry("Korrektur", LocalDate.of(2019, 11, 2), new TimeSpan(10, 0), new TimeSpan(11, 0), new TimeSpan(0, 0), false)));
		assert (entryList
				.contains(new Entry("Fragen beantworten", LocalDate.of(2019, 11, 4), new TimeSpan(11, 31), new TimeSpan(15, 11), new TimeSpan(0, 30), false)));
		assert (entryList
				.contains(new Entry("Urlaub in Italien", LocalDate.of(2019, 11, 11), new TimeSpan(9, 0), new TimeSpan(12, 0), new TimeSpan(0, 0), true)));
	}

	@Test
	public void testParsePredTransferEmptyJson() throws ParseException {
		// data
		IMonthParser parser = new JsonMonthParser(JSON_EMPTY);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getPredTransfer);
	}

	@Test
	public void testParsePredTransferAdditionalProperty() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"vacation\": \"3:00\"," + "\"pred_transfer\": \"2:00\"," + "\"succ_transfer\": \"1:00\","
				+ "\"entries\": [" + "{\"action\": \"Korrektur\", \"day\": 2, \"start\": \"10:00\", \"end\": \"11:00\"},"
				+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"00:30\"},"
				+ "{\"action\": \"Urlaub in Italien\", \"day\": 11, \"start\": \"09:00\", \"end\": \"12:00\", \"vacation\": true}" + "],"
				+ "\"something\": \"else\"" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getPredTransfer);
	}

	@Test
	public void testParsePredTransferWrongFormat() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"vacation\": \"3:00\"," + "\"pred_transfer\": \"2 Stunden 0 Minuten\","
				+ "\"succ_transfer\": \"1:00\"," + "\"entries\": [" + "{\"action\": \"Korrektur\", \"day\": 2, \"start\": \"10:00\", \"end\": \"11:00\"},"
				+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"00:30\"},"
				+ "{\"action\": \"Urlaub in Italien\", \"day\": 11, \"start\": \"09:00\", \"end\": \"12:00\", \"vacation\": true}" + "]" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getPredTransfer);
	}

	@Test
	public void testParsePredTransfer() throws ParseException {
		// data
		IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_FULL);

		// execute
		TimeSpan vacation = parser.getPredTransfer();

		// assert
		assertEquals(new TimeSpan(2, 0), vacation);
	}

	@Test
	public void testParsePredTransferDefault() throws ParseException {
		// data
		IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_NECESSARY);

		// execute
		TimeSpan vacation = parser.getPredTransfer();

		// assert
		assertEquals(new TimeSpan(0, 0), vacation);
	}

	@Test
	public void testParseSuccTransferEmptyJson() throws ParseException {
		// data
		IMonthParser parser = new JsonMonthParser(JSON_EMPTY);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getSuccTransfer);
	}

	@Test
	public void testParseSuccTransferAdditionalProperty() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"vacation\": \"3:00\"," + "\"pred_transfer\": \"2:00\"," + "\"succ_transfer\": \"1:00\","
				+ "\"entries\": [" + "{\"action\": \"Korrektur\", \"day\": 2, \"start\": \"10:00\", \"end\": \"11:00\"},"
				+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"00:30\"},"
				+ "{\"action\": \"Urlaub in Italien\", \"day\": 11, \"start\": \"09:00\", \"end\": \"12:00\", \"vacation\": true}" + "],"
				+ "\"something\": \"else\"" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getSuccTransfer);
	}

	@Test
	public void testParseSuccTransferWrongFormat() throws ParseException {
		// data
		String json = "{" + "\"year\": 2019," + "\"month\": 11," + "\"vacation\": \"3:00\"," + "\"pred_transfer\": \"2:00\","
				+ "\"succ_transfer\": \"1 Stunden 0 Minuten\"," + "\"entries\": ["
				+ "{\"action\": \"Korrektur\", \"day\": 2, \"start\": \"10:00\", \"end\": \"11:00\"},"
				+ "{\"action\": \"Fragen beantworten\", \"day\": 4, \"start\": \"11:31\", \"end\": \"15:11\", \"pause\": \"00:30\"},"
				+ "{\"action\": \"Urlaub in Italien\", \"day\": 11, \"start\": \"09:00\", \"end\": \"12:00\", \"vacation\": true}" + "]" + "}";
		IMonthParser parser = new JsonMonthParser(json);

		// execute
		Assertions.assertThrows(ParseException.class, parser::getSuccTransfer);
	}

	@Test
	public void testParseSuccTransfer() throws ParseException {
		// data
		IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_FULL);

		// execute
		TimeSpan vacation = parser.getSuccTransfer();

		// assert
		assertEquals(new TimeSpan(1, 0), vacation);
	}

	@Test
	public void testParseSuccTransferDefault() throws ParseException {
		// data
		IMonthParser parser = new JsonMonthParser(JSON_EXAMPLE_NECESSARY);

		// execute
		TimeSpan vacation = parser.getSuccTransfer();

		// assert
		assertEquals(new TimeSpan(0, 0), vacation);
	}

}
