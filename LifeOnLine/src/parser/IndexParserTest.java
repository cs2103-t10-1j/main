package parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class IndexParserTest {

	@Test
	public void testCleanUpString() {
		fail("Not yet implemented");
	}

	@Test
	public void testCleanUpStringArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveFirstWord() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveCommandName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTaskIndexArray() {
		IndexParser ip1 = new IndexParser("rm 1 2 3");
		int[] arr1 = {1,2,3};
		assertArrayEquals(arr1, ip1.getTaskIndexArray());
		
		IndexParser ip2 = new IndexParser("rm 7,1,9");
		int[] arr2 = {1,7,9};
		assertArrayEquals(arr2, ip2.getTaskIndexArray());
		
		IndexParser ip3 = new IndexParser("rm 5-8");
		int[] arr3 = {5,6,7,8};
		assertArrayEquals(arr3, ip3.getTaskIndexArray());
		
		IndexParser ip4 = new IndexParser("delete 2-7, 10");
		int[] arr4 = {2,3,4,5,6,7,10};
		assertArrayEquals(arr4, ip4.getTaskIndexArray());
		
		IndexParser ip5 = new IndexParser("delete 1-3, 19, 20-23");
		int[] arr5 = {1,2,3,19,20,21,22,23};
		assertArrayEquals(arr5, ip5.getTaskIndexArray());
		
		IndexParser ip6 = new IndexParser("delete 1-3, 19 to 22, 20-23");
		int[] arr6 = {1,2,3,19,20,21,22,23};
		assertArrayEquals(arr6, ip6.getTaskIndexArray());
	}

	@Test
	public void testGetIndexSeparatedBySpace() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetIndexSeparatedByComma() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetIndexRangesSeparatedByComma() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStartIndex() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEndIndex() {
		fail("Not yet implemented");
	}

	@Test
	public void testBuildIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsInteger() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveDuplicates() {
		fail("Not yet implemented");
	}

}
