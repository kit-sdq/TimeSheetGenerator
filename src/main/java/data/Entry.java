package data;

import java.time.LocalDate;

/**
 * An entry represents a continuous work interval of an {@link Employee}.
 */
public class Entry {
    private String action;
    private LocalDate date;
    private TimeSpan start, end, pause;

    /**
     * Constructs a new instance of {@link Entry}
     * @param action - the activity or title of the work done in this period of time
     * @param date - the date on which this {@link Entry} took place
     * @param start - the starting time of the work interval
     * @param end - the end time of the work interval
     * @param pause - the breaks taken in this work interval
     */
    public Entry(String action, LocalDate date, TimeSpan start, TimeSpan end, TimeSpan pause) {
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

    /**
     * Gets the action of an {@link Entry}.
     * @return The action.
     */
    public String getAction() {
        return action;
    }

    /**
     * Gets the date of an {@link Entry}.
     * @return The date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Gets the start of an {@link Entry}.
     * @return The start.
     */
    public TimeSpan getStart() {
        return start;
    }

    /**
     * Gets the end of an {@link Entry}.
     * @return The end.
     */
    public TimeSpan getEnd() {
        return end;
    }

    /**
     * Gets the pause of an {@link Entry}.
     * @return The pause.
     */
    public TimeSpan getPause() {
        return pause;
    }

    /**
     * Calculates the working time.
     * Working time defines the difference between start time and end time without break time.
     * @return The working time
     */
    public TimeSpan getWorkingTime() {
        TimeSpan workingTime = this.getEnd();
        
        workingTime = workingTime.subtract(this.getStart());
        workingTime = workingTime.subtract(this.getPause());
        
        return workingTime;
    }
    
}
