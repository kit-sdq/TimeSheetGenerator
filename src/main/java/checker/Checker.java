package checker;

import data.Entry;
import data.FullDocumentation;
import data.TimeSpan;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

/**
 * TODO Documentation of class
 * @author Liam Wachter
 */
public class Checker {
    private static final TimeSpan WORKDAY_LOWER_BOUND = new TimeSpan(6, 0);
    private static final TimeSpan WORKDAY_UPPER_BOUND = new TimeSpan(22, 0);
    
    //TODO Replace with enum
    private static final TimeSpan[][] PAUSE_RULES = {{new TimeSpan(6, 0), new TimeSpan(0, 30)},{new TimeSpan(9, 0), new TimeSpan(0, 45)}};
    private static final int MAX_ROW_NUM = 22;
    private static final PublicHolidayFetcher HOLIDAY_FETCHER = new PublicHolidayFetcher(State.BW);

    /**
     * Runs all of the needed tests in order to validate the {@link FullDocumentation} instance.
     * 
     * @param fullDoc - {@link FullDocumentation} instance to get checked
     * @return {@link CheckerReturn} value with error or validity message
     */
    public CheckerReturn check(FullDocumentation fullDoc) {
        CheckerReturn result = CheckerReturn.VALID;
        result = (result.equals(CheckerReturn.VALID)) ? checkTotalTimeExceedance(fullDoc) : result;
        result = (result.equals(CheckerReturn.VALID)) ? checkDayTimeExceedances(fullDoc) : result;
        result = (result.equals(CheckerReturn.VALID)) ? checkDayTimeBounds(fullDoc) : result;
        result = (result.equals(CheckerReturn.VALID)) ? checkNoWorkingDays(fullDoc) : result;
        result = (result.equals(CheckerReturn.VALID)) ? checkRowNumExceedance(fullDoc) : result;
        result = (result.equals(CheckerReturn.VALID)) ? checkDepartmentName(fullDoc) : result;
        
        //Always returns the last error that occurred.
        return result;
    }

    /**
     * Checks whether maximum working time was exceeded.
     * 
     * @param fullDoc - {@link FullDocumentation} instance to get checked
     * @return {@link CheckerReturn} value for time exceedance or validity
     */
    protected CheckerReturn checkTotalTimeExceedance(FullDocumentation fullDoc) {
        TimeSpan maxWorkingTime = new TimeSpan(fullDoc.getMaxWorkTime(), 0);
        
        if (fullDoc.getTotalWorkTime().compareTo(maxWorkingTime) > 0) {
            return CheckerReturn.TIME_EXCEEDANCE;
        }
        
        return CheckerReturn.VALID;
    }
    
    /**
     * Checks whether the working time per day meets all legal pause rules.
     * 
     * @param fullDoc - {@link FullDocumentation} instance to get checked
     * @return {@link CheckerReturn} value for missing pause or validity
     */
    protected CheckerReturn checkDayTimeExceedances(FullDocumentation fullDoc) {    
        //This map contains all dates associated with their working times
        HashMap<Date,TimeSpan[]> workingDays = new HashMap<Date, TimeSpan[]>();
        
        for (Entry entry : fullDoc.getEntries()) {
            //This deep copy allows us arithmetic operations without changing the fullDoc
            TimeSpan clonedWorkingTime = new TimeSpan(entry.getWorkingTime().getHour(), entry.getWorkingTime().getMinute());
            TimeSpan clonedPause = new TimeSpan(entry.getPause().getHour(), entry.getPause().getMinute());
            Date date = entry.getDate();
            
            //If a day appears more than once this logic sums all of the working times
            if (workingDays.containsKey(date)) {
                TimeSpan oldWorkingTime = workingDays.get(date)[0];
                clonedWorkingTime.add(oldWorkingTime);
                
                TimeSpan oldPause = workingDays.get(date)[1];
                clonedPause.add(oldPause);
            }
            
            //Adds the day to the map, replaces the old entry respectively
            TimeSpan[] mapTimeEntry = {clonedWorkingTime, clonedPause};
            workingDays.put(date, mapTimeEntry);
        }
        
        //Check for every mapTimeEntry (first for), whether all Pause Rules (second for) where met.
        for (TimeSpan[] mapTimeEntry : workingDays.values()) {
            for (TimeSpan[] pauseRule : PAUSE_RULES) {
                
                //Checks whether time of entry is greater than or equal pause rule "activation" time
                //and pause time is less than the needed time.
                if (mapTimeEntry[0].compareTo(pauseRule[0]) >= 0
                        && mapTimeEntry[1].compareTo(pauseRule[1]) < 0) {
                    
                    return CheckerReturn.TIME_PAUSE;
                }
            }
        }
        
        return CheckerReturn.VALID;
    }
    
    /**
     * Checks whether the working time per day is inside the legal bounds.
     * 
     * @param fullDoc - {@link FullDocumentation} instance to get checked
     * @return {@link CheckerReturn} value for time out of bounds or validity
     */
    protected CheckerReturn checkDayTimeBounds(FullDocumentation fullDoc) {
        for (Entry entry : fullDoc.getEntries()) {
            if (entry.getStart().compareTo(WORKDAY_LOWER_BOUND) < 0
                    || entry.getEnd().compareTo(WORKDAY_UPPER_BOUND) > 0) {
                
                return CheckerReturn.TIME_OUTOFBOUNDS;
            }
        }
        return CheckerReturn.VALID;
    }
    
    /**
     * Checks whether all of the days are valid working days.
     * 
     * @param fullDoc - {@link FullDocumentation} instance to get checked
     * @return {@link CheckerReturn} value for Sunday, holiday or validity
     */
    protected CheckerReturn checkNoWorkingDays(FullDocumentation fullDoc) {
        //TODO What happens to non-valid days like 32snd of January?
        for (Entry entry : fullDoc.getEntries()) {
            LocalDate localDate = entry.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            //Checks whether the day of the entry is Sunday
            if (localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                return CheckerReturn.TIME_SUNDAY;
            }
            
            //Check for each entry whether it is a holiday
            for (Holiday holiday : HOLIDAY_FETCHER.getHolidaysByYear(fullDoc.getYear())) {
                if (holiday.getDate().equals(localDate)) {
                    return CheckerReturn.TIME_HOLIDAY;
                }
            }
        }
        
        return CheckerReturn.VALID;
    }
    
    /**
     * Checks whether the number of entries exceeds the maximum number of rows of the template document.
     * 
     * @param fullDoc - {@link FullDocumentation} instance to get checked
     * @return {@link CheckerReturn} value for row number exceedance or validity
     */
    protected CheckerReturn checkRowNumExceedance(FullDocumentation fullDoc) {
        if (fullDoc.getEntries().length > MAX_ROW_NUM) {
            return CheckerReturn.ROWNUM_EXCEEDENCE;
        }
        return CheckerReturn.VALID;
    }

    /**
     * Checks whether the department name is empty.
     * 
     * @param fullDoc - {@link FullDocumentation} instance to get checked
     * @return {@link CheckerReturn} value for missing department name or validity
     */
    protected static CheckerReturn checkDepartmentName(FullDocumentation fullDoc) {
        return fullDoc.getDepartmentName().equals("") ? CheckerReturn.NAME_MISSING : CheckerReturn.VALID;
    }
}
