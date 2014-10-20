package lol;

public class Constants {
	// Limit for number of days before current day for which task without year will be entered as overdue task
	public static final int LIMIT_DAYS_BEFORE_TODAY_ASSUME_SAME_YEAR = 7;

	// Separators
	public static final String SEPARATOR = "\\\\";
	public static final String SEPARATOR_DOT = "\\.";
	public static final String SEPARATOR_TO = " to ";
	public static final String SEPARATOR_DASH = "-";

	// Dictionaries
	public static final String[] DICTIONARY_ADD = { "add" };
	public static final String[] DICTIONARY_DELETE = { "delete" };
	public static final String[] DICTIONARY_SHOW = { "show" };
	public static final String[] DICTIONARY_SEARCH = { "search" };
	public static final String[] DICTIONARY_EDIT = { "edit" };
	public static final String[] DICTIONARY_DONE = { "done" };
	public static final String[] DICTIONARY_NOT_DONE = { "undone" };
	public static final String[] DICTIONARY_UNDO = { "undo" };
	public static final String[] DICTIONARY_REDO = { "redo" };
	public static final String[] DICTIONARY_VIEW_HOMESCREEN = { "home" };
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
	public static final String COMMAND_SEARCH = "search";
	public static final String COMMAND_EDIT = "edit";
	public static final String COMMAND_UNDO = "undo";
	public static final String COMMAND_REDO = "redo";
	public static final String COMMAND_DONE = "done";
	public static final String COMMAND_NOT_DONE = "undone";
	public static final String COMMAND_VIEW_HOMESCREEN = "home";
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
	public static final String LINEBREAK = "\n";
	public static final String FEEDBACK_ADD_SUCCESS = " added successfully!";
	public static final String FEEDBACK_DEL_SUCCESS = " deleted successfully!";
	public static final String FEEDBACK_EDIT_SUCCESS = " was editted succesfully!";
	public static final String FEEDBACK_SHOW_SUCCESS = "Displaying search results for ";
	public static final String FEEDBACK_SHOW_HITS_SINGLE = " task found!";
	public static final String FEEDBACK_SHOW_HITS_MULTI = " tasks found!";
	public static final String FEEDBACK_SHOW_FAILURE = "There are no tasks due on that day!";
	public static final String FEEDBACK_SHOW_OVERDUE_SUCCESS = "Displaying overdue tasks!";
	public static final String FEEDBACK_SHOW_OVERDUE_FAILURE = "No overdue tasks were found!";
	public static final String FEEDBACK_SHOW_ARCHIVE_SUCCESS = "Displaying tasks in archive!";
	public static final String FEEDBACK_SHOW_ARCHIVE_FAILURE = "There are no tasks in your archive!";
	public static final String FEEDBACK_SHOW_ALL_SUCCESS = "Displaying all tasks found in database!";
	public static final String FEEDBACK_SHOW_ALL_FAILURE = "No tasks found in database!";
	public static final String FEEDBACK_SEARCH_SUCCESS_SINGLE = "Search results for tasks with the keyword: ";
	public static final String FEEDBACK_SEARCH_SUCCESS_MULTI = "Search results for tasks with the keywords: ";
	public static final String FEEDBACK_SEARCH_FAILURE_SINGLE = "No tasks were found with the keyword: ";
	public static final String FEEDBACK_SEARCH_FAILURE_MULTI = "No tasks were found with the keywords: ";
	public static final String FEEDBACK_DONE_SUCCESS = " has been completed!";
	public static final String FEEDBACK_DONE_FAILURE = "That task has already been completed!";
	public static final String FEEDBACK_NOT_DONE_SUCCESS = " is now pending completion!";
	public static final String FEEDBACK_NOT_DONE_FAILURE = "That task is already pending completion!";
	public static final String FEEDBACK_UNDO_SUCCESS = "Last action has been undone!";
	public static final String FEEDBACK_UNDO_FAILURE = "Nothing to undo!";
	public static final String FEEDBACK_REDO_SUCCESS = "Redone!!";
	public static final String FEEDBACK_REDO_FAILURE = "Nothing to redo!";
	public static final String FEEDBACK_VIEW_HOMESCREEN = "Displaying Home-Screen!";
	public static final String FEEDBACK_INVALID = "That is an invalid action!";
	
	// show parameters
	public static final String SHOW_OVERDUE = "overdue";
	public static final String SHOW_ARCHIVE = "archive";
	public static final String SHOW_ALL = "all";
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

	// empty string
	public static final String EMPTY_STRING = "";

	// error messages
	public static final String ERROR_MULTIPLE_LOCATION = "Error! More than one location.";

	// constants used in GUI
	final static String HEADER_OVERDUE = "Overdue Task";
	final static String HEADER_FLOATING = "Task With No date";

	final static String FORMAT_HEADER_OVERDUE = "overdue header";
	final static String FORMAT_HEADER_FLOATING = "floating header";
	final static String FORMAT_HEADER_NORMAL = "normal header";
	final static String FORMAT_TIME = "time";
	final static String FORMAT_DESCRIPTION = "description";
	final static String FORMAT_LOCATION = "location";
	final static String FORMAT_OVERDUE = "overdue";
	final static String FORMAT_NONE = "null";
	final static String FORMAT_TIME_STRIKE = "time strike";
	final static String FORMAT_DESCRIPTION_STRIKE = "description strike";
	final static String FORMAT_LOCATION_STRIKE = "location strike";
	final static String FORMAT_OVERDUE_STRIKE = "overdue strike";

	final static int DISPLAY_IN_TP1 = 1;
	final static int DISPLAY_IN_TP2 = 2;
}
