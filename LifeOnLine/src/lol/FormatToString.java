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


	private enum HEADER_TYPE{
		OVERDUE_DATE, UPCOMING_DATE, INVALID;
	}

	public FormatToString() {
		clearAllLinkedList();
		hasTodayTask = false;
		thisTaskIsNotTodayTask = false;
		hasSeparator = false;
	}
	
	public void format(TaskList<Task> taskList){
		Task currentTask, previousTask;
		
		//set previousTask as impossible task with impossible dates
		previousTask = new Task("ŒŒŒŒŒŒŒŒ", "", new Date(-1, -1, -9999, null), new Date(-1, -1, -9999, null));
		
		for(int i = 0; i < taskList.size(); i++){
			currentTask = taskList.get(i);
			int toBeDisplayedIn = determineToBeDisplayedIn(currentTask);
			setStrToShowTemp(toBeDisplayedIn);
			
			if(isNeedHeader(previousTask, currentTask)){
				formatAsHeader(currentTask);
			}
			
			formatAsTask(i, currentTask);
			
			previousTask = currentTask;
		}	
	}
	
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
	
	private boolean isNeedHeader(Task previousTask, Task currentTask){
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
		
		if(!Date.equalDate(currentDueDate, previousDueDate)){
			return true;
		}
		else if(!Date.equalDate(currentEndDate, previousEndDate)){
			return true;
		}
		else{
			return false;
		}
	}

	private void formatAsHeader(Task task) {
		String headerStr;

		HEADER_TYPE headerType = getHeaderType(task);

		switch(headerType){
		case OVERDUE_DATE:
			headerStr = newLine() + newLine() + dateFormatAsHeader(task.getTaskDueDate());
			if(task.getEndDate() != null){
				headerStr = headerStr + " to " + dateFormatAsHeader(task.getEndDate());
			}
			strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_DATE));
			break;

		case UPCOMING_DATE:
			headerStr = newLine() + newLine() + addTodayOrTomorrow(task.getTaskDueDate());
			headerStr = headerStr + dateFormatAsHeader(task.getTaskDueDate());
			if(task.getEndDate() != null){
				headerStr = headerStr + " to ";
				headerStr = headerStr + addTodayOrTomorrow(task.getEndDate());
				headerStr = headerStr + dateFormatAsHeader(task.getEndDate());
			}
			
			//add separator if have tasks today
			if(hasTodayTask && thisTaskIsNotTodayTask && !hasSeparator){
				String separator = newLine() + Constants.GUI_SEPARATOR;
				strToShowTemp.add(new StringWithFormat(separator, Constants.FORMAT_NONE));
				hasSeparator = true;
			}
			
			strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_DATE));
			break;
			
		default:
			assert false : "invalid header format";	
		}
	}

	private HEADER_TYPE getHeaderType(Task task){
		if(task.getIsOverdue()){
			return HEADER_TYPE.OVERDUE_DATE;
		}
		else if(task.getTaskDueDate() != null){
			return HEADER_TYPE.UPCOMING_DATE;
		}
		return HEADER_TYPE.INVALID;
	}

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
			return ""; //today or tomorrow is not needed because the task is neither today's nor tomorrow's task
		}
	}
	
	private void formatAsTask(int i, Task task) {
		String description = task.getTaskDescription();
		Time dueStartTime = task.getStartTime();
		Time dueEndTime = task.getEndTime();
		String location = task.getTaskLocation();
		boolean isDone = task.getIsDone();

		strToShowTemp.add(new StringWithFormat(newLine(), Constants.FORMAT_NONE));
		strToShowTemp.add(new StringWithFormat(numbering(i), Constants.FORMAT_NUMBER));

		// add description
		if (isDone) {
			strToShowTemp.add(new StringWithFormat(description, Constants.FORMAT_DONE));
			strToShowTemp.add(new StringWithFormat("   \u2713", Constants.FORMAT_TICK));
		} 
		else {
			strToShowTemp.add(new StringWithFormat(description, Constants.FORMAT_DESCRIPTION));
		}

		// add time
		if (dueStartTime != null && dueEndTime != null) {
			if(isDone){
				String time = timeStr(dueStartTime, dueEndTime);

				strToShowTemp.add(new StringWithFormat(newLine() + "      \u25D5", Constants.FORMAT_DONE));
				strToShowTemp.add(new StringWithFormat(time, Constants.FORMAT_DONE));
			}
			else{
				String time = timeStr(dueStartTime, dueEndTime);

				strToShowTemp.add(new StringWithFormat(newLine() + "      \u25D5", Constants.FORMAT_TIME));
				strToShowTemp.add(new StringWithFormat(time, Constants.FORMAT_NONE));
			}
		} 
		else if (dueStartTime != null) {
			if(isDone){
				String time = timeStr(dueStartTime);
				strToShowTemp.add(new StringWithFormat(newLine() + "      \u25D5", Constants.FORMAT_DONE));
				strToShowTemp.add(new StringWithFormat(time, Constants.FORMAT_DONE));
			}
			else{
				String time = timeStr(dueStartTime);
				strToShowTemp.add(new StringWithFormat(newLine() + "      \u25D5", Constants.FORMAT_TIME));
				strToShowTemp.add(new StringWithFormat(time, Constants.FORMAT_NONE));
			}

		}

		// add location
		if (location != null) {
			if(isDone){
				location = locationStr(location);
				strToShowTemp.add(new StringWithFormat(newLine() + "      @", Constants.FORMAT_DONE));
				strToShowTemp.add(new StringWithFormat(location, Constants.FORMAT_DONE));	
			}
			else{
				location = locationStr(location);
				strToShowTemp.add(new StringWithFormat(newLine() + "      @", Constants.FORMAT_LOCATION));
				strToShowTemp.add(new StringWithFormat(location, Constants.FORMAT_NONE));
			}
		}
	}

	private static String newLine() { // should format to final string
		return "\n";
	}

	private static String numbering(int i) {
		return (i + 1) + ") ";
	}

	private static String timeStr(Time startTime, Time endTime) {
		return startTime.toString() + " - " + endTime.toString();
	}

	private static String timeStr(Time startTime) {
		return startTime.toString();

	}

	private static String locationStr(String location) {
		return location;
	}

	private static String dateFormatAsHeader(Date dueDate) {
		// should somehow change to dueDate.toString() method to lower coupling
		DateParser dp = new DateParser();
		String dayOfTheWeek = dp.getDayOfTheWeek(dueDate);
		return dayOfTheWeek + " " + dueDate.toString();
	}

	public static LinkedList<StringWithFormat> getLinkedList(int num) {
		switch (num) {
		case 1:
			if(strToShow1.isEmpty()){
				strToShow1.add(new StringWithFormat("", Constants.FORMAT_NONE));
				//.add(new StringWithFormat("You do not have any upcoming tasks. ADD NOW!", Constants.FORMAT_NONE));

			}
			return strToShow1;
		case 2:
			if(strToShow2.isEmpty()){
				strToShow2.add(new StringWithFormat("", Constants.FORMAT_NONE));
				//.add(new StringWithFormat("Not a single task without date..HAVE SOME TO ADD?", Constants.FORMAT_NONE));
			}
			return strToShow2;
		case 3:
			if(strToShow3.isEmpty()){
				strToShow3.add(new StringWithFormat("", Constants.FORMAT_NONE));
				//.add(new StringWithFormat("No overdue tasks..THATS GREAT!", Constants.FORMAT_NONE));
			}
			return strToShow3;
		default:
			assert false : "Parameter of FormatTostring.getLinkedList(num) is wrong";
		return strToShow1;
		}
	}

	private static void clearAllLinkedList() {
		strToShow1.clear();
		strToShow2.clear();
		strToShow3.clear();

		strToShow1.add(new StringWithFormat(Constants.HEADER_UPCOMING, Constants.FORMAT_HEADER_UPCOMING));
		strToShow2.add(new StringWithFormat(Constants.HEADER_FLOATING + newLine(), Constants.FORMAT_HEADER_FLOATING));
		strToShow3.add(new StringWithFormat(Constants.HEADER_OVERDUE, Constants.FORMAT_HEADER_OVERDUE));
	}

	public static int getLinkedListNum() {
		return 3;
	}
}