package data;

/**
 * A time span consisting of hour and minutes as well as basic arithmetic for it.
 *
 * @author Liam Wachter
 */
public class TimeSpan {
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
     *
     * @param toAdd
     */
    public void add(TimeSpan toAdd) {
        minute += toAdd.getMinute();
        minute %= 60;
        hour += toAdd.getMinute() / 60;
        hour += toAdd.getHour();
    }

    @Override
    public String toString() {
        return String.format("%d:%2d", hour, minute);
    }
}
