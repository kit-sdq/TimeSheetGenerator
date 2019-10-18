package parser;

import data.FullDocumentation;

public interface IParser {
    FullDocumentation parse(String global, String month) throws IllegalArgumentException;
}
