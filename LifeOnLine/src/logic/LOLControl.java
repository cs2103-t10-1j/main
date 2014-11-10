package logic;

import java.util.logging.*;

import parser.DateParser;
import parser.LOLParser;
import parser.TimeParser;
import lol.CommandLine;
import lol.Constants;
import lol.Date;
import lol.History;
import lol.LOLGui;
import lol.Task;
import lol.TaskList;
import lol.Time;
import io.StorageFacade;

public class LOLControl {
	// @author A0111422A
	public static String userEmail = null;

	private static Logger logger = Logger.getLogger("LOLControl");

	public static int progress = 0;
	public static int progressMaximum = 0;
	public static boolean isAlertMode = false;
	public static boolean isBlockMode = false;
	public static int alertTime = 200;

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

	public static TaskList<Task> loadTaskList() {
		storageList = LOLStorage.loadTasks();
		return storageList;
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

	public static String executeUserInput(String userInput) {
		setAllTaskIsJustAddedAsFalse();

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
		case (Constants.COMMAND_HELP):
			return executeHelp(userInput);
		case (Constants.COMMAND_EXIT):
			return executeExit(userInput);
		default:
			// logger.log(Level.WARNING, "Unsupported CommandType entered");
			return executeAdd(Constants.DICTIONARY_ADD[0] + " " + userInput);
		}
	}

	public static String getCommandType(String userInput) {
		String command = LOLParser.getCommandName(userInput);
		return command;
	}

	private static String executeAdd(String userInput) {

		try {
			LOLParser.getTask(userInput);
		} catch (Exception e) {
			logger.log(Level.WARNING, "No description entered!");
			return executeInvalid(userInput);
		}

		try {
			if (LOLParser.getTask(userInput) == null) {
				logger.log(Level.WARNING, "No description entered!");
				return executeInvalid(userInput);
			} else {
				Task newTask = LOLParser.getTask(userInput);
				newTask.setIsJustAdded(true);

				if (storageList.add(newTask)) {
					History.emptyRedoStack();
					History.undoAdd(newTask);
					ControlDisplay.refreshDisplay(toDoList, storageList);
					LOLStorage.saveTasks(storageList);
					return showFeedback(newTask, Constants.COMMAND_ADD);
				} else
					return executeInvalid(userInput);
			}
		} catch (Exception e) {
			// or do some other thing
			return executeInvalid(userInput);
		}
	}

	private static String executeDel(String userInput) {

		int[] taskIndices = LOLParser.getTaskIndexArray(userInput);
		int numToDel = taskIndices.length;

		for (int i = 0; i < numToDel; i++) {
			if ((taskIndices[i]) > displayList.size()) {
				logger.log(Level.WARNING,
						"Processing Error, deleting invalid index");
				return Constants.FEEDBACK_MASS_DEL_FAILURE;
			}
		}

		for (int i = 0; i < numToDel; i++) {
			Task delTask = displayList.get(taskIndices[i] - 1);

			if (numToDel == 1) {
				if (storageList.delete(delTask)) {
					History.emptyRedoStack();
					History.undoDelete(delTask);
					ControlDisplay.refreshDisplay(toDoList, storageList);
					LOLStorage.saveTasks(storageList);
					return showFeedback(delTask, Constants.COMMAND_DELETE);
				}
				return executeInvalid(userInput);
			}

			if (i != numToDel - 1) {
				if (storageList.delete(delTask)) {
					History.emptyRedoStack();
					History.undoDelete(delTask);
					LOLStorage.saveTasks(storageList);
					continue;
				}
				return executeInvalid(userInput);

			} // need to refresh screen after last index has been deleted
			if (i == numToDel - 1) {
				if (storageList.delete(delTask)) {
					History.emptyRedoStack();
					History.undoDelete(delTask);
					History.peekUndoStack().setPrecedingTasks(numToDel);
					ControlDisplay.refreshDisplay(toDoList, storageList);
					LOLStorage.saveTasks(storageList);
					return Constants.FEEDBACK_MASS_DEL_SUCCESS;
				}
				return executeInvalid(userInput);
			}
		}
		return executeInvalid(userInput);
	}

