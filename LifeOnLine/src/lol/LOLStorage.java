package lol;

public class LOLStorage {
private static TaskList list = new TaskList();

public static TaskList load(){
	return list;
}

public static boolean save(){
	return true;
}
}
