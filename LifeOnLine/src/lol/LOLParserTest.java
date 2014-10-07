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

		// date dependent
		assertEquals(new Date(15, 10),
				LOLParser.getDueDate("add buy milk\\supermarket\\wed"));
		assertEquals(new Date(8, 10),
				LOLParser.getDueDate("add buy milk\\supermarket\\11.50pm"));
		assertEquals(new Date(1, 1, 15),
				LOLParser.getDueDate("add new year party\\grand hotel\\1/1/15"));
	}

	@Test
	public void testGetTask() {
		assertEquals(new Task("buy milk", null, new Date(14, 9)),
				LOLParser.getTask("add buy milk\\14 sep"));
		assertEquals(new Task("buy milk", null, null),
				LOLParser.getTask("add buy milk"));
		assertEquals(new Task("buy bread", null, new Date(8, 10), new Time(11, 30, "pm"), null),
				LOLParser.getTask("add buy bread\\11.30pm"));
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
		// date dependent
		assertEquals(new Date(11,10),
				LOLParser.getEditDueDate("edit 6 send letter\\sat"));
		assertEquals(
				new Date(19, 7),
				LOLParser
						.getEditDueDate("edit 6 send letter\\post office\\19 jul"));
	}

	@Test
	public void testGetEditTask() {
		// date dependent
		assertEquals(new Task("send letter", null, new Date(11, 10)),
				LOLParser.getEditTask("edit 6 send letter\\sat"));
		assertEquals(new Task("send letter", "post office", new Date(29, 7)),
				LOLParser
						.getEditTask("edit 6 send letter\\post office\\29 jul"));
	}
	
	@Test
	public void testGetStartTime() {
		assertEquals(new Time(2, 20, "am"), LOLParser.getStartTime("add send letter\\3 nov\\2.20am"));
		assertEquals(new Time(2, 20, "am"), LOLParser.getStartTime("add send letter\\post office\\3 nov\\2.20-3am"));
		assertEquals(new Time(2, 20, "am"), LOLParser.getStartTime("add send letter\\2.20am"));
	}
	
	@Test
	public void testGetEndTime() {
		assertEquals(new Time(2, 20, "am"), LOLParser.getEndTime("add send letter\\3 nov\\2-2.20am"));
		assertEquals(new Time("0300"), LOLParser.getEndTime("add send letter\\post office\\3 nov\\2.20 to 3am"));
		assertEquals(new Time(4, 5, "am"), LOLParser.getEndTime("add send letter\\2.20am to 4.05 am"));
	}
	
}
