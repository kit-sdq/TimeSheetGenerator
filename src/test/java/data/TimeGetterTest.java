package data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TimeGetterTest {

    @Test
    public void testGetHourMinute() {
        Time time = new TestTime(143);
        
        assertEquals(2, time.getHour());
        assertEquals(23, time.getMinute());
    }
    
    @Test
    public void testGetHourMinuteNegative() {
        Time time = new TestTime(-143);
        
        assertEquals(2, time.getHour());
        assertEquals(23, time.getMinute());
    }
    
    @Test
    public void testGetTotalMinutes() {
        Time time = new TestTime(2, 23, true);
        
        assertEquals(143, time.getTotalMinutes());
    }
    
    @Test
    public void testGetTotalMinutesNegative() {
        Time time = new TestTime(2, 23, false);
        
        assertEquals(-143, time.getTotalMinutes());
    }
    
    @Test
    public void testGetSignZero() {
        Time time = new TestTime(0);
        
        assertEquals(0, time.getSign());
        assertEquals(false, time.isPositive());
        assertEquals(true, time.isZero());
        assertEquals(false, time.isNegative());
    }
    
    @Test
    public void testGetSignPositive() {
        Time time = new TestTime(17);
        
        assertEquals(1, time.getSign());
        assertEquals(true, time.isPositive());
        assertEquals(false, time.isZero());
        assertEquals(false, time.isNegative());
    }
    
    @Test
    public void testGetSignNegative() {
        Time time = new TestTime(-17);
        
        assertEquals(-1, time.getSign());
        assertEquals(false, time.isPositive());
        assertEquals(false, time.isZero());
        assertEquals(true, time.isNegative());
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
