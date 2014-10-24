package parser;

import static org.junit.Assert.*;
import lol.Constants;

import org.junit.Test;

public class LocationParserTest {

	@Test
	public void testGetLocation() {
		LocationParser lp = new LocationParser("   add    buy pizza at  clementi on 23   oct at   10am   ");
		assertEquals("clementi", lp.getLocation());
		LocationParser lp1 = new LocationParser("   add   buy pizza at 3am  on sun   at  clementi ");
		assertEquals("clementi", lp1.getLocation());
		LocationParser lp2 = new LocationParser("   add    buy pizza  on 23   oct at   10am   ");
		assertEquals(null, lp2.getLocation());
		LocationParser lp3 = new LocationParser("   add    buy pizza    ");
		assertEquals(null, lp3.getLocation());
		LocationParser lp4 = new LocationParser("   add    buy pizza  at jurong  tomorrow  ");
		assertEquals("jurong", lp4.getLocation());
		LocationParser lp5 = new LocationParser("add at clementi 6 oct  buy pizza    ");
		assertEquals("clementi", lp5.getLocation());
		LocationParser lp6 = new LocationParser("add at westside 9am  buy pizza    ");
		assertEquals("westside", lp6.getLocation());
		LocationParser lp7 = new LocationParser("add sleep at 12.20am");
		assertEquals(null, lp7.getLocation());
		LocationParser lp8 = new LocationParser("add sleep at 12.20am at \"sunday hotel\"");
		assertEquals("sunday hotel", lp8.getLocation());
		LocationParser lp9 = new LocationParser("add at 12.20am do");
		assertEquals(null, lp9.getLocation());
	}
	
	@Test
	public void testRemoveDescriptionAfterTimeIfAny() {
		LocationParser lp = new LocationParser("abc");
		assertEquals("12.20am", lp.removeDescriptionAfterTimeIfAny("12.20am"));
		assertEquals("12.20am", lp.removeDescriptionAfterTimeIfAny("12.20am do"));
		assertEquals("12.20 am - 1 pm", lp.removeDescriptionAfterTimeIfAny("12.20 am - 1 pm do"));
		assertEquals("12.20 - 1pm", lp.removeDescriptionAfterTimeIfAny("12.20 - 1pm do"));
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
		assertEquals(14, lp.getIndexOfNextReservedWord(0));
		assertEquals(26, lp.getIndexOfNextReservedWord(17));
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
	
	@Test
	public void testIsReservedWord() {
		LocationParser lp = new LocationParser("abc");
		assertTrue(lp.isReservedWord("sunday"));
	}
	
	@Test
	public void testHasDate() {
		LocationParser lp = new LocationParser("abc");
		String[] arr1 = { "nov" , "now"};
		assertTrue(lp.hasDate("6", arr1));
		String[] arr2 = { "nov" , "now"};
		assertTrue(lp.hasDate("6/10", arr2));
		String[] arr3 = { "october" , "15", "16"};
		assertTrue(lp.hasDate("6", arr3));
		String[] arr4 = { "octopus" , "15", "16"};
		assertFalse(lp.hasDate("6", arr4));
	}
	
	@Test
	public void testHasTime() {
		LocationParser lp = new LocationParser("abc");
		String[] arr1 = { "am" , "-", "7" , "am"};
		assertTrue(lp.hasTime("6", arr1));
		String[] arr2 = { "am" , "n"};
		assertTrue(lp.hasTime("6.30", arr2));
		String[] arr3 = { "to" , "8.30", "am"};
		assertTrue(lp.hasTime("6am", arr3));
	}
	
	@Test
	public void testGetUserInputWithoutLocation() {
		LocationParser lp = new LocationParser("  add    buy pizza   at 10am at   clementi on   23 oct ");
		assertEquals("add buy pizza at 10am on 23 oct", lp.getUserInputWithoutLocation());
		LocationParser lp2 = new LocationParser("  add    buy pizza   at 10am  on   23 oct ");
		assertEquals("add buy pizza at 10am on 23 oct", lp2.getUserInputWithoutLocation());
		LocationParser lp3 = new LocationParser("  add    buy pizza   at 10am  on   21 oct  at clementi");
		assertEquals("add buy pizza at 10am on 21 oct", lp3.getUserInputWithoutLocation());
	}
}
