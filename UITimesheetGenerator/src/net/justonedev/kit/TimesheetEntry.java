package net.justonedev.kit;

import java.time.LocalTime;

public class TimesheetEntry {

    public static final String TIMESHEET_FORMAT_HEADER = "         %-40s %-10s %-25s %-25s %-25s %-25s %-25s"; //"%-20s %-10s %-10s %-10s %-10s";
    public static final String TIMESHEET_FORMAT = " %-40s      %-10s %-25s %-25s %-25s %-25s %-25s"; //"%-20s %-10s %-10s %-10s %-10s";
    public static final String COMPRESSED_TIMESHEET_FORMAT = "%s, %s. %s - %s, Break: %s, Vacation: %s";

    public static final TimesheetEntry EMPTY_ENTRY = new TimesheetEntry(
            "", -1, -1, -1, -1, -1, -1, -1, false
    );

    private String activity;
    private int day;
    private int fromHour, fromMinute;
    private int toHour, toMinute;
    private int breakHour, breakMinutes;
    private boolean isVacation;

    public static TimesheetEntry generateTimesheetEntry(String activity, int day, String startText, String endText, String breakText, boolean isVacation) {
        LocalTime startTime = DialogHelper.parseTime(startText);
        LocalTime endTime = DialogHelper.parseTime(endText);
        LocalTime breakTime = DialogHelper.parseTime(breakText);

        int fromHour = -1, fromMinute = -1, toHour = -1, toMinute = -1, breakHour = -1, breakMinutes = -1;

        if (startTime != null && endTime != null && breakTime != null) {
            fromHour = startTime.getHour();
            fromMinute = startTime.getMinute();
            toHour = endTime.getHour();
            toMinute = endTime.getMinute();
            breakHour = breakTime.getHour();
            breakMinutes = breakTime.getMinute();
        }
        return new TimesheetEntry(activity, day, fromHour, fromMinute, toHour, toMinute, breakHour, breakMinutes, isVacation);
    }

    public TimesheetEntry(String activity, int day,
                          int fromHour, int fromMinute,
                          int toHour, int toMinute,
                          int breakHour, int breakMinutes,
                          boolean isVacation)
    {
        this.activity = activity.trim();
        this.day = day;
        this.fromHour = fromHour;
        this.fromMinute = fromMinute;
        this.toHour = toHour;
        this.toMinute = toMinute;
        this.breakHour = breakHour;
        this.breakMinutes = breakMinutes;
        this.isVacation = isVacation;
    }

    public String getActivity() {
        return activity;
    }

    public String getDayString() {
        if (day == -1) return "";
        return String.format("%d", day);
    }

    public String getStartTimeString() {
        if (fromHour == -1) return "";
        return String.format("%02d:%02d", fromHour, fromMinute);
    }

    public String getEndTimeString() {
        if (fromHour == -1) return "";
        return String.format("%02d:%02d", toHour, toMinute);
    }

    public String getBreakTimeString() {
        if (fromHour == -1) return "";
        return String.format("%02d:%02d", breakHour, breakMinutes);
    }

    public String getTotalTimeWorkedString() {
        if (fromHour == -1) return "";
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
        return String.format("%02d:%02d", hoursWorked, minutesWorked);
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
        if (this.day < other.day) return false;
        if (this.day > other.day) return true;
        if (this.fromHour < other.fromHour) return false;
        if (this.fromHour > other.fromHour) return true;
        if (this.fromMinute < other.fromMinute) return false;
        if (this.fromMinute > other.fromMinute) return true;
        // If none of those, select the one that ends later is later
        if (this.toHour < other.toHour) return false;
        if (this.toHour > other.toHour) return true;
        // Return true if this.toMinute >= other.toMinute
        return this.toMinute >= other.toMinute;
    }

    @Override
    public String toString() {
        return String.format(TIMESHEET_FORMAT,
                getActivity(),
                getDayString() + ".",
                getStartTimeString(),
                getEndTimeString(),
                getBreakTimeString(),
                isVacationStr(),
                getTotalTimeWorkedString());
    }

    public String toShortString() {
        return String.format(COMPRESSED_TIMESHEET_FORMAT,
                getActivity(),
                getDayString(),
                getStartTimeString(),
                getEndTimeString(),
                getBreakTimeString(),
                isVacationStr());
    }
}
