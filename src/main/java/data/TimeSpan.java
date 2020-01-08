package data;

/**
 * An immutable time span consisting of hours and minutes as well as basic arithmetic for it.
 */
public class TimeSpan implements Comparable<TimeSpan> {
    
    public static final int MIN_HOUR = 0;
    public static final int MIN_MINUTE = 0;
    public static final int MAX_MINUTE = 59;
    
    // TODO these might be locale specific
    private static final String PARSE_REGEX = "^[0-9]+:[0-5]?[0-9]$";
    private static final String HOUR_MINUTE_SEPARATOR = ":";
    private static final String STRING_FORMAT = "%02d:%02d";
    
    private final int minute;
    private final int hour;

    /**
     * Constructs a new TimeSpan instance.
     * @param hour - Non-negative amount of hours
     * @param minute - Number of minutes between 0 and 59
     */
    public TimeSpan(int hour, int minute) {
        if (hour < MIN_HOUR || minute < MIN_MINUTE) {
            throw new IllegalArgumentException("Hour and minute may not be negative.");
        } else if (minute > MAX_MINUTE) {
            throw new IllegalArgumentException(String.format("Minute may not be greater than %s.", MAX_MINUTE));
        }
        this.minute = minute;
        this.hour = hour;
    }

    /**
     * Gets the minutes of a TimeSpan.
     * @return - The minutes.
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Gets the hours of a TimeSpan.
     * @return - The hours.
     */
    public int getHour() {
        return hour;
    }

    /**
     * Sums up hours and minutes taking carryover into account.
     * @param addend - TimeSpan that should be added
     * @return The {@link TimeSpan} representing the sum
     */
    public TimeSpan add(TimeSpan addend) {
        int hourSum = this.hour + addend.getHour();
        int minuteSum = this.minute + addend.getMinute();
        int carry = minuteSum / (MAX_MINUTE + 1);
        
        return new TimeSpan(
            hourSum + carry,
            minuteSum % (MAX_MINUTE + 1)
        );
    }

    /**
     * Subtracts hours and minutes taking carryover into account.
     * @param subtrahend - TimeSpan that should be subtracted
     * @return The {@link TimeSpan} representing the difference
     * @throws IllegalArgumentException thrown if the subtrahend is greater than the minuend
     */
    public TimeSpan subtract(TimeSpan subtrahend) throws IllegalArgumentException {
        if (this.compareTo(subtrahend) < 0) {
            throw new IllegalArgumentException("Subtrahend may not be greater than Minuend.");
        }
        
        int hourDiff = this.hour - subtrahend.getHour();
        int minuteDiff = this.minute - subtrahend.getMinute();
        
        return new TimeSpan(
            hourDiff - ((MAX_MINUTE - minuteDiff) / (MAX_MINUTE + 1)),
            ((MAX_MINUTE + 1) + minuteDiff) % (MAX_MINUTE + 1)
        );
    }

    /**
     * Attempts to interpret a string as a representation of a {@link TimeSpan}.
     * @param s - the string to be parsed.
     * @return A {@link TimeSpan} representing the input string
     */
    public static TimeSpan parse(String s) {
        if (!s.matches(PARSE_REGEX)) {
            throw new IllegalArgumentException("Invalid time string. Usage: h...h:mm");
        }
        String[] splittedString = s.split(HOUR_MINUTE_SEPARATOR);
        
        int hours;
        int minutes;
        try {
            hours = Integer.parseInt(splittedString[0]);
            minutes = Integer.parseInt(splittedString[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        
        return new TimeSpan(hours, minutes);
    }
    
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, hour, minute);
    }

    @Override
    public int compareTo(TimeSpan other) {
        if (this.hour > other.getHour()) {
            return 1;
        } else if (this.hour == other.getHour()) {
            return Integer.compare(this.minute, other.getMinute());
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TimeSpan)) {
            return false;
        }

        TimeSpan otherTimeSpan = (TimeSpan)other;
        if (this.hour != otherTimeSpan.hour) {
            return false;
        } else if (this.minute != otherTimeSpan.minute) {
            return false;
        } else {
            return true;
        }
    }

}
