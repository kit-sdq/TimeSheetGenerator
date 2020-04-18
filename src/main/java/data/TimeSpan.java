package data;

import i18n.ResourceHandler;

/**
 * An immutable time span consisting of hours and minutes as well as basic arithmetic for it.
 * The time span may be positive or negative, but can only be created as positive.
 * To create a negative time span use the negate method as shown in this example:
 * <br><br>
 * <code>
 * TimeSpan ts = new TimeSpan(2, 15).negate();
 * </code>
 */
public class TimeSpan extends Time {
    
    /**
     * Minimum number of minutes.
     */
    public static final int MIN_MINUTE = 0;
    /**
     * Maxmimum number of minutes.
     */
    public static final int MAX_MINUTE = 59;
    
    /**
     * Constructs a new TimeSpan instance from a total number of minutes.
     * @param totalMinutes - Total number of minutes.
     */
    public TimeSpan(int totalMinutes) {
        super(totalMinutes);
    }
    
    /**
     * Constructs a new TimeSpan instance.
     * @param hour - Number of hours
     * @param minute - Number of minutes between 0 and 59
     */
    public TimeSpan(int hour, int minute) {
        super(hour, minute, true);
        
        if (minute < MIN_MINUTE || minute > MAX_MINUTE) {
            throw new IllegalArgumentException(ResourceHandler.getMessage("error.time.minuteOutOfBounds", MIN_MINUTE, MAX_MINUTE));
        }
    }
    
    /**
     * Get a positive TimeSpan with the same number of hours and minutes.
     * @return Absolute TimeSpan.
     */
    public TimeSpan abs() {
        return new TimeSpan(Math.abs(getTotalMinutes()));
    }

    /**
     * Get the sum of two TimeSpans.
     * @param addend - TimeSpan to be added.
     * @return Sum of the two TimeSpans.
     */
    public TimeSpan add(TimeSpan addend) {
        return new TimeSpan(getTotalMinutes() + addend.getTotalMinutes());
    }
    
    /**
     * Get a TimeSpan with the same number of hours and minutes but with the opposite sign.
     * @return Negated TimeSpan.
     */
    public TimeSpan negate() {
        return new TimeSpan(-getTotalMinutes());
    }

    /**
     * Get the difference between two TimeSpans.
     * @param subtrahend - TimeSpan to be subtracted.
     * @return Difference between the TimeSpans.
     */
    public TimeSpan subtract(TimeSpan subtrahend) {
        return new TimeSpan(getTotalMinutes() - subtrahend.getTotalMinutes());
    }

    /**
     * Parse a TimeSpan from a string. The format is defined in the i18n files with the key "locale.time.parseRegex".
     * @param string - String representation of the TimeSpan.
     * @return Parsed TimeSpan.
     */
    public static TimeSpan parse(String string) {
        return parse(string, (totalMinutes) -> new TimeSpan(totalMinutes));
    }

}
