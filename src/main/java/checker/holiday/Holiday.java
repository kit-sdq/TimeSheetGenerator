package checker.holiday;

import java.time.LocalDate;

/**
 * Represents a certain holiday used by a class implementing {@link IHolidayChecker}.
 */
public class Holiday {
    
    private final LocalDate date;
    private final String description;

    /**
     * Constructs a new {@link Holiday} instance.
     * @param date - on which the holiday takes place.
     * @param description - of the holiday.
     */
    public Holiday(LocalDate date, String description) {
        this.date = date;
        this.description = description;
    }

    /**
     * Gets the description of a {@link Holiday}.
     * @return The description.
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * Gets the date of a {@link Holiday}.
     * @return The date.
     */
    public LocalDate getDate() {
        return this.date;
    }
    
    /**
     * Checks whether a {@link Holiday} takes place on a given date.
     * @param otherDate - to check if the holiday takes place on.
     * @return True if the {@link Holiday} takes place on this day, false otherwise.
     */
    public boolean equalsDate(LocalDate otherDate) {
        return date.equals(otherDate);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Holiday)) {
            return false;
        }

        Holiday otherHoliday = (Holiday)other;
        if (!this.date.equals(otherHoliday.date)) {
            return false;
        } else if (!this.description.equals(otherHoliday.description)) {
            return false;
        } else {
            return true;
        }
    }

}
