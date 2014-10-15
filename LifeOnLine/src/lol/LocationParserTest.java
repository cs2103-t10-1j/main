package lol;

import static org.junit.Assert.*;

import org.junit.Test;

public class LocationParserTest {

	@Test
	public void testGetLocation() {
		LocationParser lp = new LocationParser("add buy pizza at clementi on 23 oct at 10am");
		assertEquals("clementi", lp.getLocation());
	}

	@Test
	public void testCleanUp() {
		LocationParser lp = new LocationParser("  add buy pizza at   clementi on 23 oct  at   10am  ");
		assertEquals("add buy pizza at clementi on 23 oct at 10am", lp.cleanUp());
	}

	@Test
	public void testCountNumberOfAt() {
		LocationParser lp = new LocationParser("add buy pizza at clementi on 23 oct at 10am");
		assertEquals(2, lp.countNumberOfAt());
	}

	@Test
	public void testGetParameterStartingAtIndex() {
		LocationParser lp = new LocationParser("add buy pizza at clementi on 23 oct at 10am");
		assertEquals("clementi", lp.getParameterStartingAtIndex(17));
	}

	@Test
	public void testGetIndexOfNextKeyword() {
		LocationParser lp = new LocationParser("add buy pizza at clementi on 23 oct at 10am");
		assertEquals(14, lp.getIndexOfNextKeyword(0));
		assertEquals(26, lp.getIndexOfNextKeyword(17));
	}

	@Test
	public void testGetIndexOfAt() {
		LocationParser lp = new LocationParser("add buy pizza at clementi on 23 oct at 10am");
		assertEquals(14, lp.getIndexOfAt());
	}

	@Test
	public void testIsIndexOutOfBounds() {
		LocationParser lp = new LocationParser("add buy pizza at clementi on 23 oct at 10am");
		assertTrue(lp.isIndexOutOfBounds(-1));
		assertTrue(lp.isIndexOutOfBounds(50));
		assertFalse(lp.isIndexOutOfBounds(10));
	}

	@Test
	public void testHasWordInDictionary() {
		LocationParser lp = new LocationParser("add buy pizza at clementi on 23 oct at 10am");
		assertTrue(lp.hasWordInDictionary(Constants.KEYWORDS, "at"));
	}

}
