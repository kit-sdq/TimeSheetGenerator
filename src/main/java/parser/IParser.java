package parser;

import data.FullDocumentation;

/**
 * @author Liam Wachter
 */
public interface IParser {
    FullDocumentation parse(String global, String month) throws IllegalArgumentException, ParseException;
}
