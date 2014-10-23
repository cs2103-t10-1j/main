package logic;

import lol.Date;
import lol.DateParser;
import lol.Task;
import lol.TaskList;
import lol.Time;
import lol.TimeParser;

public class ControlDisplay {
	
	/********** Load Temporary Lists ***********/	
	private static TaskList<Task> displayList = LOLControl.getTaskList();
	private static TaskList<Task> toDoList = LOLControl.getToDoList();
	private static TaskList<Task> archiveList = LOLControl.getArchiveList();
	
	/********** ControlDisplay Methods ***********/	

	public static void refreshDisplay(TaskList<Task> listToDisplay, TaskList<Task> storageList) {
		//sort the storage (chronological & mark overdue tasks)
		sortList(storageList);
		
		//tasks that are completed/done get saved to archive & wont be displayed by default  
		addToArchive(storageList);
		
		//sort the tasks that are yet to be completed/done (chronological & mark overdue tasks)
		sortList(listToDisplay);
		
		//display the tasks that are yet to be completed/done
		updateDisplay(listToDisplay);
	}

	private static void updateDisplay(TaskList<Task> listToDisplay) {
		displayList.clear();
		cloneToDisplayList(listToDisplay);
	}

	private static void cloneToDisplayList(TaskList<Task> fromList) {

		for (int i = 0; i < fromList.size(); i++) {
			if (fromList.size() == 0) {
				break;
			} else {
				displayList.add(fromList.get(i));
				sortList(displayList);
			}
		}
	}

	private static void sortList(TaskList<Task> list) {
		list.sort();
		markOverdueTasks(list);
	}

	private static void markOverdueTasks(TaskList<Task> list) {

		TimeParser tp = new TimeParser();
		DateParser dp = new DateParser();

		Time currentTime = tp.getCurrentTime();
		Date currentDate = dp.getTodaysDate();

		for (int i = 0; i < list.size(); i++) {

			if (list.size() == 0) {
				break;
			}

			else if (list.get(i).getTaskDueDate() == null) {
				continue;
			}

			else if (list.get(i).getTaskDueDate() != null
					&& list.get(i).getStartTime() == null) {

				if (list.get(i).getTaskDueDate().isBefore(currentDate)) {

					list.get(i).setIsOverdue(true);
				}

				else {
					continue;
				}
			}

			else if (list.get(i).getTaskDueDate() != null
					&& list.get(i).getStartTime() != null
					&& list.get(i).getEndTime() != null) {

				if (list.get(i).getTaskDueDate().isBefore(currentDate)
						|| list.get(i).getTaskDueDate().equals(currentDate)
						&& list.get(i).getEndTime().isBefore(currentTime)) {

					list.get(i).setIsOverdue(true);
				}

				else {
					continue;
				}
			}

			else if (list.get(i).getTaskDueDate() != null
					&& list.get(i).getStartTime() != null
					&& list.get(i).getEndTime() == null) {

				if (list.get(i).getTaskDueDate().isBefore(currentDate)
						|| list.get(i).getTaskDueDate().equals(currentDate)
						&& list.get(i).getStartTime().isBefore(currentTime)) {

					list.get(i).setIsOverdue(true);
				}

				else {
					continue;
				}
			}
		}
	}
	
	private static void addToArchive(TaskList<Task> listFromStorage) {

		toDoList.clear();
		archiveList.clear();
		
		// if a task is overdue & done, it gets added to the archive
		for (int i = 0; i < listFromStorage.size(); i++) {
			if ((listFromStorage.get(i).getIsOverdue() == true) &&
					listFromStorage.get(i).getIsDone() == true) {
				archiveList.add(listFromStorage.get(i));
			} else {
				toDoList.add(listFromStorage.get(i));
			}
		}
	}
}
