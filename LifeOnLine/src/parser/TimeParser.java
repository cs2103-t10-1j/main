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
package parser;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lol.Constants;
import lol.Time;

public class TimeParser {
	/************* Attributes ***************/
	private String userInput;
	private String timeKeyword; // keyword preceding time - at, by, from or no keyword

	/************* Constructors ***************/
	public TimeParser() {
		this("");
	}

	public TimeParser(String userInput) {
		setUserInput(userInput);
		setTimeKeyword("");
	}

	/************* Accessors ***************/
	public String getUserInput() {
		return userInput;
	}
	
	public String getTimeKeyword() {
		return timeKeyword;
	}

	/************* Mutators ***************/
	public void setUserInput(String userInput) {
		this.userInput = userInput.toLowerCase();
	}
	
	public void setTimeKeyword(String timeKeyword) {
		this.timeKeyword = timeKeyword.toLowerCase();
	}

	/************* Other methods ***************/
	
	/**
	 * Returns the start time of task in userInput
	 * 
	 * @return start of a task in userInput if it exists, else returns null
	 */
	public Time getStartTime() {
		cleanUp();
		DateParser dtp = new DateParser();
		
		// at
		Pattern pAt = Pattern.compile("\\bat\\b");
		Matcher mAt = pAt.matcher(getUserInput());

		while (mAt.find()) {
			String parameter = getParameterStartingAtIndex(mAt.end());
			if (isValidTimeFormat(parameter)) {
				setTimeKeyword("at");
				if (isTimeRange(parameter)) {
					return createStartTimeFromRange(parameter);
				} else if (is24hrTime(parameter)) {
					return new Time(parameter.trim());
				} else {
					assert is12hrTime(parameter);
					return create12hrTime(parameter);
				}
			}
		}

		// by
		Pattern pBy = Pattern.compile("\\bby\\b");
		Matcher mBy = pBy.matcher(getUserInput());

		while (mBy.find()) {
			String parameter = getParameterStartingAtIndex(mBy.end());
			if (isValidTimeFormat(parameter)) {
				setTimeKeyword("by");
				if (isTimeRange(parameter)) {
					return createStartTimeFromRange(parameter);
				} else if (is24hrTime(parameter)) {
					return new Time(parameter.trim());
				} else {
					assert is12hrTime(parameter);
					return create12hrTime(parameter);
				}
			}
		}
		
		// from
		Pattern pFrom = Pattern.compile("\\bfrom\\b");
		Matcher mFrom = pFrom.matcher(getUserInput());

		while (mFrom.find()) {
			String parameter = getParameterStartingAtIndex(mFrom.end());
			if (isValidTimeFormat(parameter)) {
				setTimeKeyword("from");
				if (isTimeRange(parameter)) {
					return createStartTimeFromRange(parameter);
				} else if (is24hrTime(parameter)) {
					return new Time(parameter.trim());
				} else {
					assert is12hrTime(parameter);
					return create12hrTime(parameter);
				}
			}
		}

		// no keyword
		String temp = getUserInput();
		String[] words = temp.split(" ");

		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (isValidTimeFormat(word)) {
				if (isTimeRange(word)) {
					return createStartTimeFromRange(word);
				} else if (is24hrTime(word)) {
					// check if the word is both a year and 24hr time
					if (dtp.isBoth24hrTimeAndYear(word)) {
						// if it is meant to be a year, it should be preceded by day and month
						if (i - 2 > 0 && dtp.isValidDateFormat(words[i-2] + " " + words[i-1])) {
							continue;
						}
					}
					return new Time(word.trim());
				} else {
					assert is12hrTime(word);
					return create12hrTime(word);
				}
			}
			
			// find next 4 words because time formats can have at most
			// 5 words
			String[] nextWords = getNext4Words(words, i);

			if (hasTime(word, nextWords)) {
				Pattern p = Pattern.compile(word + "\\s*" + nextWords[0]);
				Matcher m = p.matcher(temp);

				if (m.find()) {
					String parameter = getParameterStartingAtIndex(m.start());
					if (isTimeRange(parameter)) {
						return createStartTimeFromRange(parameter);
					} else if (is24hrTime(parameter)) {
						return new Time(parameter.trim());
					} else {
						assert is12hrTime(parameter);
						return create12hrTime(parameter);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the end time of task in userInput
	 * 
	 * @return end of a task in userInput if it exists, else returns null
	 */
	public Time getEndTime() {
		cleanUp();

		// at
		Pattern pAt = Pattern.compile("\\bat\\b");
		Matcher mAt = pAt.matcher(getUserInput());

		while (mAt.find()) {
			String parameter = getParameterStartingAtIndex(mAt.end());
			if (isTimeRange(parameter)) {
				setTimeKeyword("at");
				return createEndTimeFromRange(parameter);
			}
		}

		// by
		Pattern pBy = Pattern.compile("\\bby\\b");
		Matcher mBy = pBy.matcher(getUserInput());

		while (mBy.find()) {
			String parameter = getParameterStartingAtIndex(mBy.end());
			if (isTimeRange(parameter)) {
				setTimeKeyword("by");
				return createEndTimeFromRange(parameter);
			}
		}
		
		// from
		Pattern pFrom = Pattern.compile("\\bfrom\\b");
		Matcher mFrom = pFrom.matcher(getUserInput());

		while (mFrom.find()) {
			String parameter = getParameterStartingAtIndex(mFrom.end());
			if (isTimeRange(parameter)) {
				setTimeKeyword("from");
				return createEndTimeFromRange(parameter);
			}
		}

		// no keyword
		String temp = getUserInput();
		String[] words = temp.split(" ");

		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (isTimeRange(word)) {
				return createEndTimeFromRange(word);
			}
			
			// find next 4 words because time formats can have at most
			// 5 words
			String[] nextWords = getNext4Words(words, i);

			if (hasTime(word, nextWords)) {
				Pattern p = Pattern.compile("\\b" + word + "\\b");
				Matcher m = p.matcher(temp);

				if (m.find()) {
					String parameter = getParameterStartingAtIndex(m.start());
					if (isTimeRange(parameter)) {
						return createEndTimeFromRange(parameter);
					} 
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the userInput without start time, end time, time range and keywords preceding that
	 * time. Example: if userInput is "add task on 6 oct 6-9pm", returns "add task on 6 oct"
	 * 
	 * @return userInput without time and keywords preceding that time
	 */
	public String getUserInputWithoutTime() {
		if (getStartTime() == null) {
			return getUserInput();
		}
		String keyword = getTimeKeyword();
		String time = "";

		if (keyword.isEmpty()) {
			String temp = getUserInput();
			
			String[] words = temp.split(" ");

			for (int i = 0; i < words.length; i++) {
				String word = words[i];
				if (isValidTimeFormat(word)) {
					time = word.trim();
					break;
				}
				
				// find next 4 words because time formats can have at most 5 words
				String[] nextWords = getNext4Words(words, i);
				
				if (hasTime(word, nextWords)) {
					Pattern p = Pattern.compile(word + "\\s*" + nextWords[0]);
					Matcher m = p.matcher(temp);

					if (m.find()) {
						time = getParameterStartingAtIndex(m.start()).trim();
					}
					break;
				}
			}
		} else {
			Pattern p = Pattern.compile("\\b" + keyword + "\\b");
			Matcher m = p.matcher(getUserInput());

			while (m.find()) {
				String parameter = getParameterStartingAtIndex(m.end());
				if (isValidTimeFormat(parameter)) {
					time = parameter.trim();
				}
			}
		}
		
		if (getTimeKeyword().isEmpty()) {
			return cleanUp(getUserInput().replaceAll("\\b" + time + "\\b", " "));
		} else {
			return cleanUp(getUserInput().replaceAll("\\b" + keyword + "\\b\\s\\b" + time + "\\b", " "));
		}
	}
	
	/**
	 * Checks whether a string is a valid time format
	 * @param string  String to be checked
	 * @return true if string is a valid time format, else false
	 */
	public boolean isValidTimeFormat(String string) {
		return is12hrTime(string) || is24hrTime(string) || isTimeRange(string);
	}
	
	/**
	 * Checks whether a string represents time in 12-hour format, e.g. 2pm or
	 * 1.20am
	 * 
	 * @param string
	 *            String to be checked
	 * @return true if it represents 12-hour time, else false
	 */
	public boolean is12hrTime(String string) {
		try {
			string = string.trim();
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
		try {
			string = string.trim();
			if (string.length() != Constants.LENGTH_24HOUR_FORMAT) {
				return false;
			}
			
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
				times = cleanUp(times);
				containsSeparator = true;
			} else if (string.contains(Constants.SEPARATOR_DASH)) {
				times = string.split(Constants.SEPARATOR_DASH);
				times = cleanUp(times);
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
			String[] splitHourMin = cleanUp(hourMin.split(Constants.SEPARATOR_DOT));

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
				times = cleanUp(string.split(Constants.SEPARATOR_TO));
			} else if (string.contains(Constants.SEPARATOR_DASH)) {
				times = cleanUp(string.split(Constants.SEPARATOR_DASH));
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

	/**
	 * Returns current time as a Time object
	 * @return current time
	 */
	public Time getCurrentTime() {
		Calendar rightNow = Calendar.getInstance(); // Get the current date and
													// time
		int hour = rightNow.get(Calendar.HOUR);
		if (hour == 0) {
			hour = 12;
		}
		int minute = rightNow.get(Calendar.MINUTE);
		String ampm = rightNow.get(Calendar.AM_PM) == Calendar.AM ? Constants.STRING_AM
				: Constants.STRING_PM;

		return new Time(hour, minute, ampm);
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
	 * Returns a time starting from 'index' till
	 * the occurrence of another reserved word or the end of the string or another parameter,
	 * whichever is earlier
	 * 
	 * @param index
	 *            index of userInput at which the parameter to be returned starts
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
		return removeDescriptionAfterTimeIfAny(parameter);

	}
	
	/**
	 * Removes task description after a time or time range, if any
	 * 
	 * @param time
	 *            String containing a time or time range which may or may not be followed
	 *            by a task description
	 * @return time or time range as a string
	 */
	public String removeDescriptionAfterTimeIfAny(String time) {
		String[] words = time.split(" ");
		String firstWord = words[0];
		String[] nextWords = getNext4Words(words, 0);
		
		if (isValidTimeFormat(firstWord + " " + nextWords[0] + " " + nextWords[1] + " " + nextWords[2] + " " + nextWords[3])) {
			return (firstWord + " " + nextWords[0] + " " + nextWords[1] + " " + nextWords[2] + " " + nextWords[3]).trim();
		} else if(isValidTimeFormat(firstWord + " " + nextWords[0] + " " + nextWords[1] + " " + nextWords[2])) {
			return (firstWord + " " + nextWords[0] + " " + nextWords[1] + " " + nextWords[2]).trim();
		} else if (isValidTimeFormat(firstWord + " " + nextWords[0] + " " + nextWords[1])) {
			return (firstWord + " " + nextWords[0] + " " + nextWords[1]).trim();
		} else if (isValidTimeFormat(firstWord + " " + nextWords[0])) {
			return (firstWord + " " + nextWords[0]).trim();
		} else if (isValidTimeFormat(firstWord)) {
			return firstWord.trim();
		} else {
			return time.trim();
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

			if (isReservedWord(word)
					|| hasDate(word, nextWords)) {
				Pattern p = Pattern.compile(word + "\\s*" + nextWords[0]);
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
		DateParser dp = new DateParser();
		try {
			return dp.isValidDateFormat(word)
					|| dp.isValidDateFormat(word + " " + nextWords[0])
					|| dp.isValidDateFormat(word + " " + nextWords[0] + " "
							+ nextWords[1])
					|| dp.isValidDateFormat(word + " " + nextWords[0] + " "
							+ nextWords[1] + " " + nextWords[2])
					|| dp.isValidDateFormat(word + " " + nextWords[0] + " "
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
		try {
			return isValidTimeFormat(word + " " + nextWords[0] + " " + nextWords[1] + " " + nextWords[2] + " " + nextWords[3]) || isValidTimeFormat(word + " " + nextWords[0] + " "
							+ nextWords[1] + " " + nextWords[2]) || isValidTimeFormat(word + " " + nextWords[0] + " "
							+ nextWords[1]) || isValidTimeFormat(word + " " + nextWords[0]) || isValidTimeFormat(word);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Checks is a word is a keyword (at, by etc.) or reserved word (days of the
	 * week, today, tomorrow, tmw)
	 * 
	 * @param word
	 *            word to be checked
	 * @return true if a word is a keyword or reserved word, else false
	 */
	public boolean isReservedWord(String word) {
		return hasWordInDictionary(Constants.KEYWORDS, word)
				|| hasWordInDictionary(Constants.DAYS_IMMEDIATE, word)
				|| hasWordInDictionary(Constants.DAYS_LONG, word)
				|| hasWordInDictionary(Constants.DAYS_SHORT, word);
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
