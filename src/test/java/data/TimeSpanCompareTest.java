package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import utils.randomtest.RandomParameterExtension;
import utils.randomtest.RandomParameterExtension.RandomTimeSpan;
import utils.randomtest.RandomTestExtension.RandomTest;

@ExtendWith(RandomParameterExtension.class)
public class TimeSpanCompareTest {

    @Test
    public void testIdentical1() {
        
        TimeSpan ts1 = new TimeSpan(0, 0);
        TimeSpan ts2 = new TimeSpan(0, 0);
        
        assertEquals(ts1.compareTo(ts2), 0);
    }

    @Test
    public void testIdentical2() {
        
        TimeSpan ts1 = new TimeSpan(26, 33);
        TimeSpan ts2 = new TimeSpan(26, 33);
        
        assertEquals(ts1.compareTo(ts2), 0);
    }
    
    @RandomTest
    public void testIdenticalRandom(
        @RandomTimeSpan(upperBoundHour = 999) TimeSpan ts1
    ) {

        TimeSpan ts2 = new TimeSpan(ts1.getHour(), ts1.getMinute());
        
        assertEquals(ts1.compareTo(ts2), 0);
    }
    
    @RandomTest
    public void testRandom(
        @RandomTimeSpan(upperBoundHour = 999) TimeSpan ts1,
        @RandomTimeSpan(upperBoundHour = 999) TimeSpan ts2
    ) {

        if (Integer.compare(ts1.getHour(), ts2.getHour()) > 0) {
            assertEquals(ts1.compareTo(ts2), 1);
        } else if (Integer.compare(ts1.getHour(), ts2.getHour()) == 0) {
            assertEquals(ts1.compareTo(ts2), Integer.compare(ts1.getMinute(), ts2.getMinute()));
        } else {
            assertEquals(ts1.compareTo(ts2), -1);
        }
    }
}
