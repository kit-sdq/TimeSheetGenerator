package checker;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Random;

import org.junit.Test;

import data.Employee;
import data.Entry;
import data.TimeSheet;
import data.Profession;
import data.TimeSpan;
import data.WorkingArea;

public class CheckerRowNumExceedanceTest {

    //TODO Out source entry generator
    private static final int RANDOM_ENTRY_BOUND = 50;
    private static final int CHECKER_ENTRY_MAX = MiLoGChecker.getMaxEntries();
    
    ////Placeholder for documentation construction
    private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
    private static final Profession PROFESSION = new Profession("Fakultät für Informatik", WorkingArea.UB, new TimeSpan(40, 0), 10.31);
    private static final YearMonth YEAR_MONTH = YearMonth.of(2019, Month.NOVEMBER);
    private static final TimeSpan zeroTs = new TimeSpan(0, 0);
    
    @Test
    public void testNoExceedanceLowerBound() {
        ////Checker initialization
        Entry entry = new Entry("Test", LocalDate.of(2019, 11, 22),
                new TimeSpan(0, 0), new TimeSpan(0, 0), new TimeSpan(0, 0));
        Entry[] entries = {entry};
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(fullDoc);
        
        ////Execution
        checker.checkRowNumExceedance();
        
        ////Assertions
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
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
            
            Entry entry = new Entry("Test", LocalDate.of(2019, 11, 22), start, end, pause);
            entries[i] = entry;
        }
        
        ////Checker initialization
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(fullDoc);
        
        ////Execution
        checker.checkRowNumExceedance();
        
        ////Assertions
        assertTrue(numberOfEntries == fullDoc.getEntries().length);
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
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
            
            Entry entry = new Entry("Test", LocalDate.of(2019, 11, 22), start, end, pause);
            entries[i] = entry;
        }
        
        ////Checker initialization
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(fullDoc);
        
        ////Execution
        checker.checkRowNumExceedance();
        
        ////Assertions
        assertTrue(numberOfEntries == fullDoc.getEntries().length);
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(MiLoGChecker.CheckerErrorMessage.ROWNUM_EXCEEDENCE.getErrorMessage())));
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
            
            Entry entry = new Entry("Test", LocalDate.of(2019, 11, 22), start, end, pause);
            entries[i] = entry;
        }
        
        ////Checker initialization
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(fullDoc);
        
        ////Execution
        checker.checkRowNumExceedance();
        
        ////Assertions
        assertTrue(numberOfEntries == fullDoc.getEntries().length);
        if (fullDoc.getEntries().length > MiLoGChecker.getMaxEntries()) {
            assertEquals(CheckerReturn.INVALID, checker.getResult());
            assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals(MiLoGChecker.CheckerErrorMessage.ROWNUM_EXCEEDENCE.getErrorMessage())));
        } else {
            assertEquals(CheckerReturn.VALID, checker.getResult());
            assertTrue(checker.getErrors().isEmpty());
        }
    }

}
