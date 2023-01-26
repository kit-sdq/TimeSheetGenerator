/* Licensed under MIT 2023. */
package io;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import data.Employee;
import data.Entry;
import data.Profession;
import data.TimeSheet;
import data.TimeSpan;
import data.WorkingArea;

public class LatexGeneratorPlaceholderTest {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy");

	private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
	private static final Profession PROFESSION = new Profession("Fakultät für Informatik", WorkingArea.UB, new TimeSpan(40, 0), 10.31);
	private static final YearMonth YEAR_MONTH = YearMonth.of(2019, Month.NOVEMBER);

	private static TimeSheet timeSheet;

	@BeforeAll
	public static void beforeAll() {
		Entry entry0 = new Entry("Test Action 1", YEAR_MONTH.atDay(12), new TimeSpan(10, 0), new TimeSpan(14, 0), new TimeSpan(0, 30), false);
		Entry entry1 = new Entry("Test Action 2", YEAR_MONTH.atDay(14), new TimeSpan(8, 0), new TimeSpan(12, 0), new TimeSpan(0, 0), false);
		Entry entry2 = new Entry("Test Vacation", YEAR_MONTH.atDay(19), new TimeSpan(9, 0), new TimeSpan(10, 0), new TimeSpan(0, 0), true);
		Entry entry3 = new Entry("Test 2 Vacation", YEAR_MONTH.atDay(21), new TimeSpan(15, 0), new TimeSpan(16, 30), new TimeSpan(0, 0), true);
		Entry[] entries = new Entry[] { entry0, entry1, entry2, entry3 };

		timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, new TimeSpan(1, 0), new TimeSpan(2, 0));
	}

	@ParameterizedTest
	@MethodSource("getTemplateAndExpected")
	public void testGenerate(final String template, final String expected) {
		LatexGenerator generator = new LatexGenerator(timeSheet, template);
		String latex = generator.generate();
		assertEquals(expected, latex);
	}

	private static Stream<Arguments> getTemplateAndExpected() {
		return Stream.of(Arguments.of("Year: !year", "Year: 2019"), Arguments.of("Month: !month", "Month: 11"),
				Arguments.of("Employee Name: !employeeName", "Employee Name: Max Mustermann"), Arguments.of("Employee ID: !employeeID", "Employee ID: 1234567"),
				Arguments.of("GF / UB: !workingArea", "GF / UB: \\textbf{GF:} $\\Box$ \\textbf{UB:} $\\boxtimes$"),
				Arguments.of("Department: !department", "Department: Fakultät für Informatik"), Arguments.of("Max Hours: !workingTime", "Max Hours: 40:00"),
				Arguments.of("Wage: !wage", "Wage: 10.31"), Arguments.of("Vacation: !vacation", "Vacation: 02:30"), Arguments.of("Hours: !sum", "Hours: 10:00"),
				Arguments.of("Transfer Pred: !carryPred", "Transfer Pred: 02:00"), Arguments.of("Transfer Succ: !carrySucc", "Transfer Succ: 01:00"),
				Arguments.of("Action 1: !action, Action 2: !action, Action 3: !action, Action 4: !action",
						"Action 1: Test Action 1, Action 2: Test Action 2, Action 3: Test Vacation, Action 4: Test 2 Vacation"),
				Arguments.of("Date 1: !date, Date 2: !date, Date 3: !date, Date 4: !date",
						"Date 1: " + LocalDate.of(2019, 11, 12).format(DATE_TIME_FORMATTER) + ", Date 2: "
								+ LocalDate.of(2019, 11, 14).format(DATE_TIME_FORMATTER) + ", Date 3: " + LocalDate.of(2019, 11, 19).format(DATE_TIME_FORMATTER)
								+ ", Date 4: " + LocalDate.of(2019, 11, 21).format(DATE_TIME_FORMATTER)),
				Arguments.of("Start 1: !begin, Start 2: !begin, Start 3: !begin, Start 4: !begin",
						"Start 1: 10:00, Start 2: 08:00, Start 3: 09:00, Start 4: 15:00"),
				Arguments.of("End 1: !end, End 2: !end, End 3: !end, End 4: !end", "End 1: 14:00, End 2: 12:00, End 3: 10:00, End 4: 16:30"),
				Arguments.of("Pause 1: !break, Pause 2: !break, Pause 3: !break, Pause 4: !break",
						"Pause 1: 00:30, Pause 2: 00:00, Pause 3: 00:00, Pause 4: 00:00"),
				Arguments.of("Time 1: !dayTotal, Time 2: !dayTotal, Time 3: !dayTotal, Time 4: !dayTotal",
						"Time 1: 03:30, Time 2: 04:00, Time 3: 01:00 U, Time 4: 01:30 U"));
	}

}