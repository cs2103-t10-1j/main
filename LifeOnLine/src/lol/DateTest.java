//@author A0118886M

package lol;

import static org.junit.Assert.*;

import org.junit.Test;

public class DateTest {

	@Test
	public void testGetDay() {
		Date d = new Date(4, 2, new Time(10, "am"));
		assertEquals(4, d.getDay());
	}

	@Test
	public void testGetMonth() {
		Date d = new Date(4, 2, new Time(10, "am"));
		assertEquals(2, d.getMonth());
	}

	@Test
	public void testGetMonthName() {
		Date d = new Date(4, 2, new Time(10, "am"));
		assertEquals("Feb", d.getMonthName());
	}

	@Test
	public void testGetYear4Digit() {
		Date d = new Date(4, 2, new Time(10, "am"));
		assertEquals(2015, d.getYear4Digit());
		Date d1 = new Date(4, 12, new Time(10, "am"));
		assertEquals(2014, d1.getYear4Digit());
	}

	@Test
	public void testGetYear2Digit() {
		Date d = new Date(4, 2, new Time(10, "am"));
		assertEquals(15, d.getYear2Digit());
		Date d1 = new Date(4, 12, new Time(10, "am"));
		assertEquals(14, d1.getYear2Digit());
	}

	@Test
	public void testGetTime() {
		Date d = new Date(4, 2, new Time(10, "am"));
		assertEquals(new Time("1000"), d.getTime());
	}
	
	@Test
	public void testGetCurrentYear() {
		Date d = new Date();
		assertEquals(2014, d.getCurrentYear());
	}

	@Test
	public void testToString() {
		Date d1 = new Date(4, 2, new Time(10, "am"));
		Date d2 = new Date(5, 6, 15, new Time(11, "am"));
		Date d3 = new Date(18, 8, 2013, new Time(10, "am"));
		Date d4 = new Date(18, 11, 14, new Time(10, "am"));
		Date d5 = new Date(19, 10);
		Date d6 = new Date(11, 10);
		assertEquals("11 Oct 2015", d6.toString());
		assertEquals("19 Oct 2015", d5.toString());
		assertEquals("18 Nov 2014", d4.toString());
		assertEquals("18 Aug 2013", d3.toString());
		assertEquals("5 Jun 2015", d2.toString());
		assertEquals("4 Feb 2015", d1.toString());
	}

	@Test
	public void testEqualsObject() {
		Date d1 = new Date(4, 2, new Time(10, "am"));
		Date d2 = new Date(4, 2, 15, new Time("1000"));
		Date d3 = new Date(18, 8, 2013, new Time(10, "am"));
		assertTrue(d1.equals(d2));
		assertFalse(d2.equals(d3));
	}

	@Test
	public void testGetMonthNameInt() {
		Date d = new Date();
		assertEquals("Mar", d.getMonthName(3));
	}

	@Test
	public void testIsBefore() {
		Date d1 = new Date(4, 2, new Time(10, "am"));
		Date d2 = new Date(4, 2, 14, new Time("1000"));
		Date d3 = new Date(18, 8, 2013, new Time(10, "am"));
		assertTrue(d3.isBefore(d2));
		assertFalse(d1.isBefore(d2));
	}

	@Test
	public void testIsAfter() {
		Date d1 = new Date(4, 2, new Time(10, "am"));
		Date d2 = new Date(4, 2, 14, new Time("1000"));
		Date d3 = new Date(18, 8, 2013, new Time(10, "am"));
		assertTrue(d2.isAfter(d3));
		assertFalse(d2.isAfter(d1));
	}

	@Test
	public void testGetDateDifference() {
		Date d = new Date();
		Date d1 = new Date(20, 10, 2014);
		Date d2 = new Date(23, 10, 2014);
		Date d3 = new Date(1, 9, 2014);
		assertEquals(3, d.getDateDifference(d1, d2));
		assertEquals(52, d.getDateDifference(d3, d2));
		assertEquals(-3, d1.getDateDifference(d2, d1));
	}
}
