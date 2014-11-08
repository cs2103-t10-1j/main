/**
 * This class contains methods to load a task list from a text file and to save a task list into a text file.
 */
package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;

import lol.Task;
import lol.Date;
import lol.Time;
import lol.TaskList;

/**
 * @author aviral
 */
public class StorageOperations {

	/************* Attributes ***************/
	
	/**Using special characters to split because a user is unlikely to enter them in description using keyboard*/
	protected static final String NEW_LINE = "\r\n";
	protected static final String WHITESPACE = " ";
	protected static final String CMD_START = "ŒŒ"; 
	protected static final String CMD_END = "þþ";
	protected static final String CMD_SPLIT = CMD_END + WHITESPACE + CMD_START;

	protected String _fileName;

	/**
	 * each index of _commandStrings stores a string converted task in specified
	 * format
	 */
	protected List<String> _commandStrings;
	protected TaskList<Task> _taskList;

	/************* Constructor ***************/
	protected StorageOperations(String fileName) {
		this._fileName = fileName;
		_commandStrings = new ArrayList<String>();
	}

	/************* Other methods ***************/

	/**
	 * loads the task list from text file
	 * 
	 * @return TaskList object
	 */
	protected TaskList<Task> load() {
		boolean isSuccessfulRead = this.readFromFile();
		_taskList = new TaskList<Task>();

		// return loaded task list if no errors in reading else empty taskList
		if (isSuccessfulRead) {
			this.generateTaskList();
			return _taskList;
		} else {
			return _taskList;
		}
	}

	/**
	 * saves the TaskList in text file
	 * 
	 * @param list
	 *            TaskList object
	 */
	protected void save(TaskList<Task> list) {
		_taskList = list;
		this.recreateCommands();
		this.writeToFile();
	}

	/************ Start of methods used for loading **********/

