/* Licensed under MIT 2023-2024. */
package data;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeSpanCompareTest {

	private static final int RANDOM_HOURS_BOUND = 9999;

	// Exclusively. Refer to
	// https://docs.oracle.com/javase/8/docs/api/java/util/Random.html
	private static final int RANDOM_MINUTES_BOUND = 60;

	@Test
	public void testIdentical1() {

		TimeSpan ts1 = new TimeSpan(0, 0);
		TimeSpan ts2 = new TimeSpan(0, 0);

		assertEquals(ts1.compareTo(ts2), 0);
	}

	@Test
	public void testIdentical2() {

		TimeSpan ts1 = new TimeSpan(26, 33);
		TimeSpan ts2 = new TimeSpan(26, 33);

		assertEquals(ts1.compareTo(ts2), 0);
	}

	@Test
	public void testIdenticalRandom() {

		Random rand = new Random();
		int hours = rand.nextInt(RANDOM_HOURS_BOUND);
		int minutes = rand.nextInt(RANDOM_MINUTES_BOUND);

		TimeSpan ts1 = new TimeSpan(hours, minutes);
		TimeSpan ts2 = new TimeSpan(hours, minutes);

		assertEquals(ts1.compareTo(ts2), 0);
	}

	@Test
	public void testRandom() {

		Random rand = new Random();
		int hoursFst = rand.nextInt(RANDOM_HOURS_BOUND);
		int minutesFst = rand.nextInt(RANDOM_MINUTES_BOUND);
		int hoursSnd = rand.nextInt(RANDOM_HOURS_BOUND);
		int minutesSnd = rand.nextInt(RANDOM_MINUTES_BOUND);

		TimeSpan ts1 = new TimeSpan(hoursFst, minutesFst);
		TimeSpan ts2 = new TimeSpan(hoursSnd, minutesSnd);

		if (hoursFst > hoursSnd) {
			assertEquals(ts1.compareTo(ts2), 1);
		} else if (hoursFst == hoursSnd) {
			assertEquals(ts1.compareTo(ts2), Integer.compare(minutesFst, minutesSnd));
		} else {
			assertEquals(ts1.compareTo(ts2), -1);
		}
	}
}
