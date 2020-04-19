package data;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

public class EntryArithmeticTest {

    private static LocalDate DATE_NOW = LocalDate.now();
    
    @Test
    public void testGetWorkingTime1() {
        TimeSpan start = new TimeSpan(14, 0);
        TimeSpan end = new TimeSpan(18, 0);
        TimeSpan pause = new TimeSpan(0, 30);
        Entry entry = new Entry("Test", DATE_NOW, start, end, pause, false);
        
        TimeSpan workingTime = entry.getWorkingTime();
        assertEquals(workingTime.getHour(), 3);
        assertEquals(workingTime.getMinute(), 30);
    }
    
    @Test
    public void testGetWorkingTime2() {
        TimeSpan start = new TimeSpan(17, 0);
        TimeSpan end = new TimeSpan(20, 30);
        TimeSpan pause = new TimeSpan(0, 0);
        Entry entry = new Entry("Test", DATE_NOW, start, end, pause, false);
        
        TimeSpan workingTime = entry.getWorkingTime();
        assertEquals(workingTime.getHour(), 3);
        assertEquals(workingTime.getMinute(), 30);
    }
    
    @Test
    public void testGetWorkingTime3() {
        TimeSpan start = new TimeSpan(12, 30);
        TimeSpan end = new TimeSpan(21, 0);
        TimeSpan pause = new TimeSpan(0, 30);
        Entry entry = new Entry("Test", DATE_NOW, start, end, pause, false);
        
        TimeSpan workingTime = entry.getWorkingTime();
        assertEquals(workingTime.getHour(), 8);
        assertEquals(workingTime.getMinute(), 0);
    }
    
    @Test
    public void testGetWorkingTime4() {
        TimeSpan start = new TimeSpan(13, 0);
        TimeSpan end = new TimeSpan(21, 25);
        TimeSpan pause = new TimeSpan(0, 30);
        Entry entry = new Entry("Test", DATE_NOW, start, end, pause, false);
        
        TimeSpan workingTime = entry.getWorkingTime();
        assertEquals(workingTime.getHour(), 7);
        assertEquals(workingTime.getMinute(), 55);
    }
    
    @Test
    public void testGetWorkingTime5() {
        TimeSpan start = new TimeSpan(19, 30);
        TimeSpan end = new TimeSpan(20, 0);
        TimeSpan pause = new TimeSpan(0, 0);
        Entry entry = new Entry("Test", DATE_NOW, start, end, pause, false);
        
        TimeSpan workingTime = entry.getWorkingTime();
        assertEquals(workingTime.getHour(), 0);
        assertEquals(workingTime.getMinute(), 30);
    }
    
    @Test
    public void testGetWorkingTime6() {
        TimeSpan start = new TimeSpan(13, 0);
        TimeSpan end = new TimeSpan(23, 0);
        TimeSpan pause = new TimeSpan(5, 0);
        Entry entry = new Entry("Test", DATE_NOW, start, end, pause, false);
        
        TimeSpan workingTime = entry.getWorkingTime();
        assertEquals(workingTime.getHour(), 5);
        assertEquals(workingTime.getMinute(), 0);
    }
    
    @Test
    public void testGetWorkingTimeVacation() {
        TimeSpan start = new TimeSpan(9, 0);
        TimeSpan end = new TimeSpan(12, 0);
        TimeSpan pause = new TimeSpan(0, 0);
        Entry entry = new Entry("Test", DATE_NOW, start, end, pause, true);
        
        TimeSpan workingTime = entry.getWorkingTime();
        assertEquals(workingTime.getHour(), 3);
        assertEquals(workingTime.getMinute(), 0);
    }
    
}
