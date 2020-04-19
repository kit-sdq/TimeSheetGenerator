package parser.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import data.TimeSpan;

class MonthEntryJson {

    private final String action;
    private final int day;
    private final TimeSpan start;
    private final TimeSpan end;
    private TimeSpan pause;
    private boolean vacation;
    
    @JsonCreator
    MonthEntryJson(
        @JsonProperty(value="action", required=true) String action,
        @JsonProperty(value="day", required=true) int day,
        @JsonProperty(value="start", required=true) String start,
        @JsonProperty(value="end", required=true) String end
    ) {
        this.action = action;
        this.day = day;
        this.start = TimeSpan.parse(start);
        this.end = TimeSpan.parse(end);
        this.pause = new TimeSpan(0, 0); // default
        this.vacation = false; // default
    }

    public String getAction() {
        return action;
    }

    public int getDay() {
        return day;
    }

    public TimeSpan getStart() {
        return start;
    }

    public TimeSpan getEnd() {
        return end;
    }

    public TimeSpan getPause() {
        return pause;
    }
    
    @JsonProperty("pause")
    public void setPause(String pause) {
        this.pause = TimeSpan.parse(pause);
    }
    
    public boolean getVacation() {
        return vacation;
    }
    
    @JsonProperty("vacation")
    public void setVacation(boolean vacation) {
        this.vacation = vacation;
    }

}
