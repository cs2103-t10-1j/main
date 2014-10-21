package lol;

import java.util.logging.*;

import io.StorageFacade;

public class LOLControl {

	private static Logger logger = Logger.getLogger("LOLControl");

	/********** Load Storage ***********/
	private static StorageFacade LOLStorage = StorageFacade
			.getInstance(Constants.FILE_NAME);

	private static TaskList<Task> storageList;

	/********** Initialize Temporary Storage ***********/

	private static TaskList<Task> displayList = new TaskList<Task>();
	private static TaskList<Task> searchList = new TaskList<Task>();
	private static TaskList<Task> toDoList = new TaskList<Task>();
	private static TaskList<Task> archiveList = new TaskList<Task>();

	/********** Controller methods ***********/
	public static int getTaskListSize() {
		return storageList.size();
	}

	public static void loadTaskList() {
		storageList = LOLStorage.loadTasks();
	}

	public static TaskList<Task> getTaskList() {
		return displayList;
	}

	public static TaskList<Task> getToDoList() {
		return toDoList;
	}

	public static TaskList<Task> getArchiveList() {
		return archiveList;
	}

	public static String executeUserInput(String userInput) throws Exception {

		String commandType = getCommandType(userInput);

		switch (commandType) {
		case (Constants.COMMAND_ADD):
			return executeAdd(userInput);
		case (Constants.COMMAND_DELETE):
			return executeDel(userInput);
		case (Constants.COMMAND_EDIT):
			return executeEdit(userInput);
		case (Constants.COMMAND_SHOW):
			return executeShow(userInput);
		case (Constants.COMMAND_SEARCH):
			return executeSearch(userInput);
		case (Constants.COMMAND_DONE):
			return executeDone(userInput);
		case (Constants.COMMAND_NOT_DONE):
			return executeNotDone(userInput);
		case (Constants.COMMAND_UNDO):
			return executeUndo(userInput);
		case (Constants.COMMAND_REDO):
			return executeRedo(userInput);
		case (Constants.COMMAND_VIEW_HOMESCREEN):
			return executeHomeScreen(userInput);
		case (Constants.COMMAND_EXIT):
			return executeExit(userInput);
		default:
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
				History.emptyRedoStack();
				History.undoAdd(newTask);
				ControlDisplay.refreshDisplay(toDoList, storageList);
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
			History.emptyRedoStack();
			History.undoDelete(delTask);
			ControlDisplay.refreshDisplay(toDoList, storageList);
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
			History.emptyRedoStack();
			History.undoEdit(editTask, taskAtIndex);
			ControlDisplay.refreshDisplay(toDoList, storageList);
			LOLStorage.saveTasks(storageList);
			return showFeedback(oldTask, Constants.COMMAND_EDIT);
		} else
			return executeInvalid(userInput);
	}

	private static String executeShow(String userInput) {
		DateParser dp = new DateParser();
		Date searchDate = LOLParser.getDateForShowCommand(userInput);
		String searchKey = LOLParser.getKeywordsForSearchCommand(userInput);

		if (!dp.isValidDate(userInput)) {
			switch (searchKey.toLowerCase()) {
			case (Constants.SHOW_OVERDUE):
				return showOverdue(userInput);
			case (Constants.SHOW_ARCHIVE):
				return showArchive(userInput);
			case (Constants.SHOW_ALL):
				return showAll(userInput);
			default:
				if (searchDate == null)
					return executeInvalid(userInput);
				break;
			}
		}

		Task searchTask = new Task(null, null, searchDate);

		searchList.clear();

		for (int i = 0; i < storageList.size(); i++) {
			if (storageList.get(i).getTaskDueDate() == null) {
				continue;
			}
			if (storageList.get(i).getTaskDueDate().equals(searchDate)) {
				searchList.add(storageList.get(i));
			}
		}

		ControlDisplay.refreshDisplay(searchList, storageList);

		switch (displayList.size()) {
		case (Constants.EMPTY_LIST):
			return Constants.FEEDBACK_SHOW_FAILURE;
		default:
			return showFeedback(searchTask, Constants.COMMAND_SHOW);
		}
	}

