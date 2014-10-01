package lol;

public class LOLControl {

	public static final String QUOTE = "\"";
	public static final String COMMAND_ADD = "add";
	public static final String COMMAND_DEL = "delete";
	public static final String COMMAND_INVALID = "invalid command";
	public static final String FEEDBACK_ADD_SUCCESS = " added successfully";
	public static final String FEEDBACK_DEL_SUCCESS = " deleted successfully";
	public static final String FEEDBACK_INVALID = "That is an invalid action";

	/********** Load Storage ***********/

	public static TaskList list = LOLStorage.load();

	/********** Controller methods ***********/

	public static String executeUserInput(String userInput) {
		if (getCommandType(userInput) == "COMMAND_ADD") {
			return executeAdd(userInput);
		}

		if (getCommandType(userInput) == "COMMAND_DEL") {
			return executeDel(userInput);
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
		list.add(newTask);
		LOLStorage.save();
		return showFeedback(newTask, COMMAND_ADD);
	}

	public static String executeDel(String userInput) {
		Task delTask = LOLParser.getTask(userInput);
		list.delete(delTask);
		LOLStorage.save();
		return showFeedback(delTask, COMMAND_DEL);
	}

	public static String executeInvalid(String userInput) {
		Task invalidTask = LOLParser.getTask(userInput);
		return showFeedback(invalidTask, COMMAND_INVALID);
	}

	public static String showFeedback(Task task, String commandType) {
		if (commandType == COMMAND_ADD) {
			return (QUOTE + task + QUOTE + FEEDBACK_ADD_SUCCESS);
		}
		if (commandType == COMMAND_DEL) {
			return (QUOTE + task + QUOTE + FEEDBACK_DEL_SUCCESS);
		}
		else
			return (FEEDBACK_INVALID);
	}
}
