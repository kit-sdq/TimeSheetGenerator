/* Licensed under MIT 2023-2024. */
package checker;

import java.io.Serial;

/**
 * A CheckerException is thrown by a {@link IChecker} instance when an
 * unexpected or insoluble error occurs.
 */
public class CheckerException extends Exception {

	/**
	 * Auto-generated serial version UID
	 */
	@Serial
	private static final long serialVersionUID = 4362647380313599066L;

	/**
	 * Constructs a new {@link CheckerException}.
	 * 
	 * @param message - message of the error that occurred.
	 */
	public CheckerException(String message) {
		super(message);
	}
}
