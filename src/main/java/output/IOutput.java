package output;

import data.FullDocumentation;

/**
 * @author Liam Wachter
 */
public interface IOutput {
    /**
     * @param documentation the checked documentation
     * @return LaTeX code that can be used for generating the documentation.pdf
     */
    String generateLaTeX(FullDocumentation documentation);
}
