package data;

public class TimeSpan {
    private int minute;
    private int hour;

    public TimeSpan(int minute, int hour) {
        this.minute = minute;
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }
}
