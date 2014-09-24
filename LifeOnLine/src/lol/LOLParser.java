package lol;

public class LOLParser {

	public static final String SEPARATOR = "\\";

	// Dictionaries
	public static final String[] DICTIONARY_ADD = { "add" };
	public static final String[] DICTIONARY_DELETE = { "delete" };
	public static final String[] DICTIONARY_SHOW = { "show" };
	public static final String[] DICTIONARY_EDIT = { "edit" };
	public static final String[] DICTIONARY_DONE = { "done" };
	public static final String[] DICTIONARY_EXIT = { "exit" };

	public static final String[] MONTHS_SHORT = { "jan", "feb", "mar", "apr",
			"may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" };
	public static final String[] MONTHS_LONG = { "january", "february",
			"march", "april", "may", "june", "july", "august", "september",
			"october", "november", "december" };

	public static final String[] DAYS_SHORT = { "sun", "mon", "tue", "wed",
			"thu", "fri", "sat" };
	public static final String[] DAYS_LONG = { "sunday", "monday", "tuesday",
			"wednesday", "thursday", "friday", "saturday" };
	public static final String[] DAYS_IMMEDIATE = { "today", "tomorrow", "tmw" };

	public static final String COMMAND_ADD = "add";
	public static final String COMMAND_DELETE = "delete";
	public static final String COMMAND_SHOW = "show";
	public static final String COMMAND_EDIT = "edit";
	public static final String COMMAND_DONE = "done";
	public static final String COMMAND_EXIT = "exit";
	public static final String COMMAND_INVALID = "invalid command";

	public static String getCommandName(String input) {
		String command = getFirstWord(input);

		if (hasWordInDictionary(DICTIONARY_ADD, command)) {
			return COMMAND_ADD;
		} else if (hasWordInDictionary(DICTIONARY_DELETE, command)) {
			return COMMAND_DELETE;
		} else if (hasWordInDictionary(DICTIONARY_SHOW, command)) {
			return COMMAND_SHOW;
		} else if (hasWordInDictionary(DICTIONARY_EDIT, command)) {
			return COMMAND_EDIT;
		} else if (hasWordInDictionary(DICTIONARY_DONE, command)) {
			return COMMAND_DONE;
		} else if (hasWordInDictionary(DICTIONARY_EXIT, command)) {
			return COMMAND_EXIT;
		} else {
			return COMMAND_INVALID;
		}
	}

	public static String getDescription(String input) {
		String details = removeFirstWord(input); // remove command
		return details.split(SEPARATOR)[0];
	}

	public static String getLocation(String input) {
		String details = removeFirstWord(input); // remove command
		String[] detailsArray = details.split(SEPARATOR);

		// 1st element is the description
		// If only 1 element or 2nd element is due date then no location
		if (detailsArray.length < 2 || isDate(detailsArray[1])) {
			return null;
		} else {
			return detailsArray[1];
		}
	}
	
	public static Date getDueDate(String input) {
		String details = removeFirstWord(input); // remove command
		String[] detailsArray = details.split(SEPARATOR);

		// 1st element is the description
		// Check from 2nd element onwards
		for (int i = 1; i < detailsArray.length; i++) {
			if (isDate(detailsArray[i])) {
				return createDate(detailsArray[i]);
			}
		}
		return new Date();
	}
	
