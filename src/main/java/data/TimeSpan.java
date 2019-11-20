package data;

/**
 * A time span consisting of hour and minutes as well as basic arithmetic for it.
 *
 * @author Liam Wachter
 */
public class TimeSpan implements Comparable<TimeSpan> {
    private int minute;
    private int hour;

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
     */
    public void add(TimeSpan addend) {
        int hourSum = this.hour + addend.getHour();
        int minuteSum = this.minute + addend.getMinute();
        int carry = minuteSum / 60;
        
        this.hour = hourSum + carry;
        this.minute = minuteSum % 60;
    }

    /**
     * Subtracts hours and minutes taking carryover into account.
     * @param addend - TimeSpan that should be added
     */
    public void subtract(TimeSpan subtrahend) throws IllegalArgumentException {
        if (this.compareTo(subtrahend) < 0) {
            throw new IllegalArgumentException("Subtrahend may not be greater than Minuend.");
        }
        
        int hourDiff = this.hour - subtrahend.getHour();
        int minuteDiff = this.minute - subtrahend.getMinute();
        
        this.hour = hourDiff - ((59 - minuteDiff) / 60);
        this.minute = (60 + minuteDiff) % 60;
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
