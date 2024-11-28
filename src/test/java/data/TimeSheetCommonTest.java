/* Licensed under MIT 2023-2024. */
package data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TimeSheetCommonTest {

	@Test
	public void testInvalidTransferVacation() {
		//// Test values
		TimeSpan maxWorkingTime = new TimeSpan(40, 0);
		TimeSpan predTransfer = new TimeSpan(60, 0);
		TimeSpan succTransfer = new TimeSpan(20, 0);

		//// TimeSheet initialization
		Employee employee = new Employee("Max Mustermann", 1234567);
		Profession profession = new Profession("IPD", WorkingArea.UB, maxWorkingTime, 10.31);
		YearMonth yearMonth = YearMonth.of(2019, 11);
		Entry[] entries = new Entry[] { new Entry("Vacation", LocalDate.of(2019, 11, 29), new TimeSpan(10, 0), new TimeSpan(20, 0), new TimeSpan(0, 0), true) };
		Assertions.assertThrows(IllegalArgumentException.class, () -> new TimeSheet(employee, profession, yearMonth, entries, succTransfer, predTransfer));
	}

	@Test
	public void testValidTransferVacationUpperBound() {
		//// Test values
		TimeSpan maxWorkingTime = new TimeSpan(40, 0);
		TimeSpan predTransfer = new TimeSpan(50, 0);
		TimeSpan succTransfer = new TimeSpan(20, 0);

		//// TimeSheet initialization
		Employee employee = new Employee("Max Mustermann", 1234567);
		Profession profession = new Profession("IPD", WorkingArea.UB, maxWorkingTime, 10.31);
		YearMonth yearMonth = YearMonth.of(2019, 11);
		Entry[] entries = new Entry[] { new Entry("Vacation", LocalDate.of(2019, 11, 29), new TimeSpan(10, 0), new TimeSpan(20, 0), new TimeSpan(0, 0), true) };
		TimeSheet timeSheet = new TimeSheet(employee, profession, yearMonth, entries, succTransfer, predTransfer);

		//// Assertions
		assertNotNull(timeSheet);
	}

}
