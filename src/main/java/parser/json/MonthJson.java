package parser.json;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import data.TimeSpan;

@JsonIgnoreProperties({ "$schema" })
class MonthJson {
    
    private final YearMonth yearMonth;
    private TimeSpan predTransfer;
    private TimeSpan succTransfer;
    private final List<MonthEntryJson> entries;

    @JsonCreator
    MonthJson(
        @JsonProperty(value="year", required=true) int year,
        @JsonProperty(value="month", required=true) int month,
        @JsonProperty(value="entries", required=true) List<MonthEntryJson> entries
    ) {
        this.yearMonth = YearMonth.of(year, month);
        this.predTransfer = new TimeSpan(0, 0); // default
        this.succTransfer = new TimeSpan(0, 0); // default
        this.entries = new ArrayList<MonthEntryJson>(entries);
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }
    
    public TimeSpan getPredTransfer() {
        return predTransfer;
    }
    
    @JsonProperty("pred_transfer")
    public void setPredTransfer(String predTransfer) {
        this.predTransfer = TimeSpan.parse(predTransfer);
    }

    public TimeSpan getSuccTransfer() {
        return succTransfer;
    }

    @JsonProperty("succ_transfer")
    public void setSuccTransfer(String succTransfer) {
        this.succTransfer = TimeSpan.parse(succTransfer);
    }

    public List<MonthEntryJson> getEntries() {
        return entries;
    }
    
}
