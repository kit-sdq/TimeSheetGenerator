package checker.holiday;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import utils.randomtest.RandomParameterExtension.RandomLocalDate;
import utils.randomtest.RandomTestExtension.RandomTest;
import utils.randomtest.RandomTestExtension.RandomTestClass;

@RandomTestClass
public class HolidayEqualsDateTest {

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

    @RandomTest(iterations = 1000)
    public void testEqualsDateRandom(
        @RandomLocalDate(lowerBoundYear = 1950, upperBoundYear = 2150) LocalDate holidayDate,
        @RandomLocalDate LocalDate localDate
    ) {
        localDate = localDate.withYear(holidayDate.getYear());

        ////Holiday initialization
        Holiday holiday = new Holiday(holidayDate, "");
        
        ////Assertions
        assertEquals(holidayDate.equals(localDate), holiday.equalsDate(localDate));
    }
    
}
