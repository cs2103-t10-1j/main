/**
 * This class holds the Linked List of type Task and provides functionality to add, delete, sort etc.
 */
package lol;

import java.util.Collections;
import java.util.LinkedList;
/**
 * 
 * @author Aviral
 *
 * @param <E>
 */
public class TaskList<E extends Comparable<Task>> {
	
	/******************* Attributes *********************/
	private LinkedList<Task> list;

	/******************* Constructors *********************/
	public TaskList() {
		list = new LinkedList<Task>();
	}
	
	/******************* Accessors *********************/
	public LinkedList<Task> getList() {
		return list;
	}

	/******************* Methods/Mutators *********************/
	public boolean add(Task taskObject) {
		return list.add(taskObject);
	}

	public boolean delete(Task taskObject) {
		return list.removeFirstOccurrence(taskObject);
	}

	public boolean deleteByIndex(int index) {
		try {
			list.remove(index);
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}
	
	public int indexOf(Task taskObject) {
		return list.indexOf(taskObject);
	}

	public void clear() {
		list.clear();
	}

	public void sort() {
		Collections.sort(list);
	}

	
	public int size() {
		return list.size();
	}

	public Task get(int index) {
		try {
			return list.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public boolean set(int index, Task taskObject) {
		try {
			list.set(index, taskObject);
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}
}