package lol;

import java.LinkedList;

public class LOLControl {

  public static final String COMMAND_ADD = "add";
  public static final String COMMAND_DELETE = "delete";
  public static final String COMMAND_INVALID = "invalid command";
  public static final String FEEDBACK_ADD_SUCCESS = " added successfully";
  public static final String FEEDBACK_DEL_SUCCESS = " deleted successfully";
  public static final String FEEDBACK_INVALID = " is an invalid action.";

  /********** Load Storage ***********/

  LinkedList<Task> list = storage.load();


  /********** Controller methods ***********/

  public static void executeUserInput(String userInput) {
    if (getCommandType(userInput) == "COMMAND_ADD") {
      executeAdd(userInput);
    }

    if (getCommandType(userInput) == "COMMAND_DEL") {
      executeDel(userInput);

      if (getCommandType(userInput) == "COMMAND_INVALID")
      executeInvalid(userInput);
    }
  }

  private static String getCommandType(String userInput) {
    String command = LOLParser.getCommandName(userInput);
    return command;
  }

  private static void executeAdd(String userInput) {
    Task newTask = LOLParser.getTask(userInput);
    list.add(newTask);
    storage.save();
    showFeedback(newTask, COMMAND_ADD);
  }

  private static void executeDel(String userInput) {
    Task delTask = LOLParser.getTask(userInput);
    list.delete(delTask);
    storage.save();
    showFeedback(delTask, COMMAND_DEL);
  }

  private static void executeInvalid(String userInput) {
    Task invalidTask = LOL.Parser.getTask(userInput);
    showFeedback(invalidTask, COMMAND_INVALID);
  }

  private static void showFeedback(Task task, String commandType) {
    if (commandType == COMMAND_ADD) {
      System.out.println(+ Task + FEEDBACK_ADD_SUCCESS);
    }
    if (commandType == COMMAND_DEL) {
      System.out.println(+ Task + FEEDBACK_DEL_SUCCESS);
    }
    if (commandType == COMMAND_INVALID) {
      System.out.println(+ Task + FEEDBACK_INVALID);
    }
  }


















}
