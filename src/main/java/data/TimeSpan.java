package data;

/**
 * An immutable time span consisting of hour and minutes as well as basic arithmetic for it.
 */
public class TimeSpan implements Comparable<TimeSpan> {
    
    //TODO Replace magic numbers with constants
    private final int minute;
    private final int hour;

    /**
     * Constructs a new TimeSpan instance.
     * @param hour - Non-negative amount of hours
     * @param minute - Number of minutes between 0 and 59
     */
    public TimeSpan(int hour, int minute) {
        if (hour < 0 || minute < 0) {
            throw new IllegalArgumentException("Hour and minute may not be negative.");
        } else if (minute > 59) {
            throw new IllegalArgumentException("Minute may not be greater than 59.");
        }
        this.minute = minute;
        this.hour = hour;
    }

    /**
     * Gets the minute of the TimeSpan.
     * @return - The minute
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Gets the hour of the TimeSpan.
     * @return - The hour
     */
    public int getHour() {
        return hour;
    }

    /**
     * Sums up hours and minutes taking carryover into account.
     * @param addend - TimeSpan that should be added
     * @return The TimeSpan representing the sum
     */
    public TimeSpan add(TimeSpan addend) {
        int hourSum = this.hour + addend.getHour();
        int minuteSum = this.minute + addend.getMinute();
        int carry = minuteSum / 60;
        
        return new TimeSpan(
            hourSum + carry,
            minuteSum % 60
        );
    }

    /**
     * Subtracts hours and minutes taking carryover into account.
     * @param subtrahend - TimeSpan that should be subtracted
     * @return The TimeSpan representing the difference
     * @throws IllegalArgumentException thrown if the subtrahend is greater than the minuend
     */
    public TimeSpan subtract(TimeSpan subtrahend) throws IllegalArgumentException {
        if (this.compareTo(subtrahend) < 0) {
            throw new IllegalArgumentException("Subtrahend may not be greater than Minuend.");
        }
        
        int hourDiff = this.hour - subtrahend.getHour();
        int minuteDiff = this.minute - subtrahend.getMinute();
        
        return new TimeSpan(
            hourDiff - ((59 - minuteDiff) / 60),
            (60 + minuteDiff) % 60
        );
    }

    /**
     * Attempts to interpret the string s as a representation of a {@link TimeSpan}.
     * @param s - the string to be parsed.
     * @return A {@link TimeSpan} representing the input string
     */
    public static TimeSpan parse(String s) {
        if (!s.matches("^[0-9]+:[0-5]?[0-9]$")) {
            throw new IllegalArgumentException("Invalid time string. Usage: h...h:mm");
        }
        String[] splittedString = s.split(":");
        
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
        return String.format("%02d:%02d", hour, minute);
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
}
