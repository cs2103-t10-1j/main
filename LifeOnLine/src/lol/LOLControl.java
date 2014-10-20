package lol;

import java.util.logging.*;

import io.StorageFacade;
public class LOLControl {

	private static Logger logger = Logger.getLogger("LOLControl");

	/********** Load Storage ***********/
	private static StorageFacade LOLStorage = StorageFacade.getInstance("LOL.txt");

	private static TaskList<Task> storageList = LOLStorage.loadTasks();

	/********** Initialize Temporary Storage ***********/

	private static TaskList<Task> searchList = new TaskList<Task>();
	private static TaskList<Task> displayList = new TaskList<Task>();

	/********** Controller methods ***********/

	public static TaskList<Task> getTaskList() {
		return displayList;
	}

	public static String executeUserInput(String userInput) throws Exception {

		if (getCommandType(userInput).equals(Constants.COMMAND_ADD)) {
			return executeAdd(userInput);
		}

		if (getCommandType(userInput).equals(Constants.COMMAND_DELETE)) {
			return executeDel(userInput);
		}

		if (getCommandType(userInput).equals(Constants.COMMAND_EDIT)) {
			return executeEdit(userInput);
		}

		if (getCommandType(userInput).equals(Constants.COMMAND_SHOW)) {
			return executeShow(userInput);
		}

		if (getCommandType(userInput).equals(Constants.COMMAND_SEARCH)) {
			return executeSearch(userInput);
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

		if (getCommandType(userInput).equals(Constants.COMMAND_VIEW_HOMESCREEN)) {
			return executeHomeScreen(userInput);
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

	private static String executeAdd(String userInput) throws Exception {
		if (LOLParser.getTask(userInput) == null) {
			return executeInvalid(userInput);
		}

		else {
			Task newTask = LOLParser.getTask(userInput);

			if (storageList.add(newTask)) {
				History.undoAdd(newTask);
				ControlDisplay.refreshDisplay(storageList);
				LOLStorage.saveTasks(storageList);
				return showFeedback(newTask, Constants.COMMAND_ADD);
			} else
				return executeInvalid(userInput);
		}
	}

	private static String executeDel(String userInput) {
		int taskIndex = LOLParser.getTaskIndex(userInput);
		Task delTask = displayList.get(taskIndex - 1);

		if (storageList.delete(delTask)) {
			History.undoDelete(delTask);
			ControlDisplay.refreshDisplay(storageList);
			LOLStorage.saveTasks(storageList);
			return showFeedback(delTask, Constants.COMMAND_DELETE);
		} else
			logger.log(Level.WARNING,
					"Processing Error, deleting invalid index");
		return executeInvalid(userInput);
	}

	private static String executeEdit(String userInput) {
		int taskIndex = LOLParser.getTaskIndex(userInput);
		Task taskAtIndex = displayList.get(taskIndex - 1);
		Task oldTask = new Task(taskAtIndex.getTaskDescription(),
				taskAtIndex.getTaskLocation(), taskAtIndex.getTaskDueDate(),
				taskAtIndex.getStartTime(), taskAtIndex.getEndTime());
		Task editTask = LOLParser.getEditTask(userInput, oldTask);

		if ((storageList.delete(taskAtIndex)) && (storageList.add(editTask))) {
			History.undoEdit(editTask, taskAtIndex);
			ControlDisplay.refreshDisplay(storageList);
			LOLStorage.saveTasks(storageList);
			return showFeedback(oldTask, Constants.COMMAND_EDIT);
		} else
			return executeInvalid(userInput);
	}

	private static String executeShow(String userInput) {
		Date searchDate = LOLParser.getDateForShowCommand(userInput);

		if (searchDate == null) {
			return executeInvalid(userInput);
		}
		Task searchTask = new Task(null, null, searchDate);

		if (storageList.size() == 0) {
			return Constants.FEEDBACK_SHOW_FAILURE;
		}

		else {
			int count = 0;
			searchList.clear();

			for (int i = 0; i < storageList.size(); i++) {

				if (storageList.get(i).getTaskDueDate() == null) {
					continue;
				}

				if (storageList.get(i).getTaskDueDate().equals(searchDate)) {
					searchList.add(storageList.get(i));
					count++;
				}
			}

			if (count == 0) {
				ControlDisplay.refreshDisplay(searchList);
				return Constants.FEEDBACK_SHOW_FAILURE;
			}

			else {
				ControlDisplay.refreshDisplay(searchList);
				return showFeedback(searchTask, Constants.COMMAND_SHOW);
			}
		}
	}

	private static String executeSearch(String userInput) {
		DateParser dp = new DateParser();
		String searchKey = LOLParser.getKeywordsForSearchCommand(userInput);
		Task searchTask = new Task(searchKey, null, null);

		if (dp.isValidDateFormat(searchKey)) {
			return executeShow(userInput);
		}

		if (storageList.size() == 0) {
			return (Constants.FEEDBACK_SEARCH_FAILURE + Constants.QUOTE
					+ searchKey + Constants.QUOTE);
		}

		else {
			int count = 0;
			searchList.clear();

			for (int i = 0; i < storageList.size(); i++) {

				if (storageList.get(i).getTaskDescription().contains(searchKey)) {
					searchList.add(storageList.get(i));
					count++;
				}
			}

			if (count == 0) {
				ControlDisplay.refreshDisplay(searchList);
				return (Constants.FEEDBACK_SEARCH_FAILURE + Constants.QUOTE
						+ searchKey + Constants.QUOTE);
			}

			else {
				ControlDisplay.refreshDisplay(searchList);
				return showFeedback(searchTask, Constants.COMMAND_SEARCH);
			}
		}
	}

	private static String executeDone(String userInput) {
		int taskIndex = LOLParser.getTaskIndex(userInput);

		try {
			Task undoneTask = displayList.get(taskIndex - 1);
			int undoneTaskStorageIndex = storageList.indexOf(undoneTask);

			if (undoneTask.getIsDone()) {
				return Constants.FEEDBACK_DONE_FAILURE;
			}

			Task doneTask = new Task(undoneTask.getTaskDescription(),
					undoneTask.getTaskLocation(), undoneTask.getTaskDueDate(),
					undoneTask.getStartTime(), undoneTask.getEndTime());

			doneTask.setIsDone(true);

			if (storageList.set(undoneTaskStorageIndex, doneTask)) {
				History.undoEdit(doneTask, undoneTask);
				ControlDisplay.refreshDisplay(storageList);
				LOLStorage.saveTasks(storageList);
				return showFeedback(doneTask, Constants.COMMAND_DONE);
			}
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Processing Error, task does not exist");
		}
		return executeInvalid(userInput);
	}

	private static String executeNotDone(String userInput) {
		int taskIndex = LOLParser.getTaskIndex(userInput);

		try {
			Task doneTask = displayList.get(taskIndex - 1);
			int doneTaskStorageIndex = storageList.indexOf(doneTask);

			if (!doneTask.getIsDone()) {
				return Constants.FEEDBACK_NOT_DONE_FAILURE;
			}

			Task notDoneTask = new Task(doneTask.getTaskDescription(),
					doneTask.getTaskLocation(), doneTask.getTaskDueDate(),
					doneTask.getStartTime(), doneTask.getEndTime());

			notDoneTask.setIsDone(false);

			if (storageList.set(doneTaskStorageIndex, notDoneTask)) {
				History.undoEdit(notDoneTask, doneTask);
				ControlDisplay.refreshDisplay(storageList);
				LOLStorage.saveTasks(storageList);
				return showFeedback(notDoneTask, Constants.COMMAND_NOT_DONE);
			}
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Processing Error, task does not exist");
		}
		return executeInvalid(userInput);
	}

	private static String executeUndo(String userInput) {
		if (History.isEmptyUndoStack()) {
			return Constants.FEEDBACK_UNDO_FAILURE;
		}

		else {

			CommandLine undoCmd = History.popUndoStack();

			String undoCmdType = undoCmd.getCommandType();
			Task undoCmdTask = undoCmd.getTask();

			if (undoCmdType.equals(Constants.COMMAND_DELETE)) {
				storageList.add(undoCmdTask);
				History.redoAdd(undoCmd);
				ControlDisplay.refreshDisplay(storageList);
				LOLStorage.saveTasks(storageList);
			}

			if (undoCmdType.equals(Constants.COMMAND_ADD)) {
				storageList.delete(undoCmdTask);
				History.redoAdd(undoCmd);
				ControlDisplay.refreshDisplay(storageList);
				LOLStorage.saveTasks(storageList);
			}
			if (undoCmdType.equals(Constants.COMMAND_EDIT)) {

				CommandLine undoCmdNew = History.popUndoStack();
				CommandLine undoCmdOld = History.popUndoStack();
				Task undoCmdTaskNew = undoCmdNew.getTask();
				Task undoCmdTaskOld = undoCmdOld.getTask();

				storageList.delete(undoCmdTaskNew);
				storageList.add(undoCmdTaskOld);
				History.redoAdd(undoCmd);
				History.redoAdd(undoCmdOld);
				History.redoAdd(undoCmdNew);
				ControlDisplay.refreshDisplay(storageList);
				LOLStorage.saveTasks(storageList);

			}

			return showFeedback(null, Constants.COMMAND_UNDO);
		}
	}

	private static String executeRedo(String userInput) {
		if (History.isEmptyRedoQueue()) {
			return Constants.FEEDBACK_REDO_FAILURE;
		}

		else {

			CommandLine undoCmd = History.peekRedoQueue();

			String undoCmdType = undoCmd.getCommandType();
			Task undoCmdTask = undoCmd.getTask();

			if (undoCmdType.equals(Constants.COMMAND_DELETE)) {
				storageList.delete(undoCmdTask);
				History.undoDelete(undoCmdTask);
				ControlDisplay.refreshDisplay(storageList);
				LOLStorage.saveTasks(storageList);
			}

			if (undoCmdType.equals(Constants.COMMAND_ADD)) {
				storageList.add(undoCmdTask);
				History.undoAdd(undoCmdTask);
				ControlDisplay.refreshDisplay(storageList);
				LOLStorage.saveTasks(storageList);
			}
			if (undoCmdType.equals(Constants.COMMAND_EDIT)) {

				CommandLine undoCmdOld = History.peekRedoQueue();
				CommandLine undoCmdNew = History.peekRedoQueue();
				Task undoCmdTaskNew = undoCmdNew.getTask();
				Task undoCmdTaskOld = undoCmdOld.getTask();

				storageList.add(undoCmdTaskNew);
				storageList.delete(undoCmdTaskOld);
				History.undoEdit(undoCmdTaskNew, undoCmdTaskOld);
				ControlDisplay.refreshDisplay(storageList);
				LOLStorage.saveTasks(storageList);
			}

			return showFeedback(null, Constants.COMMAND_REDO);
		}
	}

	private static String executeHomeScreen(String userInput) {
		ControlDisplay.refreshDisplay(storageList);
		return showFeedback(null, Constants.COMMAND_VIEW_HOMESCREEN);
	}

	private static String executeExit(String userInput) {
		System.exit(0);
		return showFeedback(null, Constants.COMMAND_EXIT);
	}

	private static String executeInvalid(String userInput) {
		return showFeedback(null, Constants.COMMAND_INVALID);
	}

	private static String showFeedback(Task task, String commandType) {
		if (commandType.equals(Constants.COMMAND_ADD)) {
			return (Constants.QUOTE + task + Constants.QUOTE + Constants.FEEDBACK_ADD_SUCCESS);
		}
		if (commandType.equals(Constants.COMMAND_DELETE)) {
			return (Constants.QUOTE + task + Constants.QUOTE + Constants.FEEDBACK_DEL_SUCCESS);
		}
		if (commandType.equals(Constants.COMMAND_EDIT)) {
			return (Constants.QUOTE + task + Constants.QUOTE + Constants.FEEDBACK_EDIT_SUCCESS);
		}
		if (commandType.equals(Constants.COMMAND_SHOW)) {
			return (Constants.FEEDBACK_SHOW_SUCCESS + task.getTaskDueDate()
					+ Constants.LINEBREAK + searchList.size() + Constants.FEEDBACK_SHOW_HITS);
		}
		if (commandType.equals(Constants.COMMAND_SEARCH)) {
			return (Constants.FEEDBACK_SEARCH_SUCCESS
					+ task.getTaskDescription() + Constants.LINEBREAK
					+ searchList.size() + Constants.FEEDBACK_SHOW_HITS);
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
		if (commandType.equals(Constants.COMMAND_VIEW_HOMESCREEN)) {
			return (Constants.FEEDBACK_VIEW_HOMESCREEN);
		}
		if (commandType.equals(Constants.COMMAND_EXIT)) {
			return (null);
		} else
			return (Constants.FEEDBACK_INVALID);
	}
}