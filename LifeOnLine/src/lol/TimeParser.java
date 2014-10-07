/**
 * This class parses a string containing a time or time range.
 * The following formats of time are allowed: (not case-sensitive)
 * 
 * 12-hour format:
 * 2pm
 * 2.30pm
 * 
 * 24-hour format:
 * 0930
 * 
 * Time range (Space before and after 'to'):
 * 11am to 1 pm
 * 4pm to 6pm
 * 11 to 1pm
 * 4 to 6pm
 * 11am-1pm
 * 4pm-6pm
 * 11-1pm
 * 4-6pm
 * 
 */
package lol;

public class TimeParser {
	/**
	 * Checks whether a string represents time in 12-hour format, e.g. 2pm or
	 * 1.20am
	 * 
	 * @param string
	 *            String to be checked
	 * @return true if it represents 12-hour time, else false
	 */
	public boolean is12hrTime(String string) {
		string = string.trim();
		try {
			// Time format 1.30pm or 1 pm
			if ((string.endsWith(Constants.STRING_AM) || string
					.endsWith(Constants.STRING_PM))) {
				// extract time without am/pm
				String hourMin = string.substring(Constants.INDEX_BEGIN,
						string.length() - Constants.LENGTH_AM_PM).trim();
				return isTimeWithoutAmpm(hourMin);
			}
			return false;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * Checks whether a string represents time in 24-hour format, e.g. 2359 or
	 * 1731
	 * 
	 * @param string
	 *            String to be checked
	 * @return true if it represents 24-hour time, else false
	 */
	public boolean is24hrTime(String string) {
		string = string.trim();
		if (string.length() != Constants.LENGTH_24HOUR_FORMAT) {
			return false;
		}
		try {
			int hour = Integer.parseInt(string.substring(Constants.INDEX_BEGIN,
					Constants.LENGTH_AM_PM));
			int min = Integer
					.parseInt(string.substring(Constants.LENGTH_AM_PM));
			return is24hrFormatInRange(hour, min);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Checks whether a string contains a time range in the following formats:
	 * (Space before and after 'to') 11am to 1 pm, 4pm to 6pm, 11 to 1pm, 4 to
	 * 6pm, 11am-1pm, 4pm-6pm, 11-1pm, 4-6pm
	 * 
	 * @param string
	 *            string to be checked
	 * @return true if the string is a time range in any of the above formats,
	 *         else false
	 */
	public boolean isTimeRange(String string) {
		boolean containsSeparator = false;
		String[] times = {};

		try {
			if (string.contains(Constants.SEPARATOR_TO)) {
				times = string.split(Constants.SEPARATOR_TO);
				containsSeparator = true;
			} else if (string.contains(Constants.SEPARATOR_DASH)) {
				times = string.split(Constants.SEPARATOR_DASH);
				containsSeparator = true;
			}

			if (containsSeparator) {
				// 11am to 1pm, 4pm to 6pm, 11 to 1pm, 4 to 6pm
				if (times.length == Constants.LENGTH_TIME_RANGE) {
					return (is12hrTime(times[Constants.INDEX_START_TIME].trim()) && is12hrTime(times[Constants.INDEX_END_TIME]
							.trim()))
							|| (isTimeWithoutAmpm(times[Constants.INDEX_START_TIME]
									.trim()) && is12hrTime(times[Constants.INDEX_END_TIME]
									.trim()));
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Checks whether a string represents time in 12-hour format without the
	 * suffix am/pm, e.g. 2 or 1.20
	 * 
	 * @param string
	 *            String to be checked
	 * @return true if it represents 12-hour time without am/pm suffix, else
	 *         false
	 */
	public boolean isTimeWithoutAmpm(String string) {
		string = string.trim();
		String[] splitHourMin = string.split(Constants.SEPARATOR_DOT);

		try {
			// e.g 1 or 1.30
			return (splitHourMin.length == Constants.LENGTH_HOUR && isHourInRange(splitHourMin[Constants.INDEX_HOUR]))
					|| (splitHourMin.length == Constants.LENGTH_HOUR_MINUTE
							&& isHourInRange(splitHourMin[Constants.INDEX_HOUR]) && isMinuteInRange(splitHourMin[Constants.INDEX_MINUTE]));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Checks whether hour is between 1 and 12 (both inclusive)
	 * 
	 * @param hourStr
	 *            String to be checked
	 * @return true if hour is between 1 and 12 (both inclusive), else false
	 */
	public boolean isHourInRange(String hourStr) {
		int hour = Integer.parseInt(hourStr.trim());
		return (hour > 0) && (hour <= 12);
	}

	/**
	 * Checks if minute is between 0 and 59 (both inclusive)
	 * 
	 * @param minuteStr
	 *            String to be checked
	 * @return true if minute is between 0 and 59 (both inclusive), else false
	 */
	public boolean isMinuteInRange(String minuteStr) {
		int minute = Integer.parseInt(minuteStr.trim());
		return (minute >= 0) && (minute < 60);
	}

	/**
	 * Checks if hour and minute are inside the 24hr format range i.e. hour is
	 * between 0 and 23 (inclusive) and minute is between 0 and 59 (inclusive)
	 * 
	 * @param hour
	 *            Hour
	 * @param min
	 *            Minute
	 * @return true if hour is between 0 and 23 (inclusive) and minute is
	 *         between 0 and 59 (inclusive), else false
	 */
	public boolean is24hrFormatInRange(int hour, int min) {
		return hour >= 0 && hour < 24 && min >= 0 && min < 60;
	}

	/**
	 * Given a string that represents 12hr time, creates a Time object
	 * Precondition: valid 12 hour format is passed as a parameter
	 * 
	 * @param string
	 *            time in 12-hour format, e.g 4pm or 6.20am
	 * @return Time object
	 */
	public Time create12hrTime(String string) {
		string = string.trim();
		if (!is12hrTime(string)) {
			return null;
		}

		try {
			String ampm = string.substring(string.length()
					- Constants.LENGTH_AM_PM);
			String hourMin = string.substring(Constants.INDEX_BEGIN,
					string.length() - Constants.LENGTH_AM_PM).trim(); // 1.30 or
																		// 1
			String[] splitHourMin = hourMin.split(Constants.SEPARATOR_DOT);

			if (splitHourMin.length == Constants.LENGTH_HOUR) {
				return new Time(
						Integer.parseInt(splitHourMin[Constants.INDEX_HOUR]),
						ampm);
			} else if (splitHourMin.length == Constants.LENGTH_HOUR_MINUTE) {
				return new Time(
						Integer.parseInt(splitHourMin[Constants.INDEX_HOUR]),
						Integer.parseInt(splitHourMin[Constants.INDEX_MINUTE]),
						ampm);
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	/**
	 * Given a string that represents a time range, creates a Time object of
	 * start time
	 * 
	 * @param string
	 *            time range in the following formats: 11am to 1 pm, 4pm to 6pm,
	 *            11 to 1pm, 4 to 6pm, 11am-1pm, 4pm-6pm, 11-1pm, 4-6pm
	 * @return start time as a Time object, null if invalid format
	 */
	public Time createStartTimeFromRange(String string) {
		string = string.trim();
		String[] times = {};

		try {
			if (!isTimeRange(string)) {
				return null;
			}
			if (string.contains(Constants.SEPARATOR_TO)) {
				times = string.split(Constants.SEPARATOR_TO);
			} else if (string.contains(Constants.SEPARATOR_DASH)) {
				times = string.split(Constants.SEPARATOR_DASH);
			} else {
				return null;
			}

			// 11am to 1pm, 4pm to 6pm
			if (is12hrTime(times[Constants.INDEX_START_TIME].trim())
					&& is12hrTime(times[Constants.INDEX_END_TIME].trim())) {
				return create12hrTime(times[Constants.INDEX_START_TIME]);
			}

			// 11 to 1pm, 4 to 6pm
			if (isTimeWithoutAmpm(times[Constants.INDEX_START_TIME].trim())
					&& is12hrTime(times[Constants.INDEX_END_TIME].trim())) {

				times[Constants.INDEX_END_TIME] = times[Constants.INDEX_END_TIME]
						.trim();
				String ampm = times[Constants.INDEX_END_TIME].substring(
						times[Constants.INDEX_END_TIME].length()
								- Constants.LENGTH_AM_PM).trim(); // suffix of
																	// end time

				String startTimeWithoutAmpm = times[Constants.INDEX_START_TIME]
						.trim();
				times[Constants.INDEX_END_TIME] = times[Constants.INDEX_END_TIME]
						.trim();
				String endTimeWithoutAmpm = times[Constants.INDEX_END_TIME]
						.substring(0, times[Constants.INDEX_END_TIME].length()
								- Constants.LENGTH_AM_PM);

				if (Float.parseFloat(startTimeWithoutAmpm) >= Float
						.parseFloat(endTimeWithoutAmpm)) {
					ampm = toggleAmPm(ampm);
				}
				return create12hrTime(startTimeWithoutAmpm + ampm);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Given a string that represents a time range, creates a Time object of end
	 * time
	 * 
	 * @param string
	 *            time range in the following formats: 11am to 1 pm, 4pm to 6pm,
	 *            11 to 1pm, 4 to 6pm, 11am-1pm, 4pm-6pm, 11-1pm, 4-6pm
	 * @return end time as a Time object, null if invalid format
	 */
	public Time createEndTimeFromRange(String string) {
		string = string.trim();
		String[] times = {};

		try {
			if (!isTimeRange(string)) {
				return null;
			}
			if (string.contains(Constants.SEPARATOR_TO)) {
				times = string.split(Constants.SEPARATOR_TO);
			} else if (string.contains(Constants.SEPARATOR_DASH)) {
				times = string.split(Constants.SEPARATOR_DASH);
			} else {
				return null;
			}
			return create12hrTime(times[Constants.INDEX_END_TIME]);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns the opposite am/pm value of the parameter. If input is "am", it
	 * returns "pm" and vice-versa.
	 * 
	 * @param ampm
	 *            "am" or "pm"
	 * @return opposite value
	 */
	public String toggleAmPm(String ampm) {
		if (ampm.equalsIgnoreCase(Constants.STRING_AM)) {
			return Constants.STRING_PM;
		} else {
			return Constants.STRING_AM;
		}
	}
}
