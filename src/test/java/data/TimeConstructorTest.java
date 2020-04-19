package data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TimeConstructorTest {

    @Test
    public void testConstructorTotalMinutes() {
        Time time = new TestTime(16);
        
        assertEquals(16, time.getTotalMinutes());
    }
    
    @Test
    public void testConstructorTotalMinutesGreater60() {
        Time time = new TestTime(81);
        
        assertEquals(81, time.getTotalMinutes());
    }
    
    @Test
    public void testConstructorTotalMinutesNegative() {
        Time time = new TestTime(-7);
        
        assertEquals(-7, time.getTotalMinutes());
    }
    
    @Test
    public void testConstructorHourMinute() {
        Time time = new TestTime(2, 43, true);
        
        assertEquals(2, time.getHour());
        assertEquals(43, time.getMinute());
        assertEquals(true, time.isPositive());
    }
    
    @Test
    public void testConstructorHourMinuteCarry() {
        Time time = new TestTime(2, 71, true);
        
        assertEquals(3, time.getHour());
        assertEquals(11, time.getMinute());
        assertEquals(true, time.isPositive());
    }
    
    @Test
    public void testConstructorHourMinuteZeroPositive() {
        Time time = new TestTime(0, 0, true);
        
        assertEquals(0, time.getHour());
        assertEquals(0, time.getMinute());
        assertEquals(true, time.isZero());
        assertEquals(false, time.isPositive());
        assertEquals(false, time.isNegative());
    }
    
    @Test
    public void testConstructorHourMinuteZeroNegative() {
        Time time = new TestTime(0, 0, false);
        
        assertEquals(0, time.getHour());
        assertEquals(0, time.getMinute());
        assertEquals(true, time.isZero());
        assertEquals(false, time.isPositive());
        assertEquals(false, time.isNegative());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorHourMinuteNegativeHourPositive() {
        new TestTime(-2, 43, true);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorHourMinuteNegativeHourNegative() {
        new TestTime(-2, 43, false);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorHourMinuteNegativeMinutePositive() {
        new TestTime(2, -43, true);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorHourMinuteNegativeMinuteNegative() {
        new TestTime(2, -43, false);
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
