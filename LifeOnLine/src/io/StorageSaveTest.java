/**
 * JUnit tests for saving file
 */
package io;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import lol.Date;
import lol.Task;
import lol.TaskList;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
/**
 * 
 * @author aviral
 *
 */
public class StorageSaveTest {

	@Test
	public void test() {
		StorageFacade storageTest;
		storageTest = StorageFacade.getInstance("TestSaveList.txt");

		Task testTask = new Task("description", "location", new Date(12, 12,
				2012), null, null);
		// gray box - description case
		Task testTask1 = new Task("", "location", new Date(01, 01, 0001), null,
				null);
		testTask1.setIsOverdue(true);
		Task testTask2 = new Task("", null, new Date(01, 01, 0001), null, null);
		// 4 - boundary testing
		Task testTask3 = new Task("", null, null, null, null);
		testTask3.setIsOverdue(true);
		// 5 - boundary testing
		Task testTask4 = new Task("", null, null, null, null);
		testTask4.setIsDone(true);
		// 6 - boundary testing
		Task testTask5 = new Task("", null, null, null, null);
		// 7 - boundary testing
		Task testTask6 = new Task(
				"a bc asdasod 1234 &&**$$ !! //++ by at today null", null,
				null, null, null);
		// 8 - boundary testing
		Task testTask7 = new Task("", null, null, null, null);
		testTask7.setEndDate(new Date(01, 01, 0001));

		TaskList<Task> taskList = new TaskList<Task>();
		taskList.add(testTask);
		taskList.add(testTask1);
		taskList.add(testTask2);
		taskList.add(testTask3);
		taskList.add(testTask4);
		taskList.add(testTask5);
		taskList.add(testTask6);
		taskList.add(testTask7);

		storageTest.saveTasks(taskList);

		File file1 = new File("TestLoadList.txt");
		File file2 = new File("TestSaveList.txt");
		try {
			assertTrue("The saved file differs from expected file!",
					FileUtils.contentEquals(file1, file2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
