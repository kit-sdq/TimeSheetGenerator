package parser;

import data.FullDocumentation;
import org.json.*;

public class Parser implements IParser {
    @Override
    public FullDocumentation parse(String global, String month) throws IllegalArgumentException {
        return null;
    }

    private void parseGlobal(String global) {
        JSONObject json = new JSONObject(global);
        //TODO do actual parsing
    }

    private void parseMonth(String month) {
        JSONObject json = new JSONObject(month);
        //TODO do actual parsing
    }
}
