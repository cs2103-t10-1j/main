/**
 * This class parses the location of an input.
 */
package lol;

import java.util.Arrays;
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
		try {
			TimeParser tp = new TimeParser();
			DateParser dp = new DateParser();
			cleanUp();
			
			int countAts = countNumberOfAt();
			
			if (countAts == 1) {
				String parameter = getParameterStartingAtIndex(getIndexOfAt() + 3);
				if (!tp.isValidTimeFormat(parameter) && !dp.isValidDateFormat(parameter)) {
					return parameter;
				} else {
					return null;
				}
			} else {
				Pattern p = Pattern
						.compile("\\bat\\b");
				Matcher m = p.matcher(getUserInput());

				while (m.find()) {
					String parameter = getParameterStartingAtIndex(m.end() + 1);
					if (!tp.isValidTimeFormat(parameter) && !dp.isValidDateFormat(parameter)) {
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
		
		System.out.println("******************************");
		System.out.println(userInput);
		System.out.println("******************************");

		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			System.out.println("word: " + word);
			String[] nextWords = { "", "", "", "" }; // next 4 words
			
			if (i < words.length - 4) {
				int index = 0;
				while (index < 4) {
					nextWords[index] = words[i + 1 + index];
					index++;
				}
				System.out.println("<4 " + Arrays.toString(nextWords));
			} else if (i < words.length - 3) {
				int index = 0;
				while (index < 3) {
					nextWords[index] = words[i + 1 + index];
					index++;
				}
				System.out.println("<3 " + Arrays.toString(nextWords));
			} else if (i < words.length - 2) {
				int index = 0;
				while (index < 2) {
					nextWords[index] = words[i + 1 + index];
					index++;
				}
				System.out.println("<2 " + Arrays.toString(nextWords));
			} else if (i < words.length - 1) {
				int index = 0;
				while (index < 1) {
					nextWords[index] = words[i + 1 + index];
					index++;
				}
				System.out.println("<1 " + Arrays.toString(nextWords));
			} else {
				System.out.println("last " + Arrays.toString(nextWords));
			}
			
			if (isReservedWord(word) || hasDate(word, nextWords) || hasTime(word, nextWords)) {
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
	
	public boolean hasDate(String word, String[] nextWords) {
		DateParser dp = new DateParser();
		return dp.isValidDateFormat(word) || dp.isValidDateFormat(word + " " + nextWords[0]) || dp.isValidDateFormat(word + " " + nextWords[0] + " " + nextWords[1]) || dp.isValidDateFormat(word + " " + nextWords[0] + " " + nextWords[1] + " " + nextWords[2]) || dp.isValidDateFormat(word + " " + nextWords[0] + " " + nextWords[1] + " " + nextWords[2] + " " + nextWords[3]);
	}
	
	public boolean hasTime(String word, String[] nextWords) {
		TimeParser tp = new TimeParser();
		return tp.isValidTimeFormat(word) || tp.isValidTimeFormat(word + " " + nextWords[0]) || tp.isValidTimeFormat(word + " " + nextWords[0] + " " + nextWords[1]) || tp.isValidTimeFormat(word + " " + nextWords[0] + " " + nextWords[1] + " " + nextWords[2]) || tp.isValidTimeFormat(word + " " + nextWords[0] + " " + nextWords[1] + " " + nextWords[2] + " " + nextWords[3]);
	}
	/**
	 * Checks is a word is a keyword (at, by etc.) or reserved word (days of the week, today, tomorrow, tmw)
	 * @param word   word to be checked
	 * @return  true if a word is a keyword or reserved word, else false
	 */
	public boolean isReservedWord(String word) {
		return hasWordInDictionary(Constants.KEYWORDS, word) || hasWordInDictionary(Constants.DAYS_IMMEDIATE, word) || hasWordInDictionary(Constants.DAYS_LONG, word) || hasWordInDictionary(Constants.DAYS_SHORT, word);
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
