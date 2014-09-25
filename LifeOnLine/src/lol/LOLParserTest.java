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
	}
	
	@Test
	public void testGetDescription() {
		assertEquals("buy milk", LOLParser.getDescription("add buy milk"));
		assertEquals("buy milk", LOLParser.getDescription("add buy milk\\supermarket"));
		assertEquals("buy milk", LOLParser.getDescription("add buy milk\\today"));
		assertEquals("buy milk", LOLParser.getDescription("add buy milk\\14 sep"));
		assertEquals("buy milk", LOLParser.getDescription("add buy milk\\supermarket\\14 sep"));
	}
/*
	@Test
	public void testGetLocation() {
		assertEquals("supermarket", LOLParser.getLocation("add buy milk \\ supermarket"));
		assertEquals("supermarket", LOLParser.getLocation("add buy milk \\ supermarket \\ tmw"));
	}

	@Test
	public void testGetDueDate() {
		assertEquals(new Date(14,9), LOLParser.getDueDate("add buy milk\\14 sep"));
		assertEquals(new Date(14,9), LOLParser.getDueDate("add buy milk\\supermarket\\14 sep"));
	}

	@Test
	public void testGetTask() {
		assertEquals(new Task("buy milk", null, new Date(14, 9)), LOLParser.getTask("add buy milk\\14 sep"));
	}*/
}
