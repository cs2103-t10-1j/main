/**
 * NOT DONE YET
 * Parses the user input
 * User input formats:
 * 
 * add <description>\<location>\<due date>
 * start and end time not implemented yet
 */
package lol;


public class LOLParser {
	
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
		return new Task(getDescription(input), getLocation(input),
				getDueDate(input), getStartTime(input), getEndTime(input));
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
			return Integer.parseInt(input.split(" ")[1]);
		} catch (Exception e) {
			return -1;
		}
	}
	
	/**
	 * Returns a Task object with details given in the parameter for edit command
	 * 
	 * @param input
	 *            user input
	 * @return Task added
	 */
	public static Task getEditTask(String input) {
		return getTask(removeFirstWord(input));
	}
	
	/**
	 * Returns description of task for add command. Example: for input "add attend meeting \
	 * meeting room 3 \ 16 Oct \ 2pm" returns "attend meeting". Precondition: Task
	 * has a description
	 * 
	 * @param input
	 *            user input
	 * @return task description
	 */
	public static String getDescription(String input) {
		String details = removeFirstWord(input); // remove command
		String[] detailsArray = details.split(Constants.SEPARATOR);
		detailsArray = removeSpaces(detailsArray);
		return detailsArray[Constants.INDEX_BEGIN];
	}

	/**
	 * Returns location of task for add command. Example: for input " add attend meeting \
	 * meeting room 3 \ 16 Oct \ 2pm" returns "meeting room 3"
	 * 
	 * @param input
	 *            user input
	 * @return location of task if it exists, else null
	 */
	public static String getLocation(String input) {
		String details = removeFirstWord(input); // remove command
		String[] detailsArray = details.split(Constants.SEPARATOR);
		detailsArray = removeSpaces(detailsArray);
		DateParser dp = new DateParser();
		TimeParser tp = new TimeParser();

		// 1st element is the description
		// If only 1 element or 2nd element is due date then no location
		if (detailsArray.length < 2 || dp.isValidDate(detailsArray[1])
				|| dp.isValidDay(detailsArray[1]) || tp.is12hrTime(detailsArray[1]) || tp.is24hrTime(detailsArray[1]) || tp.isTimeRange(detailsArray[1])) {
			return null;
		} else {
			return detailsArray[1];
		}
	}

	/**
	 * Returns due date of task as a date object for add command.
	 * 
	 * @param input
	 *            user input
	 * @return due date if it exists, else null
	 */
	public static Date getDueDate(String input) {
		String details = removeFirstWord(input); // remove command
		String[] detailsArray = details.split(Constants.SEPARATOR);
		detailsArray = removeSpaces(detailsArray);
		DateParser dp = new DateParser();

		// 1st element is the description
		// Check from 2nd element onwards
		for (int i = 1; i < detailsArray.length; i++) {
			if (dp.isValidDate(detailsArray[i]) || dp.isValidDay(detailsArray[i])) {
				return dp.createDate(detailsArray[i]);
			}
		}
		
		if (getStartTime(input) != null) {
			return dp.getTodaysDate();
		}
		return null;
	}

	/**
	 * Returns start time of task as a time object for add command.
	 * 
	 * @param input
	 *            user input
	 * @return start time if it exists, else null
	 */
	public static Time getStartTime(String input) {
		String details = removeFirstWord(input); // remove command
		String[] detailsArray = details.split(Constants.SEPARATOR);
		detailsArray = removeSpaces(detailsArray);
		TimeParser tp = new TimeParser();

		// 1st element is the description
		// Check from 2nd element onwards
		for (int i = 1; i < detailsArray.length; i++) {
			if (tp.is12hrTime(detailsArray[i])) {
				return tp.create12hrTime(detailsArray[i]);
			} else if (tp.is24hrTime(detailsArray[i])) {
				return new Time(detailsArray[i]);
			} else if (tp.isTimeRange(detailsArray[i])) {
				return tp.createStartTimeFromRange(detailsArray[i]);
			}
		}
		return null;
	}

	/**
	 * Returns end time of task as a time object for add command.
	 * 
	 * @param input
	 *            user input
	 * @return end time if it exists, else null
	 */
	public static Time getEndTime(String input) {
		String details = removeFirstWord(input); // remove command
		String[] detailsArray = details.split(Constants.SEPARATOR);
		detailsArray = removeSpaces(detailsArray);
		TimeParser tp = new TimeParser();

		// 1st element is the description
		// Check from 2nd element onwards
		for (int i = 1; i < detailsArray.length; i++) {
			if (tp.isTimeRange(detailsArray[i])) {
				return tp.createEndTimeFromRange(detailsArray[i]);
			}
		}
		return null;
	}
	
	/**
	 * Returns description of task for edit command. Precondition: Task
	 * has a description
	 * 
	 * @param input
	 *            user input
	 * @return task description
	 */
	public static String getEditDescription(String input) {
		return getDescription(removeFirstWord(input));
	}
	
	/**
	 * Returns location of task for edit command
	 * 
	 * @param input
	 *            user input
	 * @return location of task if it exists, else null
	 */
	public static String getEditLocation(String input) {
		return getLocation(removeFirstWord(input));
	}
	
	/**
	 * Returns due date of task as a date object for edit command.
	 * 
	 * @param input
	 *            user input
	 * @return due date if it exists, else null
	 */
	public static Date getEditDueDate(String input) {
		return getDueDate(removeFirstWord(input));
	}
	
	/**
	 * Remove the first word of the input and returns the resulting string
	 * 
	 * @param input
	 *            Input string
	 * @return Input string without the first word
	 */
	public static String removeFirstWord(String input) {
		if (countWords(input) <= 1) {
			return "";
		} else {
			// Get the index of the first space in the line
			int indexFirstSpace = input.indexOf(' ');
			return input.substring(indexFirstSpace + 1);
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
		if (countWords(input) <= 1) {
			return input;
		} else {
			// Get the index of the first space in the line
			int indexFirstSpace = input.indexOf(' ');
			return input.substring(0, indexFirstSpace);
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
	
	public static String[] removeSpaces(String[] details) {
		for (int i = 0; i < details.length; i++) {
			details[i] = details[i].trim();
		}
		return details;
	}

}
