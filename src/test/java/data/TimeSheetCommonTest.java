package data;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.Test;

public class TimeSheetCommonTest {

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTransferVacation() {
        ////Test values
        TimeSpan maxWorkingTime = new TimeSpan(40, 0);
        TimeSpan predTransfer = new TimeSpan(60, 0);
        TimeSpan succTransfer = new TimeSpan(20, 0);
        
        ////TimeSheet initialization
        Employee employee = new Employee("Max Mustermann", 1234567);
        Profession profession = new Profession("IPD", WorkingArea.UB, maxWorkingTime, 10.31);
        YearMonth yearMonth = YearMonth.of(2019, 11);
        Entry[] entries = new Entry[] {
            new Entry("Vacation", LocalDate.of(2019, 11, 29), new TimeSpan(10, 0), new TimeSpan(20, 0), new TimeSpan(0, 0), true)
        };
        TimeSheet timeSheet = new TimeSheet(employee, profession, yearMonth, entries, succTransfer, predTransfer);
        
        ////Assertions
    }

    @Test
    public void testValidTransferVacationUpperBound() {
        ////Test values
        TimeSpan maxWorkingTime = new TimeSpan(40, 0);
        TimeSpan predTransfer = new TimeSpan(50, 0);
        TimeSpan succTransfer = new TimeSpan(20, 0);
        
        ////TimeSheet initialization
        Employee employee = new Employee("Max Mustermann", 1234567);
        Profession profession = new Profession("IPD", WorkingArea.UB, maxWorkingTime, 10.31);
        YearMonth yearMonth = YearMonth.of(2019, 11);
        Entry[] entries = new Entry[] {
            new Entry("Vacation", LocalDate.of(2019, 11, 29), new TimeSpan(10, 0), new TimeSpan(20, 0), new TimeSpan(0, 0), true)
        };
        TimeSheet timeSheet = new TimeSheet(employee, profession, yearMonth, entries, succTransfer, predTransfer);
        
        ////Assertions
        assertTrue(timeSheet != null);
    }

}
