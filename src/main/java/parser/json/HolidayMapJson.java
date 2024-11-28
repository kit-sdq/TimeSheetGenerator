/* Licensed under MIT 2023-2024. */
package parser.json;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

class HolidayMapJson {

	private final Map<String, HolidayJson> holidays;

	HolidayMapJson() {
		this.holidays = new HashMap<>();
	}

	@JsonAnySetter
	public void addHoliday(String key, HolidayJson value) {
		holidays.put(key, value);
	}

	public Map<String, HolidayJson> getHolidays() {
		return holidays;
	}

}
