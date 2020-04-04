package parser.json;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;

class HolidayMapJson {

    private final Map<String, HolidayJson> holidays;
    
    HolidayMapJson() {
        this.holidays = new HashMap<String, HolidayJson>();
    }
    
    @JsonAnySetter
    public void addHoliday(String key, HolidayJson value) {
        holidays.put(key, value);
    }
    
    public Map<String, HolidayJson> getHolidays() {
        return holidays;
    }
    
}
