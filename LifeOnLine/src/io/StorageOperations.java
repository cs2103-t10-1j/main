/**
 * 
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
 * @author owner
 *
 */
public class StorageOperations {

	// special characters
	protected static final String NEW_LINE = "\r\n";
	protected static final String WHITESPACE = " ";
	protected static final String CMD_START = "ŒŒ";
	protected static final String CMD_END = "þþ";
	protected static final String CMD_SPLIT = CMD_END + WHITESPACE + CMD_START;

	protected String fileName;
	protected List<String> commandStrings;
	protected TaskList<Task> taskList;

	protected StorageOperations(String fileName) {
		this.fileName = fileName;
		commandStrings = new ArrayList<String>();
	}

	protected TaskList<Task> load() {
		boolean isSuccessfulRead =this.readFromFile();
		taskList = new TaskList<Task>();
		if (isSuccessfulRead) {
			this.generateTaskList();
			return taskList;
		} else {
			return taskList;
		}
	}

	private boolean readFromFile() {
		try {

			String command;
			File file = new File(fileName);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			commandStrings.clear();
			while ((command = br.readLine()) != null) {
				commandStrings.add(command);
			}

			br.close();
			fr.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void generateTaskList() {
		int numberOfTasks = commandStrings.size();
		Task tempTask;

		for (int i = 0; i < numberOfTasks; i++) {
			tempTask = this.createTask(commandStrings.get(i));
			taskList.add(tempTask);
		}

	}

	private Task createTask(String command) {
 		String description = "", location = "", date = "", startTime = "", endTime = "", done = "", overdue = "";
		Date dateOb;
		Time startTimeOb, endTimeOb;
		boolean isDone, isOverdue;

		
		String[] commandComponents = command.split(CMD_SPLIT);
		int i = commandComponents.length;
		if(i==7){
		description = commandComponents[0];
		location = commandComponents[1];
		date = commandComponents[2];
		startTime = commandComponents[3];
		endTime = commandComponents[4];
		done = commandComponents[5];
		overdue = commandComponents[6];
		}
		
		
		System.out.println(i + " " + description +"l"+ location+"d"+ date+"strt"+ startTime +"end"+endTime+" "+done+" "+ overdue);

		startTimeOb = getTime(startTime);
		endTimeOb = getTime(endTime);
		dateOb = getDate(date, startTimeOb);
		isDone = Boolean.parseBoolean(done);
		isOverdue = Boolean.parseBoolean(overdue);
		
		Task task = new Task(description, location, dateOb, startTimeOb, endTimeOb);
		task.setIsDone(isDone);
		task.setIsDone(isOverdue);
		if(location.equals("null"))
			task.setLocation(null);
		return task;
		/* catch (Exception e){
			return null;
		} */
		
	}

	private Date getDate(String date, Time startTimeOb) {
		if (!date.equals("null")){
			String[] dateComponents = date.split("/");
			int day = Integer.parseInt(dateComponents[0]);
			int month = Integer.parseInt(dateComponents[1]);
			int year = Integer.parseInt(dateComponents[2]);
			
			return new Date(day, month, year, startTimeOb);
		}
		return null;
	}

	private Time getTime(String time) {
		if (!time.equals("null")) {
			return new Time(time);
		} else
			return null;
	}

	protected void save(TaskList<Task> list) {
		taskList = list;
		this.recreateCommands();
		this.writeToFile();
	}

	private void recreateCommands() {
		int numberOfTasks = taskList.size();
		String command;

		commandStrings.clear();
		for (int i = 0; i < numberOfTasks; i++) {
			command = generateCommand(taskList.get(i));
			commandStrings.add(command);

		}

	}

	private String generateCommand(Task task) {
		StringBuilder sb = new StringBuilder();

		try {
			String description = task.getTaskDescription();
			String location = task.getTaskLocation();
			Date dateOb = task.getTaskDueDate();
			String date = null;
			if (dateOb != null)
				date = dateOb.toString2();
			String endTime = null;
			String startTime = null;
			Time endTimeOb = task.getEndTime();
			Time startTimeOb = task.getStartTime();
			if (endTimeOb != null)
				endTime = endTimeOb.getFormat24hr();
			if (startTimeOb != null)
				startTime = startTimeOb.getFormat24hr();
			String done = task.getIsDone() ? "true" : "false";
			String overdue = task.getIsOverdue() ? "true" : "false";

			sb.append(description);
			sb.append(CMD_END + WHITESPACE);

			sb.append(CMD_START);
			sb.append(location);
			sb.append(CMD_END + WHITESPACE);

			sb.append(CMD_START);
			sb.append(date);
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

		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return sb.toString();

	}

	private void writeToFile() {
		try {
			FileWriter fw = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(fw);

			while (commandStrings.size() != 0) {
				bw.write(commandStrings.remove(0) + NEW_LINE);
			}

			bw.close();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
