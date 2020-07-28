package data;

import static org.junit.jupiter.api.Assertions.*;
import static utils.IteratorUtils.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Iterator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import utils.randomtest.RandomParameterExtension;
import utils.randomtest.RandomParameterExtension.RandomBoolean;
import utils.randomtest.RandomParameterExtension.RandomInt;
import utils.randomtest.RandomParameterExtension.RandomTimeSpan;
import utils.randomtest.RandomTestExtension.RandomTest;

@ExtendWith(RandomParameterExtension.class)
public class TimeSheetArithmeticTest {

    private static final Employee EMPLOYEE = new Employee("Moritz Gstür", 1234567);
    private static final Profession PROFESSION = new Profession("Fakultät für Informatik", WorkingArea.UB, new TimeSpan(40, 0), 10.31);
    private static final YearMonth YEAR_MONTH = YearMonth.of(2019, Month.NOVEMBER);
    private static final TimeSpan ZERO_TS = new TimeSpan(0, 0);

    private Entry getRandomEntry(Iterator<TimeSpan> timeSpans, Iterator<Boolean> booleans) {
        boolean isVacation = booleans.next();

        Tuple<TimeSpan, TimeSpan> zipStartEnd = nextWhere(zip(timeSpans, timeSpans), (start, end) -> start.compareTo(end) <= 0);

        TimeSpan start = zipStartEnd.getFirst();
        TimeSpan end = zipStartEnd.getSecond();
        TimeSpan pause = isVacation ? ZERO_TS : nextWhere(timeSpans, timeSpan -> timeSpan.compareTo(end.subtract(start)) <= 0);

        return new Entry("Test", LocalDate.now(), start, end, pause, isVacation);
    }

    @Test
    public void testGetTotalWorkTime() {
        Entry[] entries = new Entry[] {
            new Entry("Test1", LocalDate.now(), new TimeSpan(10, 0), new TimeSpan(18, 30), new TimeSpan(3, 15), false),
            new Entry("Test2", LocalDate.now(), new TimeSpan(12, 0), new TimeSpan(16, 30), new TimeSpan(0, 0), false),
            new Entry("Test3", LocalDate.now(), new TimeSpan(10, 0), new TimeSpan(12, 0), new TimeSpan(0, 15), false),
            new Entry("Test4", LocalDate.now(), new TimeSpan(15, 0), new TimeSpan(16, 30), new TimeSpan(0, 0), false),
            new Entry("Test5", LocalDate.now(), new TimeSpan(20, 0), new TimeSpan(20, 30), new TimeSpan(0, 0), false),
            new Entry("Test6", LocalDate.now(), new TimeSpan(9, 30), new TimeSpan(18, 30), new TimeSpan(1, 0), false),
            new Entry("Test7", LocalDate.now(), new TimeSpan(9, 0), new TimeSpan(12, 0), new TimeSpan(0, 0), true)
        };
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, ZERO_TS, ZERO_TS);
        
        assertEquals(timeSheet.getTotalWorkTime(), new TimeSpan(21, 30));
    }
    
    @Test
    public void testGetTotalVacationTime() {
        Entry[] entries = new Entry[] {
            new Entry("Test1", LocalDate.now(), new TimeSpan(10, 0), new TimeSpan(18, 30), new TimeSpan(3, 15), false),
            new Entry("Test2", LocalDate.now(), new TimeSpan(12, 0), new TimeSpan(16, 30), new TimeSpan(0, 0), true),
            new Entry("Test3", LocalDate.now(), new TimeSpan(10, 0), new TimeSpan(12, 0), new TimeSpan(0, 0), true),
            new Entry("Test4", LocalDate.now(), new TimeSpan(15, 0), new TimeSpan(16, 30), new TimeSpan(0, 0), true)
        };
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, ZERO_TS, ZERO_TS);
        
        assertEquals(timeSheet.getTotalVacationTime(), new TimeSpan(8, 0));
    }

    @RandomTest
    public void testGetTotalWorkTimeRandom(
        @RandomInt(upperBound = 20) int entryCount,
        @RandomTimeSpan Iterator<TimeSpan> timeSpans,
        @RandomBoolean Iterator<Boolean> booleans
    ) {
        // prepare
        Entry[] entries = new Entry[entryCount];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = getRandomEntry(timeSpans, booleans);
        }
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, new TimeSpan(10000, 0), ZERO_TS);
            // huge succ transfer, so that working + vacation time are not greater than max working time + succ transfer

        TimeSpan expectedTotalWorkTime = ZERO_TS;
        for (int i = 0; i < entries.length; i++) {
            if (!entries[i].isVacation()) {
                expectedTotalWorkTime = expectedTotalWorkTime.add(entries[i].getWorkingTime());
            }
        }

        // execute
        TimeSpan actualTotalWorkTime = timeSheet.getTotalWorkTime();

        // assert
        assertEquals(expectedTotalWorkTime, actualTotalWorkTime);
    }

    @RandomTest
    public void testGetTotalVacationTimeRandom(
        @RandomInt(upperBound = 20) int entryCount,
        @RandomTimeSpan Iterator<TimeSpan> timeSpans,
        @RandomBoolean Iterator<Boolean> booleans
    ) {
        // prepare
        Entry[] entries = new Entry[entryCount];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = getRandomEntry(timeSpans, booleans);
        }
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, new TimeSpan(10000, 0), ZERO_TS);
            // huge succ transfer, so that working + vacation time are not greater than max working time + succ transfer

        TimeSpan expectedTotalVacationTime = ZERO_TS;
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].isVacation()) {
                expectedTotalVacationTime = expectedTotalVacationTime.add(entries[i].getWorkingTime());
            }
        }

        // execute
        TimeSpan actualTotalVacationTime = timeSheet.getTotalVacationTime();

        // assert
        assertEquals(expectedTotalVacationTime, actualTotalVacationTime);
    }

}
