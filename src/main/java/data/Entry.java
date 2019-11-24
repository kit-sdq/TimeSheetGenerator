package data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Liam Wachter
 */
public class Entry {
    private String action;
    private Date date;
    private TimeSpan start, end, pause;

    //TODO Document
    
    //TODO Validity check - working time less than zero case
    public Entry(String action, Date date, TimeSpan start, TimeSpan end, TimeSpan pause) {
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

    public String getAction() {
        return action;
    }

    public Date getDate() {
        return date;
    }

    public TimeSpan getStart() {
        return start;
    }

    public TimeSpan getEnd() {
        return end;
    }

    public TimeSpan getPause() {
        return pause;
    }

    /**
     * Calculates the working time.
     * Working time defines the difference between start time and end time without break time.
     * @return The working time
     */
    public TimeSpan getWorkingTime() {
        TimeSpan workingTime = new TimeSpan(this.getEnd().getHour(), this.getEnd().getMinute());
        workingTime.subtract(this.getStart());
        workingTime.subtract(this.getPause());
        
        return workingTime;
    }
    
    /**
     * Gets the value of an element.
     * @param element - The element to get the value from
     * @return The value of an element as a String
     */
    public String getElementStringValue(Element element) {
        String value;
        switch (element) {
            case TABLE_ACTION:
                value = this.getAction();
                break;
            case TABLE_DATE:
                SimpleDateFormat datePattern = new SimpleDateFormat("dd.MM.yy");
                value = datePattern.format(this.getDate());
                break;
            case TABLE_START:
                value = this.getStart().toString();
                break;
            case TABLE_END:
                value = this.getEnd().toString();
                break;
            case TABLE_PAUSE:
                value = this.getPause().toString();
                break;
            case TABLE_TIME:
                value = this.getWorkingTime().toString();
                break;
            default:
                value = null;
                break;
        }
        return value;
    }
    
    public static enum Element {
        TABLE_ACTION,
        TABLE_DATE,
        TABLE_START,
        TABLE_END,
        TABLE_PAUSE,
        TABLE_TIME;
    }
}
