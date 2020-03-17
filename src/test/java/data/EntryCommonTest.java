package data;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

public class EntryCommonTest {

    @Test
    public void testConstructor() {
        String action = "Test";
        TimeSpan start = new TimeSpan(14, 0);
        TimeSpan end = new TimeSpan(18, 0);
        TimeSpan pause = new TimeSpan(0, 30);

        LocalDate date = LocalDate.of(2019, 11, 16);
        
        Entry entry = new Entry(action, date, start, end, pause, false);
        
        TimeSpan workingTime = entry.getWorkingTime();
        assertEquals(workingTime.getHour(), 3);
        assertEquals(workingTime.getMinute(), 30);
        
        assertEquals(entry.getAction(), action);
        assertTrue(entry.getDate().equals(date));
        assertEquals(entry.getStart().compareTo(start), 0);
        assertEquals(entry.getEnd().compareTo(end), 0);
        assertEquals(entry.getPause().compareTo(pause), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgument1() {
        String action = "Test";
        TimeSpan start = new TimeSpan(14, 0);
        TimeSpan end = new TimeSpan(42, 0);
        TimeSpan pause = new TimeSpan(0, 30);
        
        LocalDate date = LocalDate.of(2019, 11, 16);
        
        new Entry(action, date, start, end, pause, false);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgument2() {
        String action = "Test";
        TimeSpan start = new TimeSpan(23, 59);
        TimeSpan end = new TimeSpan(24, 0);
        TimeSpan pause = new TimeSpan(0, 30);
        
        LocalDate date = LocalDate.of(2019, 11, 16);
        
        new Entry(action, date, start, end, pause, false);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgument3() {
        String action = "Test";
        TimeSpan start = new TimeSpan(23, 00);
        TimeSpan end = new TimeSpan(22, 0);
        TimeSpan pause = new TimeSpan(0, 30);
        
        LocalDate date = LocalDate.of(2019, 11, 16);
        
        new Entry(action, date, start, end, pause, false);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgument4() {
        String action = "Test";
        TimeSpan start = new TimeSpan(25, 00);
        TimeSpan end = new TimeSpan(26, 0);
        TimeSpan pause = new TimeSpan(0, 30);
        
        LocalDate date = LocalDate.of(2019, 11, 16);
        
        new Entry(action, date, start, end, pause, false);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIllegalArgumentPauseAndVacation() {
        String action = "Test";
        TimeSpan start = new TimeSpan(14, 0);
        TimeSpan end = new TimeSpan(18, 0);
        TimeSpan pause = new TimeSpan(0, 30);

        LocalDate date = LocalDate.of(2019, 11, 16);
        
        new Entry(action, date, start, end, pause, true);
    }
    
}
