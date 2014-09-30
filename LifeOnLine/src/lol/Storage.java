package lol;

public class Storage {
private static TaskList list = new TaskList();

public static TaskList load(){
	return list;
}

public static boolean save(){
	return true;
}
}
