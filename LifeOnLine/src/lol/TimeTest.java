package lol;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeTest {

	@Test
	public void testConvertTo24hr() {
		Time t = new Time();
		assertEquals("1130", t.convertTo24hr(11, 30, "am"));
		assertEquals("0530", t.convertTo24hr(5, 30, "am"));
		assertEquals("0050", t.convertTo24hr(12, 50, "am"));
		assertEquals("0800", t.convertTo24hr(8, 0, "am"));
		assertEquals("1200", t.convertTo24hr(12, 0, "pm"));
		assertEquals("1410", t.convertTo24hr(2, 10, "pm"));
		assertEquals("2359", t.convertTo24hr(11, 59, "pm"));
	}
	
	@Test
	public void testGetHour() {
		Time t = new Time();
		assertEquals(11, t.getHour("1130"));
		assertEquals(5, t.getHour("0530"));
		assertEquals(12, t.getHour("0050"));
		assertEquals(8, t.getHour("0800"));
		assertEquals(12, t.getHour("1200"));
		assertEquals(2, t.getHour("1410"));
		assertEquals(11, t.getHour("2359"));
	}
	
	@Test
	public void testGetMin() {
		Time t = new Time();
		assertEquals(30, t.getMin("1130"));
		assertEquals(6, t.getMin("0506"));
		assertEquals(50, t.getMin("0050"));
		assertEquals(0, t.getMin("0800"));
		assertEquals(0, t.getMin("1200"));
		assertEquals(10, t.getMin("1410"));
		assertEquals(59, t.getMin("2359"));
	}
	
	@Test
	public void testGetAmpm() {
		Time t = new Time();
		assertEquals("am", t.getAmpm("1130"));
		assertEquals("am", t.getAmpm("0530"));
		assertEquals("am", t.getAmpm("0050"));
		assertEquals("am", t.getAmpm("0800"));
		assertEquals("pm", t.getAmpm("1200"));
		assertEquals("pm", t.getAmpm("1410"));
		assertEquals("pm", t.getAmpm("2359"));
	}

}
