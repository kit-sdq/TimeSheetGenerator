/* Licensed under MIT 2023-2024. */
package data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntryCommonTest {

	@Test
	public void testConstructor() {
		String action = "Test";
		TimeSpan start = new TimeSpan(14, 0);
		TimeSpan end = new TimeSpan(18, 0);
		TimeSpan pause = new TimeSpan(0, 30);

		LocalDate date = LocalDate.of(2019, 11, 16);

		Entry entry = new Entry(action, date, start, end, pause, false);

		TimeSpan workingTime = entry.getWorkingTime();
		assertEquals(workingTime.getHour(), 3);
		assertEquals(workingTime.getMinute(), 30);

		assertEquals(entry.getAction(), action);
		assertEquals(entry.getDate(), date);
		assertEquals(entry.getStart().compareTo(start), 0);
		assertEquals(entry.getEnd().compareTo(end), 0);
		assertEquals(entry.getPause().compareTo(pause), 0);
	}

	@Test
	public void testConstructorIllegalArgument1() {
		String action = "Test";
		TimeSpan start = new TimeSpan(14, 0);
		TimeSpan end = new TimeSpan(42, 0);
		TimeSpan pause = new TimeSpan(0, 30);

		LocalDate date = LocalDate.of(2019, 11, 16);

		Assertions.assertThrows(IllegalArgumentException.class, () -> new Entry(action, date, start, end, pause, false));
	}

	@Test
	public void testConstructorIllegalArgument2() {
		String action = "Test";
		TimeSpan start = new TimeSpan(23, 59);
		TimeSpan end = new TimeSpan(24, 0);
		TimeSpan pause = new TimeSpan(0, 30);

		LocalDate date = LocalDate.of(2019, 11, 16);

		Assertions.assertThrows(IllegalArgumentException.class, () -> new Entry(action, date, start, end, pause, false));
	}

	@Test
	public void testConstructorIllegalArgument3() {
		String action = "Test";
		TimeSpan start = new TimeSpan(23, 00);
		TimeSpan end = new TimeSpan(22, 0);
		TimeSpan pause = new TimeSpan(0, 30);

		LocalDate date = LocalDate.of(2019, 11, 16);

		Assertions.assertThrows(IllegalArgumentException.class, () -> new Entry(action, date, start, end, pause, false));
	}

	@Test
	public void testConstructorIllegalArgument4() {
		String action = "Test";
		TimeSpan start = new TimeSpan(25, 00);
		TimeSpan end = new TimeSpan(26, 0);
		TimeSpan pause = new TimeSpan(0, 30);

		LocalDate date = LocalDate.of(2019, 11, 16);

		Assertions.assertThrows(IllegalArgumentException.class, () -> new Entry(action, date, start, end, pause, false));
	}

	@Test
	public void testConstructorIllegalArgumentPauseAndVacation() {
		String action = "Test";
		TimeSpan start = new TimeSpan(14, 0);
		TimeSpan end = new TimeSpan(18, 0);
		TimeSpan pause = new TimeSpan(0, 30);

		LocalDate date = LocalDate.of(2019, 11, 16);

		Assertions.assertThrows(IllegalArgumentException.class, () -> new Entry(action, date, start, end, pause, true));
	}

}
