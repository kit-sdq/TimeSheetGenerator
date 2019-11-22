package data;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class FullDocumentationArithmeticTest {

    @Test
    public void testGetTotalWorkTime() {
        String employeeName = "Moritz Gstür";
        String department = "Fakultät für Informatik";
        int id = 4613854;
        boolean gfub = true;
        Entry[] entries = new Entry[6];
        entries[0] = new Entry("Test1", new Date(), new TimeSpan(10, 0), new TimeSpan(18, 30), new TimeSpan(3, 15));
        entries[1] = new Entry("Test2", new Date(), new TimeSpan(12, 0), new TimeSpan(16, 30), new TimeSpan(0, 0));
        entries[2] = new Entry("Test3", new Date(), new TimeSpan(10, 0), new TimeSpan(12, 0), new TimeSpan(0, 15));
        entries[3] = new Entry("Test4", new Date(), new TimeSpan(15, 0), new TimeSpan(16, 30), new TimeSpan(0, 0));
        entries[4] = new Entry("Test5", new Date(), new TimeSpan(20, 0), new TimeSpan(20, 30), new TimeSpan(0, 0));
        entries[5] = new Entry("Test6", new Date(), new TimeSpan(9, 30), new TimeSpan(18, 30), new TimeSpan(1, 0));
        FullDocumentation fullDoc = new FullDocumentation(employeeName, department, id, gfub, entries);
        
        assertEquals(fullDoc.getTotalWorkTime().toString(), "21:30");
    }

    //TODO Create random tests
}
