package lol;

public class LOLControl {

	/********** Load Storage ***********/

	public static TaskList<Task> list = LOLStorage.load();

	/********** Controller methods ***********/

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

		else {
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
			LOLStorage.save();
			return showFeedback(delTask, Constants.COMMAND_DELETE);
		} else
			return executeInvalid(userInput);
	}

	public static String executeEdit(String userInput) {
		int taskIndex = LOLParser.getTaskIndex(userInput);
		Task editTask = LOLParser.getEditTask(userInput);
		Task oldTask = list.get(taskIndex - 1);

		if ((list.deleteByIndex(taskIndex - 1)) && (list.add(editTask))) {
			list.sortList();
			LOLStorage.save();
			return showFeedback(oldTask, Constants.COMMAND_EDIT);
		} else
			return executeInvalid(userInput);
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
		} else
			return (Constants.FEEDBACK_INVALID);
	}
}
