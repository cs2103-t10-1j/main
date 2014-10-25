/**
 * 
 */
package io;

import lol.Date;
import lol.Task;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author owner
 *
 */
public class StorageTest {

	@Test
	public void loadTest() {
	   StorageFacade storageTest;
	   storageTest = StorageFacade.getInstance("TestList.txt");
	   
	   //1
	   Task testTask = new Task("description", "location", new Date(12, 12, 2012), null, null);
	   testTask.setIsDone(false);
	   testTask.setIsOverdue(false);
		assertEquals("description, location and date with values", testTask, storageTest.loadTasks().get(0));
		
		// gray box -  description case
	
		   Task testTask1 = new Task("", "location", new Date(01, 01, 0001), null, null);
		   testTask1.setIsDone(false);
		   testTask1.setIsOverdue(true);
			assertEquals("no description, value of locatation, 01/01/0001 date" ,testTask1, storageTest.loadTasks().get(1));
			
			//3
			
			Task testTask2 = new Task("", null, new Date(01, 01, 0001), null, null);
		    testTask2.setIsDone(false);
		    testTask2.setIsOverdue(false);
		    assertEquals("no description, location, dummy date, no time no done no overdue", testTask2, storageTest.loadTasks().get(2));
		     
		    //4 - boundary testing
		    Task testTask3 = new Task("", null, null, null, null);
		    testTask3.setIsOverdue(true);
		    assertEquals("isoverdue loaded", testTask3, storageTest.loadTasks().get(3));
		    
		    //5 - boundary testing
		    Task testTask4 = new Task("", null, null, null, null);
		    testTask4.setIsDone(true);
		    assertEquals("isDone loaded", testTask4, storageTest.loadTasks().get(4));
		    
		    //6 - boundary testing
		    Task testTask5 = new Task("", null, null, null, null);
		    assertEquals("every variable is empty", testTask5, storageTest.loadTasks().get(5));
		     
             //7 - boundary testing
		    Task testTask6 = new Task("a bc asdasod 1234 &&**$$ !! //++ by at today null", null, null, null, null);
		    assertEquals("description loaded", testTask6, storageTest.loadTasks().get(6));
		    
		    //8 - Out of boundary testing
		    Task testTask7 = new Task("", null, null, null, null);
		    testTask7.setIsDone(false);
		    testTask7.setIsOverdue(false);
		    assertNull("no task present after last line", storageTest.loadTasks().get(7));
		    
	}

}
