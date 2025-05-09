/* Licensed under MIT 2024. */
package ui.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Global Settings stored in AppData files.
 */
@Getter
@Setter
public class Global {
	@JsonProperty("$schema")
	private String schema;
	private String name;
	private int staffId;
	private String department;
	private String workingTime;
	private double wage;
	private String workingArea;

	public Global() {
		schema = "https://raw.githubusercontent.com/kit-sdq/TimeSheetGenerator/main/examples/schemas/global.json";
	}

	public Global(Global global) {
		this();
		this.name = global.name;
		this.staffId = global.staffId;
		this.department = global.department;
		this.workingTime = global.workingTime;
		this.wage = global.wage;
		this.workingArea = global.workingArea;
	}

	// Constructors, Getters, and Setters

	/**
	 * Formats the name, e.g. "Firstname-middle Lastname", to be "Lastname,
	 * Firstname-middle" by splitting the name at the spaces and rotating the last
	 * String to the front. Everything else is interpreted as first- and middle
	 * names. <br/>
	 * If the name does not contain any spaces, returns the regular name.
	 *
	 * @return The name, formatted to start with the lastname.
	 */
	@JsonIgnore
	public String getNameFormalFormat() {
		if (!getName().contains(" ")) {
			return getName();
		}

		StringBuilder formattedName = new StringBuilder();
		String[] nameParts = getName().split(" ");

		formattedName.append(nameParts[nameParts.length - 1]).append(",");
		for (int i = 0; i < nameParts.length - 1; ++i) {
			formattedName.append(" ").append(nameParts[i]);
		}

		return formattedName.toString();
	}

	/**
	 * Formats the name, e.g. "Firstname-middle Lastname", to be
	 * "Lastname_Firstname-middle" by splitting the name at the spaces and rotating
	 * the last String to the front. Everything else is interpreted as first- and
	 * middle names. <br/>
	 * This method is similar to {@link Global#getNameFormalFormat()}, but it
	 * separates the formatted name using underscores, and does not add a comma
	 * after the lastname. <br/>
	 * This is used when determining the filename when exporting to a PDF. <br/>
	 * If the name does not contain any spaces, returns the regular name.
	 *
	 * @return Like {@link Global#getNameFormalFormat()}, but with underscores and
	 *         no comma.
	 */
	@JsonIgnore
	public String getNameUnderscoreFormat() {
		if (!getName().contains(" ")) {
			return getName();
		}

		StringBuilder formattedName = new StringBuilder();
		String[] nameParts = getName().split(" ");

		formattedName.append(nameParts[nameParts.length - 1]);
		for (int i = 0; i < nameParts.length - 1; ++i) {
			formattedName.append("_").append(nameParts[i]);
		}

		return formattedName.toString();
	}
}
