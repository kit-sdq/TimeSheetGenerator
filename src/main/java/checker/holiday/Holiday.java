package checker.holiday;

import java.time.LocalDate;

/**
 * Represents a certain holiday used by a class implementing {@link IHolidayChecker}.
 */
public class Holiday {
    
    //TODO JavaDoc
    private final LocalDate date;
    private final String description;

    public Holiday(LocalDate date, String description) {
        this.date = date;
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
    
    public LocalDate getDate() {
        return this.date;
    }
    
    //TODO Test equalsDate 
    public boolean equalsDate(LocalDate otherDate) {
        return date.equals(otherDate);
    }
    
}