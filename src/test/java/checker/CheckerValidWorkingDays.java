package checker;

import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Test;

import data.Entry;
import data.FullDocumentation;
import data.TimeSpan;

public class CheckerValidWorkingDays {

    @Test
    public void testValidWorkingDay() {
        ////Test values
        TimeSpan start = new TimeSpan(0, 0);
        TimeSpan end = new TimeSpan(0, 0);
        TimeSpan pause = new TimeSpan(0, 0);
        Date date = Date.valueOf("2019-11-22"); //Friday, 22. November 2019, no Holiday in BW Germany
        
        ////Checker initialization
        Entry entry = new Entry("Test", date, start, end, pause);
        Entry[] entries = {entry};
        FullDocumentation fullDoc = new FullDocumentation("Max Mustermann", "Fakultät für Informatik", 123456, true, entries);
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
        Date date = Date.valueOf("2019-01-01"); //New year: Holiday in BW Germany
        
        ////Checker initialization
        Entry entry = new Entry("Test", date, start, end, pause);
        Entry[] entries = {entry};
        FullDocumentation fullDoc = new FullDocumentation("Max Mustermann", "Fakultät für Informatik", 123456, true, entries);
        Checker checker = new Checker(fullDoc);
        
        ////Assertions
        assertEquals(CheckerReturn.TIME_HOLIDAY, checker.checkValidWorkingDays());
    }
}
