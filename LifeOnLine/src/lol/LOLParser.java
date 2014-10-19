/**
 * Parses the user input and returns command name
 * add - returns a Task object with details of task to be added
 * delete, done - returns task index
 * edit - returns task index and Task object of the new task
 * show - returns Date object of the date specified after 'show' command
 * search - returns keywords to be searched
 */
package lol;

import java.util.logging.*;

public class LOLParser {
	private static Logger logger = Logger.getLogger("LOLParser");
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
	 */
	public static Task getTask(String input) {
		DescriptionParser dp = new DescriptionParser(input);
		LocationParser lp = new LocationParser(input);
		DateParser dtp = new DateParser(input);
		TimeParser tp = new TimeParser(input);

		String description = dp.getDescription();
		String location = lp.getLocation();
		Date date = dtp.getDueDate();
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
		return new Task(description, location, date, startTime, endTime);
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
	public static Task getEditTask(String input, Task task) {
		try {
			String inputWithoutCommandAndIndex = removeFirst2Words(input);
			DescriptionParser dp = new DescriptionParser(
					inputWithoutCommandAndIndex);
			LocationParser lp = new LocationParser(inputWithoutCommandAndIndex);
			DateParser dtp = new DateParser(inputWithoutCommandAndIndex);
			TimeParser tp = new TimeParser(inputWithoutCommandAndIndex);

			String description = dp.getDescriptionForEdit();
			String location = lp.getLocation();
			Date date = dtp.getDueDate();
			Time startTime = tp.getStartTime();
			Time endTime = tp.getEndTime();

			Task newTask = task;
			if (description != null) {
				newTask.setDescription(description);
			}

			if (location != null) {
				newTask.setLocation(location);
			}

			if (date != null) {
				newTask.setDueDate(date);
			}

			if (startTime != null) {
				newTask.setStartTime(startTime);
			}

			if (endTime != null) {
				newTask.setEndTime(endTime);
			}

			return newTask;
		} catch (Exception e) {
			return task;
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
			handler = new FileHandler("logfile%g.txt", true);
			logger.addHandler(handler);
			handler.setFormatter(new SimpleFormatter());
			// logger.log(Level.INFO, "going to start processing");
			return Integer.parseInt(input.split(" ")[1]);
		} catch (Exception e) {
			// logger.log(Level.WARNING, "processing error", e);
			return -1;
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
		return input.split(" ").length;
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
	 * Remove the first word of the input and returns the resulting string
	 * 
	 * @param input
	 *            Input string
	 * @return Input string without the first word
	 */
	public static String removeFirstWord(String input) {
		try {
			return input.split(" ", 2)[1];
		} catch (Exception e) {
			return input;
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
			return input.split(" ", 3)[2];
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
			return input.split(" ", 2)[0];
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
}
