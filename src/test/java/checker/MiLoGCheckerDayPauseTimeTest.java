package checker;

import static org.junit.jupiter.api.Assertions.*;
import static utils.randomtest.RandomAssertions.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;

import org.junit.jupiter.api.Test;

import data.Employee;
import data.Entry;
import data.TimeSheet;
import data.Profession;
import data.TimeSpan;
import data.WorkingArea;
import utils.randomtest.RandomParameterExtension.RandomTimeSpan;
import utils.randomtest.RandomTestExtension.RandomTest;
import utils.randomtest.RandomTestExtension.RandomTestClass;

@RandomTestClass
public class MiLoGCheckerDayPauseTimeTest {
    
    //Checker constants
    TimeSpan[][] PAUSE_RULES = MiLoGChecker.getPauseRules();
    
    ////Placeholder for time sheet construction
    private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
    private static final Profession PROFESSION = new Profession("Fakultät für Informatik", WorkingArea.UB, new TimeSpan(40, 0), 10.31);
    private static final YearMonth YEAR_MONTH = YearMonth.of(2019, Month.NOVEMBER);
    private static final TimeSpan zeroTs = new TimeSpan(0, 0);
    
    @Test
    public void testValidLowerBound() {
        ////Test values
        TimeSpan start = zeroTs;
        TimeSpan end = zeroTs;
        TimeSpan pause = zeroTs;
        
        ////Checker initialization
        Entry entry = new Entry("Test", LocalDate.of(2019, 11, 22), start, end, pause, false);
        Entry[] entries = {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Execution
        checker.checkDayPauseTime();
        
        ////Assertions
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }
    
    @Test
    public void testValidMultipleEntries() {
        ////Test values
        Entry entry1 = new Entry("Test1", LocalDate.of(2019, 11, 22), new TimeSpan(8, 0), new TimeSpan(11, 0), zeroTs, false);
        Entry entry2 = new Entry("Test2", LocalDate.of(2019, 11, 22), new TimeSpan(16, 0), new TimeSpan(21, 0), new TimeSpan(0, 30), false);
        Entry entry3 = new Entry("Test3", LocalDate.of(2019, 11, 23), new TimeSpan(16, 0), new TimeSpan(21, 0), zeroTs, false);
        
        ////Checker initialization
        Entry[] entries = {entry1, entry2, entry3};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Execution
        checker.checkDayPauseTime();
        
        ////Assertions
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }

    @Test
    public void testExceedanceMultipleEntries() {
        ////Test values
        Entry entry1 = new Entry("Test1", LocalDate.of(2019, 11, 22), new TimeSpan(8, 0), new TimeSpan(12, 0), zeroTs, false);
        Entry entry2 = new Entry("Test2", LocalDate.of(2019, 11, 22), new TimeSpan(16, 0), new TimeSpan(21, 0), zeroTs, false);
        Entry entry3 = new Entry("Test3", LocalDate.of(2019, 11, 23), new TimeSpan(16, 0), new TimeSpan(21, 0), zeroTs, false);
        
        ////Checker initialization
        Entry[] entries = {entry1, entry2, entry3};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        checker.checkDayPauseTime();
        
        ////Expectation
        String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TIME_PAUSE.getErrorMessage(entry1.getDate());
        
        ////Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
    }

    @RandomTest(iterations = 5)
    public void testRandomSingleEntryNoPause(
        @RandomTimeSpan TimeSpan start,
        @RandomTimeSpan TimeSpan end
    ) {
        randomAssert(start.compareTo(end) < 0);

        ////Test values
        TimeSpan pause = zeroTs;
        
        ////Checker initialization
        Entry entry = new Entry("Test", LocalDate.of(2019, 11, 22), start, end, pause, false);
        Entry[] entries = {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Expectation
        String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TIME_PAUSE.getErrorMessage(entry.getDate());
        
        ////Assertions
        for (TimeSpan[] pauseRule : PAUSE_RULES) {
            if (entry.getWorkingTime().compareTo(pauseRule[0]) >= 0) {
                checker.checkDayPauseTime();
                
                assertEquals(CheckerReturn.INVALID, checker.getResult());
                assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
                return;
            }
        }
        
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }
    
    @RandomTest
    public void testRandomSingleEntry(
        @RandomTimeSpan TimeSpan start,
        @RandomTimeSpan TimeSpan end,
        @RandomTimeSpan(upperBoundHour = 4) TimeSpan pause
    ) {
        randomAssert(start.compareTo(end) < 0);
        randomAssert(pause.compareTo(end.subtract(start)) < 0);

        ////Checker initialization
        Entry entry = new Entry("Test", LocalDate.of(2019, 11, 22), start, end, pause, false);
        Entry[] entries = {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Expectation
        String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TIME_PAUSE.getErrorMessage(entry.getDate());
        
        ////Assertions
        TimeSpan startToEnd = end.subtract(start);
        
        for (TimeSpan[] pauseRule : PAUSE_RULES) {
            if (startToEnd.compareTo(pauseRule[0]) >= 0) {
                if (entry.getPause().compareTo(pauseRule[1]) < 0) {
                    checker.checkDayPauseTime();
                    
                    assertEquals(CheckerReturn.INVALID, checker.getResult());
                    assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
                    return;
                }
            }
        }
        
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }
    
    @Test
    public void testMultipleExceedences() {
        ////Test values
        Entry entry1 = new Entry("Test1", LocalDate.of(2019, 11, 22), new TimeSpan(8, 0), new TimeSpan(12, 0), zeroTs, false);
        Entry entry2 = new Entry("Test2", LocalDate.of(2019, 11, 22), new TimeSpan(16, 0), new TimeSpan(21, 0), zeroTs, false);
        Entry entry3 = new Entry("Test3", LocalDate.of(2019, 11, 23), new TimeSpan(8, 0), new TimeSpan(20, 0), zeroTs, false);
        
        ////Checker initialization
        Entry[] entries = {entry1, entry2, entry3};
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(fullDoc);
        
        ////Execution
        checker.checkDayPauseTime();
        
        ////Expectation
        String error1 = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TIME_PAUSE.getErrorMessage(entry1.getDate());
        String error3 = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TIME_PAUSE.getErrorMessage(entry3.getDate());
        
        ////Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error1)));
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error3)));
    }
}
