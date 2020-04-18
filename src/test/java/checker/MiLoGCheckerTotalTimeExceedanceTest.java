package checker;

import static org.junit.Assert.*;

import java.time.LocalDate;
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

public class MiLoGCheckerTotalTimeExceedanceTest {
    
    private static final int RANDOM_HOUR_BOUND = 999;
    private static final int RANDOM_DAY_BOUND = 24; 
    //Exclusively. Refer to https://docs.oracle.com/javase/8/docs/api/java/util/Random.html
    private static final int RANDOM_MINUTES_BOUND = 60;
    
    ////Placeholder for time sheet construction
    private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
    private static final YearMonth YEAR_MONTH = YearMonth.of(2019, Month.NOVEMBER);
    private static final TimeSpan zeroTs = new TimeSpan(0, 0);
    
    @Test
    public void testNoExceedanceLowerBound() {
        //Test values
        TimeSpan maxWorkTime = new TimeSpan(22, 0);
        int hoursToWork = 0;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22),
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, 0), new TimeSpan(0, 0), false);
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Execution
        checker.checkTotalTimeExceedance();
        
        //Assertions
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }
    
    @Test
    public void testNoExceedanceUpperBound() {
        //Test values
        TimeSpan maxWorkTime = new TimeSpan(22, 0);
        int hoursToWork = 22;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22),
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, 0), new TimeSpan(0, 0), false);
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Execution
        checker.checkTotalTimeExceedance();
        
        //Assertions
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }

    @Test
    public void testExceedanceMinutes() {
        //Test values
        TimeSpan maxWorkTime = new TimeSpan(14, 0);
        int hoursToWork = 14;
        int minutesToWork = 1;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22), 
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, minutesToWork), new TimeSpan(0, 0), false);
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Executions
        checker.checkTotalTimeExceedance();
        
        ////Expectation
        String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TOTAL_TIME_EXCEEDANCE.getErrorMessage( 
                maxWorkTime, new TimeSpan(0, 1));
        
        //Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
    }
    
    @Test
    public void testExceedanceHours() {
        //Test values
        TimeSpan maxWorkTime = new TimeSpan(14, 0);
        int hoursToWork = 15;
        int minutesToWork = 0;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22), 
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, minutesToWork), new TimeSpan(0, 0), false);
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Executions
        checker.checkTotalTimeExceedance();
        
        ////Expectation
        String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TOTAL_TIME_EXCEEDANCE.getErrorMessage( 
                maxWorkTime, new TimeSpan(1, 0));
        
        //Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
    }
    
    @Test
    public void testNoExceedanceSuccTransferUpperBound() {
        //Test values
        TimeSpan maxWorkTime = new TimeSpan(14, 0);
        TimeSpan succTransfer = new TimeSpan(4, 0);
        int hoursToWork = 18;
        int minutesToWork = 0;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22), 
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, minutesToWork), new TimeSpan(0, 0), false);
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, succTransfer, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Executions
        checker.checkTotalTimeExceedance();
        
        //Assertions
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }
    
    @Test
    public void testExceedanceSuccTransferLowerBound() {
        //Test values
        TimeSpan maxWorkTime = new TimeSpan(14, 0);
        TimeSpan succTransfer = new TimeSpan(3, 59);
        int hoursToWork = 18;
        int minutesToWork = 0;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22), 
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, minutesToWork), new TimeSpan(0, 0), false);
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, succTransfer, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Executions
        checker.checkTotalTimeExceedance();
        
        ////Expectation
        String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TOTAL_TIME_EXCEEDANCE.getErrorMessage( 
                maxWorkTime, new TimeSpan(0, 1));
        
        //Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
    }
    
    @Test
    public void testNoExceedanceVacationUpperBound() {
        //Test values
        TimeSpan maxWorkTime = new TimeSpan(14, 0);
        int hoursVacation = 1;
        int minutesVacation = 0;
        int hoursToWork = 13;
        int minutesToWork = 0;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22), 
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, minutesToWork), new TimeSpan(0, 0), false);
        Entry entry2 = new Entry("Test 2", LocalDate.of(2019, 11, 29),
                new TimeSpan(0, 0), new TimeSpan(hoursVacation, minutesVacation), new TimeSpan(0, 0), true);
        Entry[] entries = {entry1, entry2};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Executions
        checker.checkTotalTimeExceedance();
        
        //Assertions
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }
    
    @Test
    public void testExceedanceCausedByVacationLowerBound() {
        //Test values
        TimeSpan maxWorkTime = new TimeSpan(14, 0);
        int hoursVacation = 0;
        int minutesVacation = 1;
        int hoursToWork = 14;
        int minutesToWork = 0;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22), 
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, minutesToWork), new TimeSpan(0, 0), false);
        Entry entry2 = new Entry("Test 2", LocalDate.of(2019, 11, 29),
                new TimeSpan(0, 0), new TimeSpan(hoursVacation, minutesVacation), new TimeSpan(0, 0), true);
        Entry[] entries = {entry1, entry2};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Executions
        checker.checkTotalTimeExceedance();
        
        ////Expectation
        String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TOTAL_TIME_EXCEEDANCE.getErrorMessage( 
                maxWorkTime, new TimeSpan(0, 1));
        
        //Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
    }
    
    @Test
    public void testExceedanceCausedByVacationHours() {
        //Test values
        TimeSpan maxWorkTime = new TimeSpan(14, 0);
        int hoursVacation = 2;
        int minutesVacation = 0;
        int hoursToWork = 13;
        int minutesToWork = 0;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22), 
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, minutesToWork), new TimeSpan(0, 0), false);
        Entry entry2 = new Entry("Test 2", LocalDate.of(2019, 11, 29),
                new TimeSpan(0, 0), new TimeSpan(hoursVacation, minutesVacation), new TimeSpan(0, 0), true);
        Entry[] entries = {entry1, entry2};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Executions
        checker.checkTotalTimeExceedance();
        
        ////Expectation
        String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TOTAL_TIME_EXCEEDANCE.getErrorMessage( 
                maxWorkTime, new TimeSpan(1, 0));
        
        //Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
    }
    
    @Test
    public void testExceedanceCausedByPredTransferHours() {
        //Test values
        TimeSpan maxWorkTime = new TimeSpan(14, 0);
        TimeSpan predTransfer = new TimeSpan(2, 0);
        int hoursToWork = 13;
        int minutesToWork = 0;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22), 
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, minutesToWork), new TimeSpan(0, 0), false);
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, predTransfer);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Executions
        checker.checkTotalTimeExceedance();
        
        ////Expectation
        String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TOTAL_TIME_EXCEEDANCE.getErrorMessage( 
                maxWorkTime, new TimeSpan(1, 0));
        
        //Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
    }
    
    @Test
    public void testExceedanceCausedByPredTransferLowerBound() {
        //Test values
        TimeSpan maxWorkTime = new TimeSpan(14, 0);
        TimeSpan predTransfer = new TimeSpan(0, 1);
        int hoursToWork = 14;
        int minutesToWork = 0;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22), 
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, minutesToWork), new TimeSpan(0, 0), false);
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, predTransfer);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Executions
        checker.checkTotalTimeExceedance();
        
        ////Expectation
        String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TOTAL_TIME_EXCEEDANCE.getErrorMessage( 
                maxWorkTime, new TimeSpan(0, 1));
        
        //Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
    }
    
    @Test
    public void testExceedancePredTransferVacationLowerBound() {
        //Test values
        TimeSpan maxWorkTime = new TimeSpan(14, 0);
        TimeSpan predTransfer = new TimeSpan(1, 30);
        int hoursVacation = 2;
        int minutesVacation = 31;
        int hoursToWork = 10;
        int minutesToWork = 0;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22), 
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, minutesToWork), new TimeSpan(0, 0), false);
        Entry entry2 = new Entry("Test 2", LocalDate.of(2019, 11, 29),
                new TimeSpan(0, 0), new TimeSpan(hoursVacation, minutesVacation), new TimeSpan(0, 0), true);
        Entry[] entries = {entry1, entry2};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, predTransfer);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Executions
        checker.checkTotalTimeExceedance();
        
        ////Expectation
        String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TOTAL_TIME_EXCEEDANCE.getErrorMessage( 
                maxWorkTime, new TimeSpan(0, 1));
        
        //Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
    }
    
    @Test
    public void testNoExceedancePredTransferVacationUpperBound() {
        //Test values
        TimeSpan maxWorkTime = new TimeSpan(14, 0);
        TimeSpan predTransfer = new TimeSpan(1, 30);
        int hoursVacation = 2;
        int minutesVacation = 30;
        int hoursToWork = 10;
        int minutesToWork = 0;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22), 
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, minutesToWork), new TimeSpan(0, 0), false);
        Entry entry2 = new Entry("Test 2", LocalDate.of(2019, 11, 29),
                new TimeSpan(0, 0), new TimeSpan(hoursVacation, minutesVacation), new TimeSpan(0, 0), true);
        Entry[] entries = {entry1, entry2};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, predTransfer);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Executions
        checker.checkTotalTimeExceedance();
        
        //Assertions
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }
    
    @Test
    public void testNoExceedancePredSuccTransferVacationLowerBound() {
        //Test values
        TimeSpan maxWorkTime = new TimeSpan(14, 0);
        TimeSpan succTransfer = new TimeSpan(4, 0);
        TimeSpan predTransfer = new TimeSpan(1, 30);
        int hoursVacation = 2;
        int minutesVacation = 30;
        int hoursToWork = 14;
        int minutesToWork = 0;
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22), 
                new TimeSpan(0, 0), new TimeSpan(hoursToWork, minutesToWork), new TimeSpan(0, 0), false);
        Entry entry2 = new Entry("Test 2", LocalDate.of(2019, 11, 29),
                new TimeSpan(0, 0), new TimeSpan(hoursVacation, minutesVacation), new TimeSpan(0, 0), true);
        Entry[] entries = {entry1, entry2};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, succTransfer, predTransfer);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Executions
        checker.checkTotalTimeExceedance();
        
        //Assertions
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }
    
    @Test
    public void testExceedanceRandomHoursWithoutPause() {
        //Random
        Random rand = new Random();
        
        //Test values
        int maxWorkHours = rand.nextInt(RANDOM_DAY_BOUND);
        TimeSpan maxWorkTime = new TimeSpan(maxWorkHours, 0);
        TimeSpan start = new TimeSpan(0, 0);
        TimeSpan end = new TimeSpan(rand.nextInt(RANDOM_DAY_BOUND), 0);
        TimeSpan pause = new TimeSpan(0, 0);
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22), start, end, pause, false);
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Execution
        checker.checkTotalTimeExceedance();
        
        //Assertions
        if (maxWorkTime.compareTo(end) < 0) {
            ////Expectation
            String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TOTAL_TIME_EXCEEDANCE.getErrorMessage( 
                    maxWorkTime, end.subtract(maxWorkTime));
            
            assertEquals(CheckerReturn.INVALID, checker.getResult());
            assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
        } else {
            assertEquals(CheckerReturn.VALID, checker.getResult());
            assertTrue(checker.getErrors().isEmpty());
        }
    }
    
    @Test
    public void testExceedanceRandomMinutesWithoutPause() {
        //Random
        Random rand = new Random();
        
        //Test values
        int maxWorkHours = rand.nextInt(RANDOM_DAY_BOUND);
        TimeSpan maxWorkTime = new TimeSpan(maxWorkHours, 0);
        TimeSpan start = new TimeSpan(0, 0);
        TimeSpan end = new TimeSpan(maxWorkHours, rand.nextInt(RANDOM_MINUTES_BOUND));
        TimeSpan pause = new TimeSpan(0, 0);
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22), start, end, pause, false);
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Execution
        checker.checkTotalTimeExceedance();
        
        //Assertions
        if (end.getMinute() > 0) {
            ////Expectation
            String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TOTAL_TIME_EXCEEDANCE.getErrorMessage( 
                    maxWorkTime, end.subtract(maxWorkTime));
            
            assertEquals(CheckerReturn.INVALID, checker.getResult());
            assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
        } else {
            assertEquals(CheckerReturn.VALID, checker.getResult());
            assertTrue(checker.getErrors().isEmpty());
        }
    }
    
    @Test
    public void testExceedanceRandomWithoutPause() {
        //Random
        Random rand = new Random();
        
        //Test values
        int maxWorkHours = rand.nextInt(RANDOM_DAY_BOUND);
        TimeSpan maxWorkTime = new TimeSpan(maxWorkHours, 0);
        TimeSpan start = new TimeSpan(0, 0);
        TimeSpan end = new TimeSpan(rand.nextInt(RANDOM_DAY_BOUND), rand.nextInt(RANDOM_MINUTES_BOUND));
        TimeSpan pause = new TimeSpan(0, 0);
        
        //Checker initialization
        Entry entry1 = new Entry("Test 1", LocalDate.of(2019, 11, 22), start, end, pause, false);
        Entry[] entries = {entry1};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Execution
        checker.checkTotalTimeExceedance();
        
        //Assertions
        if (end.compareTo(maxWorkTime) > 0) {
            ////Expectation
            String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TOTAL_TIME_EXCEEDANCE.getErrorMessage( 
                    maxWorkTime, end.subtract(maxWorkTime));
            
            assertEquals(CheckerReturn.INVALID, checker.getResult());
            assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
        } else {
            assertEquals(CheckerReturn.VALID, checker.getResult());
            assertTrue(checker.getErrors().isEmpty());
        }
    }
    
    @Test
    public void testExceedanceRandom() {
        ////Random
        Random rand = new Random();
        
        ////Test values
        int maxWorkHours = rand.nextInt(RANDOM_DAY_BOUND);
        TimeSpan maxWorkTime = new TimeSpan(maxWorkHours, 0);
        TimeSpan start = new TimeSpan(0, 0);
        //The end hour has to be greater than 1 to guarantee that the pause is not longer than the work
        TimeSpan end = new TimeSpan(rand.nextInt(RANDOM_DAY_BOUND - 1) + 1, rand.nextInt(RANDOM_MINUTES_BOUND));
        TimeSpan pause = new TimeSpan((rand.nextInt(end.getHour())), rand.nextInt(RANDOM_MINUTES_BOUND));
        
        ////Checker initialization
        Entry entry = new Entry("Test 1", LocalDate.of(2019, 11, 22), start, end, pause, false);
        Entry[] entries = {entry};
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Execution
        checker.checkTotalTimeExceedance();
        
        ////Assertions
        TimeSpan workingTime = entry.getWorkingTime();
        if (workingTime.compareTo(maxWorkTime) > 0) {
            ////Expectation
            String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TOTAL_TIME_EXCEEDANCE.getErrorMessage( 
                    maxWorkTime, timeSheet.getTotalWorkTime().subtract(maxWorkTime));
                
            assertEquals(CheckerReturn.INVALID, checker.getResult());
            assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
        } else {
            assertEquals(CheckerReturn.VALID, checker.getResult());
            assertTrue(checker.getErrors().isEmpty());
        }
    }
    
    @Test
    public void testExceedanceRandomMultipleEntries() {
        ////Random
        Random rand = new Random();
        
        ////Test values
        int maxWorkHours = rand.nextInt(RANDOM_HOUR_BOUND);
        TimeSpan maxWorkTime = new TimeSpan(maxWorkHours, 0);
        int numberOfEntries = rand.nextInt(50) + 1; //May not be zero!
        
        ////Entry generator
        Entry[] entries = new Entry[numberOfEntries];
        for (int i = 0; i < numberOfEntries; i++) {
            TimeSpan start = new TimeSpan(0, 0);
            //The end hour has to be greater than 1 to guarantee that the pause is not longer than the work
            TimeSpan end = new TimeSpan(rand.nextInt(RANDOM_DAY_BOUND - 1) + 1, rand.nextInt(RANDOM_MINUTES_BOUND));
            TimeSpan pause = new TimeSpan((rand.nextInt(end.getHour())), rand.nextInt(RANDOM_MINUTES_BOUND));
            
            Entry entry = new Entry("Test", LocalDate.of(2019, 11, 22), start, end, pause, false);
            entries[i] = entry;
        }
        
        ////Checker initialization
        Profession profession = new Profession("Fakultät für Informatik", WorkingArea.UB, maxWorkTime, 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        //Execution
        checker.checkTotalTimeExceedance();
        
        ////Assertions
        if (timeSheet.getTotalWorkTime().compareTo(maxWorkTime) > 0) {
            ////Expectation
            String error = MiLoGChecker.MiLoGCheckerErrorMessageProvider.TOTAL_TIME_EXCEEDANCE.getErrorMessage( 
                    maxWorkTime, timeSheet.getTotalWorkTime().subtract(maxWorkTime));
            
            assertEquals(CheckerReturn.INVALID, checker.getResult());
            assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(error)));
        } else {
            assertEquals(CheckerReturn.VALID, checker.getResult());
            assertTrue(checker.getErrors().isEmpty());
        }
    }
}
