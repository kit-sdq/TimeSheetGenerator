package io;

import java.text.SimpleDateFormat;
import data.Entry;

enum TablePlaceholder implements IPlaceholder {
    
    TABLE_ACTION("<action>"),
    TABLE_DATE("<date>"),
    TABLE_BEGIN("<begin>"),
    TABLE_END("<end>"),
    TABLE_BREAK("<break>"),
    TABLE_TIME("<time>");
    
    private final String placeholder;
    
    private TablePlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
    
    @Override
    public String getPlaceholder() {
        return this.placeholder;
    }
    
    /**
     * Gets a substitute associated with a placeholder from an Entry.
     * 
     * @param entry - Entry to get the substitutes from
     * @return The substitute element parsed to a string
     */
    public String getSubstitute(Entry entry) {
        String substitute;
        switch (this) {
            case TABLE_ACTION:
                substitute = entry.getAction();
                break;
            case TABLE_DATE:
                SimpleDateFormat datePattern = new SimpleDateFormat("dd.MM.yy");
                substitute = datePattern.format(entry.getDate());
                break;
            case TABLE_BEGIN:
                substitute = entry.getStart().toString();
                break;
            case TABLE_END:
                substitute = entry.getEnd().toString();
                break;
            case TABLE_BREAK:
                substitute = entry.getPause().toString();
                break;
            case TABLE_TIME:
                substitute = entry.getWorkingTime().toString();
                break;
            default:
                substitute = null;
                break;
        }
        return substitute;
    }
}
