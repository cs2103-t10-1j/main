package lol;

public class LOLStorage {
private static TaskList<Task> list = new TaskList<Task>();

public static TaskList<Task> load(){
	return list;
}

public static boolean save(){
	return true;
}
}
