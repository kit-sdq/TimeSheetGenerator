package data;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeSpanCommonTest {

    @Test
    public void testConstructor1() {
        TimeSpan ts = new TimeSpan(22, 17);
        
        assertEquals(ts.getHour(), 22);
        assertEquals(ts.getMinute(), 17);
    }
    
    @Test
    public void testConstructor2() {
        TimeSpan ts = new TimeSpan(0, 0);
        
        assertEquals(ts.getHour(), 0);
        assertEquals(ts.getMinute(), 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor3() {
        new TimeSpan(-1, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor4() {
        new TimeSpan(1, -3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor5() {
        new TimeSpan(2, 60);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor6() {
        new TimeSpan(0, 322);
    }
    
    @Test
    public void testToString1() {
        TimeSpan ts = new TimeSpan(0, 0);
        
        assertEquals(ts.toString(), "00:00");
    }
    
    @Test
    public void testToString2() {
        TimeSpan ts = new TimeSpan(2, 0);
        
        assertEquals(ts.toString(), "02:00");
    }
    
    @Test
    public void testToString3() {
        TimeSpan ts = new TimeSpan(0, 4);
        
        assertEquals(ts.toString(), "00:04");
    }
    
    @Test
    public void testToString4() {
        TimeSpan ts = new TimeSpan(20, 9);
        
        assertEquals(ts.toString(), "20:09");
    }
    
    @Test
    public void testToString5() {
        TimeSpan ts = new TimeSpan(7, 36);
        
        assertEquals(ts.toString(), "07:36");
    }
    
    @Test
    public void testToString6() {
        TimeSpan ts = new TimeSpan(56, 13);
        
        assertEquals(ts.toString(), "56:13");
    }
    
    @Test
    public void testToString7() {
        TimeSpan ts = new TimeSpan(102, 7);
        
        assertEquals(ts.toString(), "102:07");
    }
    
    @Test
    public void testToString8() {
        TimeSpan ts = new TimeSpan(1923, 0);
        
        assertEquals(ts.toString(), "1923:00");
    }
    
}
