package checker.holiday;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import utils.randomtest.RandomParameterExtension;
import utils.randomtest.RandomParameterExtension.RandomInt;
import utils.randomtest.RandomParameterExtension.RandomLocalDate;
import utils.randomtest.RandomTestExtension.RandomTest;

@ExtendWith(RandomParameterExtension.class)
public class GermanyHolidayCheckerTest {

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
    
    @RandomTest(iterations = 10)
    public void testIsHolidayRandom(
        @RandomInt(lowerBound = 1950, upperBound = 2150) int year,
        @RandomLocalDate Iterator<LocalDate> localDates
    ) throws HolidayFetchException {
        ////Test values
        GermanState state = GermanState.BW;
        IHolidayChecker holidayChecker = new GermanyHolidayChecker(year, state);
        
        for (int i = 0; i < 365; i++) {
            LocalDate localDate = localDates.next().withYear(year);
            
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
    
    @RandomTest
    public void testIsHolidayHolidays(
        @RandomInt(lowerBound = 1950, upperBound = 2150) int year
    ) throws HolidayFetchException {
        ////Test values
        GermanState state = GermanState.BW;
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
