package parser;

import static org.junit.Assert.*;
import lol.Time;

import org.junit.Test;

public class TimeParserTest {
	@Test
	public void testGetUserInputWithoutTime() {
		TimeParser tp = new TimeParser("add send letter at post office at 2.20am");
		assertEquals("add send letter at post office", tp.getUserInputWithoutTime());
		TimeParser tp1 = new TimeParser(" add   send letter  by 1800 at post  office on mon");
		assertEquals("add send letter at post office on mon", tp1.getUserInputWithoutTime());
		TimeParser tp2 = new TimeParser("add from 7pm meeting");
		assertEquals("add meeting", tp2.getUserInputWithoutTime());
		TimeParser tp3 = new TimeParser("add 4am do something");
		assertEquals("add do something", tp3.getUserInputWithoutTime());
		TimeParser tp4 = new TimeParser("add 11 meetings 11-4pm");
		assertEquals("add 11 meetings", tp4.getUserInputWithoutTime());
		TimeParser tp5 = new TimeParser("add something from 1 pm to 2 pm ");
		assertEquals("add something", tp5.getUserInputWithoutTime());
		TimeParser tp6 = new TimeParser("add something");
		assertEquals("add something", tp6.getUserInputWithoutTime());
		TimeParser tp7 = new TimeParser("add eat 3 pizzas at 3 pm");
		assertEquals("add eat 3 pizzas", tp7.getUserInputWithoutTime());
		TimeParser tp8 = new TimeParser("add eat 3 pizzas by 3 pm");
		assertEquals("add eat 3 pizzas", tp8.getUserInputWithoutTime());
		TimeParser tp9 = new TimeParser("add eat 3 pizzas 3 pm");
		assertEquals("add eat 3 pizzas", tp9.getUserInputWithoutTime());
		TimeParser tp10 = new TimeParser("add watch at golden village today 6 pm to 8 pm");
		assertEquals("add watch at golden village today", tp10.getUserInputWithoutTime());
		TimeParser tp11 = new TimeParser("add watch at golden village today 6 - 8 pm");
		assertEquals("add watch at golden village today", tp11.getUserInputWithoutTime());
	}
	@Test
	public void testGetStartTime() {
		TimeParser tp = new TimeParser("add send letter at post office at 2.20am");
		assertEquals(new Time(2, 20, "am"), tp.getStartTime());
		TimeParser tp1 = new TimeParser(" add   send letter  by 1800 at post  office on mon");
		assertEquals(new Time(6, "pm"), tp1.getStartTime());
		TimeParser tp2 = new TimeParser("add from 7pm meeting");
		assertEquals(new Time("1900"), tp2.getStartTime());
		TimeParser tp3 = new TimeParser("add 4am do something");
		assertEquals(new Time(4, "am"), tp3.getStartTime());
		TimeParser tp4 = new TimeParser("add 11 meetings 11-4pm");
		assertEquals(new Time(11, "am"), tp4.getStartTime());
		TimeParser tp5 = new TimeParser("add something from 1 pm to 2 pm ");
		assertEquals(new Time("1300"), tp5.getStartTime());
		TimeParser tp9 = new TimeParser("add eat 3 pizzas 3 pm");
		assertEquals(new Time("1500"), tp9.getStartTime());
	}
	
	@Test
	public void testGetEndTime() {
		TimeParser tp2 = new TimeParser("add meeting 11pm");
		assertEquals(null, tp2.getEndTime());
		TimeParser tp3 = new TimeParser("add from 3-6pm do something");
		assertEquals(new Time(6, "pm"), tp3.getEndTime());
		TimeParser tp4 = new TimeParser("add meeting 11-4pm");
		assertEquals(new Time(4, "pm"), tp4.getEndTime());
		TimeParser tp5 = new TimeParser("add something from 1 pm to 2 pm ");
		assertEquals(new Time("1400"), tp5.getEndTime());
	}
	
	@Test
	public void testRemoveDescriptionAfterTimeIfAny() {
		TimeParser tp = new TimeParser();
		assertEquals("7am", tp.removeDescriptionAfterTimeIfAny("7am do this"));
		assertEquals("7 am", tp.removeDescriptionAfterTimeIfAny("7 am do this"));
		assertEquals("8 am - 9 am", tp.removeDescriptionAfterTimeIfAny("8 am - 9 am do this"));
	}
	
	@Test
	public void testHasTime() {
		TimeParser tp = new TimeParser();
		String[] nextWords = {"am", "to", "8", "am"};
		assertTrue(tp.hasTime("7", nextWords));
		String[] nextWords1 = {"", "", "", ""};
		assertTrue(tp.hasTime("7am", nextWords1));
		String[] nextWords2 = {"am", "", "", ""};
		assertTrue(tp.hasTime("7", nextWords2));
	}

	@Test
	public void testIs12hrTime() {
		TimeParser tp = new TimeParser();
		assertTrue(tp.is12hrTime("2pm"));
		assertTrue(tp.is12hrTime("2.30 am"));
		assertFalse(tp.is12hrTime("19.20am"));
	}

