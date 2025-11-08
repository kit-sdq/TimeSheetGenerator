/* Licensed under MIT 2025. */
package ui.json.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class Preset {
	public static final Preset NO_PRESET = new Preset("nopreset", "Choose preset...", true, "", "", "", "", "", List.of());

	@JsonProperty("id")
	private String presetId;
	@JsonProperty("name")
	private String displayName;
	@JsonProperty("visible")
	private boolean isVisible = true;
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

	@Override
	public String toString() {
		return displayName;
	}

	public Preset() {
		// Required default constructor
	}

	private Preset(String presetId, String displayName, boolean isVisible, String description, String fileFormat, String department, String mailSubject,
			String mailRecipient, List<String> mailRecipientsCC) {
		this.presetId = presetId;
		this.displayName = displayName;
		this.isVisible = isVisible;
		this.description = description;
		this.fileFormat = fileFormat;
		this.department = department;
		this.mailSubject = mailSubject;
		this.mailRecipient = mailRecipient;
		this.mailRecipientsCC = mailRecipientsCC;
	}
}
