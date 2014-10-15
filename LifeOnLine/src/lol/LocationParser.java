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

	public String getLocation() {
		cleanUp();
		return getParameterStartingAtIndex(getIndexOfAt() + 3);
	}

	/**
	 * Removes multiple spaces between words, leading and trailing spaces
	 * 
	 * @return string without extra spaces
	 */
	public String cleanUp() {
		String input = getUserInput();
		input = input.trim();
		input = input.replaceAll("\\s+", " ");
		setUserInput(input);
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
		int nextKeywordIndex = getIndexOfNextKeyword(index + 1);
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
	public int getIndexOfNextKeyword(int beginIndex) {
		String temp = getUserInput().substring(beginIndex);
		String[] words = temp.split(Constants.SPACE);
		int minIndex = Constants.NOT_FOUND;

		for (String word : words) {
			if (hasWordInDictionary(Constants.KEYWORDS, word)) {
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

	public boolean isIndexOutOfBounds(int index) {
		return index < 0 || index >= getUserInput().length();
	}

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
