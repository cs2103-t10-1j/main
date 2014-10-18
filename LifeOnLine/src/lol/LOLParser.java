/**
 * NOT DONE YET
 * Parses the user input
 * User input formats:
 * 
 * add <description>\<location>\<due date>
 * start and end time not implemented yet
 */
package lol;

import java.util.logging.*;


public class LOLParser {
	private static Logger logger = Logger.getLogger("LOLParser");
	static FileHandler handler;

	
	/*********** Methods to return task details ***************/

	/**
	 * Returns the command name. 
	 * Example: add, delete, show, etc.
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
		}  else if (hasWordInDictionary(Constants.DICTIONARY_REDO, command)) {
			return Constants.COMMAND_REDO;
		}  else if (hasWordInDictionary(Constants.DICTIONARY_EXIT, command)) {
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
		
		return new Task(description, location,
				date, startTime, endTime);
	}
	
	public static Task getEditTask(String s) {
		return null;
	}
	
	/**
	 * Returns index of task for delete/edit/done commands, counting from 1. 
	 * Example: 'delete 1' returns 1. 
	 * If index is not an integer or no the input does not contain an index, -1 is returned.
	 * @param input   user input
	 * @return  index of task if it is in the input and is an integer, else -1
	 */
	public static int getTaskIndex(String input) {
		try {
			handler = new FileHandler("logfile%g.txt", true);
			logger.addHandler(handler);
			handler.setFormatter(new SimpleFormatter());
			//logger.log(Level.INFO, "going to start processing");
			return Integer.parseInt(input.split(" ")[1]);
		} catch (Exception e) {
			//logger.log(Level.WARNING, "processing error", e);
			return -1;
		}
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
	 * Return number of words in a string
	 * 
	 * @param input
	 *            Input string
	 * @return Number of words in input string
	 */
	public static int countWords(String input) {
		input = input.trim();

		if (input.isEmpty()) { // Zero words
			return 0;
		}

		int numWords = 1; // There is at least one word in the string

		for (int i = 0; i < input.length(); i++) {
			// Disregard multiple spaces between 2 words
			if (input.charAt(i) == ' ' && input.charAt(i - 1) != ' ') {
				numWords++;
			}
		}
		return numWords;
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
