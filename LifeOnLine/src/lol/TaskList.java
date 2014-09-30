package lol;

import java.util.LinkedList;

public class TaskList {

	private  LinkedList<Task> list;
	
	public TaskList(){
		list = new LinkedList<Task>();
	}
	
	public boolean add(Task taskObject) {
		return list.add(taskObject);
	}

	public  boolean delete(Task taskObject) {
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

	public LinkedList<Task> getList() {
		return list;
	}
	
	public int size(){
		return list.size();
	}
	
	public Task get(int index){
		try {
			return list.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
}