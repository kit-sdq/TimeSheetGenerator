package checker;

import data.FullDocumentation;
import data.TimeSpan;

import java.util.Arrays;

/**
 * @author Liam Wachter
 */
public class Checker {
    /**
     * Perform the actual checking.
     *
     * @param toCheck object to check
     * @return error message that may be outputted to the user. Or an empty String if no error was found
     */
    public String check(FullDocumentation toCheck) {
        // TODO call functions and return error messages
        return "";
    }
    /**
     * Check if sum is correct and less or equal max working hours per month
     */
    private String checkSum(FullDocumentation toCheck) {
        TimeSpan sum = new TimeSpan(0, 0);
        Arrays.stream(toCheck.getEntries()).forEach(e -> sum.add(e.getWorkingTime()));
        // worked more than max work hours
        if (sum.compareTo(toCheck.getMaxWorkTime()) > 0)
            return ErrorMessages.workedToMuch;
        return ErrorMessages.none;
    }

    private String nameNotEmpty(FullDocumentation toCheck)  {
        return toCheck.getDepartmentName().equals("") ? ErrorMessages.nameMissing : ErrorMessages.none;
    }
}
