package checker;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import org.junit.Test;

import data.Employee;
import data.Entry;
import data.Profession;
import data.TimeSheet;
import data.TimeSpan;
import data.WorkingArea;

public class MiLoGCheckerDayTimeExceedanceTest {
    
    //// Placeholder for time sheet construction
    private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
    private static final Profession PROFESSION = new Profession("Fakultät für Informatik", WorkingArea.UB, new TimeSpan(40, 0), 10.31);
    private static final YearMonth YEAR_MONTH = YearMonth.of(2019, Month.NOVEMBER);
    private static final TimeSpan ZERO_TS = new TimeSpan(0, 0);
    private static final LocalDate WORKINGDAY_VALID = LocalDate.of(2019, 11, 22);

    @Test
    public void testUpperBoundValidSingleEntry() throws CheckerException {
        ////Test values
        TimeSpan start = new TimeSpan(8, 0);
        TimeSpan end = new TimeSpan(18, 45);
        TimeSpan pause = new TimeSpan(0, 45);

        ////Checker initialization
        Entry entry = new Entry("Test", WORKINGDAY_VALID, start, end, pause);
        Entry[] entries = {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, ZERO_TS, ZERO_TS, ZERO_TS);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);

        ////Execution
        checker.checkDayTimeExceedance();

        ////Expectation
        String error = String.format(MiLoGChecker.CheckerErrorMessage.DAY_TIME_EXCEEDANCE.getErrorMessage(), 
                MiLoGChecker.getWorkdayMaxWorkingTime(), WORKINGDAY_VALID);

        ////Assertions
        assertTrue(MiLoGChecker.getWorkdayMaxWorkingTime().compareTo(entry.getWorkingTime()) == 0);
        assertFalse(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
        assertEquals(CheckerReturn.VALID, checker.getResult());
    }

    @Test
    public void testExceedanceSingleEntry() throws CheckerException {
        ////Test values
        TimeSpan start = new TimeSpan(8, 0);
        TimeSpan end = new TimeSpan(18, 46);
        TimeSpan pause = new TimeSpan(0, 45);

        ////Checker initialization
        Entry entry = new Entry("Test", WORKINGDAY_VALID, start, end, pause);
        Entry[] entries = {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, ZERO_TS, ZERO_TS, ZERO_TS);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);

        ////Execution
        checker.checkDayTimeExceedance();

        ////Expectation
        String error = String.format(MiLoGChecker.CheckerErrorMessage.DAY_TIME_EXCEEDANCE.getErrorMessage(), 
                MiLoGChecker.getWorkdayMaxWorkingTime(), WORKINGDAY_VALID);

