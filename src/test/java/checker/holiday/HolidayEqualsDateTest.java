package checker.holiday;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Random;

import org.junit.Test;

public class HolidayEqualsDateTest {

    private static final int RANDOM_YEAR_BOUND = 200;
    private static final int RANDOM_MONTH_BOUND = 11;
    private static final int RANDOM_DAY_BOUND = 28;
    private static final int MULTIPLE_TEST_ITERATIONS = 10000;
    
    @Test
    public void testEqualDates() {
        ////Test values
        LocalDate localDate = LocalDate.of(2019, 11, 28);
        LocalDate holidayDate = LocalDate.of(2019, 11, 28);
        
        ////Holiday initialization
        Holiday holiday = new Holiday(holidayDate, "");
        
        ////Assertions
        assertEquals(true, holiday.equalsDate(localDate));
    }
    
    @Test
    public void testNotEqualDates() {
        ////Test values
        LocalDate localDate = LocalDate.of(2019, 11, 29);
        LocalDate holidayDate = LocalDate.of(2019, 11, 28);
        
        ////Holiday initialization
        Holiday holiday = new Holiday(holidayDate, "");
        
        ////Assertions
        assertEquals(false, holiday.equalsDate(localDate));
    }

    @Test
    public void testFixpointRandom() {
        ////Random
        Random rand = new Random();
        
        ////Test values
        int year = rand.nextInt(RANDOM_YEAR_BOUND) + 1950;
        int fixMonth = rand.nextInt(RANDOM_MONTH_BOUND) + 1;
        int fixDay = rand.nextInt(RANDOM_DAY_BOUND) + 1;
        LocalDate fixDate = LocalDate.of(year, fixMonth, fixDay);
        
        for (int i = 0; i < MULTIPLE_TEST_ITERATIONS; i++) {
            int month = rand.nextInt(RANDOM_MONTH_BOUND) + 1;
            int day = rand.nextInt(RANDOM_DAY_BOUND) + 1;
            LocalDate dynDate = LocalDate.of(year, month, day);
            
            ////Holiday initialization
            Holiday holiday = new Holiday(dynDate, "");
            
            ////Assertions
            assertEquals(fixDate.equals(dynDate), holiday.equalsDate(fixDate));
        }
        
    }
    
}
