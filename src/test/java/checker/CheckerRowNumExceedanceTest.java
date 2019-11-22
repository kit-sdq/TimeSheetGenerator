package checker;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.Random;

import org.junit.Test;

import data.Entry;
import data.FullDocumentation;
import data.TimeSpan;

public class CheckerRowNumExceedanceTest {

    //TODO Out source entry generator
    private static final int RANDOM_ENTRY_BOUND = 50;
    private static final int CHECKER_ENTRY_MAX = Checker.getMaxEntries();
    
    @Test
    public void testNoExceedanceLowerBound() {
        ////Checker initialization
        Entry entry = new Entry("Test", Date.valueOf("2019-11-22"),
                new TimeSpan(0, 0), new TimeSpan(0, 0), new TimeSpan(0, 0));
        Entry[] entries = {entry};
        FullDocumentation fullDoc = new FullDocumentation("Max Mustermann", "Fakultät für Informatik", 123456, true, entries);
        Checker checker = new Checker(fullDoc);
        
        ////Assertions
        assertEquals(CheckerReturn.VALID, checker.checkRowNumExceedance());
    }
    
    @Test
    public void testNoExceedanceUpperBound() {
        ////Test values
        int numberOfEntries = CHECKER_ENTRY_MAX;
        
        ////Entry generator
        Entry[] entries = new Entry[numberOfEntries];
        for (int i = 0; i < numberOfEntries; i++) {
            TimeSpan start = new TimeSpan(0, 0);
            TimeSpan end = new TimeSpan(0, 0);
            TimeSpan pause = new TimeSpan(0, 0);
            
            Entry entry = new Entry("Test", Date.valueOf("2019-11-22"), start, end, pause);
            entries[i] = entry;
        }
        
        ////Checker initialization
        FullDocumentation fullDoc = new FullDocumentation("Max Mustermann", "Fakultät für Informatik", 123456, true, entries);
        Checker checker = new Checker(fullDoc);
        
        ////Assertions
        assertTrue(numberOfEntries == fullDoc.getEntries().length);
        assertEquals(CheckerReturn.VALID, checker.checkRowNumExceedance());
    }
    
    @Test
    public void testExceedanceLowerBound() {
        ////Test values
        int numberOfEntries = CHECKER_ENTRY_MAX + 1;
        
        ////Entry generator
        Entry[] entries = new Entry[numberOfEntries];
        for (int i = 0; i < numberOfEntries; i++) {
            TimeSpan start = new TimeSpan(0, 0);
            TimeSpan end = new TimeSpan(0, 0);
            TimeSpan pause = new TimeSpan(0, 0);
            
            Entry entry = new Entry("Test", Date.valueOf("2019-11-22"), start, end, pause);
            entries[i] = entry;
        }
        
        ////Checker initialization
        FullDocumentation fullDoc = new FullDocumentation("Max Mustermann", "Fakultät für Informatik", 123456, true, entries);
        Checker checker = new Checker(fullDoc);
        
        ////Assertions
        assertTrue(numberOfEntries == fullDoc.getEntries().length);
        assertEquals(CheckerReturn.ROWNUM_EXCEEDENCE, checker.checkRowNumExceedance());
    }
    
    @Test
    public void testExceedanceRandom() {
        ////Random
        Random rand = new Random();
        
        ////Test values
        int numberOfEntries = rand.nextInt(RANDOM_ENTRY_BOUND) + 1; //May not be zero!
        
        ////Entry generator
        Entry[] entries = new Entry[numberOfEntries];
        for (int i = 0; i < numberOfEntries; i++) {
            TimeSpan start = new TimeSpan(0, 0);
            TimeSpan end = new TimeSpan(0, 0);
            TimeSpan pause = new TimeSpan(0, 0);
            
            Entry entry = new Entry("Test", Date.valueOf("2019-11-22"), start, end, pause);
            entries[i] = entry;
        }
        
        ////Checker initialization
        FullDocumentation fullDoc = new FullDocumentation("Max Mustermann", "Fakultät für Informatik", 123456, true, entries);
        Checker checker = new Checker(fullDoc);
        
        ////Assertions
        assertTrue(numberOfEntries == fullDoc.getEntries().length);
        if (fullDoc.getEntries().length > Checker.getMaxEntries()) {
            assertEquals(CheckerReturn.ROWNUM_EXCEEDENCE, checker.checkRowNumExceedance());
        } else {
            assertEquals(CheckerReturn.VALID, checker.checkRowNumExceedance());
        }
    }

}
