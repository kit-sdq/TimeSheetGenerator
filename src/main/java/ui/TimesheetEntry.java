/* Licensed under MIT 2024. */
package ui;

import ui.json.Month;

import java.time.Duration;
import java.time.LocalTime;

public class TimesheetEntry {

	public static final String TIMESHEET_FORMAT_HEADER = "            %-55s %-15s %-20s %-20s %-20s %-20s %-20s";
	public static final String TIMESHEET_FORMAT = " %-40s      %-10s %-25s %-25s %-25s %-25s %-25s";
	public static final String COMPRESSED_TIMESHEET_FORMAT = "%s, %s. %s - %s, Break: %s, Vacation: %s";

	private static final String TIME_FORMAT = "%02d:%02d";

	public static final TimesheetEntry EMPTY_ENTRY = new TimesheetEntry("", -1, Time.none(), Time.none(), Time.none(), false);

	private final String activity;
	private final int day;
	private final int fromHour;
	private final int fromMinute;
	private final int toHour;
	private final int toMinute;
	private final int breakHour;
	private final int breakMinutes;
	private final boolean isVacation;

	public static TimesheetEntry generateTimesheetEntry(String activity, int day, String startText, String endText, String breakText, boolean isVacation) {
		LocalTime startTime = DialogHelper.parseTime(startText);
		LocalTime endTime = DialogHelper.parseTime(endText);
		LocalTime breakTime = DialogHelper.parseTime(breakText);

		int fromHour = -1;
		int fromMinute = -1;
		int toHour = -1;
		int toMinute = -1;
		int breakHour = -1;
		int breakMinutes = -1;

		if (startTime != null && endTime != null && breakTime != null) {
			fromHour = startTime.getHour();
			fromMinute = startTime.getMinute();
			toHour = endTime.getHour();
			toMinute = endTime.getMinute();
			breakHour = breakTime.getHour();
			breakMinutes = breakTime.getMinute();
		}

		Time start = new Time(fromHour, fromMinute);
		Time end = new Time(toHour, toMinute);
		Time _break = new Time(breakHour, breakMinutes);

		return new TimesheetEntry(activity, day, start, end, _break, isVacation);
	}

	public TimesheetEntry(String activity, int day, Time startTime, Time endTime, Time breakTime, boolean isVacation) {
		this.activity = activity.trim();
		this.day = day;
		this.fromHour = startTime.getHours();
		this.fromMinute = startTime.getMinutes();
		this.toHour = endTime.getHours();
		this.toMinute = endTime.getMinutes();
		this.breakHour = breakTime.getHours();
		this.breakMinutes = breakTime.getMinutes();
		this.isVacation = isVacation;
	}

	public TimesheetEntry(TimesheetEntry copy) {
		this.activity = copy.activity;
		this.day = copy.day;
		this.fromHour = copy.fromHour;
		this.fromMinute = copy.fromMinute;
		this.toHour = copy.toHour;
		this.toMinute = copy.toMinute;
		this.breakHour = copy.breakHour;
		this.breakMinutes = copy.breakMinutes;
		this.isVacation = copy.isVacation;
	}

	public TimesheetEntry(Month.Entry entry) {
		this.activity = entry.getAction();
		this.day = entry.getDay();
		LocalTime time = DialogHelper.parseTime(entry.getStart());
		if (time != null) {
			this.fromHour = time.getHour();
			this.fromMinute = time.getMinute();
		} else {
			this.fromHour = 0;
			this.fromMinute = 0;
		}
		time = DialogHelper.parseTime(entry.getEnd());
		if (time != null) {
			this.toHour = time.getHour();
			this.toMinute = time.getMinute();
		} else {
			this.toHour = 0;
			this.toMinute = 0;
		}
		time = DialogHelper.parseTime(entry.getPause());
		if (time != null) {
			this.breakHour = time.getHour();
			this.breakMinutes = time.getMinute();
		} else {
			this.breakHour = 0;
			this.breakMinutes = 0;
		}
		this.isVacation = entry.isVacation();
	}

