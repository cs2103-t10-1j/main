package lol;

public class CommandLine {

	private String commandType;
	String command;
	
	public CommandLine(String commandType, String command){
	this.commandType = commandType;
	this.command = command;
	}
	
	public String getCommandType (){
		return commandType;
	}
	
	public String getCommand(){
		return command;
	}
}
