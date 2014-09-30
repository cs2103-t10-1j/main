package lol;

import java.util.LinkedList;

public class TaskList {

	private static LinkedList<Task> list = new LinkedList<Task>();

	public static boolean add(Task taskObject) {
		return list.add(taskObject);
	}

	public static boolean delete(Task taskObject) {
		return list.removeFirstOccurrence(taskObject);
	}

	public static boolean deleteByIndex(int index) {
		try {
			list.remove(index);
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	public static LinkedList<Task> getList() {
		return list;
	}
	
	public static int size(){
		return list.size();
	}
	
	public static Task get(int index){
		try {
			return list.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
}