package ui.json;

import io.FileController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class fetches default values for certain fields like the default file name format for programming or the program newestVersion from an endpoint on Internet.
 */
public class DefaultsFetcher {

    private static final String ENDPOINT_DEFAULT_VALUES = "https://kit.api.justonedev.net/api/prog/timesheetgen/defaults";

    public static DefaultsData fetch() {
        return new DefaultsData("", "", "", "", "");
    }

    private static String readJSONStringFromAddress() throws URISyntaxException, IOException {

        return FileController.readURLToString(new URI(ENDPOINT_DEFAULT_VALUES).toURL());
    }

}