	private static String showOverdue(String userInput) {
		searchList.clear();

		// Before current date & time + Not marked as done
		for (int i = 0; i < storageList.size(); i++) {
			if ((storageList.get(i).getIsOverdue())
					&& (!storageList.get(i).getIsDone())) {
				searchList.add(storageList.get(i));
			}
		}

		ControlDisplay.refreshDisplay(searchList, storageList);

		switch (displayList.size()) {
		case (Constants.EMPTY_LIST):
			return Constants.FEEDBACK_SHOW_OVERDUE_FAILURE;
		case (Constants.LIST_SIZE_ONE):
			return (Constants.FEEDBACK_SHOW_OVERDUE_SUCCESS
					+ Constants.LINEBREAK + displayList.size() + Constants.FEEDBACK_SHOW_HITS_SINGLE);
		default:
			return (Constants.FEEDBACK_SHOW_OVERDUE_SUCCESS
					+ Constants.LINEBREAK + displayList.size() + Constants.FEEDBACK_SHOW_HITS_MULTI);
		}
	}

	private static String showArchive(String userInput) {

		ControlDisplay.refreshDisplay(archiveList, storageList);

		switch (displayList.size()) {
		case (Constants.EMPTY_LIST):
			return Constants.FEEDBACK_SHOW_ARCHIVE_FAILURE;
		default:
			return Constants.FEEDBACK_SHOW_ARCHIVE_SUCCESS;
		}
	}

	private static String showAll(String userInput) {

		ControlDisplay.refreshDisplay(storageList, storageList);

		switch (displayList.size()) {
		case (Constants.EMPTY_LIST):
			return Constants.FEEDBACK_SHOW_ALL_FAILURE;
		default:
			return Constants.FEEDBACK_SHOW_ALL_SUCCESS;
		}
	}

