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

	// Separator
	public static final String SEPARATOR = "\\\\";

	// Dictionaries
	public static final String[] DICTIONARY_ADD = { "add" };
	public static final String[] DICTIONARY_DELETE = { "delete" };
	public static final String[] DICTIONARY_SHOW = { "show" };
	public static final String[] DICTIONARY_EDIT = { "edit" };
	public static final String[] DICTIONARY_DONE = { "done" };
	public static final String[] DICTIONARY_EXIT = { "exit" };

	// Months
	public static final String[] MONTHS_SHORT = { "jan", "feb", "mar", "apr",
			"may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" };
	public static final String[] MONTHS_LONG = { "january", "february",
			"march", "april", "may", "june", "july", "august", "september",
			"october", "november", "december" };

	// Days of the week
	public static final String[] DAYS_SHORT = { "sun", "mon", "tue", "wed",
			"thu", "fri", "sat" };
	public static final String[] DAYS_LONG = { "sunday", "monday", "tuesday",
			"wednesday", "thursday", "friday", "saturday" };
	public static final String[] DAYS_IMMEDIATE = { "today", "tomorrow", "tmw" };

	// Commands
	public static final String COMMAND_ADD = "add";
	public static final String COMMAND_DELETE = "delete";
	public static final String COMMAND_SHOW = "show";
	public static final String COMMAND_EDIT = "edit";
	public static final String COMMAND_DONE = "done";
	public static final String COMMAND_EXIT = "exit";
	public static final String COMMAND_INVALID = "invalid command";

	// Array indices
	public static final int INDEX_DAY = 0;
	public static final int INDEX_TODAY = 0;
	public static final int INDEX_MONTH = 1;
	public static final int INDEX_YEAR = 2;

	// Array lengths
	public static final int LENGTH_DAY_MONTH_YEAR = 3;
	public static final int LENGTH_DAY_MONTH = 2;

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

		if (hasWordInDictionary(DICTIONARY_ADD, command)) {
			return COMMAND_ADD;
		} else if (hasWordInDictionary(DICTIONARY_DELETE, command)) {
			return COMMAND_DELETE;
		} else if (hasWordInDictionary(DICTIONARY_SHOW, command)) {
			return COMMAND_SHOW;
		} else if (hasWordInDictionary(DICTIONARY_EDIT, command)) {
			return COMMAND_EDIT;
		} else if (hasWordInDictionary(DICTIONARY_DONE, command)) {
			return COMMAND_DONE;
		} else if (hasWordInDictionary(DICTIONARY_EXIT, command)) {
			return COMMAND_EXIT;
		} else {
			return COMMAND_INVALID;
		}
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
		return details.split(SEPARATOR)[0];
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
		String[] detailsArray = details.split(SEPARATOR);
		DateParser dp = new DateParser();

		// 1st element is the description
		// If only 1 element or 2nd element is due date then no location
		if (detailsArray.length < 2 || dp.isValidDate(detailsArray[1])
				|| dp.isValidDay(detailsArray[1])) {
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
		String[] detailsArray = details.split(SEPARATOR);
		DateParser dp = new DateParser();

		// 1st element is the description
		// Check from 2nd element onwards
		for (int i = 1; i < detailsArray.length; i++) {
			if (dp.isValidDate(detailsArray[i]) || dp.isValidDay(detailsArray[i])) {
				return dp.createDate(detailsArray[i]);
			}
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
		String[] detailsArray = details.split(SEPARATOR);
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
	 * Returns a Task object with details given in the parameter for add command
	 * 
	 * @param input
	 *            user input
	 * @return Task added
	 */
	public static Task getTask(String input) {
		return new Task(getDescription(input), getLocation(input),
				getDueDate(input));
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
		for (int i = 0; i < dictionary.length; i++) {
			if (dictionary[i].equalsIgnoreCase(word)) {
				return true;
			}
		}
		return false;
	}

}
