/**
 * 
 */
package io;

import lol.TaskList;
import lol.Task;
/**
 * @author aviral
 *
 */
public interface Storage {
	public TaskList<Task> loadTasks();
	
	public void saveTasks(TaskList<Task> list);
	
}
