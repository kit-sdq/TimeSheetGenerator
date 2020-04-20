package checker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;

import org.junit.Test;

import data.ClockTime;
import data.Employee;
import data.Entry;
import data.Profession;
import data.TimeSheet;
import data.TimeSpan;
import data.WorkingArea;

public class MiLoGCheckerNegativeAllowedWorkingTimeTest {

    private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
    private static final YearMonth YEAR_MONTH = YearMonth.of(2019, Month.NOVEMBER);
    
    @Test
    public void testPositiveAllowedWorkingTime() {
        // data
        TimeSpan maxWorkTime = new TimeSpan(40, 0);
        TimeSpan predTransfer = new TimeSpan(2, 0);
        TimeSpan succTransfer = new TimeSpan(4, 0);
        
        Entry entry1 = new Entry("Vacation", LocalDate.of(2020, 4, 21), new ClockTime(10, 0), new ClockTime(14, 0), new TimeSpan(0, 0), true);
        
        assertTrue(maxWorkTime.subtract(predTransfer).add(succTransfer).subtract(entry1.getWorkingTime()).isPositive());
        
        // prepare
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, succTransfer, predTransfer);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        // execute
        checker.checkNegativeAllowedWorkingTime();
        
        // assert
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }
    
    @Test
    public void testZeroAllowedWorkingTime() {
        // data
        TimeSpan maxWorkTime = new TimeSpan(10, 0);
        TimeSpan predTransfer = new TimeSpan(6, 0);
        TimeSpan succTransfer = new TimeSpan(2, 0);
        
        Entry entry1 = new Entry("Vacation", LocalDate.of(2020, 4, 21), new ClockTime(10, 0), new ClockTime(16, 0), new TimeSpan(0, 0), true);
        
        assertTrue(maxWorkTime.subtract(predTransfer).add(succTransfer).subtract(entry1.getWorkingTime()).isZero());
        
        // prepare
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, succTransfer, predTransfer);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        // execute
        checker.checkNegativeAllowedWorkingTime();
        
        // assert
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }
    
    @Test
    public void testNegativeAllowedWorkingTime1() {
        // data
        TimeSpan maxWorkTime = new TimeSpan(2, 0);
        TimeSpan predTransfer = new TimeSpan(10, 0);
        TimeSpan succTransfer = new TimeSpan(4, 0);
        
        Entry entry1 = new Entry("Vacation", LocalDate.of(2020, 4, 21), new ClockTime(10, 0), new ClockTime(18, 0), new TimeSpan(0, 0), true);
        
        assertTrue(maxWorkTime.subtract(predTransfer).add(succTransfer).subtract(entry1.getWorkingTime()).isNegative());
        
        // prepare
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, succTransfer, predTransfer);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        // execute
        checker.checkNegativeAllowedWorkingTime();
        
        // assert
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(error ->
            error.getErrorMessageProvider().equals(MiLoGChecker.MiLoGCheckerErrorMessageProvider.ALLOWED_WORKING_TIME_NEGATIVE)
        ));
    }
    
    @Test
    public void testNegativeAllowedWorkingTime2() {
        // data
        TimeSpan maxWorkTime = new TimeSpan(4, 0);
        TimeSpan predTransfer = new TimeSpan(5, 0);
        TimeSpan succTransfer = new TimeSpan(6, 0);
        
        Entry entry1 = new Entry("Vacation", LocalDate.of(2020, 4, 21), new ClockTime(10, 0), new ClockTime(17, 0), new TimeSpan(0, 0), true);
        
        assertTrue(maxWorkTime.subtract(predTransfer).add(succTransfer).subtract(entry1.getWorkingTime()).isNegative());
        
        // prepare
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, succTransfer, predTransfer);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        // execute
        checker.checkNegativeAllowedWorkingTime();
        
        // assert
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(error ->
            error.getErrorMessageProvider().equals(MiLoGChecker.MiLoGCheckerErrorMessageProvider.ALLOWED_WORKING_TIME_NEGATIVE)
        ));
    }
    
}
