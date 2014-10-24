package parser;

import static org.junit.Assert.*;
import lol.Date;
import lol.Task;
import lol.Time;

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
	public void testGetTask() throws Exception {
		assertEquals(new Task("buy milk", null, new Date(14, 9)),
				LOLParser.getTask("add buy milk 14 sep"));
		assertEquals(new Task("buy milk", null, null),
				LOLParser.getTask("add buy milk"));
		assertEquals(null, LOLParser.getTask("add "));
		
		/*assertEquals(new Task("sleep", null, new Date(25, 10),
				new Time("0020"), null),
				LOLParser.getTask("add sleep at 12.20am"));*/
		/*
		 * assertEquals(new Task("buy bread", null, new Date(8, 10), new
		 * Time(11, 30, "pm"), null),
		 * LOLParser.getTask("add buy bread\\11.30pm"));
		 */
	}

	@Test
	public void testGetTaskIndex() {
		assertEquals(2, LOLParser.getTaskIndex("delete 2"));
		assertEquals(6, LOLParser.getTaskIndex("edit 6 send letter sat"));
		assertEquals(-1, LOLParser.getTaskIndex("delete tt"));
	}

	@Test
	public void testGetEditTask() {
		assertEquals(new Task("buy milk", "supermarket", null),
				LOLParser.getEditTask("edit 6 at supermarket", new Task(
						"buy milk", null, null)));
		assertEquals(new Task("buy milk", "supermarket", null),
				LOLParser.getEditTask("edit 6 buy milk", new Task("buy juice",
						"supermarket", null)));
		assertEquals(new Task("buy milk", "supermarket", new Date(13, 11)),
				LOLParser.getEditTask("edit 6 13 nov", new Task("buy milk",
						"supermarket", null)));
		assertEquals(new Task("eat food", null, new Date(19, 10), new Time(
				"1800"), null), LOLParser.getEditTask("edit 2 6pm", new Task(
				"eat food", null, new Date(19, 10), new Time("1700"), null)));
		assertEquals(null, LOLParser.getEditTask("edit  1", new Task(
				"buy milk", "supermarket", null)));
		assertEquals(new Task("do something", "home", null),
				LOLParser.getEditTask("edit 2 at home rm date", new Task(
						"do something", null, new Date(1, 1, 15))));
	}

	@Test
	public void testGetDateForShowCommand() {
		assertEquals(new Date(20, 11),
				LOLParser.getDateForShowCommand("show 20/11/14"));
		assertEquals(null, LOLParser.getDateForShowCommand("show 20 novv"));
	}

	@Test
	public void testCountWords() {
		assertEquals(5, LOLParser.countWords("add on mon  do   something  "));
	}

	@Test
	public void testGetKeywordsForSearchCommand() {
		assertEquals("friends",
				LOLParser.getKeywordsForSearchCommand("search friends"));
		assertEquals("10 oct",
				LOLParser.getKeywordsForSearchCommand("search 10 oct"));
		assertEquals(
				"dinner with friends",
				LOLParser
						.getKeywordsForSearchCommand("search dinner with friends"));
		assertEquals(null, LOLParser.getKeywordsForSearchCommand("search"));
	}
}
