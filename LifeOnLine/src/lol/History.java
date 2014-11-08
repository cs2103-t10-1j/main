package lol;


import java.util.EmptyStackException;
import java.util.Stack;


public class History {

	private static final String undoAddCommandType = "add";
	private static final String undoDeleteCommandType = "delete";
	private static final String undoEditCommandType = "edit";

	private static Stack<CommandLine> undoStack = new Stack<CommandLine>();
	private static Stack<CommandLine> redoStack = new Stack<CommandLine>();

	public static void undoAdd(Task newTask) {
		undoStack.push(new CommandLine(undoAddCommandType, newTask));
	}

	public static void undoDelete(Task newTask) {
		undoStack.push(new CommandLine(undoDeleteCommandType, newTask));
		newTask.setIsJustAdded(true);
	}

	public static void undoEdit(Task newTask, Task oldTask) {
		undoDelete(oldTask);
		undoAdd(newTask);
		undoStack.push(new CommandLine(undoEditCommandType, null));
		newTask.setIsJustAdded(true);
	}

	public static CommandLine popUndoStack() {
		assert !undoStack.empty() : "empty undo stack popped";
		return undoStack.pop();
	}

	public static boolean isEmptyUndoStack() {
		return undoStack.empty();
	}

	public static void redoAdd(CommandLine line) {
		redoStack.push(line);
	}

	public static boolean isEmptyRedoStack() {
		return redoStack.empty();
	}

	public static CommandLine popRedoStack() {
		assert !redoStack.empty() : "empty redo stack popped";
		return redoStack.pop();
	}
	
	public static void emptyRedoStack(){
		redoStack = new Stack<CommandLine>();
	}
	
	public static CommandLine peekUndoStack(){
		try{
			return undoStack.peek();
		} catch (EmptyStackException e){
			return null;
		}
	}
	public static CommandLine peekRedoStack(){
		try{
			return redoStack.peek();
		} catch (EmptyStackException e){
			return null;
		}
	}

}
