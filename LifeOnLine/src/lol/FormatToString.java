/**
 * This class formats the tasks in the given task list so that tasks can be displayed in GUI in
 * a more meaningful and easily understandable way.
 * 
 * In this class, numbering will be added to the task and the time and location of the task will 
 * be displayed below the description of the task with bullet points. The latest added tasks will
 * also be highlighted.
 * 
 * The formatted task is stored in object StringWithFormat and the objects are put inside three
 * LinkedList whereby each LinkedList correspond to each main task display panel
 * 
 * Tasks will be formatted with a certain color coding principles which are stated below:
 * 1. Overdue header : red
 * 2. Upcoming and Floating header : blue
 * 3. Done task: grey color with a green tick next to the task description
 * 4. Date headers for all tasks : blue
 */

package lol;

import java.util.LinkedList;

import parser.DateParser;

public class FormatToString {
	private static LinkedList<StringWithFormat> strToShow1 = new LinkedList<StringWithFormat>(); //for TP1
	private static LinkedList<StringWithFormat> strToShow2 = new LinkedList<StringWithFormat>(); //for TP2
	private static LinkedList<StringWithFormat> strToShow3 = new LinkedList<StringWithFormat>(); //for TP3

	private static LinkedList<StringWithFormat> strToShowTemp = new LinkedList<StringWithFormat>();

	private static boolean hasTodayTask = false;
	private static boolean thisTaskIsNotTodayTask = false;
	private static boolean hasSeparator = false; //separator here is the separator between today's tasks and other upcoming tasks
	private static boolean hasHeader = false;

	private enum HEADER_TYPE{
		OVERDUE_DATE, UPCOMING_DATE, INVALID;
	}

	public FormatToString() {
		clearAllLinkedList();
		hasTodayTask = false;
		thisTaskIsNotTodayTask = false;
		hasSeparator = false;
		hasHeader = false;
	}

	/**
	 * Format the given taskList with appropriate formats, add them to the appropriate display
	 * panel and add date header for tasks
	 * 
	 * @param taskList
	 */
	public void format(TaskList<Task> taskList){
		Task currentTask, previousTask;

		//set previousTask as impossible task with impossible dates
		previousTask = new Task("ŒŒŒŒŒŒŒŒ", "", new Date(-1, -1, -9999, null), new Date(-1, -1, -9999, null));

		for(int i = 0; i < taskList.size(); i++){
			currentTask = taskList.get(i);

			int toBeDisplayedIn = determineToBeDisplayedIn(currentTask);
			setStrToShowTemp(toBeDisplayedIn);

			if(isNeedHeader(previousTask, currentTask, toBeDisplayedIn)){
				formatAsHeader(currentTask);
			}

			formatAsTask(i, currentTask);

			previousTask = currentTask;
		}	
	}

	/**
	 * Return which display panel should the given task be displayed in
	 * 
	 * @param task
	 * @return display panel integer
	 */
	private int determineToBeDisplayedIn(Task task){
		if(task.getIsOverdue()){
			return Constants.DISPLAY_IN_TP3;
		}
		else if(task.getTaskDueDate() != null){
			return Constants.DISPLAY_IN_TP1;
		}
		else if(task.getTaskDueDate() == null){
			return Constants.DISPLAY_IN_TP2;
		}
		else{
			assert false : "task is neither suitable to be displayed in TP1, TP2 and TP3";
		return Constants.DISPLAY_IN_TP1;
		}
	}

	/**
	 * Set strToShowX to strToShowTemp which X is determined by toBeDisplayedIn
	 * strToShow1 is to be displayed in display panel 1 and strToShow2 is to be displayed in
	 * display panel 2 and the same goes for strToShow3. 
	 * 
	 * @param toBeDisplayedIn
	 */
	private void setStrToShowTemp(int toBeDisplayedIn){
		switch (toBeDisplayedIn) {
		case Constants.DISPLAY_IN_TP1:
			strToShowTemp = strToShow1;
			break;
		case Constants.DISPLAY_IN_TP2:
			strToShowTemp = strToShow2;
			break;
		case Constants.DISPLAY_IN_TP3:
			strToShowTemp = strToShow3;
			break;
		}
	}

	/**
	 * determine whether the current task needs header or not
	 * 
	 * @param previousTask
	 * @param currentTask
	 * @return boolean value
	 */
	private boolean isNeedHeader(Task previousTask, Task currentTask, int toBeDisplayedIn){
		Date previousDueDate, previousEndDate, currentDueDate, currentEndDate;
		previousDueDate = previousEndDate = currentDueDate = currentEndDate = null;

		if(previousTask.getTaskDueDate() != null){
			previousDueDate = previousTask.getTaskDueDate();
		}
		if(previousTask.getEndDate() != null){
			previousEndDate = previousTask.getEndDate();
		}
		if(currentTask.getTaskDueDate() != null){
			currentDueDate = currentTask.getTaskDueDate();
		}
		if(currentTask.getEndDate() != null){
			currentEndDate = currentTask.getEndDate();
		}

		
		if(toBeDisplayedIn == Constants.DISPLAY_IN_TP1 && !hasHeader){
			hasHeader = true;
			return true;
		}
		else if(!Date.equalDate(currentDueDate, previousDueDate)){
			return true;
		}
		else if(!Date.equalDate(currentEndDate, previousEndDate)){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * format the current task as header by adding header to display according to task's details
	 * 
	 * @param task
	 */
	private void formatAsHeader(Task task) {
		String headerStr;

		HEADER_TYPE headerType = getHeaderType(task);

		switch(headerType){
		case OVERDUE_DATE:
			headerStr = getDateHeader(task);
			strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_DATE));
			break;

		case UPCOMING_DATE:
			headerStr = getDateHeader(task);
			if(isNeedSeparator()){
				addSeparator();
			}
			strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_DATE));
			break;

