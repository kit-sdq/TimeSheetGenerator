/* Licensed under MIT 2023-2025. */
package checker;

import checker.holiday.GermanState;
import checker.holiday.GermanyHolidayChecker;
import checker.holiday.HolidayFetchException;
import checker.holiday.IHolidayChecker;
import data.Entry;
import data.TimeSheet;
import data.TimeSpan;
import i18n.ResourceHandler;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

/**
 * The MiLoGChecker checks whether a {@link TimeSheet} instance is valid or not.
 * This checker uses rules for its checks based on the German
 * "Mindestlohngesetz" or "MiLoG".
 */
public class MiLoGChecker implements IChecker {
	// TODO Are those static constant values better than attributes?
	private static final TimeSpan WORKDAY_LOWER_BOUND = new TimeSpan(6, 0);
	private static final TimeSpan WORKDAY_UPPER_BOUND = new TimeSpan(22, 0);
	private static final TimeSpan WORKDAY_MAX_WORKING_TIME = new TimeSpan(10, 0);

	// TODO Replace with enum
	private static final TimeSpan[][] PAUSE_RULES = { { new TimeSpan(6, 0), new TimeSpan(0, 30) }, { new TimeSpan(9, 0), new TimeSpan(0, 45) } };
	private static final GermanState STATE = GermanState.BW;

	private final TimeSheet timeSheet;
	private final int maxRowNum;

	private CheckerReturn result;
	private final Collection<CheckerError> errors;

	/**
	 * Constructs a new {@link MiLoGChecker} instance. The default value for the
	 * {@link ExportType} is {@link ExportType#EXPORT_LATEX}.
	 *
	 * @param timeSheet - to be checked.
	 */
	public MiLoGChecker(TimeSheet timeSheet) {
		this(timeSheet, ExportType.EXPORT_LATEX);
	}

	/**
	 * Constructs a new {@link MiLoGChecker} instance.
	 *
	 * @param timeSheet  - to be checked.
	 * @param exportType The export type, so if this is being exported to PDF or
	 *                   Latex.
	 */
	public MiLoGChecker(TimeSheet timeSheet, ExportType exportType) {
		this.timeSheet = timeSheet;

		this.result = CheckerReturn.VALID;
		this.errors = Collections.synchronizedCollection(new ArrayList<>());
		this.maxRowNum = exportType.getMaxEntries();
	}

	/**
	 * Runs all of the needed tests in order to validate the {@link TimeSheet}
	 * instance.
	 *
	 * @return {@link CheckerReturn} value with error or validity message
	 * @throws CheckerException Thrown if an error occurs while checking the
	 *                          validity
	 */
	@Override
	public CheckerReturn check() throws CheckerException {
		result = CheckerReturn.VALID;
		errors.clear();

		checkTotalTimeExceedance();
		checkDayTimeExceedance();
		checkDayPauseTime();
		checkDayTimeBounds();
		checkValidWorkingDays();
		checkTimeOverlap();

		checkRowNumExceedance();
		checkDepartmentName();

		return result;
	}

	/**
	 * Returns a collection of all occurred checker errors during the execution of
	 * the last call to {@link #check()}
	 *
	 * @return {@link Collection} of occurred checker errors.
	 */
	@Override
	public Collection<CheckerError> getErrors() {
		return new ArrayList<>(errors);
	}

	/**
	 * Checks whether total maximum working time was exceeded.
	 */
	protected void checkTotalTimeExceedance() {
		// Legal maximum working time per month
		TimeSpan maxWorkingTime = timeSheet.getProfession().getMaxWorkingTime();

		// Sum of all daily working times (without pauses!)
		TimeSpan totalWorkingTime = timeSheet.getTotalWorkTime();

		// Vacation and transfer corrected time
		TimeSpan correctedMaxWorkingTime = maxWorkingTime.add(timeSheet.getSuccTransfer()).subtract(timeSheet.getPredTransfer())
				.subtract(timeSheet.getTotalVacationTime());

		if (totalWorkingTime.compareTo(correctedMaxWorkingTime) > 0) {
			// Calculate difference
			TimeSpan difference = totalWorkingTime.subtract(correctedMaxWorkingTime);

			errors.add(new CheckerError(MiLoGCheckerErrorMessageProvider.TOTAL_TIME_EXCEEDANCE, maxWorkingTime, difference));
			result = CheckerReturn.INVALID;
		}
	}

