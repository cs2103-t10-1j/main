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
		assertEquals("buy milk",
				LOLParser.getDescription("add buy milk\\supermarket"));
		assertEquals("buy milk",
				LOLParser.getDescription("add buy milk\\today"));
		assertEquals("buy milk",
				LOLParser.getDescription("add buy milk\\14 sep"));
		assertEquals("buy milk",
				LOLParser.getDescription("add buy milk\\supermarket\\14 sep"));
	}

	@Test
	public void testGetLocation() {
		assertEquals("supermarket",
				LOLParser.getLocation("add buy milk\\supermarket"));
		assertEquals("supermarket",
				LOLParser.getLocation("add buy milk\\supermarket\\tmw"));
	}

	@Test
	public void testGetDueDate() {
		assertEquals(new Date(14, 9),
				LOLParser.getDueDate("add buy milk\\14/9"));
		assertEquals(new Date(14, 9),
				LOLParser.getDueDate("add buy milk\\supermarket\\14 sep"));

		assertEquals(new Date(28, 9),
				LOLParser.getDueDate("add buy milk\\supermarket\\sun"));
		assertEquals(new Date(1, 1, 15),
				LOLParser.getDueDate("add new year party\\grand hotel\\1/1/15"));
	}

	@Test
	public void testGetTask() {
		assertEquals(new Task("buy milk", null, new Date(14, 9)),
				LOLParser.getTask("add buy milk\\14 sep"));
		assertEquals(new Task("buy milk", null, null),
				LOLParser.getTask("add buy milk"));
	}

	@Test
	public void testIsValidDate() {
		assertTrue(LOLParser.isValidDate("30/9/2014"));
		assertTrue(LOLParser.isValidDate("30/9/14"));
		assertTrue(LOLParser.isValidDate("1/1/15"));
		assertTrue(LOLParser.isValidDate("30 september 2014"));
		assertTrue(LOLParser.isValidDate("30 sep 2014"));
		assertTrue(LOLParser.isValidDate("30 september 14"));
		assertTrue(LOLParser.isValidDate("30 sep 14"));
		assertTrue(LOLParser.isValidDate("30 sep"));
	}

	@Test
	public void testIsValidDay() {
		assertTrue(LOLParser.isValidDay("fri"));
		assertTrue(LOLParser.isValidDay("monday"));
		assertTrue(LOLParser.isValidDay("today"));
		assertTrue(LOLParser.isValidDay("tomorrow"));
		assertTrue(LOLParser.isValidDay("tmw"));
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

		assertEquals(new Time(), LOLParser.createDate("30 sep").getTime());
		assertEquals(new Date(30, 9, 2014), LOLParser.createDate("30/9/2014"));
		assertEquals(new Date(1, 1, 15), LOLParser.createDate("1/1/15"));

		assertEquals(new Date(26, 9, 2014), LOLParser.createDate("today"));
		assertEquals(new Date(27, 9, 2014), LOLParser.createDate("saturday"));
		assertEquals(new Date(27, 9, 2014), LOLParser.createDate("tmw"));
		assertEquals(new Date(29, 9, 2014), LOLParser.createDate("monday"));
		assertEquals(new Date(1, 10), LOLParser.createDate("wed"));
		assertEquals(new Date(3, 10), LOLParser.createDate("fri"));
	}

	@Test
	public void testGetTodaysDate() {
		assertEquals(26, LOLParser.getTodaysDate().getDay());
		assertEquals(9, LOLParser.getTodaysDate().getMonth());
		assertEquals(new Date(26, 9, 2014), LOLParser.getTodaysDate());
	}

	@Test
	public void testAddDaysToToday() {
		assertEquals(new Date(27, 9, 2014), LOLParser.addDaysToToday(1));
		assertEquals(new Date(1, 10, 2014), LOLParser.addDaysToToday(5));
		assertEquals(new Date(4, 1, 2015), LOLParser.addDaysToToday(100));
	}

	@Test
	public void testGetTodaysDayOfTheWeek() {
		assertEquals("friday", LOLParser.getTodaysDayOfTheWeek());
	}

	@Test
	public void testGetDayOfTheWeekIndex() {
		assertEquals(0, LOLParser.getDayOfTheWeekIndex("sun"));
		assertEquals(6, LOLParser.getDayOfTheWeekIndex("saturday"));
		assertEquals(3, LOLParser.getDayOfTheWeekIndex("wed"));
	}

	@Test
	public void testGetTaskIndex() {
		assertEquals(2, LOLParser.getTaskIndex("delete 2"));
		assertEquals(6, LOLParser.getTaskIndex("edit 6 send letter\\sat"));
		assertEquals(-1, LOLParser.getTaskIndex("delete tt"));
	}

	@Test
	public void testGetEditDescription() {
		assertEquals("send letter",
				LOLParser.getEditDescription("edit 6 send letter\\sun"));
		assertEquals(
				"send letter",
				LOLParser
						.getEditDescription("edit 6 send letter\\post office\\19 jul"));
	}

	@Test
	public void testGetEditLocation() {
		assertEquals(null, LOLParser.getEditLocation("edit 6 send letter\\sat"));
		assertEquals(
				"post office",
				LOLParser
						.getEditLocation("edit 6 send letter\\post office\\19 jul"));
	}

	@Test
	public void testGetEditDueDate() {
		assertEquals(new Date(27, 9),
				LOLParser.getEditDueDate("edit 6 send letter\\sat"));
		assertEquals(
				new Date(19, 7),
				LOLParser
						.getEditDueDate("edit 6 send letter\\post office\\19 jul"));
	}

	@Test
	public void testGetEditTask() {
		assertEquals(new Task("send letter", null, new Date(27, 9)),
				LOLParser.getEditTask("edit 6 send letter\\sat"));
		assertEquals(new Task("send letter", "post office", new Date(29, 7)),
				LOLParser
						.getEditTask("edit 6 send letter\\post office\\29 jul"));
	}
}
