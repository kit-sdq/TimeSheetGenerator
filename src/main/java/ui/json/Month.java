/* Licensed under MIT 2024. */
package ui.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonSerialize(using = MonthSerializer.class)
@Getter
@Setter
public class Month {

    @JsonProperty("$schema")
	private String schema;

	private int year;

	@JsonProperty("month")
	private int month;

	@JsonProperty("pred_transfer")
	private String predTransfer;

	@JsonProperty("succ_transfer")
	private String succTransfer;
	private List<Entry> entries;

	// Constructors, Getters, and Setters
	public Month() {
		schema = "https://raw.githubusercontent.com/kit-sdq/TimeSheetGenerator/master/examples/schemas/month.json";
	}

	// Nested class for individual entries
	@Setter
    @Getter
    @JsonSerialize(using = EntrySerializer.class)
	public static class Entry {
		private String action;
		private int day;
		private String start;
		private String end;
		private String pause;
		private boolean vacation;
    }

	// Getters and Setters for Month class fields

	public String getGermanName() {
		return switch (month) {
			case 1 -> "Januar";
			case 2 -> "Februar";
			case 3 -> "März";
			case 4 -> "April";
			case 5 -> "Mai";
			case 6 -> "Juni";
			case 7 -> "Juli";
			case 8 -> "August";
			case 9 -> "September";
			case 10 -> "Oktober";
			case 11 -> "November";
			case 12 -> "Dezember";
			default -> "null";
		};
	}
}
