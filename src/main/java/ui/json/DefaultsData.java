package ui.json;

/**
 * Record for all data that is fetched from the endpoint in {@link DefaultsFetcher}.
 * @param newestVersion The version of the newest available release of the TimesheetGenerator.
 * @param defaultFilenameProg The default timesheet filename format for Programming. May be different from Algo.
 * @param defaultFilenameAlgo The default timesheet filename format for Algo. May be different from Programming.
 * @param defaultMailTitleProg The default title format for the email sent to KASTEL with the timesheet.
 * @param defaultMailTitleAlgo The default title format for the email sent to Algo with the timesheet.
 */
public record DefaultsData(String newestVersion, String defaultFilenameProg, String defaultFilenameAlgo, String defaultMailTitleProg, String defaultMailTitleAlgo) {

    public static final DefaultsData DEFAULT_VALUES = new DefaultsData(
            "0",
            "",
            "",
            "",
            ""
    );

}
