/********** TODO ***********/

package lol;

import static org.junit.Assert.*;

import org.junit.Test;

public class LOLControlTest {
	
	public static final String COMMAND_ADD = "add";
	public static final String COMMAND_DEL = "delete";
	public static final String COMMAND_INVALID = "invalid command";
	
	@Test
	public void testGetCommandType() {
		assertEquals(COMMAND_ADD, LOLControl.getCommandType("add do dishes"));
		assertEquals(COMMAND_DEL, LOLControl.getCommandType("delete do dishes"));
		assertEquals(COMMAND_INVALID, LOLControl.getCommandType("asfd do dishes"));
}

}
