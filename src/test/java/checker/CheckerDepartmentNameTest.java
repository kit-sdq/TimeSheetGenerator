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

public class CheckerDepartmentNameTest {

    private static final Entry[] ENTRIES = {new Entry("Test", LocalDate.of(2019, 11, 22),
            new TimeSpan(0, 0), new TimeSpan(0, 0), new TimeSpan(0, 0))};
    
    ////Placeholder for documentation construction
    private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
    private static final YearMonth YEAR_MONTH = YearMonth.of(2019, Month.NOVEMBER);
    private static final TimeSpan zeroTs = new TimeSpan(0, 0);
    
    @Test
    public void testEmptyName() {
        ////Test values
        String departmentName = "";
        
        ////Checker initialization
        Profession profession = new Profession(departmentName, WorkingArea.UB, 40, 10.31);
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, ENTRIES, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        assertEquals(CheckerReturn.NAME_MISSING, checker.checkDepartmentName());
    }

    @Test
    public void testValidName() {
        ////Test values
        String departmentName = "validName Test Word";

        ////Checker initialization
        Profession profession = new Profession(departmentName, WorkingArea.UB, 40, 10.31);
        TimeSheet fullDoc = new TimeSheet(EMPLOYEE, profession, YEAR_MONTH, ENTRIES, zeroTs, zeroTs, zeroTs);
        Checker checker = new Checker(fullDoc);
        
        assertEquals(CheckerReturn.VALID, checker.checkDepartmentName());
    }
    
}
