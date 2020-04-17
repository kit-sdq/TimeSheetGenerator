package checker;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;

import org.junit.Test;

import data.Employee;
import data.Entry;
import data.TimeSheet;
import data.Profession;
import data.TimeSpan;
import data.WorkingArea;

public class MiLoGCheckerDepartmentNameTest {

    private static final Entry[] ENTRIES = {new Entry("Test", LocalDate.of(2019, 11, 22),
            new TimeSpan(0, 0), new TimeSpan(0, 0), new TimeSpan(0, 0), false)};
    
    ////Placeholder for time sheet construction
    private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
    private static final YearMonth YEAR_MONTH = YearMonth.of(2019, Month.NOVEMBER);
    private static final TimeSpan zeroTs = new TimeSpan(0, 0);
    
    @Test
    public void testEmptyName() {
        ////Test values
        String departmentName = "";
        
        ////Checker initialization
        Profession profession = new Profession(departmentName, WorkingArea.UB, new TimeSpan(40, 0), 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, ENTRIES, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Execution
        checker.checkDepartmentName();
        
        ////Assertions
        assertEquals(CheckerReturn.INVALID, checker.getResult());
        assertTrue(checker.getErrors().stream().anyMatch(item -> item.getErrorMessage().equals( MiLoGChecker.MiLoGCheckerErrorMessageProvider.NAME_MISSING.getErrorMessage())));
    }

    @Test
    public void testValidName() {
        ////Test values
        String departmentName = "validName Test Word";

        ////Checker initialization
        Profession profession = new Profession(departmentName, WorkingArea.UB, new TimeSpan(40, 0), 10.31);
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, ENTRIES, zeroTs, zeroTs);
        MiLoGChecker checker = new MiLoGChecker(timeSheet);
        
        ////Execution
        checker.checkDepartmentName();
        
        ////Assertions
        assertEquals(CheckerReturn.VALID, checker.getResult());
        assertTrue(checker.getErrors().isEmpty());
    }
    
}
