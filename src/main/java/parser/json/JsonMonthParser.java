package parser.json;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import data.Entry;
import data.TimeSpan;
import parser.IMonthParser;
import parser.ParseException;

/**
 * A JsonMonthParser provides the functionality to parse the
 * elements specified by {@link IMonthParser} from a json string.
 */
public class JsonMonthParser implements IMonthParser {

    private final String json;
    
    private MonthJson monthJson; // caching
    
    /**
     * Constructs a new {@link JsonMonthParser} instance.
     * @param json - to parse the data from.
     */
    public JsonMonthParser(String json) {
        this.json = json;
    }
    
    private MonthJson parse() throws JsonProcessingException {
        if (monthJson == null) {
            ObjectMapper mapper = JsonMapper.builder()
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            monthJson = mapper.readValue(json, MonthJson.class);
        }
        
        return monthJson;
    }

    @Override
    public YearMonth getYearMonth() throws ParseException {
        try {
            return parse().getYearMonth();
        } catch (JsonProcessingException e) {
            throw new ParseException(e.getMessage());
        }
    }

    @Override
    public Entry[] getEntries() throws ParseException {
        List<Entry> entries;
        try {
            MonthJson month = parse();
            
            // catching the ParseException is necessary, because Java complains about a unhandled exception otherwise
            // declaring the exception is not possible in a lambda function
            // a workaround is to encapsulate the exception in a RuntimeException, which doesn't have to be declared
            // since we expect the RuntimeException caught outside the lambda function to include the actual exception as the cause,
            // we need to encapsulate RuntimeExceptions thrown in parseEntry(..) as well
            // (note that IllegalArgumentException is a RuntimeException as well)
            entries = month.getEntries().stream().map(entry -> {
                try {
                    return parseEntry(entry);
                } catch (ParseException | RuntimeException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        } catch(RuntimeException e) {
            throw new ParseException(e.getCause().getMessage());
        } catch (JsonProcessingException e) {
            throw new ParseException(e.getMessage());
        }
        
        return entries.toArray(new Entry[entries.size()]);
    }

    @Override
    public TimeSpan getSuccTransfer() throws ParseException {
        try {
            return parse().getSuccTransfer();
        } catch (JsonProcessingException e) {
            throw new ParseException(e.getMessage());
        }
    }

    @Override
    public TimeSpan getPredTransfer() throws ParseException {
        try {
            return parse().getPredTransfer();
        } catch (JsonProcessingException e) {
            throw new ParseException(e.getMessage());
        }
    }
    
    /**
     * Parses an {@link Entry} from an {@link MonthEntryJson}.
     * @param json - to parse {@link Entry} from
     * @return The entry parsed from the {@link MonthEntryJson}.
     * @throws ParseException if an error occurs while fetching the {@link YearMonth}.
     */
    private Entry parseEntry(MonthEntryJson entry) throws ParseException {
        // LocalDate construction
        YearMonth yearMonth = getYearMonth();
        LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), entry.getDay());
        
        return new Entry(
            entry.getAction(),
            date,
            entry.getStart(),
            entry.getEnd(),
            entry.getPause(),
            entry.getVacation()
        );
    }
    
}
