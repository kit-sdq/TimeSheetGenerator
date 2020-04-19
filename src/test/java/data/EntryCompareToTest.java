package data;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class EntryCompareToTest {

    @Test
    public void testCompareToSmallerDate() {
        String action0 = "Test";
        TimeSpan start0 = new TimeSpan(14, 0);
        TimeSpan end0 = new TimeSpan(18, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test";
        TimeSpan start1 = new TimeSpan(14, 0);
        TimeSpan end1 = new TimeSpan(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 30);
        LocalDate date1 = LocalDate.of(2019, 11, 17);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) < 0);
    }
    
    @Test
    public void testCompareToGreaterDate() {
        String action0 = "Test";
        TimeSpan start0 = new TimeSpan(14, 0);
        TimeSpan end0 = new TimeSpan(18, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test";
        TimeSpan start1 = new TimeSpan(14, 0);
        TimeSpan end1 = new TimeSpan(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 30);
        LocalDate date1 = LocalDate.of(2019, 11, 15);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) > 0);
    }
    
    @Test
    public void testCompareToSmallerHour() {
        String action0 = "Test";
        TimeSpan start0 = new TimeSpan(14, 0);
        TimeSpan end0 = new TimeSpan(18, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test";
        TimeSpan start1 = new TimeSpan(15, 0);
        TimeSpan end1 = new TimeSpan(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 30);
        LocalDate date1 = LocalDate.of(2019, 11, 16);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) < 0);
    }
    
    @Test
    public void testCompareToGreaterHour() {
        String action0 = "Test";
        TimeSpan start0 = new TimeSpan(14, 0);
        TimeSpan end0 = new TimeSpan(18, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test";
        TimeSpan start1 = new TimeSpan(13, 0);
        TimeSpan end1 = new TimeSpan(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 30);
        LocalDate date1 = LocalDate.of(2019, 11, 16);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) > 0);
    }
    
    @Test
    public void testCompareToSmallerMinute() {
        String action0 = "Test";
        TimeSpan start0 = new TimeSpan(14, 0);
        TimeSpan end0 = new TimeSpan(18, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test";
        TimeSpan start1 = new TimeSpan(14, 10);
        TimeSpan end1 = new TimeSpan(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 30);
        LocalDate date1 = LocalDate.of(2019, 11, 16);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) < 0);
    }
    
    @Test
    public void testCompareToGreaterMinute() {
        String action0 = "Test";
        TimeSpan start0 = new TimeSpan(14, 10);
        TimeSpan end0 = new TimeSpan(18, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test";
        TimeSpan start1 = new TimeSpan(14, 0);
        TimeSpan end1 = new TimeSpan(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 30);
        LocalDate date1 = LocalDate.of(2019, 11, 16);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) > 0);
    }
    
    @Test
    public void testCompareToEqual() {
        String action0 = "Test";
        TimeSpan start0 = new TimeSpan(14, 0);
        TimeSpan end0 = new TimeSpan(18, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test";
        TimeSpan start1 = new TimeSpan(14, 0);
        TimeSpan end1 = new TimeSpan(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 30);
        LocalDate date1 = LocalDate.of(2019, 11, 16);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) == 0);
    }
    
    @Test
    public void testCompareToEqualDateHourMinute() {
        String action0 = "Test 1";
        TimeSpan start0 = new TimeSpan(14, 0);
        TimeSpan end0 = new TimeSpan(19, 0);
        TimeSpan pause0 = new TimeSpan(0, 30);
        LocalDate date0 = LocalDate.of(2019, 11, 16);
        
        Entry entry0 = new Entry(action0, date0, start0, end0, pause0, false);
        
        String action1 = "Test 2";
        TimeSpan start1 = new TimeSpan(14, 0);
        TimeSpan end1 = new TimeSpan(18, 0);
        TimeSpan pause1 = new TimeSpan(0, 15);
        LocalDate date1 = LocalDate.of(2019, 11, 16);
        
        Entry entry1 = new Entry(action1, date1, start1, end1, pause1, false);
        
        assertTrue(entry0.compareTo(entry1) == 0);
    }
    
}
