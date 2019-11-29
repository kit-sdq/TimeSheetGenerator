package checker;

import data.Entry;
import data.TimeSheet;
import data.TimeSpan;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;

import checker.holiday.Holiday;
import checker.holiday.PublicHolidayFetcher;
import checker.holiday.GermanState;

/**
 * TODO Documentation of class
 * @author Liam Wachter
 */
public class Checker {
    //TODO Are those static constant values better than attributes?
    private static final TimeSpan WORKDAY_LOWER_BOUND = new TimeSpan(6, 0);
    private static final TimeSpan WORKDAY_UPPER_BOUND = new TimeSpan(22, 0);
    
    //TODO Replace with enum
    private static final TimeSpan[][] PAUSE_RULES = {{new TimeSpan(6, 0), new TimeSpan(0, 30)},{new TimeSpan(9, 0), new TimeSpan(0, 45)}};
    private static final int MAX_ROW_NUM = 22;
    private static final PublicHolidayFetcher HOLIDAY_FETCHER = new PublicHolidayFetcher(GermanState.BW);

    private final TimeSheet fullDoc;
    
    public Checker(TimeSheet fullDoc) {
        this.fullDoc = fullDoc;
    }
    
    /**
     * Runs all of the needed tests in order to validate the {@link TimeSheet} instance.
     * 
     * @param fullDoc - {@link TimeSheet} instance to get checked
     * @return {@link CheckerReturn} value with error or validity message
     */
    public CheckerReturn check() {
        CheckerReturn result = CheckerReturn.VALID;
        result = (result.equals(CheckerReturn.VALID)) ? checkTotalTimeExceedance() : result;
        result = (result.equals(CheckerReturn.VALID)) ? checkDayTimeExceedances() : result;
        result = (result.equals(CheckerReturn.VALID)) ? checkDayTimeBounds() : result;
        result = (result.equals(CheckerReturn.VALID)) ? checkValidWorkingDays() : result;
        result = (result.equals(CheckerReturn.VALID)) ? checkRowNumExceedance() : result;
        result = (result.equals(CheckerReturn.VALID)) ? checkDepartmentName() : result;
        
        //Always returns the first error that occurred.
        return result;
    }

    /**
     * Checks whether maximum working time was exceeded.
     * 
     * @param fullDoc - {@link TimeSheet} instance to get checked
     * @return {@link CheckerReturn} value for time exceedance or validity
     */
    protected CheckerReturn checkTotalTimeExceedance() {
        TimeSpan maxWorkingTime = new TimeSpan(fullDoc.getProfession().getMaxWorkingHours(), 0);
        
        if (fullDoc.getTotalWorkTime().compareTo(maxWorkingTime) > 0) {
            return CheckerReturn.TIME_EXCEEDANCE;
        }
        
        return CheckerReturn.VALID;
    }
    
    /**
     * Checks whether the working time per day meets all legal pause rules.
     * 
     * @param fullDoc - {@link TimeSheet} instance to get checked
     * @return {@link CheckerReturn} value for missing pause or validity
     */
    protected CheckerReturn checkDayTimeExceedances() {    
        //This map contains all dates associated with their working times
        HashMap<LocalDate,TimeSpan[]> workingDays = new HashMap<LocalDate, TimeSpan[]>();
        
        for (Entry entry : fullDoc.getEntries()) {
            //EndToStart is the number of hours at this work shift.
            TimeSpan endToStart = entry.getEnd().clone();
            endToStart.subtract(entry.getStart());
            TimeSpan pause = entry.getPause().clone();
            LocalDate date = entry.getDate();
            
            //If a day appears more than once this logic sums all of the working times
            if (workingDays.containsKey(date)) {
                TimeSpan oldWorkingTime = workingDays.get(date)[0];
                endToStart.add(oldWorkingTime);
                
                TimeSpan oldPause = workingDays.get(date)[1];
                pause.add(oldPause);
            }
            
            //Adds the day to the map, replaces the old entry respectively
            TimeSpan[] mapTimeEntry = {endToStart, pause};
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
     * @param fullDoc - {@link TimeSheet} instance to get checked
     * @return {@link CheckerReturn} value for time out of bounds or validity
     */
    protected CheckerReturn checkDayTimeBounds() {
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
     * @param fullDoc - {@link TimeSheet} instance to get checked
     * @return {@link CheckerReturn} value for Sunday, holiday or validity
     */
    protected CheckerReturn checkValidWorkingDays() {
        for (Entry entry : fullDoc.getEntries()) {
            LocalDate localDate = entry.getDate();
            
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
     * @param fullDoc - {@link TimeSheet} instance to get checked
     * @return {@link CheckerReturn} value for row number exceedance or validity
     */
    protected CheckerReturn checkRowNumExceedance() {
        if (fullDoc.getEntries().length > MAX_ROW_NUM) {
            return CheckerReturn.ROWNUM_EXCEEDENCE;
        }
        return CheckerReturn.VALID;
    }

    /**
     * Checks whether the department name is empty.
     * 
     * @param fullDoc - {@link TimeSheet} instance to get checked
     * @return {@link CheckerReturn} value for missing department name or validity
     */
    protected CheckerReturn checkDepartmentName() {
        return fullDoc.getProfession().getDepartmentName().equals("") ? CheckerReturn.NAME_MISSING : CheckerReturn.VALID;
    }
    
    
    ////Following methods are primarily for testing purposes.
    /**
     * This method gets the maximally allowed number of entries inside a {@link TimeSheet}.
     * @return The maximum number of {@link Entry entries}.
     */
    protected static int getMaxEntries() {
        return MAX_ROW_NUM;
    }
    
    /**
     * This method gets the legal lower bound of time to start a working day.
     * @return The legal lower bound of time to start a working day.
     */
    protected static TimeSpan getWorkdayLowerBound() {
        return WORKDAY_LOWER_BOUND;
    }
    
    /**
     * This method gets the legal upper bound of time to end a working day.
     * @return The legal upper bound of time to end a working day.
     */
    protected static TimeSpan getWorkdayUpperBound() {
        return WORKDAY_UPPER_BOUND;
    }
    
    /**
     * This method gets the legal pause rules to conform to laws.
     * @return The legal pause rules.
     */
    protected static TimeSpan[][] getPauseRules() {
        return PAUSE_RULES;
    }
}
