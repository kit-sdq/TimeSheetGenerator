/* Licensed under MIT 2023-2024. */
package parser;

import checker.holiday.Holiday;

import java.util.Collection;

/**
 * A holiday parser provides the functionality of parsing {@link Holiday
 * holidays} from given data.
 */
public interface IHolidayParser {

	/**
	 * Returns a {@link Collection} of {@link Holiday holidays} parsed from data.
	 * 
	 * @return A collection of holidays.
	 * @throws ParseException if an error occurs while parsing.
	 */
	Collection<Holiday> getHolidays() throws ParseException;

}
