package lol;

public class CommandLine {

	private String commandType;
	private Task object;
	
	public CommandLine(String commandType, Task ob){
	this.commandType = commandType;
	this.object = ob;
	}
	
	public String getCommandType (){
		return commandType;
	}
	
	public Task getTask(){
		return object;
	}
}