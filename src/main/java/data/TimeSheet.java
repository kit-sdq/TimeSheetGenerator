package data;

import java.time.Month;
import java.time.YearMonth;

/**
 * A time sheet represents a whole month of work done by an {@link Employee}.
 */
public class TimeSheet {
    private final Employee employee;
    private final Profession profession;
    private final YearMonth yearMonth;
    private final TimeSpan vacation, succTransfer, predTransfer;
    private final Entry[] entries;
    
    /**
     * Constructs a new instance of {@code TimeSheet}.
     *
     * @param employee - The {@link Employee employee} this documentation is associated with.
     * @param profession - The {@link Profession profession} of the {@link Employee employee}.
     * @param yearMonth - The year and month this documentation is associated with.
     * @param entries - The {@link Entry entries} this documentation should consist of.
     * @param vacation - The vacation time that should get taken into account.
     * @param succTransfer - The time that should be carried over to the next documentation.
     * @param predTransfer - The time that got carried over from the last documentation.
     */
    public TimeSheet(Employee employee, Profession profession, YearMonth yearMonth, Entry[] entries, TimeSpan vacation,
            TimeSpan succTransfer, TimeSpan predTransfer) {
        this.employee = employee;
        this.profession = profession;
        this.yearMonth = yearMonth;
        this.vacation = vacation;
        this.succTransfer = succTransfer;
        this.predTransfer = predTransfer;
        this.entries = entries;
    }

    /**
     * Gets the year of a {@link TimeSheet}.
     * @return The year.
     */
    public int getYear() {
        return yearMonth.getYear();
    }
    
    /**
     * Gets the {@link Month} of a {@link TimeSheet}.
     * @return The month.
     */
    public Month getMonth() {
        return yearMonth.getMonth();
    }

    /**
     * Gets all entries associated with a {@link TimeSheet}.
     * @return The entries.
     */
    public Entry[] getEntries() {
        return entries;
    }
    
    /**
     * Gets the vacation of a {@link TimeSheet}.
     * @return The vacation.
     */
    public TimeSpan getVacation() {
        return this.vacation;
    }

    /**
     * Gets the transfered time from the predecessor month of a {@link TimeSheet}.
     * @return The transfered time from the predecessor month.
     */
    public TimeSpan getPredTransfer() {
        return this.predTransfer;
    }
    
    /**
     * Gets the transfered time from the successor month of a {@link TimeSheet}.
     * @return The transfered time from the successor month.
     */
    public TimeSpan getSuccTransfer() {
        return this.succTransfer;
    }
    
    /**
     * Gets the {@link Employee} associated with a {@link TimeSheet}.
     * @return The employee.
     */
    public Employee getEmployee() {
        return this.employee;
    }
    
    /**
     * Gets the {@link Profession} associated with a {@link TimeSheet}.
     * @return The profession.
     */
    public Profession getProfession() {
        return this.profession;
    }
    
    /**
     * Calculates the overall working time of all entries.
     * @return The overall, summed up working time.
     */
    public TimeSpan getTotalWorkTime() {
        TimeSpan totalWorkTime = new TimeSpan(0, 0);
        
        //Sums up the working times entry per entry
        for (Entry entry : this.getEntries()) {
            totalWorkTime = totalWorkTime.add(entry.getWorkingTime());
        }
        return totalWorkTime;
    }
  
}