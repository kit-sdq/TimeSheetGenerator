package io;

import javax.swing.filechooser.FileNameExtensionFilter;

import data.Entry;
import data.TimeSheet;

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
        for (TimeSheet.Element elem : TimeSheet.Element.values()) {
            if (hasPlaceholder(elem)) {
                filledTex = filledTex.replace(getPlaceholder(elem), timeSheet.getElementStringValue(elem));
            }
        }
        
        /*
         * This loop replaces the placeholder in the TeX template with the correct data.
         * If the TimeSheet contains to many elements for the table,
         * all rows get filled and the rest of data gets lost.
         */
        for (Entry.Element elem : Entry.Element.values()) {
            if (hasPlaceholder(elem)) {
                String placeholder = getPlaceholder(elem);
                for (Entry entry : timeSheet.getEntries()) {
                    filledTex = filledTex.replaceFirst(placeholder, entry.getElementStringValue(elem));
                }
            }
        }
        
        /*
         * This loop fills up all not-needed rows of the table with a blank space.
         * IMPORTANT: Some kind of character is needed to make the TeX compile correctly on some TeX compilers.
         */
        for (Entry.Element elem : Entry.Element.values()) {
            filledTex = filledTex.replace(getPlaceholder(elem), "\\thinspace");
        }
         
        return filledTex;
    }

    @Override
    public FileNameExtensionFilter getFileNameExtensionFilter() {
        return new FileNameExtensionFilter(FILE_DESCRIPTION, FILE_EXTENSION);
    }

    private static String getPlaceholder(TimeSheet.Element element) {
        String value;
        switch (element) {
            case YEAR:
                value = "<year>";
                break;
            case MONTH:
                value = "<month>";
                break;
            case EMPLOYEE_NAME:
                value = "<employeeName>";
                break;
            case EMPLOYEE_ID:
                value = "<employeeNumber>";
                break;
            case GFUB:
                value = "<gfub>";
                break;
            case DEPARTMENT:
                value = "<department>";
                break;
            case MAX_HOURS:
                value = "<maxHours>";
                break;
            case WAGE:
                value = "<wage>";
                break;
            case VACATION:
                value = "<vacation>";
                break;
            case HOURS_SUM:
                value = "<sum>";
                break;
            case TRANSFER_PRED:
                value = "<carryFrom>";
                break;
            case TRANSFER_SUCC:
                value = "<carryTo>";
                break;
            default:
                value = null;
                break;
        }
        return value;
    }
    
    private static String getPlaceholder(Entry.Element element) {
        String value;
        switch (element) {
            case TABLE_ACTION:
                value = "<action>";
                break;
            case TABLE_DATE:
                value = "<date>";
                break;
            case TABLE_START:
                value = "<begin>";
                break;
            case TABLE_END:
                value = "<end>";
                break;
            case TABLE_PAUSE:
                value = "<break>";
                break;
            case TABLE_TIME:
                value = "<time>";
                break;
            default:
                value = null;
                break;
        }
        return value;
    }
    
    private static boolean hasPlaceholder(TimeSheet.Element element) {
        return getPlaceholder(element) != null;
    }
    
    private static boolean hasPlaceholder(Entry.Element element) {
        return getPlaceholder(element) != null;
    }
}
