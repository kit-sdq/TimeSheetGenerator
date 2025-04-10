/* Licensed under MIT 2023-2024. */
package parser;

import data.Entry;
import data.TimeSheet;
import data.TimeSpan;

import java.time.YearMonth;

/**
 * A month parser provides the functionality of parsing month-related
 * {@link TimeSheet} information from given data.
 */
public interface IMonthParser {

	/**
	 * Returns a {@link YearMonth} parsed from data.
	 * 
	 * @return A YearMonth.
	 * @throws ParseException if an error occurs while parsing.
	 */
	YearMonth getYearMonth() throws ParseException;

	/**
	 * Returns an array of {@link Entry entries} parsed from data.
	 * 
	 * @return An array of entries.
	 * @throws ParseException if an error occurs while parsing.
	 */
	Entry[] getEntries() throws ParseException;

	/**
	 * Returns {@link TimeSpan} representing the transfered time from the successor
	 * month parsed from data.
	 * 
	 * @return A transfered time {@link TimeSpan}.
	 * @throws ParseException if an error occurs while parsing.
	 */
	TimeSpan getSuccTransfer() throws ParseException;

	/**
	 * Returns {@link TimeSpan} representing the transfered time from the
	 * predecessor month parsed from data.
	 * 
	 * @return A transferred time {@link TimeSpan}.
	 * @throws ParseException if an error occurs while parsing.
	 */
	TimeSpan getPredTransfer() throws ParseException;

}
