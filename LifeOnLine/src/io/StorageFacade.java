/**
 * StorageFacade which provides functions of loading and saving
 */
package io;

import lol.Task;
import lol.TaskList;

//@author A0118903H
public class StorageFacade implements Storage {
	private String fileName;
	private StorageOperations operations;
	private static StorageFacade instance = null;

	// private constructor to implement singleton object
	private StorageFacade(String fileName) {
		this.fileName = fileName;
		operations = new StorageOperations(this.fileName);
	}

	/**
	 * Singleton object method
	 * 
	 * @param fileName
	 * @return StorageFacade Object
	 */
	public static StorageFacade getInstance(String fileName) {
		if (instance == null) {
			instance = new StorageFacade(fileName);
		}
		return instance;
	}

	/**
	 * Facade function to load task list
	 * 
	 * @return TaskList
	 */
	public TaskList<Task> loadTasks() {
		return operations.load();
	}

	/**
	 * Facade function to save task list
	 * 
	 * @param TaskList
	 */
	public void saveTasks(TaskList<Task> list) {
		operations.save(list);

	}
}
