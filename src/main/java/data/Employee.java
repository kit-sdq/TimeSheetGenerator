package data;

/**
 * Represents an employee.
 */
public class Employee {

    private final String name;
    private final int id;
    
    /**
     * Constructs a new employee instance.
     * @param name - The full name of the employee
     * @param id - The employee id
     */
    public Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Gets the name of an employee.
     * @return The name of the employee.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Gets the id of an employee.
     * @return The id of the employee.
     */
    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Employee)) {
            return false;
        }

        Employee otherEmployee = (Employee)other;
        if (!this.name.equals(otherEmployee.name)) {
            return false;
        } else if (this.id != otherEmployee.id) {
            return false;
        } else {
            return true;
        }
    }

}
