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

public class CheckerValidWorkingDays {

    ////Placeholder for documentation construction
    private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
    private static final Profession PROFESSION = new Profession("Fakultät für Informatik", WorkingArea.UB, 40, 10.31);
    private static final TimeSpan zeroTs = new TimeSpan(0, 0);
    
    @Test
    public void testValidWorkingDay() {
        ////Test values
        TimeSpan start = new TimeSpan(0, 0);
        TimeSpan end = new TimeSpan(0, 0);
        TimeSpan pause = new TimeSpan(0, 0);
        YearMonth yearMonth = YearMonth.of(2019, Month.NOVEMBER);
        Date date = Date.valueOf(yearMonth.getYear() + "-" + yearMonth.getMonthValue() + "-22"); //Friday, 22. November 2019, no Holiday in BW Germany
        
        ////Checker initialization
        Entry entry = new Entry("Test", date, start, end, pause);
        Entry[] entries = {entry};
        FullDocumentation fullDoc = new FullDocumentation(EMPLOYEE, PROFESSION, yearMonth, entries, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        ////Assertions
        assertEquals(CheckerReturn.VALID, checker.checkValidWorkingDays());
    }

    @Test
    public void testNewYear() {
        ////Test values
        TimeSpan start = new TimeSpan(0, 0);
        TimeSpan end = new TimeSpan(0, 0);
        TimeSpan pause = new TimeSpan(0, 0);
        YearMonth yearMonth = YearMonth.of(2019, Month.JANUARY);
        Date date = Date.valueOf(yearMonth.getYear() + "-" + yearMonth.getMonthValue() + "-01"); //New years day: Holiday in BW Germany
        
        ////Checker initialization
        Entry entry = new Entry("Test", date, start, end, pause);
        Entry[] entries = {entry};
        FullDocumentation fullDoc = new FullDocumentation(EMPLOYEE, PROFESSION, yearMonth, entries, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        ////Assertions
        assertEquals(CheckerReturn.TIME_HOLIDAY, checker.checkValidWorkingDays());
    }
}
