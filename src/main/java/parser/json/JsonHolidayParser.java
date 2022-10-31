package parser.json;

import java.util.Collection;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import checker.holiday.Holiday;
import parser.IHolidayParser;
import parser.ParseException;

/**
 * A JsonHolidayParser provides the functionality to parse the
 * elements specified by {@link IHolidayParser} from a json string.
 */
public class JsonHolidayParser implements IHolidayParser {

    private static final String SCHOOL_HOLIDAY_NOTE = "Gemäß § 4 Abs. 3 des Feiertagsgesetzes von " +
            "Baden-Württemberg[10] haben Schüler am Gründonnerstag und am Reformationstag schulfrei. In der Regel " +
            "legt das Kultusministerium die Ferientermine so fest, dass diese beiden Tage in die Osterferien bzw. " +
            "in die Herbstferien fallen.";
    
    private final String json;
    
    private HolidayMapJson holidayMap; // caching
    
    /**
     * Constructs a new {@link JsonHolidayParser} instance.
     * @param json - to parse the data from.
     */
    public JsonHolidayParser(String json) {
        this.json = json;
    }
    
    private HolidayMapJson parseJson() throws JsonProcessingException {
        if (holidayMap == null) {
            ObjectMapper mapper = JsonMapper.builder()
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build();
            holidayMap = mapper.readValue(json, HolidayMapJson.class);
        }
        
        return holidayMap;
    }

    @Override
    public Collection<Holiday> getHolidays() throws ParseException {
        try {
            HolidayMapJson holidayMap = parseJson();
            
            return holidayMap.getHolidays().entrySet().stream()
                    .filter(e -> e.getValue().getNote() == null || !e.getValue().getNote().equals(SCHOOL_HOLIDAY_NOTE))
                    .map(e -> new Holiday(
                            e.getValue().getDate(),
                            e.getKey()
                    )).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new ParseException(e.getMessage());
        }
    }

}
