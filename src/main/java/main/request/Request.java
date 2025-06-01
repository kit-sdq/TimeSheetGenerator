/* Licensed under MIT 2025. */
package main.request;

import lombok.Getter;

/**
 * Base class for an action a user requested through the command line arguments.
 * Contains only a request type, and cannot be instantiated outside of this
 * class.
 */
@Getter
public class Request {
	private final RequestType type;

	protected Request(RequestType type) {
		this.type = type;
	}
}
