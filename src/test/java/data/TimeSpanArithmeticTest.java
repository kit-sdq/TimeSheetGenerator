package data;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeSpanArithmeticTest {

    @Test
    public void testAddWithoutCarry1() {
        TimeSpan ts1 = new TimeSpan(0, 20);
        TimeSpan ts2 = new TimeSpan(0, 5);
        
        ts1 = ts1.add(ts2);
        assertEquals(ts1.getHour(), 0);
        assertEquals(ts1.getMinute(), 25);
    }
    
    @Test
    public void testAddWithoutCarry2() {
        TimeSpan ts1 = new TimeSpan(0, 0);
        TimeSpan ts2 = new TimeSpan(0, 37);
        
        ts1 = ts1.add(ts2);
        assertEquals(ts1.getHour(), 0);
        assertEquals(ts1.getMinute(), 37);
    }
    
    @Test
    public void testAddWithoutCarry3() {
        TimeSpan ts1 = new TimeSpan(0, 0);
        TimeSpan ts2 = new TimeSpan(0, 0);
        
        ts1 = ts1.add(ts2);
        assertEquals(ts1.getHour(), 0);
        assertEquals(ts1.getMinute(), 0);
    }
    
    @Test
    public void testAddWithCarry1() {
        TimeSpan ts1 = new TimeSpan(11, 26);
        TimeSpan ts2 = new TimeSpan(2, 56);
        
        ts1 = ts1.add(ts2);
        assertEquals(ts1.getHour(), 14);
        assertEquals(ts1.getMinute(), 22);
    }

    @Test
    public void testAddWithCarry2() {
        TimeSpan ts1 = new TimeSpan(0, 56);
        TimeSpan ts2 = new TimeSpan(0, 5);
        
        ts1 = ts1.add(ts2);
        assertEquals(ts1.getHour(), 1);
        assertEquals(ts1.getMinute(), 1);
    }
    
    @Test
    public void testAddWithCarry3() {
        TimeSpan ts1 = new TimeSpan(0, 59);
        TimeSpan ts2 = new TimeSpan(0, 1);
        
        ts1 = ts1.add(ts2);
        assertEquals(ts1.getHour(), 1);
        assertEquals(ts1.getMinute(), 0);
    }
    
    @Test
    public void testSubtractWithoutCarry1() {
        TimeSpan ts1 = new TimeSpan(0, 59);
        TimeSpan ts2 = new TimeSpan(0, 1);
        
        ts1 = ts1.subtract(ts2);
        assertEquals(ts1.getHour(), 0);
        assertEquals(ts1.getMinute(), 58);
    }
    
    @Test
    public void testSubtractWithoutCarry2() {
        TimeSpan ts1 = new TimeSpan(17, 22);
        TimeSpan ts2 = new TimeSpan(12, 16);
        
        ts1 = ts1.subtract(ts2);
        assertEquals(ts1.getHour(), 5);
        assertEquals(ts1.getMinute(), 6);
    }
    
    @Test
    public void testSubtractWithoutCarry3() {
        TimeSpan ts1 = new TimeSpan(57, 2);
        TimeSpan ts2 = new TimeSpan(2, 2);
        
        ts1 = ts1.subtract(ts2);
        assertEquals(ts1.getHour(), 55);
        assertEquals(ts1.getMinute(), 0);
    }
    
    @Test
    public void testSubtractWithoutCarry4() {
        TimeSpan ts1 = new TimeSpan(16, 5);
        TimeSpan ts2 = new TimeSpan(16, 5);
        
        ts1 = ts1.subtract(ts2);
        assertEquals(ts1.getHour(), 0);
        assertEquals(ts1.getMinute(), 0);
    }

    @Test
    public void testSubtractWithCarry1() {
        TimeSpan ts1 = new TimeSpan(16, 5);
        TimeSpan ts2 = new TimeSpan(15, 6);
        
        ts1 = ts1.subtract(ts2);
        assertEquals(ts1.getHour(), 0);
        assertEquals(ts1.getMinute(), 59);
    }
    
    @Test
    public void testSubtractWithCarry2() {
        TimeSpan ts1 = new TimeSpan(11, 26);
        TimeSpan ts2 = new TimeSpan(2, 56);
        
        ts1 = ts1.subtract(ts2);
        assertEquals(ts1.getHour(), 8);
        assertEquals(ts1.getMinute(), 30);
    }
    
    @Test
    public void testSubtractWithCarry3() {
        TimeSpan ts1 = new TimeSpan(5, 0);
        TimeSpan ts2 = new TimeSpan(4, 59);
        
        ts1 = ts1.subtract(ts2);
        assertEquals(ts1.getHour(), 0);
        assertEquals(ts1.getMinute(), 1);
    }
    
    @Test
    public void testSubtractWithCarry4() {
        TimeSpan ts1 = new TimeSpan(40, 58);
        TimeSpan ts2 = new TimeSpan(30, 59);
        
        ts1 = ts1.subtract(ts2);
        assertEquals(ts1.getHour(), 9);
        assertEquals(ts1.getMinute(), 59);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubtractIllegalArgument1() {
        TimeSpan ts1 = new TimeSpan(0, 0);
        TimeSpan ts2 = new TimeSpan(2, 7);
        
        ts1 = ts1.subtract(ts2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubtractIllegalArgument2() {
        TimeSpan ts1 = new TimeSpan(17, 0);
        TimeSpan ts2 = new TimeSpan(17, 5);
        
        ts1 = ts1.subtract(ts2);
    }
}