	/**
	 * Checks whether daily maximum working time was exceeded.
	 */
	protected void checkDayTimeExceedance() {
		// This map contains all working days and their summed up working times.
		Map<LocalDate, TimeSpan> workingTimeMap = new HashMap<>();
		for (Entry entry : timeSheet.getEntries()) {
			if (!entry.isVacation()) {
				// A check is performed whether a day contains more than one entry.
				if (workingTimeMap.containsKey(entry.getDate())) {
					// If so, the working times are summed up and written back to the map.
					TimeSpan summedTime = workingTimeMap.get(entry.getDate()).add(entry.getWorkingTime());
					workingTimeMap.put(entry.getDate(), summedTime);
				} else {
					// If not, a new entry will be created in the map.
					workingTimeMap.put(entry.getDate(), entry.getWorkingTime());
				}
			}
		}

		for (Map.Entry<LocalDate, TimeSpan> mapEntry : workingTimeMap.entrySet()) {
			if (WORKDAY_MAX_WORKING_TIME.compareTo(mapEntry.getValue()) < 0) {
				errors.add(new CheckerError(MiLoGCheckerErrorMessageProvider.DAY_TIME_EXCEEDANCE, WORKDAY_MAX_WORKING_TIME, mapEntry.getKey()));
				result = CheckerReturn.INVALID;
			}
		}
	}

