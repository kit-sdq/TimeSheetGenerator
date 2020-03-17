package checker;

import data.Entry;
import data.TimeSheet;
import data.TimeSpan;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import checker.holiday.HolidayFetchException;
import checker.holiday.IHolidayChecker;
import checker.holiday.GermanState;
import checker.holiday.GermanyHolidayChecker;

/**
 * The MiLoGChecker checks whether a {@link TimeSheet} instance is valid or not.
 * This checker uses rules for its checks based on the German "Mindestlohngesetz" or "MiLoG".
 */
public class MiLoGChecker implements IChecker {
    //TODO Are those static constant values better than attributes?
    private static final TimeSpan WORKDAY_LOWER_BOUND = new TimeSpan(6, 0);
    private static final TimeSpan WORKDAY_UPPER_BOUND = new TimeSpan(22, 0);
    
    //TODO Replace with enum
    private static final TimeSpan[][] PAUSE_RULES = {{new TimeSpan(6, 0), new TimeSpan(0, 30)},{new TimeSpan(9, 0), new TimeSpan(0, 45)}};
    private static final int MAX_ROW_NUM = 22;
    private static final GermanState STATE = GermanState.BW;

    private final TimeSheet timeSheet;
    
    private CheckerReturn result;
    private final Collection<CheckerError> errors;
    
    /**
     * Constructs a new {@link MiLoGChecker} instance.
     * @param timeSheet - to be checked.
     */
    public MiLoGChecker(TimeSheet timeSheet) {
        this.timeSheet = timeSheet;
        
        this.result = CheckerReturn.VALID;
        this.errors = Collections.synchronizedCollection(new ArrayList<CheckerError>());
    }
    
    /**
     * Runs all of the needed tests in order to validate the {@link TimeSheet} instance.
     * 
     * @return {@link CheckerReturn} value with error or validity message
     * @throws CheckerException Thrown if an error occurs while checking the validity
     */
    @Override
    public CheckerReturn check() throws CheckerException {
        result = CheckerReturn.VALID;
        errors.clear();
        
        checkTotalTimeExceedance();
        checkDayTimeExceedances();
        checkDayTimeBounds();
        checkValidWorkingDays();
        checkTimeOverlap();
        
        checkRowNumExceedance();
        checkDepartmentName();
        
        return result;
    }
    
    /**
     * Returns a collection of all occurred checker errors during the execution of the last call to {@link #check()} 
     * @return {@link Collection} of occurred checker errors.
     */
    @Override
    public Collection<CheckerError> getErrors() {
        return new ArrayList<CheckerError>(errors);
    }

    /**
     * Checks whether maximum working time was exceeded.
     */
    protected void checkTotalTimeExceedance() {
        //Legal maximum working time per month
        TimeSpan maxWorkingTime = timeSheet.getProfession().getMaxWorkingTime();
        
        //Sum of all daily working times (without pauses!)
        TimeSpan totalWorkingTime = timeSheet.getTotalWorkTime();
        
        //Vacation and transfer corrected time
        TimeSpan correctedMaxWorkingTime = maxWorkingTime
                .add(timeSheet.getSuccTransfer())
                .subtract(timeSheet.getPredTransfer())
                .subtract(timeSheet.getTotalVacationTime());
        
        if (totalWorkingTime.compareTo(correctedMaxWorkingTime) > 0) {
            errors.add(new CheckerError(CheckerErrorMessage.TIME_EXCEEDANCE.getErrorMessage()));
            result = CheckerReturn.INVALID;
        }
    }
    
    /**
     * Checks whether the working time per day meets all legal pause rules.
     */
    protected void checkDayTimeExceedances() {    
        //This map contains all dates associated with their working times
        HashMap<LocalDate,TimeSpan[]> workingDays = new HashMap<LocalDate, TimeSpan[]>();
        
        for (Entry entry : timeSheet.getEntries()) {
            if (entry.isVacation()) {
                // TODO: what is the limit for a vacation?
            } else {
                //EndToStart is the number of hours at this work shift.
                TimeSpan endToStart = entry.getEnd();
                endToStart = endToStart.subtract(entry.getStart());
                TimeSpan pause = entry.getPause();
                LocalDate date = entry.getDate();
                
                //If a day appears more than once this logic sums all of the working times
                if (workingDays.containsKey(date)) {
                    TimeSpan oldWorkingTime = workingDays.get(date)[0];
                    endToStart = endToStart.add(oldWorkingTime);
                    
                    TimeSpan oldPause = workingDays.get(date)[1];
                    pause = pause.add(oldPause);
                }
                
                //Adds the day to the map, replaces the old entry respectively
                TimeSpan[] mapTimeEntry = {endToStart, pause};
                workingDays.put(date, mapTimeEntry);
            }
        }
        
        //Check for every mapTimeEntry (first for), whether all Pause Rules (second for) where met.
        for (Map.Entry<LocalDate, TimeSpan[]> mapEntry : workingDays.entrySet()) {
            for (TimeSpan[] pauseRule : PAUSE_RULES) {
                
                //Checks whether time of entry is greater than or equal pause rule "activation" time
                //and pause time is less than the needed time.
                if (mapEntry.getValue()[0].compareTo(pauseRule[0]) >= 0
                        && mapEntry.getValue()[1].compareTo(pauseRule[1]) < 0) {
                    
                    errors.add(new CheckerError(CheckerErrorMessage.TIME_PAUSE.getErrorMessage(), mapEntry.getKey()));
                    result = CheckerReturn.INVALID;
                    break;
                }
            }
        }
    }
    
