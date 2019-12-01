package data;

import java.time.Month;
import java.time.YearMonth;

/**
 * @author Liam Wachter
 */
public class TimeSheet {
    private final Employee employee;
    private final Profession profession;
    private final YearMonth yearMonth;
    private final TimeSpan vacation, succTransfer, predTransfer;
    private final Entry[] entries;
    
    /**
     * Constructs a new instance of {@code FullDocumentation}.
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

    //TODO JavaDoc for Getter
    public YearMonth getYearMonth() {
        return yearMonth;
    }
    
    public int getYear() {
        return yearMonth.getYear();
    }
    
    public Month getMonth() {
        return yearMonth.getMonth();
    }

    public Entry[] getEntries() {
        return entries;
    }
    
    public TimeSpan getVacation() {
        return this.vacation;
    }

    public TimeSpan getPredTranfer() {
        return this.predTransfer;
    }
    
    public TimeSpan getSuccTransfer() {
        return this.succTransfer;
    }
    
    public Employee getEmployee() {
        return this.employee;
    }
    
    public Profession getProfession() {
        return this.profession;
    }
    
    /**
     * Calculates the overall working time of all entries.
     * @return The overall, summed up working time
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