	/**
	 * Checks whether the working time per day meets all legal pause rules.
	 */
	protected void checkDayPauseTime() {
		// This map contains all dates associated with their working times
		HashMap<LocalDate, TimeSpan[]> workingDays = new HashMap<>();

		for (Entry entry : timeSheet.getEntries()) {
			if (!entry.isVacation()) {
				// EndToStart is the number of hours at this work shift.
				TimeSpan endToStart = entry.getEnd();
				endToStart = endToStart.subtract(entry.getStart());
				TimeSpan pause = entry.getPause();
				LocalDate date = entry.getDate();

				// If a day appears more than once this logic sums all of the working times
				if (workingDays.containsKey(date)) {
					TimeSpan oldWorkingTime = workingDays.get(date)[0];
					endToStart = endToStart.add(oldWorkingTime);

					TimeSpan oldPause = workingDays.get(date)[1];
					pause = pause.add(oldPause);
				}

				// Adds the day to the map, replaces the old entry respectively
				TimeSpan[] mapTimeEntry = { endToStart, pause };
				workingDays.put(date, mapTimeEntry);
			}
		}

		// Check for every mapTimeEntry (first for), whether all Pause Rules (second
		// for) where met.
		for (Map.Entry<LocalDate, TimeSpan[]> mapEntry : workingDays.entrySet()) {
			for (TimeSpan[] pauseRule : PAUSE_RULES) {

				// Checks whether time of entry is greater than pause rule "activation"
				// time
				// and pause time is less than the needed time.
				if (mapEntry.getValue()[0].compareTo(pauseRule[0]) > 0 && mapEntry.getValue()[1].compareTo(pauseRule[1]) < 0) {
					errors.add(new CheckerError(MiLoGCheckerErrorMessageProvider.TIME_PAUSE, mapEntry.getKey()));
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
			if (entry.getStart().compareTo(WORKDAY_LOWER_BOUND) < 0 || entry.getEnd().compareTo(WORKDAY_UPPER_BOUND) > 0) {

				errors.add(new CheckerError(MiLoGCheckerErrorMessageProvider.TIME_OUTOFBOUNDS, entry.getDate()));
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

			// Checks whether the day of the entry is Sunday
			if (localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				errors.add(new CheckerError(MiLoGCheckerErrorMessageProvider.TIME_SUNDAY, localDate));
				result = CheckerReturn.INVALID;
				continue;
			}

			// Check for each entry whether it is a holiday
			try {
				if (holidayChecker.isHoliday(localDate)) {
					errors.add(new CheckerError(MiLoGCheckerErrorMessageProvider.TIME_HOLIDAY, localDate));
					result = CheckerReturn.INVALID;
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
		if (entries.isEmpty()) {
			return;
		}

		for (int i = 0; i < entries.size() - 1; i++) {
			if (entries.get(i).getDate().equals(entries.get(i + 1).getDate()) && entries.get(i).getEnd().compareTo(entries.get(i + 1).getStart()) > 0) {
				errors.add(new CheckerError(MiLoGCheckerErrorMessageProvider.TIME_OVERLAP, entries.get(i).getDate()));
				result = CheckerReturn.INVALID;
			}
		}
	}

	/**
	 * Checks whether the number of entries exceeds the maximum number of rows of
	 * the template document.
	 */
	protected void checkRowNumExceedance() {
		int rowCount = timeSheet.getEntries().size();
		if (rowCount > maxRowNum) {
			errors.add(new CheckerError(MiLoGCheckerErrorMessageProvider.ROWNUM_EXCEEDENCE, maxRowNum, rowCount - maxRowNum));
			result = CheckerReturn.INVALID;
		}
	}

	/**
	 * Checks whether the department name is empty.
	 */
	protected void checkDepartmentName() {
		if (timeSheet.getProfession().getDepartmentName().isEmpty()) {
			errors.add(new CheckerError(MiLoGCheckerErrorMessageProvider.NAME_MISSING));
			result = CheckerReturn.INVALID;
		}
	}

	//// Following methods are primarily for testing purposes.
	/**
	 * This method gets the result of the last call to {@link #check()}
	 *
	 * @return The result of the last call to {@link #check()}
	 */
	protected CheckerReturn getResult() {
		return this.result;
	}

	/**
	 * This method gets the maximally allowed number of entries inside a
	 * {@link TimeSheet}. This method is used for the latex template, as the
	 * template is different from the PDF template.
	 *
	 * @return The maximum number of {@link Entry entries} when using the latex
	 *         template.
	 */
	protected static int getMaxEntriesLatex() {
		return ExportType.EXPORT_LATEX.getMaxEntries();
	}

	/**
	 * This method gets the maximally allowed number of entries inside a
	 * {@link TimeSheet}. This method is used for the exported PDF, as the template
	 * is different from the Latex template.
	 *
	 * @return The maximum number of {@link Entry entries} when using the pdf
	 *         template.
	 */
	protected static int getMaxEntriesPDF() {
		return ExportType.EXPORT_PDF.getMaxEntries();
	}

	/**
	 * This method gets the legal lower bound of time to start a working day.
	 *
	 * @return The legal lower bound of time to start a working day.
	 */
	protected static TimeSpan getWorkdayLowerBound() {
		return WORKDAY_LOWER_BOUND;
	}

	/**
	 * This method gets the legal upper bound of time to end a working day.
	 *
	 * @return The legal upper bound of time to end a working day.
	 */
	protected static TimeSpan getWorkdayUpperBound() {
		return WORKDAY_UPPER_BOUND;
	}

	/**
	 * This method gets the legal daily maximum working time to conform to laws.
	 *
	 * @return The daily maximum working time.
	 */
	protected static TimeSpan getWorkdayMaxWorkingTime() {
		return WORKDAY_MAX_WORKING_TIME;
	}

	/**
	 * This method gets the legal pause rules to conform to laws.
	 *
	 * @return The legal pause rules.
	 */
	protected static TimeSpan[][] getPauseRules() {
		return PAUSE_RULES;
	}

	/**
	 * This enum holds the possible error messages (including format specifiers) for
	 * this checker
	 */
	protected enum MiLoGCheckerErrorMessageProvider implements CheckerError.CheckerErrorMessageProvider {
		TOTAL_TIME_EXCEEDANCE("totalTimeExceedance"), DAY_TIME_EXCEEDANCE("dayTimeExceedance"), TIME_OUTOFBOUNDS("timeOutOfBounds"), TIME_SUNDAY("timeSunday"),
		TIME_HOLIDAY("timeHoliday"), TIME_PAUSE("timePause"), TIME_OVERLAP("timeOverlap"),

		ROWNUM_EXCEEDENCE("rowNumExceedance"), NAME_MISSING("nameMissing");

		private static final String messageKeyPrefix = "error.checker.";

		MiLoGCheckerErrorMessageProvider(String messageKey) {
			this.messageKey = messageKey;
		}

		private final String messageKey;

		@Override
		public String getErrorMessage(Object... args) {
			String key = messageKeyPrefix + messageKey;

			return ResourceHandler.getMessage(key, args);
		}
	}

}
