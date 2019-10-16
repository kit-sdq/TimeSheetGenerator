package data;

/**
 * @author Liam Wachter
 */
public class FullDocumentation {
    private String departmentName;
    private int month;
    private int id;
    private boolean gfub;
    private int year;
    private int maxWorkTime;
    private double wage = 10.31;
    private int sum;
    private Entry[] entries;

    public FullDocumentation(String departmentName, int month, int id, boolean gfub, int year, int maxWorkTime, int sum, Entry[] entries) {
        this.departmentName = departmentName;
        this.month = month;
        this.id = id;
        this.gfub = gfub;
        this.year = year;
        this.maxWorkTime = maxWorkTime;
        this.sum = sum;
        this.entries = entries;
    }

    public int getSum() {
        return sum;
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
