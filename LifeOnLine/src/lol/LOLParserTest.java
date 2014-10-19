package lol;

import static org.junit.Assert.*;

import org.junit.Test;

public class LOLParserTest {

	@Test
	public void testGetCommandName() {
		assertEquals("add", LOLParser.getCommandName("add buy milk"));
		assertEquals("delete", LOLParser.getCommandName("delete 2"));
		assertEquals("edit", LOLParser.getCommandName("edit 5"));
		assertEquals("show", LOLParser.getCommandName("show"));
		assertEquals("undo", LOLParser.getCommandName("undo "));
	}

	@Test
	public void testGetTask() {
		assertEquals(new Task("buy milk", null, new Date(14, 9)),
				LOLParser.getTask("add buy milk 14 sep"));
		assertEquals(new Task("buy milk", null, null),
				LOLParser.getTask("add buy milk"));
		assertEquals(new Task("sleep", null, new Date(20, 10), new Time("0020"), null), LOLParser.getTask("add sleep at 12.20am"));
	/*	assertEquals(new Task("buy bread", null, new Date(8, 10), new Time(11, 30, "pm"), null),
				LOLParser.getTask("add buy bread\\11.30pm"));*/
	}

	@Test
	public void testGetTaskIndex() {
		assertEquals(2, LOLParser.getTaskIndex("delete 2"));
		assertEquals(6, LOLParser.getTaskIndex("edit 6 send letter sat"));
		assertEquals(-1, LOLParser.getTaskIndex("delete tt"));
	}
	
	@Test
	public void testGetEditTask() {
		assertEquals(new Task("buy milk", "supermarket", null), LOLParser.getEditTask("edit 6 at supermarket", new Task("buy milk", null, null)));
		assertEquals(new Task("buy milk", "supermarket", null), LOLParser.getEditTask("edit 6 buy milk", new Task("buy juice", "supermarket", null)));
		assertEquals(new Task("buy milk", "supermarket", new Date(13,11)), LOLParser.getEditTask("edit 6 13 nov", new Task("buy milk", "supermarket", null)));
		assertEquals(new Task("eat food", null, new Date(19,10), new Time("1800"), null), LOLParser.getEditTask("edit 2 6pm", new Task("eat food", null, new Date(19,10), new Time("1700"), null)));
	}
}