		default:
			assert false : "invalid header format";	
		}
	}

	/**
	 * return what type of header does task needs
	 * 
	 * @param task
	 * @return HEADER_TYPE
	 */
	private HEADER_TYPE getHeaderType(Task task){
		if(task.getIsOverdue()){
			return HEADER_TYPE.OVERDUE_DATE;
		}
		else if(task.getTaskDueDate() != null){
			return HEADER_TYPE.UPCOMING_DATE;
		}
		return HEADER_TYPE.INVALID;
	}

	/**
	 * Determines whether a separator is needed or not
	 * A separator is needed to differentiate today's tasks and upcoming tasks in upcoming tasks
	 * display panel.
	 * 
	 * @return boolean
	 */
	private boolean isNeedSeparator(){
		return hasTodayTask && thisTaskIsNotTodayTask && !hasSeparator;
	}

	/**
	 * will add the separator to LinkedList
	 */
	private void addSeparator(){
		String separator = Constants.LINEBREAK + Constants.GUI_SEPARATOR;
		strToShowTemp.add(new StringWithFormat(separator, Constants.FORMAT_NONE));
		hasSeparator = true;
	}

	/**
	 * get the upcoming date as header String
	 * 
	 * @param task
	 * @return String
	 */
	private String getDateHeader(Task task){
		String headerStr;
		headerStr = Constants.LINEBREAK + Constants.LINEBREAK + addTodayOrTomorrow(task.getTaskDueDate());
		headerStr = headerStr + dateFormatAsHeader(task.getTaskDueDate());

		// for multi-day tasks
		if(task.getEndDate() != null){
			headerStr = headerStr + " to ";
			headerStr = headerStr + addTodayOrTomorrow(task.getEndDate());
			headerStr = headerStr + dateFormatAsHeader(task.getEndDate());
		}

		return headerStr;
	}

	/**
	 * return a String representing today, tomorrow or none at all
	 * 
	 * @param date
	 * @return
	 */
	private String addTodayOrTomorrow(Date date){
		if(date.getDay() == date.getCurrentDay() 
				&& date.getMonth() == date.getCurrentMonth() 
				&& date.getYear4Digit() == date.getCurrentYear()){
			hasTodayTask = true;
			thisTaskIsNotTodayTask = false;
			return "Today, ";
		}
		else if(date.getDay() == date.getCurrentDay()+1 && 
				date.getMonth() == date.getCurrentMonth() && 
				date.getYear4Digit() == date.getCurrentYear()){
			thisTaskIsNotTodayTask = true;
			return "Tomorrow, ";
		}
		else{
			thisTaskIsNotTodayTask = true;
			return Constants.EMPTY_STRING; //today or tomorrow is not needed because the task is neither today's nor tomorrow's task
		}
	}

	/**
	 * format task as non-header and add to strToShow
	 * 
	 * @param i
	 * @param task
	 */
	private void formatAsTask(int i, Task task) {
		String description = task.getTaskDescription();
		Time dueStartTime = task.getStartTime();
		Time dueEndTime = task.getEndTime();
		String location = task.getTaskLocation();
		boolean isDone = task.getIsDone();
		boolean isJustAdded = task.getIsJustAdded();

		strToShowTemp.add(new StringWithFormat(Constants.LINEBREAK, Constants.FORMAT_NONE));
		strToShowTemp.add(new StringWithFormat(numbering(i), Constants.FORMAT_NUMBER));

		addDescription(description, isDone, isJustAdded);
		addTime(dueStartTime, dueEndTime, isDone);
		addLocation(location, isDone);
	}

	/**
	 * add description to LinkedList
	 * 
	 * @param description
	 * @param isDone
	 * @param isJustAdded
	 */
	private void addDescription(String description, boolean isDone, boolean isJustAdded){
		if (isDone) {
			strToShowTemp.add(new StringWithFormat(description, Constants.FORMAT_DONE));
			strToShowTemp.add(new StringWithFormat("   \u2713", Constants.FORMAT_TICK));
		} 
		else if(isJustAdded){
			StringWithFormat strWithFormat = new StringWithFormat(description, Constants.FORMAT_IS_JUST_ADDED);
			strWithFormat.setIsJustAdded(true);
			strToShowTemp.add(strWithFormat);
		}
		else {
			strToShowTemp.add(new StringWithFormat(description, Constants.FORMAT_DESCRIPTION));
		}
	}

	/**
	 * add time to LinkedList
	 * 
	 * @param dueStartTime
	 * @param dueEndTime
	 * @param isDone
	 */
	private void addTime(Time dueStartTime, Time dueEndTime, boolean isDone){
		if (dueStartTime != null && dueEndTime != null) {
			if(isDone){
				String time = timeStr(dueStartTime, dueEndTime);

				strToShowTemp.add(new StringWithFormat(Constants.LINEBREAK + "      \u25D5", Constants.FORMAT_DONE));
				strToShowTemp.add(new StringWithFormat(time, Constants.FORMAT_DONE));
			}
			else{
				String time = timeStr(dueStartTime, dueEndTime);

				strToShowTemp.add(new StringWithFormat(Constants.LINEBREAK + "      \u25D5", Constants.FORMAT_TIME));
				strToShowTemp.add(new StringWithFormat(time, Constants.FORMAT_NONE));
			}
		} 
		else if (dueStartTime != null) {
			if(isDone){
				String time = timeStr(dueStartTime);
				strToShowTemp.add(new StringWithFormat(Constants.LINEBREAK + "      \u25D5", Constants.FORMAT_DONE));
				strToShowTemp.add(new StringWithFormat(time, Constants.FORMAT_DONE));
			}
			else{
				String time = timeStr(dueStartTime);
				strToShowTemp.add(new StringWithFormat(Constants.LINEBREAK + "      \u25D5", Constants.FORMAT_TIME));
				strToShowTemp.add(new StringWithFormat(time, Constants.FORMAT_NONE));
			}

		}
	}

	/**
	 * add location to LinkedList
	 * 
	 * @param location
	 * @param isDone
	 */
	private void addLocation(String location, boolean isDone){
		if (location != null) {
			if(isDone){
				location = locationStr(location);
				strToShowTemp.add(new StringWithFormat(Constants.LINEBREAK + "      @", Constants.FORMAT_DONE));
				strToShowTemp.add(new StringWithFormat(location, Constants.FORMAT_DONE));	
			}
			else{
				location = locationStr(location);
				strToShowTemp.add(new StringWithFormat(Constants.LINEBREAK + "      @", Constants.FORMAT_LOCATION));
				strToShowTemp.add(new StringWithFormat(location, Constants.FORMAT_NONE));
			}
		}
	}

	/**
	 * add numbering for displaying task as task id
	 * 
	 * @param i
	 * @return numbering String
	 */
	private static String numbering(int i) {
		return (i + 1) + ") ";
	}

	/**
	 * Format time to String with appropriate format
	 * 
	 * @param startTime
	 * @param endTime
	 * @return String
	 */
	private static String timeStr(Time startTime, Time endTime) {
		return startTime.toString() + " - " + endTime.toString();
	}

	/**
	 * format time to String with appropriate format
	 * 
	 * @param startTime
	 * @return String
	 */
	private static String timeStr(Time startTime) {
		return startTime.toString();

	}

	/**
	 * Format location to String with appropriate format
	 * 
	 * @param location
	 * @return String
	 */
	private static String locationStr(String location) {
		return location;
	}

	/**
	 * Format the date as header with appropriate format
	 * 
	 * @param dueDate
	 * @return String
	 */
	private static String dateFormatAsHeader(Date dueDate) {
		// should somehow change to dueDate.toString() method to lower coupling
		DateParser dp = new DateParser();
		String dayOfTheWeek = dp.getDayOfTheWeek(dueDate);
		return dayOfTheWeek + " " + dueDate.toString();
	}

	/**
	 * return strToShowX which is determined by num
	 * 
	 * @param num
	 * @return LinkedList of strToShowX
	 */
	public static LinkedList<StringWithFormat> getLinkedList(int num) {
		switch (num) {
		case 1:
			if(strToShow1.isEmpty()){
				strToShow1.add(new StringWithFormat("", Constants.FORMAT_NONE));
			}
			return strToShow1;
		case 2:
			if(strToShow2.isEmpty()){
				strToShow2.add(new StringWithFormat("", Constants.FORMAT_NONE));
			}
			return strToShow2;
		case 3:
			if(strToShow3.isEmpty()){
				strToShow3.add(new StringWithFormat("", Constants.FORMAT_NONE));
			}
			return strToShow3;
		default:
			assert false : "Parameter of FormatTostring.getLinkedList(num) is wrong";
		return strToShow1;
		}
	}

	/**
	 * will clear all the LinkedList strToShow
	 */
	private static void clearAllLinkedList() {
		strToShow1.clear();
		strToShow2.clear();
		strToShow3.clear();

		//add main header to the three displaying panels
		strToShow1.add(new StringWithFormat(Constants.HEADER_UPCOMING, Constants.FORMAT_HEADER_UPCOMING));
		strToShow2.add(new StringWithFormat(Constants.LINEBREAK , Constants.FORMAT_HEADER_FLOATING));
		strToShow3.add(new StringWithFormat(Constants.HEADER_OVERDUE, Constants.FORMAT_HEADER_OVERDUE));
	}

	/**
	 * return total number of LinkedList used
	 * 
	 * @return int
	 */
	public static int getLinkedListNum() {
		return 3;
	}
}