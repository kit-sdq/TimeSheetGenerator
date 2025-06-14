/* Licensed under MIT 2025. */
package main.request;

import lombok.Getter;

/**
 * A generation request. The Request to be returned if the user has requested to
 * generate a time sheet. In this case, the user can specify if they want to
 * explicitly generate vacation entries or not.<br/>
 * The {@code getType()} method will return {@link RequestType#GENERATE}.
 */
@Getter
public class GenerateRequest extends Request {
	private final boolean excludeVacationEntries;

	public GenerateRequest(boolean excludeVacationEntries) {
		super(RequestType.GENERATE);
		this.excludeVacationEntries = excludeVacationEntries;
	}
}
