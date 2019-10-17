package data;

import java.util.Calendar;

/**
 * @author Liam Wachter
 */
public class FullDocumentation {
    private String departmentName;
    private int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
    private int year = Calendar.getInstance().get(Calendar.YEAR);
    private int id;
    private boolean gfub;
    private int maxWorkTime = 40;
    /**
     * Not intended to use for calculation just for ==
     */
    private double wage = 10.31;
    private Entry[] entries;

    /**
     * <code>year</code>, <code>month</code> are by default set to current
     * <code>wage</code> is by default 10.31
     * <code>maxWorkTime</code> is by default 40
     * those values can be set via setters of this class
     *
     * @param departmentName e.g IDP Prof. Reusner
     * @param id "Personalnummer"
     * @param gfub Checkbox GF or UB, <code>true</code> for GF
     * @param entries
     */
    public FullDocumentation(String departmentName, int id, boolean gfub, Entry[] entries) {
        this.departmentName = departmentName;
        this.id = id;
        this.gfub = gfub;
        this.entries = entries;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public void setMaxWorkTime(int maxWorkTime) {
        this.maxWorkTime = maxWorkTime;
    }

    public double getWage() {
        return wage;
    }

    public int getMaxWorkTime() {
        return maxWorkTime;
    }

    public int getYear() {
        return year;
    }

    public boolean isGfub() {
        return gfub;
    }

    public int getId() {
        return id;
    }

    public int getMonth() {
        return month;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public Entry[] getEntries() {
        return entries;
    }
}
