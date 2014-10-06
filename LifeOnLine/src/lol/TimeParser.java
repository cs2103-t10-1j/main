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
			if ((string.endsWith(Constants.STRING_AM) || string.endsWith(Constants.STRING_PM))) {
				// extract time without am/pm
				String hourMin = string.substring(0, string.length() - 2).trim();
				String[] splitHourMin = hourMin.split("\\.");
				
				// e.g 1 or 1.30
				if ((splitHourMin.length == 1 && isHourInRange(splitHourMin[0]))
						|| (splitHourMin.length == 2
								&& isHourInRange(splitHourMin[0]) && isMinuteInRange(splitHourMin[1]))) {
					return true;
				}
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
		if (string.length() != 4) {
			return false;
		}
		try {
			int hour = Integer.parseInt(string.substring(0, 2));
			int min = Integer.parseInt(string.substring(2));
			return hour >= 0 && hour < 24 && min >= 0 && min < 60;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Checks whether a string contains a time range in the following formats:
	 * (Space before and after 'to')
	 * 11am to 1 pm, 4pm to 6pm, 11 to 1pm, 4 to 6pm, 11am-1pm, 4pm-6pm, 11-1pm, 4-6pm
	 * @param string  string to be checked
	 * @return true if the string is a time range in any of the above formats, else false
	 */
	public boolean isTimeRange(String string) {
		if (string.contains(" to ")) {
			String[] times = string.split(" to ");
			
			// 11am to 1pm, 4pm to 6pm
			if (times.length == 2 && is12hrTime(times[0].trim()) && is12hrTime(times[1].trim())) {
				return true;
			}
			
			// 11 to 1pm, 4 to 6pm
			if (times.length == 2 && isTimeWithoutAmpm(times[0].trim()) && is12hrTime(times[1].trim())) {
				return true;
			}
		}
		
		if (string.contains("-")) {
			String[] times = string.split("-");
			
			// 11am-1pm, 4pm-6pm
			if (times.length == 2 && is12hrTime(times[0].trim()) && is12hrTime(times[1].trim())) {
				return true;
			}
			
			// 11-1pm, 4-6pm
			if (times.length == 2 && isTimeWithoutAmpm(times[0].trim()) && is12hrTime(times[1].trim())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether a string represents time in 12-hour format without the suffix am/pm, e.g. 2 or
	 * 1.20
	 * 
	 * @param string
	 *            String to be checked
	 * @return true if it represents 12-hour time without am/pm suffix, else false
	 */
	public boolean isTimeWithoutAmpm(String string) {
		string = string.trim();
		String[] splitHourMin = string.split("\\.");
		
		try {
			// e.g 1 or 1.30
			if ((splitHourMin.length == 1 && isHourInRange(splitHourMin[0]))
					|| (splitHourMin.length == 2
							&& isHourInRange(splitHourMin[0]) && isMinuteInRange(splitHourMin[1]))) {
				return true;
			}
		} catch(Exception e) {
			return false;
		}
		return false;
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
	 * Given a string that represents 12hr time, creates a Time object
	 * Precondition: valid 12 hour format is passed as a parameter
	 * @param string
	 *            time in 12-hour format, e.g 4pm or 6.20am
	 * @return Time object
	 */
	public Time create12hrTime(String string) {
		string = string.trim();

		String ampm = string.substring(string.length() - 2);
		String hourMin = string.substring(0, string.length() - 2); // 1.30 or 1
		String[] splitHourMin = hourMin.split("\\.");
			
		if (splitHourMin.length == 1) {
			return new Time(Integer.parseInt(splitHourMin[0]), ampm);
		} else if (splitHourMin.length == 2) {
			return new Time(Integer.parseInt(splitHourMin[0]),
						Integer.parseInt(splitHourMin[1]), ampm);
		}
		return null;
	}
	
	/**
	 * Given a string that represents a time range, creates a Time object of start time
	 * Precondition: valid time range format is passed as a parameter
	 * @param string
	 *            time range in the following formats: 11am to 1 pm, 4pm to 6pm, 11 to 1pm, 4 to 6pm, 11am-1pm, 4pm-6pm, 11-1pm, 4-6pm
	 * @return start time as a Time object
	 */
	public Time createStartTimeFromRange(String string) {
		string = string.trim();
		
		if (string.contains(" to ")) {
			String[] times = string.split(" to ");
			
			// 11am to 1pm, 4pm to 6pm
			if (is12hrTime(times[0].trim()) && is12hrTime(times[1].trim())) {
				return create12hrTime(times[0]);
			}
			
			// 11 to 1pm, 4 to 6pm
			if (isTimeWithoutAmpm(times[0].trim()) && is12hrTime(times[1].trim())) {
				
				String ampm = times[1].substring(times[1].length() - 2);  //suffix of end time
				
				// start time
				String[] splitHourMin = times[0].split("\\.");
				for (int i = 0; i < splitHourMin.length; i++) {
					splitHourMin[i] = splitHourMin[i].trim();
				}
					
				if (splitHourMin.length == 1) {
					if (Integer.parseInt(splitHourMin[0]) > Integer.parseInt(times[1].substring(0, times[1].length() - 2).split("\\.")[0])) {
						if (ampm.equals(Constants.STRING_AM)) {
							ampm = Constants.STRING_PM;
						}
					}
					return new Time(Integer.parseInt(splitHourMin[0]), ampm);
				} else if (splitHourMin.length == 2) {
					return new Time(Integer.parseInt(splitHourMin[0]),
								Integer.parseInt(splitHourMin[1].trim()), ampm);
				}
			}
		}
		
		if (string.contains("-")) {
			String[] times = string.split("-");
			
			// 11am-1pm, 4pm-6pm
			if (is12hrTime(times[0].trim()) && is12hrTime(times[1].trim())) {
				return create12hrTime(times[0]);
			}
			
			// 11-1pm, 4-6pm
			if (isTimeWithoutAmpm(times[0].trim()) && is12hrTime(times[1].trim())) {
				
				String ampm = times[1].substring(times[1].length() - 2);  //suffix of end time
				
				// start time
				String[] splitHourMin = times[0].split("\\.");
				for (int i = 0; i < splitHourMin.length; i++) {
					splitHourMin[i] = splitHourMin[i].trim();
				}
					
				if (splitHourMin.length == 1) {
					if (Integer.parseInt(splitHourMin[0]) > Integer.parseInt(times[1].substring(0, times[1].length() - 2).split("\\.")[0])) {
						if (ampm.equals(Constants.STRING_AM)) {
							ampm = Constants.STRING_PM;
						}
					}
					return new Time(Integer.parseInt(splitHourMin[0]), ampm);
				} else if (splitHourMin.length == 2) {
					return new Time(Integer.parseInt(splitHourMin[0]),
								Integer.parseInt(splitHourMin[1].trim()), ampm);
				}
			}
		}
		return null;
	}


}
