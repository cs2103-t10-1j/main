package lol;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class Constants {

	public static final String WELCOME_MESSAGE = "Type in your task and hit enter to ADD. \n"
			+ "For more information on other commands type HELP and hit enter.";
	// Limit for number of days before current day for which task without year
	// will be entered as overdue task
	public static final int DAYS_OFFSET = 7;

	public static final int REFRESH_TIME = 60 * 1000; // refers to the time in
														// milliseconds
	// File name
	public static final String FILE_NAME = "LOLTaskList.txt";
	// Separators
	public static final String SEPARATOR = "\\\\";
	public static final String SEPARATOR_SLASH = "/";
	public static final String SEPARATOR_DOT = "\\.";
	public static final String SEPARATOR_TO = " to ";
	public static final String SEPARATOR_DASH = "-";
	
	// double quote
	public static final String DOUBLE_QUOTE = "\"";
	
	//dot
	public static final String DOT = ".";

	// Dictionaries
	public static final String[] DICTIONARY_ADD = { "add", "a" };
	public static final String[] DICTIONARY_DELETE = { "delete", "rm", "del" };
	public static final String[] DICTIONARY_SHOW = { "show", "display", "sh" };
	public static final String[] DICTIONARY_SEARCH = { "search", "find", "sr" };
	public static final String[] DICTIONARY_EDIT = { "edit", "change", "e" };
	public static final String[] DICTIONARY_DONE = { "done", "dn" };
	public static final String[] DICTIONARY_NOT_DONE = { "undone", "pending" };
	public static final String[] DICTIONARY_UNDO = { "undo" };
	public static final String[] DICTIONARY_REDO = { "redo" };
	public static final String[] DICTIONARY_VIEW_HOMESCREEN = { "home", "h" };
	public static final String[] DICTIONARY_HELP = { "help" };
	public static final String[] DICTIONARY_EXIT = { "exit", "ex" };
	public static final String[] DICTIONARY_PARAMETERS = { "location", "loc",
			"date", "startdate", "enddate", "sd", "ed", "time", "starttime",
			"endtime", "st", "et" };
	
	// Months
	public static final String[] MONTHS_SHORT = { "jan", "feb", "mar", "apr",
			"may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" };
	public static final String[] MONTHS_LONG = { "january", "february",
			"march", "april", "may", "june", "july", "august", "september",
			"october", "november", "december" };
	public static final String[] MONTHS_1ST_LETTER_CAPS_SHORT = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
		"Aug", "Sep", "Oct", "Nov", "Dec" };
	public static final String[] MONTHS_1ST_LETTER_CAPS_LONG = { "January", "February", "March", "April", "May",
		"June", "July", "August", "September", "October", "November",
		"December" };

	// Days of the week
	public static final String[] DAYS_SHORT = { "sun", "mon", "tue", "wed",
			"thu", "fri", "sat" };
	public static final String[] DAYS_LONG = { "sunday", "monday", "tuesday",
			"wednesday", "thursday", "friday", "saturday" };
	public static final String[] DAYS_IMMEDIATE = { "today", "tomorrow", "tmw",
			"tmrw", "tmr" };

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
	public static final String COMMAND_HELP = "help";
	public static final String COMMAND_EXIT = "exit";
	public static final String COMMAND_INVALID = "invalid command";
	public static final String COMMAND_ALERT = "alert";

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
	public static final int INDEX_START_DATE = 0;
	public static final int INDEX_END_DATE = 1;
	public static final int INDEX_SEPARATOR = 0;
	public static final int INDEX_1ST_WORD = 0;
	public static final int INDEX_2ND_WORD = 0;
	public static final int INDEX_3RD_WORD = 1;
	public static final int INDEX_4TH_WORD = 2;
	public static final int INDEX_5TH_WORD = 3;
	public static final int INDEX_1ST_LETTER = 0;
	public static final int INDEX_2ND_LETTER = 1;
	public static final int INDEX_2ND_PART = 1;
	public static final int INDEX_3RD_PART = 2;
	public static final int INDEX_START = 0;
	public static final int INDEX_END = 1;
	
	// year
	public static final int YEAR_2000 = 2000;
	
	// number of milliseconds in a day
	public static final int MILLISECONDS_IN_A_DAY = 1000 * 60 * 60 * 24;
	
	// parameters
	public static final String PARAMETER_LOCATION = "location";
	public static final String PARAMETER_LOC = "loc";
	public static final String PARAMETER_DATE = "date";
	public static final String PARAMETER_START_DATE = "startdate";
	public static final String PARAMETER_END_DATE = "enddate";
	public static final String PARAMETER_SD = "sd";
	public static final String PARAMETER_ED = "ed";
	public static final String PARAMETER_TIME = "time";
	public static final String PARAMETER_START_TIME = "starttime";
	public static final String PARAMETER_END_TIME = "endtime";
	public static final String PARAMETER_ST = "st";
	public static final String PARAMETER_ET = "et";
	

	// Array/String lengths
	public static final int LENGTH_DAY_MONTH_YEAR = 3;
	public static final int LENGTH_DAY_MONTH = 2;
	public static final int LENGTH_AM_PM = 2;
	public static final int LENGTH_HOUR = 1;
	public static final int LENGTH_HOUR_MINUTE = 2;
	public static final int LENGTH_24HOUR_FORMAT = 4;
	public static final int LENGTH_TIME_RANGE = 2;

	// time strings
	public static final String STRING_AM = "am";
	public static final String STRING_PM = "pm";
	public static final String STRING_2400 = "2400";
	public static final String STRING_0000 = "0000";
	
	// number of days in a week
	public static final int NUMBER_OF_DAYS_IN_A_WEEK = 7;
	
	// 2 digits
	public static final String FORMAT_2_DIGITS = "00";
	
	// limits
	public static final int LIMIT_ZERO = 0;
	public static final int LIMIT_MIN_2DIGIT_YEAR = 10;
	public static final int LIMIT_MAX_2DIGIT_YEAR = 99;
	public static final int LIMIT_MIN_4DIGIT_YEAR = 2010;
	public static final int LIMIT_MAX_4DIGIT_YEAR = 2099;
	public static final int LIMIT_MIN_MINUTE = 0;
	public static final int LIMIT_MAX_MINUTE = 59;
	public static final int LIMIT_MIN_HR = 1;
	public static final int LIMIT_MAX_12HR = 12;
	public static final int LIMIT_MAX_24HR = 23;

	// Feedback Strings
	public static final String QUOTE = "\"";
	// public static final String LINEBREAK =
	// System.getProperty("line.separator");
	public static final String LINEBREAK = "\n";
	public static final String FEEDBACK_ADD_SUCCESS = " added successfully!";
	public static final String FEEDBACK_DEL_SUCCESS = " deleted successfully!";
	public static final String FEEDBACK_MASS_DEL_SUCCESS = "Mass delete performed successfully!";
	public static final String FEEDBACK_MASS_DEL_FAILURE = "Invalid task ID specified!";
	public static final String FEEDBACK_EDIT_SUCCESS = " was editted succesfully!";
	public static final String FEEDBACK_SHOW_SUCCESS = "<html>Displaying search results for ";
	public static final String FEEDBACK_SHOW_HITS_SINGLE = " task found!";
	public static final String FEEDBACK_SHOW_HITS_MULTI = " tasks found!";
	public static final String FEEDBACK_SHOW_FAILURE = "There are no tasks due on that day!";
	public static final String FEEDBACK_SHOW_OVERDUE_SUCCESS = "<html>Displaying overdue tasks!";
	public static final String FEEDBACK_SHOW_OVERDUE_FAILURE = "No overdue tasks were found!";
	public static final String FEEDBACK_SHOW_ARCHIVE_SUCCESS = "Displaying tasks in archive!";
	public static final String FEEDBACK_SHOW_ARCHIVE_FAILURE = "There are no tasks in your archive!";
	public static final String FEEDBACK_SHOW_WEEK_SUCCESS = "Displaying agenda for next 7 days!";
	public static final String FEEDBACK_SHOW_WEEK_FAILURE = "No tasks due in the next 7 days!";
	public static final String FEEDBACK_SHOW_MONTH_SUCCESS = "Displaying agenda for ";
	public static final String FEEDBACK_SHOW_MONTH_FAILURE = "No tasks due in ";
	public static final String FEEDBACK_SHOW_ALL_SUCCESS = "Displaying all tasks found in database!";
	public static final String FEEDBACK_SHOW_ALL_FAILURE = "No tasks found in database!";
	public static final String FEEDBACK_SEARCH_SUCCESS_SINGLE = "Search results for tasks with the keyword: ";
	public static final String FEEDBACK_SEARCH_SUCCESS_MULTI = "Search results for tasks with the keywords: ";
	public static final String FEEDBACK_SEARCH_FAILURE = "SEARCH FAILED";
	public static final String FEEDBACK_SEARCH_FAILURE_SINGLE = "No tasks were found with the keyword: ";
	public static final String FEEDBACK_SEARCH_FAILURE_MULTI = "No tasks were found with the keywords: ";
	public static final String FEEDBACK_DONE_SUCCESS = " has been completed!";
	public static final String FEEDBACK_DONE_FAILURE = "That task has already been completed!";
	public static final String FEEDBACK_MASS_DONE_SUCCESS = "Specified tasks have been marked as done!";
	public static final String FEEDBACK_MASS_DONE_FAILURE = "Invalid task ID specified!";
	public static final String FEEDBACK_NOT_DONE_SUCCESS = " is now pending completion!";
	public static final String FEEDBACK_NOT_DONE_FAILURE = "That task is already pending completion!";
	public static final String FEEDBACK_MASS_NOT_DONE_SUCCESS = "Specified tasks have been marked as pending!";
	public static final String FEEDBACK_MASS_NOT_DONE_FAILURE = "Invalid task ID specified!";
	public static final String FEEDBACK_UNDO_SUCCESS = "Last action has been undone!";
	public static final String FEEDBACK_UNDO_FAILURE = "Nothing to undo!";
	public static final String FEEDBACK_REDO_SUCCESS = "Redone!!";
	public static final String FEEDBACK_REDO_FAILURE = "Nothing to redo!";
	public static final String FEEDBACK_VIEW_HOMESCREEN = "Displaying Home-Screen!";
	public static final String FEEDBACK_INVALID = "That is an invalid action!";
	public static final String FEEDBACK_INVALID_NUMBER_OF_QUOTES = "Invaild number of quotes";
	public static final String FEEDBACK_INVALID_PARAMETERS_FOR_EDIT = "Invalid parameters for edit command";

	// Switch-case
	public static final int EMPTY_LIST = 0;
	public static final int LIST_SIZE_ONE = 1;
	public static final int WORD_COUNT_ONE = 1;
	
	// date formats
	public static final String DATE_FORMAT_DAY_NUM_MONTH_NUM_YEAR_LONG = "d/M/yyyy";
	public static final String DATE_FORMAT_DAY_NUM_MONTH_NUM_YEAR_SHORT = "d/M/yy";
	public static final String DATE_FORMAT_DAY_NUM_MONTH_LONG_YEAR_LONG = "d MMMM yyyy";
	public static final String DATE_FORMAT_DAY_NUM_MONTH_LONG_YEAR_SHORT = "d MMMM yy";
	public static final String DATE_FORMAT_DAY_NUM_MONTH_NUM = "d/M";
	public static final String DATE_FORMAT_DAY_NUM_MONTH_SHORT_YEAR_LONG = "d MMM yyyy";
	public static final String DATE_FORMAT_DAY_NUM_MONTH_SHORT_YEAR_SHORT = "d MMM yy";
	public static final String DATE_FORMAT_DAY_NUM_MONTH_SHORT = "d MMM";
	public static final String DATE_FORMAT_DAY_NUM_MONTH_LONG = "d MMMM";

	// show parameters (LOWERCASE)
	public static final String SHOW_OVERDUE = "overdue";
	public static final String SHOW_ARCHIVE = "archive";
	public static final String SHOW_WEEK = "week";
	public static final String SHOW_MONTH = "month";
	public static final String SHOW_ALL = "all";

	public static final int ONE_WEEK = 8;

	// not found
	public static int NOT_FOUND = -1;

	// keywords
	public static final String[] KEYWORDS = { "at", "on", "by", "from" };

	// index of keywords
	public static final int INDEX_KEYWORD_AT = 0;
	public static final int INDEX_KEYWORD_ON = 1;
	public static final int INDEX_KEYWORD_BY = 2;
	public static final int INDEX_KEYWORD_FROM = 3;

	// regex
	public static final String REGEX_ONE_OR_MORE_SPACES = "\\s+";
	public static final String REGEX_ZERO_OR_MORE_SPACES = "\\s*";
	public static final String REGEX_WORD_START = "\\b";
	public static final String REGEX_NON_DIGIT = "\\D";
	public static final String REGEX_WORD_END = "\\b";
	public static final String REGEX_AT_WITH_SPACES = "\\bat\\b\\s*";
	public static final String REGEX_AT_SPACE_WORD_START = "\\bat\\b\\s\\b";
	public static final String REGEX_WORD_END_1SPACE_WORD_START = "\\b\\s\\b";
	public static final String REGEX_WORD_END_SPACES_WORD_START = "\\b\\s*\\b";
	public static final String[] REGEX_KEYWORDS = { "\\bat\\b", "\\bon\\b", "\\bby\\b", "\\bfrom\\b" };

	// space
	public static final String SPACE = " ";
	public static final char SPACE_CHAR = ' ';
	
	// comma
	public static final String COMMA = ",";

	// empty string
	public static final String EMPTY_STRING = "";
	
	// strings
	public static final String STRING_ZERO = "0";
	public static final String STRING_M_LOWERCASE = "m";
	
	// logging
	public static final String LOGGER_PARSER = "LOLParser";
	public static final String LOGGER_FILE_NAME = "C:\\ParserLogFile%g.txt";

	// error messages
	public static final String ERROR_MULTIPLE_LOCATION = "Error! More than one location.";
	public static final String ERROR_PROCESSING = "processing error";

	// below are all constants used in GUI
	final static Color DARK_ORANGE = new Color(253, 101, 0);
	final static Color PURPLE = new Color(204, 0, 204);
	final static Color BG = new Color(0, 129, 72);
	final static Color DARK_BLUE = new Color(3, 97, 148);
	final static Color MEDIUM_BLUE = new Color(82, 161, 204);

	final static Font TREBUCHET_14 = new Font("Trebuchet MS", Font.PLAIN, 14);
	final static Font TREBUCHET_BOLD_14 = new Font("Trebuchet MS", Font.BOLD, 14);
	final static Font TREBUCHET_BOLD_16 = new Font("Trebuchet MS", Font.BOLD, 16);
	final static Font TREBUCHET_16 = new Font("Trebuchet MS", Font.PLAIN, 16);
	final static Font CALIBRI_14 = new Font("Calibri", Font.PLAIN, 14);
	final static Font CALIBRI_BOLD_14 = new Font("Calibri", Font.BOLD, 14);
	final static Font CALIBRI_BOLD_16 = new Font("Calibri", Font.BOLD, 16);
	final static Font CALIBRI_16 = new Font("Calibri", Font.PLAIN, 16);
	final static Font TAHOMA_14 = new Font("Tahoma", Font.PLAIN, 14);
	
	final static String LOL_NAME = "LOL - LifeOnLine";
	final static String TODAY = "Today";
	final static String PROGRESS_BAR = "Progress Bar";
	
	final static String HEADER_OVERDUE = "Overdue Tasks";
	final static String HEADER_FLOATING = "Tasks With No date \n";
	final static String HEADER_UPCOMING = "Upcoming Tasks";

	final static String FORMAT_HEADER_OVERDUE = "overdue header";
	final static String FORMAT_HEADER_FLOATING = "floating header";
	final static String FORMAT_HEADER_NORMAL = "normal header";
	final static String FORMAT_HEADER_UPCOMING = "upcoming header";
	final static String FORMAT_TIME = "time";
	final static String FORMAT_DESCRIPTION = "description";
	final static String FORMAT_LOCATION = "location";
	final static String FORMAT_OVERDUE = "overdue";
	final static String FORMAT_NONE = "null";
	final static String FORMAT_TICK = "tick mark";
	final static String FORMAT_HEADER_DATE = "date header";
	final static String FORMAT_NUMBER = "numbering";
	final static String FORMAT_DONE = "done";
	final static String FORMAT_IS_JUST_ADDED = "is just added";
	
	final static String BULLET_TIME = "      \u25D5";
	final static String BULLET_LOCATION = "      @";
	final static String GREEN_TICK = "   \u2713";
	
	final static Border DISPLAY_PANEL_FOCUS_BORDER = BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createLineBorder(Color.BLACK));
	final static Border INPUT_TF_FOCUS_BORDER = BorderFactory.createLoweredBevelBorder();
	final static Border ALERT_BUTTON_FOCUS_BORDER = BorderFactory.createRaisedBevelBorder();
	
	final static int DISPLAY_IN_TP1 = 1; //upcoming TP
	final static int DISPLAY_IN_TP2 = 2; //no date TP
	final static int DISPLAY_IN_TP3 = 3; //overdue TP
	
	final static boolean IS_NEED_HEADER = true;

	final static int IMPOSSIBLE_ARRAYLIST_INDEX = -1;
	
	//message for LOL still running in background
	final static String MSG_BACKGROUND_TITLE = "Hey,";
	final static String MSG_BACKGROUND_CONTENT = "LOL is still running in the background!";
	
	//message used in small pop up window for email functionality
	final static String MSG_PLEASE_ENTER_EMAIL = "Please enter you email id to receive alerts in your inbox.";
	final static String MSG_WELCOME = "Welcome to LOL!";
	final static String MSG_EMAIL_EXAMPLE = "example@example.com";
	
	//message used for Help pop up window
	final static String MSG_WELCOME_HELP = "Help";
	final static String TAB = "           ";
	final static String MSG_HELP_INFO = "LOL can have a task with description, date, date range, time, time range and location.\n"
			+ "Whereby all of the mentioned parameters can be interchanged.\n\n"
			+ "Below are all the available functions of LOL:\n"
			+ "1. Add task\n"
			+ TAB + "add <description> <date> <time> <location>\n"
			+ TAB + "Example : Add buy milk on friday at 9am at Fair Price\n"
			+ "2. Delete task\n"
			+ TAB + "delete <task id number>\n"
			+ TAB + "Example : delete 4\n"
			+ "3. Edit task\n"
			+ TAB + "edit <task id number> <one parameter that you want to edit>\n"
			+ TAB + "Example : Edit 2 at 9pm\n"
			+ "4. Search for keywords (in description and location)\n"
			+ TAB + "search <keywords>\n"
			+ TAB + "Example : search milk\n"
			+ "5. Show tasks with certain date/month and overdue/archive\n"
			+ TAB + "show <date/ month/ overdue/ archive>\n"
			+ TAB + "Example : show january\n"
			+ "6. Mark a task as done\n"
			+ TAB + "done <task id number>\n"
			+ "7. Mark a done task as undone\n"
			+ TAB + "undone <task id number>\n"
			+ "8. Undo\n"
			+ TAB + "Type \"undo\" to undo the last action\n"
			+ "9. Redo\n"
			+ TAB + "Type \"redo\" to redo the last undo action\n"
			+ "10. Exit\n"
			+ TAB + "Type \"exit\" to exit LOL";
			
	// Tray icon display messages
	final static String MSG_LOL_IS_RUNNING = "LOL is Already Running!" ;
	final static String MSG_RESTORE = "CTRL + L to Restore";
	final static String MSG_ERROR = "Error occured";

	// Separator between today's tasks and upcoming tasks in gui
	final static String GUI_SEPARATOR = "===========================";
	
	//LOLEmail configurations
	final static String LOLEmailId = "alert.lifeonline@gmail.com";
	final static String LOLEmailPasswd = "lifeonline99";
	final static String LOLEmailHost = "smtp.gmail.com";
	final static String LOLEmailPort = "587";
}


