package ui.json.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class Preset {
    @JsonProperty("id")
    private String presetId;
    @JsonProperty("name")
    private String displayName;
    @JsonProperty("visible")
    private boolean isVisible;
    @JsonProperty("description")
    private String description;
    @JsonProperty("fileFormat")
    private String fileFormat;
    @JsonProperty("department")
    private String department;
    @JsonProperty("mailSubject")
    private String mailSubject;
    @JsonProperty("mailRecipient")
    private String mailRecipient;
    @JsonProperty("mailRecipientCC")
    private List<String> mailRecipientsCC;
}
