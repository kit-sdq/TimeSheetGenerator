package io;

import data.TimeSheet;

import java.io.IOException;

/**
 * @author Liam Wachter
 */
@Deprecated
public interface IOutput {
    /**
     * @param documentation the checked documentation
     * @return LaTeX code that can be used for generating the documentation.pdf
     */
    String generateLaTeX(TimeSheet documentation) throws IOException;
}