	/**
	 * reads from the text file specified by the LOLControl and saves the tasks
	 * in commandString array
	 * 
	 * @return boolean value
	 */
	private boolean readFromFile() {
		try {

			String command;
			File file = new File(_fileName);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			_commandStrings.clear();

			// stores each line as string into commandStrings array
			while ((command = br.readLine()) != null) {
				_commandStrings.add(command);
			}

			br.close();
			fr.close();

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Converts all the tasks stored as string in commadString to task objects
	 * and stores them in taskList
	 */
	private void generateTaskList() {
		int numberOfTasks = _commandStrings.size();
		Task tempTask;

		for (int i = 0; i < numberOfTasks; i++) {
			tempTask = this.createTask(_commandStrings.get(i));
			_taskList.add(tempTask);
		}

	}

	/**
	 * Converts one string of task to task object by loading in the following
	 * expected format of task string -
	 * 
	 * <description>þþ ŒŒ<location>þþ ŒŒ<startDate>þþ ŒŒ<startTime>þþ
	 * ŒŒ<endTime>þþ ŒŒ<isDone>þþ ŒŒ<isOverdue>þþ ŒŒ<endDate>
	 * 
	 * @param command
	 *            String form of task
	 * @return Task object
	 */
	private Task createTask(String command) {
		String description = "", location = "", startDate = "", startTime = "", endTime = "", done = "", overdue = "", endDate = "";
		Date startDateOb, endDateOb;
		Time startTimeOb, endTimeOb;
		boolean isDone, isOverdue;

		String[] commandComponents = command.split(CMD_SPLIT);

		try {
			description = commandComponents[0];
			location = commandComponents[1];
			startDate = commandComponents[2];
			startTime = commandComponents[3];
			endTime = commandComponents[4];
			done = commandComponents[5];
			overdue = commandComponents[6];
			endDate = commandComponents[7];
		} catch (Exception e) {
			System.out.println("file not in correct format");
		}

		startTimeOb = getTime(startTime);
		endTimeOb = getTime(endTime);
		startDateOb = getDate(startDate);
		endDateOb = getDate(endDate);
		isDone = Boolean.parseBoolean(done);
		isOverdue = Boolean.parseBoolean(overdue);

		Task task = new Task(description, location, startDateOb, startTimeOb,
				endTimeOb);
		task.setIsDone(isDone);
		task.setIsOverdue(isOverdue);
		if (location.equals("null"))
			task.setLocation(null);
		task.setEndDate(endDateOb);
		return task;
	}

	/**
	 * This function converts the date written in the format of dd/mm/yy to a
	 * Date class objects and return it.
	 * 
	 * @param String
	 *            date
	 * @return Date object
	 */
	private Date getDate(String date) {
		if (!date.equals("null")) {
			String[] dateComponents = date.split("/");
			int day, month, year;
			day = month = year = 0;

			try {
				day = Integer.parseInt(dateComponents[0]);
				month = Integer.parseInt(dateComponents[1]);
				year = Integer.parseInt(dateComponents[2]);
			} catch (Exception e) {
				System.out.println("date not in correct format");
			}

			if (day != 0 && month != 0 && year != 0) {
				return new Date(day, month, year);
			}
		}
		return null;
	}

	/**
	 * This function converts the time string to Time class object.
	 * 
	 * @param String
	 *            time
	 * @return Time object
	 */
	private Time getTime(String time) {
		if (!time.equals("null")) {
			return new Time(time);
		} else
			return null;
	}

	/************ End of methods used for loading **********/

	/************ Start of methods used for saving **********/

	/**
	 * This function recreates the task object as string and stores in
	 * commandsStrings array
	 */
	private void recreateCommands() {
		int numberOfTasks = _taskList.size();
		String command;

		_commandStrings.clear();

		// storing each task as a String line in commandStrings array
		for (int i = 0; i < numberOfTasks; i++) {
			command = generateCommand(_taskList.get(i));
			_commandStrings.add(command);

		}

	}

	/**
	 * this method transforms the task object to string type using particular
	 * format to append different attributes of tasks format of saving-
	 * 
	 * <description>þþ ŒŒ<location>þþ ŒŒ<startDate>þþ ŒŒ<startTime>þþ
	 * ŒŒ<endTime>þþ ŒŒ<isDone>þþ ŒŒ<isOverdue>þþ ŒŒ<endDate>
	 * 
	 * @param task
	 *            object
	 * @return String of task object
	 */
	private String generateCommand(Task task) {
		StringBuilder sb = new StringBuilder();

		try {
			String description = task.getTaskDescription();
			String location = task.getTaskLocation();

			Date startDateOb = task.getTaskDueDate();
			String startDate = null;
			if (startDateOb != null) {
				startDate = startDateOb.toString2();
			}

			String endTime = null;
			String startTime = null;
			Time endTimeOb = task.getEndTime();
			Time startTimeOb = task.getStartTime();

			if (endTimeOb != null) {
				endTime = endTimeOb.getFormat24hr();
			}

			if (startTimeOb != null) {
				startTime = startTimeOb.getFormat24hr();
			}

			String done = task.getIsDone() ? "true" : "false";
			String overdue = task.getIsOverdue() ? "true" : "false";

			Date endDateOb = task.getEndDate();
			String endDate = null;
			if (endDateOb != null)
				endDate = endDateOb.toString2();

			sb.append(description);
			sb.append(CMD_END + WHITESPACE);

			sb.append(CMD_START);
			sb.append(location);
			sb.append(CMD_END + WHITESPACE);

			sb.append(CMD_START);
			sb.append(startDate);
			sb.append(CMD_END + WHITESPACE);

			sb.append(CMD_START);
			sb.append(startTime);
			sb.append(CMD_END + WHITESPACE);

			sb.append(CMD_START);
			sb.append(endTime);
			sb.append(CMD_END + WHITESPACE);

			sb.append(CMD_START);
			sb.append(done);
			sb.append(CMD_END + WHITESPACE);

			sb.append(CMD_START);
			sb.append(overdue);
			sb.append(CMD_END + WHITESPACE);

			sb.append(CMD_START);
			sb.append(endDate);

		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return sb.toString();

	}

	/**
	 * This method writes the tasks saved as String in commandStrings array in a
	 * text file
	 */
	private void writeToFile() {
		try {
			FileWriter fw = new FileWriter(_fileName);
			BufferedWriter bw = new BufferedWriter(fw);

			while (_commandStrings.size() != 0) {
				bw.write(_commandStrings.remove(0) + NEW_LINE);
			}

			bw.close();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/************ End of methods used for saving **********/

}
