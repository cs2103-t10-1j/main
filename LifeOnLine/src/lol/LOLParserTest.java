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

	@Test
	public void testGetLocation() {
		assertEquals("supermarket", LOLParser.getLocation("add buy milk\\supermarket"));
		assertEquals("supermarket", LOLParser.getLocation("add buy milk\\supermarket\\tmw"));
	}

	@Test
	public void testGetDueDate() {
		assertEquals(new Date(14,9), LOLParser.getDueDate("add buy milk\\14/9"));
		assertEquals(new Date(14,9), LOLParser.getDueDate("add buy milk\\supermarket\\14 sep"));
	}

	@Test
	public void testGetTask() {
		assertEquals(new Task("buy milk", null, new Date(14, 9)), LOLParser.getTask("add buy milk\\14 sep"));
	}
	
	@Test
	public void testIsDate() {
		assertTrue(LOLParser.isDate("30/9/2014"));
		assertTrue(LOLParser.isDate("30/9/14"));
		assertTrue(LOLParser.isDate("30 / 9"));
		assertTrue(LOLParser.isDate("30 september 2014"));
		assertTrue(LOLParser.isDate("30 sep 2014"));
		assertTrue(LOLParser.isDate("30 september 14"));
		assertTrue(LOLParser.isDate("30 sep 14"));
		assertTrue(LOLParser.isDate("30 sep"));
		assertTrue(LOLParser.isDate("fri"));
		assertTrue(LOLParser.isDate("monday"));
		assertTrue(LOLParser.isDate("today"));
		assertTrue(LOLParser.isDate("tomorrow"));
		assertTrue(LOLParser.isDate("tmw"));
	}
	
	@Test
	public void testCreateDate() {
		assertEquals(30, LOLParser.createDate("30/9/2014").getDay());
		assertEquals("Sep", LOLParser.createDate("30/9/2014").getMonthName());
		assertEquals(30, LOLParser.createDate("30 sep 15").getDay());
		assertEquals("Sep", LOLParser.createDate("30 sep 15").getMonthName());
		assertEquals(30, LOLParser.createDate("30/9").getDay());
		assertEquals("Sep", LOLParser.createDate("30/9").getMonthName());
		assertEquals(30, LOLParser.createDate("30 sep").getDay());
		assertEquals("Sep", LOLParser.createDate("30 sep").getMonthName());
		assertEquals(new Date(30,9,2014), LOLParser.createDate("30/9/2014"));
	}
}
