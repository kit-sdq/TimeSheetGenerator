package data;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

public class EntryArithmeticTest {

    private static LocalDate DATE_NOW = LocalDate.now();
    
    @Test
    public void testGetWorkingTime1() {
        ClockTime start = new ClockTime(14, 0);
        ClockTime end = new ClockTime(18, 0);
        TimeSpan pause = new TimeSpan(0, 30);
        Entry entry = new Entry("Test", DATE_NOW, start, end, pause, false);
        
        TimeSpan workingTime = entry.getWorkingTime();
        assertEquals(workingTime.getHour(), 3);
        assertEquals(workingTime.getMinute(), 30);
    }
    
    @Test
    public void testGetWorkingTime2() {
        ClockTime start = new ClockTime(17, 0);
        ClockTime end = new ClockTime(20, 30);
        TimeSpan pause = new TimeSpan(0, 0);
        Entry entry = new Entry("Test", DATE_NOW, start, end, pause, false);
        
        TimeSpan workingTime = entry.getWorkingTime();
        assertEquals(workingTime.getHour(), 3);
        assertEquals(workingTime.getMinute(), 30);
    }
    
    @Test
    public void testGetWorkingTime3() {
        ClockTime start = new ClockTime(12, 30);
        ClockTime end = new ClockTime(21, 0);
        TimeSpan pause = new TimeSpan(0, 30);
        Entry entry = new Entry("Test", DATE_NOW, start, end, pause, false);
        
        TimeSpan workingTime = entry.getWorkingTime();
        assertEquals(workingTime.getHour(), 8);
        assertEquals(workingTime.getMinute(), 0);
    }
    
    @Test
    public void testGetWorkingTime4() {
        ClockTime start = new ClockTime(13, 0);
        ClockTime end = new ClockTime(21, 25);
        TimeSpan pause = new TimeSpan(0, 30);
        Entry entry = new Entry("Test", DATE_NOW, start, end, pause, false);
        
        TimeSpan workingTime = entry.getWorkingTime();
        assertEquals(workingTime.getHour(), 7);
        assertEquals(workingTime.getMinute(), 55);
    }
    
    @Test
    public void testGetWorkingTime5() {
        ClockTime start = new ClockTime(19, 30);
        ClockTime end = new ClockTime(20, 0);
        TimeSpan pause = new TimeSpan(0, 0);
        Entry entry = new Entry("Test", DATE_NOW, start, end, pause, false);
        
        TimeSpan workingTime = entry.getWorkingTime();
        assertEquals(workingTime.getHour(), 0);
        assertEquals(workingTime.getMinute(), 30);
    }
    
    @Test
    public void testGetWorkingTime6() {
        ClockTime start = new ClockTime(13, 0);
        ClockTime end = new ClockTime(23, 0);
        TimeSpan pause = new TimeSpan(5, 0);
        Entry entry = new Entry("Test", DATE_NOW, start, end, pause, false);
        
        TimeSpan workingTime = entry.getWorkingTime();
        assertEquals(workingTime.getHour(), 5);
        assertEquals(workingTime.getMinute(), 0);
    }
    
    @Test
    public void testGetWorkingTimeVacation() {
        ClockTime start = new ClockTime(9, 0);
        ClockTime end = new ClockTime(12, 0);
        TimeSpan pause = new TimeSpan(0, 0);
        Entry entry = new Entry("Test", DATE_NOW, start, end, pause, true);
        
        TimeSpan workingTime = entry.getWorkingTime();
        assertEquals(workingTime.getHour(), 3);
        assertEquals(workingTime.getMinute(), 0);
    }
    
}
