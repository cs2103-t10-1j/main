package lol;

import java.util.LinkedList;

import parser.DateParser;

public class FormatToString {
	public static LinkedList<StringWithFormat> strToShow1 = 
			new LinkedList<StringWithFormat>(); //this is for TP1
	
	public static LinkedList<StringWithFormat> strToShow2 = 
			new LinkedList<StringWithFormat>(); //this is for TP2
	
	private static LinkedList<StringWithFormat> strToShowTemp =
			new LinkedList<StringWithFormat>();

	public static boolean hasOverdueHeader = false;
	public static boolean hasFloatingHeader = false;
	public static boolean isFirst = true;

	public FormatToString(){
	}

	public void format(boolean isHeader, Task task, int i, int toBeDisplayedIn){
		switch(toBeDisplayedIn){
		case Constants.DISPLAY_IN_TP1:
			strToShowTemp = strToShow1;
			break;
		case Constants.DISPLAY_IN_TP2:
			strToShowTemp = strToShow2;
			break;
		default:
			assert false : "variable toBeDisplayedIn is incorrect";
		}
		
		if(isHeader){
			formatAsHeader(task);
			isFirst = false;
		}
		else{
			formatAsTask(i, task);
		}
	}
	
	//private void copy(LinkedList<StringWithFormat> list1, LinkedList<StringWithFormat> list2){
		//for(int j = 0; j < list1.size(); j++){
			//StringWithFormat.copy(list1.get(j), list2.get(j));
	//	}
		//list1.clear();
	//}

	private void formatAsHeader(Task task){
		String headerStr;
		
		if(task.getIsOverdue() && !hasOverdueHeader){
			if(!isFirst){
				strToShowTemp.add(new StringWithFormat(newLine(), Constants.FORMAT_NONE));
			}
			
			headerStr = Constants.HEADER_OVERDUE + newLine();
				strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_OVERDUE));
				hasOverdueHeader = true;
				
				headerStr = dateFormatAsHeader(task.getTaskDueDate());
				strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_OVERDUE));
		}
		else if(task.getTaskDueDate() != null){
			if(!isFirst){
				strToShowTemp.add(new StringWithFormat(newLine(), Constants.FORMAT_NONE));
			}
			
			Date dueDate = task.getTaskDueDate();

			headerStr = dateFormatAsHeader(dueDate);
			if(task.getIsOverdue()){
				strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_OVERDUE));
			}
			else{
			strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_NORMAL));
			}
		}
		else{
			if(!hasFloatingHeader){
				headerStr = Constants.HEADER_FLOATING + newLine();
				strToShowTemp.add(new StringWithFormat(headerStr, Constants.FORMAT_HEADER_FLOATING));
				hasFloatingHeader = true;
			}
		}
	}

	private void formatAsTask(int i, Task task){
		String description = task.getTaskDescription();
		Time dueStartTime = task.getStartTime();
		Time dueEndTime = task.getEndTime();
		String location = task.getTaskLocation();
		boolean isDone = task.getIsDone();

		strToShowTemp.add(new StringWithFormat(numbering(i), Constants.FORMAT_NONE));

		//add time with format
		if(dueStartTime != null && dueEndTime != null)
		{
			String time = timeStr(dueStartTime, dueEndTime);
			if(isDone){
				strToShowTemp.add(new StringWithFormat(time, Constants.FORMAT_TIME_STRIKE));
			}
			else{
				strToShowTemp.add(new StringWithFormat(time, Constants.FORMAT_TIME));
			}
		}
		else if(dueStartTime != null){
			String time = timeStr(dueStartTime);
			if(isDone){
				strToShowTemp.add(new StringWithFormat(time, Constants.FORMAT_TIME_STRIKE));
			}
			else{
				strToShowTemp.add(new StringWithFormat(time, Constants.FORMAT_TIME));
			}
		}

		//add description with format
		if(isDone){
			strToShowTemp.add(new StringWithFormat(description, Constants.FORMAT_DESCRIPTION_STRIKE));
		}
		else{
			strToShowTemp.add(new StringWithFormat(description, Constants.FORMAT_DESCRIPTION));
		}

		//add location with format
		if(location != null){
			location = locationStr(location);
			if(isDone){
				strToShowTemp.add(new StringWithFormat(location, Constants.FORMAT_LOCATION_STRIKE));
			}
			else{
				strToShowTemp.add(new StringWithFormat(location, Constants.FORMAT_LOCATION));
			}
		}

		strToShowTemp.add(new StringWithFormat(newLine(), Constants.FORMAT_NONE));
	}


	private static String newLine(){ //should format to final string
		return "\n";
	}

	private static String numbering(int i){
		return (i + 1) + ". ";
	}

	private static String timeStr(Time startTime, Time endTime){
		return "[" + startTime.toString() + " - " + endTime.toString() + "] ";
	}

	private static String timeStr(Time startTime){
		return "[" + startTime.toString() + "] ";

	}

	private static String locationStr(String location){
		return " at [" + location + "]";
	}

	private static String dateFormatAsHeader(Date dueDate){
		//should somehow change to dueDate.toString() method to lower coupling
		DateParser dp = new DateParser(); 
		String dayOfTheWeek = dp.getDayOfTheWeek(dueDate);
		return dayOfTheWeek + " " + dueDate.toString() + newLine();
	}

	public static LinkedList<StringWithFormat> getLinkedList(int num){
		switch(num){
		case 1:
			return strToShow1;
		case 2:
			return strToShow2;
		default:
			assert false : "Parameter of FormatTostring.getLinkedList(num) is wrong";
			return strToShow1;
		}
	}
	
	public static void clearAllLinkedList(){
		strToShow1.clear();
		strToShow2.clear();
	}
	
	public static int getLinkedListNum(){
		return 2;
	}
}