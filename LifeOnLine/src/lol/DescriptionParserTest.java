package lol;

import static org.junit.Assert.*;

import org.junit.Test;

public class DescriptionParserTest {

	@Test
	public void testGetDescription() throws Exception {
		DescriptionParser dp = new DescriptionParser("add at home on mon do something 8-10pm");
		assertEquals("do something", dp.getDescription());
		DescriptionParser dp1 = new DescriptionParser("add eat 3 pizzas 3 nov at 3 new street 3 pm");
		assertEquals("eat 3 pizzas", dp1.getDescription());
		DescriptionParser dp2 = new DescriptionParser("add today from 9 am-9 pm write 9 pages homework at 9 th room");
		assertEquals("write 9 pages homework", dp2.getDescription());
		DescriptionParser dp3 = new DescriptionParser("add sleep at 12.20am");
		assertEquals("sleep", dp3.getDescription());
	}

	@Test
	public void testRemoveFirstWord() {
		DescriptionParser dp = new DescriptionParser("abc");
		assertEquals("two three four", dp.removeFirstWord("one two three four"));
	}
	
	@Test
	public void testGetDescriptionForEdit() {
		DescriptionParser dp = new DescriptionParser("do something 8-10pm");
		assertEquals("do something", dp.getDescriptionForEdit());
		DescriptionParser dp1 = new DescriptionParser("at post office");
		assertEquals(null, dp1.getDescriptionForEdit());
		DescriptionParser dp2 = new DescriptionParser("11 dec 8-10pm");
		assertEquals(null, dp2.getDescriptionForEdit());
	}
}
