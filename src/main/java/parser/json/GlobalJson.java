package parser.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import data.TimeSpan;
import data.WorkingArea;

@JsonIgnoreProperties({ "$schema" })
class GlobalJson {

    private final String name;
    private final int staffId;
    private final String department;
    private final TimeSpan workingTime;
    private final double wage;
    private final WorkingArea workingArea;
    
    @JsonCreator
    GlobalJson(
        @JsonProperty(value="name", required=true) String name,
        @JsonProperty(value="staffId", required=true) int staffId,
        @JsonProperty(value="department", required=true) String department,
        @JsonProperty(value="workingTime", required=true) String workingTime,
        @JsonProperty(value="wage", required=true) double wage,
        @JsonProperty(value="workingArea", required=true) String workingArea
    ) {
        this.name = name;
        this.staffId = staffId;
        this.department = department;
        this.workingTime = TimeSpan.parse(workingTime);
        this.wage = wage;
        this.workingArea = WorkingArea.parse(workingArea);
    }
    
    public String getName() {
        return name;
    }

    public int getStaffId() {
        return staffId;
    }

    public String getDepartment() {
        return department;
    }

    public TimeSpan getWorkingTime() {
        return workingTime;
    }

    public double getWage() {
        return wage;
    }

    public WorkingArea getWorkingArea() {
        return workingArea;
    }
    
}
