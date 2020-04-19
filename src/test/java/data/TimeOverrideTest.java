package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeOverrideTest {
    
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testEqualsDifferentClass() {
        Time time = new TestTime(137);
        
        assertFalse(time.equals("Hello World!"));
    }
    
    @Test
    public void testEqualsDifferentTotalMinutes() {
        Time time = new TestTime(137);
        Time other = new TestTime(81);
        
        assertFalse(time.equals(other));
    }
    
    @Test
    public void testEqualsDifferentHour() {
        Time time = new TestTime(12, 3, true);
        Time other = new TestTime(11, 3, true);
        
        assertFalse(time.equals(other));
    }
    
    @Test
    public void testEqualsDifferentMinutes() {
        Time time = new TestTime(4, 13, true);
        Time other = new TestTime(4, 17, true);
        
        assertFalse(time.equals(other));
    }
    
    @Test
    public void testEqualsDifferentSign() {
        Time time = new TestTime(1, 17, true);
        Time other = new TestTime(1, 17, false);
        
        assertFalse(time.equals(other));
    }
    
    @Test
    public void testEqualsEqual() {
        Time time = new TestTime(234);
        Time other = new TestTime(234);
        
        assertTrue(time.equals(other));
    }
    
    @Test
    public void testCompareToEqual() {
        Time time = new TestTime(234);
        Time other = new TestTime(234);
        
        assertEquals(0, time.compareTo(other));
    }
    
    @Test
    public void testCompareToGreater() {
        Time time = new TestTime(234);
        Time other = new TestTime(117);
        
        assertTrue(time.compareTo(other) > 0);
    }
    
    @Test
    public void testCompareToSmaller() {
        Time time = new TestTime(117);
        Time other = new TestTime(234);
        
        assertTrue(time.compareTo(other) < 1);
    }
    
    @Test
    public void testCompareToNegativeGreater() {
        Time time = new TestTime(-117);
        Time other = new TestTime(-234);
        
        assertTrue(time.compareTo(other) > 0);
    }
    
    @Test
    public void testCompareToNegativeSmaller() {
        Time time = new TestTime(-234);
        Time other = new TestTime(-117);
        
        assertTrue(time.compareTo(other) < 1);
    }
    
    @Test
    public void testToStringZero() {
        Time time = new TestTime(0);
        
        assertEquals("00:00", time.toString());
    }
    
    @Test
    public void testToStringPositive() {
        Time time = new TestTime(143);
        
        assertEquals("02:23", time.toString());
    }
    
    @Test
    public void testToStringNegative() {
        Time time = new TestTime(-143);
        
        assertEquals("-02:23", time.toString());
    }
    
    @Test
    public void testToStringGreater99Hours() {
        Time time = new TestTime(101, 13, true);
        
        assertEquals("101:13", time.toString());
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
