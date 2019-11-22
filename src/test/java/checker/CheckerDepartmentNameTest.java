package checker;

import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Test;

import data.Entry;
import data.FullDocumentation;
import data.TimeSpan;

public class CheckerDepartmentNameTest {

    private static final Entry[] ENTRIES = {new Entry("Test", Date.valueOf("2019-11-22"),
            new TimeSpan(0, 0), new TimeSpan(0, 0), new TimeSpan(0, 0))};
    
    @Test
    public void testEmptyName() {
        ////Test values
        String departmentName = "";
        
        ////Checker initialization
        FullDocumentation fullDoc = new FullDocumentation("Max Mustermann", departmentName, 123456, true, ENTRIES);
        Checker checker = new Checker(fullDoc);
        
        assertEquals(CheckerReturn.NAME_MISSING, checker.checkDepartmentName());
    }

    @Test
    public void testValidName() {
        ////Test values
        String departmentName = "validName Test Word";
        
        ////Checker initialization
        FullDocumentation fullDoc = new FullDocumentation("Max Mustermann", departmentName, 123456, true, ENTRIES);
        Checker checker = new Checker(fullDoc);
        
        assertEquals(CheckerReturn.VALID, checker.checkDepartmentName());
    }
    
}
