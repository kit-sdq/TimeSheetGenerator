/* Licensed under MIT 2023-2024. */
package checker;

import data.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MiLoGCheckerCheckTest {

	//// Placeholder for time sheet construction
	private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
	private static final Profession PROFESSION = new Profession("Fakultät für Informatik", WorkingArea.UB, new TimeSpan(40, 0), 10.31);
	private static final YearMonth YEAR_MONTH = YearMonth.of(2024, Month.NOVEMBER);
	private static final TimeSpan zeroTs = new TimeSpan(0, 0);

	@Test
	public void testSingleEntryValidTimeSheet() throws CheckerException {
		//// Test values
		TimeSpan start = new TimeSpan(8, 0);
		TimeSpan end = new TimeSpan(12, 0);
		TimeSpan pause = zeroTs;
		LocalDate date = LocalDate.of(2024, 11, 22); // Valid working day

		//// Checker initialization
		Entry entry = new Entry("Test", date, start, end, pause, false);
		Entry[] entries = { entry };
		TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
		MiLoGChecker checker = new MiLoGChecker(timeSheet);

		//// Assertions
		assertEquals(CheckerReturn.VALID, checker.check());
		assertTrue(checker.getErrors().isEmpty());
	}

	@Test
	public void testSingleEntryValidButHoliday() throws CheckerException {
		//// Test values
		TimeSpan start = new TimeSpan(8, 0);
		TimeSpan end = new TimeSpan(12, 0);
		TimeSpan pause = zeroTs;
		LocalDate date = LocalDate.of(2024, 12, 25); // Holiday

		//// Checker initialization
		Entry entry = new Entry("Test", date, start, end, pause, false);
		Entry[] entries = { entry };
		TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
		MiLoGChecker checker = new MiLoGChecker(timeSheet);

		//// Expectation
		String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TIME_HOLIDAY.getErrorMessage(date);

		//// Assertions
		assertEquals(CheckerReturn.INVALID, checker.check());
		assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
	}

	@Test
	public void testSingleEntryValidButSunday() throws CheckerException {
		//// Test values
		TimeSpan start = new TimeSpan(8, 0);
		TimeSpan end = new TimeSpan(12, 0);
		TimeSpan pause = zeroTs;
		LocalDate date = LocalDate.of(2024, 12, 1); // Sunday

		//// Checker initialization
		Entry entry = new Entry("Test", date, start, end, pause, false);
		Entry[] entries = { entry };
		TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
		MiLoGChecker checker = new MiLoGChecker(timeSheet);

		//// Expectation
		String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TIME_SUNDAY.getErrorMessage(date);

		//// Assertions
		assertEquals(CheckerReturn.INVALID, checker.check());
		assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
	}

	@Test
	public void testSingleEntryValidButSundayAndHoliday() throws CheckerException {
		//// Test values
		TimeSpan start = new TimeSpan(8, 0);
		TimeSpan end = new TimeSpan(12, 0);
		TimeSpan pause = zeroTs;
		LocalDate date = LocalDate.of(2022, 12, 25); // Sunday and holiday

		//// Checker initialization
		Entry entry = new Entry("Test", date, start, end, pause, false);
		Entry[] entries = { entry };
		TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
		MiLoGChecker checker = new MiLoGChecker(timeSheet);

		//// Expectation
		String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TIME_SUNDAY.getErrorMessage(date);

		//// Assertions
		assertEquals(CheckerReturn.INVALID, checker.check());
		assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
	}
}
