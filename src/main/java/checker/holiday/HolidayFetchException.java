/* Licensed under MIT 2023-2024. */
package checker.holiday;

import java.io.Serial;

/**
 * This exception is thrown by an {@link IHolidayChecker} instance if an error
 * occurs while fetching the possible {@link Holiday holidays}.
 */
public class HolidayFetchException extends Exception {

	/**
	 * Auto-generated serialVersionUID
	 */
	@Serial
	private static final long serialVersionUID = -4763109415356430991L;

	/**
	 * Constructs a new {@link HolidayFetchException}.
	 * 
	 * @param message - message of the error that occurred.
	 */
	public HolidayFetchException(String message) {
		super(message);
	}
}
