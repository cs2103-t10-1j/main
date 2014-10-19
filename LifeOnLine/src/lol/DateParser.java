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
 * 
 * Month names can have 3 or more letters.
 */

package lol;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateParser {
	private String userInput;
	private String dateKeyword; // keyword preceding date - on, by or no keyword

	public DateParser() {
		this("");
	}

	public DateParser(String userInput) {
		setUserInput(userInput);
		setDateKeyword("");
	}

	public String getUserInput() {
		return userInput;
	}
	
	public String getDateKeyword() {
		return dateKeyword;
	}

	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}
	
	public void setDateKeyword(String dateKeyword) {
		this.dateKeyword = dateKeyword;
	}

	public Date getDueDate() {
		cleanUp();

		// on
		Pattern pOn = Pattern.compile("\\bon\\b");
		Matcher mOn = pOn.matcher(getUserInput());

		while (mOn.find()) {
			String parameter = getParameterStartingAtIndex(mOn.end());
			if (isValidDateFormat(parameter)) {
				setDateKeyword("on");
				return createDate(parameter);
			}
		}

		// by
		Pattern pBy = Pattern.compile("\\bby\\b");
		Matcher mBy = pBy.matcher(getUserInput());

		while (mBy.find()) {
			String parameter = getParameterStartingAtIndex(mBy.end());
			if (isValidDateFormat(parameter)) {
				setDateKeyword("by");
				return createDate(parameter);
			}
		}

		// no keyword
		String temp = getUserInput();
		String[] words = temp.split(" ");

		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (isValidDay(word)) {
				return createDate(word);
			}
			
			// find next 4 words because date and time formats can have at most
			// 5 words
			String[] nextWords = getNext4Words(words, i);

			if (hasDate(word, nextWords)) {
				Pattern p = Pattern.compile("\\b" + word + "\\b\\s*\\b" + nextWords[0] + "\\b");
				Matcher m = p.matcher(temp);

				if (m.find()) {
					String parameter = getParameterStartingAtIndex(m.start());
					return createDate(parameter);
				}
			}
		}
		return null;
	}

	public String getUserInputWithoutDueDate() {
		if (getDueDate() == null) {
			return getUserInput();
		}
		String keyword = getDateKeyword();
		String date = "";
		
		if (keyword.isEmpty()) {
			String temp = getUserInput();
			String[] words = temp.split(" ");

			for (int i = 0; i < words.length; i++) {
				String word = words[i];
				if (isValidDay(word)) {
					date = word.trim();
					break;
				}
				
				// find next 4 words because date and time formats can have at most
				// 5 words
				String[] nextWords = getNext4Words(words, i);

				if (hasDate(word, nextWords)) {
					Pattern p = Pattern.compile("\\b" + word + "\\b\\s*\\b" + nextWords[0] + "\\b");
					Matcher m = p.matcher(temp);

					if (m.find()) {
						date = getParameterStartingAtIndex(m.start()).trim();
					}
				}
			}
		} else {
			Pattern p = Pattern.compile("\\b" + keyword + "\\b");
			Matcher m = p.matcher(getUserInput());

			while (m.find()) {
				String parameter = getParameterStartingAtIndex(m.end());
				
				if (isValidDateFormat(parameter)) {
					date = parameter.trim();
				}
			}
		}
		
		if (getDateKeyword().isEmpty()) {
			return cleanUp(getUserInput().replaceAll("\\b" + date + "\\b", " "));
		} else {
			return cleanUp(getUserInput().replaceAll("\\b" + keyword + "\\b\\s\\b" + date + "\\b", " "));
		}
	}
	
	/**
	 * Checks if a string is a valid date format
	 * 
	 * @param inDate
	 *            string to be checked
	 * @return true if inDate is a valid date format, else false
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
		dateFormatsAbbreviatedMonth.add(new SimpleDateFormat("d MMM yyyy")); // 14
																				// Mar
																				// 2014
		dateFormatsAbbreviatedMonth.add(new SimpleDateFormat("d MMM yy")); // 14
																			// Mar
																			// 14
		dateFormatsAbbreviatedMonth.add(new SimpleDateFormat("d MMM")); // 14
																		// Mar
		dateFormatsAbbreviatedMonth.add(new SimpleDateFormat("d MMMM")); // 14
																			// March

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
		dateSlash = cleanUp(dateSlash);
		String[] dateSpace = string.split(" "); // separated by space
		dateSpace = cleanUp(dateSpace);

		// Date format 30/9/2014 or 30/9/14
		// if the date has 3 parts
		if (dateSlash.length == Constants.LENGTH_DAY_MONTH_YEAR) {
			if (Integer.parseInt(dateSlash[Constants.INDEX_DAY]) < 0 || Integer.parseInt(dateSlash[Constants.INDEX_MONTH]) < 0 || Integer.parseInt(dateSlash[Constants.INDEX_YEAR]) < 0) {
				return null;
			}
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
			if (Integer.parseInt(dateSpace[Constants.INDEX_DAY]) < 0 || monthNum < 0 || Integer.parseInt(dateSpace[Constants.INDEX_YEAR]) < 0) {
				return null;
			}
			return new Date(Integer.parseInt(dateSpace[Constants.INDEX_DAY]),
					monthNum, Integer.parseInt(dateSpace[Constants.INDEX_YEAR]));
		}

		// Date format 30/9
		// if the date has 2 parts
		if (dateSlash.length == Constants.LENGTH_DAY_MONTH) {
			if (Integer.parseInt(dateSlash[Constants.INDEX_DAY]) < 0 || Integer.parseInt(dateSlash[Constants.INDEX_MONTH]) < 0) {
				return null;
			}
			return new Date(Integer.parseInt(dateSlash[Constants.INDEX_DAY]),
					Integer.parseInt(dateSlash[Constants.INDEX_MONTH]));
		}

		// Date format 30 September or 30 Sep
		// if the date has 2 parts
		if (dateSpace.length == Constants.LENGTH_DAY_MONTH) {
			// get number of month e.g 1 for jan
			int monthNum = getMonthNum(dateSpace[Constants.INDEX_MONTH]);
			if (Integer.parseInt(dateSpace[Constants.INDEX_DAY]) < 0 || monthNum < 0) {
				return null;
			}
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
	 * 
	 * @param input
	 *            string to be cleaned up
	 * @return string without extra spaces
	 */
	public String cleanUp(String input) {
		input = input.trim();
		input = input.replaceAll("\\s+", " ");
		return input;
	}
	
	/**
	 * Removes multiple spaces between words, leading and trailing spaces for all strings in an array
	 * @param input  array containing strings to be formatted
	 * @return  array with formatted strings
	 */
	public String[] cleanUp(String[] input) {
		for (int i = 0; i < input.length; i++) {
			String temp = input[i];
			temp = temp.trim();
			temp = cleanUp(temp);
			input[i] = temp;
		}
		return input;
	}

	/**
	 * Removes multiple spaces between words, leading and trailing spaces in the
	 * userInput
	 * 
	 * @return string without extra spaces
	 */
	public String cleanUp() {
		String input = getUserInput();
		input = input.trim();
		input = input.replaceAll(Constants.REGEX_ONE_OR_MORE_SPACES,
				Constants.SPACE);
		setUserInput(input);
		return input;
	}

	/**
	 * Returns the Nth word of an input, where n starts from 1, e.g. 1 for 1st
	 * word
	 * 
	 * @param input
	 *            input string
	 * @param n
	 *            the index of word to return, starting from 1
	 * @return Nth word of input
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

	/**
	 * Returns a due date starting from 'index' till
	 * the occurrence of another reserved word or the end of the string or another parameter,
	 * whichever is earlier
	 * 
	 * @param index
	 *            index of userInput at which the paramter to be returned starts
	 * @return parameter starting from index
	 */
	public String getParameterStartingAtIndex(int index) {
		if (isIndexOutOfBounds(index)) {
			return null;
		}
		String parameter;
		int nextKeywordIndex = getIndexOfNextReservedWord(index + 1);
		
		if (nextKeywordIndex == Constants.NOT_FOUND) {
			parameter = getUserInput().substring(index).trim();
		} else {
			assert nextKeywordIndex > 0;
			parameter = getUserInput().substring(index, nextKeywordIndex).trim();
		}
		
		// Check if the due date is followed by a description
		return removeDescriptionFromDueDateIfAny(parameter);

	}
	
	public boolean isYear(String year) {
		try {
			int yr = Integer.parseInt(year);
			return (yr > 9 && yr < 100) || (yr > 2010 && yr < 2099);
		} catch (Exception e) {
			return false;
		}
	}
	
	public String removeDescriptionFromDueDateIfAny(String date) {
		String[] words = date.split(" ");
		String firstWord = words[0];
		String[] nextWords = getNext4Words(words, 0);
		
		if (isValidDateFormat(firstWord)) {
			return firstWord.trim();
		} else if (isValidDateFormat(firstWord + " " + nextWords[0]) && !(isYear(nextWords[1]))) {
			return (firstWord + " " + nextWords[0]).trim();
		} else if (isValidDateFormat(firstWord + " " + nextWords[0] + " " + nextWords[1]) && !(isYear(nextWords[2]))) {
			return (firstWord + " " + nextWords[0] + " " + nextWords[1]).trim();
		} else if (isValidDateFormat(firstWord + " " + nextWords[0] + " " + nextWords[1] + " " + nextWords[2]) && !(isYear(nextWords[3]))) {
			return (firstWord + " " + nextWords[0] + " " + nextWords[1] + " " + nextWords[2]).trim();
		} else {
			return date;
		}
	}

	/**
	 * Returns the starting index of the next keyword
	 * 
	 * @param beginIndex
	 *            index of userInput to start searching from
	 * @return starting index of next keyword, -1 if not found
	 */
	public int getIndexOfNextReservedWord(int beginIndex) {
		String temp = getUserInput().substring(beginIndex);
		String[] words = temp.split(Constants.SPACE);
		int minIndex = Constants.NOT_FOUND;

		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			String[] nextWords = getNext4Words(words, i);

			if (isKeyword(word)
					|| hasTime(word, nextWords)) {
				Pattern p = Pattern.compile("\\b" + word + "\\b\\s*\\b" + nextWords[0] + "\\b");
				Matcher m = p.matcher(temp);

				if (m.find()) {
					int keywordIndex = m.start() + beginIndex;
					if (minIndex == Constants.NOT_FOUND
							|| keywordIndex < minIndex) {
						minIndex = keywordIndex;
					}
				}
			}
		}
		return minIndex;
	}

	/**
	 * Checks whether the word and the next 4 words form a valid date
	 * 
	 * @param word
	 *            first word
	 * @param nextWords
	 *            second to fifth words, if there are less than 4 elements,
	 *            empty strings are entered for the array element
	 * @return true if word and nextWords contain a valid date, else false
	 */
	public boolean hasDate(String word, String[] nextWords) {
		try {
			return isValidDateFormat(word)
					|| isValidDateFormat(word + " " + nextWords[0])
					|| isValidDateFormat(word + " " + nextWords[0] + " "
							+ nextWords[1])
					|| isValidDateFormat(word + " " + nextWords[0] + " "
							+ nextWords[1] + " " + nextWords[2])
					|| isValidDateFormat(word + " " + nextWords[0] + " "
							+ nextWords[1] + " " + nextWords[2] + " "
							+ nextWords[3]);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Checks whether the word and the next 4 words form a valid time or time
	 * range
	 * 
	 * @param word
	 *            first word
	 * @param nextWords
	 *            second to fifth words, if there are less than 4 elements,
	 *            empty strings are entered for the array element
	 * @return true if word and nextWords contain a valid time format, else
	 *         false
	 */
	public boolean hasTime(String word, String[] nextWords) {
		TimeParser tp = new TimeParser();
		try {
			return tp.isValidTimeFormat(word)
					|| tp.isValidTimeFormat(word + " " + nextWords[0])
					|| tp.isValidTimeFormat(word + " " + nextWords[0] + " "
							+ nextWords[1])
					|| tp.isValidTimeFormat(word + " " + nextWords[0] + " "
							+ nextWords[1] + " " + nextWords[2])
					|| tp.isValidTimeFormat(word + " " + nextWords[0] + " "
							+ nextWords[1] + " " + nextWords[2] + " "
							+ nextWords[3]);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Checks is a word is a keyword (at, by etc.) but not a reserved word (days of the
	 * week, today, tomorrow, tmw)
	 * 
	 * @param word
	 *            word to be checked
	 * @return true if a word is a keyword but not reserved word, else false
	 */
	public boolean isKeyword(String word) {
		return hasWordInDictionary(Constants.KEYWORDS, word);
	}

	/**
	 * Checks whether index is out of bounds for userInput
	 * 
	 * @param index
	 *            index to be checked
	 * @return true if index out of bounds, else false
	 */
	public boolean isIndexOutOfBounds(int index) {
		return index < 0 || index >= getUserInput().length();
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
	public boolean hasWordInDictionary(String[] dictionary, String word) {
		word = word.trim();
		for (int i = 0; i < dictionary.length; i++) {
			if (dictionary[i].equalsIgnoreCase(word)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the next 4 words of userInput, starting from the index i + 1
	 * @param words  Array of words in userInput
	 * @param i  The index of the first word
	 * @return   The next 4 words after index i. If there are less than 4 words, the empty words are represented by empty strings
	 */
	public String[] getNext4Words(String[] words, int i) {
		String[] nextWords = { Constants.EMPTY_STRING,
				Constants.EMPTY_STRING, Constants.EMPTY_STRING,
				Constants.EMPTY_STRING };

		if (i < words.length - 4) {
			int index = 0;
			while (index < 4) {
				nextWords[index] = words[i + 1 + index];
				index++;
			}
		} else if (i < words.length - 3) {
			int index = 0;
			while (index < 3) {
				nextWords[index] = words[i + 1 + index];
				index++;
			}
		} else if (i < words.length - 2) {
			int index = 0;
			while (index < 2) {
				nextWords[index] = words[i + 1 + index];
				index++;
			}
		} else if (i < words.length - 1) {
			int index = 0;
			while (index < 1) {
				nextWords[index] = words[i + 1 + index];
				index++;
			}
		} else {
			assert i == words.length - 1;
		}
		return nextWords;
	}


}
