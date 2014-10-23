/********** TODO ***********/

package logic;

import static org.junit.Assert.*;
import lol.Constants;
import lol.Date;
import lol.Task;
//import lol.TaskList;
import lol.Time;

import org.junit.Before;
import org.junit.Test;

public class LOLControlTest {

	// private static TaskList<Task> storageList;

	Task testTask = new Task("this is a test task", "home", new Date(13, 7,
			new Time(10, "am")));

	@Before
	public void initialize() {
		LOLControl.loadTaskList();
	}

	@Test
	public void testGetCommandType() {
		assertEquals(Constants.COMMAND_ADD,
				LOLControl.getCommandType("add do dishes"));
		assertEquals(Constants.COMMAND_DELETE,
				LOLControl.getCommandType("delete do dishes"));
		assertEquals(Constants.COMMAND_INVALID,
				LOLControl.getCommandType("asfd do dishes"));
	}

	@Test
	public void testExecuteUserInput() throws Exception {

		// ADD Task with ALL PARAMETERS
		assertEquals(
				Constants.QUOTE + "this is a test task" + Constants.QUOTE
						+ " added successfully!",
				LOLControl
						.executeUserInput("add this is a test task at home by 13/7 at 10am"));
		LOLControl.executeUndo("undo");
		// ADD Task with NO LOCATION
		assertEquals(
				Constants.QUOTE + "this is a test task" + Constants.QUOTE
						+ " added successfully!",
				LOLControl
						.executeUserInput("add this is a test task by 13/7 at 10am"));
		LOLControl.executeUndo("undo");
		// ADD Task with NO DATE
		assertEquals(
				Constants.QUOTE + "this is a test task" + Constants.QUOTE
						+ " added successfully!",
				LOLControl
						.executeUserInput("add this is a test task at home by 10am"));
		LOLControl.executeUndo("undo");
		// ADD Task with NO TIME
		assertEquals(
				Constants.QUOTE + "this is a test task" + Constants.QUOTE
						+ " added successfully!",
				LOLControl
						.executeUserInput("add this is a test task by 13/7 at home by 13/7"));
		LOLControl.executeUndo("undo");
		// ADD Task with NO PARAMETERS
		assertEquals("That is an invalid action!",
				LOLControl.executeUserInput("add"));
		assertEquals("That is an invalid action!",
				LOLControl.executeUserInput("add "));
		assertEquals("That is an invalid action!",
				LOLControl.executeUserInput("add     "));
		assertEquals("That is an invalid action!",
				LOLControl.executeUserInput(" add"));
		assertEquals("That is an invalid action!",
				LOLControl.executeUserInput("     add"));
		assertEquals("That is an invalid action!",
				LOLControl.executeUserInput("  add   "));

	}
}