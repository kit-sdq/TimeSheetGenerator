package checker.holiday;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author copied from swt docu generator
 */
@Deprecated
public class PublicHolidayFetcher {

    private final String API_URL = "https://feiertage-api.de/api/?jahr=";
    private GermanState state;

    public PublicHolidayFetcher(GermanState state) {
        this.state = state;
    }

    public List<Holiday> getHolidaysByYear(int year) {
        String url = API_URL + year;
        try {
            JSONObject json = readJsonFromUrl(url);
            JSONObject stateJson = (JSONObject) json.get(state.name());
            DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();
            ArrayList<Holiday> result = new ArrayList<>();
            for (String name : stateJson.keySet()) {
                JSONObject data = (JSONObject) stateJson.get(name);
                String dateString = (String) data.get("datum");
                LocalDate date = LocalDate.parse(dateString, dateFormatter);
                result.add(new Holiday(date, name));
            }
            return result;
        } catch (Exception e) {
            System.out.println("Failed to fetch public holidays!");
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
