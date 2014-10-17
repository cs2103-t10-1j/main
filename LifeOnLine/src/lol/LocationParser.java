/**
 * This class parses the location of an input.
 */
package lol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationParser {
	private String userInput;

	public LocationParser(String userInput) {
		this.userInput = userInput;
	}

	public String getUserInput() {
		return userInput;
	}

	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}

	/**
	 * Returns the location from the user input
	 * 
	 * @return location if it exists, else null
	 */
	public String getLocation() {
		try {
			TimeParser tp = new TimeParser();
			DateParser dp = new DateParser();
			cleanUp();

			if (countNumberOfAt() == 1) {
				String parameter = getParameterStartingAtIndex(getIndexOfAt()
						+ Constants.KEYWORDS[Constants.INDEX_KEYWORD_AT]
								.length() + 1);
				if (!tp.isValidTimeFormat(parameter)
						&& !dp.isValidDateFormat(parameter)) {
					return parameter;
				} else {
					return null;
				}
			} else {
				Pattern p = Pattern
						.compile(Constants.REGEX_KEYWORDS[Constants.INDEX_KEYWORD_AT]);
				Matcher m = p.matcher(getUserInput());

				while (m.find()) {
					String parameter = getParameterStartingAtIndex(m.end() + 1);
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
	 * Returns user input excluding the location and the keyword "at" preceding it
	 * @return   user input excluding the location and the keyword "at" preceding it
	 */
	public String getUserInputWithoutLocation() {
		cleanUp();
		String output = userInput.replaceAll("\\bat\\b\\s\\b" + getLocation() + "\\b", Constants.EMPTY_STRING);
		return cleanUp(output);
	}

	/**
	 * Removes multiple spaces between words, leading and trailing spaces in the userInput
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
	 * Counts number of "at" in userInput
	 * 
	 * @return number of "at" in userInput
	 */
	public int countNumberOfAt() {
		Pattern p = Pattern
				.compile(Constants.REGEX_KEYWORDS[Constants.INDEX_KEYWORD_AT]);
		Matcher m = p.matcher(getUserInput());

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
	 *            index of userInput at which the paramter to be returned starts
	 * @return parameter starting from index
	 */
	public String getParameterStartingAtIndex(int index) {
		if (isIndexOutOfBounds(index)) {
			return null;
		}
		int nextKeywordIndex = getIndexOfNextReservedWord(index + 1);
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

			if (isReservedWord(word) || hasDate(word, nextWords)
					|| hasTime(word, nextWords)) {
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
}
