package parser;

import data.Employee;
import data.Profession;

/**
 * A global parser provides the functionality of parsing an {@link Employee} and {@link Profession}
 * out of given data.
 */
public interface IGlobalParser {
    
    /**
     * Returns an {@link Employee} that got parsed from data. 
     * @return An employee.
     * @throws ParseException if an error occurs while parsing.
     */
    public Employee getEmployee() throws ParseException;
    
    /**
     * Returns a {@link Profession} that got parsed from data. 
     * @return A profession.
     * @throws ParseException if an error occurs while parsing.
     */
    public Profession getProfession() throws ParseException;
    
}
