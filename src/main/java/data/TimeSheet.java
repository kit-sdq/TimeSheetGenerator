package data;

import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import i18n.ResourceHandler;

/**
 * A time sheet represents a whole month of work done by an {@link Employee}.
 */
public class TimeSheet {
    private final Employee employee;
    private final Profession profession;
    private final YearMonth yearMonth;
    private final TimeSpan succTransfer, predTransfer;
    private final List<Entry> entries;
    
    /**
     * Constructs a new instance of {@code TimeSheet}.
     *
     * @param employee - The {@link Employee employee} this time sheet is associated with.
     * @param profession - The {@link Profession profession} of the {@link Employee employee}.
     * @param yearMonth - The year and month this time sheet is associated with.
     * @param entries - The {@link Entry entries} this time sheet should consist of.
     * @param succTransfer - The time that should be carried over to the next time sheet.
     * @param predTransfer - The time that got carried over from the last time sheet.
     */
    public TimeSheet(Employee employee, Profession profession, YearMonth yearMonth, Entry[] entries,
            TimeSpan succTransfer, TimeSpan predTransfer) {
        
        this.employee = employee;
        this.profession = profession;
        this.yearMonth = yearMonth;
        this.succTransfer = succTransfer;
        this.predTransfer = predTransfer;
        
        List<Entry> entryList = Arrays.asList(entries);
        Collections.sort(entryList);
        this.entries = Collections.unmodifiableList(entryList);
        
        /*
         * This check has to be done in order to guarantee that the corrected max working time
         * (corrected => taking vacation and transfer into account) is not negative.
         * 
         * TODO: I don't think this belongs here, should probably be in some checker.
         */
        if (profession.getMaxWorkingTime().add(succTransfer).compareTo(predTransfer.add(getTotalVacationTime())) < 0) {
            throw new IllegalArgumentException(ResourceHandler.getMessage("error.timesheet.sumOfTimeNegative"));
        }
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
     * The list of entries is sorted as specified in {@link Entry}.
     * @return The entries.
     */
    public List<Entry> getEntries() {
        return entries;
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
        
        for (Entry entry : this.getEntries()) {
            if (!entry.isVacation()) {
                totalWorkTime = totalWorkTime.add(entry.getWorkingTime());
            }
        }
        
        return totalWorkTime;
    }
    
    /**
     * Calculates the overall vacation time of all entries.
     * @return The overall, summed up vacation time.
     */
    public TimeSpan getTotalVacationTime() {
        TimeSpan totalVacationTime = new TimeSpan(0, 0);
        
        for (Entry entry : this.getEntries()) {
            if (entry.isVacation()) {
                totalVacationTime = totalVacationTime.add(entry.getWorkingTime());
            }
        }
        
        return totalVacationTime;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TimeSheet)) {
            return false;
        }

        TimeSheet otherTimeSheet = (TimeSheet)other;
        if (!this.employee.equals(otherTimeSheet.employee)) {
            return false;
        } else if (!this.profession.equals(otherTimeSheet.profession)) {
            return false;
        } else if (!this.yearMonth.equals(otherTimeSheet.yearMonth)) {
            return false;
        } else if (!this.succTransfer.equals(otherTimeSheet.succTransfer)) {
            return false;
        } else if (!this.predTransfer.equals(otherTimeSheet.predTransfer)) {
            return false;
        } else if (!this.entries.equals(otherTimeSheet.entries)) {
            return false;
        } else {
            return true;
        }
    }
  
}
