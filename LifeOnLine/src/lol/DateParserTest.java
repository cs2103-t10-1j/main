package lol;

import static org.junit.Assert.*;

import org.junit.Test;

public class DateParserTest {

	@Test
	public void testIsValidDate() {
		DateParser dp = new DateParser();
		assertTrue(dp.isValidDate("31 oct"));
		assertTrue(dp.isValidDate("31 october"));
		assertTrue(dp.isValidDate("31 oct 2014"));
		assertTrue(dp.isValidDate("31 october 2014"));
		assertTrue(dp.isValidDate("31 oct 14"));
		assertTrue(dp.isValidDate("31 october 14"));
		assertTrue(dp.isValidDate("31/10"));
		assertTrue(dp.isValidDate("31/10/14"));
		assertTrue(dp.isValidDate("31/10/2014"));
		assertFalse(dp.isValidDate("31/13/14"));
		assertFalse(dp.isValidDate("abcde"));
		assertFalse(dp.isValidDate("5 novel"));
		assertTrue(dp.isValidDate("16 octo 15"));
		assertFalse(dp.isValidDate("16"));
	}

	@Test
	public void testCreateDate() {
		DateParser dp = new DateParser();
		assertEquals(new Date(31, 10, 14), dp.createDate("31 oct"));
		assertEquals(new Date(31, 10, 14), dp.createDate("31/10"));
		assertEquals(null, dp.createDate("abcde"));
	}

	@Test
	public void testGetMonthNum() {
		DateParser dp = new DateParser();
		assertEquals(1, dp.getMonthNum("jan"));
		assertEquals(2, dp.getMonthNum("february"));
		assertEquals(12, dp.getMonthNum("dec"));
		assertEquals(-1, dp.getMonthNum("abcde"));
	}

	@Test
	public void testGetDayOfTheWeekIndex() {
		DateParser dp = new DateParser();
		assertEquals(0, dp.getDayOfTheWeekIndex("sun"));
		assertEquals(2, dp.getDayOfTheWeekIndex("tuesday"));
		assertEquals(6, dp.getDayOfTheWeekIndex("sat"));
		assertEquals(-1, dp.getDayOfTheWeekIndex("abcde"));
	}

	@Test
	public void testIsValidDay() {
		DateParser dp = new DateParser();
		assertTrue(dp.isValidDay("mon"));
		assertTrue(dp.isValidDay("thursday"));
		assertTrue(dp.isValidDay("today"));
		assertTrue(dp.isValidDay("tomorrow"));
		assertTrue(dp.isValidDay("tmw"));
		assertFalse(dp.isValidDay("abcde"));
	}

	// The following methods depend on the current date.
	
	/*
	@Test
	public void testGetTodaysDate() {
		DateParser dp = new DateParser();
		assertEquals(new Date(6,10), dp.getTodaysDate());
	}

	@Test
	public void testAddDaysToToday() {
		DateParser dp = new DateParser();
		assertEquals(new Date(7,10), dp.addDaysToToday(1));
		assertEquals(new Date(5,10), dp.addDaysToToday(-1));
	}

	@Test
	public void testGetTodaysDayOfTheWeek() {
		DateParser dp = new DateParser();
		assertEquals("monday", dp.getTodaysDayOfTheWeek());
	}

	@Test
	public void testGetTodaysDayOfTheWeekIndex() {
		DateParser dp = new DateParser();
		assertEquals(1, dp.getTodaysDayOfTheWeekIndex());
	}
	*/

}
