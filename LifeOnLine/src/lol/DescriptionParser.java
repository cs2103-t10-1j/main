package lol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DescriptionParser {
	private String userInput;

	public DescriptionParser(String userInput) {
		this.userInput = userInput;
	}

	public String getUserInput() {
		return userInput;
	}

	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}

	/**
	 * Returns the description of a task
	 * 
	 * @return description of a task
	 * @throws Exception
	 */
	public String getDescription() throws Exception {
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

				// if word preceding the quote is not "at", the quote encloses a
				// description, else it contains a location
				if (!getWordBeforeQuote(startQuoteIndex).equalsIgnoreCase("at")) {
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
				
				// if word preceding the quote is not "at", the quote encloses a
				// description, else it contains a location
				if (!getWordBeforeQuote(firstQuoteStart).equalsIgnoreCase("at")) {
					return cleanUp(input.substring(firstQuoteStart + 1,
							firstQuoteEnd));
				}

				if (!getWordBeforeQuote(secondQuoteStart)
						.equalsIgnoreCase("at")) {
					return cleanUp(input.substring(secondQuoteStart + 1,
							secondQuoteEnd));
				}
			}
			
			// either no double quotes in input or no description found within
			// double quotes
			input = removeWordsWithinQuotes(input);
			System.out.println("Input without quotes: "+input);
			LocationParser lp = new LocationParser(input);
			String inputWithoutLocation = lp.getUserInputWithoutLocation();

			DateParser datep = new DateParser(inputWithoutLocation);
			String inputWithoutLocationAndDate = datep
					.getUserInputWithoutDueDate();

			TimeParser tp = new TimeParser(inputWithoutLocationAndDate);
			String inputWithoutLocationDateAndTime = tp
					.getUserInputWithoutTime();

			return removeFirstWord(inputWithoutLocationDateAndTime).trim();

		} catch (Exception e) {
			throw new Exception("Invaild number of quotes");
		}

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
	 * This method is called with user input without the command name and index.
	 * 
	 * @return
	 */
	public String getDescriptionForEdit() {
		cleanUp();
		// user input without command name and index
		// for example, for input "edit 3 do something today",
		// inputWithoutCommandAndIndex is "do something today"
		String inputWithoutCommandAndIndex = getUserInput();

		LocationParser lp = new LocationParser(inputWithoutCommandAndIndex);
		String inputWithoutLocation = lp.getUserInputWithoutLocation();

		DateParser datep = new DateParser(inputWithoutLocation);
		String inputWithoutLocationAndDate = datep.getUserInputWithoutDueDate();

		TimeParser tp = new TimeParser(inputWithoutLocationAndDate);
		String inputWithoutLocationDateAndTime = tp.getUserInputWithoutTime();

		if (inputWithoutLocationDateAndTime.isEmpty()) {
			return null;
		} else {
			return inputWithoutLocationDateAndTime.trim();
		}
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
	public static String cleanUp(String input) {
		input = input.trim();
		input = input.replaceAll("\\s+", " ");
		return input;
	}

	/**
	 * Removes the first word of input
	 * 
	 * @param input
	 *            string whose first word is to be removed
	 * @return input without the first word
	 */
	public String removeFirstWord(String input) {
		try {
			return input.split(" ", 2)[1];
		} catch (Exception e) {
			return input;
		}
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
	 * Removes double quotes, words within double quotes and the preceding keyword "at" if any
	 * @param input String from which words within quotes are to be removed
	 * @return  input without double quotes, words within double quotes and the preceding keyword "at" if any
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
			String wordsWithinQuotes = input.substring(startQuoteIndex + 1, endQuoteIndex);
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
			String wordsWithinFirstQuotes = input.substring(firstQuoteStart + 1, firstQuoteEnd);
			String stringToRemoveFromFirstQuotes = "\"" + wordsWithinFirstQuotes + "\"";
			
			if (getWordBeforeQuote(firstQuoteStart).equalsIgnoreCase("at")) {
				stringToRemoveFromFirstQuotes = "\\bat\\b\\s*" + stringToRemoveFromFirstQuotes;
			}

			
			// next 2 double quotes
			String wordsWithinSecondQuotes = input.substring(secondQuoteStart + 1, secondQuoteEnd);
			String stringToRemoveFromSecondQuotes = "\"" + wordsWithinSecondQuotes + "\"";
			
			if (getWordBeforeQuote(secondQuoteStart).equalsIgnoreCase("at")) {
				stringToRemoveFromSecondQuotes = "\\bat\\b\\s*" + stringToRemoveFromSecondQuotes;
			}
			
			String temp = cleanUp(input.replaceAll(stringToRemoveFromFirstQuotes, ""));
			temp = cleanUp(temp.replaceAll(stringToRemoveFromSecondQuotes, ""));
			return temp;
		}
	}
}
