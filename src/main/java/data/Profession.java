package data;

/**
 * Represents the profession of an {@link Employee employee}.
 */
public class Profession {

    private final String departmentName;
    private final WorkingArea workingArea;
    private final TimeSpan maxWorkingTime;
    private final double wage;
    
    /**
     * Constructs a new {@link Profession profession} instance.
     * @param departmentName - Name of the department this profession is associated with.
     * @param workingArea - {@link WorkingArea} of this profession.
     * @param maxWorkingHours - Maximum number of hours an {@link Employee employee} may work per month.
     * @param wage - Wage per hour an {@link Employee employee} earns.
     */
    public Profession(String departmentName, WorkingArea workingArea, TimeSpan maxWorkingTime, double wage) {
        this.departmentName = departmentName;
        this.workingArea = workingArea;
        this.maxWorkingTime = maxWorkingTime;
        this.wage = wage;
    }

    /**
     * Gets the name of the department this profession is associated with.
     * @return The name of the department.
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * Gets the {@link WorkingArea} of this profession.
     * @return The {@link WorkingArea}.
     */
    public WorkingArea getWorkingArea() {
        return workingArea;
    }

    /**
     * Gets the maximum number of hours an {@link Employee employee} may work per month.
     * @return The maximum number of hours.
     */
    public TimeSpan getMaxWorkingTime() {
        return maxWorkingTime;
    }

    /**
     * Gets the wage per hour an {@link Employee employee} earns.
     * @return The wage per hour.
     */
    public double getWage() {
        return wage;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Profession)) {
            return false;
        }

        Profession otherProfession = (Profession)other;
        if (!this.departmentName.equals(otherProfession.departmentName)) {
            return false;
        } else if (!this.workingArea.equals(otherProfession.workingArea)) {
            return false;
        } else if (!this.maxWorkingTime.equals(otherProfession.maxWorkingTime)) {
            return false;
        } else if (this.wage != otherProfession.wage) {
            return false;
        } else {
            return true;
        }
    }

}
