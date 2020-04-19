package parser.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import data.ClockTime;
import data.TimeSpan;

class MonthEntryJson {

    private final String action;
    private final int day;
    private final ClockTime start;
    private final ClockTime end;
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
        this.start = ClockTime.parse(start);
        this.end = ClockTime.parse(end);
        this.pause = new TimeSpan(0, 0); // default
        this.vacation = false; // default
    }

    public String getAction() {
        return action;
    }

    public int getDay() {
        return day;
    }

    public ClockTime getStart() {
        return start;
    }

    public ClockTime getEnd() {
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
