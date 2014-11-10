//@author A0118886M

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
 * Day of the week: Monday, Mon, Sunday, sun, etc.
 * Today, tomorrow, tmr, tmw, tmrw
 * 
 * Date ranges:
 * 31 october- 5 November
 * 31 oct 2014 to 5 Nov 2014
 * 31/10-5/11
 * Monday-Wed
 * tmr-sun
 * 4-9/12
 * 4-9 Dec
 * 4 to 9 December 2015
 * 
 */

package parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lol.Constants;
import lol.Date;

public class DateParser {
	/************* Attributes ***************/
	private String userInput;
	private String dateKeyword; // keyword preceding date - on, by, from or no
								// keyword

	/************* Constructors ***************/
	public DateParser() {
		this("");
	}

	public DateParser(String userInput) {
		setUserInput(userInput);
		setDateKeyword("");
	}

	/************* Accessors ***************/
	public String getUserInput() {
		return userInput;
	}

	public String getDateKeyword() {
		return dateKeyword;
	}

	/************* Mutators ***************/
	public void setUserInput(String userInput) {
		this.userInput = userInput.toLowerCase();
	}

	public void setDateKeyword(String dateKeyword) {
		this.dateKeyword = dateKeyword.toLowerCase();
	}

	/************* Other methods ***************/

