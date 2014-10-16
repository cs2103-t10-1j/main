package lol;

import java.util.logging.*;

public class LOLControl {

	private static Logger logger = Logger.getLogger("LOLControl");

	/********** Load Storage ***********/

	public static TaskList<Task> list = LOLStorage.load();

	/********** Controller methods ***********/

	public static TaskList<Task> getTaskList() {
		return list;
	}

	public static String executeUserInput(String userInput) {

		if (getCommandType(userInput).equals(Constants.COMMAND_ADD)) {
			return executeAdd(userInput);
		}

		if (getCommandType(userInput).equals(Constants.COMMAND_DELETE)) {
			return executeDel(userInput);
		}

		if (getCommandType(userInput).equals(Constants.COMMAND_EDIT)) {
			return executeEdit(userInput);
		}

		if (getCommandType(userInput).equals(Constants.COMMAND_DONE)) {
			return executeDone(userInput);
		}

		if (getCommandType(userInput).equals(Constants.COMMAND_NOT_DONE)) {
			return executeNotDone(userInput);
		}

		if (getCommandType(userInput).equals(Constants.COMMAND_UNDO)) {
			return executeUndo(userInput);
		}
		if (getCommandType(userInput).equals(Constants.COMMAND_REDO)) {
			return executeRedo(userInput);
		}
		if (getCommandType(userInput).equals(Constants.COMMAND_EXIT)) {
			return executeExit(userInput);
		}

		else {
			logger.log(Level.WARNING,
					"Processing Error, unsupported CommandType entered");
			return executeInvalid(userInput);
		}
	}

	public static String getCommandType(String userInput) {
		String command = LOLParser.getCommandName(userInput);
		return command;
	}

	public static String executeAdd(String userInput) {
		Task newTask = LOLParser.getTask(userInput);

		if (list.add(newTask)) {
			History.undoAdd(newTask);
			list.sortList();
			LOLStorage.save();
			return showFeedback(newTask, Constants.COMMAND_ADD);
		} else
			return executeInvalid(userInput);
	}

	public static String executeDel(String userInput) {
		int taskIndex = LOLParser.getTaskIndex(userInput);
		Task delTask = list.get(taskIndex - 1);

		if (list.deleteByIndex(taskIndex - 1)) {
			History.undoDelete(delTask);
			LOLStorage.save();
			return showFeedback(delTask, Constants.COMMAND_DELETE);
		} else
			logger.log(Level.WARNING,
					"Processing Error, deleting invalid index");
		return executeInvalid(userInput);
	}

	public static String executeEdit(String userInput) {
		int taskIndex = LOLParser.getTaskIndex(userInput);
		Task editTask = LOLParser.getEditTask(userInput);
		Task oldTask = list.get(taskIndex - 1);

		if ((list.deleteByIndex(taskIndex - 1)) && (list.add(editTask))) {
			History.undoEdit(editTask, oldTask);
			list.sortList();
			LOLStorage.save();
			return showFeedback(oldTask, Constants.COMMAND_EDIT);
		} else
			return executeInvalid(userInput);
	}

	public static String executeSearch(String userInput) {
	//TODO	
		return null;
	}

	public static String executeDone(String userInput) {
		int taskIndex = LOLParser.getTaskIndex(userInput);

		try {
			Task undoneTask = list.get(taskIndex - 1);

			if (undoneTask.getIsDone()) {
				return Constants.FEEDBACK_DONE_FAILURE;
			}

			Task doneTask = new Task(undoneTask.getTaskDescription(),
					undoneTask.getTaskLocation(), undoneTask.getTaskDueDate(),
					undoneTask.getStartTime(), undoneTask.getEndTime());

			doneTask.setIsDone(true);

			if (list.set(taskIndex - 1, doneTask)) {
				History.undoEdit(doneTask, undoneTask);
				LOLStorage.save();
				return showFeedback(doneTask, Constants.COMMAND_DONE);
			}
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Processing Error, task does not exist");
		}
		return executeInvalid(userInput);
	}

	public static String executeNotDone(String userInput) {
		int taskIndex = LOLParser.getTaskIndex(userInput);

		try {
			Task doneTask = list.get(taskIndex - 1);

			if (!doneTask.getIsDone()) {
				return Constants.FEEDBACK_NOT_DONE_FAILURE;
			}

			Task notDoneTask = new Task(doneTask.getTaskDescription(),
					doneTask.getTaskLocation(), doneTask.getTaskDueDate(),
					doneTask.getStartTime(), doneTask.getEndTime());

			notDoneTask.setIsDone(false);

			if (list.set(taskIndex - 1, notDoneTask)) {
				History.undoEdit(notDoneTask, doneTask);
				LOLStorage.save();
				return showFeedback(notDoneTask, Constants.COMMAND_NOT_DONE);
			}
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Processing Error, task does not exist");
		}
		return executeInvalid(userInput);
	}

