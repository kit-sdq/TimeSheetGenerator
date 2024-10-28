package net.justonedev.kit.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Global {
    @JsonProperty("$schema")
    private String schema;
    private String name;
    private int staffId;
    private String department;
    private String workingTime;
    private double wage;
    private String workingArea;

    public Global() {
        schema = "https://raw.githubusercontent.com/kit-sdq/TimeSheetGenerator/master/examples/schemas/global.json";
    }

    // Constructors, Getters, and Setters

    public String getSchema() { return schema; }
    public void setSchema(String schema) { this.schema = schema; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getWorkingTime() { return workingTime; }
    public void setWorkingTime(String workingTime) { this.workingTime = workingTime; }

    public double getWage() { return wage; }
    public void setWage(double wage) { this.wage = wage; }

    public String getWorkingArea() { return workingArea; }
    public void setWorkingArea(String workingArea) { this.workingArea = workingArea; }
}
