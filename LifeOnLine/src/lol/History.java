package lol;

import java.util.Stack;

public class History {
	
	private static final String undoAddCommandType = "delete";
	private static final String undoDeleteCommandType = "add";
	
	
	private static Stack<CommandLine> undoStack = new Stack<CommandLine>();
	
	public static void undoAdd (Task newTask){
		undoStack.push(new CommandLine(undoAddCommandType, newTask));
	}
	
	public static void undoDelete (Task newTask){
		undoStack.push(new CommandLine(undoDeleteCommandType, newTask));
	}
	
	public static void undoEdit (Task newTask, Task oldTask)
	{
		undoDelete(oldTask);
		undoAdd(newTask);
	}
	
	public static CommandLine pop()
	{
		if(undoStack.empty()){
			return null;
		} else
			return undoStack.pop();
	}
	
	public static boolean isEmpty()
	{
		return undoStack.empty();
	}
	
}