	public Month.Entry toMonthEntry() {
		Month.Entry entry = new Month.Entry();
		entry.setAction(activity);
		entry.setDay(day);
		entry.setStart(getStartTimeString());
		entry.setEnd(getEndTimeString());
		entry.setPause(getBreakTimeString());
		entry.setVacation(isVacation);
		return entry;
	}

	public String getActivity() {
		return activity;
	}

	public String getDayString() {
		if (day == -1)
			return "";
		return String.format("%02d", day);
	}

	public String getStartTimeString() {
		if (fromHour == -1)
			return "";
		return String.format(TIME_FORMAT, fromHour, fromMinute);
	}

	public String getEndTimeString() {
		if (fromHour == -1)
			return "";
		return String.format(TIME_FORMAT, toHour, toMinute);
	}

	public String getBreakTimeString() {
		if (fromHour == -1)
			return "";
		return String.format(TIME_FORMAT, breakHour, breakMinutes);
	}

	public String getTotalTimeWorkedString() {
		if (fromHour == -1)
			return "";
		int hoursWorked = toHour - fromHour - breakHour;
		int minutesWorked = toMinute - fromMinute - breakMinutes;
		while (minutesWorked > 60) {
			minutesWorked -= 60;
			hoursWorked++;
		}
		while (minutesWorked < 0) {
			minutesWorked += 60;
			hoursWorked--;
		}
		return String.format(TIME_FORMAT, hoursWorked, minutesWorked);
	}

	public Time getWorkedTime() {
		Duration workDuration = Duration.between(LocalTime.of(fromHour, fromMinute), LocalTime.of(toHour, toMinute))
				.minus(Duration.ofHours(breakHour).plusMinutes(breakMinutes));
		return new Time((int) workDuration.toHours(), (int) workDuration.toMinutes() % 60);
	}

	public boolean isVacation() {
		return isVacation;
	}

	public String isVacationStr() {
		return isVacation ? "yes" : "no";
	}

	public boolean isEmpty() {
		return fromHour == -1 && fromMinute == -1 && toHour == -1 && toMinute == -1;
	}

	public boolean isLaterThan(TimesheetEntry other) {
		if (this.day < other.day)
			return false;
		if (this.day > other.day)
			return true;
		if (this.fromHour < other.fromHour)
			return false;
		if (this.fromHour > other.fromHour)
			return true;
		if (this.fromMinute < other.fromMinute)
			return false;
		if (this.fromMinute > other.fromMinute)
			return true;
		// If none of those, select the one that ends later is later
		if (this.toHour < other.toHour)
			return false;
		if (this.toHour > other.toHour)
			return true;
		// Return true if this.toMinute >= other.toMinute
		return this.toMinute >= other.toMinute;
	}

	@Override
	public String toString() {
		return String.format(TIMESHEET_FORMAT, getActivity(), getDayString() + ".", getStartTimeString(), getEndTimeString(), getBreakTimeString(),
				isVacationStr(), getTotalTimeWorkedString());
	}

	public String toHtmlString() {
		return String.format("<html>" + "<table width='100%%' cellpadding='0' cellspacing='0'>" + "<tr>"
				+ "<td width='360' style='text-align:left; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;'>%s</td>"
				+ "<td width='105' style='text-align:left;'>%s</td>" + "<td width='133' style='text-align:left;'>%s</td>"
				+ "<td width='145' style='text-align:left;'>%s</td>" + "<td width='105' style='text-align:left;'>%s</td>"
				+ "<td width='100' style='text-align:center;'>%s</td>" + "<td width='140' style='text-align:right;'>%s</td>" + "</tr>" + "</table>" + "</html>",
				getActivity(), getDayString() + ".", getStartTimeString(), getEndTimeString(), getBreakTimeString(), isVacationStr(),
				getTotalTimeWorkedString());
	}

	public String toShortString() {
		return String.format(COMPRESSED_TIMESHEET_FORMAT, getActivity(), getDayString(), getStartTimeString(), getEndTimeString(), getBreakTimeString(),
				isVacationStr());
	}
}
