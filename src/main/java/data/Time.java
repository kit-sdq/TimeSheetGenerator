package data;

import java.util.function.Function;

import i18n.ResourceHandler;

/**
 * An abstract representation of time separated in hours and minutes.
 */
public abstract class Time implements Comparable<Time> {
    
    /**
     * Number of minutes in an hour.
     */
    public static final int MINUTES_PER_HOUR = 60;

    /**
     * Create a new Time instance.
     * @param totalMinutes Total number of minutes.
     */
    public Time(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }
    
    /**
     * Create a new Time instance.
     */
    public Time(int hours, int minutes, boolean positive) {
        if (hours < 0 || minutes < 0) {
            throw new IllegalArgumentException(ResourceHandler.getMessage("error.time.negativeHourMinute"));
        }
        
        int totalMinutes = hours * MINUTES_PER_HOUR + minutes;
        if (positive) {
            this.totalMinutes = totalMinutes;
        } else {
            this.totalMinutes = -totalMinutes;
        }
    }
    
    protected final int totalMinutes;
    
    /**
     * Get the total number of minutes.
     * @return Total number of minutes.
     */
    public int getTotalMinutes() {
        return totalMinutes;
    }
    
    /**
     * Get the absolute number of hours.
     * @return Absolute number of hours.
     */
    public int getHour() {
        return Math.abs(totalMinutes / MINUTES_PER_HOUR);
    }
    
    /**
     * Get the absolute number of minutes.
     * @return Absolute number of minutes.
     */
    public int getMinute() {
        return Math.abs(totalMinutes % MINUTES_PER_HOUR);
    }
    
    /**
     * Get the sign of the time representation.
     * 
     * The sign is calculated as follows:
     * 1 if the time is positive,
     * -1 if the time is negative,
     * 0 otherwise
     * 
     * @return Sign of the time representaiton.
     */
    public int getSign() {
        if (totalMinutes < 0) {
            return -1;
        } else if (totalMinutes > 0) {
            return 1;
        } else {
            return 0;
        }
    }
    
    /**
     * Get if the time is negative.
     * @return True if the time is negative.
     */
    public boolean isNegative() {
        return totalMinutes < 0;
    }
    
    /**
     * Parse the string representation of a Time object.
     * @param string String representation.
     * @param create Encapsulated constructor function for the requested object. The provided argument is the total number of minutes.
     * @return New object of a subtype of the Time class.
     */
    protected static <T extends Time> T parse(String string, Function<Integer, T> create) {
        if (!string.matches(ResourceHandler.getMessage("locale.time.parseRegex"))) {
            throw new IllegalArgumentException(ResourceHandler.getMessage("error.time.invalidParseInput"));
        }
        
        int sign = 1;
        if (string.startsWith(ResourceHandler.getMessage("locale.time.negativeSign"))) {
            sign = -1;
            string = string.substring(1);
        }
        
        String[] splittedString = string.split(ResourceHandler.getMessage("locale.time.separatorHourMinute"));
        
        int hours;
        int minutes;
        try {
            hours = Integer.parseInt(splittedString[0]);
            minutes = Integer.parseInt(splittedString[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        
        return create.apply(sign * (hours * MINUTES_PER_HOUR + minutes));
    }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Time)) {
            return false;
        }
        return totalMinutes == ((Time)other).getTotalMinutes();
    }
    
    @Override
    public int compareTo(Time other) {
        return Integer.compare(totalMinutes, other.getTotalMinutes());
    }
    
    @Override
    public String toString() {
        return ResourceHandler.getMessage("locale.time.stringFormat", getHour(), getMinute());
    }
    
}
