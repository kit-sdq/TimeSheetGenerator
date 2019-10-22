package checker;

import java.time.LocalDate;

/**
 * @author copied from swt docu generator
 */
class Holiday {
    private String name;
    private LocalDate date;

    Holiday(String name, LocalDate date) {
        this.name = name;
        this.date = date;
    }

    String getName() { return this.name; }
    LocalDate getDate() { return this.date; }
}