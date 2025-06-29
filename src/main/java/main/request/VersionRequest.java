/* Licensed under MIT 2025. */
package main.request;

import lombok.Getter;

/**
 * A version request. The Request to be returned if the user has requested the
 * version.<br/>
 * The {@code getType()} method will return {@link RequestType#VERSION}.
 */
@Getter
public class VersionRequest extends Request {
	public VersionRequest() {
		super(RequestType.VERSION);
	}
}
