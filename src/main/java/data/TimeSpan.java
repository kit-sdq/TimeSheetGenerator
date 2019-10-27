package data;

/**
 * A time span consisting of hour and minutes as well as basic arithmetic for it.
 *
 * @author Liam Wachter
 */
public class TimeSpan implements Comparable {
    private int minute;
    private int hour;

    public TimeSpan(int hour, int minute) {
        // TODO check valid
        this.minute = minute;
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }

    /**
     * add toAdd to this time span in the sense of time arithmetic. e.g 0:59 + 0:01  = 1:0
     */
    public void add(TimeSpan toAdd) {
        minute += toAdd.getMinute();
        minute %= 60;
        hour += toAdd.getMinute() / 60;
        hour += toAdd.getHour();
    }

    public void subtract(TimeSpan toSub) throws IllegalArgumentException {
        //TODO subtract
        //TODO check result if negative throw exception
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }

    @Override
    public int compareTo(Object o) {
        TimeSpan timeSpan = (TimeSpan) o;
        if (hour > timeSpan.getHour())
            return 1;
        if (hour == timeSpan.getHour())
            return Integer.compare(minute, timeSpan.getMinute());
        return -1;
    }
}
