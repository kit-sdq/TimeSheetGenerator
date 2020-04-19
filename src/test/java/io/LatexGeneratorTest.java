package io;

import static org.junit.Assert.*;

import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.junit.Test;

import data.Employee;
import data.Entry;
import data.Profession;
import data.TimeSheet;
import data.TimeSpan;
import data.WorkingArea;

public class LatexGeneratorTest {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy");

    private static final Employee EMPLOYEE = new Employee("Max Mustermann", 1234567);
    private static final Profession PROFESSION = new Profession("Fakultät für Informatik", WorkingArea.UB, new TimeSpan(40, 0), 10.31);
    private static final YearMonth YEAR_MONTH = YearMonth.of(2019, Month.NOVEMBER);
    private static final TimeSpan zeroTs = new TimeSpan(0, 0);

    @Test
    public void testCreate() {
        // data
        Entry entry = new Entry("Test Action", YEAR_MONTH.atDay(12), new TimeSpan(10, 0), new TimeSpan(14, 0), new TimeSpan(0, 30), false);
        Entry[] entries = new Entry[] {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        String template = "something something";
        // execute
        LatexGenerator generator = new LatexGenerator(timeSheet, template);
        // assert
        assertNotNull(generator);
    }

    @Test
    public void testGetFileNameExtensionFilter() {
        // data
        Entry entry = new Entry("Test Action", YEAR_MONTH.atDay(12), new TimeSpan(10, 0), new TimeSpan(14, 0), new TimeSpan(0, 30), false);
        Entry[] entries = new Entry[] {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        String template = "something something";
        LatexGenerator generator = new LatexGenerator(timeSheet, template);
        // execute
        FileNameExtensionFilter filter = generator.getFileNameExtensionFilter();
        // assert
        assertTrue(Arrays.asList(filter.getExtensions()).contains("tex"));
    }

    @Test
    public void testGenerateEmptyTemplate() {
        // data
        Entry entry = new Entry("Test Action", YEAR_MONTH.atDay(12), new TimeSpan(10, 0), new TimeSpan(14, 0), new TimeSpan(0, 30), false);
        Entry[] entries = new Entry[] {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        String template = "";
        LatexGenerator generator = new LatexGenerator(timeSheet, template);
        // execute
        String latex = generator.generate();
        // assert
        assertEquals("", latex);
    }

    @Test
    public void testGenerateWithoutPlaceholders() {
        // data
        Entry entry = new Entry("Test Action", YEAR_MONTH.atDay(12), new TimeSpan(10, 0), new TimeSpan(14, 0), new TimeSpan(0, 30), false);
        Entry[] entries = new Entry[] {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        String template = "something else";
        LatexGenerator generator = new LatexGenerator(timeSheet, template);
        // execute
        String latex = generator.generate();
        // assert
        assertEquals("something else", latex);
    }

    @Test
    public void testGenerateWithInvalidPlaceholder() {
        // data
        Entry entry = new Entry("Test Action", YEAR_MONTH.atDay(12), new TimeSpan(10, 0), new TimeSpan(14, 0), new TimeSpan(0, 30), false);
        Entry[] entries = new Entry[] {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        String template = "something !noPlaceholderHere else";
        LatexGenerator generator = new LatexGenerator(timeSheet, template);
        // execute
        String latex = generator.generate();
        // assert
        assertEquals("something !noPlaceholderHere else", latex);
    }

    @Test
    public void testGenerateTimeSheetPlaceholder() {
        // data
        Entry entry = new Entry("Test Action", YEAR_MONTH.atDay(12), new TimeSpan(10, 0), new TimeSpan(14, 0), new TimeSpan(0, 30), false);
        Entry[] entries = new Entry[] {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        String template = "Name: !employeeName";
        LatexGenerator generator = new LatexGenerator(timeSheet, template);
        // execute
        String latex = generator.generate();
        // assert
        assertEquals("Name: " + EMPLOYEE.getName(), latex);
    }

    @Test
    public void testGenerateTimeSheetPlaceholderMultiple() {
        // data
        Entry entry = new Entry("Test Action", YEAR_MONTH.atDay(12), new TimeSpan(10, 0), new TimeSpan(14, 0), new TimeSpan(0, 30), false);
        Entry[] entries = new Entry[] {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        String template = "Name: !employeeName, ID: !employeeID";
        LatexGenerator generator = new LatexGenerator(timeSheet, template);
        // execute
        String latex = generator.generate();
        // assert
        assertEquals("Name: " + EMPLOYEE.getName() + ", ID: " + EMPLOYEE.getId(), latex);
    }

    @Test
    public void testGenerateEntryPlaceholder() {
        // data
        Entry entry = new Entry("Test Action", YEAR_MONTH.atDay(12), new TimeSpan(10, 0), new TimeSpan(14, 0), new TimeSpan(0, 30), false);
        Entry[] entries = new Entry[] {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        String template = "Date: !date";
        LatexGenerator generator = new LatexGenerator(timeSheet, template);
        // execute
        String latex = generator.generate();
        // assert
        assertEquals("Date: " + entry.getDate().format(DATE_TIME_FORMATTER), latex);
    }

    @Test
    public void testGenerateEntryPlaceholderMultipleEqual() {
        // data
        Entry entry0 = new Entry("Test Action 1", YEAR_MONTH.atDay(12), new TimeSpan(10, 0), new TimeSpan(14, 0), new TimeSpan(0, 30), false);
        Entry entry1 = new Entry("Test Action 2", YEAR_MONTH.atDay(14), new TimeSpan(8, 0), new TimeSpan(12, 0), new TimeSpan(0, 0), false);
        Entry[] entries = new Entry[] {entry0, entry1};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        String template = "Date 1: !date, Date 2: !date";
        LatexGenerator generator = new LatexGenerator(timeSheet, template);
        // expected
        String latexExp = "Date 1: " + entry0.getDate().format(DATE_TIME_FORMATTER) + ", Date 2: " + entry1.getDate().format(DATE_TIME_FORMATTER);
        // execute
        String latex = generator.generate();
        // assert
        assertEquals(latexExp, latex);
    }

    @Test
    public void testGenerateEntryPlaceholderMultipleDifferent() {
        // data
        Entry entry = new Entry("Test Action 1", YEAR_MONTH.atDay(12), new TimeSpan(10, 0), new TimeSpan(14, 0), new TimeSpan(0, 30), false);
        Entry[] entries = new Entry[] {entry};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        String template = "Date: !date, Pause: !break";
        LatexGenerator generator = new LatexGenerator(timeSheet, template);
        // expected
        String latexExp = "Date: " + entry.getDate().format(DATE_TIME_FORMATTER) + ", Pause: " + entry.getPause().toString();
        // execute
        String latex = generator.generate();
        // assert
        assertEquals(latexExp, latex);
    }

    @Test
    public void testGenerateEntryPlaceholderMultipleEqualAndDifferent() {
        // data
        Entry entry0 = new Entry("Test Action 1", YEAR_MONTH.atDay(12), new TimeSpan(10, 0), new TimeSpan(14, 0), new TimeSpan(0, 30), false);
        Entry entry1 = new Entry("Test Action 2", YEAR_MONTH.atDay(14), new TimeSpan(8, 0), new TimeSpan(12, 0), new TimeSpan(0, 0), false);
        Entry[] entries = new Entry[] {entry0, entry1};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        String template = "Date 1: !date, Date 2: !date, Pause 1: !break, Pause 2: !break";
        LatexGenerator generator = new LatexGenerator(timeSheet, template);
        // expected
        String latexExp = "Date 1: " + entry0.getDate().format(DATE_TIME_FORMATTER) + ", Date 2: " + entry1.getDate().format(DATE_TIME_FORMATTER) +
            ", Pause 1: " + entry0.getPause().toString() + ", Pause 2: " + entry1.getPause().toString();
        // execute
        String latex = generator.generate();
        // assert
        assertEquals(latexExp, latex);
    }

    @Test
    public void testGenerateEntryPlaceholderClearRemaining() {
        // data
        Entry entry0 = new Entry("Test Action 1", YEAR_MONTH.atDay(12), new TimeSpan(10, 0), new TimeSpan(14, 0), new TimeSpan(0, 30), false);
        Entry entry1 = new Entry("Test Action 2", YEAR_MONTH.atDay(14), new TimeSpan(8, 0), new TimeSpan(12, 0), new TimeSpan(0, 0), false);
        Entry[] entries = new Entry[] {entry0, entry1};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        String template = "Date 1: !date, Date 2: !date, (Date 3: !date)";
        LatexGenerator generator = new LatexGenerator(timeSheet, template);
        // expected
        String latexExp = "Date 1: " + entry0.getDate().format(DATE_TIME_FORMATTER) + ", Date 2: " + entry1.getDate().format(DATE_TIME_FORMATTER) + ", (Date 3: )";
        // execute
        String latex = generator.generate();
        // assert
        assertEquals(latexExp, latex);
    }

    @Test
    public void testGenerateEntryPlaceholderTooMany() {
        // data
        Entry entry0 = new Entry("Test Action 1", YEAR_MONTH.atDay(12), new TimeSpan(10, 0), new TimeSpan(14, 0), new TimeSpan(0, 30), false);
        Entry entry1 = new Entry("Test Action 2", YEAR_MONTH.atDay(14), new TimeSpan(8, 0), new TimeSpan(12, 0), new TimeSpan(0, 0), false);
        Entry[] entries = new Entry[] {entry0, entry1};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        String template = "Date: !date";
        LatexGenerator generator = new LatexGenerator(timeSheet, template);
        // expected
        String latexExp = "Date: " + entry0.getDate().format(DATE_TIME_FORMATTER);
        // execute
        String latex = generator.generate();
        // assert
        assertEquals(latexExp, latex);
    }

    @Test
    public void testGeneratePlaceholderMixed() {
        // data
        Entry entry0 = new Entry("Test Action 1", YEAR_MONTH.atDay(12), new TimeSpan(10, 0), new TimeSpan(14, 0), new TimeSpan(0, 30), false);
        Entry entry1 = new Entry("Test Action 2", YEAR_MONTH.atDay(14), new TimeSpan(8, 0), new TimeSpan(12, 0), new TimeSpan(0, 0), false);
        Entry[] entries = new Entry[] {entry0, entry1};
        TimeSheet timeSheet = new TimeSheet(EMPLOYEE, PROFESSION, YEAR_MONTH, entries, zeroTs, zeroTs);
        String template = "Name: !employeeName, ID: !employeeID, Date 1: !date, Date 2: !date, Pause 1: !break, Pause 2: !break";
        LatexGenerator generator = new LatexGenerator(timeSheet, template);
        // expected
        String latexExp = "Name: " + EMPLOYEE.getName() + ", ID: " + EMPLOYEE.getId() +
            ", Date 1: " + entry0.getDate().format(DATE_TIME_FORMATTER) + ", Date 2: " + entry1.getDate().format(DATE_TIME_FORMATTER) +
            ", Pause 1: " + entry0.getPause().toString() + ", Pause 2: " + entry1.getPause().toString();
        // execute
        String latex = generator.generate();
        // assert
        assertEquals(latexExp, latex);
    }

}
