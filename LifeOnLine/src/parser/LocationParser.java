/**
 * This class parses the location of an input.
 */
package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lol.Constants;

public class LocationParser {
	/************* Attribute ***************/
	private String userInput;

	/************* Constructor ***************/
	public LocationParser(String userInput) {
		this.userInput = userInput;
	}

	/************* Accessor ***************/
	public String getUserInput() {
		return userInput;
	}

	/************* Mutator ***************/
	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}

	/************* Other methods ***************/
	/**
	 * Returns the location from the user input
	 * 
	 * @return location if it exists, else null
	 */
	public String getLocation() {
		try {
			cleanUp();
			String input = getUserInput();

			// check for double quotes
			Pattern p = Pattern.compile("\"");
			Matcher m = p.matcher(input);

			int countDoubleQuotes = countNumberOfDoubleQuotes();

			// only location and description can be enclosed in double quotes
			// more than 4 quotes and odd number of quotes are invalid
			if (countDoubleQuotes % 2 == 1 || countDoubleQuotes > 4) {
				throw new Exception("Invaild number of quotes");
			}

			if (countDoubleQuotes == 2) { // one parameter within quotes
				int startQuoteIndex = 0, endQuoteIndex = 0, count = 0;
				while (m.find()) {
					count++;
					if (count == 1) {
						startQuoteIndex = m.start();
					} else {
						assert count == 2;
						endQuoteIndex = m.start();
					}
				}

				// if word preceding the quote is "at", the quote encloses a
				// location, else it contains a description
				if (getWordBeforeQuote(startQuoteIndex).equalsIgnoreCase("at")) {
					return cleanUp(input.substring(startQuoteIndex + 1,
							endQuoteIndex));
				}

			} else if (countDoubleQuotes == 4) { // two parameters within quotes

				int firstQuoteStart = 0, firstQuoteEnd = 0, secondQuoteStart = 0, secondQuoteEnd = 0, count = 0;
				while (m.find()) {
					count++;
					if (count == 1) {
						firstQuoteStart = m.start();
					} else if (count == 2) {
						firstQuoteEnd = m.start();
					} else if (count == 3) {
						secondQuoteStart = m.start();
					} else {
						assert count == 4;
						secondQuoteEnd = m.start();
					}
				}

				// if word preceding the quote is "at", the quote encloses a
				// location, else it contains a description
				if (getWordBeforeQuote(firstQuoteStart).equalsIgnoreCase("at")) {
					return cleanUp(input.substring(firstQuoteStart + 1,
							firstQuoteEnd));
				}

				if (getWordBeforeQuote(secondQuoteStart).equalsIgnoreCase("at")) {
					return cleanUp(input.substring(secondQuoteStart + 1,
							secondQuoteEnd));
				}
			}

			// either no double quotes in input or no location found within
			// double quotes
			input = removeWordsWithinQuotes(input);
			setUserInput(input);

			TimeParser tp = new TimeParser();
			DateParser dp = new DateParser();

			if (countNumberOfAt(input) == 1) {
				String parameter = getParameterStartingAtIndex(getIndexOfAt()
						+ Constants.KEYWORDS[Constants.INDEX_KEYWORD_AT]
								.length() + 1);
				parameter = removeDescriptionAfterTimeIfAny(parameter);

				if (!tp.isValidTimeFormat(parameter)
						&& !dp.isValidDateFormat(parameter)) {
					return parameter;
				} else {
					return null;
				}
			} else {
				Pattern pAt = Pattern
						.compile(Constants.REGEX_KEYWORDS[Constants.INDEX_KEYWORD_AT]);
				Matcher mAt = pAt.matcher(getUserInput());

				while (mAt.find()) {
					String parameter = getParameterStartingAtIndex(mAt.end() + 1);
					parameter = removeDescriptionAfterTimeIfAny(parameter);
					if (!tp.isValidTimeFormat(parameter)
							&& !dp.isValidDateFormat(parameter)) {
						return parameter;
					}
				}
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns user input excluding the location and the keyword "at" preceding
	 * it
	 * 
	 * @return user input excluding the location and the keyword "at" preceding
	 *         it
	 */
	public String getUserInputWithoutLocation() {
		cleanUp();
		String output = userInput.replaceAll("\\bat\\b\\s\\b" + getLocation()
				+ "\\b", Constants.EMPTY_STRING);
		return cleanUp(output);
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
	 * Counts number of "at" in input
	 * 
	 * @param input  string in which number of 'at's are to be counted
	 * @return number of "at" in userInput
	 */
	public int countNumberOfAt(String input) {
		Pattern p = Pattern
				.compile(Constants.REGEX_KEYWORDS[Constants.INDEX_KEYWORD_AT]);
		Matcher m = p.matcher(input);

		int count = 0;
		while (m.find()) {
			count++;
		}
		return count;
	}

	/**
	 * Returns a parameter (e.g location, due date) starting from 'index' till
	 * the occurrence of another reserved word or the end of the string,
	 * whichever is earlier
	 * 
	 * @param index
	 *            index of userInput at which the parameter to be returned
	 *            starts
	 * @return parameter starting from index
	 */
	public String getParameterStartingAtIndex(int index) {
		if (isIndexOutOfBounds(index)) {
			return null;
		}
		String stringToCheck = getUserInput().substring(index);
		int nextSpaceIndex = stringToCheck.indexOf(' ');
		
		int nextSpaceIndexInOriginalString;
		
		if (nextSpaceIndex == Constants.NOT_FOUND) {
			nextSpaceIndexInOriginalString = Constants.NOT_FOUND;
		} else {
			nextSpaceIndexInOriginalString = nextSpaceIndex + index;
		}
		
		int nextKeywordIndex;
		
		if (nextSpaceIndexInOriginalString == Constants.NOT_FOUND) {
			nextKeywordIndex = Constants.NOT_FOUND;
		} else {
			nextKeywordIndex = getIndexOfNextReservedWord(nextSpaceIndexInOriginalString);
		}
		
		if (nextKeywordIndex == Constants.NOT_FOUND) {
			return getUserInput().substring(index).trim();
		} else {
			assert nextKeywordIndex > 0;
			return getUserInput().substring(index, nextKeywordIndex).trim();
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
			// find next 4 words because date and time formats can have at most
			// 5 words
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

			if (isReservedWord(word)
					|| hasDate(word, nextWords)
					|| (hasTime(word, nextWords) && !(word.startsWith("0") && word
							.endsWith("m")))) {
				Pattern p = Pattern.compile("\\b" + word + "\\b");
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
	 * Returns the index of first occurrence of the word "at" in userInput
	 * 
	 * @return index of first occurrence of the word "at" in userInput
	 */
	public int getIndexOfAt() {
		Pattern p = Pattern
				.compile(Constants.REGEX_KEYWORDS[Constants.INDEX_KEYWORD_AT]);
		Matcher m = p.matcher(userInput);

		if (m.find()) {
			return m.start();
		} else {
			return Constants.NOT_FOUND;
		}
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
	 * Returns the number of double quotes(") in the userInput
	 * 
	 * @return number of double quotes(") in the userInput
	 */
	public int countNumberOfDoubleQuotes() {
		Pattern p = Pattern.compile("\"");
		Matcher m = p.matcher(getUserInput());

		int count = 0;
		while (m.find()) {
			count++;
		}
		return count;
	}

	/**
	 * Returns the word immediately before the quotation mark whose index is
	 * specified
	 * 
	 * @param indexQuote
	 *            index of quotation mark
	 * @return word immediately before the quotation mark. If there are no words
	 *         before the quotation mark, an empty string is returned.
	 */
	public String getWordBeforeQuote(int indexQuote) {
		String inputUntilQuote = cleanUp(getUserInput()
				.substring(0, indexQuote));
		return getLastWord(inputUntilQuote);
	}

	/**
	 * Returns the last word of a string
	 * 
	 * @param str
	 *            string whose last word is to be returned
	 * @return last word of the string
	 */
	public String getLastWord(String str) {
		try {
			str = cleanUp(str);
			int indexOfLastSpace = str.lastIndexOf(' ');

			if (indexOfLastSpace >= 0) {
				return str.substring(indexOfLastSpace).trim();
			} else {
				assert indexOfLastSpace == -1;
				return str;
			}
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Removes double quotes, words within double quotes and the preceding
	 * keyword "at" if any
	 * 
	 * @param input
	 *            String from which words within quotes are to be removed
	 * @return input without double quotes, words within double quotes and the
	 *         preceding keyword "at" if any
	 */
	public String removeWordsWithinQuotes(String input) {
		cleanUp();
		input = cleanUp(input);

		int countDoubleQuotes = countNumberOfDoubleQuotes();
		Pattern p = Pattern.compile("\"");
		Matcher m = p.matcher(input);

		if (countDoubleQuotes == 0) {
			return input;
		} else if (countDoubleQuotes == 2) {
			int startQuoteIndex = 0, endQuoteIndex = 0, count = 0;
			while (m.find()) {
				count++;
				if (count == 1) {
					startQuoteIndex = m.start();
				} else {
					assert count == 2;
					endQuoteIndex = m.start();
				}
			}
			String wordsWithinQuotes = input.substring(startQuoteIndex + 1,
					endQuoteIndex);
			String stringToRemove = "\"" + wordsWithinQuotes + "\"";

			if (getWordBeforeQuote(startQuoteIndex).equalsIgnoreCase("at")) {
				stringToRemove = "\\bat\\b\\s*" + stringToRemove;
			}
			return cleanUp(input.replaceAll(stringToRemove, ""));
		} else {
			assert countDoubleQuotes == 4;
			int firstQuoteStart = 0, firstQuoteEnd = 0, secondQuoteStart = 0, secondQuoteEnd = 0, count = 0;
			while (m.find()) {
				count++;
				if (count == 1) {
					firstQuoteStart = m.start();
				} else if (count == 2) {
					firstQuoteEnd = m.start();
				} else if (count == 3) {
					secondQuoteStart = m.start();
				} else {
					assert count == 4;
					secondQuoteEnd = m.start();
				}
			}

			// first 2 double quotes
			String wordsWithinFirstQuotes = input.substring(
					firstQuoteStart + 1, firstQuoteEnd);
			String stringToRemoveFromFirstQuotes = "\""
					+ wordsWithinFirstQuotes + "\"";

			if (getWordBeforeQuote(firstQuoteStart).equalsIgnoreCase("at")) {
				stringToRemoveFromFirstQuotes = "\\bat\\b\\s*"
						+ stringToRemoveFromFirstQuotes;
			}

			// next 2 double quotes
			String wordsWithinSecondQuotes = input.substring(
					secondQuoteStart + 1, secondQuoteEnd);
			String stringToRemoveFromSecondQuotes = "\""
					+ wordsWithinSecondQuotes + "\"";

			if (getWordBeforeQuote(secondQuoteStart).equalsIgnoreCase("at")) {
				stringToRemoveFromSecondQuotes = "\\bat\\b\\s*"
						+ stringToRemoveFromSecondQuotes;
			}

			String temp = cleanUp(input.replaceAll(
					stringToRemoveFromFirstQuotes, ""));
			temp = cleanUp(temp.replaceAll(stringToRemoveFromSecondQuotes, ""));
			return temp;
		}
	}

	/**
	 * Removes task description after a time, if any
	 * 
	 * @param date
	 *            String containing a time which may or may not be followed by a
	 *            task description
	 * @return due date as a string
	 */
	public String removeDescriptionAfterTimeIfAny(String time) {
		String[] words = time.split(" ");
		String firstWord = words[0];
		String[] nextWords = getNext4Words(words, 0);
		TimeParser tp = new TimeParser();

		if (tp.isValidTimeFormat(firstWord)
				&& !(isPartOfTimeFormat(nextWords[0]))) {
			return firstWord.trim();
		} else if (tp.isValidTimeFormat(firstWord + " " + nextWords[0])
				&& !(isPartOfTimeFormat(nextWords[1]))) {
			return (firstWord + " " + nextWords[0]).trim();
		} else if (tp.isValidTimeFormat(firstWord + " " + nextWords[0] + " "
				+ nextWords[1])
				&& !(isPartOfTimeFormat(nextWords[2]))) {
			return (firstWord + " " + nextWords[0] + " " + nextWords[1]).trim();
		} else if (tp.isValidTimeFormat(firstWord + " " + nextWords[0] + " "
				+ nextWords[1] + " " + nextWords[2])
				&& !(isPartOfTimeFormat(nextWords[3]))) {
			return (firstWord + " " + nextWords[0] + " " + nextWords[1] + " " + nextWords[2])
					.trim();
		} else {
			return (firstWord + " " + nextWords[0] + " " + nextWords[1] + " "
					+ nextWords[2] + " " + nextWords[3]).trim();
		}
	}

	/**
	 * Returns true if a word is a part of a time format
	 * 
	 * @param word
	 *            word to be checked
	 * @return true if the word is a part of a time format, else false
	 */
	public boolean isPartOfTimeFormat(String word) {
		word = word.trim();
		return word.equalsIgnoreCase("am") || word.equalsIgnoreCase("pm")
				|| word.equalsIgnoreCase("to") || word.equalsIgnoreCase("-");
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

}
