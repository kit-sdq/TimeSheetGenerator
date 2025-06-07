/* Licensed under MIT 2025. */
package main.request;

/**
 * A help request. The Request to be returned if the user has requested
 * help.<br/>
 * The {@code getType()} method will return {@link RequestType#HELP}.
 */
public class HelpRequest extends Request {
	public HelpRequest() {
		super(RequestType.HELP);
	}
}
