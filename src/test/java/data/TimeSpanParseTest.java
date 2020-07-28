package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import utils.randomtest.RandomParameterExtension.RandomInt;
import utils.randomtest.RandomTestExtension.RandomTest;
import utils.randomtest.RandomTestExtension.RandomTestClass;

@RandomTestClass(iterations = 1000)
public class TimeSpanParseTest {

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
    
    @Test
    public void testInvalidUpperMinuteBound() {
        ////Test values
        String timeString = "0:60";
        
        ////TimeSpan initialization
        assertThrows(IllegalArgumentException.class, () -> {
            TimeSpan.parse(timeString);
        });
    }
    
    @RandomTest
    public void testInvalidMinutesRandom(
        @RandomInt(lowerBound = 60, upperBound = 120) int minutes
    ) {        
        ////Test values
        String timeString = "0:" + minutes;
        
        ////TimeSpan initialization
        assertThrows(IllegalArgumentException.class, () -> {
            TimeSpan.parse(timeString);
        });
    }
    
    @RandomTest
    public void testMinutesMultipleRandom(
        @RandomInt(upperBound = 120) int minutes
    ) {        
        ////Test values
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
    
    @RandomTest
    public void testValidMultipleRandom(
        @RandomInt(upperBound = 24) int hours,
        @RandomInt(upperBound = 60) int minutes
    ) {
        ////Test values
        String timeString = hours + ":" + minutes;
        
        ////TimeSpan initialization
        TimeSpan timeSpan = TimeSpan.parse(timeString);
        TimeSpan checkTS = new TimeSpan(hours, minutes);
        
        ////Assertions
        assertTrue(timeSpan.compareTo(checkTS) == 0);
        assertEquals(minutes, timeSpan.getMinute());
        assertEquals(hours, timeSpan.getHour());
    }
    
    @RandomTest
    public void testMultipleRandom(
        @RandomInt(upperBound = 48) int hours,
        @RandomInt(upperBound = 120) int minutes
    ) {
        ////Test values
        String timeString = hours + ":" + minutes;
        
        ////TimeSpan initialization
        TimeSpan timeSpan;
        TimeSpan checkTS;
        try {
            timeSpan = TimeSpan.parse(timeString);
            checkTS = new TimeSpan(hours, minutes);
        } catch (IllegalArgumentException e) {
            assertTrue(minutes > 59);
            return;
        }
        
        ////Assertions
        assertTrue(timeSpan.compareTo(checkTS) == 0);
        assertEquals(minutes, timeSpan.getMinute());
        assertEquals(hours, timeSpan.getHour());
    }
    
    @Test
    public void testNegativeHours() {
        ////Test values
        String timeString = "-2:00";
        
        ////TimeSpan initialization
        assertThrows(IllegalArgumentException.class, () -> {
            TimeSpan.parse(timeString);
        });
    }
    
    @Test
    public void testNegativeMinutes() {
        ////Test values
        String timeString = "0:-4";
        
        ////TimeSpan initialization
        assertThrows(IllegalArgumentException.class, () -> {
            TimeSpan.parse(timeString);
        });
    }
    
    @Test
    public void testEmptyString() {
        ////Test values
        String timeString = "";
        
        ////TimeSpan initialization
        assertThrows(IllegalArgumentException.class, () -> {
            TimeSpan.parse(timeString);
        });
    }
    
    @Test
    public void testInvalidStringColon() {
        ////Test values
        String timeString = ":";
        
        ////TimeSpan initialization
        assertThrows(IllegalArgumentException.class, () -> {
            TimeSpan.parse(timeString);
        });
    }
    
    @Test
    public void testInvalidStringNumber() {
        ////Test values
        String timeString = "7";
        
        ////TimeSpan initialization
        assertThrows(IllegalArgumentException.class, () -> {
            TimeSpan.parse(timeString);
        });
    }
    
    @Test
    public void testInvalidStringNaN() {
        ////Test values
        String timeString = "TestString";
        
        ////TimeSpan initialization
        assertThrows(IllegalArgumentException.class, () -> {
            TimeSpan.parse(timeString);
        });
    }
}
