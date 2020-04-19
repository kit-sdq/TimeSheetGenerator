package data;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class EntryCompareToTest {

    @Test
    public void testCompareToSmallerDate() {
        String action0 = "Test";
        ClockTime start0 = new ClockTime(14, 0);
        ClockTime end0 = new ClockTime(18, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test";
        ClockTime start1 = new ClockTime(14, 0);
        ClockTime end1 = new ClockTime(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 30);
        LocalDate date1 = LocalDate.of(2019, 11, 17);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) < 0);
    }
    
    @Test
    public void testCompareToGreaterDate() {
        String action0 = "Test";
        ClockTime start0 = new ClockTime(14, 0);
        ClockTime end0 = new ClockTime(18, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test";
        ClockTime start1 = new ClockTime(14, 0);
        ClockTime end1 = new ClockTime(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 30);
        LocalDate date1 = LocalDate.of(2019, 11, 15);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) > 0);
    }
    
    @Test
    public void testCompareToSmallerHour() {
        String action0 = "Test";
        ClockTime start0 = new ClockTime(14, 0);
        ClockTime end0 = new ClockTime(18, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test";
        ClockTime start1 = new ClockTime(15, 0);
        ClockTime end1 = new ClockTime(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 30);
        LocalDate date1 = LocalDate.of(2019, 11, 16);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) < 0);
    }
    
    @Test
    public void testCompareToGreaterHour() {
        String action0 = "Test";
        ClockTime start0 = new ClockTime(14, 0);
        ClockTime end0 = new ClockTime(18, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test";
        ClockTime start1 = new ClockTime(13, 0);
        ClockTime end1 = new ClockTime(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 30);
        LocalDate date1 = LocalDate.of(2019, 11, 16);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) > 0);
    }
    
    @Test
    public void testCompareToSmallerMinute() {
        String action0 = "Test";
        ClockTime start0 = new ClockTime(14, 0);
        ClockTime end0 = new ClockTime(18, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test";
        ClockTime start1 = new ClockTime(14, 10);
        ClockTime end1 = new ClockTime(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 30);
        LocalDate date1 = LocalDate.of(2019, 11, 16);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) < 0);
    }
    
    @Test
    public void testCompareToGreaterMinute() {
        String action0 = "Test";
        ClockTime start0 = new ClockTime(14, 10);
        ClockTime end0 = new ClockTime(18, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test";
        ClockTime start1 = new ClockTime(14, 0);
        ClockTime end1 = new ClockTime(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 30);
        LocalDate date1 = LocalDate.of(2019, 11, 16);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) > 0);
    }
    
    @Test
    public void testCompareToEqual() {
        String action0 = "Test";
        ClockTime start0 = new ClockTime(14, 0);
        ClockTime end0 = new ClockTime(18, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test";
        ClockTime start1 = new ClockTime(14, 0);
        ClockTime end1 = new ClockTime(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 30);
        LocalDate date1 = LocalDate.of(2019, 11, 16);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) == 0);
    }
    
    @Test
    public void testCompareToEqualDateHourMinute() {
        String action0 = "Test 1";
        ClockTime start0 = new ClockTime(14, 0);
        ClockTime end0 = new ClockTime(19, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test 2";
        ClockTime start1 = new ClockTime(14, 0);
        ClockTime end1 = new ClockTime(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 15);
        LocalDate date1 = LocalDate.of(2019, 11, 16);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) == 0);
    }
    
}
