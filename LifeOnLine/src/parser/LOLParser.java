/**
 * This class parses the user input and returns command name
 * 
 * add - returns a Task object with details of task to be added
 * delete, done - returns array of task indexes
 * edit - returns task index and Task object of the new task
 * show - returns Date object of the date specified after 'show' command
 * search - returns keywords to be searched
 * 
 */
package parser;

import java.util.logging.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lol.Constants;
import lol.Date;
import lol.Task;
import lol.Time;

public class LOLParser {
	private static Logger logger = Logger.getLogger(Constants.LOGGER_PARSER);
	static FileHandler handler;

	/*********** Methods to return task details ***************/

	/**
	 * Returns the command name. Example: add, delete, show, etc.
	 * 
	 * @param input
	 *            user input
	 * @return command name
	 */
	public static String getCommandName(String input) {
		String command = getFirstWord(input);

		if (hasWordInDictionary(Constants.DICTIONARY_ADD, command)) {
			return Constants.COMMAND_ADD;
		} else if (hasWordInDictionary(Constants.DICTIONARY_DELETE, command)) {
			return Constants.COMMAND_DELETE;
		} else if (hasWordInDictionary(Constants.DICTIONARY_SHOW, command)) {
			return Constants.COMMAND_SHOW;
		} else if (hasWordInDictionary(Constants.DICTIONARY_SEARCH, command)) {
			return Constants.COMMAND_SEARCH;
		} else if (hasWordInDictionary(Constants.DICTIONARY_EDIT, command)) {
			return Constants.COMMAND_EDIT;
		} else if (hasWordInDictionary(Constants.DICTIONARY_DONE, command)) {
			return Constants.COMMAND_DONE;
		} else if (hasWordInDictionary(Constants.DICTIONARY_NOT_DONE, command)) {
			return Constants.COMMAND_NOT_DONE;
		} else if (hasWordInDictionary(Constants.DICTIONARY_UNDO, command)) {
			return Constants.COMMAND_UNDO;
		} else if (hasWordInDictionary(Constants.DICTIONARY_REDO, command)) {
			return Constants.COMMAND_REDO;
		} else if (hasWordInDictionary(Constants.DICTIONARY_VIEW_HOMESCREEN,
				command)) {
			return Constants.COMMAND_VIEW_HOMESCREEN;
		} else if (hasWordInDictionary(Constants.DICTIONARY_EXIT, command)) {
			return Constants.COMMAND_EXIT;
		} else {
			return Constants.COMMAND_INVALID;
		}
	}

	/**
	 * Returns a Task object with details given in the parameter for add command
	 * 
	 * @param input
	 *            user input
	 * @return Task added
	 * @throws Exception
	 */
	public static Task getTask(String input) throws Exception {
		input = cleanUp(input);
		if (countWords(input) <= 1) {
			return null;
		}
		String originalInput = input;
		input = input.toLowerCase();

		DescriptionParser dp = new DescriptionParser(input);
		LocationParser lp = new LocationParser(input);
		DateParser dtp = new DateParser(input);
		TimeParser tp = new TimeParser(removeWordsWithinQuotes(input));

		String description = dp.getDescription();
		String location = lp.getLocation();
		Date date = dtp.getDueDate();
		Date endDate = dtp.getEndDate();
		Time startTime = tp.getStartTime();
		Time endTime = tp.getEndTime();

		if ((startTime != null || endTime != null) && date == null) {
			Time currentTime = tp.getCurrentTime();
			if (currentTime.isBefore(startTime)) {
				date = dtp.getTodaysDate();
			} else {
				date = dtp.addDaysToToday(1);
			}
		}
		if (description != null) {
			description = getParameterInOriginalCase(originalInput, description);
		}
		if (location != null) {
			location = getParameterInOriginalCase(originalInput, location);
		}
		return new Task(description, location, date, endDate, startTime,
				endTime);
	}

