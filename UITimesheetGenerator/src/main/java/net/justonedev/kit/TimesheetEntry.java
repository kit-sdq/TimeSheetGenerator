/*
 * Licensed under the MIT License (MIT)
 * Copyright (c) 2024 Benjamin Claus
 */
package net.justonedev.kit;

import net.justonedev.kit.json.Month;

import java.time.Duration;
import java.time.LocalTime;

public class TimesheetEntry {

    public static final String TIMESHEET_FORMAT_HEADER = "            %-55s %-15s %-20s %-20s %-20s %-20s %-20s"; //"%-20s %-10s %-10s %-10s %-10s";
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

    public TimesheetEntry(Month.Entry entry)
    {
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
    public TimesheetEntry clone() {
        return new TimesheetEntry(
                activity, day, fromHour, fromMinute,
                toHour, toMinute, breakHour, breakMinutes, isVacation
        );
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

    public String toHtmlString() {
        return String.format(
                "<html>"
                        + "<table width='100%%' cellpadding='0' cellspacing='0'>"
                        + "<tr>"
                        + "<td width='360' style='text-align:left; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;'>%s</td>"
                        + "<td width='105' style='text-align:left;'>%s</td>"
                        + "<td width='133' style='text-align:left;'>%s</td>"
                        + "<td width='145' style='text-align:left;'>%s</td>"
                        + "<td width='105' style='text-align:left;'>%s</td>"
                        + "<td width='100' style='text-align:center;'>%s</td>"
                        + "<td width='140' style='text-align:right;'>%s</td>"
                        + "</tr>"
                        + "</table>"
                        + "</html>",
                getActivity(),
                getDayString() + ".",
                getStartTimeString(),
                getEndTimeString(),
                getBreakTimeString(),
                isVacationStr(),
                getTotalTimeWorkedString()
        );
    }

    /*
    This works for the following header:
    "         %-40s %-10s %-25s %-25s %-25s %-25s %-25s"
                "<html>"
                        + "<table width='100%%' cellpadding='0' cellspacing='0'>"
                        + "<tr>"
                        + "<td width='270' style='text-align:left; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;'>%s</td>"
                        + "<td width='80' style='text-align:left;'>%s</td>"
                        + "<td width='156' style='text-align:left;'>%s</td>"
                        + "<td width='170' style='text-align:left;'>%s</td>"
                        + "<td width='170' style='text-align:left;'>%s</td>"
                        + "<td width='180' style='text-align:left;'>%s</td>"
                        + "<td width='100' style='text-align:left;'>%s</td>"
                        + "</tr>"
                        + "</table>"
                        + "</html>",

                        Problem: Activity field not wide enough

    * */

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
