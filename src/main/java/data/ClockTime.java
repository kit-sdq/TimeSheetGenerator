package data;

import i18n.ResourceHandler;

/**
 * Immutable clock time consisting of hours and minutes in the 24-hour format.
 */
public class ClockTime extends Time {

    /**
     * Minimum number of hours.
     */
    public static final int MIN_HOUR = 0;
    /**
     * Maximum number of hours.
     */
    public static final int MAX_HOUR = 23;
    /**
     * Minimum number of minutes.
     */
    public static final int MIN_MINUTE = 0;
    /**
     * Maximum number of minutes.
     */
    public static final int MAX_MINUTE = 59;
    
    /**
     * Create a new clock time.
     * @param totalMinutes Total number of minutes between 0 and 24 * 60 - 1.
     */
    private ClockTime(int totalMinutes) {
        super(totalMinutes);
        
        if (totalMinutes < 0 || totalMinutes > MAX_HOUR * Time.MINUTES_PER_HOUR + MAX_MINUTE) {
            throw new IllegalArgumentException(ResourceHandler.getMessage("error.time.timeOutOfDayBound"));
        }
    }
    
    /**
     * Create a new clock time.
     * @param hours
     */
    public ClockTime(int hours, int minutes) {
        super(hours, minutes, true);
        
        if (hours < MIN_HOUR || hours > MAX_HOUR) {
            throw new IllegalArgumentException(ResourceHandler.getMessage("error.time.hourOutOfBounds", MIN_HOUR, MAX_HOUR));
        }
        if (minutes < MIN_MINUTE || minutes > MAX_MINUTE) {
            throw new IllegalArgumentException(ResourceHandler.getMessage("error.time.minuteOutOfBounds", MIN_MINUTE, MAX_MINUTE));
        }
    }
    
    /**
     * Calculate the difference to another ClockTime.
     * The difference is positive if this ClockTime is after the other ClockTime.
     * @return Difference between the ClockTimes.
     */
    public TimeSpan differenceTo(ClockTime other) {
        return new TimeSpan(getTotalMinutes() - other.getTotalMinutes());
    }
    
    /**
     * Get if this ClockTime is after another ClockTime.
     * @return True if this ClockTime is after the other ClockTime.
     */
    public boolean isAfter(ClockTime other) {
        return getTotalMinutes() > other.getTotalMinutes();
    }
    
    /**
     * Get if this ClockTime is before another ClockTime.
     * @return True if this ClockTime is before the other ClockTime.
     */
    public boolean isBefore(ClockTime other) {
        return getTotalMinutes() < other.getTotalMinutes();
    }
    
    /**
     * Add the TimeSpan to this ClockTime.
     * 
     * @param timeSpan The TimeSpan to add.
     * @return Sum of this ClockTime and the TimeSpan.
     * @throws IllegalArgumentException When the resulting ClockTime would be on a different day.
     */
    public ClockTime add(TimeSpan timeSpan) {
        return new ClockTime(getTotalMinutes() + timeSpan.getTotalMinutes());
    }
    
    /**
     * Subtract the TimeSpan from this ClockTime.
     * 
     * @param timeSpan The TimeSpan to subtract.
     * @return Resulting ClockTime after the TimeSpan has been subtracted.
     * @throws IllegalArgumentException When the resulting ClockTime would be on a different day.
     */
    public ClockTime subtract(TimeSpan timeSpan) {
        return new ClockTime(getTotalMinutes() - timeSpan.getTotalMinutes());
    }
    
    /**
     * Parse a ClockTime from a string. The format is defined in the i18n files with the key "locale.time.parseRegex".
     * @param string String representation of the ClockTime.
     * @return Parsed ClockTime.
     */
    public static ClockTime parse(String string) {
        return parse(string, totalMinutes -> new ClockTime(totalMinutes));
    }

}
