package data;

import java.util.Date;

/**
 * @author Liam Wachter
 */
public class Entry {
    private String action;
    private Date date;
    private TimeSpan start, end, pause;

    //TODO Document
    
    //TODO Validity check - working time less than zero case
    public Entry(String action, Date date, TimeSpan start, TimeSpan end, TimeSpan pause) {
        if (start.getHour() > 23 || end.getHour() > 23) {
            throw new IllegalArgumentException("Start and end time may not be greater than 23:59.");
        } else if (end.compareTo(start) < 0) {
            throw new IllegalArgumentException("Start time may not be greater than end time.");
        }
        
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

    /**
     * Calculates the working time.
     * Working time defines the difference between start time and end time without break time.
     * @return The working time
     */
    public TimeSpan getWorkingTime() {
        TimeSpan workingTime = new TimeSpan(this.getEnd().getHour(), this.getEnd().getMinute());
        workingTime.subtract(this.getStart());
        workingTime.subtract(this.getPause());
        
        return workingTime;
    }
}
