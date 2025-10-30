/* Licensed under MIT 2025. */
package ui.json;

import io.FileController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * This class fetches default values for certain fields like the default file
 * name format for programming or the program newestVersion from an endpoint on
 * Internet.
 */
public final class DefaultsFetcher {

	private static final String ENDPOINT_DEFAULT_VALUES = "https://kit.api.justonedev.net/api/prog/timesheetgen/defaults";

	// Approx. < 300 byte of data, should be fine for slow connections, but since
	// we're fetching during startup
	// in the main thread, we don't want to spend more than 2.5 seconds on this.
	// On my pc with good connection, in the IDE, it takes about 1 second.
	private static final int MAX_CONNECTION_TIME = 1250;
	private static final int MAX_READ_TIME = 1100;

	private DefaultsFetcher() {
	}

	public static Optional<String> fetchJSONFromEndpoint() {
		try {
			return Optional.of(readJSONStringFromAddress());
		} catch (URISyntaxException | IOException e) {
			return Optional.empty();
		}
	}

	private static String readJSONStringFromAddress() throws URISyntaxException, IOException {
		return FileController.readURLToString(new URI(ENDPOINT_DEFAULT_VALUES).toURL(), MAX_CONNECTION_TIME, MAX_READ_TIME);
	}

}
