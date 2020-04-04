package parser.json;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

class HolidayJson {

    private final static String DATE_PATTERN = "yyyy-MM-dd";
    
    private final LocalDate date;
    private final String note;
    
    @JsonCreator
    HolidayJson(
        @JsonProperty(value="datum", required=true) String date,
        @JsonProperty(value="hinweis", required=false) String note
    ) {
        DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder().appendPattern(DATE_PATTERN).toFormatter();
        
        this.date = LocalDate.parse(date, dateFormatter);
        this.note = note;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public String getNote() {
        return note;
    }
    
}
