package ui;

import lombok.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A date represents any day by its year, month of the year (1-12) and day (1-31 usually).
 * Dates are immutable and compared by values, Dates have multiple methods for earlier/later
 * comparison.
 *
 * @param year The year.
 * @param month The month (1-12).
 * @param day The day of the month (1-31).
 */
public record Date(int year, int month, int day) {
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{1,7}-\\d{1,2}-\\d{1,2}$");

    public static final Date ZERO_DATE = new Date(0, 0, 0);

    @Override
    @NonNull
    public String toString() {
        return "%04d-%02d-%02d".formatted(year, month, day);
    }

    /**
     * If the Date is zero, so all fields are zero,
     * and thus equal to the constant {@link Date#ZERO_DATE}.
     * @return True if the Date is the same as ZERO_DATE, false if not.
     */
    public boolean isZero() {
        return this.equals(ZERO_DATE);
    }

    /**
     * Returns if this Date is earlier than another given Date.
     * If the two Dates are equal, returns false.
     * <p>
     *  If the given Date is null, returns false.
     * </p>
     * @return True if the other Date is earlier, false if not.
     */
    public boolean earlierThan(Date other) {
        if (other == null) return false;
        if (other.year() > this.year()) return true;
        if (other.year() < this.year()) return false;
        if (other.month() > this.month()) return true;
        if (other.month() < this.month()) return false;
        return other.day() > this.day();
    }

    /**
     * Returns if this Date is later than another given Date.
     * If the two Dates are equal, returns false.
     * <p>
     *  If the given Date is null, returns false.
     * </p>
     * @return True if the other Date is later, false if not.
     */
    public boolean laterThan(Date other) {
        if (other == null || this.equals(other)) return false;
        return !earlierThan(other);
    }

    /**
     * Parses a Date from the format YYYY-MM-DD. This format is required
     * but allows some variance in the length of numbers.
     *
     * <p>
     *     Restrictions for year, month and day:
     *     <ul>
     *         <li>Year: A number consisting of 1-7 digits.</li>
     *         <li>Month: A number consisting of 1-2 digits.</li>
     *         <li>Day: A number consisting of 1-2 digits.</li>
     *     </ul>
     * </p>
     *
     * <p>
     *     If not Date can be parsed, returns the ZERO_DATE, so the default
     *     Date where all fields are 0.
     * </p>
     * @param date The date, format YYYY-MM-DD.
     * @return The parsed date.
     */
    public static Date parseDate(String date) {
        Matcher matcher = DATE_PATTERN.matcher(date);
        if (!matcher.matches()) return ZERO_DATE;
        int year = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        int day = Integer.parseInt(matcher.group(3));
        return new Date(year, month, day);
    }
}
