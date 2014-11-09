package parser;

import static org.junit.Assert.*;
import lol.Date;

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
		
		DateParser dp7 = new DateParser("add camp at island from 12-15 Dec");
		assertEquals(new Date(12, 12, 14), dp7.getDueDate());
		DateParser dp8 = new DateParser("add camp at island from 24/11 to 2 dec");
		assertEquals(new Date(24, 11, 14), dp8.getDueDate());
		DateParser dp9 = new DateParser("add camp at island from 24 Dec 2014 to 2 Jan 2015");
		assertEquals(new Date(24, 12, 14), dp9.getDueDate());
		
		DateParser dp10 = new DateParser("add camp at island 12-15 Dec");
		assertEquals(new Date(12, 12, 14), dp10.getDueDate());
		DateParser dp11 = new DateParser("add camp at island 24/11 to 2 dec");
		assertEquals(new Date(24, 11, 14), dp11.getDueDate());
		DateParser dp12 = new DateParser("add camp at island 24 Dec 2014 to 2 Jan 2015");
		assertEquals(new Date(24, 12, 14), dp12.getDueDate());
	}
	
	@Test
	public void testGetEndDate() {
		DateParser dp6 = new DateParser("add eat 3 octo");
		assertEquals(null, dp6.getEndDate());
		DateParser dp7 = new DateParser("add camp at island from 12-15 Dec");
		assertEquals(new Date(15, 12, 14), dp7.getEndDate());
		DateParser dp8 = new DateParser("add camp at island from 24/11 to 2 dec");
		assertEquals(new Date(2, 12, 14), dp8.getEndDate());
		DateParser dp9 = new DateParser("add camp at island from 24 Dec 2014 to 2 Jan 2015");
		assertEquals(new Date(2, 1, 2015), dp9.getEndDate());
	}
	@Test
	public void testGetDayOfTheWeek() {
		DateParser dp = new DateParser();
		assertEquals("Wed", dp.getDayOfTheWeek(new Date(22, 10,2014)));
		assertEquals("Thu", dp.getDayOfTheWeek(new Date(23, 10,2014)));
		assertEquals("Fri", dp.getDayOfTheWeek(new Date(24, 10,2014)));
		assertEquals("Sat", dp.getDayOfTheWeek(new Date(25, 10,2014)));
		assertEquals("Sun", dp.getDayOfTheWeek(new Date(26, 10,2014)));
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
		
		DateParser dp10 = new DateParser("add camp at island from 12-15 Dec at 8am");
		assertEquals("add camp at island at 8am", dp10.getUserInputWithoutDueDate());
		DateParser dp20 = new DateParser("add at island  from 24/11 to 2/12 9am camp");
		assertEquals("add at island 9am camp", dp20.getUserInputWithoutDueDate());
		DateParser dp9 = new DateParser("add camp at island from 24 Dec 2014 to 2 Jan 2015");
		assertEquals("add camp at island", dp9.getUserInputWithoutDueDate());
		
		DateParser dp11 = new DateParser("add camp at island on 12-15 Dec");
		assertEquals("add camp at island", dp11.getUserInputWithoutDueDate());
		DateParser dp12 = new DateParser("add camp at island on 24/11 to 2/12");
		assertEquals("add camp at island", dp12.getUserInputWithoutDueDate());
		DateParser dp13 = new DateParser("add camp at island on 24 Dec 2014 to 2 Jan 2015");
		assertEquals("add camp at island", dp13.getUserInputWithoutDueDate());
		
		DateParser dp14 = new DateParser("add at island 9am 12-15 Dec camp");
		assertEquals("add at island 9am camp", dp14.getUserInputWithoutDueDate());
		DateParser dp15 = new DateParser("add at island 24/11 to 2/12 camp");
		assertEquals("add at island camp", dp15.getUserInputWithoutDueDate());
		DateParser dp16 = new DateParser("add camp at island 24 Dec 2014 to 2 Jan 2015 8.30-10am");
		assertEquals("add camp at island 8.30-10am", dp16.getUserInputWithoutDueDate());
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
		assertEquals(new Date(31, 10, 15), dp.createDate("31 oct"));
		assertEquals(new Date(31, 10, 15), dp.createDate("31/10"));
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
	
	@Test
	public void testIsDateRange() {
		DateParser dp = new DateParser();
		assertTrue(dp.isDateRange("24-26 Nov"));
		assertTrue(dp.isDateRange("24 Nov - 26 Nov"));
		assertTrue(dp.isDateRange("Sun to Tue"));
		assertTrue(dp.isDateRange("24/11/14-2/12/14"));
		
	}
	
	@Test
	public void testAddDaysToDate() {
		DateParser dp = new DateParser();
		assertEquals(new Date(4, 12, 2014), dp.addDaysToDate(new Date(1, 12, 2014), 3));
		assertEquals(new Date(4, 11, 2014), dp.addDaysToDate(new Date(31, 10, 2014), 4));
	}
	
	@Test
	public void testCreateDatesFromRange() {
		DateParser dp = new DateParser();
		Date[] arr1 = { new Date(2, 12, 2014), new Date(5, 12, 2014) };
		assertArrayEquals(arr1, dp.createDatesFromRange("2-5 Dec"));
		Date[] arr2 = { new Date(29, 11, 2014), new Date(15, 12, 2014) };
		assertArrayEquals(arr2, dp.createDatesFromRange("29/11 to 15/12"));
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
