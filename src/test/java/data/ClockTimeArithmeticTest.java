package data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ClockTimeArithmeticTest {

    @Test
    public void addZero() {
        ClockTime clockTime = new ClockTime(16, 15);
        TimeSpan timeSpan = new TimeSpan(0, 0);
        
        ClockTime result = clockTime.add(timeSpan);
        
        assertEquals(16, result.getHour());
        assertEquals(15, result.getMinute());
    }
    
    @Test
    public void addPositive() {
        ClockTime clockTime = new ClockTime(16, 15);
        TimeSpan timeSpan = new TimeSpan(1, 7);
        
        ClockTime result = clockTime.add(timeSpan);
        
        assertEquals(17, result.getHour());
        assertEquals(22, result.getMinute());
    }
    
    @Test
    public void addNegative() {
        ClockTime clockTime = new ClockTime(16, 15);
        TimeSpan timeSpan = new TimeSpan(1, 17).negate();
        
        ClockTime result = clockTime.add(timeSpan);
        
        assertEquals(14, result.getHour());
        assertEquals(58, result.getMinute());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addNextDay() {
        ClockTime clockTime = new ClockTime(16, 15);
        TimeSpan timeSpan = new TimeSpan(8, 55);
        
        clockTime.add(timeSpan);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addPreviousDay() {
        ClockTime clockTime = new ClockTime(16, 15);
        TimeSpan timeSpan = new TimeSpan(16, 30).negate();
        
        clockTime.add(timeSpan);
    }
    
    @Test
    public void subtractZero() {
        ClockTime clockTime = new ClockTime(16, 15);
        TimeSpan timeSpan = new TimeSpan(0, 0);
        
        ClockTime result = clockTime.subtract(timeSpan);
        
        assertEquals(16, result.getHour());
        assertEquals(15, result.getMinute());
    }
    
    @Test
    public void subtractPositive() {
        ClockTime clockTime = new ClockTime(16, 15);
        TimeSpan timeSpan = new TimeSpan(1, 7);
        
        ClockTime result = clockTime.subtract(timeSpan);
        
        assertEquals(15, result.getHour());
        assertEquals(8, result.getMinute());
    }
    
    @Test
    public void subtractNegative() {
        ClockTime clockTime = new ClockTime(16, 15);
        TimeSpan timeSpan = new TimeSpan(1, 17).negate();
        
        ClockTime result = clockTime.subtract(timeSpan);
        
        assertEquals(17, result.getHour());
        assertEquals(32, result.getMinute());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void subtractNextDay() {
        ClockTime clockTime = new ClockTime(16, 15);
        TimeSpan timeSpan = new TimeSpan(8, 55).negate();
        
        clockTime.subtract(timeSpan);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void subtractPreviousDay() {
        ClockTime clockTime = new ClockTime(16, 15);
        TimeSpan timeSpan = new TimeSpan(16, 30);
        
        clockTime.subtract(timeSpan);
    }
    
}
