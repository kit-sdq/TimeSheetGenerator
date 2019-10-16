package data;

import java.util.Date;

public class Entry {
    private String action;
    private Date date;
    private TimeSpan start, end, pause, workingTime;

    public Entry(String action, Date date, TimeSpan start, TimeSpan end, TimeSpan pause, TimeSpan workingTime) {
        this.action = action;
        this.date = date;
        this.start = start;
        this.end = end;
        this.pause = pause;
        this.workingTime = workingTime;
    }

    public String getAction() {
        return action;
    }

    public Date getDate() {
        return date;
    }

    public TimeSpan getStart() {
        return start;
    }

    public TimeSpan getEnd() {
        return end;
    }

    public TimeSpan getPause() {
        return pause;
    }

    public TimeSpan getWorkingTime() {
        return workingTime;
    }
}