	/**
	 * Returns the due date of task in userInput
	 * 
	 * @return due date of a task in userInput if it exists, else returns null
	 */
	public Date getDueDate() {
		cleanUp();
		String userInput = getUserInput();

		// on
		Pattern pOn = Pattern
				.compile(Constants.REGEX_KEYWORDS[Constants.INDEX_KEYWORD_ON]);
		Matcher mOn = pOn.matcher(getUserInput());

		while (mOn.find()) {
			String parameter = getParameterStartingAtIndex(mOn.end());
			if (isValidDateFormat(parameter)) {
				setDateKeyword(Constants.KEYWORDS[Constants.INDEX_KEYWORD_ON]);

				if (isDateRange(parameter)) {
					return createDatesFromRange(parameter)[Constants.INDEX_START_DATE];
				} else {
					return createDate(parameter);
				}
			}
		}

		// by
		Pattern pBy = Pattern
				.compile(Constants.REGEX_KEYWORDS[Constants.INDEX_KEYWORD_BY]);
		Matcher mBy = pBy.matcher(getUserInput());

		while (mBy.find()) {
			String parameter = getParameterStartingAtIndex(mBy.end());
			if (isValidDateFormat(parameter)) {
				setDateKeyword(Constants.KEYWORDS[Constants.INDEX_KEYWORD_BY]);

				if (isDateRange(parameter)) {
					return createDatesFromRange(parameter)[Constants.INDEX_START_DATE];
				} else {
					return createDate(parameter);
				}
			}
		}

		// from
		Pattern pFrom = Pattern
				.compile(Constants.REGEX_KEYWORDS[Constants.INDEX_KEYWORD_FROM]);
		Matcher mFrom = pFrom.matcher(userInput);

		while (mFrom.find()) {
			String parameter = getParameterStartingAtIndex(mFrom.end());

			if (isDateRange(parameter)) {
				setDateKeyword(Constants.KEYWORDS[Constants.INDEX_KEYWORD_FROM]);
				return createDatesFromRange(parameter)[Constants.INDEX_START_DATE];
			}
		}

		// no keyword
		String temp = getUserInput();
		String[] words = temp.split(Constants.SPACE);

		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			/*
			 * find next 4 words because date and time formats (excluding date
			 * range) can have at most 5 words
			 */
			String[] nextWords = getNext4Words(words, i);

			if (isValidDay(word)
					&& !nextWords[Constants.INDEX_SEPARATOR]
							.equalsIgnoreCase(Constants.SEPARATOR_TO)
					&& !nextWords[Constants.INDEX_SEPARATOR]
							.equals(Constants.SEPARATOR_DASH)) {
				return createDate(word);
			}

			/* check for date range */
			// 1 word - 24-26/11
			if (isDateRange(word)) {
				return createDatesFromRange(word)[Constants.INDEX_START_DATE];
			}
			// 2 words - 24-26 nov
			if (isDateRange(word + " " + nextWords[Constants.INDEX_2ND_WORD])) {
				return createDatesFromRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD])[Constants.INDEX_START_DATE];
			}
			// 3 words - sun - tue, 24/11 to 26/11
			if (isDateRange(word + Constants.SPACE
					+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
					+ nextWords[Constants.INDEX_3RD_WORD])) {
				return createDatesFromRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_3RD_WORD])[Constants.INDEX_START_DATE];
			}
			// 4 words - 24 to 29 Nov
			if (isDateRange(word + Constants.SPACE
					+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
					+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE
					+ nextWords[Constants.INDEX_4TH_WORD])) {
				return createDatesFromRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_4TH_WORD])[Constants.INDEX_START_DATE];
			}
			// 5 words - 24 Nov to 26 Nov
			if (isDateRange(word + Constants.SPACE
					+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
					+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE
					+ nextWords[Constants.INDEX_4TH_WORD] + Constants.SPACE
					+ nextWords[Constants.INDEX_5TH_WORD])) {
				return createDatesFromRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_4TH_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_5TH_WORD])[Constants.INDEX_START_DATE];
			}

			// more than 5 words
			if (i + Constants.INDEX_4TH_WORD < words.length) {
				// 4th word to 7th word
				String[] words4to7 = getNext4Words(words, i
						+ Constants.INDEX_4TH_WORD);

				// 6 words - 24 nov 14 to 26 nov
				if (isDateRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_4TH_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_5TH_WORD] + Constants.SPACE
						+ words4to7[Constants.INDEX_4TH_WORD])) {
					return createDatesFromRange(word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_3RD_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_4TH_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_5TH_WORD]
							+ Constants.SPACE
							+ words4to7[Constants.INDEX_4TH_WORD])[Constants.INDEX_START_DATE];
				}

				// 7 words - 30 dec 2014 to 2 jan 2015
				if (isDateRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_4TH_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_5TH_WORD] + Constants.SPACE
						+ words4to7[Constants.INDEX_4TH_WORD] + Constants.SPACE
						+ words4to7[Constants.INDEX_5TH_WORD])) {
					return createDatesFromRange(word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_3RD_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_4TH_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_5TH_WORD]
							+ Constants.SPACE
							+ words4to7[Constants.INDEX_4TH_WORD]
							+ Constants.SPACE
							+ words4to7[Constants.INDEX_5TH_WORD])[Constants.INDEX_START_DATE];
				}
			}

			// no date range starting from this word
			if (hasDate(word, nextWords)) {
				Pattern p = Pattern.compile(Constants.REGEX_WORD_START + word
						+ Constants.REGEX_WORD_END_SPACES_WORD_START
						+ nextWords[Constants.INDEX_2ND_WORD]
						+ Constants.REGEX_WORD_END);
				Matcher m = p.matcher(temp);

				if (m.find()) {
					String parameter = getParameterStartingAtIndex(m.start());
					return createDate(parameter);
				}
			}
		}
		return null;
	}

	/**
	 * Returns the end date of task in userInput
	 * 
	 * @return end date of a task in userInput if it exists, else returns null
	 */
	public Date getEndDate() {
		cleanUp();

		// on
		Pattern pOn = Pattern
				.compile(Constants.REGEX_KEYWORDS[Constants.INDEX_KEYWORD_ON]);
		Matcher mOn = pOn.matcher(getUserInput());

		while (mOn.find()) {
			String parameter = getParameterStartingAtIndex(mOn.end());
			if (isDateRange(parameter)) {
				setDateKeyword(Constants.KEYWORDS[Constants.INDEX_KEYWORD_ON]);
				return createDatesFromRange(parameter)[Constants.INDEX_END_DATE];
			}
		}

		// by
		Pattern pBy = Pattern
				.compile(Constants.REGEX_KEYWORDS[Constants.INDEX_KEYWORD_BY]);
		Matcher mBy = pBy.matcher(getUserInput());

		while (mBy.find()) {
			String parameter = getParameterStartingAtIndex(mBy.end());
			if (isDateRange(parameter)) {
				setDateKeyword(Constants.KEYWORDS[Constants.INDEX_KEYWORD_BY]);
				return createDatesFromRange(parameter)[Constants.INDEX_END_DATE];
			}
		}

		// from
		Pattern pFrom = Pattern
				.compile(Constants.REGEX_KEYWORDS[Constants.INDEX_KEYWORD_FROM]);
		Matcher mFrom = pFrom.matcher(getUserInput());

		while (mFrom.find()) {
			String parameter = getParameterStartingAtIndex(mFrom.end());
			if (isDateRange(parameter)) {
				setDateKeyword(Constants.KEYWORDS[Constants.INDEX_KEYWORD_FROM]);
				return createDatesFromRange(parameter)[Constants.INDEX_END_DATE];
			}
		}

		// no keyword
		String temp = getUserInput();
		String[] words = temp.split(Constants.SPACE);

		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			/*
			 * find next 4 words because date and time formats (excluding date
			 * range) can have at most 5 words
			 */
			String[] nextWords = getNext4Words(words, i);

			/* check for date range */
			// 1 word - 24-26/11
			if (isDateRange(word)) {
				return createDatesFromRange(word)[Constants.INDEX_END_DATE];
			}
			// 2 words - 24-26 nov
			if (isDateRange(word + Constants.SPACE
					+ nextWords[Constants.INDEX_2ND_WORD])) {
				return createDatesFromRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD])[Constants.INDEX_END_DATE];
			}
			// 3 words - sun - tue, 24/11 to 26/11
			if (isDateRange(word + Constants.SPACE
					+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
					+ nextWords[Constants.INDEX_3RD_WORD])) {
				return createDatesFromRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_3RD_WORD])[Constants.INDEX_END_DATE];
			}
			// 4 words - 24 to 29 Nov
			if (isDateRange(word + Constants.SPACE
					+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
					+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE
					+ nextWords[Constants.INDEX_4TH_WORD])) {
				return createDatesFromRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_4TH_WORD])[Constants.INDEX_END_DATE];
			}
			// 5 words - 24 Nov to 26 Nov
			if (isDateRange(word + Constants.SPACE
					+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
					+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE
					+ nextWords[Constants.INDEX_4TH_WORD] + Constants.SPACE
					+ nextWords[Constants.INDEX_5TH_WORD])) {
				return createDatesFromRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_4TH_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_5TH_WORD])[Constants.INDEX_END_DATE];
			}

			// more than 5 words
			if (i + Constants.INDEX_4TH_WORD < words.length) {
				String[] words4to7 = getNext4Words(words, i
						+ Constants.INDEX_4TH_WORD);
				// 6 words - 24 nov 14 to 26 nov
				if (isDateRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_4TH_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_5TH_WORD] + Constants.SPACE
						+ words4to7[Constants.INDEX_4TH_WORD])) {
					return createDatesFromRange(word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_3RD_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_4TH_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_5TH_WORD]
							+ Constants.SPACE
							+ words4to7[Constants.INDEX_4TH_WORD])[Constants.INDEX_END_DATE];
				}

				// 7 words - 30 dec 2014 to 2 jan 2015
				if (isDateRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_4TH_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_5TH_WORD] + Constants.SPACE
						+ words4to7[Constants.INDEX_4TH_WORD] + Constants.SPACE
						+ words4to7[Constants.INDEX_5TH_WORD])) {
					return createDatesFromRange(word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_3RD_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_4TH_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_5TH_WORD]
							+ Constants.SPACE
							+ words4to7[Constants.INDEX_4TH_WORD]
							+ Constants.SPACE
							+ words4to7[Constants.INDEX_5TH_WORD])[Constants.INDEX_END_DATE];
				}
			}
			// no date range starting from this word
		}
		return null;
	}

	/**
	 * Returns the userInput without the due date and keywords preceding that
	 * due date. Example: if userInput is "add task on 6 oct", returns
	 * "add task"
	 * 
	 * @return userInput without the due date and keywords preceding that due
	 *         date
	 */
	public String getUserInputWithoutDueDate() {
		if (getDueDate() == null) {
			return getUserInput();
		}
		String keyword = getDateKeyword();
		String date = Constants.EMPTY_STRING;

		if (keyword.isEmpty()) {
			String temp = getUserInput();
			String[] words = temp.split(Constants.SPACE);

			for (int i = 0; i < words.length; i++) {
				String word = words[i];

				if (isValidDay(word) && getEndDate() == null) {
					date = word.trim();
					break;
				}

				/*
				 * find next 4 words because date and time formats (excluding
				 * date range) can have at most 5 words
				 */
				String[] nextWords = getNext4Words(words, i);

				/* check for date range */
				// 1 word - 24-26/11
				if (isDateRange(word)) {
					date = word;
					break;
				}
				// 2 words - 24-26 nov
				if (isDateRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD])) {
					date = word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD];
					break;
				}
				// 3 words - sun - tue, 24/11 to 26/11
				if (isDateRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_3RD_WORD])) {
					date = word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_3RD_WORD];
					break;
				}
				// 4 words - 24 to 29 Nov
				if (isDateRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_4TH_WORD])) {
					date = word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_3RD_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_4TH_WORD];
					break;
				}
				// 5 words - 24 Nov to 26 Nov
				if (isDateRange(word + Constants.SPACE
						+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_4TH_WORD] + Constants.SPACE
						+ nextWords[Constants.INDEX_5TH_WORD])) {
					date = word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_3RD_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_4TH_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_5TH_WORD];
					break;
				}

				// more than 5 words
				if (i + Constants.INDEX_4TH_WORD < words.length) {
					String[] words4to7 = getNext4Words(words, i
							+ Constants.INDEX_4TH_WORD);

					// 7 words - 30 dec 2014 to 2 jan 2015
					if (isDateRange(word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_3RD_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_4TH_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_5TH_WORD]
							+ Constants.SPACE
							+ words4to7[Constants.INDEX_4TH_WORD]
							+ Constants.SPACE
							+ words4to7[Constants.INDEX_5TH_WORD])) {
						date = word + Constants.SPACE
								+ nextWords[Constants.INDEX_2ND_WORD]
								+ Constants.SPACE
								+ nextWords[Constants.INDEX_3RD_WORD]
								+ Constants.SPACE
								+ nextWords[Constants.INDEX_4TH_WORD]
								+ Constants.SPACE
								+ nextWords[Constants.INDEX_5TH_WORD]
								+ Constants.SPACE
								+ words4to7[Constants.INDEX_4TH_WORD]
								+ Constants.SPACE
								+ words4to7[Constants.INDEX_5TH_WORD];
						break;
					}

					// 6 words - 24 nov 14 to 26 nov
					if (isDateRange(word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_3RD_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_4TH_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_5TH_WORD]
							+ Constants.SPACE
							+ words4to7[Constants.INDEX_4TH_WORD])) {
						date = word + Constants.SPACE
								+ nextWords[Constants.INDEX_2ND_WORD]
								+ Constants.SPACE
								+ nextWords[Constants.INDEX_3RD_WORD]
								+ Constants.SPACE
								+ nextWords[Constants.INDEX_4TH_WORD]
								+ Constants.SPACE
								+ nextWords[Constants.INDEX_5TH_WORD]
								+ Constants.SPACE
								+ words4to7[Constants.INDEX_4TH_WORD];
						break;
					}

				}

				if (hasDate(word, nextWords)) {
					Pattern p = Pattern.compile(Constants.REGEX_WORD_START
							+ word + Constants.REGEX_WORD_END_SPACES_WORD_START
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.REGEX_WORD_END);
					Matcher m = p.matcher(temp);

					if (m.find()) {
						date = getParameterStartingAtIndex(m.start()).trim();
					}
					break;
				}
			}
		} else {
			// there is a keyword
			Pattern p = Pattern.compile(Constants.REGEX_WORD_START + keyword
					+ Constants.REGEX_WORD_END);
			Matcher m = p.matcher(getUserInput());

			while (m.find()) {
				String parameter = getParameterStartingAtIndex(m.end());
				if (isValidDateFormat(parameter)) {
					date = parameter.trim();
				}
			}
		}

		if (getDateKeyword().isEmpty()) {
			return cleanUp(getUserInput().replaceAll(
					Constants.REGEX_WORD_START + date
							+ Constants.REGEX_WORD_END, Constants.SPACE));
		} else {
			return cleanUp(getUserInput().replaceAll(
					Constants.REGEX_WORD_START + keyword
							+ Constants.REGEX_WORD_END_1SPACE_WORD_START + date
							+ Constants.REGEX_WORD_END, Constants.SPACE));
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
		return isValidDate(inDate) || isValidDay(inDate) || isDateRange(inDate);
	}

	/**
	 * Checks if a String represents date in any of the allowed formats
	 * 
	 * @param inDate
	 *            string to be checked
	 * @return true if it matches any of the above formats, else false
	 */
	public boolean isValidDate(String inDate) {
		inDate = inDate.trim().toLowerCase();
		List<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>();

		// Allowed date formats
		dateFormats.add(new SimpleDateFormat(
				Constants.DATE_FORMAT_DAY_NUM_MONTH_NUM_YEAR_LONG)); // 14/3/2014
		dateFormats.add(new SimpleDateFormat(
				Constants.DATE_FORMAT_DAY_NUM_MONTH_NUM_YEAR_SHORT)); // 14/3/14
		dateFormats.add(new SimpleDateFormat(
				Constants.DATE_FORMAT_DAY_NUM_MONTH_LONG_YEAR_LONG)); // 14
																		// March
																		// 2014
		dateFormats.add(new SimpleDateFormat(
				Constants.DATE_FORMAT_DAY_NUM_MONTH_LONG_YEAR_SHORT)); // 14
																		// March
																		// 14
		dateFormats.add(new SimpleDateFormat(
				Constants.DATE_FORMAT_DAY_NUM_MONTH_NUM)); // 14/3

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
		// 14 Mar 2014
		dateFormatsAbbreviatedMonth.add(new SimpleDateFormat(
				Constants.DATE_FORMAT_DAY_NUM_MONTH_SHORT_YEAR_LONG));

		// 14 Mar 14
		dateFormatsAbbreviatedMonth.add(new SimpleDateFormat(
				Constants.DATE_FORMAT_DAY_NUM_MONTH_SHORT_YEAR_SHORT));

		// 14 Mar
		dateFormatsAbbreviatedMonth.add(new SimpleDateFormat(
				Constants.DATE_FORMAT_DAY_NUM_MONTH_SHORT));

		// 14 March
		dateFormatsAbbreviatedMonth.add(new SimpleDateFormat(
				Constants.DATE_FORMAT_DAY_NUM_MONTH_LONG));

		for (SimpleDateFormat format : dateFormatsAbbreviatedMonth) {
			format.setLenient(false);
			try {
				format.parse(inDate.trim());
				String month = getNthWord(inDate, 2); // get the 2nd word
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

		String[] dateSlash = string.split(Constants.SEPARATOR_SLASH); // separated
																		// by
																		// forward-slash
		dateSlash = cleanUp(dateSlash);
		String[] dateSpace = string.split(Constants.SPACE); // separated by
															// space
		dateSpace = cleanUp(dateSpace);

		// Date format 30/9/2014 or 30/9/14
		// if the date has 3 parts
		if (dateSlash.length == Constants.LENGTH_DAY_MONTH_YEAR) {
			if (Integer.parseInt(dateSlash[Constants.INDEX_DAY]) < Constants.LIMIT_ZERO
					|| Integer.parseInt(dateSlash[Constants.INDEX_MONTH]) < Constants.LIMIT_ZERO
					|| Integer.parseInt(dateSlash[Constants.INDEX_YEAR]) < Constants.LIMIT_ZERO) {
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
			if (Integer.parseInt(dateSpace[Constants.INDEX_DAY]) < Constants.LIMIT_ZERO
					|| monthNum < Constants.LIMIT_ZERO
					|| Integer.parseInt(dateSpace[Constants.INDEX_YEAR]) < Constants.LIMIT_ZERO) {
				return null;
			}
			return new Date(Integer.parseInt(dateSpace[Constants.INDEX_DAY]),
					monthNum, Integer.parseInt(dateSpace[Constants.INDEX_YEAR]));
		}

		// Date format 30/9
		// if the date has 2 parts
		if (dateSlash.length == Constants.LENGTH_DAY_MONTH) {
			if (Integer.parseInt(dateSlash[Constants.INDEX_DAY]) < Constants.LIMIT_ZERO
					|| Integer.parseInt(dateSlash[Constants.INDEX_MONTH]) < Constants.LIMIT_ZERO) {
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
			if (Integer.parseInt(dateSpace[Constants.INDEX_DAY]) < Constants.LIMIT_ZERO
					|| monthNum < Constants.LIMIT_ZERO) {
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
				numDaysLater += Constants.NUMBER_OF_DAYS_IN_A_WEEK;
			}
			return addDaysToToday(numDaysLater);
		}

		// today, tomorrow, tmw, tmr, tmrw
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
		return Constants.NOT_FOUND;
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
		return Constants.NOT_FOUND;
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
	 * Returns the first 3 letters of the day of the week for a given date
	 * 
	 * @param date
	 *            date whose day of the week is to be found
	 * @return first 3 letters of the day of the week, 1st letter uppercase
	 */
	public String getDayOfTheWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.set(date.getYear4Digit(), date.getMonth() - 1, date.getDay());
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 1-sun to 7-sat
		String day = Constants.DAYS_SHORT[dayOfWeek - 1];
		return Character.toUpperCase(day.charAt(Constants.INDEX_1ST_LETTER))
				+ day.substring(Constants.INDEX_2ND_LETTER);
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
		input = input.replaceAll(Constants.REGEX_ONE_OR_MORE_SPACES,
				Constants.SPACE);
		return input;
	}

	/**
	 * Removes multiple spaces between words, leading and trailing spaces for
	 * all strings in an array
	 * 
	 * @param input
	 *            array containing strings to be formatted
	 * @return array with formatted strings
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
			return Constants.EMPTY_STRING;
		} else {
			return words[n - 1];
		}
	}

	/**
	 * Returns a due date starting from 'index' till the occurrence of another
	 * reserved word or the end of the string or another parameter, whichever is
	 * earlier
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
			assert nextKeywordIndex > Constants.LIMIT_ZERO;
			parameter = getUserInput().substring(index, nextKeywordIndex)
					.trim();
		}

		// Check if the due date is followed by a description
		if (isDateRange(parameter)) {
			return removeDescriptionFromDateRangeIfAny(parameter);
		} else {
			return removeDescriptionFromDueDateIfAny(parameter);
		}
	}

	/**
	 * Checks if a string represents a 2 digit year from 10 to 99 or 4 digit
	 * year from 2010 to 2099
	 * 
	 * @param year
	 *            string to be checked
	 * @return true if the string is a year, else false
	 */
	public boolean isYear(String year) {
		try {
			int yr = Integer.parseInt(year);
			return (yr >= Constants.LIMIT_MIN_2DIGIT_YEAR && yr <= Constants.LIMIT_MAX_2DIGIT_YEAR)
					|| (yr >= Constants.LIMIT_MIN_4DIGIT_YEAR && yr <= Constants.LIMIT_MAX_4DIGIT_YEAR);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Removes task description after a due date, if any
	 * 
	 * @param date
	 *            String containing a due date which may or may not be followed
	 *            by a task description
	 * @return due date as a String
	 */
	public String removeDescriptionFromDueDateIfAny(String date) {
		String[] words = date.split(Constants.SPACE);
		String firstWord = words[Constants.INDEX_1ST_WORD];
		String[] nextWords = getNext4Words(words, Constants.INDEX_1ST_WORD);

		if (isValidDateFormat(firstWord)) {
			return firstWord.trim();
		} else if (isValidDateFormat(firstWord + Constants.SPACE
				+ nextWords[Constants.INDEX_2ND_WORD])
				&& !(isYear(nextWords[Constants.INDEX_3RD_WORD]))) {
			return (firstWord + Constants.SPACE + nextWords[Constants.INDEX_2ND_WORD])
					.trim();
		} else if (isValidDateFormat(firstWord + Constants.SPACE
				+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
				+ nextWords[Constants.INDEX_3RD_WORD])
				&& !(isYear(nextWords[Constants.INDEX_4TH_WORD]))) {
			return (firstWord + Constants.SPACE
					+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE + nextWords[Constants.INDEX_3RD_WORD])
					.trim();
		} else if (isValidDateFormat(firstWord + Constants.SPACE
				+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
				+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE
				+ nextWords[Constants.INDEX_4TH_WORD])
				&& !(isYear(nextWords[Constants.INDEX_5TH_WORD]))) {
			return (firstWord + Constants.SPACE
					+ nextWords[Constants.INDEX_2ND_WORD] + Constants.SPACE
					+ nextWords[Constants.INDEX_3RD_WORD] + Constants.SPACE + nextWords[Constants.INDEX_4TH_WORD])
					.trim();
		} else {
			return date;
		}
	}

	/**
	 * Removes task description after a date range, if any
	 * 
	 * @param dateRange
	 *            String containing a date range which may or may not be
	 *            followed by a task description
	 * @return date range as a String
	 */
	public String removeDescriptionFromDateRangeIfAny(String dateRange) {
		while (!dateRange.isEmpty() && !isDateRange(dateRange)) {
			dateRange = removeLastWord(dateRange);
		}
		return dateRange;
	}

	/**
	 * Removes the last word from a String
	 * 
	 * @param string
	 *            string from which last word is to be removed
	 * @return String without the last word. If there is only one word or less,
	 *         an empty string is returned.
	 */
	public String removeLastWord(String string) {
		string = cleanUp(string);
		int index = string.lastIndexOf(Constants.SPACE_CHAR);
		if (index == Constants.NOT_FOUND) {
			return "";
		} else {
			return string.substring(Constants.INDEX_BEGIN, index);
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
					|| (hasTime(word, nextWords)
							&& !(word.startsWith(Constants.STRING_ZERO) && word
									.endsWith(Constants.STRING_M_LOWERCASE)) && !isBoth24hrTimeAndYear(word))) {
				Pattern p = Pattern.compile(Constants.REGEX_WORD_START + word
						+ Constants.REGEX_WORD_END_SPACES_WORD_START
						+ nextWords[Constants.INDEX_2ND_WORD]
						+ Constants.REGEX_WORD_END);
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
	 * Checks if a string is both a year and a 24hr time format
	 * 
	 * @param string
	 *            string to be checked
	 * @return true if string is both a year and a 24hr time format, else false
	 */
	public boolean isBoth24hrTimeAndYear(String string) {
		TimeParser tp = new TimeParser();
		return isYear(string) && tp.is24hrTime(string);
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
					|| isValidDateFormat(word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD])
					|| isValidDateFormat(word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_3RD_WORD])
					|| isValidDateFormat(word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_3RD_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_4TH_WORD])
					|| isValidDateFormat(word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_3RD_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_4TH_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_5TH_WORD]);
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
					|| tp.isValidTimeFormat(word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD])
					|| tp.isValidTimeFormat(word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_3RD_WORD])
					|| tp.isValidTimeFormat(word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_3RD_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_4TH_WORD])
					|| tp.isValidTimeFormat(word + Constants.SPACE
							+ nextWords[Constants.INDEX_2ND_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_3RD_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_4TH_WORD]
							+ Constants.SPACE
							+ nextWords[Constants.INDEX_5TH_WORD]);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Checks is a word is a keyword (at, by etc.) but not a reserved word (days
	 * of the week, today, tomorrow, tmw)
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
		return index < Constants.LIMIT_ZERO || index >= getUserInput().length();
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
	 * 
	 * @param words
	 *            Array of words in userInput
	 * @param i
	 *            The index of the first word
	 * @return The next 4 words after index i. If there are less than 4 words,
	 *         the empty words are represented by empty strings
	 */
	public String[] getNext4Words(String[] words, int i) {
		String[] nextWords = { Constants.EMPTY_STRING, Constants.EMPTY_STRING,
				Constants.EMPTY_STRING, Constants.EMPTY_STRING };

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

	/**
	 * Checks whether a string is a date range in the following formats: (Space
	 * before and after 'to') <date1> to <date2> e.g. 24 Nov to 26 Nov, 24 to 26
	 * Nov 14 <date1>-<date2> e.g. 24/11-26/11, 24-26 Nov, 24-26 Nov 2014
	 * 
	 * @param string
	 *            string to be checked
	 * @return true if the string is a date range in any of the above formats,
	 *         else false
	 */
	public boolean isDateRange(String string) {
		boolean containsSeparator = false;
		String[] dates = {};

		try {
			if (string.contains(Constants.SEPARATOR_TO)) {
				dates = string.split(Constants.SEPARATOR_TO);
				dates = cleanUp(dates);
				containsSeparator = true;
			} else if (string.contains(Constants.SEPARATOR_DASH)) {
				dates = string.split(Constants.SEPARATOR_DASH);
				dates = cleanUp(dates);
				containsSeparator = true;
			}

			if (containsSeparator) {
				// start and end date are both valid dates, e.g. 24 Nov to 26
				// Nov, 24/11-26/11, sun-tue
				if ((isValidDate(dates[Constants.INDEX_START_DATE]) || isValidDay(dates[Constants.INDEX_START_DATE]))
						&& (isValidDate(dates[Constants.INDEX_END_DATE]) || isValidDay(dates[Constants.INDEX_END_DATE]))) {
					// if both are days of the week e.g sun-tue
					if (isDayOfTheWeek(dates[Constants.INDEX_START_DATE])
							&& isDayOfTheWeek(dates[Constants.INDEX_END_DATE])) {
						return true;
					}
					Date start = createDate(dates[Constants.INDEX_START_DATE]);
					Date end = createDate(dates[Constants.INDEX_END_DATE]);
					return start.isBefore(end);
				}
				// start date is the day only, end date is a valid date, e.g.
				// 24-26 Nov, 24 to 26 nov
				if (isValidDate(dates[Constants.INDEX_END_DATE])) {
					Date end = createDate(dates[Constants.INDEX_END_DATE]);
					return Integer.parseInt(dates[Constants.INDEX_START_DATE]) < end
							.getDay();
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isDayOfTheWeek(String string) {
		return hasWordInDictionary(Constants.DAYS_LONG, string)
				|| hasWordInDictionary(Constants.DAYS_SHORT, string);
	}

	public boolean isEitherTodayOrTmr(String string) {
		return hasWordInDictionary(Constants.DAYS_IMMEDIATE, string);
	}

	/**
	 * Given a string that represents a date range, creates an array of Date
	 * objects of start date and end date
	 * 
	 * @param string
	 *            date range
	 * @return array with start date and end date, null if invalid format
	 */
	public Date[] createDatesFromRange(String string) {
		string = string.trim().toLowerCase();
		String[] dates = {};

		try {
			if (!isDateRange(string)) {
				return null;
			}
			if (string.contains(Constants.SEPARATOR_TO)) {
				dates = cleanUp(string.split(Constants.SEPARATOR_TO));
			} else if (string.contains(Constants.SEPARATOR_DASH)) {
				dates = cleanUp(string.split(Constants.SEPARATOR_DASH));
			} else {
				return null;
			}

			// start and end date are both valid dates, e.g. 24 Nov to 26 Nov,
			// 24/11-26/11, sun-tue
			if ((isValidDate(dates[Constants.INDEX_START_DATE]) || isValidDay(dates[Constants.INDEX_START_DATE]))
					&& (isValidDate(dates[Constants.INDEX_END_DATE]) || isValidDay(dates[Constants.INDEX_END_DATE]))) {

				// if both are days of the week
				if (isDayOfTheWeek(dates[Constants.INDEX_START_DATE])
						&& isDayOfTheWeek(dates[Constants.INDEX_END_DATE])) {
					Date start = createDate(dates[Constants.INDEX_START_DATE]);

					int startDayIndex = getDayOfTheWeekIndex(dates[Constants.INDEX_START_DATE]);
					int endDayIndex = getDayOfTheWeekIndex(dates[Constants.INDEX_END_DATE]);
					int duration = endDayIndex - startDayIndex;

					if (duration <= Constants.LIMIT_ZERO) {
						duration += Constants.NUMBER_OF_DAYS_IN_A_WEEK;
					}
					Date end = addDaysToDate(start, duration);

					Date[] dateArr = { start, end };
					return dateArr;
				} else {
					Date start = createDate(dates[Constants.INDEX_START_DATE]);
					Date end = createDate(dates[Constants.INDEX_END_DATE]);
					Date[] dateArr = { start, end };
					return dateArr;
				}
			} else if (isValidDate(dates[Constants.INDEX_END_DATE])) {
				// start date is the day only, end date is a valid date, e.g.
				// 24-26 Nov, 24 to 26 nov
				Date end = createDate(dates[Constants.INDEX_END_DATE]);
				Date start = new Date(
						Integer.parseInt(dates[Constants.INDEX_START_DATE]),
						end.getMonth(), end.getYear4Digit());
				Date[] dateArr = { start, end };
				return dateArr;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Adds a specific number of days to a given date
	 * 
	 * @param date
	 *            date to which days are to be added
	 * @param amount
	 *            number of days to be added
	 * @return advanced date
	 */
	public Date addDaysToDate(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.set(date.getYear4Digit(), date.getMonth() - 1, date.getDay());
		cal.add(Calendar.DATE, amount);
		return new Date(cal.get(Calendar.DATE), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.YEAR));
	}
}
