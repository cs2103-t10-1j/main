package lol;

public class CommandLine {

	private String commandType;
	private Task object;
	private int precedingTasks;
	
	public CommandLine(String commandType, Task ob){
	this.commandType = commandType;
	this.object = ob;
	this.precedingTasks = 0;
	}
	
	public void setPrecedingTasks(int num){
		this.precedingTasks = num;
	}
	
	public String getCommandType (){
		return commandType;
	}
	
	public Task getTask(){
		return object;
	}

	public int getPrecedingTasks(){
		return precedingTasks;
	}
}