	private static String executeEdit(String userInput) {
		boolean runOnce = true;
		int taskIndex = LOLParser.getTaskIndex(userInput);
		Task taskAtIndex = displayList.get(taskIndex - 1);
		Task oldTask = new Task(taskAtIndex.getTaskDescription(),
				taskAtIndex.getTaskLocation(), taskAtIndex.getTaskDueDate(),
				taskAtIndex.getEndDate(), taskAtIndex.getStartTime(),
				taskAtIndex.getEndTime());

		try {
			Task editTask = LOLParser.getEditTask(userInput, oldTask);
			editTask.setIsJustAdded(true);

			Task oldTaskDesc = new Task(null, null, null);
			if (runOnce) {
				oldTaskDesc.setDescription(taskAtIndex.getTaskDescription());
				runOnce = false;
			}

			if ((storageList.delete(taskAtIndex))
					&& (storageList.add(editTask))) {
				History.emptyRedoStack();
				History.undoEdit(editTask, taskAtIndex);
				ControlDisplay.refreshDisplay(toDoList, storageList);
				LOLStorage.saveTasks(storageList);
				return showFeedback(oldTaskDesc, Constants.COMMAND_EDIT);
			} else
				return executeInvalid(userInput);
		} catch (Exception e) {
			// do something
			return executeInvalid(userInput);
		}
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
			case (Constants.SHOW_WEEK):
				return showWeek(userInput);
			default:
				if (searchDate == null) {
					if ((LOLParser.hasWordInDictionary(Constants.MONTHS_SHORT,
							searchKey))
							|| (LOLParser.hasWordInDictionary(
									Constants.MONTHS_LONG, searchKey))) {
						return showMonth(searchKey);
					}
					return executeInvalid(userInput);
				}
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

	private static String showWeek(String userInput) {

		searchList.clear();

		DateParser dp = new DateParser();

		Date currentDate = dp.getTodaysDate();
		Date nextWeekDate = dp.addDaysToToday(Constants.ONE_WEEK);

		for (int i = 0; i < storageList.size(); i++) {

			if (storageList.get(i).getTaskDueDate() == null) {
				continue;
			}

			else {
				Date taskDate = storageList.get(i).getTaskDueDate();

				if ((taskDate.equals(currentDate))
						|| taskDate.isAfter(currentDate)
						&& taskDate.isBefore(nextWeekDate)) {
					searchList.add(storageList.get(i));
				}
			}
		}

		ControlDisplay.refreshDisplay(searchList, storageList);

		switch (displayList.size()) {
		case (Constants.EMPTY_LIST):
			return Constants.FEEDBACK_SHOW_WEEK_FAILURE;
		default:
			return Constants.FEEDBACK_SHOW_WEEK_SUCCESS;
		}
	}

	private static String showMonth(String monthName) {

		Date date = new Date();
		int currentYear = date.getYear4Digit();

		DateParser dp = new DateParser();
		int searchMonth = dp.getMonthNum(monthName);

		searchList.clear();

		for (int i = 0; i < storageList.size(); i++) {

			if (storageList.get(i).getTaskDueDate() == null) {
				continue;
			}

			else {
				Task iteratorTask = storageList.get(i);
				int iteratorYear = iteratorTask.getTaskDueDate()
						.getYear4Digit();
				int iteratorMonth = iteratorTask.getTaskDueDate().getMonth();

				if ((iteratorYear >= currentYear)
						&& (searchMonth == iteratorMonth)) {
					searchList.add(storageList.get(i));
				}
			}
		}

		ControlDisplay.refreshDisplay(searchList, storageList);

		switch (displayList.size()) {
		case (Constants.EMPTY_LIST):
			return Constants.FEEDBACK_SHOW_MONTH_FAILURE
					+ date.getMonthNameLong(searchMonth);
		default:
			return Constants.FEEDBACK_SHOW_MONTH_SUCCESS
					+ date.getMonthNameLong(searchMonth);
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

		int[] taskIndices = LOLParser.getTaskIndexArray(userInput);
		int numToDone = taskIndices.length;

		for (int i = 0; i < numToDone; i++) {
			if ((taskIndices[i]) > displayList.size()) {
				return Constants.FEEDBACK_MASS_DONE_FAILURE;
			}
		}

		for (int i = 0; i < numToDone; i++) {

			if (numToDone == 1) {
				Task undoneTask = displayList.get(taskIndices[i] - 1);
				int undoneTaskStorageIndex = storageList.indexOf(undoneTask);

				if (undoneTask.getIsDone()) {
					return Constants.FEEDBACK_DONE_FAILURE;
				}
				Task doneTask = new Task(undoneTask.getTaskDescription(),
						undoneTask.getTaskLocation(),
						undoneTask.getTaskDueDate(), undoneTask.getStartTime(),
						undoneTask.getEndTime());
				doneTask.setIsDone(true);
				doneTask.setIsJustAdded(true);

				if (storageList.set(undoneTaskStorageIndex, doneTask)) {
					History.emptyRedoStack();
					History.undoEdit(doneTask, undoneTask);
					ControlDisplay.refreshDisplay(toDoList, storageList);
					LOLStorage.saveTasks(storageList);
					return showFeedback(doneTask, Constants.COMMAND_DONE);
				}
			}
			if (i != numToDone - 1) {
				Task undoneTask = displayList.get(taskIndices[i] - 1);
				int undoneTaskStorageIndex = storageList.indexOf(undoneTask);

				Task doneTask = new Task(undoneTask.getTaskDescription(),
						undoneTask.getTaskLocation(),
						undoneTask.getTaskDueDate(), undoneTask.getStartTime(),
						undoneTask.getEndTime());

				doneTask.setIsDone(true);

				if (storageList.set(undoneTaskStorageIndex, doneTask)) {
					History.emptyRedoStack();
					History.undoEdit(doneTask, undoneTask);
					LOLStorage.saveTasks(storageList);
				}
			}
			if (i == numToDone - 1) {
				Task undoneTask = displayList.get(taskIndices[i] - 1);
				int undoneTaskStorageIndex = storageList.indexOf(undoneTask);

				Task doneTask = new Task(undoneTask.getTaskDescription(),
						undoneTask.getTaskLocation(),
						undoneTask.getTaskDueDate(), undoneTask.getStartTime(),
						undoneTask.getEndTime());

				doneTask.setIsDone(true);

				if (storageList.set(undoneTaskStorageIndex, doneTask)) {
					History.emptyRedoStack();
					History.undoEdit(doneTask, undoneTask);
					History.peekUndoStack().setPrecedingTasks(numToDone);
					ControlDisplay.refreshDisplay(toDoList, storageList);
					LOLStorage.saveTasks(storageList);
					return Constants.FEEDBACK_MASS_DONE_SUCCESS;
				}
			}
		}
		return executeInvalid(userInput);
	}

	private static String executeNotDone(String userInput) {
		int[] taskIndices = LOLParser.getTaskIndexArray(userInput);
		int numToNotDone = taskIndices.length;

		for (int i = 0; i < numToNotDone; i++) {
			if ((taskIndices[i]) > displayList.size()) {
				return Constants.FEEDBACK_MASS_NOT_DONE_FAILURE;
			}
		}

		for (int i = 0; i < numToNotDone; i++) {

			if (numToNotDone == 1) {
				Task doneTask = displayList.get(taskIndices[i] - 1);
				int doneTaskStorageIndex = storageList.indexOf(doneTask);

				if (!doneTask.getIsDone()) {
					return Constants.FEEDBACK_NOT_DONE_FAILURE;
				}

				Task notDoneTask = new Task(doneTask.getTaskDescription(),
						doneTask.getTaskLocation(), doneTask.getTaskDueDate(),
						doneTask.getStartTime(), doneTask.getEndTime());

				notDoneTask.setIsDone(false);
				notDoneTask.setIsJustAdded(true);

				if (storageList.set(doneTaskStorageIndex, notDoneTask)) {
					History.emptyRedoStack();
					History.undoEdit(notDoneTask, doneTask);
					ControlDisplay.refreshDisplay(toDoList, storageList);
					LOLStorage.saveTasks(storageList);
					return showFeedback(notDoneTask, Constants.COMMAND_NOT_DONE);
				}
			}
			if (i != numToNotDone - 1) {
				Task doneTask = displayList.get(taskIndices[i] - 1);
				int doneTaskStorageIndex = storageList.indexOf(doneTask);

				Task notDoneTask = new Task(doneTask.getTaskDescription(),
						doneTask.getTaskLocation(), doneTask.getTaskDueDate(),
						doneTask.getStartTime(), doneTask.getEndTime());

				notDoneTask.setIsDone(false);

				if (storageList.set(doneTaskStorageIndex, notDoneTask)) {
					History.emptyRedoStack();
					History.undoEdit(notDoneTask, doneTask);
					LOLStorage.saveTasks(storageList);
				}
			}
			if (i == numToNotDone - 1) {
				Task doneTask = displayList.get(taskIndices[i] - 1);
				int doneTaskStorageIndex = storageList.indexOf(doneTask);

				Task notDoneTask = new Task(doneTask.getTaskDescription(),
						doneTask.getTaskLocation(), doneTask.getTaskDueDate(),
						doneTask.getStartTime(), doneTask.getEndTime());

				notDoneTask.setIsDone(false);

				if (storageList.set(doneTaskStorageIndex, notDoneTask)) {
					History.emptyRedoStack();
					History.undoEdit(notDoneTask, doneTask);
					History.peekUndoStack().setPrecedingTasks(numToNotDone);
					ControlDisplay.refreshDisplay(toDoList, storageList);
					LOLStorage.saveTasks(storageList);
					return Constants.FEEDBACK_MASS_NOT_DONE_SUCCESS;
				}
			}
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
			int undoCmdNum = undoCmd.getPrecedingTasks();

			switch (undoCmdType) {
			case (Constants.COMMAND_DELETE):

				if (undoCmdNum == 0) {
					storageList.add(undoCmdTask);
					History.redoAdd(undoCmd);
				} else if (undoCmdNum > 0) {
					storageList.add(undoCmdTask);
					History.redoAdd(undoCmd);
					for (int i = 1; i < undoCmdNum; i++) {
						CommandLine iterateCmd = History.popUndoStack();
						Task iterateTask = iterateCmd.getTask();
						storageList.add(iterateTask);
						History.redoAdd(iterateCmd);
					}
					History.peekRedoStack().setPrecedingTasks(undoCmdNum);
				}
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

				if (undoCmdNum == 0) {
					storageList.delete(undoCmdTaskNew);
					storageList.add(undoCmdTaskOld);
					History.redoAdd(undoCmdOld);
					History.redoAdd(undoCmdNew);
					History.redoAdd(undoCmd);
				}

				else if (undoCmdNum > 0) {
					storageList.delete(undoCmdTaskNew);
					storageList.add(undoCmdTaskOld);
					History.redoAdd(undoCmdOld);
					History.redoAdd(undoCmdNew);
					History.redoAdd(undoCmd);
					for (int i = 1; i < undoCmdNum; i++) {
						History.popUndoStack();
						CommandLine iterateCmdNew = History.popUndoStack();
						CommandLine iterateCmdOld = History.popUndoStack();
						Task iterateCmdTaskNew = iterateCmdNew.getTask();
						Task iterateCmdTaskOld = iterateCmdOld.getTask();

						storageList.delete(iterateCmdTaskNew);
						storageList.add(iterateCmdTaskOld);
						History.redoAdd(iterateCmdOld);
						History.redoAdd(iterateCmdNew);
						History.redoAdd(undoCmd);
					}
					History.peekRedoStack().setPrecedingTasks(undoCmdNum);
				}
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
			int undoCmdNum = undoCmd.getPrecedingTasks();

			switch (undoCmdType) {
			case (Constants.COMMAND_DELETE):
				if (undoCmdNum == 0) {
					storageList.delete(undoCmdTask);
					History.undoDelete(undoCmdTask);
				} else if (undoCmdNum > 0) {
					storageList.delete(undoCmdTask);
					History.undoDelete(undoCmdTask);
					for (int i = 1; i < undoCmdNum; i++) {
						CommandLine iterateCmd = History.popRedoStack();
						Task iterateTask = iterateCmd.getTask();
						storageList.delete(iterateTask);
						History.undoDelete(iterateTask);
					}
					History.peekUndoStack().setPrecedingTasks(undoCmdNum);
				}
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

				if (undoCmdNum == 0) {
					storageList.add(undoCmdTaskNew);
					storageList.delete(undoCmdTaskOld);
					History.undoEdit(undoCmdTaskNew, undoCmdTaskOld);
				}

				else if (undoCmdNum > 0) {
					storageList.add(undoCmdTaskNew);
					storageList.delete(undoCmdTaskOld);
					History.undoEdit(undoCmdTaskNew, undoCmdTaskOld);
					for (int i = 1; i < undoCmdNum; i++) {
						History.popRedoStack();
						CommandLine iterateCmdNew = History.popRedoStack();
						CommandLine iterateCmdOld = History.popRedoStack();
						Task iterateCmdTaskNew = iterateCmdNew.getTask();
						Task iterateCmdTaskOld = iterateCmdOld.getTask();

						storageList.add(iterateCmdTaskNew);
						storageList.delete(iterateCmdTaskOld);
						History.undoEdit(iterateCmdTaskNew, iterateCmdTaskOld);
					}
					History.peekUndoStack().setPrecedingTasks(undoCmdNum);
				}
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

	private static String executeHelp(String userInput) {
		LOLGui.showHelpWindow();
		return null;
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

	private static void setAllTaskIsJustAddedAsFalse() {
		for (int i = 0; i < storageList.size(); i++) {
			storageList.get(i).setIsJustAdded(false);
		}
	}

	// @author A0118903H
	public static void refreshProgress() {

		progress = 0;
		progressMaximum = 0;
		DateParser dp = new DateParser();
		Date currentDate = dp.getTodaysDate();

		for (int i = 0; i < storageList.size(); i++) {
			if (storageList.size() == 0)
				break;
			if (storageList.get(i).getTaskDueDate() != null) {
				if (storageList.get(i).getTaskDueDate().equals(currentDate)) {
					progressMaximum++;
				}
				if (storageList.get(i).getTaskDueDate().equals(currentDate)
						&& storageList.get(i).getIsDone()) {
					progress++;
				}
				if (storageList.get(i).getTaskDueDate().isAfter(currentDate))
					break;
			}

		}

	}

	/*
	 * returns a task if it is upcoming within specified alert time, if no task
	 * then null
	 */
	public static Task refreshAlert() {

		DateParser dp = new DateParser();
		Date currentDate = dp.getTodaysDate();
		Date tomorrowDate = dp.addDaysToToday(1);

		TimeParser tp = new TimeParser();
		Time currentTime = tp.getCurrentTime();

		for (int i = 0; i < storageList.size(); i++) {
			Task temp = storageList.get(i);
			if (storageList.size() == 0)
				break;
			if (temp.getIsDone())
				continue;
			if (temp.getTaskDueDate() != null) {
				if (temp.getTaskDueDate().isAfter(tomorrowDate))
					break;
				if (!temp.getIsOverdue()) {
					if (temp.getTaskDueDate().equals(currentDate)
							&& temp.getStartTime() != null) {
						if (!temp.getAlerted()
								&& isAlertRangeToday(temp, currentTime)) {
							temp.setAlerted(true);
							return temp;
						}else{
							if(!temp.getAlerted()
									&& isAlertRangeTomorrow(temp, currentTime)){
								
							}
						}
					}

				} else {
					continue;
				}

			} else {
				break;
			}
		}
		return null;

	}

	private static boolean isAlertRangeTomorrow(Task temp, Time currentTime) {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean isAlertRangeToday(Task temp, Time currentTime) {

		int tempTime = Integer.parseInt(temp.getStartTime().getFormat24hr());
		int crrntTime = Integer.parseInt(currentTime.getFormat24hr());

		if ((tempTime - crrntTime) <= alertTime)
			return true;

		return false;
	}
}