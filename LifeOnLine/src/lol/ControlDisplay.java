package lol;

public class ControlDisplay {
	
	/********** Load Display List ***********/	
	private static TaskList<Task> displayList = LOLControl.getTaskList();
	
	/********** ControlDisplay Methods ***********/	

	public static void refreshDisplay(TaskList<Task> listToDisplay) {
		sortList(listToDisplay);
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
}
