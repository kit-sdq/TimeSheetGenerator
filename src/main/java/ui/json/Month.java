/* Licensed under MIT 2024-2025. */
package ui.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonSerialize(using = MonthSerializer.class)
public class Month {

	@JsonProperty("$schema")
	@Getter
	@Setter
	private String schema;

	@Getter
	@Setter
	private int year;

	@JsonProperty("month")
	private int monthNr;

	@JsonProperty("pred_transfer")
	@Getter
	@Setter
	private String predTransfer;

	@JsonProperty("succ_transfer")
	@Getter
	@Setter
	private String succTransfer;

	@Getter
	@Setter
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

	public void setMonth(int month) {
		this.monthNr = month;
	}

	public int getMonth() {
		return monthNr;
	}

	public String getGermanName() {
		return switch (monthNr) {
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
