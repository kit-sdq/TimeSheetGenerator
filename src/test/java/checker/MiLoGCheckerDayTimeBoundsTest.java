package checker;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import data.Employee;
import data.Entry;
import data.TimeSheet;
import data.Profession;
import data.TimeSpan;
import data.WorkingArea;

public class MiLoGCheckerDayTimeBoundsTest {

    private static TimeSpan CHECKER_WORKDAY_LOWER_BOUND;
    private static TimeSpan CHECKER_WORKDAY_UPPER_BOUND;
    
    //Exclusively. Refer to https://docs.oracle.com/javase/8/docs/api/java/util/Random.html
    private static final int RANDOM_HOUR_BOUND = 24;
    private static final int RANDOM_MINUTES_BOUND = 60;
    
    ////Placeholder for time sheet construction
    private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
    private static final Profession PROFESSION = new Profession("Fakultät für Informatik", WorkingArea.UB, new TimeSpan(40, 0), 10.31);
    private static final YearMonth YEAR_MONTH = YearMonth.of(2019, Month.NOVEMBER);
    private static final TimeSpan zeroTs = new TimeSpan(0, 0);
    
    @Before
    public void init() {
        CHECKER_WORKDAY_LOWER_BOUND = MiLoGChecker.getWorkdayLowerBound();
        CHECKER_WORKDAY_UPPER_BOUND = MiLoGChecker.getWorkdayUpperBound();
    }
    
