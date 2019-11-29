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
            String maxWorkingTimeString = json.getString("workingTime");
            TimeSpan maxWorkingTime = TimeSpan.parse(maxWorkingTimeString);
            double wage = json.getDouble("wage");    
            //TODO Replace boolean with string in global.json because ub tag deprecated 
            WorkingArea workingArea = json.getBoolean("gf") ? WorkingArea.GF : WorkingArea.UB;
            profession = new Profession(departmentName, workingArea, maxWorkingTime, wage);
        } catch (IllegalArgumentException | JSONException e) {
            throw new ParseException(e.getMessage());
        }
        
        return profession;
    }

}
