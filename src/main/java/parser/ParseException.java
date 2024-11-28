/* Licensed under MIT 2023-2024. */
package parser;

import java.io.Serial;

/**
 * Exception thrown by parser classes to signal that an error occurred while
 * parsing.
 */
public class ParseException extends Exception {

	@Serial
	private static final long serialVersionUID = -644519857973827281L;

	/**
	 * Constructs a new {@link ParseException}.
	 * 
	 * @param error - message of the error that occurred.
	 */
	public ParseException(String error) {
		super(error);
	}

}
