package lol;

public class LOLControl {

	public static final String QUOTE = "\"";
	public static final String FEEDBACK_ADD_SUCCESS = " added successfully";
	public static final String FEEDBACK_DEL_SUCCESS = " deleted successfully";
	public static final String FEEDBACK_INVALID = "That is an invalid action!";

	/********** Load Storage ***********/

	public static TaskList list = LOLStorage.load();

	/********** Controller methods ***********/

	public static String executeUserInput(String userInput) {
		if (getCommandType(userInput).equals(Constants.COMMAND_ADD)) {
			return executeAdd(userInput);
		}

		if (getCommandType(userInput).equals(Constants.COMMAND_DELETE)) {
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
		
		if (list.add(newTask)) {
		LOLStorage.save();
		return showFeedback(newTask, Constants.COMMAND_ADD);
		}
		else
			return executeInvalid(userInput);
	}

	public static String executeDel(String userInput) {
		Task delTask = LOLParser.getTask(userInput);
		
		if (list.delete(delTask)) {
		LOLStorage.save();
		return showFeedback(delTask, Constants.COMMAND_DELETE);
	}
		else 
			return executeInvalid(userInput);
	}

	public static String executeInvalid(String userInput) {
		Task invalidTask = LOLParser.getTask(userInput);
		return showFeedback(invalidTask, Constants.COMMAND_INVALID);
	}

	public static String showFeedback(Task task, String commandType) {
		if (commandType.equals(Constants.COMMAND_ADD)) {
			return (QUOTE + task + QUOTE + FEEDBACK_ADD_SUCCESS);
		}
		if (commandType.equals(Constants.COMMAND_DELETE)) {
			return (QUOTE + task + QUOTE + FEEDBACK_DEL_SUCCESS);
		}
		else
			return (FEEDBACK_INVALID);
	}
}
