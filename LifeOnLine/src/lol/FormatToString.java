package lol;

import java.util.LinkedList;

public class FormatToString {
	public static LinkedList<StringWithFormat> strToShow = 
			new LinkedList<StringWithFormat>();

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

	public FormatToString(){
	}

	public void format(boolean isHeader, Task task, int i){
		if(isHeader){
			formatAsHeader(task);
		}
		else{
			formatAsTask(i, task);
		}
	}

	private void formatAsHeader(Task task){
		String headerStr;
		if(task.getIsOverdue()){
			headerStr = HEADER_OVERDUE + newLine();
			strToShow.add(new StringWithFormat(headerStr, FORMAT_HEADER_OVERDUE));
		}
		else if(task.getTaskDueDate() != null){
			Date dueDate = task.getTaskDueDate();
			int dueDay = dueDate.getDay();
			int dueMonth = dueDate.getMonth();
			int dueYear = dueDate.getYear4Digit();

			headerStr = dateFormatAsHeader(dueDay, dueMonth, dueYear);
			strToShow.add(new StringWithFormat(headerStr, FORMAT_HEADER_NORMAL));
		}
		else{
			headerStr = HEADER_FLOATING + newLine();
			strToShow.add(new StringWithFormat(headerStr, FORMAT_HEADER_FLOATING));
		}
	}

	private void formatAsTask(int i, Task task){
		String description = task.getTaskDescription();
		Time dueStartTime = task.getStartTime();
		Time dueEndTime = task.getEndTime();
		String location = task.getTaskLocation();
		boolean isDone = task.getIsDone();

		strToShow.add(new StringWithFormat(numbering(i), FORMAT_NONE));

		//add time with format
		if(dueStartTime != null && dueEndTime != null)
		{
			String time = timeStr(dueStartTime, dueEndTime);
			if(isDone){
				strToShow.add(new StringWithFormat(time, FORMAT_TIME_STRIKE));
			}
			else{
				strToShow.add(new StringWithFormat(time, FORMAT_TIME));
			}
		}
		else if(dueStartTime != null){
			String time = timeStr(dueStartTime);
			if(isDone){
				strToShow.add(new StringWithFormat(time, FORMAT_TIME_STRIKE));
			}
			else{
				strToShow.add(new StringWithFormat(time, FORMAT_TIME));
			}
		}

		//add description with format
		if(isDone){
			strToShow.add(new StringWithFormat(description, FORMAT_DESCRIPTION_STRIKE));
		}
		else{
			strToShow.add(new StringWithFormat(description, FORMAT_DESCRIPTION));
		}

		//add location with format
		if(location != null){
			location = locationStr(location);
			if(isDone){
				strToShow.add(new StringWithFormat(location, FORMAT_LOCATION_STRIKE));
			}
			else{
				strToShow.add(new StringWithFormat(location, FORMAT_LOCATION));
			}
		}

		strToShow.add(new StringWithFormat(newLine(), FORMAT_NONE));

		//for(int k = 0; i < strToShow.size(); k++){
		//	System.out.println(strToShow.get(k));
		//}
	}


	public static String newLine(){ //should format to final string
		return "\n";
	}

	public static String numbering(int i){
		return (i + 1) + ". ";
	}

	public static String timeStr(Time startTime, Time endTime){
		return "[" + startTime.toString() + " - " + endTime.toString() + "] ";
	}

	public static String timeStr(Time startTime){
		return "[" + startTime.toString() + "] ";

	}

	public static String locationStr(String location){
		return " at [" + location + "]";
	}

	public static String dateFormatAsHeader(int dueDay, int dueMonth, int dueYear){
		return dueDay + "/" + dueMonth + "/" + dueYear + newLine();
	}

	public static LinkedList<StringWithFormat> getLinkedList(){
		return strToShow;
	}
}