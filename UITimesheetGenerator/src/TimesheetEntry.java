import java.time.LocalTime;

public class TimesheetEntry {

    public static final String TIMESHEET_FORMAT_HEADER = "         %-40s %-25s %-25s %-25s %-10s"; //"%-20s %-10s %-10s %-10s %-10s";
    public static final String TIMESHEET_FORMAT = " %-40s         %-25s %-25s %-25s %-10s"; //"%-20s %-10s %-10s %-10s %-10s";

    public static final TimesheetEntry EMPTY_ENTRY = new TimesheetEntry(
            "", -1, -1, -1, -1, -1, -1, false
    );

    private String activity;
    private int fromHour, fromMinute;
    private int toHour, toMinute;
    private int breakHour, breakMinutes;
    private boolean isVacation;

    public static TimesheetEntry generateTimesheetEntry(String activity, String startText, String endText, String breakText, boolean isVacation) {
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
        return new TimesheetEntry(activity, fromHour, fromMinute, toHour, toMinute, breakHour, breakMinutes, isVacation);
    }

    public TimesheetEntry(String activity,
                          int fromHour, int fromMinute,
                          int toHour, int toMinute,
                          int breakHour, int breakMinutes,
                          boolean isVacation)
    {
        this.activity = activity.trim();
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

    public boolean isVacation() {
        return isVacation;
    }

    public String isVacationStr() {
        return isVacation ? "yes" : "no";
    }

    public boolean isEmpty() {
        return fromHour == -1 && fromMinute == -1 && toHour == -1 && toMinute == -1;
    }

    @Override
    public String toString() {
        return String.format(TIMESHEET_FORMAT,
                getActivity(),
                getStartTimeString(),
                getEndTimeString(),
                getBreakTimeString(),
                isVacationStr());
    }
}
