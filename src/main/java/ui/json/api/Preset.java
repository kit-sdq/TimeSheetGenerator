/* Licensed under MIT 2025-2026. */
package ui.json.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import ui.Date;

import java.util.List;

@Getter
public class Preset {
	public static final Preset NO_PRESET = new Preset("nopreset", "Choose preset...", true, "", "", "", "");

	@JsonProperty("id")
	private String presetId;
	@JsonProperty("name")
	private String displayName;
	@JsonProperty("visible")
	private boolean isVisible = true;
	@JsonProperty("visibleFrom")
	private String visibleFrom = "";
	@JsonProperty("visibleUntil")
	private String visibleUntil = "";
	@JsonProperty("description")
	private String description = "";
	@JsonProperty("fileFormat")
	private String fileFormat = "";
	@JsonProperty("department")
	private String department = "";
	@JsonProperty("mailSubject")
	private String mailSubject = "";
	@JsonProperty("mailRecipient")
	private String mailRecipient = "";
	@JsonProperty("mailRecipientCC")
	private List<String> mailRecipientsCC = List.of();

	@JsonIgnore
	private Date visibleFromDate;
	@JsonIgnore
	private Date visibleUntilDate;

	@Override
	public String toString() {
		return displayName;
	}

	public Preset() {
		// Required default constructor
	}

	private Preset(String presetId, String displayName, boolean isVisible, String fileFormat, String department, String mailSubject, String mailRecipient) {
		this.presetId = presetId;
		this.displayName = displayName;
		this.isVisible = isVisible;
		this.description = "";
		this.fileFormat = fileFormat;
		this.department = department;
		this.mailSubject = mailSubject;
		this.mailRecipient = mailRecipient;
		this.mailRecipientsCC = List.of();
	}

	/**
	 * Returns if the Preset should be visible. This is a combination of if the
	 * isVisible field is true, and if the current Date is within visibleFrom and
	 * visibleUntil, or if the non-matching Date values are Zero (non-existent).
	 *
	 * @return True if the Preset should be visible, taking isVisible and from/until
	 *         into consideration.
	 */
	public boolean shouldBeVisible() {
		if (!isVisible())
			return false;
		Date currentDate = Date.currentLocalDate();
		if (!visibleFromDate.isZero() && currentDate.isEarlierThan(visibleFromDate)) {
			return false;
		}
		return visibleUntilDate.isZero() || !currentDate.isLaterThan(visibleUntilDate);
	}

	/**
	 * Parses the stored Dates in the presets. Executed manually after fields have
	 * been filled.
	 */
	public void parseDates() {
		visibleFromDate = Date.parseDate(visibleFrom);
		visibleUntilDate = Date.parseDate(visibleUntil);
	}
}