        ////Assertions
        assertTrue(MiLoGChecker.getWorkdayMaxWorkingTime().compareTo(entry.getWorkingTime()) < 0);
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
        assertEquals(CheckerReturn.INVALID, checker.getResult());
    }

    @Test
    public void testUpperBoundValidMultipleDays() throws CheckerException {
        ////Test values
        TimeSpan start1 = new TimeSpan(8, 0);
        TimeSpan end1 = new TimeSpan(13, 0);
        TimeSpan start2 = new TimeSpan(15, 0);
        TimeSpan end2 = new TimeSpan(20, 0);
        TimeSpan start3 = new TimeSpan(14, 0);
        TimeSpan end3 = new TimeSpan(18, 0);
        LocalDate secondValidWorkingDay = LocalDate.of(2019, 11, 23);
        
        ////Checker initialization
        Entry entry1 = new Entry("Test 1", WORKINGDAY_VALID, start1, end1, ZERO_TS);
        Entry entry2 = new Entry("Test 2", WORKINGDAY_VALID, start2, end2, ZERO_TS);
        Entry entry3 = new Entry("Test 3", secondValidWorkingDay, start3, end3, ZERO_TS);
        Entry[] entries = {entry1, entry2, entry3};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, ZERO_TS, ZERO_TS, ZERO_TS);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);

        ////Execution
        checker.checkDayTimeExceedance();

        ////Expectation
        String errorDayOne = String.format(MiLoGChecker.CheckerErrorMessage.DAY_TIME_EXCEEDANCE.getErrorMessage(), 
                MiLoGChecker.getWorkdayMaxWorkingTime(), WORKINGDAY_VALID);
        String errorDayTwo = String.format(MiLoGChecker.CheckerErrorMessage.DAY_TIME_EXCEEDANCE.getErrorMessage(), 
                MiLoGChecker.getWorkdayMaxWorkingTime(), secondValidWorkingDay);
        
        ////Assertions
        assertTrue(MiLoGChecker.getWorkdayMaxWorkingTime().compareTo(entry1.getWorkingTime().add(entry2.getWorkingTime())) >= 0);
        assertTrue(MiLoGChecker.getWorkdayMaxWorkingTime().compareTo(entry3.getWorkingTime()) >= 0);
        assertFalse(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(errorDayOne)));
        assertFalse(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(errorDayTwo)));
        assertEquals(CheckerReturn.VALID, checker.getResult());
    }

    @Test
    public void testMultipleExceedances() throws CheckerException {
        ////Test values
        TimeSpan start1 = new TimeSpan(9, 59);
        TimeSpan end1 = new TimeSpan(20, 0);
        TimeSpan start2 = new TimeSpan(15, 0);
        TimeSpan end2 = new TimeSpan(20, 0);
        TimeSpan start3 = new TimeSpan(11, 0);
        TimeSpan end3 = new TimeSpan(21, 1);
        LocalDate[] validWorkingDays = {LocalDate.of(2019, 11, 21), WORKINGDAY_VALID,LocalDate.of(2019, 11, 21)};
        
        ////Checker initialization
        Entry entry1 = new Entry("Test 1", validWorkingDays[0], start1, end1, ZERO_TS);
        Entry entry2 = new Entry("Test 2", validWorkingDays[1], start2, end2, ZERO_TS);
        Entry entry3 = new Entry("Test 3", validWorkingDays[2], start3, end3, ZERO_TS);
        Entry[] entries = {entry1, entry2, entry3};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, ZERO_TS, ZERO_TS, ZERO_TS);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);

        ////Execution
        checker.checkDayTimeExceedance();

        ////Expectation
        String errorDayOne = String.format(MiLoGChecker.CheckerErrorMessage.DAY_TIME_EXCEEDANCE.getErrorMessage(), 
                MiLoGChecker.getWorkdayMaxWorkingTime(), validWorkingDays[0]);
        String errorDayTwo = String.format(MiLoGChecker.CheckerErrorMessage.DAY_TIME_EXCEEDANCE.getErrorMessage(), 
                MiLoGChecker.getWorkdayMaxWorkingTime(), validWorkingDays[1]);
        String errorDayThree = String.format(MiLoGChecker.CheckerErrorMessage.DAY_TIME_EXCEEDANCE.getErrorMessage(), 
                MiLoGChecker.getWorkdayMaxWorkingTime(), validWorkingDays[2]);
        
        ////Assertions
        assertTrue(MiLoGChecker.getWorkdayMaxWorkingTime().compareTo(entry1.getWorkingTime()) < 0);
        assertTrue(MiLoGChecker.getWorkdayMaxWorkingTime().compareTo(entry2.getWorkingTime()) >= 0);
        assertTrue(MiLoGChecker.getWorkdayMaxWorkingTime().compareTo(entry3.getWorkingTime()) < 0);
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(errorDayOne)));
        assertFalse(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(errorDayTwo)));
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(errorDayThree)));
        assertEquals(CheckerReturn.INVALID, checker.getResult());
    }
    
    @Test
    public void testExceedanceMultipleEntriesSingleDay() throws CheckerException {
        ////Test values
        TimeSpan start1 = new TimeSpan(8, 0);
        TimeSpan end1 = new TimeSpan(13, 0);
        TimeSpan start2 = new TimeSpan(15, 0);
        TimeSpan end2 = new TimeSpan(20, 1);
        
        ////Checker initialization
        Entry entry1 = new Entry("Test 1", WORKINGDAY_VALID, start1, end1, ZERO_TS);
        Entry entry2 = new Entry("Test 2", WORKINGDAY_VALID, start2, end2, ZERO_TS);
        Entry[] entries = {entry1, entry2};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, ZERO_TS, ZERO_TS, ZERO_TS);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);

        ////Execution
        checker.checkDayTimeExceedance();

        ////Expectation
        String error = String.format(MiLoGChecker.CheckerErrorMessage.DAY_TIME_EXCEEDANCE.getErrorMessage(), 
                MiLoGChecker.getWorkdayMaxWorkingTime(), WORKINGDAY_VALID);

        ////Assertions
        assertTrue(MiLoGChecker.getWorkdayMaxWorkingTime().compareTo(entry1.getWorkingTime().add(entry2.getWorkingTime())) < 0);
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
        assertEquals(CheckerReturn.INVALID, checker.getResult());
    }
    
    @Test
    public void testExceedanceUniqueErrorMultipleEntriesSingleDay() throws CheckerException {
        ////Test values
        TimeSpan start1 = new TimeSpan(7, 0);
        TimeSpan end1 = new TimeSpan(10, 59);
        TimeSpan start2 = new TimeSpan(11, 0);
        TimeSpan end2 = new TimeSpan(14, 59);
        TimeSpan start3 = new TimeSpan(15, 0);
        TimeSpan end3 = new TimeSpan(20, 59);
        TimeSpan start4 = new TimeSpan(21, 0);
        TimeSpan end4 = new TimeSpan(22, 0);
        
        ////Checker initialization
        Entry entry1 = new Entry("Test 1", WORKINGDAY_VALID, start1, end1, ZERO_TS);
        Entry entry2 = new Entry("Test 2", WORKINGDAY_VALID, start2, end2, ZERO_TS);
        Entry entry3 = new Entry("Test 3", WORKINGDAY_VALID, start3, end3, ZERO_TS);
        Entry entry4 = new Entry("Test 4", WORKINGDAY_VALID, start4, end4, ZERO_TS);
        Entry[] entries = {entry1, entry2, entry3, entry4};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, ZERO_TS, ZERO_TS, ZERO_TS);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);

        ////Execution
        checker.checkDayTimeExceedance();

        ////Expectation
        String error = String.format(MiLoGChecker.CheckerErrorMessage.DAY_TIME_EXCEEDANCE.getErrorMessage(), 
                MiLoGChecker.getWorkdayMaxWorkingTime(), WORKINGDAY_VALID);
        TimeSpan summedTime = entry1.getWorkingTime()
                .add(entry2.getWorkingTime())
                .add(entry3.getWorkingTime())
                .add(entry4.getWorkingTime());
        
        ////Assertions: Basics
        assertTrue(MiLoGChecker.getWorkdayMaxWorkingTime().compareTo(summedTime) < 0);
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        
        ////Assertions: Uniqueness
        assertTrue(checker.getErrors().stream().filter(item -> item.getErrorMessage().equals(error)).count() == 1L);
    }
}
