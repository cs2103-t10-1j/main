/********** TODO ***********/

package lol;

import static org.junit.Assert.*;

import org.junit.Test;

public class LOLControlTest {
	
	@Test
	public void testGetCommandType() {
		assertEquals(Constants.COMMAND_ADD, LOLControl.getCommandType("add do dishes"));
		assertEquals(Constants.COMMAND_DELETE, LOLControl.getCommandType("delete do dishes"));
		assertEquals(Constants.COMMAND_INVALID, LOLControl.getCommandType("asfd do dishes"));
}

}
