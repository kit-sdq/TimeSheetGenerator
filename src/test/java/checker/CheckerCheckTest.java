package checker;

import static org.junit.Assert.*;

import java.sql.Date;
import java.time.Month;
import java.time.YearMonth;

import org.junit.Test;

import data.Employee;
import data.Entry;
import data.FullDocumentation;
import data.Profession;
import data.TimeSpan;
import data.WorkingArea;

public class CheckerCheckTest {

    ////Placeholder for documentation construction
    private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
    private static final Profession PROFESSION = new Profession("Fakultät für Informatik", WorkingArea.UB, 40, 10.31);
    private static final YearMonth YEAR_MONTH = YearMonth.of(2019, Month.NOVEMBER);
    private static final TimeSpan zeroTs = new TimeSpan(0, 0);
    
    @Test
    public void testSingleEntryValidDocumentation() {
        ////Test values
        TimeSpan start = new TimeSpan(8, 0);
        TimeSpan end = new TimeSpan(12, 0);
        TimeSpan pause = zeroTs.clone();
        Date date = Date.valueOf("2019-11-22"); //Valid working day
        
        ////Checker initialization
        Entry entry = new Entry("Test", date, start, end, pause);
        Entry[] entries = {entry};
        FullDocumentation fullDoc = new FullDocumentation(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        ////Assertions
        assertEquals(CheckerReturn.VALID, checker.check());
    }
    
    @Test
    public void testSingleEntryValidButHoliday() {
        ////Test values
        TimeSpan start = new TimeSpan(8, 0);
        TimeSpan end = new TimeSpan(12, 0);
        TimeSpan pause = zeroTs.clone();
        Date date = Date.valueOf("2019-12-25"); //Holiday
        
        ////Checker initialization
        Entry entry = new Entry("Test", date, start, end, pause);
        Entry[] entries = {entry};
        FullDocumentation fullDoc = new FullDocumentation(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        ////Assertions
        assertEquals(CheckerReturn.TIME_HOLIDAY, checker.check());
    }
    
    @Test
    public void testSingleEntryValidButSunday() {
        ////Test values
        TimeSpan start = new TimeSpan(8, 0);
        TimeSpan end = new TimeSpan(12, 0);
        TimeSpan pause = zeroTs.clone();
        Date date = Date.valueOf("2019-12-1"); //Sunday
        
        ////Checker initialization
        Entry entry = new Entry("Test", date, start, end, pause);
        Entry[] entries = {entry};
        FullDocumentation fullDoc = new FullDocumentation(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        ////Assertions
        assertEquals(CheckerReturn.TIME_SUNDAY, checker.check());
    }
    
    @Test
    public void testSingleEntryValidButSundayAndHoliday() {
        ////Test values
        TimeSpan start = new TimeSpan(8, 0);
        TimeSpan end = new TimeSpan(12, 0);
        TimeSpan pause = zeroTs.clone();
        Date date = Date.valueOf("2022-12-25"); //Sunday and holiday
        
        ////Checker initialization
        Entry entry = new Entry("Test", date, start, end, pause);
        Entry[] entries = {entry};
        FullDocumentation fullDoc = new FullDocumentation(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        ////Assertions
        assertEquals(CheckerReturn.TIME_SUNDAY, checker.check());
    }
}
