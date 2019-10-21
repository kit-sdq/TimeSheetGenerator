package data;

import java.util.Date;

/**
 * @author Liam Wachter
 */
public class Entry {
    private String action;
    private Date date;
    private TimeSpan start, end, pause;

    public Entry(String action, Date date, TimeSpan start, TimeSpan end, TimeSpan pause) {
        this.action = action;
        this.date = date;
        this.start = start;
        this.end = end;
        this.pause = pause;
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
        // TODO calculate this, it's bad to store duplicate data
        return null;
    }
}
