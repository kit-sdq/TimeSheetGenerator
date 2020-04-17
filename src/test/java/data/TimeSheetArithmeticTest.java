package data;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;

import org.junit.Test;

public class TimeSheetArithmeticTest {

    @Test
    public void testGetTotalWorkTime() {
        Employee employee = new Employee("Moritz Gstür", 1234567);
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, new TimeSpan(40, 0), 10.31);
        TimeSpan zeroTs = new TimeSpan(0, 0);
        Entry[] entries = new Entry[7];
        entries[0] = new Entry("Test1", LocalDate.now(), new TimeSpan(10, 0), new TimeSpan(18, 30), new TimeSpan(3, 15), false);
        entries[1] = new Entry("Test2", LocalDate.now(), new TimeSpan(12, 0), new TimeSpan(16, 30), new TimeSpan(0, 0), false);
        entries[2] = new Entry("Test3", LocalDate.now(), new TimeSpan(10, 0), new TimeSpan(12, 0), new TimeSpan(0, 15), false);
        entries[3] = new Entry("Test4", LocalDate.now(), new TimeSpan(15, 0), new TimeSpan(16, 30), new TimeSpan(0, 0), false);
        entries[4] = new Entry("Test5", LocalDate.now(), new TimeSpan(20, 0), new TimeSpan(20, 30), new TimeSpan(0, 0), false);
        entries[5] = new Entry("Test6", LocalDate.now(), new TimeSpan(9, 30), new TimeSpan(18, 30), new TimeSpan(1, 0), false);
        entries[6] = new Entry("Test7", LocalDate.now(), new TimeSpan(9, 0), new TimeSpan(12, 0), new TimeSpan(0, 0), true);
        TimeSheet timeSheet = new TimeSheet(employee, profession, YearMonth.of(2019, Month.NOVEMBER), entries, zeroTs, zeroTs);
        
        assertEquals(timeSheet.getTotalWorkTime(), new TimeSpan(21, 30));
    }
    
    @Test
    public void testGetTotalVacationTime() {
        Employee employee = new Employee("Moritz Gstür", 1234567);
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, new TimeSpan(40, 0), 10.31);
        TimeSpan zeroTs = new TimeSpan(0, 0);
        Entry[] entries = new Entry[4];
        entries[0] = new Entry("Test1", LocalDate.now(), new TimeSpan(10, 0), new TimeSpan(18, 30), new TimeSpan(3, 15), false);
        entries[1] = new Entry("Test2", LocalDate.now(), new TimeSpan(12, 0), new TimeSpan(16, 30), new TimeSpan(0, 0), true);
        entries[2] = new Entry("Test3", LocalDate.now(), new TimeSpan(10, 0), new TimeSpan(12, 0), new TimeSpan(0, 0), true);
        entries[3] = new Entry("Test4", LocalDate.now(), new TimeSpan(15, 0), new TimeSpan(16, 30), new TimeSpan(0, 0), true);
        TimeSheet timeSheet = new TimeSheet(employee, profession, YearMonth.of(2019, Month.NOVEMBER), entries, zeroTs, zeroTs);
        
        assertEquals(timeSheet.getTotalVacationTime(), new TimeSpan(8, 0));
    }

    //TODO Create random tests
}
