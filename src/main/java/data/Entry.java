package data;

import java.time.LocalDate;

import i18n.ResourceHandler;

/**
 * An entry represents a continuous work interval of an {@link Employee}.
 */
public class Entry implements Comparable<Entry> {
    
    private int MAX_HOUR_PER_DAY = 23;
    
    private final String action;
    private final LocalDate date;
    private final TimeSpan start, end, pause;
    private final boolean vacation;

    /**
     * Constructs a new instance of {@link Entry}
     * @param action - the activity or title of the work done in this period of time
     * @param date - the date on which this {@link Entry} took place
     * @param start - the starting time of the work interval
     * @param end - the end time of the work interval
     * @param pause - the breaks taken in this work interval
     * @param vacation - if the entry is a vacation entry (may not contain a pause if true)
     */
    public Entry(String action, LocalDate date, TimeSpan start, TimeSpan end, TimeSpan pause, boolean vacation) {
        if (start.getHour() > MAX_HOUR_PER_DAY || end.getHour() > MAX_HOUR_PER_DAY) {
            throw new IllegalArgumentException(ResourceHandler.getMessage("error.entry.timeOverUpperLimit"));
        } else if (end.compareTo(start) < 0) {
            throw new IllegalArgumentException(ResourceHandler.getMessage("error.entry.startGreaterThanEnd"));
        }
        
        if (!pause.equals(new TimeSpan(0, 0)) && vacation) {
            throw new IllegalArgumentException("Vacation entries may not contain a pause.");
        }
        
        this.action = action;
        this.date = date;
        this.start = start;
        this.end = end;
        this.pause = pause;
        this.vacation = vacation;
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
     * If the entry is a vacation entry.
     * @return True if the entry represents vacation time, False otherwise.
     */
    public boolean isVacation() {
        return vacation;
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

    /**
     * Compare by date and start time.
     */
    @Override
    public int compareTo(Entry o) {
        if (this.date.compareTo(o.getDate()) != 0) {
            return this.date.compareTo(o.getDate());
        } else {
            return this.start.compareTo(o.getStart());
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Entry)) {
            return false;
        }

        Entry otherEntry = (Entry)other;
        if (!this.action.equals(otherEntry.action)) {
            return false;
        } else if (!this.date.equals(otherEntry.date)) {
            return false;
        } else if (!this.start.equals(otherEntry.start)) {
            return false;
        } else if (!this.end.equals(otherEntry.end)) {
            return false;
        } else if (!this.pause.equals(otherEntry.pause)) {
            return false;
        } else if (this.vacation != otherEntry.vacation) {
            return false;
        } else {
            return true;
        }
    }

}
