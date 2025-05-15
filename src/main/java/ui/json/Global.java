/* Licensed under MIT 2024-2025. */
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
		return "%s, %s".formatted(getLastname(), getFirstname());
	}

	/**
	 * Formats the name, e.g. "Firstname Middlename Lastname", to be
	 * "Firstname middle" by splitting the name at the spaces and removing
	 * the last String, as this is probably the lastname. Everything else is
	 * interpreted as first- and middle names. <br/>
	 * For separated by underscores instead of spaces, see {@link Global#getFirstnameUnderscoreFormat()}
	 * This is used when determining the filename when exporting to a PDF. <br/>
	 *
	 * If the name does not contain any spaces, returns the regular name.
	 *
	 * @return the firstname of the employee, separated by spaces.
	 */
	@JsonIgnore
	public String getFirstname() {
		if (!getName().contains(" ")) {
			return getName();
		}

		String[] nameParts = getName().split(" ");
		String[] onlyFirstnameParts = new String[nameParts.length - 1];
		System.arraycopy(nameParts, 0, onlyFirstnameParts, 0, onlyFirstnameParts.length);

		return String.join(" ", onlyFirstnameParts);
	}


	/**
	 * Formats the name, e.g. "Firstname Middlename Lastname", to be
	 * "Firstname middle" by splitting the name at the spaces and removing
	 * the last String, as this is probably the lastname. Everything else is
	 * interpreted as first- and middle names. <br/>
	 *
	 * For separated by spaces instead of underscores, see {@link Global#getFirstname()}
	 * This is used when determining the filename when exporting to a PDF. <br/>
	 *
	 * <p>
	 *     This method literally does {@code Global#getFirstname().replace(' ', '_')}.
	 * </p>
	 *
	 * If the name does not contain any spaces, returns the regular name.
	 *
	 * @return the firstname of the employee, separated by underscores.
	 */
	@JsonIgnore
	public String getFirstnameUnderscoreFormat() {
		return getFirstname().replace(' ', '_');
	}

	/**
	 * Gets the lastname of the employee. <br/>
	 * This method formats the name, e.g. "Firstname Middlename Lastname", to be
	 * "Lastname" by splitting the name at the spaces and returning the last
	 * String. Everything else is interpreted as first- and middle names. <br/>
	 *
	 * If the name does not contain any spaces, returns the regular name.
	 *
	 * @return the lastname of the employee.
	 */
	@JsonIgnore
	public String getLastname() {
		if (!getName().contains(" ")) {
			return getName();
		}
		String[] nameParts = getName().split(" ");
		return nameParts[nameParts.length - 1];
	}
}
