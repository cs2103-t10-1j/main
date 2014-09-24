package lol;

public class LOLParser {
	
	public static final String SEPARATOR = "\\";
	
	// Dictionaries
	public static final String[] DICTIONARY_ADD = { "add" };
	public static final String[] DICTIONARY_DELETE = { "delete" };
	public static final String[] DICTIONARY_SHOW = { "show" };
	public static final String[] DICTIONARY_EDIT = { "edit" };
	public static final String[] DICTIONARY_DONE = { "done" };
	public static final String[] DICTIONARY_EXIT = { "exit" };
	
	public static final String COMMAND_ADD = "add";
	public static final String COMMAND_DELETE = "delete";
	public static final String COMMAND_SHOW = "show";
	public static final String COMMAND_EDIT = "edit";
	public static final String COMMAND_DONE = "done";
	public static final String COMMAND_EXIT = "exit";
	public static final String COMMAND_INVALID = "invalid command";

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
	
	public static String getDescription(String input) {
		String details = removeFirstWord(input);
		return details;
	}
	
	/**
	 * Remove the first word of the input and returns the resulting string
	 * 
	 * @param input
	 *            Input string
	 * @return Input string without the first word
	 */
	private static String removeFirstWord(String input) {
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
	private static String getFirstWord(String input) {
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
	private static int countWords(String input) {
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

	private static boolean hasWordInDictionary(String[] dictionary, String word) {
		for (int i = 0; i < dictionary.length; i++) {
			if (dictionary[i].equalsIgnoreCase(word)) {
				return true;
			}
		}
		return false;
	}

}
