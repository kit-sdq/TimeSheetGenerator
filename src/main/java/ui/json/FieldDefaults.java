/* Licensed under MIT 2025. */
package ui.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class and JSON object for all data that is fetched from the endpoint in
 * {@link DefaultsFetcher}.
 */
@Getter
@Setter
public class FieldDefaults {

	/**
	 * Default file name format for, as requested by KASTEL @ KIT.
	 * <p>
	 * Format Explained: - %LAST%: Last name of the user. - %FIRST_U%: First name of
	 * the user with underscores instead of spaces. - %MM%: Two-digit month (e.g.,
	 * 01 for January). - %YYYY%: Four-digit year (e.g., 2025).
	 * </p>
	 * <p>
	 * An example would be:<br/>
	 * Mustermann_Max_Tobias_04_2025.pdf<br/>
	 * </p>
	 */
	public static final String DEFAULT_PDF_NAME_FORMAT = "%LAST%_%FIRST_U%_%MM%_%YYYY%";

	@JsonProperty("version")
	private String newestVersion;
	@JsonProperty("fileFormatProg")
	private String defaultFilenameProg;
	@JsonProperty("fileFormatAlgo")
	private String defaultFilenameAlgo;
	@JsonProperty("emailTitleProg")
	private String defaultMailSubjectProg;
	@JsonProperty("emailTitleAlgo")
	private String defaultMailSubjectAlgo;
	@JsonProperty("emailReceiverProg")
	private String defaultMailReceiverProg;

	/**
	 * Creates a new object for all data that is fetched from the endpoint in
	 * {@link DefaultsFetcher}. Default constructor, assigns the values from
	 * {@link FieldDefaults#DEFAULT_VALUES}.
	 */
	public FieldDefaults() {
		this(DEFAULT_VALUES);
	}

	/**
	 * Creates a new object for all data that is fetched from the endpoint in
	 * {@link DefaultsFetcher}.
	 *
	 * @param newestVersion        The version of the newest available release of
	 *                             the TimesheetGenerator.
	 * @param defaultFilenameProg  The default timesheet filename format for
	 *                             Programming. May be different from Algo.
	 * @param defaultFilenameAlgo  The default timesheet filename format for Algo.
	 *                             May be different from Programming.
	 * @param defaultMailSubjectProg The default title format for the email sent to
	 *                             KASTEL with the timesheet.
	 * @param defaultMailSubjectAlgo The default title format for the email sent to
	 *                             Algo with the timesheet.
	 */
	public FieldDefaults(String newestVersion, String defaultFilenameProg, String defaultFilenameAlgo, String defaultMailSubjectProg,
			String defaultMailSubjectAlgo, String defaultMailReceiverProg) {
		this.newestVersion = newestVersion;
		this.defaultFilenameProg = defaultFilenameProg;
		this.defaultFilenameAlgo = defaultFilenameAlgo;
		this.defaultMailSubjectProg = defaultMailSubjectProg;
		this.defaultMailSubjectAlgo = defaultMailSubjectAlgo;
		this.defaultMailReceiverProg = defaultMailReceiverProg;
	}

	/**
	 * Creates a new object for all data that is fetched from the endpoint in
	 * {@link DefaultsFetcher}. Creates a copy of the given {@link FieldDefaults}.
	 *
	 * @param other The other FieldDefaults object to copy the values from.
	 */
	public FieldDefaults(FieldDefaults other) {
		this.newestVersion = other.newestVersion;
		this.defaultFilenameProg = other.defaultFilenameProg;
		this.defaultFilenameAlgo = other.defaultFilenameAlgo;
		this.defaultMailSubjectProg = other.defaultMailSubjectProg;
		this.defaultMailSubjectAlgo = other.defaultMailSubjectAlgo;
	}

	public static final FieldDefaults DEFAULT_VALUES = new FieldDefaults(System.getProperty("version"), DEFAULT_PDF_NAME_FORMAT, DEFAULT_PDF_NAME_FORMAT,
			"Stundenzettel %FIRST% %LAST% %YYYY%-%MM%", "Stundenzettel / Timesheet %MM_GER% / %MM_ENG% %YYYY% %FIRST% %LAST%", "programmieren-vorlesung@cs.kit.edu");

}