	@Test
	public void testIs24hrTime() {
		TimeParser tp = new TimeParser();
		assertTrue(tp.is24hrTime("0800"));
		assertTrue(tp.is24hrTime("2359"));
		assertFalse(tp.is24hrTime("9999"));
	}

	@Test
	public void testIsTimeRange() {
		TimeParser tp = new TimeParser();
		assertTrue(tp.isTimeRange("11-1pm"));
		assertTrue(tp.isTimeRange("11am-1pm"));
		assertTrue(tp.isTimeRange("4-6pm"));
		assertTrue(tp.isTimeRange("4pm-6pm"));
		assertTrue(tp.isTimeRange("11  to 1pm"));
		assertTrue(tp.isTimeRange("11am to 1pm"));
		assertTrue(tp.isTimeRange("4 to 6pm"));
		assertTrue(tp.isTimeRange("4pm to 6pm"));
		assertFalse(tp.isTimeRange("9999"));
	}

	@Test
	public void testIsTimeWithoutAmpm() {
		TimeParser tp = new TimeParser();
		assertTrue(tp.isTimeWithoutAmpm("11.30"));
		assertTrue(tp.isTimeWithoutAmpm("5"));
		assertFalse(tp.isTimeWithoutAmpm("5am"));
	}

	@Test
	public void testIsHourInRange() {
		TimeParser tp = new TimeParser();
		assertTrue(tp.isHourInRange("12"));
		assertTrue(tp.isHourInRange("4"));
		assertFalse(tp.isHourInRange("40"));
	}

	@Test
	public void testIsMinuteInRange() {
		TimeParser tp = new TimeParser();
		assertTrue(tp.isMinuteInRange("12"));
		assertTrue(tp.isMinuteInRange("0"));
		assertFalse(tp.isMinuteInRange("60"));
	}

	@Test
	public void testCreate12hrTime() {
		TimeParser tp = new TimeParser();
		assertEquals(new Time(10, 30, "am"), tp.create12hrTime("10.30am"));
		assertEquals(new Time(5, "pm"), tp.create12hrTime("5 pm"));
	}

	@Test
	public void testCreateStartTimeFromRange() {
		TimeParser tp = new TimeParser();
		assertEquals(new Time(10, 30, "am"), tp.createStartTimeFromRange("10.30-2pm"));
		assertEquals(new Time(5, "pm"), tp.createStartTimeFromRange("5pm to 9pm"));
		assertEquals(new Time(7, 40, "pm"), tp.createStartTimeFromRange("7.40-9pm"));
		assertEquals(new Time(3, 10, "am"), tp.createStartTimeFromRange("3.10 - 4 am"));
	}
	
	@Test
	public void testCreateEndTimeFromRange() {
		TimeParser tp = new TimeParser();
		assertEquals(new Time(2, "pm"), tp.createEndTimeFromRange("10.30-2pm"));
		assertEquals(new Time(9, 30, "pm"), tp.createEndTimeFromRange("5pm to 9.30pm"));
		assertEquals(new Time(9, "pm"), tp.createEndTimeFromRange("7.40-9pm"));
		assertEquals(new Time(4, "am"), tp.createEndTimeFromRange("3.10 - 4 am"));
	}
	/*
	@Test
	public void testGetCurrentTime() {
		TimeParser tp = new TimeParser();
		assertEquals(new Time("1751"), tp.getCurrentTime());
	}*/
	
	@Test
	public void testIsValidTimeFormat() {
		TimeParser tp = new TimeParser();
		
		/* This is an equivalence partition for a 12-hour time format */
		assertTrue(tp.isValidTimeFormat("2pm"));
		assertTrue(tp.isValidTimeFormat("2.30 am"));
		
		/* This is a boundary case for a 12-hour time format */
		assertTrue(tp.isValidTimeFormat("12.59 am"));
		assertFalse(tp.isValidTimeFormat("12.60 am"));
		
		/* This is an equivalence partition for a time range separated by hyphen */
		assertTrue(tp.isValidTimeFormat("4-6pm"));
		assertTrue(tp.isValidTimeFormat("4pm-6pm"));
		assertTrue(tp.isValidTimeFormat("6 am - 7 am"));
		
		/* This is an equivalence partition for a time range separated by "to" */
		assertTrue(tp.isValidTimeFormat("11  to 1pm"));
		assertTrue(tp.isValidTimeFormat("11am to 1pm"));
		assertTrue(tp.isValidTimeFormat("6  am to  7 am"));
		
		/* This is an equivalence partition for a 24-hour time format */
		assertTrue(tp.isValidTimeFormat("0800"));
		
		/* This is a boundary case for a 24-hour time format */
		assertTrue(tp.isValidTimeFormat("0000"));
		assertFalse(tp.isValidTimeFormat("2400"));
	}
}
