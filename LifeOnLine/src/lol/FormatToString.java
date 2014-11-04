package lol;

import java.util.LinkedList;

import parser.DateParser;

public class FormatToString {
	private static LinkedList<StringWithFormat> strToShow1 = new LinkedList<StringWithFormat>(); //for TP1

	private static LinkedList<StringWithFormat> strToShow2 = new LinkedList<StringWithFormat>(); //for TP2

	private static LinkedList<StringWithFormat> strToShow3 = new LinkedList<StringWithFormat>(); //for TP3

	private static LinkedList<StringWithFormat> strToShowTemp = new LinkedList<StringWithFormat>();

	private static boolean hasOverdueHeader = false;
	private static boolean hasFloatingHeader = false;
	private static boolean hasUpcomingHeader = false;
	
	private static boolean hasTodayTask = false;
	private static boolean thisTaskIsNotTodayTask = false;
	private static boolean hasSeparator = false; //separator here is the separator between today's tasks and other upcoming tasks


	private enum HEADER_TYPE{
		OVERDUE_HEADER_AND_DATE, UPCOMING_HEADER_AND_DATE, OVERDUE_DATE,
		UPCOMING_DATE, FLOATING_HEADER, INVALID;
	}

	public FormatToString() {
		clearAllLinkedList();
		hasOverdueHeader = false;
		hasFloatingHeader = false;
		hasUpcomingHeader = false;
		hasTodayTask = false;
	}

	public void format(boolean isHeader, Task task, int i, int toBeDisplayedIn) {
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
		default:
			assert false : "variable toBeDisplayedIn is incorrect";
		}

		if (isHeader) {
			formatAsHeader(task);
		} else {
			formatAsTask(i, task);
		}
	}

	// private void copy(LinkedList<StringWithFormat> list1,
	// LinkedList<StringWithFormat> list2){
	// for(int j = 0; j < list1.size(); j++){
	// StringWithFormat.copy(list1.get(j), list2.get(j));
	// }
	// list1.clear();
	// }

	private void formatAsHeader(Task task) {
		String headerStr;

		HEADER_TYPE headerType = getHeaderType(task);

		switch(headerType){
		case OVERDUE_HEADER_AND_DATE:
			headerStr = Constants.HEADER_OVERDUE;
			strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_OVERDUE));
			hasOverdueHeader = true;

			headerStr = newLine() + newLine() + dateFormatAsHeader(task.getTaskDueDate());
			strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_DATE));
			break;

		case OVERDUE_DATE:
			headerStr = newLine() + newLine() + dateFormatAsHeader(task.getTaskDueDate());
			strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_DATE));
			break;

		case UPCOMING_HEADER_AND_DATE:
			headerStr = Constants.HEADER_UPCOMING;
			strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_UPCOMING));
			hasUpcomingHeader = true;

			headerStr = newLine() + newLine();
			headerStr = headerStr + addTodayOrTomorrow(task);
			headerStr = headerStr + dateFormatAsHeader(task.getTaskDueDate());

			strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_DATE));
			break;

		case UPCOMING_DATE:
			headerStr = newLine() + newLine();
			headerStr = headerStr + addTodayOrTomorrow(task);
			headerStr = headerStr + dateFormatAsHeader(task.getTaskDueDate());

			if(hasTodayTask && thisTaskIsNotTodayTask && !hasSeparator){
				String separator = newLine() + Constants.GUI_SEPARATOR;
				strToShowTemp.add(new StringWithFormat(separator, Constants.FORMAT_NONE));
				hasSeparator = true;
			}
			
			strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_DATE));
			break;

		case FLOATING_HEADER:
			if (!hasFloatingHeader) {
				headerStr = Constants.HEADER_FLOATING + newLine();

				strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_FLOATING));
				hasFloatingHeader = true;
			}
			break;

		default:
			assert false : "invalid header format";	
		}
	}

	private HEADER_TYPE getHeaderType(Task task){
		//overdue tasks
		if(task.getIsOverdue() && !hasOverdueHeader){
			hasOverdueHeader = true;
			return HEADER_TYPE.OVERDUE_HEADER_AND_DATE;
		}
		else if(task.getIsOverdue() && hasOverdueHeader){
			return HEADER_TYPE.OVERDUE_DATE;
		}
		//upcoming tasks
		else if(task.getTaskDueDate() != null && !hasUpcomingHeader){
			hasUpcomingHeader = true;
			return HEADER_TYPE.UPCOMING_HEADER_AND_DATE;
		}
		else if(task.getTaskDueDate() != null && hasUpcomingHeader){
			return HEADER_TYPE.UPCOMING_DATE;
		}
		//floating tasks
		else{
			if(!hasFloatingHeader){
				hasFloatingHeader = true;
				return HEADER_TYPE.FLOATING_HEADER;
			}
		}
		return HEADER_TYPE.INVALID;
	}

	private String addTodayOrTomorrow(Task task){
		Date dueDate = task.getTaskDueDate();

		if(dueDate.getDay() == dueDate.getCurrentDay() 
				&& dueDate.getMonth() == dueDate.getCurrentMonth() 
				&& dueDate.getYear4Digit() == dueDate.getCurrentYear()){
			hasTodayTask = true;
			thisTaskIsNotTodayTask = false;
			return "Today, ";
		}
		else if(dueDate.getDay() == dueDate.getCurrentDay()+1 && 
				dueDate.getMonth() == dueDate.getCurrentMonth() && 
				dueDate.getYear4Digit() == dueDate.getCurrentYear()){
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

		// description
		if (isDone) {
			strToShowTemp.add(new StringWithFormat(description, Constants.FORMAT_DONE));
			strToShowTemp.add(new StringWithFormat("   \u2713", Constants.FORMAT_TICK));
		} else {
			strToShowTemp.add(new StringWithFormat(description, Constants.FORMAT_DESCRIPTION));
		}

		// add time with format
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

		} else if (dueStartTime != null) {
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

		// add location with format
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
	}

	public static int getLinkedListNum() {
		return 3;
	}
}