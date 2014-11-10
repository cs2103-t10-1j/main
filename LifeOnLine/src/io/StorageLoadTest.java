/**
 * JUnit tests for loading from a file
 */
package io;

import lol.Date;
import lol.Task;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author aviral
 *
 */
public class StorageLoadTest {

	@Test
	public void loadTest() {
		StorageFacade storageTest;
		storageTest = StorageFacade.getInstance("TestLoadList.txt");

		// 1
		Task testTask = new Task("description", "location", new Date(12, 12,
				2012), null, null);
		testTask.setIsDone(false);
		testTask.setIsOverdue(false);
		testTask.setEndDate(null);
		assertEquals("description, location and date not loaded", testTask,
				storageTest.loadTasks().get(0));

		// gray box - description case

		Task testTask1 = new Task("", "location", new Date(01, 01, 2001), null,
				null);
		testTask1.setIsDone(false);
		testTask1.setIsOverdue(true);
		testTask1.setEndDate(null);
		assertFalse("task loaded when description is null", testTask1 == storageTest
				.loadTasks().get(1));

		// 3

		Task testTask2 = new Task("", null, new Date(01, 01, 2001), null, null);
		testTask2.setIsDone(false);
		testTask2.setIsOverdue(false);
		testTask2.setEndDate(null);
		assertFalse("task loaded when description is null", testTask2 == storageTest
				.loadTasks().get(2));

		// 4 - boundary testing
		Task testTask3 = new Task("", null, null, null, null);
		testTask3.setIsOverdue(true);
		testTask3.setEndDate(null);
		assertFalse("task loaded when description is null", testTask3 ==storageTest.loadTasks()
				.get(3));

		// 5 - boundary testing
		Task testTask4 = new Task("", null, null, null, null);
		testTask4.setIsDone(true);
		testTask4.setEndDate(null);
		assertFalse("task loaded when description is null", testTask4 == storageTest.loadTasks()
				.get(4));

		// 6 - boundary testing
		Task testTask5 = new Task("", null, null, null, null);
		testTask5.setEndDate(null);
		assertFalse("task loaded when description is null", testTask5 == storageTest
				.loadTasks().get(5));

		// 7 - boundary testing
		Task testTask6 = new Task(
				"a bc asdasod 1234 &&**$$ !! //++ by at today null", null,
				null, null, null);
		assertEquals("description not loaded", testTask6, storageTest
				.loadTasks().get(1));

		// 8 - boundary testing
		Task testTask7 = new Task("", null, null, null, null);
		testTask7.setEndDate(new Date(01, 01, 2001));
		assertFalse("task loaded when description is null", testTask7 == storageTest.loadTasks()
				.get(7));

		// 9 out of boundary
		Task testTask8 = new Task("", null, null, null, null);
		testTask8.setEndDate(null);
		testTask8.setIsDone(false);
		testTask8.setIsOverdue(false);
		assertNull("task loaded at null line!", storageTest.loadTasks().get(8));

	}

}