    @Test
    public void testValidLowerBound() {
        ////Test values
        TimeSpan start = CHECKER_WORKDAY_LOWER_BOUND;
        TimeSpan end = CHECKER_WORKDAY_UPPER_BOUND;
        TimeSpan pause = new TimeSpan(0, 0);
        
        //We just want to test the lower bound for now.
        end = end.subtract(new TimeSpan(1, 0));
        
        ////Checker initialization
        Entry entry = new Entry("Test", LocalDate.of(2019, 11, 22), start, end, pause, false);
        Entry[] entries = {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Execution
        checker.checkDayTimeBounds();
        
        ////Assertions
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }
    
    @Test
    public void testOutOfLowerBound() {
        ////Test values
        TimeSpan start = CHECKER_WORKDAY_LOWER_BOUND;
        TimeSpan end = CHECKER_WORKDAY_UPPER_BOUND;
        TimeSpan pause = new TimeSpan(0, 0);
        
        //This is done to lower the start one minute below the legal lower bound.
        start = start.subtract(new TimeSpan(0, 1));
        
        //We just want to test the lower bound for now.
        end = end.subtract(new TimeSpan(1, 0));
        
        ////Checker initialization
        Entry entry = new Entry("Test", LocalDate.of(2019, 11, 22), start, end, pause, false);
        Entry[] entries = {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Execution
        checker.checkDayTimeBounds();
        
        ////Expectation
        String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TIME_OUTOFBOUNDS.getErrorMessage(entry.getDate());
        
        ////Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
    }
    
    @Test
    public void testValidUpperBound() {
        ////Test values
        TimeSpan start = CHECKER_WORKDAY_LOWER_BOUND;
        TimeSpan end = CHECKER_WORKDAY_UPPER_BOUND;
        TimeSpan pause = new TimeSpan(0, 0);
        
        //We just want to test the upper bound for now.
        start = start.add(new TimeSpan(1, 0));
        
        ////Checker initialization
        Entry entry = new Entry("Test", LocalDate.of(2019, 11, 22), start, end, pause, false);
        Entry[] entries = {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Execution
        checker.checkDayTimeBounds();
        
        ////Assertions
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }
    
    @Test
    public void testOutOfUpperBound() {
        ////Test values
        TimeSpan start = CHECKER_WORKDAY_LOWER_BOUND;
        TimeSpan end = CHECKER_WORKDAY_UPPER_BOUND;
        TimeSpan pause = new TimeSpan(0, 0);
        
        //This is done to raise the end one minute above the legal upper bound.
        end = end.add(new TimeSpan(0, 1));
        
        //We just want to test the upper bound for now.
        start = start.add(new TimeSpan(1, 0));
        
        ////Checker initialization
        Entry entry = new Entry("Test", LocalDate.of(2019, 11, 22), start, end, pause, false);
        Entry[] entries = {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Execution
        checker.checkDayTimeBounds();
        
        ////Expectation
        String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TIME_OUTOFBOUNDS.getErrorMessage(entry.getDate());
        
        ////Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
    }
    
    @Test
    public void testValidBothBounds() {
        ////Test values
        TimeSpan start = CHECKER_WORKDAY_LOWER_BOUND;
        TimeSpan end = CHECKER_WORKDAY_UPPER_BOUND;
        TimeSpan pause = new TimeSpan(0, 0);
        
        ////Checker initialization
        Entry entry = new Entry("Test", LocalDate.of(2019, 11, 22), start, end, pause, false);
        Entry[] entries = {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Execution
        checker.checkDayTimeBounds();
        
        ////Assertions
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }
    
    @Test
    public void testOutOfLowerBoundRandom() {
        ////Random
        Random rand = new Random();
        
        ////Test values
        TimeSpan start = new TimeSpan(rand.nextInt(CHECKER_WORKDAY_UPPER_BOUND.getHour()), rand.nextInt(RANDOM_MINUTES_BOUND));
        TimeSpan end = CHECKER_WORKDAY_UPPER_BOUND;
        TimeSpan pause = new TimeSpan(0, 0);
        
        ////Checker initialization
        Entry entry = new Entry("Test", LocalDate.of(2019, 11, 22), start, end, pause, false);
        Entry[] entries = {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Execution
        checker.checkDayTimeBounds();
        
        ////Expectation (on error)
        String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TIME_OUTOFBOUNDS.getErrorMessage(entry.getDate());
        
        ////Assertions
        if (start.compareTo(CHECKER_WORKDAY_LOWER_BOUND) < 0) {
            assertEquals(CheckerReturn.INVALID, checker.getResult());
            assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
        } else {
            assertEquals(CheckerReturn.VALID, checker.getResult());
            assertTrue(checker.getErrors().isEmpty());
        }
    }
    
    @Test
    public void testOutOfBoundsRandom() {
        ////Random
        Random rand = new Random();
        
        ////Test values
        TimeSpan start = new TimeSpan(rand.nextInt(CHECKER_WORKDAY_UPPER_BOUND.getHour()), 0);
        TimeSpan end = new TimeSpan(start.getHour() + rand.nextInt(RANDOM_HOUR_BOUND - start.getHour()), 0);
        TimeSpan pause = new TimeSpan(0, 0);
        
        ////Checker initialization
        Entry entry = new Entry("Test", LocalDate.of(2019, 11, 22), start, end, pause, false);
        Entry[] entries = {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Execution
        checker.checkDayTimeBounds();
        
        ////Expectation (on error)
        String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TIME_OUTOFBOUNDS.getErrorMessage(entry.getDate());
        
        ////Assertions
        if (start.compareTo(CHECKER_WORKDAY_LOWER_BOUND) < 0 || end.compareTo(CHECKER_WORKDAY_UPPER_BOUND) > 0) {
            assertEquals(CheckerReturn.INVALID, checker.getResult());
            assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
        } else {
            assertEquals(CheckerReturn.VALID, checker.getResult());
            assertTrue(checker.getErrors().isEmpty());
        }
    }
    
    @Test
    public void testOutOfBoundsMultiple() {
        ////Test values
        TimeSpan start0 = CHECKER_WORKDAY_LOWER_BOUND.subtract(new TimeSpan(0, 1));
        TimeSpan end0 = CHECKER_WORKDAY_UPPER_BOUND.subtract(new TimeSpan(1, 0));
        TimeSpan pause0 = new TimeSpan(0, 0);
        Entry entry0 = new Entry("Test", LocalDate.of(2019, 11, 20), start0, end0, pause0, false);
        
        TimeSpan start1 = CHECKER_WORKDAY_LOWER_BOUND.add(new TimeSpan(1, 0));
        TimeSpan end1 = CHECKER_WORKDAY_UPPER_BOUND.add(new TimeSpan(0, 1));
        TimeSpan pause1 = new TimeSpan(0, 0);
        Entry entry1 = new Entry("Test", LocalDate.of(2019, 11, 22), start1, end1, pause1, false);
        
        Entry[] entries = {entry0, entry1};
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(fullDoc);
        
        ////Execution
        checker.checkDayTimeBounds();
        
        ////Expectation (on error)
        String error0 = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TIME_OUTOFBOUNDS.getErrorMessage(entry0.getDate());
        String error1 = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TIME_OUTOFBOUNDS.getErrorMessage(entry1.getDate());
        
        ////Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error0)));
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error1)));
    }
}
