package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeParseTest {

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmptyString() {
        Time.parse("", totalMinutes -> new TestTime(totalMinutes));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseNonTimeString() {
        Time.parse("Hello World!", totalMinutes -> new TestTime(totalMinutes));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWrongTimeFormat() {
        Time.parse("2:1.111", totalMinutes -> new TestTime(totalMinutes));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWrongSignPlacement() {
        Time.parse("02:-17", totalMinutes -> new TestTime(totalMinutes));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseUnnecessaryPositiveSign() {
        Time.parse("+02:17", totalMinutes -> new TestTime(totalMinutes));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWrongSeparator() {
        Time.parse("02.17", totalMinutes -> new TestTime(totalMinutes));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseMinuteTooLarge() {
        Time.parse("02:74", totalMinutes -> new TestTime(totalMinutes));
    }
    
    @Test
    public void testParseValidTime() {
        TestTime time = Time.parse("02:17", totalMinutes -> new TestTime(totalMinutes));
        
        assertEquals(2, time.getHour());
        assertEquals(17, time.getMinute());
        assertTrue(time.isPositive());
    }
    
    @Test
    public void testParseValidTimeNegative() {
        TestTime time = Time.parse("-02:17", totalMinutes -> new TestTime(totalMinutes));
        
        assertEquals(2, time.getHour());
        assertEquals(17, time.getMinute());
        assertTrue(time.isNegative());
    }
    
    @Test
    public void testParseValidTimeShortNumbers() {
        TestTime time = Time.parse("2:7", totalMinutes -> new TestTime(totalMinutes));
        
        assertEquals(2, time.getHour());
        assertEquals(7, time.getMinute());
        assertTrue(time.isPositive());
    }
    
    @Test
    public void testParseValidTimeHoursGreater99() {
        TestTime time = Time.parse("203:17", totalMinutes -> new TestTime(totalMinutes));
        
        assertEquals(203, time.getHour());
        assertEquals(17, time.getMinute());
        assertTrue(time.isPositive());
    }
    
    private class TestTime extends Time {

        public TestTime(int totalMinutes) {
            super(totalMinutes);
        }
        
        public TestTime(int hours, int minutes, boolean positive) {
            super(hours, minutes, positive);
        }

    }
    
}
