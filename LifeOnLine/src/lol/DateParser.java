/**
 * This class parses a string containing a date or day of the week.
 * The following formats of date are allowed: (not case-sensitive)
 * 
 * 30/9/2014
 * 30/9/14 
 * 30 September 2014 
 * 30 Sep 2014 
 * 30 September 14 
 * 30 Sep 14 
 * 30/9 
 * 30 September
 * 30 Sep
 * 
 * Day of the week: Monday, Mon, Sunday, sun
 * Today, tomorrow, tmw
 */

package lol;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateParser {
	
	/**
	 * Checks if a string is a valid date format
	 * @param inDate  string to be checked
	 * @return  true if inDate is a valid date format, else false
	 */
	public boolean isValidDateFormat(String inDate) {
		return isValidDate(inDate) || isValidDay(inDate);
	}

	/**
	 * Checks if a String represents date in any of the allowed formats
	 * 
	 * @param inDate
	 *            string to be checked
	 * @return true if it matches any of the above formats, else false
	 */
	public boolean isValidDate(String inDate) {
		inDate = inDate.trim();
		List<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>();

		// Allowed date formats
		dateFormats.add(new SimpleDateFormat("d/M/yyyy")); // 14/3/2014
		dateFormats.add(new SimpleDateFormat("d/M/yy")); // 14/3/14
		dateFormats.add(new SimpleDateFormat("d MMMM yyyy")); // 14 March 2014
		dateFormats.add(new SimpleDateFormat("d MMMM yy")); // 14 March 14
		dateFormats.add(new SimpleDateFormat("d/M")); // 14/3
		

		for (SimpleDateFormat format : dateFormats) {
			format.setLenient(false);
			try {
				format.parse(inDate.trim());
				return true;
			} catch (ParseException pe) {
				// Try other formats
			}
		}
		
		List<SimpleDateFormat> dateFormatsAbbreviatedMonth = new ArrayList<SimpleDateFormat>();
		dateFormatsAbbreviatedMonth.add(new SimpleDateFormat("d MMM yyyy")); // 14 Mar 2014
		dateFormatsAbbreviatedMonth.add(new SimpleDateFormat("d MMM yy")); // 14 Mar 14
		dateFormatsAbbreviatedMonth.add(new SimpleDateFormat("d MMM")); // 14 Mar
		dateFormatsAbbreviatedMonth.add(new SimpleDateFormat("d MMMM")); // 14 March
		
		for (SimpleDateFormat format : dateFormatsAbbreviatedMonth) {
			format.setLenient(false);
			try {
				format.parse(inDate.trim());
				String month = getNthWord(inDate, 2);
				for (String fullMonthName : Constants.MONTHS_LONG) {
					if (fullMonthName.contains(month)) {
						return true;
					}
				}
			} catch (ParseException pe) {
				// Try other formats
			}
		}
		return false;
	}

	/**
	 * Given a string that represents a valid date, creates a Date object
	 * 
	 * @param string
	 *            String representing date in any of the allowed formats
	 * @return Date object if string is a valid date, else null
	 */
	public Date createDate(String string) {
		if (!isValidDate(string) && !isValidDay(string)) {
			return null;
		}

		String[] dateSlash = string.split("/"); // separated by forward-slash
		String[] dateSpace = string.split(" "); // separated by space

		// Date format 30/9/2014 or 30/9/14
		// if the date has 3 parts
		if (dateSlash.length == Constants.LENGTH_DAY_MONTH_YEAR) {
			return new Date(Integer.parseInt(dateSlash[Constants.INDEX_DAY]),
					Integer.parseInt(dateSlash[Constants.INDEX_MONTH]),
					Integer.parseInt(dateSlash[Constants.INDEX_YEAR]));
		}

		// Date format 30 September 2014 or 30 Sep 2014 or 30 September 14 or 30
		// Sep 14
		// if the date has 3 parts
		if (dateSpace.length == Constants.LENGTH_DAY_MONTH_YEAR) {
			// get number of month e.g 1 for jan
			int monthNum = getMonthNum(dateSpace[Constants.INDEX_MONTH]);
			return new Date(Integer.parseInt(dateSpace[Constants.INDEX_DAY]),
					monthNum, Integer.parseInt(dateSpace[Constants.INDEX_YEAR]));
		}

		// Date format 30/9
		// if the date has 2 parts
		if (dateSlash.length == Constants.LENGTH_DAY_MONTH) {
			return new Date(Integer.parseInt(dateSlash[Constants.INDEX_DAY]),
					Integer.parseInt(dateSlash[Constants.INDEX_MONTH]));
		}

		// Date format 30 September or 30 Sep
		// if the date has 2 parts
		if (dateSpace.length == Constants.LENGTH_DAY_MONTH) {
			// get number of month e.g 1 for jan
			int monthNum = getMonthNum(dateSpace[Constants.INDEX_MONTH]);
			return new Date(Integer.parseInt(dateSpace[Constants.INDEX_DAY]),
					monthNum);
		}

		// Day of the week - Monday, Mon
		// if string has 1 word and day of the week is valid
		if (LOLParser.countWords(string) == 1
				&& (LOLParser.hasWordInDictionary(Constants.DAYS_SHORT, string) || LOLParser
						.hasWordInDictionary(Constants.DAYS_LONG, string))) {

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
		if (LOLParser.countWords(string) == 1
				&& LOLParser.hasWordInDictionary(Constants.DAYS_IMMEDIATE,
						string)) {
			if (string
					.equalsIgnoreCase(Constants.DAYS_IMMEDIATE[Constants.INDEX_TODAY])) { // today
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
	 * @return index of month if month is valid, else -1
	 */
	public int getMonthNum(String month) {
		for (int i = 0; i < Constants.MONTHS_SHORT.length; i++) {
			if (month.equalsIgnoreCase(Constants.MONTHS_SHORT[i])
					|| month.equalsIgnoreCase(Constants.MONTHS_LONG[i])) {
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
	 * @return index of day of the week if valid, else -1
	 */
	public int getDayOfTheWeekIndex(String string) {
		for (int i = 0; i < Constants.DAYS_SHORT.length; i++) {
			if (string.equalsIgnoreCase(Constants.DAYS_SHORT[i])
					|| string.equalsIgnoreCase(Constants.DAYS_LONG[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Checks if a string represents a day in any of the following formats: Day
	 * of the week - Monday, Mon, Sunday, Sun, etc. (or) today (or) tomorrow
	 * (or) tmw, not case-sensitive
	 * 
	 * @param string
	 *            string to be checked
	 * @return true if it matches any of the above formats, else false
	 */
	public boolean isValidDay(String string) {
		string = string.trim();

		// Day of the week - Monday, Mon
		if (LOLParser.countWords(string) == 1
				&& (LOLParser.hasWordInDictionary(Constants.DAYS_SHORT, string) || LOLParser
						.hasWordInDictionary(Constants.DAYS_LONG, string))) {
			return true;
		}

		// today, tomorrow, tmw
		if (LOLParser.countWords(string) == 1
				&& LOLParser.hasWordInDictionary(Constants.DAYS_IMMEDIATE,
						string)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns today's date
	 * 
	 * @return Date object containing today's date
	 */
	public Date getTodaysDate() {
		Calendar rightNow = Calendar.getInstance(); // Get the current date
		return new Date(rightNow.get(Calendar.DATE),
				rightNow.get(Calendar.MONTH) + 1, rightNow.get(Calendar.YEAR));
	}

	/**
	 * Returns a date which is a specified number of days after today
	 * 
	 * @param amount
	 *            number of days after today, if negative then decrement
	 * @return advanced date if amount is positive, earlier date if amount is
	 *         negative
	 */
	public Date addDaysToToday(int amount) {
		Calendar rightNow = Calendar.getInstance(); // Get the current date
		rightNow.setTime(new java.util.Date());
		rightNow.add(Calendar.DATE, amount);
		return new Date(rightNow.get(Calendar.DATE),
				rightNow.get(Calendar.MONTH) + 1, rightNow.get(Calendar.YEAR));
	}

	/**
	 * @return which day of the week (sunday, monday ... saturday) it is today
	 */
	public String getTodaysDayOfTheWeek() {
		Calendar rightNow = Calendar.getInstance(); // Get the current date
		// sun = 0, mon = 1 ... sat = 6
		return Constants.DAYS_LONG[rightNow.get(Calendar.DAY_OF_WEEK) - 1];
	}

	/**
	 * @return the index of which day of the week (0-sun, 1-mon ... 6-sat) it is
	 *         today
	 */
	public int getTodaysDayOfTheWeekIndex() {
		Calendar rightNow = Calendar.getInstance(); // Get the current date
		// sun = 0, mon = 1 ... sat = 6
		return rightNow.get(Calendar.DAY_OF_WEEK) - 1;
	}
	
	/**
	 * Removes multiple spaces between words, leading and trailing spaces
	 * @param input  string to be cleaned up
	 * @return string without extra spaces
	 */
	public String cleanUp(String input) {
		input = input.trim();
		input = input.replaceAll("\\s+", " ");
		return input;
	}
	
	/**
	 * Returns the Nth word of an input, where n starts from 1, e.g. 1 for 1st word
	 * @param input  input string
	 * @param n      the index of word to return, starting from 1
	 * @return       Nth word of input
	 */
	public String getNthWord(String input, int n) {
		input = cleanUp(input);
		String[] words = input.split(Constants.SPACE);
		if (words.length < n) {
			return "";
		} else {
			return words[n - 1];
		}
	}

}
