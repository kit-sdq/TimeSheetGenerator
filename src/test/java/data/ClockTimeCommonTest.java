package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClockTimeCommonTest {

    @Test
    public void testConstructor() {
        ClockTime time = new ClockTime(2, 17);
        
        assertEquals(2, time.getHour());
        assertEquals(17, time.getMinute());
        assertTrue(time.isPositive());
    }
    
    @Test
    public void testConstructorZero() {
        ClockTime time = new ClockTime(0, 0);
        
        assertTrue(time.isZero());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMinuteNegative() {
        new ClockTime(2, -17);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMinuteTooLarge() {
        new ClockTime(2, 60);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorHourNegative() {
        new ClockTime(-2, 17);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorHourTooLarge() {
        new ClockTime(24, 17);
    }
    
    @Test
    public void testDifferenceToEqual() {
        ClockTime time = new ClockTime(2, 17);
        ClockTime other = new ClockTime(2, 17);
        
        TimeSpan difference = time.differenceTo(other);
        
        assertTrue(difference.isZero());
    }
    
    @Test
    public void testDifferenceToGreater() {
        ClockTime time = new ClockTime(2, 17);
        ClockTime other = new ClockTime(1, 13);
        
        TimeSpan difference = time.differenceTo(other);
        
        assertTrue(difference.isPositive());
        assertEquals(1, difference.getHour());
        assertEquals(4, difference.getMinute());
    }
    
    @Test
    public void testDifferenceToSmaller() {
        ClockTime time = new ClockTime(1, 13);
        ClockTime other = new ClockTime(2, 17);
        
        TimeSpan difference = time.differenceTo(other);
        
        assertTrue(difference.isNegative());
        assertEquals(1, difference.getHour());
        assertEquals(4, difference.getMinute());
    }
    
    @Test
    public void testComparisonEqual() {
        ClockTime time = new ClockTime(2, 17);
        ClockTime other = new ClockTime(2, 17);
        
        assertEquals(false, time.isAfter(other));
        assertEquals(false, time.isBefore(other));
    }
    
    @Test
    public void testComparisonBefore() {
        ClockTime time = new ClockTime(1, 13);
        ClockTime other = new ClockTime(2, 17);
        
        assertEquals(false, time.isAfter(other));
        assertEquals(true, time.isBefore(other));
    }
    
    @Test
    public void testComparisonAfter() {
        ClockTime time = new ClockTime(2, 17);
        ClockTime other = new ClockTime(1, 13);
        
        assertEquals(true, time.isAfter(other));
        assertEquals(false, time.isBefore(other));
    }
    
    @Test
    public void testParse() {
        ClockTime time = ClockTime.parse("17:34");
        
        assertEquals(17, time.getHour());
        assertEquals(34, time.getMinute());
    }
    
    @Test
    public void testParseZero() {
        ClockTime time = ClockTime.parse("00:00");
        
        assertEquals(0, time.getHour());
        assertEquals(0, time.getMinute());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseNegative() {
        ClockTime.parse("-02:13");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseMinuteTooLarge() {
        ClockTime.parse("02:60");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseHourTooLarge() {
        ClockTime.parse("25:13");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseUpperBoundExclusive() {
        ClockTime.parse("24:00");
    }
    
}
