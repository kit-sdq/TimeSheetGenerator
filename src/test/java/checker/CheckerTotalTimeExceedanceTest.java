package checker;

import static org.junit.Assert.*;

import java.sql.Date;
import java.time.Month;
import java.time.YearMonth;
import java.util.Random;

import org.junit.Test;

import data.Employee;
import data.Entry;
import data.TimeSheet;
import data.Profession;
import data.TimeSpan;
import data.WorkingArea;

public class CheckerTotalTimeExceedanceTest {
    
    private static final int RANDOM_HOUR_BOUND = 999;
    private static final int RANDOM_DAY_BOUND = 24; 
    //Exclusively. Refer to https://docs.oracle.com/javase/8/docs/api/java/util/Random.html
    private static final int RANDOM_MINUTES_BOUND = 60;
    
    ////Placeholder for documentation construction
    private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
    private static final YearMonth YEAR_MONTH = YearMonth.of(2019, Month.NOVEMBER);
    private static final TimeSpan zeroTs = new TimeSpan(0, 0);
    
    @Test
    public void testNoExceedanceLowerBound() {
        //Test values
        int maxWorkTime = 22;
        int hoursToWork = 0;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", Date.valueOf("2019-11-22"),
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, 0), new TimeSpan(0, 0));
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        //Assertions
        assertEquals(CheckerReturn.VALID, checker.checkTotalTimeExceedance());
    }
    
    @Test
    public void testNoExceedanceUpperBound() {
        //Test values
        int maxWorkTime = 22;
        int hoursToWork = 22;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", Date.valueOf("2019-11-22"),
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, 0), new TimeSpan(0, 0));
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        //Assertions
        assertEquals(CheckerReturn.VALID, checker.checkTotalTimeExceedance());
    }

    @Test
    public void testExceedanceMinutes() {
        //Test values
        int maxWorkTime = 14;
        int hoursToWork = 14;
        int minutesToWork = 1;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", Date.valueOf("2019-11-22"), 
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, minutesToWork), new TimeSpan(0, 0));
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        //Assertions
        assertEquals(CheckerReturn.TIME_EXCEEDANCE, checker.checkTotalTimeExceedance());
    }
    
    @Test
    public void testExceedanceHours() {
        //Test values
        int maxWorkTime = 14;
        int hoursToWork = 15;
        int minutesToWork = 0;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", Date.valueOf("2019-11-22"), 
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, minutesToWork), new TimeSpan(0, 0));
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        //Assertions
        assertEquals(CheckerReturn.TIME_EXCEEDANCE, checker.checkTotalTimeExceedance());
    }
    
    @Test
    public void testExceedanceRandomHoursWithoutPause() {
        //Random
        Random rand = new Random();
        
        //Test values
        int maxWorkTime = rand.nextInt(RANDOM_DAY_BOUND);
        TimeSpan start = new TimeSpan(0, 0);
        TimeSpan end = new TimeSpan(rand.nextInt(RANDOM_DAY_BOUND), 0);
        TimeSpan pause = new TimeSpan(0, 0);
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", Date.valueOf("2019-11-22"), start, end, pause);
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        //Assertions
        if (maxWorkTime < end.getHour()) {
            assertEquals(CheckerReturn.TIME_EXCEEDANCE, checker.checkTotalTimeExceedance());
        } else {
            assertEquals(CheckerReturn.VALID, checker.checkTotalTimeExceedance());
        }    
    }
    
    @Test
    public void testExceedanceRandomMinutesWithoutPause() {
        //Random
        Random rand = new Random();
        
        //Test values
        int maxWorkTime = rand.nextInt(RANDOM_DAY_BOUND);
        TimeSpan start = new TimeSpan(0, 0);
        TimeSpan end = new TimeSpan(maxWorkTime, rand.nextInt(RANDOM_MINUTES_BOUND));
        TimeSpan pause = new TimeSpan(0, 0);
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", Date.valueOf("2019-11-22"), start, end, pause);
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        //Assertions
        if (end.getMinute() > 0) {
            assertEquals(CheckerReturn.TIME_EXCEEDANCE, checker.checkTotalTimeExceedance());
        } else {
            assertEquals(CheckerReturn.VALID, checker.checkTotalTimeExceedance());
        }
    }
    
    @Test
    public void testExceedanceRandomWithoutPause() {
        //Random
        Random rand = new Random();
        
        //Test values
        int maxWorkTime = rand.nextInt(RANDOM_DAY_BOUND);
        TimeSpan start = new TimeSpan(0, 0);
        TimeSpan end = new TimeSpan(rand.nextInt(RANDOM_DAY_BOUND), rand.nextInt(RANDOM_MINUTES_BOUND));
        TimeSpan pause = new TimeSpan(0, 0);
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", Date.valueOf("2019-11-22"), start, end, pause);
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        //Assertions
        if (end.getHour() > maxWorkTime) {
            assertEquals(CheckerReturn.TIME_EXCEEDANCE, checker.checkTotalTimeExceedance());
        } else if (end.getHour() == maxWorkTime && end.getMinute() > 0) {
            assertEquals(CheckerReturn.TIME_EXCEEDANCE, checker.checkTotalTimeExceedance());
        } else {
            assertEquals(CheckerReturn.VALID, checker.checkTotalTimeExceedance());
        }
    }
    
    @Test
    public void testExceedanceRandom() {
        ////Random
        Random rand = new Random();
        
        ////Test values
        int maxWorkTime = rand.nextInt(RANDOM_DAY_BOUND);
        TimeSpan start = new TimeSpan(0, 0);
        //The end hour has to be greater than 1 to guarantee that the pause is not longer than the work
        TimeSpan end = new TimeSpan(rand.nextInt(RANDOM_DAY_BOUND - 1) + 1, rand.nextInt(RANDOM_MINUTES_BOUND));
        TimeSpan pause = new TimeSpan((rand.nextInt(end.getHour())), rand.nextInt(RANDOM_MINUTES_BOUND));
        
        ////Checker initialization
        Entry entry = new Entry("Test 1", Date.valueOf("2019-11-22"), start, end, pause);
        Entry[] entries = {entry};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        ////Assertions
        TimeSpan workingTime = entry.getWorkingTime();
        if (workingTime.getHour() > maxWorkTime) {
            assertEquals(CheckerReturn.TIME_EXCEEDANCE, checker.checkTotalTimeExceedance());
        } else if (workingTime.getHour() == maxWorkTime && workingTime.getMinute() > 0) {
            assertEquals(CheckerReturn.TIME_EXCEEDANCE, checker.checkTotalTimeExceedance());
        } else {
            assertEquals(CheckerReturn.VALID, checker.checkTotalTimeExceedance());
        }
    }
    
    @Test
    public void testExceedanceRandomMultipleEntries() {
        ////Random
        Random rand = new Random();
        
        ////Test values
        int maxWorkTime = rand.nextInt(RANDOM_HOUR_BOUND);
        int numberOfEntries = rand.nextInt(50) + 1; //May not be zero!
        
        ////Entry generator
        Entry[] entries = new Entry[numberOfEntries];
        for (int i = 0; i < numberOfEntries; i++) {
            TimeSpan start = new TimeSpan(0, 0);
            //The end hour has to be greater than 1 to guarantee that the pause is not longer than the work
            TimeSpan end = new TimeSpan(rand.nextInt(RANDOM_DAY_BOUND - 1) + 1, rand.nextInt(RANDOM_MINUTES_BOUND));
            TimeSpan pause = new TimeSpan((rand.nextInt(end.getHour())), rand.nextInt(RANDOM_MINUTES_BOUND));
            
            Entry entry = new Entry("Test", Date.valueOf("2019-11-22"), start, end, pause);
            entries[i] = entry;
        }
        
        ////Checker initialization
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        ////Assertions
        if (fullDoc.getTotalWorkTime().getHour() > maxWorkTime) {
            assertEquals(CheckerReturn.TIME_EXCEEDANCE, checker.checkTotalTimeExceedance());
        } else if (fullDoc.getTotalWorkTime().getHour() == maxWorkTime && fullDoc.getTotalWorkTime().getMinute() > 0) {
            assertEquals(CheckerReturn.TIME_EXCEEDANCE, checker.checkTotalTimeExceedance());
        } else {
            assertEquals(CheckerReturn.VALID, checker.checkTotalTimeExceedance());
        }
    }
}
