/**
 * 
 */
package io;

import lol.Task;
import lol.TaskList;
/**
 * @author owner
 *
 */

public class StorageFacade implements Storage {
	private String fileName;
	private StorageOperations operations;
	private static StorageFacade instance = null;

	private StorageFacade(String fileName) {
		this.fileName = fileName;
		operations = new StorageOperations(this.fileName);
	}
    public static StorageFacade getInstance(String fileName){
    	if(instance==null){
    		instance = new StorageFacade(fileName);
    	}
    	return instance;
	}
    
    public TaskList<Task> loadTasks(){
    	return operations.load();
    }
    
    public void saveTasks(TaskList<Task> list){
    	operations.save(list);
    	
    }
}
