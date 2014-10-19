package lol;

import java.util.LinkedList;
import java.util.Stack;
import java.util.Queue;

public class History {

	private static final String undoAddCommandType = "add";
	private static final String undoDeleteCommandType = "delete";
	private static final String undoEditCommandType = "edit";

	private static Stack<CommandLine> undoStack = new Stack<CommandLine>();
	private static Queue<CommandLine> redoQueue = new LinkedList<CommandLine>();

	public static void undoAdd(Task newTask) {
		undoStack.push(new CommandLine(undoAddCommandType, newTask));
	}

	public static void undoDelete(Task newTask) {
		undoStack.push(new CommandLine(undoDeleteCommandType, newTask));
	}

	public static void undoEdit(Task newTask, Task oldTask) {
		undoDelete(oldTask);
		undoAdd(newTask);
		undoStack.push(new CommandLine(undoEditCommandType, null));
	}

	public static CommandLine popUndoStack() {
		assert !undoStack.empty() : "empty undo stack popped";
		return undoStack.pop();
	}

	public static boolean isEmptyUndoStack() {
		return undoStack.empty();
	}

	public static void redoAdd(CommandLine line) {
		redoQueue.add(line);
	}

	public static boolean isEmptyRedoQueue() {
		if (redoQueue.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static CommandLine peekRedoQueue() {
		assert redoQueue.size() != 0 : "empty redo queue peeked";
		return redoQueue.remove();

	}
	
	public static void emptyRedoQueue(){
		redoQueue = new LinkedList<CommandLine>();
	}

}
