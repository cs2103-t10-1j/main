/**
 * 
 */
package io;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;

import lol.Task;
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
		// TODO Auto-generated method stub
		return new TaskList<Task>();
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
		
		try{
        String description = task.getTaskDescription();
		String location = task.getTaskLocation();
		String date = task.getTaskDueDate().toString();
		String endTime = task.getEndTime().getFormat24hr();
		String startTime = task.getStartTime().getFormat24hr();
		String isDone = task.getIsDone()?"true":"false";
		String isOverdue = task.getIsOverdue()?"true":"false";
		
		sb.append(CMD_START);
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
		sb.append(isDone);
		sb.append(CMD_END + WHITESPACE);
		
		sb.append(CMD_START);
		sb.append(isOverdue);
		sb.append(CMD_END + WHITESPACE);
		}catch (NullPointerException e){
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
