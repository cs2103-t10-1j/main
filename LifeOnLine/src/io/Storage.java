/**
 * Storage interface
 */
package io;

import lol.TaskList;
import lol.Task;

//@author A0118903H
public interface Storage {
	public TaskList<Task> loadTasks();
	
	public void saveTasks(TaskList<Task> list);
	
}