	public static boolean isTime(String string) {
		// Time format 1.30pm or 1 pm
		if ((string.endsWith("am") || string.endsWith("pm"))) {
			String hourMin = string.substring(0, string.length() - 2);
			String[] splitHourMin = hourMin.split(".");
			if ((splitHourMin.length == 1 && isHourInRange(splitHourMin[0])) || (splitHourMin.length == 2 && isHourInRange(splitHourMin[0]) && isMinuteInRange(splitHourMin[1]))) {
				return true;
			}
		}
		
		// Time format 1 to 3 pm or 1.20 to 4.20pm
		if ((string.endsWith("am") || string.endsWith("pm"))) {
			String hourMin = string.substring(0, string.length() - 2);
			String[] times = hourMin.split(" ");
			if (times.length == 3) {
				String[] splitHourMinStart = times[0].split(".");
				String[] splitHourMinEnd = times[2].split(".");
				if ((splitHourMinStart.length == 1 && isHourInRange(splitHourMinStart[0])) || (splitHourMinStart.length == 2 && isHourInRange(splitHourMinStart[0]) && isMinuteInRange(splitHourMinStart[1]))) {
					if ((splitHourMinEnd.length == 1 && isHourInRange(splitHourMinEnd[0])) || (splitHourMinEnd.length == 2 && isHourInRange(splitHourMinEnd[0]) && isMinuteInRange(splitHourMinEnd[1]))) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private static boolean isHourInRange(String hourStr) {
		int hour = Integer.parseInt(hourStr);
		return (hour > 0) && (hour <= 12);
	}
	
	private static boolean isMinuteInRange(String minuteStr) {
		int minute = Integer.parseInt(minuteStr);
		return (minute >= 0) && (minute < 60);
	}
	
	private static Date createDate(String string) {
		String[] dateSlash = string.split("/");
		String[] dateSpace = string.split(" ");

		// Date format 30/9/2014 or 30/9/14
		if (dateSlash.length == 3 && isDateInRange(dateSlash[0])
				&& isMonthInRange(dateSlash[1]) && isYearInRange(dateSlash[2])) {
			return new Date(Integer.parseInt(dateSlash[0]), Integer.parseInt(dateSlash[1]), Integer.parseInt(dateSlash[2]));
		}

		// Date format 30 September 2014 or 30 Sep 2014 or 30 September 14 or 30
		// Sep 14
		if (dateSpace.length == 3
				&& isDateInRange(dateSpace[0])
				&& (hasWordInDictionary(MONTHS_SHORT, dateSpace[1]) || hasWordInDictionary(
						MONTHS_LONG, dateSpace[1]))
				&& isYearInRange(dateSpace[2])) {
			int monthNum = getMonthNum(dateSpace[1]);
			return new Date(Integer.parseInt(dateSlash[0]), monthNum , Integer.parseInt(dateSlash[2]));
		}

		// Date format 30/9
		if (dateSlash.length == 2 && isDateInRange(dateSlash[0])
				&& isMonthInRange(dateSlash[1])) {
			return new Date(Integer.parseInt(dateSlash[0]), Integer.parseInt(dateSlash[1]));
		}

		// Date format 30 September or 30 Sep
		if (dateSpace.length == 2
				&& isDateInRange(dateSpace[0])
				&& (hasWordInDictionary(MONTHS_SHORT, dateSpace[1]) || hasWordInDictionary(
						MONTHS_LONG, dateSpace[1]))) {
			int monthNum = getMonthNum(dateSpace[1]);
			return new Date(Integer.parseInt(dateSlash[0]), monthNum);
		}

		// Day of the week - Monday, Mon
		if (countWords(string) == 1
				&& (hasWordInDictionary(DAYS_SHORT, string) || hasWordInDictionary(
						DAYS_LONG, string))) {
			/* NOTE: do later */
			return new Date();
		}

		// today, tomorrow, tmw
		if (countWords(string) == 1
				&& hasWordInDictionary(DAYS_IMMEDIATE, string)) {
			/* NOTE: do later */
			return new Date();
		}
		
		return new Date();
	}

	private static int getMonthNum(String month) {
		for (int i = 0; i < MONTHS_SHORT.length; i++) {
			if (month.equalsIgnoreCase(MONTHS_SHORT[i]) || month.equalsIgnoreCase(MONTHS_LONG[i])) {
				return i + 1;
			}
		}
		return -1;
	}

	private static boolean isDate(String string) {
		String[] dateSlash = string.split("/");
		String[] dateSpace = string.split(" ");

		// Date format 30/9/2014 or 30/9/14
		if (dateSlash.length == 3 && isDateInRange(dateSlash[0])
				&& isMonthInRange(dateSlash[1]) && isYearInRange(dateSlash[2])) {
			return true;
		}

		// Date format 30 September 2014 or 30 Sep 2014 or 30 September 14 or 30
		// Sep 14
		if (dateSpace.length == 3
				&& isDateInRange(dateSpace[0])
				&& (hasWordInDictionary(MONTHS_SHORT, dateSpace[1]) || hasWordInDictionary(
						MONTHS_LONG, dateSpace[1]))
				&& isYearInRange(dateSpace[2])) {
			return true;
		}

		// Date format 30/9
		if (dateSlash.length == 2 && isDateInRange(dateSlash[0])
				&& isMonthInRange(dateSlash[1])) {
			return true;
		}

		// Date format 30 September or 30 Sep
		if (dateSpace.length == 2
				&& isDateInRange(dateSpace[0])
				&& (hasWordInDictionary(MONTHS_SHORT, dateSpace[1]) || hasWordInDictionary(
						MONTHS_LONG, dateSpace[1]))) {
			return true;
		}

		// Day of the week - Monday, Mon
		if (countWords(string) == 1
				&& (hasWordInDictionary(DAYS_SHORT, string) || hasWordInDictionary(
						DAYS_LONG, string))) {
			return true;
		}

		// today, tomorrow, tmw
		if (countWords(string) == 1
				&& hasWordInDictionary(DAYS_IMMEDIATE, string)) {
			return true;
		}
		return false;
	}

	private static boolean isDateInRange(String dateStr) {
		int date = Integer.parseInt(dateStr);
		return (date > 0) && (date <= 31);
	}

	private static boolean isMonthInRange(String monthStr) {
		int month = Integer.parseInt(monthStr);
		return (month > 0) && (month <= 12);
	}

	private static boolean isYearInRange(String yearStr) {
		int year = Integer.parseInt(yearStr);
		return ((year > 10) && (year <= 99))
				|| ((year > 2010) && (year <= 2099));
	}

	/**
	 * Remove the first word of the input and returns the resulting string
	 * 
	 * @param input
	 *            Input string
	 * @return Input string without the first word
	 */
	private static String removeFirstWord(String input) {
		if (countWords(input) <= 1) {
			return "";
		} else {
			// Get the index of the first space in the line
			int indexFirstSpace = input.indexOf(' ');
			return input.substring(indexFirstSpace + 1);
		}
	}

	/**
	 * Return the first word of the input
	 * 
	 * @param input
	 *            Input string
	 * @return First word of input
	 */
	private static String getFirstWord(String input) {
		if (countWords(input) <= 1) {
			return input;
		} else {
			// Get the index of the first space in the line
			int indexFirstSpace = input.indexOf(' ');
			return input.substring(0, indexFirstSpace);
		}
	}

	/**
	 * Return number of words in a string
	 * 
	 * @param input
	 *            Input string
	 * @return Number of words in input string
	 */
	private static int countWords(String input) {
		input = input.trim();

		if (input.isEmpty()) { // Zero words
			return 0;
		}

		int numWords = 1; // There is at least one word in the string

		for (int i = 0; i < input.length(); i++) {
			// Disregard multiple spaces between 2 words
			if (input.charAt(i) == ' ' && input.charAt(i - 1) != ' ') {
				numWords++;
			}
		}
		return numWords;
	}

	/**
	 * Check whether a word appears in a dictionary
	 * 
	 * @param dictionary
	 *            Dictionary to be searched
	 * @param word
	 *            Word to search for
	 * @return true if word appears in dictionary, false otherwise
	 */

	private static boolean hasWordInDictionary(String[] dictionary, String word) {
		for (int i = 0; i < dictionary.length; i++) {
			if (dictionary[i].equalsIgnoreCase(word)) {
				return true;
			}
		}
		return false;
	}

}
