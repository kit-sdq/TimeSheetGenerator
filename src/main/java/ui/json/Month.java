/* Licensed under MIT 2024. */
package ui.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize(using = MonthSerializer.class)
public class Month {

	@JsonProperty("$schema")
	private String schema;

	private int year;
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
	@JsonSerialize(using = EntrySerializer.class)
	public static class Entry {
		private String action;
		private int day;
		private String start;
		private String end;
		private String pause;
		private boolean vacation;

		// Constructors, Getters, and Setters

		public Entry() {
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public int getDay() {
			return day;
		}

		public void setDay(int day) {
			this.day = day;
		}

		public String getStart() {
			return start;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public String getEnd() {
			return end;
		}

		public void setEnd(String end) {
			this.end = end;
		}

		public String getPause() {
			return pause;
		}

		public void setPause(String pause) {
			this.pause = pause;
		}

		public boolean isVacation() {
			return vacation;
		}

		public void setVacation(boolean vacation) {
			this.vacation = vacation;
		}
	}

	// Getters and Setters for Month class fields

	public String getSchema() {
		return schema;
	}

	public void setSchema(String action) {
		this.schema = schema;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getPredTransfer() {
		return predTransfer;
	}

	public void setPredTransfer(String predTransfer) {
		this.predTransfer = predTransfer;
	}

	public String getSuccTransfer() {
		return succTransfer;
	}

	public void setSuccTransfer(String succTransfer) {
		this.succTransfer = succTransfer;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
}
