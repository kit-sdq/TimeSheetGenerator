package io;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.Test;

import data.Employee;
import data.Entry;
import data.Profession;
import data.TimeSheet;
import data.TimeSpan;
import data.WorkingArea;

public class LatexGeneratorEscapeTest {
    
    @Test
    public void testEscapeWithoutSpecialCharacters() {
        // execute
        String result = LatexGenerator.escapeText("Hello World");
        // assert
        assertEquals("Hello World", result);
    }
    
    @Test
    public void testEscapeAllSpecialCharactersWithoutSpace() {
        // execute
        String result = LatexGenerator.escapeText("&%$#_{}\\~^");
        // assert
        assertEquals("\\&\\%\\$\\#\\_\\{\\}\\textbackslash \\textasciitilde \\textasciicircum ", result);
    }
    
    @Test
    public void testEscapeAllSpecialCharactersWithSpace() {
        // execute
        String result = LatexGenerator.escapeText(" & % $ # _ { } \\ ~ ^ ");
        // assert
        assertEquals(" \\& \\% \\$ \\# \\_ \\{ \\} \\textbackslash\\ \\textasciitilde\\ \\textasciicircum\\ ", result);
    }
    
    @Test
    public void testEscapeWithinNormalText() {
        // execute
        String result = LatexGenerator.escapeText("He__o ~ World");
        // assert
        assertEquals("He\\_\\_o \\textasciitilde\\ World", result);
    }
    
    @Test
    public void testEscapeTimesheetWithoutSpecialCharacters() {
        // data
        Employee employee = new Employee("Max Mustermann", 1234567);
        Profession profession = new Profession("Institut für Informatik", WorkingArea.UB, new TimeSpan(40, 0), 23.71);
        Entry[] entries = new Entry[] {
            new Entry("Fragen und Antworten", LocalDate.of(2020, 3, 21), new TimeSpan(9, 0), new TimeSpan(12, 0), new TimeSpan(0, 30), false)
        };
        TimeSheet timeSheet = new TimeSheet(
            employee,
            profession,
            YearMonth.of(2020, 3),
            entries,
            new TimeSpan(0, 0),
            new TimeSpan(0, 0)
        );
        // execute
        LatexGenerator generator = new LatexGenerator(timeSheet, "\\begin !employeeName !department !action \\end");
        String result = generator.generate();
        // assert
        assertEquals("\\begin Max Mustermann Institut für Informatik Fragen und Antworten \\end", result);
    }
    
    @Test
    public void testEscapeTimesheetWithSpecialCharacters() {
        // data
        Employee employee = new Employee("Max #Mustermann", 1234567);
        Profession profession = new Profession("Institut f~r Informatik", WorkingArea.UB, new TimeSpan(40, 0), 23.71);
        Entry[] entries = new Entry[] {
            new Entry("Fragen & Antworten", LocalDate.of(2020, 3, 21), new TimeSpan(9, 0), new TimeSpan(12, 0), new TimeSpan(0, 30), false)
        };
        TimeSheet timeSheet = new TimeSheet(
            employee,
            profession,
            YearMonth.of(2020, 3),
            entries,
            new TimeSpan(0, 0),
            new TimeSpan(0, 0)
        );
        // execute
        LatexGenerator generator = new LatexGenerator(timeSheet, "\\begin !employeeName !department !action \\end");
        String result = generator.generate();
        // assert
        assertEquals("\\begin Max \\#Mustermann Institut f\\textasciitilde r Informatik Fragen \\& Antworten \\end", result);
    }
    
}
