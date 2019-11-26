package data;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class TimeSpanParseTest {

    //Exclusively. Refer to https://docs.oracle.com/javase/8/docs/api/java/util/Random.html
    private static final int RANDOM_HOUR_BOUND = 24;
    private static final int RANDOM_MINUTES_BOUND = 60;
    private static final int RANDOM_UPPER_BOUND = 120;
    
    private static final int MULTIPLE_TEST_ITERATIONS = 10000;
    
    @Test
    public void testValidLowerBound() {
        ////Test values
        String timeStringSingle = "0:0";
        String timeStringHalf = "0:00";
        String timeStringOtherHalf = "00:0";
        String timeStringDouble = "00:00";
        TimeSpan zeroTS = new TimeSpan(0, 0);
        
        ////TimeSpan initialization
        TimeSpan tsSingle = TimeSpan.parse(timeStringSingle);
        TimeSpan tsHalf = TimeSpan.parse(timeStringHalf);
        TimeSpan tsOtherHalf = TimeSpan.parse(timeStringOtherHalf);
        TimeSpan tsDouble = TimeSpan.parse(timeStringDouble);
        
        ////Assertions
        assertEquals(0, tsSingle.getHour());
        assertEquals(0, tsSingle.getMinute());
        assertTrue(tsSingle.compareTo(zeroTS) == 0);
        
        assertEquals(0, tsHalf.getHour());
        assertEquals(0, tsHalf.getMinute());
        assertTrue(tsHalf.compareTo(zeroTS) == 0);
        
        assertEquals(0, tsOtherHalf.getHour());
        assertEquals(0, tsOtherHalf.getMinute());
        assertTrue(tsOtherHalf.compareTo(zeroTS) == 0);
        
        assertEquals(0, tsDouble.getHour());
        assertEquals(0, tsDouble.getMinute());
        assertTrue(tsDouble.compareTo(zeroTS) == 0);
    }

    @Test
    public void testValidUpperMinuteBound() {
        ////Test values
        String timeString = "0:59";
        TimeSpan checkTS = new TimeSpan(0, 59);
        
        ////TimeSpan initialization
        TimeSpan timeSpan = TimeSpan.parse(timeString);

        ////Assertions
        assertEquals(0, timeSpan.getHour());
        assertEquals(59, timeSpan.getMinute());
        assertTrue(timeSpan.compareTo(checkTS) == 0);
    }
    
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class) 
    public void testInvalidUpperMinuteBound() {
        ////Test values
        String timeString = "0:60";
        
        ////TimeSpan initialization
        TimeSpan timeSpan = TimeSpan.parse(timeString);
    }
    
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class) 
    public void testInvalidMinutesRandom() {
        ////Random
        Random rand = new Random();
        
        ////Test values
        int minutes = rand.nextInt(RANDOM_UPPER_BOUND - 60) + 60;
        String timeString = "0:" + minutes;
        
        ////TimeSpan initialization
        TimeSpan timeSpan = TimeSpan.parse(timeString);
    }
    
    @Test
    public void testMinutesRandom() {
        ////Random
        Random rand = new Random();
        
        ////Test values
        int minutes = rand.nextInt(RANDOM_UPPER_BOUND);
        String timeString = "0:" + minutes;
        
        ////TimeSpan initialization
        TimeSpan timeSpan;
        TimeSpan checkTS;
        try {
            timeSpan = TimeSpan.parse(timeString);
            checkTS = new TimeSpan(0, minutes);
        } catch (IllegalArgumentException e) {
            assertTrue(minutes > 59);
            return;
        }
        
        ////Assertions
        assertTrue(timeSpan.compareTo(checkTS) == 0);
        assertEquals(minutes, timeSpan.getMinute());
        assertEquals(0, timeSpan.getHour());
    }
    
    @Test
    public void testMinutesMultiple() {
        ////Random
        Random rand = new Random();
        
        ////TimeSpan initialization
        for (int i = 0; i < MULTIPLE_TEST_ITERATIONS; i++) {
            ////Test values
            int minutes = rand.nextInt(RANDOM_UPPER_BOUND);
            String timeString = "0:" + minutes;
            
            ////TimeSpan initialization
            TimeSpan timeSpan;
            TimeSpan checkTS;
            try {
                timeSpan = TimeSpan.parse(timeString);
                checkTS = new TimeSpan(0, minutes);
            } catch (IllegalArgumentException e) {
                assertTrue(minutes > 59);
                continue;
            }
            
            ////Assertions
            assertTrue(timeSpan.compareTo(checkTS) == 0);
            assertEquals(minutes, timeSpan.getMinute());
            assertEquals(0, timeSpan.getHour());
        }
    }
    
    @Test
    public void testValidMultipleRandom() {
        ////Random
        Random rand = new Random();
        
        for (int i = 0; i < MULTIPLE_TEST_ITERATIONS; i++) {
            ////Test values
            int hours = rand.nextInt(RANDOM_HOUR_BOUND);
            int minutes = rand.nextInt(RANDOM_MINUTES_BOUND);
            String timeString = hours + ":" + minutes;
            
            ////TimeSpan initialization
            TimeSpan timeSpan = TimeSpan.parse(timeString);
            TimeSpan checkTS = new TimeSpan(hours, minutes);
            
            ////Assertions
            assertTrue(timeSpan.compareTo(checkTS) == 0);
            assertEquals(minutes, timeSpan.getMinute());
            assertEquals(hours, timeSpan.getHour());
        }
    }
    
    @Test
    public void testMultipleRandom() {
        ////Random
        Random rand = new Random();
        
        for (int i = 0; i < MULTIPLE_TEST_ITERATIONS; i++) {
            ////Test values
            int hours = rand.nextInt(RANDOM_UPPER_BOUND);
            int minutes = rand.nextInt(RANDOM_UPPER_BOUND);
            String timeString = hours + ":" + minutes;
            
            ////TimeSpan initialization
            TimeSpan timeSpan;
            TimeSpan checkTS;
            try {
                timeSpan = TimeSpan.parse(timeString);
                checkTS = new TimeSpan(hours, minutes);
            } catch (IllegalArgumentException e) {
                assertTrue(minutes > 59);
                continue;
            }
            
            ////Assertions
            assertTrue(timeSpan.compareTo(checkTS) == 0);
            assertEquals(minutes, timeSpan.getMinute());
            assertEquals(hours, timeSpan.getHour());
        }
    }
    
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class) 
    public void testNegativeHours() {
        ////Test values
        String timeString = "-2:00";
        
        ////TimeSpan initialization
        TimeSpan timeSpan = TimeSpan.parse(timeString);
    }
    
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class) 
    public void testNegativeMinutes() {
        ////Test values
        String timeString = "0:-4";
        
        ////TimeSpan initialization
        TimeSpan timeSpan = TimeSpan.parse(timeString);
    }
    
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class) 
    public void testEmptyString() {
        ////Test values
        String timeString = "";
        
        ////TimeSpan initialization
        TimeSpan timeSpan = TimeSpan.parse(timeString);
    }
    
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class) 
    public void testInvalidStringColon() {
        ////Test values
        String timeString = ":";
        
        ////TimeSpan initialization
        TimeSpan timeSpan = TimeSpan.parse(timeString);
    }
    
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class) 
    public void testInvalidStringNumber() {
        ////Test values
        String timeString = "7";
        
        ////TimeSpan initialization
        TimeSpan timeSpan = TimeSpan.parse(timeString);
    }
    
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class) 
    public void testInvalidStringNaN() {
        ////Test values
        String timeString = "TestString";
        
        ////TimeSpan initialization
        TimeSpan timeSpan = TimeSpan.parse(timeString);
    }
}
