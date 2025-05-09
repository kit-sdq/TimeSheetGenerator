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
	private int monthNr;

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

	public int getMonth() {
		return monthNr;
	}

	public void setMonth(int month) {
		this.monthNr = month;
	}
}