    /**
     * Checks whether the working time per day is inside the legal bounds.
     */
    protected void checkDayTimeBounds() {
        for (Entry entry : timeSheet.getEntries()) {
            if (entry.getStart().compareTo(WORKDAY_LOWER_BOUND) < 0
                    || entry.getEnd().compareTo(WORKDAY_UPPER_BOUND) > 0) {
                
                errors.add(new CheckerError(CheckerErrorMessage.TIME_OUTOFBOUNDS.getErrorMessage(), entry.getDate()));
                result = CheckerReturn.INVALID;
            }
        }
    }
    
    /**
     * Checks whether all of the days are valid working days.
     * 
     * @throws CheckerException Thrown if an error occurs while fetching holidays
     */
    protected void checkValidWorkingDays() throws CheckerException {
        IHolidayChecker holidayChecker = new GermanyHolidayChecker(timeSheet.getYear(), STATE);
        for (Entry entry : timeSheet.getEntries()) {
            LocalDate localDate = entry.getDate();
            
            //Checks whether the day of the entry is Sunday
            if (localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                errors.add(new CheckerError(CheckerErrorMessage.TIME_SUNDAY.getErrorMessage(), localDate));
                result = CheckerReturn.INVALID;
                continue;
            }
            
            //Check for each entry whether it is a holiday           
            try {
                if (holidayChecker.isHoliday(localDate)) {
                    errors.add(new CheckerError(CheckerErrorMessage.TIME_HOLIDAY.getErrorMessage(), localDate));
                    result = CheckerReturn.INVALID;
                    continue;
                }
            } catch (HolidayFetchException e) {
                throw new CheckerException(e.getMessage());
            }
        }
    }
    
    /**
     * Checks whether times of different entries in the time sheet overlap.
     */
    protected void checkTimeOverlap() {
        List<Entry> entries = timeSheet.getEntries();
        if (entries.size() == 0) {
            return;
        }
        
        for (int i = 0; i < entries.size() - 1; i++) {
            if (entries.get(i).getDate().equals(entries.get(i + 1).getDate()) &&
                    entries.get(i).getEnd().compareTo(entries.get(i + 1).getStart()) > 0) {
                errors.add(new CheckerError(CheckerErrorMessage.TIME_OVERLAP.getErrorMessage(), entries.get(i).getDate()));
                result = CheckerReturn.INVALID;
            }
        }
    }
    
    /**
     * Checks whether the number of entries exceeds the maximum number of rows of the template document.
     */
    protected void checkRowNumExceedance() {
        if (timeSheet.getEntries().size() > MAX_ROW_NUM) {
            errors.add(new CheckerError(CheckerErrorMessage.ROWNUM_EXCEEDENCE.getErrorMessage()));
            result = CheckerReturn.INVALID;
        }
    }

    /**
     * Checks whether the department name is empty.
     */
    protected void checkDepartmentName() {
        if (timeSheet.getProfession().getDepartmentName().isEmpty()) {
            errors.add(new CheckerError(CheckerErrorMessage.NAME_MISSING.getErrorMessage()));
            result = CheckerReturn.INVALID;
        }
    }
    
    ////Following methods are primarily for testing purposes.
    /**
     * This method gets the result of the last call to {@link #check()}
     * @return The result of the last call to {@link #check()}
     */
    protected CheckerReturn getResult() {
        return this.result;
    }
    
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
    
    /**
     * This enum holds the possible error messages (including format specifiers) for this checker
     */
    protected enum CheckerErrorMessage {
        TIME_EXCEEDANCE("Maximum legal working time exceeded."),
        TIME_OUTOFBOUNDS("Working time is out of bounds on %tF."), // date
        TIME_SUNDAY("The %tF is a sunday, which is not a valid working day."), // date
        TIME_HOLIDAY("The %tF is an official holiday, which is not a valid working day."), // date
        TIME_PAUSE("Maximum working time without pause exceeded on %tF."), // date
        TIME_OVERLAP("Start/End times in the time sheet overlap on %tF."), // date
        
        ROWNUM_EXCEEDENCE("Exceeded the maximum number of rows for the document."),
        NAME_MISSING("Name of the departement is missing.");
        
        private final String errorMsg;
        
        private CheckerErrorMessage(String errorMsg) {
            this.errorMsg = errorMsg;
        }
        
        public String getErrorMessage() {
            return this.errorMsg;
        }
    }
    
}
