package parser;

import org.json.JSONException;
import org.json.JSONObject;

import data.Employee;
import data.Profession;
import data.TimeSpan;
import data.WorkingArea;

public class JsonGlobalParser implements IGlobalParser {

    private final JSONObject json;
    
    public JsonGlobalParser(JSONObject json) {
        this.json = json;
    }

    @Override
    public Employee getEmployee() throws ParseException {
        Employee employee;
        try {
            String name = json.getString("name");
            int id = json.getInt("personnelnumber");
            employee = new Employee(name, id);
        } catch (JSONException e) {
            throw new ParseException(e.getMessage());
        }
        
        return employee;
    }

    @Override
    public Profession getProfession() throws ParseException {
        Profession profession;
        try {
            String departmentName = json.getString("institute");
            TimeSpan maxWorkingTime = TimeSpan.parse(json.getString("workingTime"));
            double wage = json.getDouble("wage");    
            WorkingArea workingArea = WorkingArea.parse(json.getString("workingArea"));
            
            profession = new Profession(departmentName, workingArea, maxWorkingTime, wage);
        } catch (IllegalArgumentException | JSONException e) {
            throw new ParseException(e.getMessage());
        }
        
        return profession;
    }

}
