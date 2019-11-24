package parser;

import data.Employee;
import data.Profession;

public interface IGlobalParser {
    
    public Employee getEmployee() throws ParseException;
    
    public Profession getProfession() throws ParseException;
    
}
