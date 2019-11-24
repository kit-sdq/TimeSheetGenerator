package checker.holiday;

import java.time.LocalDate;

/**
 * @author copied from swt docu generator
 */
public class Holiday {
    private String name;
    private LocalDate date;

    public Holiday(String name, LocalDate date) {
        this.name = name;
        this.date = date;
    }

    public String getName() { return this.name; }
    public LocalDate getDate() { return this.date; }
}