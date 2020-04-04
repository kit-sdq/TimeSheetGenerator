package parser.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import data.Employee;
import data.Profession;
import parser.IGlobalParser;
import parser.ParseException;

/**
 * A JsonGlobalParser provides the functionality to parse the
 * elements specified by {@link IGlobalParser} from a json string.
 */
public class JsonGlobalParser implements IGlobalParser {

    private final String json;
    
    private GlobalJson globalJson; // caching
    
    /**
     * Constructs a new {@link JsonGlobalParser} instance.
     * @param json - to parse the data from.
     */
    public JsonGlobalParser(String json) {
        this.json = json;
    }
    
    private GlobalJson parseJson() throws JsonProcessingException {
        if (globalJson == null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            globalJson = mapper.readValue(json, GlobalJson.class);
        }
        
        return globalJson;
    }

    @Override
    public Employee getEmployee() throws ParseException {
        Employee employee;
        try {
            GlobalJson global = parseJson();
            
            employee = new Employee(
                global.getName(),
                global.getStaffId()
            );
        } catch (JsonProcessingException e) {
            throw new ParseException(e.getMessage());
        }
        
        return employee;
    }

    @Override
    public Profession getProfession() throws ParseException {
        Profession profession;
        try {
            GlobalJson global = parseJson();
            
            profession = new Profession(
                global.getDepartment(),
                global.getWorkingArea(),
                global.getWorkingTime(),
                global.getWage()
            );
        } catch (JsonProcessingException e) {
            throw new ParseException(e.getMessage());
        }
        
        return profession;
    }

}
