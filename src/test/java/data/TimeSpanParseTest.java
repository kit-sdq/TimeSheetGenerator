/* Licensed under MIT 2023. */
package data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TimeSpanParseTest {

	// Exclusively. Refer to
	// https://docs.oracle.com/javase/8/docs/api/java/util/Random.html
	private static final int RANDOM_HOUR_BOUND = 24;
	private static final int RANDOM_MINUTES_BOUND = 60;
	private static final int RANDOM_UPPER_BOUND = 120;

	private static final int MULTIPLE_TEST_ITERATIONS = 10000;

	@Test
	public void testValidLowerBound() {
		//// Test values
		String timeStringSingle = "0:0";
		String timeStringHalf = "0:00";
		String timeStringOtherHalf = "00:0";
		String timeStringDouble = "00:00";
		TimeSpan zeroTS = new TimeSpan(0, 0);

		//// TimeSpan initialization
		TimeSpan tsSingle = TimeSpan.parse(timeStringSingle);
		TimeSpan tsHalf = TimeSpan.parse(timeStringHalf);
		TimeSpan tsOtherHalf = TimeSpan.parse(timeStringOtherHalf);
		TimeSpan tsDouble = TimeSpan.parse(timeStringDouble);

		//// Assertions
		assertEquals(0, tsSingle.getHour());
		assertEquals(0, tsSingle.getMinute());
		assertEquals(0, tsSingle.compareTo(zeroTS));

		assertEquals(0, tsHalf.getHour());
		assertEquals(0, tsHalf.getMinute());
		assertEquals(0, tsHalf.compareTo(zeroTS));

		assertEquals(0, tsOtherHalf.getHour());
		assertEquals(0, tsOtherHalf.getMinute());
		assertEquals(0, tsOtherHalf.compareTo(zeroTS));

		assertEquals(0, tsDouble.getHour());
		assertEquals(0, tsDouble.getMinute());
		assertEquals(0, tsDouble.compareTo(zeroTS));
	}

	@Test
	public void testValidUpperMinuteBound() {
		//// Test values
		String timeString = "0:59";
		TimeSpan checkTS = new TimeSpan(0, 59);

		//// TimeSpan initialization
		TimeSpan timeSpan = TimeSpan.parse(timeString);

		//// Assertions
		assertEquals(0, timeSpan.getHour());
		assertEquals(59, timeSpan.getMinute());
		assertEquals(0, timeSpan.compareTo(checkTS));
	}

	@Test
	public void testInvalidUpperMinuteBound() {
		//// Test values
		String timeString = "0:60";

		//// TimeSpan initialization
		Assertions.assertThrows(IllegalArgumentException.class, () -> TimeSpan.parse(timeString));
	}

	@Test
	public void testInvalidMinutesRandom() {
		//// Random
		Random rand = new Random();

		//// Test values
		int minutes = rand.nextInt(RANDOM_UPPER_BOUND - 60) + 60;
		String timeString = "0:" + minutes;

		//// TimeSpan initialization
		Assertions.assertThrows(IllegalArgumentException.class, () -> TimeSpan.parse(timeString));
	}

	@Test
	public void testMinutesRandom() {
		//// Random
		Random rand = new Random();

		//// Test values
		int minutes = rand.nextInt(RANDOM_UPPER_BOUND);
		String timeString = "0:" + minutes;

		//// TimeSpan initialization
		TimeSpan timeSpan;
		TimeSpan checkTS;
		try {
			timeSpan = TimeSpan.parse(timeString);
			checkTS = new TimeSpan(0, minutes);
		} catch (IllegalArgumentException e) {
			assertTrue(minutes > 59);
			return;
		}

		//// Assertions
		assertEquals(0, timeSpan.compareTo(checkTS));
		assertEquals(minutes, timeSpan.getMinute());
		assertEquals(0, timeSpan.getHour());
	}

	@Test
	public void testMinutesMultiple() {
		//// Random
		Random rand = new Random();

		//// TimeSpan initialization
		for (int i = 0; i < MULTIPLE_TEST_ITERATIONS; i++) {
			//// Test values
			int minutes = rand.nextInt(RANDOM_UPPER_BOUND);
			String timeString = "0:" + minutes;

			//// TimeSpan initialization
			TimeSpan timeSpan;
			TimeSpan checkTS;
			try {
				timeSpan = TimeSpan.parse(timeString);
				checkTS = new TimeSpan(0, minutes);
			} catch (IllegalArgumentException e) {
				assertTrue(minutes > 59);
				continue;
			}

			//// Assertions
			assertEquals(0, timeSpan.compareTo(checkTS));
			assertEquals(minutes, timeSpan.getMinute());
			assertEquals(0, timeSpan.getHour());
		}
	}

	@Test
	public void testValidMultipleRandom() {
		//// Random
		Random rand = new Random();

		for (int i = 0; i < MULTIPLE_TEST_ITERATIONS; i++) {
			//// Test values
			int hours = rand.nextInt(RANDOM_HOUR_BOUND);
			int minutes = rand.nextInt(RANDOM_MINUTES_BOUND);
			String timeString = hours + ":" + minutes;

			//// TimeSpan initialization
			TimeSpan timeSpan = TimeSpan.parse(timeString);
			TimeSpan checkTS = new TimeSpan(hours, minutes);

			//// Assertions
			assertEquals(0, timeSpan.compareTo(checkTS));
			assertEquals(minutes, timeSpan.getMinute());
			assertEquals(hours, timeSpan.getHour());
		}
	}

	@Test
	public void testMultipleRandom() {
		//// Random
		Random rand = new Random();

		for (int i = 0; i < MULTIPLE_TEST_ITERATIONS; i++) {
			//// Test values
			int hours = rand.nextInt(RANDOM_UPPER_BOUND);
			int minutes = rand.nextInt(RANDOM_UPPER_BOUND);
			String timeString = hours + ":" + minutes;

			//// TimeSpan initialization
			TimeSpan timeSpan;
			TimeSpan checkTS;
			try {
				timeSpan = TimeSpan.parse(timeString);
				checkTS = new TimeSpan(hours, minutes);
			} catch (IllegalArgumentException e) {
				assertTrue(minutes > 59);
				continue;
			}

			//// Assertions
			assertEquals(0, timeSpan.compareTo(checkTS));
			assertEquals(minutes, timeSpan.getMinute());
			assertEquals(hours, timeSpan.getHour());
		}
	}

	@Test
	public void testNegativeHours() {
		//// Test values
		String timeString = "-2:00";

		//// TimeSpan initialization
		Assertions.assertThrows(IllegalArgumentException.class, () -> TimeSpan.parse(timeString));
	}

	@Test
	public void testNegativeMinutes() {
		//// Test values
		String timeString = "0:-4";

		//// TimeSpan initialization
		Assertions.assertThrows(IllegalArgumentException.class, () -> TimeSpan.parse(timeString));
	}

	@Test
	public void testEmptyString() {
		//// Test values
		String timeString = "";

		//// TimeSpan initialization
		Assertions.assertThrows(IllegalArgumentException.class, () -> TimeSpan.parse(timeString));
	}

	@Test
	public void testInvalidStringColon() {
		//// Test values
		String timeString = ":";

		//// TimeSpan initialization
		Assertions.assertThrows(IllegalArgumentException.class, () -> TimeSpan.parse(timeString));
	}

	@Test
	public void testInvalidStringNumber() {
		//// Test values
		String timeString = "7";

		//// TimeSpan initialization
		Assertions.assertThrows(IllegalArgumentException.class, () -> TimeSpan.parse(timeString));
	}

	@Test
	public void testInvalidStringNaN() {
		//// Test values
		String timeString = "TestString";

		//// TimeSpan initialization
		Assertions.assertThrows(IllegalArgumentException.class, () -> TimeSpan.parse(timeString));
	}
}
