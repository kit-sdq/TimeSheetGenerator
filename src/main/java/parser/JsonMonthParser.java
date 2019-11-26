package parser;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.Entry;
import data.TimeSpan;

public class JsonMonthParser implements IMonthParser {

    private final JSONObject json;
    
    public JsonMonthParser(JSONObject json) {
        this.json = json;
    }

    @Override
    public YearMonth getYearMonth() throws ParseException {
        YearMonth yearMonth;
        try {
            int month = json.getInt("month");
            int year = json.getInt("year");
            yearMonth = YearMonth.of(year, month);
        } catch (JSONException e) {
            throw new ParseException(e.getMessage());
        }
        
        return yearMonth;
    }

    @Override
    public Entry[] getEntries() throws ParseException {
        Entry[] entries;
        try {
            JSONArray entriesJSONArray = json.getJSONArray("entries");
            entries = new Entry[entriesJSONArray.length()];
            
            for (int i = 0; i < entries.length; i++) {
                entries[i] = parseEntry(entriesJSONArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            throw new ParseException(e.getMessage());
        }
        
        return entries;
    }

    @Override
    public TimeSpan getVacation() throws ParseException {
        String vacation = json.optString("vacation", "0:00");
        return TimeSpan.parse(vacation);
    }

    @Override
    public TimeSpan getSuccTransfer() throws ParseException {
        String vacation = json.optString("succ_transfer", "0:00");
        return TimeSpan.parse(vacation);
    }

    @Override
    public TimeSpan getPredTransfer() throws ParseException {
        String vacation = json.optString("pred_transfer", "0:00");
        return TimeSpan.parse(vacation);
    }
    
    //TODO Rework
    private Entry parseEntry(JSONObject json) throws JSONException, ParseException {        
        String dateString = json.getString("date");
        String action = json.getString("action");
        String startString = json.getString("start");
        String endString = json.getString("end");
        String pauseString = json.optString("pause", "00:00");
        
        // Fix Date String (ugly, but it works)
        String[] split = dateString.split("\\.");
        int year;
        try {
            year = Integer.parseInt(split[2]);
        } catch (NumberFormatException e) {
            throw new ParseException("Date format error. Usage: dd.MM.YYYY");
        }
        if (year < 2000) {
            dateString = split[0] + "." + split[1] + ".20" + split[2]; 
        }
        
        TimeSpan start, end, pause;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date date;
        try {
            date = sdf.parse(dateString);
        } catch (java.text.ParseException e) {
            throw new ParseException("Date format error. Usage: dd.MM.YYYY");
        }
        
        start = TimeSpan.parse(startString);
        end = TimeSpan.parse(endString);
        pause = TimeSpan.parse(pauseString);
        
        return new Entry(action, date, start, end, pause);
    }
}
