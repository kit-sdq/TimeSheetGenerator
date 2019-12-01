package data;

import static org.junit.Assert.*;

import org.junit.Test;

public class WorkingAreaParseTest {

    @Test
    public void testValidUB_1() {
        ////Test values
        String toParse = "UB";
        
        ////WorkingArea initialization
        WorkingArea fromString = WorkingArea.parse(toParse);
        
        ////Assertions
        assertEquals(WorkingArea.UB, fromString);
    }

    @Test
    public void testValidUB_2() {
        ////Test values
        String toParse = "uB";
        
        ////WorkingArea initialization
        WorkingArea fromString = WorkingArea.parse(toParse);
        
        ////Assertions
        assertEquals(WorkingArea.UB, fromString);
    }
    
    @Test
    public void testValidUB_3() {
        ////Test values
        String toParse = "Ub";
        
        ////WorkingArea initialization
        WorkingArea fromString = WorkingArea.parse(toParse);
        
        ////Assertions
        assertEquals(WorkingArea.UB, fromString);
    }
    
    @Test
    public void testValidUB_4() {
        ////Test values
        String toParse = "ub";
        
        ////WorkingArea initialization
        WorkingArea fromString = WorkingArea.parse(toParse);
        
        ////Assertions
        assertEquals(WorkingArea.UB, fromString);
    }
    
    @Test
    public void testValidGF_1() {
        ////Test values
        String toParse = "GF";
        
        ////WorkingArea initialization
        WorkingArea fromString = WorkingArea.parse(toParse);
        
        ////Assertions
        assertEquals(WorkingArea.GF, fromString);
    }
    
    @Test
    public void testValidGF_2() {
        ////Test values
        String toParse = "gF";
        
        ////WorkingArea initialization
        WorkingArea fromString = WorkingArea.parse(toParse);
        
        ////Assertions
        assertEquals(WorkingArea.GF, fromString);
    }
    
    @Test
    public void testValidGF_3() {
        ////Test values
        String toParse = "Gf";
        
        ////WorkingArea initialization
        WorkingArea fromString = WorkingArea.parse(toParse);
        
        ////Assertions
        assertEquals(WorkingArea.GF, fromString);
    }
    
    @Test
    public void testValidGF_4() {
        ////Test values
        String toParse = "gf";
        
        ////WorkingArea initialization
        WorkingArea fromString = WorkingArea.parse(toParse);
        
        ////Assertions
        assertEquals(WorkingArea.GF, fromString);
    }
    
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSubstring_1() {
        ////Test values
        String toParse = "gfbutNotCorrect";
        
        ////WorkingArea initialization
        WorkingArea fromString = WorkingArea.parse(toParse);
    }
    
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSubstring_2() {
        ////Test values
        String toParse = "gf ";
        
        ////WorkingArea initialization
        WorkingArea fromString = WorkingArea.parse(toParse);
    }
    
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSubstring_3() {
        ////Test values
        String toParse = "gfub";
        
        ////WorkingArea initialization
        WorkingArea fromString = WorkingArea.parse(toParse);
    }
    
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmpty() {
        ////Test values
        String toParse = "";
        
        ////WorkingArea initialization
        WorkingArea fromString = WorkingArea.parse(toParse);
    }
    
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSpace() {
        ////Test values
        String toParse = " ";
        
        ////WorkingArea initialization
        WorkingArea fromString = WorkingArea.parse(toParse);
    }
}