	/**
	 * Returns a Task object with details given in the parameter for edit
	 * command
	 * 
	 * @param input
	 *            <edit> + <index> + one or more modified or newly added
	 *            parameters for the task, e.g. edit 2 4 nov
	 * @param task
	 *            Task to be edited
	 * @return Task with modified or newly added parameters
	 */
	public static Task getEditTask(String input, Task task) throws Exception {
		try {
			input = cleanUp(input);
			if (countWords(input) <= 2) {
				throw new Exception(
						Constants.FEEDBACK_INVALID_PARAMETERS_FOR_EDIT);
			}

			String originalInput = input;
			input = input.toLowerCase();

			String inputWithoutCommandAndIndex = cleanUp(removeFirst2Words(input));
			Task newTask = task;

			String[] words = inputWithoutCommandAndIndex.split(Constants.SPACE);
			// if the user wants to delete parameters
			if (containsCommand(words, Constants.DICTIONARY_DELETE)) {
				for (int i = 0; i < words.length - 1; i++) {
					// if delete command is found
					if (hasWordInDictionary(Constants.DICTIONARY_DELETE,
							words[i])) {
						int index = i + 1;
						// delete all parameters after 'delete' keyword
						while (index < words.length
								&& hasWordInDictionary(
										Constants.DICTIONARY_PARAMETERS,
										words[index])) {
							switch (words[index]) {

							case Constants.PARAMETER_LOCATION:
							case Constants.PARAMETER_LOC:
								newTask.setLocation(null);
								break;

							case Constants.PARAMETER_DATE:
								newTask.setDueDate(null);
								newTask.setEndDate(null);
								newTask.setStartTime(null);
								newTask.setEndTime(null);
								break;

							case Constants.PARAMETER_START_DATE:
							case Constants.PARAMETER_SD:
								newTask.setDueDate(null);
								newTask.setEndDate(null);
								newTask.setStartTime(null);
								newTask.setEndTime(null);
								break;

							case Constants.PARAMETER_END_DATE:
							case Constants.PARAMETER_ED:
								newTask.setEndDate(null);
								break;

							case Constants.PARAMETER_TIME:
								newTask.setStartTime(null);
								newTask.setEndTime(null);
								break;

							case Constants.PARAMETER_START_TIME:
							case Constants.PARAMETER_ST:
								newTask.setStartTime(null);
								newTask.setEndTime(null);
								break;

							case Constants.PARAMETER_END_TIME:
							case Constants.PARAMETER_ET:
								newTask.setEndTime(null);
								break;

							}
							index++;
						}
					}
				}
				inputWithoutCommandAndIndex = removeDeleteParameters(inputWithoutCommandAndIndex);
			}

			DescriptionParser dp = new DescriptionParser(
					inputWithoutCommandAndIndex);
			LocationParser lp = new LocationParser(inputWithoutCommandAndIndex);
			DateParser dtp = new DateParser(inputWithoutCommandAndIndex);
			TimeParser tp = new TimeParser(inputWithoutCommandAndIndex);

			String description = dp.getDescriptionForEdit();
			String location = lp.getLocation();
			Date startDate = dtp.getDueDate();
			Date endDate = dtp.getEndDate();
			Time startTime = tp.getStartTime();
			Time endTime = tp.getEndTime();

			if (description != null) {
				description = getParameterInOriginalCase(originalInput,
						description);
				newTask.setDescription(description);
			}

			if (location != null) {
				location = getParameterInOriginalCase(originalInput, location);
				newTask.setLocation(location);
			}

			if (startDate != null) {
				newTask.setDueDate(startDate);
			}

			if (endDate != null) {
				newTask.setEndDate(endDate);
			}

			if (startTime != null) {
				newTask.setStartTime(startTime);

				if (newTask.getTaskDueDate() == null) {
					if (startTime.isAfter(tp.getCurrentTime())) {
						// due date is today
						newTask.setDueDate(dtp.getTodaysDate());
					} else {
						// due date is tomorrow
						newTask.setDueDate(dtp.addDaysToToday(1));
					}
				}
			}

			if (endTime != null) {
				newTask.setEndTime(endTime);
			}

			return newTask;
		} catch (Exception e) {
			throw new Exception(Constants.FEEDBACK_INVALID_PARAMETERS_FOR_EDIT);
		}
	}

	/**
	 * Returns index of task for delete/edit/done commands, counting from 1.
	 * Example: 'delete 1' returns 1. If index is not an integer or no the input
	 * does not contain an index, -1 is returned.
	 * 
	 * @param input
	 *            user input
	 * @return index of task if it is in the input and is an integer, else -1
	 */
	public static int getTaskIndex(String input) {
		try {
			handler = new FileHandler(Constants.LOGGER_FILE_NAME, true);
			logger.addHandler(handler);
			handler.setFormatter(new SimpleFormatter());
			return Integer
					.parseInt(input.split(Constants.SPACE)[Constants.INDEX_2ND_PART]);
		} catch (Exception e) {
			logger.log(Level.WARNING, Constants.ERROR_PROCESSING, e);
			return -1;
		}
	}

