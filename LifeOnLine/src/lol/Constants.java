package lol;

public class Constants {
		// Separators
		public static final String SEPARATOR = "\\\\";
		public static final String SEPARATOR_DOT = "\\.";
		public static final String SEPARATOR_TO = " to ";
		public static final String SEPARATOR_DASH = "-";

		// Dictionaries
		public static final String[] DICTIONARY_ADD = { "add" };
		public static final String[] DICTIONARY_DELETE = { "delete" };
		public static final String[] DICTIONARY_SHOW = { "show" };
		public static final String[] DICTIONARY_EDIT = { "edit" };
		public static final String[] DICTIONARY_DONE = { "done" };
		public static final String[] DICTIONARY_UNDO = { "undo" };
		public static final String[] DICTIONARY_REDO = { "redo" };
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
		public static final String COMMAND_UNDO = "undo";
		public static final String COMMAND_REDO = "redo";
		public static final String COMMAND_DONE = "done";
		public static final String COMMAND_EXIT = "exit";
		public static final String COMMAND_INVALID = "invalid command";

		// Array indices
		public static final int INDEX_DAY = 0;
		public static final int INDEX_TODAY = 0;
		public static final int INDEX_MONTH = 1;
		public static final int INDEX_YEAR = 2;
		public static final int INDEX_BEGIN = 0;
		public static final int INDEX_HOUR = 0;
		public static final int INDEX_MINUTE = 1;
		public static final int INDEX_START_TIME = 0;
		public static final int INDEX_END_TIME = 1;

		// Array/String lengths
		public static final int LENGTH_DAY_MONTH_YEAR = 3;
		public static final int LENGTH_DAY_MONTH = 2;
		public static final int LENGTH_AM_PM = 2;
		public static final int LENGTH_HOUR = 1;
		public static final int LENGTH_HOUR_MINUTE = 2;
		public static final int LENGTH_24HOUR_FORMAT = 4;
		public static final int LENGTH_TIME_RANGE = 2;
		
		// am/pm
		public static final String STRING_AM = "am";
		public static final String STRING_PM = "pm";

		// Feedback Strings
		public static final String QUOTE = "\"";
		public static final String FEEDBACK_ADD_SUCCESS = " added successfully";
		public static final String FEEDBACK_DEL_SUCCESS = " deleted successfully";
		public static final String FEEDBACK_EDIT_SUCCESS = " was editted succesfully";
		public static final String FEEDBACK_DONE_SUCCESS = " has been completed!";
		public static final String FEEDBACK_UNDO_SUCCESS = "Last action has been undone!";
		public static final String FEEDBACK_UNDO_FAILURE = "Nothing to undo!";
		public static final String FEEDBACK_REDO_SUCCESS = "Redone!!";
		public static final String FEEDBACK_REDO_FAILURE = "Nothing to redo!";
		public static final String FEEDBACK_INVALID = "That is an invalid action!";
		
		// not found
		public static int NOT_FOUND = -1;
		
		// keywords
		public static final String[] KEYWORDS = { "at", "on", "by", "from" };
		
		// index of keywords
		public static final int INDEX_KEYWORD_AT = 0;
		
		// regex
		public static final String REGEX_ONE_OR_MORE_SPACES = "\\s+";
		public static final String[] REGEX_KEYWORDS = { "\\bat\\b" };
		
		// space
		public static final String SPACE = " ";
		
		// error messages
		public static final String ERROR_MULTIPLE_LOCATION = "Error! More than one location.";

}
