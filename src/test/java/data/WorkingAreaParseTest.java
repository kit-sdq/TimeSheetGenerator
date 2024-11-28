/* Licensed under MIT 2023-2024. */
package data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorkingAreaParseTest {

	@Test
	public void testValidUB_1() {
		//// Test values
		String toParse = "UB";

		//// WorkingArea initialization
		WorkingArea fromString = WorkingArea.parse(toParse);

		//// Assertions
		assertEquals(WorkingArea.UB, fromString);
	}

	@Test
	public void testValidUB_2() {
		//// Test values
		String toParse = "uB";

		//// WorkingArea initialization
		WorkingArea fromString = WorkingArea.parse(toParse);

		//// Assertions
		assertEquals(WorkingArea.UB, fromString);
	}

	@Test
	public void testValidUB_3() {
		//// Test values
		String toParse = "Ub";

		//// WorkingArea initialization
		WorkingArea fromString = WorkingArea.parse(toParse);

		//// Assertions
		assertEquals(WorkingArea.UB, fromString);
	}

	@Test
	public void testValidUB_4() {
		//// Test values
		String toParse = "ub";

		//// WorkingArea initialization
		WorkingArea fromString = WorkingArea.parse(toParse);

		//// Assertions
		assertEquals(WorkingArea.UB, fromString);
	}

	@Test
	public void testValidGF_1() {
		//// Test values
		String toParse = "GF";

		//// WorkingArea initialization
		WorkingArea fromString = WorkingArea.parse(toParse);

		//// Assertions
		assertEquals(WorkingArea.GF, fromString);
	}

	@Test
	public void testValidGF_2() {
		//// Test values
		String toParse = "gF";

		//// WorkingArea initialization
		WorkingArea fromString = WorkingArea.parse(toParse);

		//// Assertions
		assertEquals(WorkingArea.GF, fromString);
	}

	@Test
	public void testValidGF_3() {
		//// Test values
		String toParse = "Gf";

		//// WorkingArea initialization
		WorkingArea fromString = WorkingArea.parse(toParse);

		//// Assertions
		assertEquals(WorkingArea.GF, fromString);
	}

	@Test
	public void testValidGF_4() {
		//// Test values
		String toParse = "gf";

		//// WorkingArea initialization
		WorkingArea fromString = WorkingArea.parse(toParse);

		//// Assertions
		assertEquals(WorkingArea.GF, fromString);
	}

	@Test
	public void testInvalidSubstring_1() {
		//// Test values
		String toParse = "gfbutNotCorrect";

		//// WorkingArea initialization
		Assertions.assertThrows(IllegalArgumentException.class, () -> WorkingArea.parse(toParse));
	}

	@Test
	public void testInvalidSubstring_2() {
		//// Test values
		String toParse = "gf ";

		//// WorkingArea initialization
		Assertions.assertThrows(IllegalArgumentException.class, () -> WorkingArea.parse(toParse));
	}

	@Test
	public void testInvalidSubstring_3() {
		//// Test values
		String toParse = "gfub";

		//// WorkingArea initialization
		Assertions.assertThrows(IllegalArgumentException.class, () -> WorkingArea.parse(toParse));
	}

	@Test
	public void testInvalidEmpty() {
		//// Test values
		String toParse = "";

		//// WorkingArea initialization
		Assertions.assertThrows(IllegalArgumentException.class, () -> WorkingArea.parse(toParse));
	}

	@Test
	public void testInvalidSpace() {
		//// Test values
		String toParse = " ";

		//// WorkingArea initialization
		Assertions.assertThrows(IllegalArgumentException.class, () -> WorkingArea.parse(toParse));
	}
}
