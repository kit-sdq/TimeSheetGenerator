package checker;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import data.Employee;
import data.Entry;
import data.TimeSheet;
import data.Profession;
import data.TimeSpan;
import data.WorkingArea;
import utils.StreamUtils;
import utils.randomtest.RandomParameterExtension;
import utils.randomtest.RandomParameterExtension.RandomInt;
import utils.randomtest.RandomTestExtension.RandomTest;

@ExtendWith(RandomParameterExtension.class)
public class MiLoGCheckerRowNumExceedanceTest {

    private static final int CHECKER_ENTRY_MAX = MiLoGChecker.getMaxEntries();
    
    ////Placeholder for time sheet construction
    private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
    private static final Profession PROFESSION = new Profession("Fakultät für Informatik", WorkingArea.UB, new TimeSpan(40, 0), 10.31);
    private static final YearMonth YEAR_MONTH = YearMonth.of(2019, Month.NOVEMBER);
    private static final TimeSpan zeroTs = new TimeSpan(0, 0);

    private Stream<Entry> getTestEntryStream() {
        return Stream.generate(() -> new Entry(
            "Test", LocalDate.of(2019, 11, 22),
            new TimeSpan(0, 0), new TimeSpan(0, 0), new TimeSpan(0, 0),
            false
        ));
    }

    private Entry[] getTestEntryArray(int limit) {
        return StreamUtils.toArray(getTestEntryStream().limit(limit), Entry.class);
    }
    
    @Test
    public void testNoExceedanceLowerBound() {
        ////Checker initialization
        Entry[] entries = getTestEntryArray(1);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
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
        Entry[] entries = getTestEntryArray(numberOfEntries);
        
        ////Checker initialization
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Execution
        checker.checkRowNumExceedance();
        
        ////Assertions
        assertTrue(numberOfEntries == timeSheet.getEntries().size());
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }
    
    @Test
    public void testExceedanceLowerBound() {
        ////Test values
        int numberOfEntries = CHECKER_ENTRY_MAX + 1;
        
        ////Entry generator
        Entry[] entries = getTestEntryArray(numberOfEntries);
        
        ////Checker initialization
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Execution
        checker.checkRowNumExceedance();
        
        ////Assertions
        assertTrue(numberOfEntries == timeSheet.getEntries().size());
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals( MiLoGChecker.MiLoGCheckerErrorMessageProvider.ROWNUM_EXCEEDENCE.getErrorMessage())));
    }
    
    @RandomTest
    public void testExceedanceRandom(
        @RandomInt(lowerBound = 1, upperBound = 50) int numberOfEntries
    ) {
        ////Entry generator
        Entry[] entries = getTestEntryArray(numberOfEntries);
        
        ////Checker initialization
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Execution
        checker.checkRowNumExceedance();
        
        ////Assertions
        assertTrue(numberOfEntries == timeSheet.getEntries().size());
        if (timeSheet.getEntries().size() > MiLoGChecker.getMaxEntries()) {
            assertEquals(CheckerReturn.INVALID, checker.getResult());
            assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals( MiLoGChecker.MiLoGCheckerErrorMessageProvider.ROWNUM_EXCEEDENCE.getErrorMessage())));
        } else {
            assertEquals(CheckerReturn.VALID, checker.getResult());
            assertTrue(checker.getErrors().isEmpty());
        }
    }

}
