/**
 * This class helps to associate the task object with the command type so that it can be undo or re-do 
 */
package lol;

/**
 * 
 * @author aviral
 *
 */
public class CommandLine {

	/******************* Attributes *********************/
	private String commandType;
	private Task object;
	private int precedingTasks; // keeps the count of tasks/commandLines before
								// this instance for mass undo and redo

	/******************* Constructor *********************/
	public CommandLine(String commandType, Task ob) {
		this.commandType = commandType;
		this.object = ob;
		this.precedingTasks = 0;
	}

	/******************* Accessors *********************/
	public String getCommandType() {
		return commandType;
	}

	public Task getTask() {
		return object;
	}

	public int getPrecedingTasks() {
		return precedingTasks;
	}

	/******************* Mutators *********************/
	public void setPrecedingTasks(int num) {
		this.precedingTasks = num;
	}

}