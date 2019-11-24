package parser;

import data.TimeSheet;

/**
 * @author Liam Wachter
 */
public interface IParser {
    TimeSheet parse(String global, String month) throws IllegalArgumentException, ParseException;
}
