package io;

import java.time.format.DateTimeFormatter;

import javax.swing.filechooser.FileNameExtensionFilter;

import data.Entry;
import data.TimeSheet;
import data.WorkingArea;

public class LatexGenerator implements IGenerator {

    private final static String FILE_EXTENSION = "tex";
    private final static String FILE_DESCRIPTION = "TeX-File";
    
    private final TimeSheet timeSheet;
    private final String template;
    
    public LatexGenerator(TimeSheet timeSheet, String template) {
        this.timeSheet = timeSheet;
        this.template = template;
    }

    @Override
    public String generate() {
        String filledTex = template;
        
        /*
         * This loop replaces the document-public placeholder in the TeX template with the correct data.
         */
        for (TimeSheetElement elem : TimeSheetElement.values()) {
            filledTex = filledTex.replace(elem.getPlaceholder(), getSubstitute(timeSheet, elem));
        }
        
        /*
         * This loop replaces the placeholder in the TeX template with the correct data.
         * If the TimeSheet contains to many elements for the table,
         * all rows get filled and the rest of data gets lost.
         */
        for (EntryElement elem : EntryElement.values()) {
            String placeholder = elem.getPlaceholder();
            for (Entry entry : timeSheet.getEntries()) {
                filledTex = filledTex.replaceFirst(placeholder, getSubstitute(entry, elem));
            }
        }
        
        /*
         * This loop fills up all not-needed rows of the table with a blank space.
         * IMPORTANT: Some kind of character is needed to make the TeX compile correctly on some TeX compilers.
         */
        for (EntryElement elem : EntryElement.values()) {
            filledTex = filledTex.replace(elem.getPlaceholder(), "\\thinspace");
        }
         
        return filledTex;
    }

    @Override
    public FileNameExtensionFilter getFileNameExtensionFilter() {
        return new FileNameExtensionFilter(FILE_DESCRIPTION, FILE_EXTENSION);
    }

    private static String getSubstitute(TimeSheet timeSheet, TimeSheetElement element) {
        String value;
        switch (element) {
            case YEAR:
                value =  Integer.toString(timeSheet.getYear());
                break;
            case MONTH:
                value = Integer.toString(timeSheet.getMonth().getValue());
                break;
            case EMPLOYEE_NAME:
                value = timeSheet.getEmployee().getName();
                break;
            case EMPLOYEE_ID:
                value = Integer.toString(timeSheet.getEmployee().getId());
                break;
            case GFUB:
                if (timeSheet.getProfession().getWorkingArea() == WorkingArea.GF) {
                    value = "GF: $\\boxtimes$ UB: $\\Box$";
                } else {
                    value = "GF: $\\Box$ UB: $\\boxtimes$";
                }
                break;
            case DEPARTMENT:
                value = timeSheet.getProfession().getDepartmentName();
                break;
            case MAX_HOURS:
                value = timeSheet.getProfession().getMaxWorkingTime().toString();
                break;
            case WAGE:
                value = Double.toString(timeSheet.getProfession().getWage());
                break;
            case VACATION:
                value = timeSheet.getVacation().toString();
                break;
            case HOURS_SUM:
                value = timeSheet.getTotalWorkTime().add(timeSheet.getVacation()).toString();
                break;
            case TRANSFER_PRED:
                value = timeSheet.getPredTranfer().toString();
                break;
            case TRANSFER_SUCC:
                value = timeSheet.getSuccTransfer().toString();
                break;
            default:
                value = null;
                break;
        }
        return value;
    }
    
    private static String getSubstitute(Entry entry, EntryElement element) {
        String value;
        switch (element) {
            case TABLE_ACTION:
                value = entry.getAction();
                break;
            case TABLE_DATE:
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
                value = entry.getDate().format(formatter);
                break;
            case TABLE_START:
                value = entry.getStart().toString();
                break;
            case TABLE_END:
                value = entry.getEnd().toString();
                break;
            case TABLE_PAUSE:
                value = entry.getPause().toString();
                break;
            case TABLE_TIME:
                value = entry.getWorkingTime().toString();
                break;
            default:
                value = null;
                break;
        }
        return value;
    }
    
    private static enum TimeSheetElement {
        YEAR("!year"),
        MONTH("!month"),
        EMPLOYEE_NAME("!employeeName"),
        EMPLOYEE_ID("!employeeID"),
        GFUB("!workingArea"),
        DEPARTMENT("!department"),
        MAX_HOURS("!workingTime"),
        WAGE("!wage"),
        VACATION("!vacation"),
        HOURS_SUM("!sum"),
        TRANSFER_PRED("!carryPred"),
        TRANSFER_SUCC("!carrySucc");
        
        private final String placeholder;
        
        private TimeSheetElement(String placeholder) {
            this.placeholder = placeholder;
        }
        
        public String getPlaceholder() {
            return this.placeholder;
        }
    }
    
    private static enum EntryElement {
        TABLE_ACTION("!action"),
        TABLE_DATE("!date"),
        TABLE_START("!begin"),
        TABLE_END("!end"),
        TABLE_PAUSE("!break"),
        TABLE_TIME("!dayTotal");
        
        private final String placeholder;
        
        private EntryElement(String placeholder) {
            this.placeholder = placeholder;
        }
        
        public String getPlaceholder() {
            return this.placeholder;
        }
    }
}
