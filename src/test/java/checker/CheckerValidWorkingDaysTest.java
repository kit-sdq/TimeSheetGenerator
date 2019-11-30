package checker;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import checker.holiday.HolidayFetchException;
import checker.holiday.IHolidayChecker;
import checker.holiday.GermanState;
import checker.holiday.GermanyHolidayChecker;
import data.Employee;
import data.Entry;
import data.TimeSheet;
import data.Profession;
import data.TimeSpan;
import data.WorkingArea;

/**
 * ATTENTION: This test class only runs correctly if the calling machine is connected to the Internet. 
 */
public class CheckerValidWorkingDaysTest {

    ////Placeholder for documentation construction
    private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
    private static final Profession PROFESSION = new Profession("Fakultät für Informatik", WorkingArea.UB, new TimeSpan(40, 0), 10.31);
    private static final TimeSpan zeroTs = new TimeSpan(0, 0);
    
    @BeforeClass
    public static void checkInternetConnection() {
        try {
            InetAddress inetAdress = InetAddress.getByName("kit.edu");
            inetAdress.isReachable(1000);
        } catch (UnknownHostException e) {
            fail("Could not establish Internet connection.");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testValidWorkingDay() throws CheckerException {
        ////Test values
        TimeSpan start = new TimeSpan(0, 0);
        TimeSpan end = new TimeSpan(0, 0);
        TimeSpan pause = new TimeSpan(0, 0);
        YearMonth yearMonth = YearMonth.of(2019, Month.NOVEMBER);
        LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 22); //Friday, 22. November 2019, no Holiday in BW Germany
        
        ////Checker initialization
        Entry entry = new Entry("Test", date, start, end, pause);
        Entry[] entries = {entry};
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, PROFESSION, yearMonth, entries, zeroTs, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(fullDoc);
        
        ////Executions
        checker.checkValidWorkingDays();
        
        ////Assertions
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }

    @Test
    public void testNewYearsDay2019() throws CheckerException {
        ////Test values
        TimeSpan start = new TimeSpan(0, 0);
        TimeSpan end = new TimeSpan(0, 0);
        TimeSpan pause = new TimeSpan(0, 0);
        YearMonth yearMonth = YearMonth.of(2019, Month.JANUARY);
        LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1); //New years day: Holiday in BW Germany
        
        ////Checker initialization
        Entry entry = new Entry("Test", date, start, end, pause);
        Entry[] entries = {entry};
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, PROFESSION, yearMonth, entries, zeroTs, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(fullDoc);
        
        ////Executions
        checker.checkValidWorkingDays();
        
        ////Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(MiLoGChecker.CheckerErrorMessage.TIME_HOLIDAY.getErrorMessage())));
    }
    
    @Test
    public void testChristmas2020() throws CheckerException {
        ////Test values
        TimeSpan start = new TimeSpan(0, 0);
        TimeSpan end = new TimeSpan(0, 0);
        TimeSpan pause = new TimeSpan(0, 0);
        YearMonth yearMonth = YearMonth.of(2020, Month.DECEMBER);
        LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 25);
        
        ////Checker initialization
        Entry entry = new Entry("Test", date, start, end, pause);
        Entry[] entries = {entry};
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, PROFESSION, yearMonth, entries, zeroTs, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(fullDoc);
        
        ////Executions
        checker.checkValidWorkingDays();
        
        ////Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(MiLoGChecker.CheckerErrorMessage.TIME_HOLIDAY.getErrorMessage())));
    }
    
    @Test
    public void testChristmas2022() throws CheckerException {
        ////Test values
        TimeSpan start = new TimeSpan(0, 0);
        TimeSpan end = new TimeSpan(0, 0);
        TimeSpan pause = new TimeSpan(0, 0);
        YearMonth yearMonth = YearMonth.of(2022, Month.DECEMBER);
        LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 25); //Its a Sunday and Holiday
        
        ////Checker initialization
        Entry entry = new Entry("Test", date, start, end, pause);
        Entry[] entries = {entry};
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, PROFESSION, yearMonth, entries, zeroTs, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(fullDoc);
        
        ////Executions
        checker.checkValidWorkingDays();
        
        /*
         * We assert Sunday instead of Holiday here because the possibility for a Sunday is much higher then for a holiday
         * and therefore this check is done beforehand.
         */
        ////Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(MiLoGChecker.CheckerErrorMessage.TIME_SUNDAY.getErrorMessage())));
    }
    
    @Test
    public void testRandomDayBW() throws CheckerException, HolidayFetchException {
        ////Random
        Random rand = new Random();
        int randYear = (rand.nextInt(40) + 1990);
        int randMonth = (rand.nextInt(12) + 1);
        int randDay = (rand.nextInt(28) + 1); //To guarantee that the date exists. It is a day between 1 and 28 incl.
        
        ////Test values
        TimeSpan start = new TimeSpan(0, 0);
        TimeSpan end = new TimeSpan(0, 0);
        TimeSpan pause = new TimeSpan(0, 0);
        YearMonth yearMonth = YearMonth.of(randYear, randMonth);
        LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), randDay);
        GermanState state = GermanState.BW;
        
        ////Checker initialization
        Entry entry = new Entry("Test", date, start, end, pause);
        Entry[] entries = {entry};
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, PROFESSION, yearMonth, entries, zeroTs, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(fullDoc);
        
        ////Assertions
        LocalDate randDate = LocalDate.of(randYear, randMonth, randDay);
        IHolidayChecker holidayChecker = new GermanyHolidayChecker(randDate.getYear(), state);
        
        ////Executions
        checker.checkValidWorkingDays();
        
        ////Assertions
        if (randDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            assertEquals(CheckerReturn.INVALID, checker.getResult());
            assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(MiLoGChecker.CheckerErrorMessage.TIME_SUNDAY.getErrorMessage())));
        } else if (holidayChecker.isHoliday(randDate)) {
            assertEquals(CheckerReturn.INVALID, checker.getResult());
            assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(MiLoGChecker.CheckerErrorMessage.TIME_HOLIDAY.getErrorMessage())));
        } else {
            assertEquals(CheckerReturn.VALID, checker.getResult());
            assertTrue(checker.getErrors().isEmpty());
        }
        
    }
}
