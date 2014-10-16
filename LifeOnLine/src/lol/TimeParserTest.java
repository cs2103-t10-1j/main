package lol;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeParserTest {

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
		assertTrue(tp.is12hrTime("2pm"));
		assertTrue(tp.is12hrTime("2.30 am"));
		assertTrue(tp.isTimeRange("4-6pm"));
		assertTrue(tp.isTimeRange("4pm-6pm"));
		assertTrue(tp.isTimeRange("11  to 1pm"));
		assertTrue(tp.isTimeRange("11am to 1pm"));
		assertTrue(tp.is24hrTime("0800"));
	}
}
