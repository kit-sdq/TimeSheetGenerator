/* Licensed under MIT 2023-2024. */
package checker;

import data.TimeSheet;

/**
 * Represents the possible return values of the checker class.
 */
public enum CheckerReturn {
	/**
	 * Returned if the {@link TimeSheet} to be checked is invalid.
	 */
	INVALID,

	/**
	 * Returned if the {@link TimeSheet} to be checked is valid.
	 */
	VALID
}
