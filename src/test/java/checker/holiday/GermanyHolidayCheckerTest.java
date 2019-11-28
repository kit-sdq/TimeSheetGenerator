package checker.holiday;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.junit.Test;

public class GermanyHolidayCheckerTest {

    //Exclusively. Refer to https://docs.oracle.com/javase/8/docs/api/java/util/Random.html
    private static final int RANDOM_YEAR_BOUND = 200;
    private static final int RANDOM_MONTH_BOUND = 11;
    private static final int RANDOM_DAY_BOUND = 28;
    private static final int MULTIPLE_TEST_ITERATIONS = 3650;
    
    @Test
    public void testChristmas2019() throws HolidayFetchException {
        ////Test values
        LocalDate localDate = LocalDate.of(2019, 12, 25);
        GermanState state = GermanState.BW;
        
        ////HolidayChecker initialization
        IHolidayChecker holidayChecker = new GermanyHolidayChecker(localDate.getYear(), state);
        
        ////Assertions
        assertEquals(true, holidayChecker.isHoliday(localDate));
    }
    
    @Test
    public void testNewYearsDay2022() throws HolidayFetchException {
        ////Test values
        LocalDate localDate = LocalDate.of(2022, 1, 1);
        GermanState state = GermanState.BW;
        
        ////HolidayChecker initialization
        IHolidayChecker holidayChecker = new GermanyHolidayChecker(localDate.getYear(), state);
        
        ////Assertions
        assertEquals(true, holidayChecker.isHoliday(localDate));
    }

    @Test
    public void testNewYearsDay2100() throws HolidayFetchException {
        ////Test values
        LocalDate localDate = LocalDate.of(2100, 1, 1);
        GermanState state = GermanState.BW;
        
        ////HolidayChecker initialization
        IHolidayChecker holidayChecker = new GermanyHolidayChecker(localDate.getYear(), state);
        
        ////Assertions
        assertEquals(true, holidayChecker.isHoliday(localDate));
    }
    
    @Test
    public void testIsHolidayRandom() throws HolidayFetchException {
        //Random
        Random rand = new Random();
        
        ////Test values
        GermanState state = GermanState.BW;
        int year = rand.nextInt(RANDOM_YEAR_BOUND) + 1950;
        IHolidayChecker holidayChecker = new GermanyHolidayChecker(year, state);
        
        for (int i = 0; i < MULTIPLE_TEST_ITERATIONS; i++) {
            int month = rand.nextInt(RANDOM_MONTH_BOUND) + 1;
            int day = rand.nextInt(RANDOM_DAY_BOUND) + 1;
            LocalDate localDate = LocalDate.of(year, month, day);
            
            ////HolidayChecker initialization
            Collection<Holiday> holidays = holidayChecker.getHolidays();
            Collection<LocalDate> holidayDates = new ArrayList<LocalDate>();
            for (Holiday holiday : holidays) {
                holidayDates.add(holiday.getDate());
            }
            
            ////Assertions
            assertFalse(holidays.isEmpty());
            assertEquals(holidayDates.contains(localDate), holidayChecker.isHoliday(localDate));
        }
    }
    
    @Test
    public void testIsHolidayHolidays() throws HolidayFetchException {
        //Random
        Random rand = new Random();
        
        ////Test values
        GermanState state = GermanState.BW;
        int year = rand.nextInt(RANDOM_YEAR_BOUND) + 1950;
        IHolidayChecker holidayChecker = new GermanyHolidayChecker(year, state);
        
        ////HolidayChecker initialization
        Collection<Holiday> holidays = holidayChecker.getHolidays();
        Collection<LocalDate> holidayDates = new ArrayList<LocalDate>();
        for (Holiday holiday : holidays) {
            holidayDates.add(holiday.getDate());
        }
        
        for (LocalDate holidayDate : holidayDates) {
            ////Assertions
            assertFalse(holidays.isEmpty());
            assertEquals(true, holidayChecker.isHoliday(holidayDate));
        }
    }
    
}
