import java.util.*;

public class TestSortedList {
	public static void main(String[] args)
	throws NoSuchElementException {
		MySortedTaskList <Task> tasks = new MySortedTaskList <Task>();

		Task t1 = new Task( "Do Homework", "at home", (new Date(4, 2, new Time(10, "am"))) );
		Task t2 = new Task( "Drink Milk", "at work", (new Date(4, 2, 14, new Time("1100"))) );
		Task t3 = new Task( "Sleep", "at school", (new Date(1, 1, 13, new Time("1300"))) );

		tasks.addOrdered(t1);
		tasks.addOrdered(t2);
		tasks.addOrdered(t3);

		System.out.println("List: ");
		System.out.println(tasks);
	}

}
