package lol;

import static org.junit.Assert.*;

import org.junit.Test;

public class DateParserTest {
	@Test
	public void testGetDueDate() {
		DateParser dp1 = new DateParser("  add   buy   pizza  at clementi  on  29 oct at   10am ");
		assertEquals(new Date(29, 10), dp1.getDueDate());
		assertEquals("on", dp1.getDateKeyword());
		DateParser dp = new DateParser("  add  do something by  31/12 at home  at   10am ");
		assertEquals(new Date(31, 12), dp.getDueDate());
		assertEquals("by", dp.getDateKeyword());
		DateParser dp2 = new DateParser("  add by  31/12 do something  at home  at   10am ");
		assertEquals(new Date(31, 12), dp2.getDueDate());
		DateParser dp3 = new DateParser("  add do something 12/11");
		assertEquals(new Date(12, 11), dp3.getDueDate());
		DateParser dp4 = new DateParser("add eat at 10 food place 10 oct ");
		assertEquals(new Date(10, 10), dp4.getDueDate());
		DateParser dp5 = new DateParser("add eat 3 pizzas on 3 nov at 3 pm");
		assertEquals(new Date(3, 11), dp5.getDueDate());
		DateParser dp6 = new DateParser("add eat 3 octo");
		assertEquals(null, dp6.getDueDate());
	}
	
	@Test
	public void testGetUserInputWithoutDueDate() {
		DateParser dp1 = new DateParser("  add   buy   pizza  at clementi  on  29 oct at   10am ");
		assertEquals("add buy pizza at clementi at 10am", dp1.getUserInputWithoutDueDate());
		DateParser dp2 = new DateParser("  add   buy   pizza at   10am  at clementi  by  29 oct ");
		assertEquals("add buy pizza at 10am at clementi", dp2.getUserInputWithoutDueDate());
		DateParser dp3 = new DateParser("  add   buy   pizza  at clementi at   10am ");
		assertEquals("add buy pizza at clementi at 10am", dp3.getUserInputWithoutDueDate());
		DateParser dp4 = new DateParser("  add 30/10  buy   pizza  at clementi at   10am ");
		assertEquals("add buy pizza at clementi at 10am", dp4.getUserInputWithoutDueDate());
		DateParser dp5 = new DateParser("  add eat at 10 food place 10 oct");
		assertEquals("add eat at 10 food place", dp5.getUserInputWithoutDueDate());
		DateParser dp6 = new DateParser("add on mon do something 8-10pm");
		assertEquals("add do something 8-10pm", dp6.getUserInputWithoutDueDate());
		DateParser dp7 = new DateParser("add eat 3 pizzas on 3 nov at 3 pm");
		assertEquals("add eat 3 pizzas at 3 pm", dp7.getUserInputWithoutDueDate());
		DateParser dp8 = new DateParser("add mon do this");
		assertEquals("add do this", dp8.getUserInputWithoutDueDate());
	}

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
	
	@Test
	public void testRemoveDescriptionFromDueDateIfAny() {
		DateParser dp = new DateParser();
		assertEquals("31/12", dp.removeDescriptionFromDueDateIfAny("31/12 do something"));
		assertEquals("mon", dp.removeDescriptionFromDueDateIfAny("mon do something"));
		assertEquals("31/12/15", dp.removeDescriptionFromDueDateIfAny("31/12/15"));
		assertEquals("31 oct 2015", dp.removeDescriptionFromDueDateIfAny("31 oct 2015 do that"));
	}

	@Test
	public void testIsBoth24hrTimeAndYear() {
		DateParser dp = new DateParser();
		assertTrue(dp.isBoth24hrTimeAndYear("2016"));
		assertFalse(dp.isBoth24hrTimeAndYear("1300"));
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