	/**
	 * Returns an array of task indices for delete/done commands, counting from
	 * 1.
	 * 
	 * @param input
	 *            user input
	 * @return array of task indices. If any index is not an integer or no index
	 *         is found, null is returned.
	 */
	public static int[] getTaskIndexArray(String input) {
		try {
			IndexParser ip = new IndexParser(input);
			return ip.getTaskIndexArray();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns the date entered after show command as a Date object
	 * 
	 * @param input
	 *            user input
	 * @return if the date entered after the command is a valid format then the
	 *         method returns a Date object, else returns null
	 */
	public static Date getDateForShowCommand(String input) {
		try {
			if (countWords(input) <= 1) {
				return null;
			}
			input = cleanUp(input);
			String date = removeFirstWord(input);
			DateParser dp = new DateParser();
			if (dp.isValidDateFormat(date)) {
				return dp.createDate(date);
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns keywords for search command
	 * 
	 * @param input
	 *            user input
	 * @return Keywords after search command. If the user input contains 1 or no
	 *         words, returns null.
	 */
	public static String getKeywordsForSearchCommand(String input) {
		try {
			input = cleanUp(input);
			if (countWords(input) <= 1) {
				return null;
			} else {
				return removeFirstWord(input);
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Return number of words in a string
	 * 
	 * @param input
	 *            Input string
	 * @return Number of words in input string
	 */
	public static int countWords(String input) {
		input = cleanUp(input);
		return input.split(Constants.SPACE).length;
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
		input = input.replaceAll(Constants.REGEX_ONE_OR_MORE_SPACES,
				Constants.SPACE);
		return input;
	}

	/**
	 * Remove the first word of the input and returns the resulting string
	 * 
	 * @param input
	 *            Input string
	 * @return Input string without the first word
	 */
	public static String removeFirstWord(String input) {
		try {
			return input.split(Constants.SPACE, 2)[Constants.INDEX_2ND_PART];
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Remove the first 2 words of the input and returns the resulting string
	 * 
	 * @param input
	 *            Input string
	 * @return Input string without the first 2 words
	 */
	public static String removeFirst2Words(String input) {
		try {
			return input.split(Constants.SPACE, 3)[Constants.INDEX_3RD_PART];
		} catch (Exception e) {
			return input;
		}
	}

	/**
	 * Return the first word of the input
	 * 
	 * @param input
	 *            Input string
	 * @return First word of input
	 */
	public static String getFirstWord(String input) {
		try {
			return input.split(Constants.SPACE, 2)[Constants.INDEX_BEGIN];
		} catch (Exception e) {
			return input;
		}
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

	public static boolean hasWordInDictionary(String[] dictionary, String word) {
		word = word.trim();
		for (int i = 0; i < dictionary.length; i++) {
			if (dictionary[i].equalsIgnoreCase(word)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether an array of Strings contains a command in a dictionary
	 * 
	 * @param words
	 *            array to be checked
	 * @param commandDictionary
	 *            all possible formats of the command to be checked
	 * @return true if the array of words contains the command, else false
	 */
	public static boolean containsCommand(String[] words,
			String[] commandDictionary) {
		for (int i = 0; i < words.length; i++) {
			if (hasWordInDictionary(commandDictionary, words[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes all delete keywords ('delete', 'd', etc.) and parameters
	 * ('location', 'loc', 'date', etc.) from the input string
	 * 
	 * @param input
	 *            String from which delete keywords and parameters are to be
	 *            removed
	 * @return input without delete keywords and parameters
	 */
	public static String removeDeleteParameters(String input) {
		// Delete the 'delete' keywords
		for (int i = 0; i < Constants.DICTIONARY_DELETE.length; i++) {
			String word = Constants.DICTIONARY_DELETE[i];
			input = cleanUp(input.replaceAll(Constants.REGEX_WORD_START + word
					+ Constants.REGEX_WORD_END, Constants.EMPTY_STRING));
		}

		// Delete parameters
		for (int i = 0; i < Constants.DICTIONARY_PARAMETERS.length; i++) {
			String word = Constants.DICTIONARY_PARAMETERS[i];
			input = cleanUp(input.replaceAll(Constants.REGEX_WORD_START + word
					+ Constants.REGEX_WORD_END, Constants.EMPTY_STRING));
		}
		return input;
	}

	/**
	 * Returns a parameter such as description or location in the case in which
	 * the user entered it
	 * 
	 * @param originalInput
	 *            user input
	 * @param lowercaseParameter
	 *            parameter in lowercase
	 * @return parameter in the original case
	 */
	public static String getParameterInOriginalCase(String originalInput,
			String lowercaseParameter) {
		Pattern p = Pattern.compile(lowercaseParameter,
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(originalInput);

		int start = 0, end = 0;
		if (m.find()) {
			start = m.start();
			end = m.end();
		}

		return originalInput.substring(start, end);
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
	public static String removeWordsWithinQuotes(String input) {
		input = cleanUp(input);

		int countDoubleQuotes = countNumberOfDoubleQuotes(input);
		Pattern p = Pattern.compile(Constants.DOUBLE_QUOTE);
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
			String stringToRemove = Constants.DOUBLE_QUOTE + wordsWithinQuotes
					+ Constants.DOUBLE_QUOTE;

			if (getWordBeforeQuote(startQuoteIndex, input).equalsIgnoreCase(
					Constants.KEYWORDS[Constants.INDEX_KEYWORD_AT])) {
				stringToRemove = Constants.REGEX_AT_WITH_SPACES
						+ stringToRemove;
			}
			return cleanUp(input.replaceAll(stringToRemove,
					Constants.EMPTY_STRING));
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
			String stringToRemoveFromFirstQuotes = Constants.DOUBLE_QUOTE
					+ wordsWithinFirstQuotes + Constants.DOUBLE_QUOTE;

			if (getWordBeforeQuote(firstQuoteStart, input).equalsIgnoreCase(
					Constants.KEYWORDS[Constants.INDEX_KEYWORD_AT])) {
				stringToRemoveFromFirstQuotes = Constants.REGEX_AT_WITH_SPACES
						+ stringToRemoveFromFirstQuotes;
			}

			// next 2 double quotes
			String wordsWithinSecondQuotes = input.substring(
					secondQuoteStart + 1, secondQuoteEnd);
			String stringToRemoveFromSecondQuotes = Constants.DOUBLE_QUOTE
					+ wordsWithinSecondQuotes + Constants.DOUBLE_QUOTE;

			if (getWordBeforeQuote(secondQuoteStart, input).equalsIgnoreCase(
					Constants.KEYWORDS[Constants.INDEX_KEYWORD_AT])) {
				stringToRemoveFromSecondQuotes = Constants.REGEX_AT_WITH_SPACES
						+ stringToRemoveFromSecondQuotes;
			}

			String temp = cleanUp(input.replaceAll(
					stringToRemoveFromFirstQuotes, Constants.EMPTY_STRING));
			temp = cleanUp(temp.replaceAll(stringToRemoveFromSecondQuotes,
					Constants.EMPTY_STRING));
			return temp;
		}
	}

	/**
	 * Returns the number of double quotes(") in the userInput
	 * 
	 * @param input
	 *            userInput
	 * @return number of double quotes(") in the userInput
	 */
	public static int countNumberOfDoubleQuotes(String input) {
		Pattern p = Pattern.compile(Constants.DOUBLE_QUOTE);
		Matcher m = p.matcher(input);

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
	 * @param input
	 *            user input
	 * @return word immediately before the quotation mark. If there are no words
	 *         before the quotation mark, an empty string is returned.
	 */
	public static String getWordBeforeQuote(int indexQuote, String input) {
		String inputUntilQuote = cleanUp(input.substring(Constants.INDEX_BEGIN,
				indexQuote));
		return getLastWord(inputUntilQuote);
	}

	/**
	 * Returns the last word of a string
	 * 
	 * @param str
	 *            string whose last word is to be returned
	 * @return last word of the string
	 */
	public static String getLastWord(String str) {
		try {
			str = cleanUp(str);
			int indexOfLastSpace = str.lastIndexOf(Constants.SPACE_CHAR);

			if (indexOfLastSpace >= Constants.LIMIT_ZERO) {
				return str.substring(indexOfLastSpace).trim();
			} else {
				assert indexOfLastSpace == Constants.NOT_FOUND;
				return str;
			}
		} catch (Exception e) {
			return Constants.EMPTY_STRING;
		}
	}
}