	public static String executeUndo(String userInput) {
		if (History.isEmptyUndoStack()) {
			return Constants.FEEDBACK_UNDO_FAILURE;
		}

		else {

			CommandLine undoCmd = History.popUndoStack();

			String undoCmdType = undoCmd.getCommandType();
			Task undoCmdTask = undoCmd.getTask();

			if (undoCmdType.equals(Constants.COMMAND_DELETE)) {
				list.add(undoCmdTask);
				list.sortList();
				History.redoAdd(undoCmd);
				LOLStorage.save();
			}

			if (undoCmdType.equals(Constants.COMMAND_ADD)) {
				list.delete(undoCmdTask);
				History.redoAdd(undoCmd);
				LOLStorage.save();
			}
			if (undoCmdType.equals(Constants.COMMAND_EDIT)) {

				CommandLine undoCmdNew = History.popUndoStack();
				CommandLine undoCmdOld = History.popUndoStack();
				Task undoCmdTaskNew = undoCmdNew.getTask();
				Task undoCmdTaskOld = undoCmdOld.getTask();

				list.delete(undoCmdTaskNew);
				list.add(undoCmdTaskOld);
				list.sortList();
				History.redoAdd(undoCmd);
				History.redoAdd(undoCmdOld);
				History.redoAdd(undoCmdNew);
				LOLStorage.save();

			}

			return showFeedback(null, Constants.COMMAND_UNDO);
		}
	}

	public static String executeRedo(String userInput) {
		if (History.isEmptyRedoQueue()) {
			return Constants.FEEDBACK_REDO_FAILURE;
		}

		else {

			CommandLine undoCmd = History.peekRedoQueue();

			String undoCmdType = undoCmd.getCommandType();
			Task undoCmdTask = undoCmd.getTask();

			if (undoCmdType.equals(Constants.COMMAND_DELETE)) {
				list.delete(undoCmdTask);
				History.undoDelete(undoCmdTask);
				LOLStorage.save();
			}

			if (undoCmdType.equals(Constants.COMMAND_ADD)) {
				list.add(undoCmdTask);
				list.sortList();
				History.undoAdd(undoCmdTask);
				LOLStorage.save();
			}
			if (undoCmdType.equals(Constants.COMMAND_EDIT)) {

				CommandLine undoCmdOld = History.peekRedoQueue();
				CommandLine undoCmdNew = History.peekRedoQueue();
				Task undoCmdTaskNew = undoCmdNew.getTask();
				Task undoCmdTaskOld = undoCmdOld.getTask();

				list.add(undoCmdTaskNew);
				list.delete(undoCmdTaskOld);
				list.sortList();
				History.undoEdit(undoCmdTaskNew, undoCmdTaskOld);
				LOLStorage.save();

			}

			return showFeedback(null, Constants.COMMAND_REDO);
		}
	}

	public static String executeExit(String userInput) {
		System.exit(0);
		return showFeedback(null, Constants.COMMAND_EXIT);
	}

	public static String executeInvalid(String userInput) {
		Task invalidTask = LOLParser.getTask(userInput);
		return showFeedback(invalidTask, Constants.COMMAND_INVALID);
	}

	public static String showFeedback(Task task, String commandType) {
		if (commandType.equals(Constants.COMMAND_ADD)) {
			return (Constants.QUOTE + task + Constants.QUOTE + Constants.FEEDBACK_ADD_SUCCESS);
		}
		if (commandType.equals(Constants.COMMAND_DELETE)) {
			return (Constants.QUOTE + task + Constants.QUOTE + Constants.FEEDBACK_DEL_SUCCESS);
		}
		if (commandType.equals(Constants.COMMAND_EDIT)) {
			return (Constants.QUOTE + task + Constants.QUOTE + Constants.FEEDBACK_EDIT_SUCCESS);
		}
		if (commandType.equals(Constants.COMMAND_DONE)) {
			return (Constants.QUOTE + task + Constants.QUOTE + Constants.FEEDBACK_DONE_SUCCESS);
		}
		if (commandType.equals(Constants.COMMAND_NOT_DONE)) {
			return (Constants.QUOTE + task + Constants.QUOTE + Constants.FEEDBACK_NOT_DONE_SUCCESS);
		}
		if (commandType.equals(Constants.COMMAND_UNDO)) {
			return (Constants.FEEDBACK_UNDO_SUCCESS);
		}
		if (commandType.equals(Constants.COMMAND_REDO)) {
			return (Constants.FEEDBACK_REDO_SUCCESS);
		}
		if (commandType.equals(Constants.COMMAND_EXIT)) {
			return (null);
		} else
			return (Constants.FEEDBACK_INVALID);
	}
}