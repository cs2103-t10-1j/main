package parser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class IndexParserTest {

	@Test
	public void testRemoveFirstWord() {
		IndexParser ip = new IndexParser("abc");
		assertEquals("1,2, 4", ip.removeFirstWord("delete 1,2, 4"));
		assertEquals("3 5 7", ip.removeFirstWord("delete 3 5 7"));
	}

	@Test
	public void testRemoveCommandName() {
		IndexParser ip = new IndexParser("abc");
		assertEquals("1,2, 4", ip.removeFirstWord("rm 1,2, 4"));
		assertEquals("3 5 7", ip.removeFirstWord("del 3 5 7"));
	}

	@Test
	public void testGetTaskIndexArray() {
		IndexParser ip1 = new IndexParser("rm 1 2 3");
		int[] arr1 = { 1, 2, 3 };
		assertArrayEquals(arr1, ip1.getTaskIndexArray());

		IndexParser ip2 = new IndexParser("rm 7,1,9");
		int[] arr2 = { 1, 7, 9 };
		assertArrayEquals(arr2, ip2.getTaskIndexArray());

		IndexParser ip3 = new IndexParser("rm 5-8");
		int[] arr3 = { 5, 6, 7, 8 };
		assertArrayEquals(arr3, ip3.getTaskIndexArray());

		IndexParser ip4 = new IndexParser("delete 2-7, 10");
		int[] arr4 = { 2, 3, 4, 5, 6, 7, 10 };
		assertArrayEquals(arr4, ip4.getTaskIndexArray());

		IndexParser ip5 = new IndexParser("delete 1-3, 19, 20-23");
		int[] arr5 = { 1, 2, 3, 19, 20, 21, 22, 23 };
		assertArrayEquals(arr5, ip5.getTaskIndexArray());

		IndexParser ip6 = new IndexParser("delete 1-3, 19 to 22, 20-23");
		int[] arr6 = { 1, 2, 3, 19, 20, 21, 22, 23 };
		assertArrayEquals(arr6, ip6.getTaskIndexArray());
	}

	@Test
	public void testGetIndexSeparatedBySpace() {
		IndexParser ip = new IndexParser("abc");
		int[] arr = { 1, 7, 8 };
		assertArrayEquals(arr, ip.getIndexSeparatedBySpace("1 7 8"));
	}

	@Test
	public void testGetIndexSeparatedByComma() {
		IndexParser ip = new IndexParser("abc");
		int[] arr = { 12, 13, 18 };
		assertArrayEquals(arr, ip.getIndexSeparatedByComma("12,13,18"));
		assertArrayEquals(arr, ip.getIndexSeparatedByComma("12, 13, 18"));
	}

	@Test
	public void testGetIndexRangesSeparatedByComma() {
		IndexParser ip = new IndexParser("abc");
		String[] arr = { "5-8", "10" };
		assertArrayEquals(arr, ip.getIndexRangesSeparatedByComma("5-8, 10"));
	}

	@Test
	public void testGetStartIndex() {
		IndexParser ip = new IndexParser("abc");
		assertEquals(6, ip.getStartIndex("6-9"));
		assertEquals(11, ip.getStartIndex("11 to 18"));
	}

	@Test
	public void testGetEndIndex() {
		IndexParser ip = new IndexParser("abc");
		assertEquals(9, ip.getEndIndex("6-9"));
		assertEquals(18, ip.getEndIndex("11 to 18"));
	}

	@Test
	public void testBuildIntArray() {
		IndexParser ip = new IndexParser("abc");
		int[] arr = { 1, 2, 3 };
		ArrayList<Integer> al = new ArrayList<Integer>();
		al.add(1);
		al.add(2);
		al.add(3);
		assertArrayEquals(arr, ip.buildIntArray(al));
	}

	@Test
	public void testIsInteger() {
		IndexParser ip = new IndexParser("abc");
		assertTrue(ip.isInteger("7"));
		assertFalse(ip.isInteger("p"));
	}

	@Test
	public void testRemoveDuplicates() {
		IndexParser ip = new IndexParser("abc");
		int[] arr1 = { 1, 5, 9 };
		int[] arr2 = { 5, 1, 1, 1, 5, 5, 9 };
		assertArrayEquals(arr1, ip.removeDuplicates(arr2));
	}
}