	private static String executeSearch(String userInput) {
		DateParser dp = new DateParser();
		String searchKey = LOLParser.getKeywordsForSearchCommand(userInput);
		Task searchTask = new Task(searchKey, null, null);

		if (dp.isValidDateFormat(searchKey)) {
			return executeShow(userInput);
		}

		else {
			searchList.clear();

			// searches both Description & Location
			for (int i = 0; i < storageList.size(); i++) {

				if ((storageList.get(i).getTaskDescription().toLowerCase()
						.contains(searchKey.toLowerCase()))
						&& storageList.get(i).getTaskLocation() == null) {
					searchList.add(storageList.get(i));
				} else if ((storageList.get(i).getTaskDescription()
						.toLowerCase().contains(searchKey.toLowerCase()))
						&& storageList.get(i).getTaskLocation() != null) {
					if (storageList.get(i).getTaskLocation().toLowerCase()
							.contains(searchKey.toLowerCase())) {
						searchList.add(storageList.get(i));
					} else {
						searchList.add(storageList.get(i));
					}
				} else if (!(storageList.get(i).getTaskDescription()
						.toLowerCase().contains(searchKey.toLowerCase()))
						&& storageList.get(i).getTaskLocation() == null) {
					continue;
				} else if (!(storageList.get(i).getTaskDescription()
						.toLowerCase().contains(searchKey.toLowerCase()))
						&& storageList.get(i).getTaskLocation() != null) {
					if (storageList.get(i).getTaskLocation().toLowerCase()
							.contains(searchKey.toLowerCase())) {
						searchList.add(storageList.get(i));
					}
				}
			}

			ControlDisplay.refreshDisplay(searchList, storageList);

			switch (displayList.size()) {
			case (Constants.EMPTY_LIST):
				return showFeedback(searchTask,
						Constants.FEEDBACK_SEARCH_FAILURE);
			default:
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
				History.emptyRedoStack();
				History.undoEdit(doneTask, undoneTask);
				ControlDisplay.refreshDisplay(toDoList, storageList);
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
				History.emptyRedoStack();
				History.undoEdit(notDoneTask, doneTask);
				ControlDisplay.refreshDisplay(toDoList, storageList);
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

			switch (undoCmdType) {
			case (Constants.COMMAND_DELETE):
				storageList.add(undoCmdTask);
				History.redoAdd(undoCmd);
				ControlDisplay.refreshDisplay(toDoList, storageList);
				LOLStorage.saveTasks(storageList);
				break;
			case (Constants.COMMAND_ADD):
				storageList.delete(undoCmdTask);
				History.redoAdd(undoCmd);
				ControlDisplay.refreshDisplay(toDoList, storageList);
				LOLStorage.saveTasks(storageList);
				break;
			case (Constants.COMMAND_EDIT):
				CommandLine undoCmdNew = History.popUndoStack();
				CommandLine undoCmdOld = History.popUndoStack();
				Task undoCmdTaskNew = undoCmdNew.getTask();
				Task undoCmdTaskOld = undoCmdOld.getTask();

				storageList.delete(undoCmdTaskNew);
				storageList.add(undoCmdTaskOld);
				History.redoAdd(undoCmdOld);
				History.redoAdd(undoCmdNew);
				History.redoAdd(undoCmd);
				ControlDisplay.refreshDisplay(toDoList, storageList);
				LOLStorage.saveTasks(storageList);
				break;
			}
			return showFeedback(null, Constants.COMMAND_UNDO);
		}
	}

	private static String executeRedo(String userInput) {
		if (History.isEmptyRedoStack()) {
			return Constants.FEEDBACK_REDO_FAILURE;
		}

		else {

			CommandLine undoCmd = History.popRedoStack();

			String undoCmdType = undoCmd.getCommandType();
			Task undoCmdTask = undoCmd.getTask();

			switch (undoCmdType) {
			case (Constants.COMMAND_DELETE):
				storageList.delete(undoCmdTask);
				History.undoDelete(undoCmdTask);
				ControlDisplay.refreshDisplay(toDoList, storageList);
				LOLStorage.saveTasks(storageList);
				break;
			case (Constants.COMMAND_ADD):
				storageList.add(undoCmdTask);
				History.undoAdd(undoCmdTask);
				ControlDisplay.refreshDisplay(toDoList, storageList);
				LOLStorage.saveTasks(storageList);
				break;
			case (Constants.COMMAND_EDIT):
				CommandLine undoCmdNew = History.popRedoStack();
				CommandLine undoCmdOld = History.popRedoStack();
				Task undoCmdTaskNew = undoCmdNew.getTask();
				Task undoCmdTaskOld = undoCmdOld.getTask();

				storageList.add(undoCmdTaskNew);
				storageList.delete(undoCmdTaskOld);
				History.undoEdit(undoCmdTaskNew, undoCmdTaskOld);
				ControlDisplay.refreshDisplay(toDoList, storageList);
				LOLStorage.saveTasks(storageList);
				break;
			}
			return showFeedback(null, Constants.COMMAND_REDO);
		}
	}

	private static String executeHomeScreen(String userInput) {
		ControlDisplay.refreshDisplay(toDoList, storageList);
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

		System.out.println("List size= " + getTaskListSize());

		switch (commandType) {
		case (Constants.COMMAND_ADD):
			return (Constants.QUOTE + task + Constants.QUOTE + Constants.FEEDBACK_ADD_SUCCESS);
		case (Constants.COMMAND_DELETE):
			return (Constants.QUOTE + task + Constants.QUOTE + Constants.FEEDBACK_DEL_SUCCESS);
		case (Constants.COMMAND_EDIT):
			return (Constants.QUOTE + task + Constants.QUOTE + Constants.FEEDBACK_EDIT_SUCCESS);
		case (Constants.COMMAND_SHOW):
			switch (displayList.size()) {
			case (Constants.LIST_SIZE_ONE): // 1 search result
				return (Constants.FEEDBACK_SHOW_SUCCESS + task.getTaskDueDate()
						+ Constants.LINEBREAK + searchList.size() + Constants.FEEDBACK_SHOW_HITS_SINGLE);
			default: // more than 1 search result
				return (Constants.FEEDBACK_SHOW_SUCCESS + task.getTaskDueDate()
						+ Constants.LINEBREAK + searchList.size() + Constants.FEEDBACK_SHOW_HITS_MULTI);
			}
		case (Constants.COMMAND_SEARCH):
			int numKeys = LOLParser.countWords(task.getTaskDescription());
			switch (displayList.size()) {
			case (Constants.LIST_SIZE_ONE): // 1 search result
				switch (numKeys) {
				case (Constants.WORD_COUNT_ONE): // 1 keyword
					return (Constants.FEEDBACK_SEARCH_SUCCESS_SINGLE
							+ Constants.QUOTE + task.getTaskDescription()
							+ Constants.QUOTE + Constants.LINEBREAK
							+ searchList.size() + Constants.FEEDBACK_SHOW_HITS_SINGLE);
				default: // more than 1 keyword
					return (Constants.FEEDBACK_SEARCH_SUCCESS_MULTI
							+ Constants.QUOTE + task.getTaskDescription()
							+ Constants.QUOTE + Constants.LINEBREAK
							+ searchList.size() + Constants.FEEDBACK_SHOW_HITS_SINGLE);
				}
			default: // more than 1 search result
				switch (numKeys) {
				case (Constants.WORD_COUNT_ONE): // 1 keyword
					return (Constants.FEEDBACK_SEARCH_SUCCESS_SINGLE
							+ Constants.QUOTE + task.getTaskDescription()
							+ Constants.QUOTE + Constants.LINEBREAK
							+ searchList.size() + Constants.FEEDBACK_SHOW_HITS_MULTI);
				default: // more than 1 keyword
					return (Constants.FEEDBACK_SEARCH_SUCCESS_MULTI
							+ Constants.QUOTE + task.getTaskDescription()
							+ Constants.QUOTE + Constants.LINEBREAK
							+ searchList.size() + Constants.FEEDBACK_SHOW_HITS_MULTI);
				}
			}
		case (Constants.FEEDBACK_SEARCH_FAILURE):
			int numFailedKeys = LOLParser.countWords(task.getTaskDescription());
			switch (numFailedKeys) {
			case (Constants.WORD_COUNT_ONE): // 1 keyword
				return (Constants.FEEDBACK_SEARCH_FAILURE_SINGLE
						+ Constants.QUOTE + task.getTaskDescription() + Constants.QUOTE);
			default:
				return (Constants.FEEDBACK_SEARCH_FAILURE_MULTI // keywords < 1
						+ Constants.QUOTE + task.getTaskDescription() + Constants.QUOTE);
			}
		case (Constants.COMMAND_DONE):
			return (Constants.QUOTE + task + Constants.QUOTE + Constants.FEEDBACK_DONE_SUCCESS);
		case (Constants.COMMAND_NOT_DONE):
			return (Constants.QUOTE + task + Constants.QUOTE + Constants.FEEDBACK_NOT_DONE_SUCCESS);
		case (Constants.COMMAND_UNDO):
			return (Constants.FEEDBACK_UNDO_SUCCESS);
		case (Constants.COMMAND_REDO):
			return (Constants.FEEDBACK_REDO_SUCCESS);
		case (Constants.COMMAND_VIEW_HOMESCREEN):
			return (Constants.FEEDBACK_VIEW_HOMESCREEN);
		default:
			return (Constants.FEEDBACK_INVALID);
		}
	}
}