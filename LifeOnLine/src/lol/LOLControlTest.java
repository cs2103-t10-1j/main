/********** TODO ***********/

package lol;

import static org.junit.Assert.*;

import org.junit.Test;

public class LOLControlTest {

	Task testTask = new Task("this is a test task", "home", new Date(13, 7,
			new Time(10, "am")));
	String testAddInput = "add this is a test task\\home\\13 July\\10am";
	String testDelInput = "delete 1";

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
		
		//ADD
		assertEquals(Constants.QUOTE + testTask + Constants.QUOTE
				+ Constants.FEEDBACK_ADD_SUCCESS,
				LOLControl.executeUserInput(testAddInput));
		
		//DELETE
		assertEquals(Constants.QUOTE + testTask + Constants.QUOTE + Constants.FEEDBACK_DEL_SUCCESS,
				LOLControl.executeUserInput(testDelInput));
		
		//EDIT
	
	}

}
