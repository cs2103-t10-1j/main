/**
 * NOT DONE YET
 * Parses the user input
 * User input formats:
 * 
 * add <description>\<location>\<due date>
 * start and end time not implemented yet
 */
package lol;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LOLParser {

	// Separator
	public static final String SEPARATOR = "\\\\";

	// Dictionaries
	public static final String[] DICTIONARY_ADD = { "add" };
	public static final String[] DICTIONARY_DELETE = { "delete" };
	public static final String[] DICTIONARY_SHOW = { "show" };
	public static final String[] DICTIONARY_EDIT = { "edit" };
	public static final String[] DICTIONARY_DONE = { "done" };
	public static final String[] DICTIONARY_EXIT = { "exit" };

	// Months
	public static final String[] MONTHS_SHORT = { "jan", "feb", "mar", "apr",
			"may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" };
	public static final String[] MONTHS_LONG = { "january", "february",
			"march", "april", "may", "june", "july", "august", "september",
			"october", "november", "december" };

	// Days of the week
	public static final String[] DAYS_SHORT = { "sun", "mon", "tue", "wed",
			"thu", "fri", "sat" };
	public static final String[] DAYS_LONG = { "sunday", "monday", "tuesday",
			"wednesday", "thursday", "friday", "saturday" };
	public static final String[] DAYS_IMMEDIATE = { "today", "tomorrow", "tmw" };

	// Commands
	public static final String COMMAND_ADD = "add";
	public static final String COMMAND_DELETE = "delete";
	public static final String COMMAND_SHOW = "show";
	public static final String COMMAND_EDIT = "edit";
	public static final String COMMAND_DONE = "done";
	public static final String COMMAND_EXIT = "exit";
	public static final String COMMAND_INVALID = "invalid command";

	// Array indices
	public static final int INDEX_DAY = 0;
	public static final int INDEX_TODAY = 0;
	public static final int INDEX_MONTH = 1;
	public static final int INDEX_YEAR = 2;

	// Array lengths
	public static final int LENGTH_DAY_MONTH_YEAR = 3;
	public static final int LENGTH_DAY_MONTH = 2;

	/*********** Methods to return task details ***************/

	/**
	 * Returns the command name. 
	 * Example: add, delete, show, etc.
	 * 
	 * @param input
	 *            user input
	 * @return command name
	 */
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

	/**
	 * Returns description of task for add command. Example: for input "add attend meeting \
	 * meeting room 3 \ 16 Oct \ 2pm" returns "attend meeting". Precondition: Task
	 * has a description
	 * 
	 * @param input
	 *            user input
	 * @return task description
	 */
	public static String getDescription(String input) {
		String details = removeFirstWord(input); // remove command
		return details.split(SEPARATOR)[0];
		// return details;
	}

	/**
	 * Returns location of task for add command. Example: for input " add attend meeting \
	 * meeting room 3 \ 16 Oct \ 2pm" returns "meeting room 3"
	 * 
	 * @param input
	 *            user input
	 * @return location of task if it exists, else null
	 */
	public static String getLocation(String input) {
		String details = removeFirstWord(input); // remove command
		String[] detailsArray = details.split(SEPARATOR);

		// 1st element is the description
		// If only 1 element or 2nd element is due date then no location
		if (detailsArray.length < 2 || isValidDate(detailsArray[1])
				|| isValidDay(detailsArray[1])) {
			return null;
		} else {
			return detailsArray[1];
		}
	}

	/**
	 * Returns due date of task as a date object for add command.
	 * 
	 * @param input
	 *            user input
	 * @return due date if it exists, else null
	 */
	public static Date getDueDate(String input) {
		String details = removeFirstWord(input); // remove command
		String[] detailsArray = details.split(SEPARATOR);

		// 1st element is the description
		// Check from 2nd element onwards
		for (int i = 1; i < detailsArray.length; i++) {
			if (isValidDate(detailsArray[i]) || isValidDay(detailsArray[i])) {
				return createDate(detailsArray[i]);
			}
		}
		return null;
	}

	/**
	 * Returns start time of task as a time object for add command.
	 * 
	 * @param input
	 *            user input
	 * @return start time if it exists, else null
	 */
	public static Time getStartTime(String input) {
		String details = removeFirstWord(input); // remove command
		String[] detailsArray = details.split(SEPARATOR);

		// 1st element is the description
		// Check from 2nd element onwards
		for (int i = 1; i < detailsArray.length; i++) {
			if (isTime(detailsArray[i])) {
				return createTime(detailsArray[i]);
			}
		}
		return null;
	}

	/**
	 * Returns a Task object with details given in the parameter for add command
	 * 
	 * @param input
	 *            user input
	 * @return Task added
	 */
	public static Task getTask(String input) {
		return new Task(getDescription(input), getLocation(input),
				getDueDate(input));
	}
	
	/**
	 * Returns index of task for delete/edit/done commands, counting from 1. 
	 * Example: 'delete 1' returns 1. 
	 * If index is not an integer or no the input does not contain an index, -1 is returned.
	 * @param input   user input
	 * @return  index of task if it is in the input and is an integer, else -1
	 */
	public static int getTaskIndex(String input) {
		try {
			return Integer.parseInt(input.split(" ")[1]);
		} catch (Exception e) {
			return -1;
		}
	}
	
	/**
	 * Returns description of task for edit command. Precondition: Task
	 * has a description
	 * 
	 * @param input
	 *            user input
	 * @return task description
	 */
	public static String getEditDescription(String input) {
		return getDescription(removeFirstWord(input));
	}
	
	/**
	 * Returns location of task for edit command
	 * 
	 * @param input
	 *            user input
	 * @return location of task if it exists, else null
	 */
	public static String getEditLocation(String input) {
		return getLocation(removeFirstWord(input));
	}
	
	/**
	 * Returns due date of task as a date object for edit command.
	 * 
	 * @param input
	 *            user input
	 * @return due date if it exists, else null
	 */
	public static Date getEditDueDate(String input) {
		return getDueDate(removeFirstWord(input));
	}
	
	/**
	 * Returns a Task object with details given in the parameter for edit command
	 * 
	 * @param input
	 *            user input
	 * @return Task added
	 */
	public static Task getEditTask(String input) {
		return getTask(removeFirstWord(input));
	}

	/************* Start/end time methods *****************/

	/**
	 * Given a string that represents time, creates a Time object
	 * 
	 * @param string
	 *            time in 12-hour format, e.g 4pm or 6.20am
	 * @return Time object
	 */
	private static Time createTime(String string) {
		string = string.trim();

		// Time format 1.30pm or 1 pm
		if ((string.endsWith("am") || string.endsWith("pm"))) {
			String ampm = string.substring(string.length() - 2);
			String hourMin = string.substring(0, string.length() - 2); // 1.30
																		// or 1
			String[] splitHourMin = hourMin.split(".");
			if (splitHourMin.length == 1 && isHourInRange(splitHourMin[0])) {
				return new Time(Integer.parseInt(splitHourMin[0]), ampm);
			}
			if (splitHourMin.length == 2 && isHourInRange(splitHourMin[0])
					&& isMinuteInRange(splitHourMin[1])) {
				return new Time(Integer.parseInt(splitHourMin[0]),
						Integer.parseInt(splitHourMin[1]), ampm);
			}
		}
		/*
		 * // Time format 1 to 3 pm or 1.20 to 4.20pm if ((string.endsWith("am")
		 * || string.endsWith("pm"))) { String hourMin = string.substring(0,
		 * string.length() - 2); String[] times = hourMin.split(" "); if
		 * (times.length == 3) { String[] splitHourMinStart =
		 * times[0].split("."); String[] splitHourMinEnd = times[2].split(".");
		 * if ((splitHourMinStart.length == 1 &&
		 * isHourInRange(splitHourMinStart[0])) || (splitHourMinStart.length ==
		 * 2 && isHourInRange(splitHourMinStart[0]) &&
		 * isMinuteInRange(splitHourMinStart[1]))) { if ((splitHourMinEnd.length
		 * == 1 && isHourInRange(splitHourMinEnd[0])) || (splitHourMinEnd.length
		 * == 2 && isHourInRange(splitHourMinEnd[0]) &&
		 * isMinuteInRange(splitHourMinEnd[1]))) { return true; } } }
		 * 
		 * }
		 */
		return null;
	}

	/**
	 * Checks whether a string represents time in 12-hour format, e.g. 2pm or
	 * 1.20am
	 * 
	 * @param string
	 *            String to be checked
	 * @return true if it represents 12-hour time, else false
	 */
	private static boolean isTime(String string) {
		string = string.trim();

		// Time format 1.30pm or 1 pm
		if ((string.endsWith("am") || string.endsWith("pm"))) {
			String hourMin = string.substring(0, string.length() - 2);
			String[] splitHourMin = hourMin.split(".");
			if ((splitHourMin.length == 1 && isHourInRange(splitHourMin[0]))
					|| (splitHourMin.length == 2
							&& isHourInRange(splitHourMin[0]) && isMinuteInRange(splitHourMin[1]))) {
				return true;
			}
		}
		/*
		 * // Time format 1 to 3 pm or 1.20 to 4.20pm if ((string.endsWith("am")
		 * || string.endsWith("pm"))) { String hourMin = string.substring(0,
		 * string.length() - 2); String[] times = hourMin.split(" "); if
		 * (times.length == 3) { String[] splitHourMinStart =
		 * times[0].split("."); String[] splitHourMinEnd = times[2].split(".");
		 * if ((splitHourMinStart.length == 1 &&
		 * isHourInRange(splitHourMinStart[0])) || (splitHourMinStart.length ==
		 * 2 && isHourInRange(splitHourMinStart[0]) &&
		 * isMinuteInRange(splitHourMinStart[1]))) { if ((splitHourMinEnd.length
		 * == 1 && isHourInRange(splitHourMinEnd[0])) || (splitHourMinEnd.length
		 * == 2 && isHourInRange(splitHourMinEnd[0]) &&
		 * isMinuteInRange(splitHourMinEnd[1]))) { return true; } } } }
		 */
		return false;
	}

	/**
	 * Checks whether hour is between 1 and 12 (both inclusive)
	 * 
	 * @param hourStr
	 *            String to be checked
	 * @return true if hour is between 1 and 12 (both inclusive), else false
	 */
	private static boolean isHourInRange(String hourStr) {
		int hour = Integer.parseInt(hourStr);
		return (hour > 0) && (hour <= 12);
	}

	/**
	 * Checks if minute is between 0 and 59 (both inclusive)
	 * 
	 * @param minuteStr
	 *            String to be checked
	 * @return true if minute is between 0 and 59 (both inclusive), else false
	 */
	private static boolean isMinuteInRange(String minuteStr) {
		int minute = Integer.parseInt(minuteStr);
		return (minute >= 0) && (minute < 60);
	}

	/**************** Due date methods ********************/

	/**
	 * Given a string that represents date, creates a Date object
	 * 
	 * @param string
	 *            String representing date in any the following formats (not
	 *            case-sensitive): 30/9/2014 (or) 30/9/14 (or) 30 September 2014
	 *            (or) 30 Sep 2014 (or) 30 September 14 (or) 30 Sep 14 (or) 30/9
	 *            (or) 30 September (or) 30 Sep (or) Day of the week - Monday,
	 *            Mon, Sunday, Sun, etc. (or) today (or) tomorrow (or) tmw
	 * @return Date object
	 */
	public static Date createDate(String string) {
		String[] dateSlash = string.split("/"); // separated by forward-slash
		String[] dateSpace = string.split(" "); // separated by space

		// Date format 30/9/2014 or 30/9/14
		// if the date has 3 parts and day, month and year are within range
		if (dateSlash.length == LENGTH_DAY_MONTH_YEAR
				&& isDateInRange(dateSlash[INDEX_DAY])
				&& isMonthInRange(dateSlash[INDEX_MONTH])
				&& isYearInRange(dateSlash[INDEX_YEAR])) {
			return new Date(Integer.parseInt(dateSlash[INDEX_DAY]),
					Integer.parseInt(dateSlash[INDEX_MONTH]),
					Integer.parseInt(dateSlash[INDEX_YEAR]));
		}

		// Date format 30 September 2014 or 30 Sep 2014 or 30 September 14 or 30
		// Sep 14
		// if the date has 3 parts, day and year are within range and month name
		// is valid
		if (dateSpace.length == LENGTH_DAY_MONTH_YEAR
				&& isDateInRange(dateSpace[INDEX_DAY])
				&& (hasWordInDictionary(MONTHS_SHORT, dateSpace[INDEX_MONTH]) || hasWordInDictionary(
						MONTHS_LONG, dateSpace[INDEX_MONTH]))
				&& isYearInRange(dateSpace[INDEX_YEAR])) {

			// get number of month e.g 1 for jan
			int monthNum = getMonthNum(dateSpace[INDEX_MONTH]);
			return new Date(Integer.parseInt(dateSpace[INDEX_DAY]), monthNum,
					Integer.parseInt(dateSpace[INDEX_YEAR]));
		}

		// Date format 30/9
		// if the date has 2 parts and day and month are within range
		if (dateSlash.length == LENGTH_DAY_MONTH
				&& isDateInRange(dateSlash[INDEX_DAY])
				&& isMonthInRange(dateSlash[INDEX_MONTH])) {
			return new Date(Integer.parseInt(dateSlash[INDEX_DAY]),
					Integer.parseInt(dateSlash[INDEX_MONTH]));
		}

		// Date format 30 September or 30 Sep
		// if the date has 2 parts, day is within range and month name is valid
		if (dateSpace.length == LENGTH_DAY_MONTH
				&& isDateInRange(dateSpace[INDEX_DAY])
				&& (hasWordInDictionary(MONTHS_SHORT, dateSpace[INDEX_MONTH]) || hasWordInDictionary(
						MONTHS_LONG, dateSpace[INDEX_MONTH]))) {

			// get number of month e.g 1 for jan
			int monthNum = getMonthNum(dateSpace[INDEX_MONTH]);
			return new Date(Integer.parseInt(dateSpace[INDEX_DAY]), monthNum);
		}

		// Day of the week - Monday, Mon
		// if string has 1 word and day of the week is valid
		if (countWords(string) == 1
				&& (hasWordInDictionary(DAYS_SHORT, string) || hasWordInDictionary(
						DAYS_LONG, string))) {

			// get index of today's day and due date, 0 - sun, 1 - mon and so on
			int todaysDayOfTheWeekIndex = getTodaysDayOfTheWeekIndex(); // 0-6
			int dueDatesDayOfTheWeekIndex = getDayOfTheWeekIndex(string); // 0-6

			// how many days later is the due date from today, e.g 1 if due date
			// is tomorrow
			int numDaysLater = dueDatesDayOfTheWeekIndex
					- todaysDayOfTheWeekIndex;

			// if today's index > due date's index, due date is next week
			// E.g. If today is Friday, Mon refers to next Monday
			if (numDaysLater < 1) {
				numDaysLater += 7;
			}
			return addDaysToToday(numDaysLater);
		}

		// today, tomorrow, tmw
		if (countWords(string) == 1
				&& hasWordInDictionary(DAYS_IMMEDIATE, string)) {
			if (string.equalsIgnoreCase(DAYS_IMMEDIATE[INDEX_TODAY])) { // today
				return getTodaysDate();
			} else { // tomorrow
				return addDaysToToday(1);
			}
		}

		return new Date(); // Date does not match any of the above formats
	}

	/**
	 * Returns index of month, starting from 1. Example: 1 for Jan, 2 for Feb
	 * ... 12 for Dec
	 * 
	 * @param month
	 *            month name in 3-letters short form (jan) or full name of the
	 *            month (january) - not case-sensitive
	 * @return index of month
	 */
	private static int getMonthNum(String month) {
		for (int i = 0; i < MONTHS_SHORT.length; i++) {
			if (month.equalsIgnoreCase(MONTHS_SHORT[i])
					|| month.equalsIgnoreCase(MONTHS_LONG[i])) {
				return i + 1;
			}
		}
		return -1;
	}

	/**
	 * Returns index of day of the week, starting from 0 for Sunday. Example: 0
	 * for Sun, 1 for Mon, 2 for Tue ... 6 for Sat
	 * 
	 * @param string
	 *            day name in 3-letters short form (mon) or full name of the day
	 *            (monday) - not case-sensitive
	 * @return index of day of the week
	 */
	public static int getDayOfTheWeekIndex(String string) {
		for (int i = 0; i < DAYS_SHORT.length; i++) {
			if (string.equalsIgnoreCase(DAYS_SHORT[i])
					|| string.equalsIgnoreCase(DAYS_LONG[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Checks if a String represents date in any the following formats (not
	 * case-sensitive): 30/9/2014 (or) 30/9/14 (or) 30 September 2014 (or) 30
	 * Sep 2014 (or) 30 September 14 (or) 30 Sep 14 (or) 30/9 (or) 30 September
	 * (or) 30 Sep
	 * 
	 * @param inDate
	 *            string to be checked
	 * @return true if it matches any of the above formats, else false
	 */
	public static boolean isValidDate(String inDate) {
		List<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>();

		dateFormats.add(new SimpleDateFormat("d/M/yyyy")); // 14/3/2014
		dateFormats.add(new SimpleDateFormat("d/M/yy")); // 14/3/14
		dateFormats.add(new SimpleDateFormat("d MMM yyyy")); // 14 Mar 2014
		dateFormats.add(new SimpleDateFormat("d MMM yy")); // 14 Mar 14
		dateFormats.add(new SimpleDateFormat("d MMMM yyyy")); // 14 March 2014
		dateFormats.add(new SimpleDateFormat("d MMMM yy")); // 14 March 14
		dateFormats.add(new SimpleDateFormat("d/M")); // 14/3
		dateFormats.add(new SimpleDateFormat("d MMM")); // 14 Mar
		dateFormats.add(new SimpleDateFormat("d MMMM")); // 14 March

		for (SimpleDateFormat format : dateFormats) {
			format.setLenient(false);
			try {
				format.parse(inDate.trim());
				return true;
			} catch (ParseException pe) {
				// Try other formats
			}
		}
		return false;
	}

	/**
	 * Checks if a string represents a day in any of the following formats: Day
	 * of the week - Monday, Mon, Sunday, Sun, etc. (or) today (or) tomorrow
	 * (or) tmw
	 * 
	 * @param string
	 *            string to be checked
	 * @return true if it matches any of the above formats, else false
	 */
	public static boolean isValidDay(String string) {
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

	/**
	 * Returns today's date
	 * 
	 * @return Date object containing today's date
	 */
	public static Date getTodaysDate() {
		Calendar rightNow = Calendar.getInstance(); // Get the current date
		return new Date(rightNow.get(Calendar.DATE),
				rightNow.get(Calendar.MONTH) + 1, rightNow.get(Calendar.YEAR));
	}

	/**
	 * Returns a date which is a specified number of days after today
	 * 
	 * @param amount
	 *            number of days after today
	 * @return advanced date
	 */
	public static Date addDaysToToday(int amount) {
		Calendar rightNow = Calendar.getInstance(); // Get the current date
		rightNow.setTime(new java.util.Date());
		rightNow.add(Calendar.DATE, amount);
		return new Date(rightNow.get(Calendar.DATE),
				rightNow.get(Calendar.MONTH) + 1, rightNow.get(Calendar.YEAR));
	}

	/**
	 * @return which day of the week (sun, mon ... sat) it is today
	 */
	public static String getTodaysDayOfTheWeek() {
		Calendar rightNow = Calendar.getInstance(); // Get the current date
		// sun = 0, mon = 1 ... sat = 6
		return DAYS_LONG[rightNow.get(Calendar.DAY_OF_WEEK) - 1];
	}

	/**
	 * @return the index of which day of the week (0-sun, 1-mon ... 6-sat) it is
	 *         today
	 */
	public static int getTodaysDayOfTheWeekIndex() {
		Calendar rightNow = Calendar.getInstance(); // Get the current date
		// sun = 0, mon = 1 ... sat = 6
		return rightNow.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * Checks whether date is between 1 and 31 (both inclusive)
	 * 
	 * @param dateStr
	 *            string to be checked
	 * @return true if date is between 1 and 31 (both inclusive), else false
	 */
	private static boolean isDateInRange(String dateStr) {
		int date = Integer.parseInt(dateStr.trim());
		return (date > 0) && (date <= 31);
	}

	/**
	 * Checks whether month is between 1 and 12 (both inclusive)
	 * 
	 * @param monthStr
	 *            string to be checked
	 * @return true if month is between 1 and 12 (both inclusive), else false
	 */
	private static boolean isMonthInRange(String monthStr) {
		int month = Integer.parseInt(monthStr.trim());
		return (month > 0) && (month <= 12);
	}

	/**
	 * Checks whether month is between 2010 and 2099 (10 and 99 for 2-digit
	 * years)
	 * 
	 * @param yearStr
	 *            year to be checked
	 * @return true if month is between 2010 and 2099 (10 and 99 for 2-digit
	 *         years), else false
	 */
	private static boolean isYearInRange(String yearStr) {
		int year = Integer.parseInt(yearStr.trim());
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
	public static String removeFirstWord(String input) {
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
