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

		assertEquals(new Task("camp", "island", new Date(12, 12, 2014),
				new Date(15, 12, 2014)),
				LOLParser.getTask("add camp at island from 12-15 Dec"));
		assertEquals(new Task("camp", "island", new Date(12, 12, 2014),
				new Date(15, 12, 2014)),
				LOLParser.getTask("add camp at island from 12-15/12"));
		assertEquals(new Task("camp", "island", new Date(24, 11, 2014),
				new Date(2, 12, 2014)),
				LOLParser.getTask("add camp at island from 24 nov-2 dec"));
		assertEquals(new Task("camp", "island", new Date(24, 11, 2014),
				new Date(2, 12, 2014)),
				LOLParser.getTask("add camp at island from 24/11 to 2 dec"));
	}

	@Test
	public void testGetTaskIndex() {
		assertEquals(2, LOLParser.getTaskIndex("delete 2"));
		assertEquals(6, LOLParser.getTaskIndex("edit 6 send letter sat"));
		assertEquals(-1, LOLParser.getTaskIndex("delete tt"));
	}

	@Test
	public void testGetTaskIndexArray() {
		int[] arr = { 1, 2, 3 };
		int[] arr1 = { 2 };
		assertArrayEquals(arr, LOLParser.getTaskIndexArray("delete 1 2 3"));
		assertArrayEquals(arr, LOLParser.getTaskIndexArray(" done 1  2 3  "));
		assertArrayEquals(arr1, LOLParser.getTaskIndexArray("  rm   2  "));
		assertArrayEquals(null, LOLParser.getTaskIndexArray(" add task jjj"));
	}

	@Test
	public void testGetEditTask() throws Exception {
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

	@Test
	public void testGetParameterInOriginalCase() {
		assertEquals("StuDy ProGRaMMiNg", LOLParser.getParameterInOriginalCase(
				"StuDy ProGRaMMiNg AT HoMe", "study programming"));
		assertEquals("HoMe", LOLParser.getParameterInOriginalCase(
				"StuDy ProGRaMMiNg AT HoMe", "home"));
		assertEquals("pIcNIc", LOLParser.getParameterInOriginalCase(
				"pIcNIc AT PicNic SPoT", "picnic"));
		assertEquals("PicNic SPoT", LOLParser.getParameterInOriginalCase(
				"pIcNIc AT PicNic SPoT", "picnic spot"));
	}
}
