package lol;

public class Task implements Comparable<Task>{

	/************ Attributes ***************/
	private String description;
	private String location;
	private Date dueDate;
	private boolean isDone;

	/************ Constructors *************/
	public Task(String description, String location, Date dueDate) {
		setDescription(description);
		setLocation(location);
		setDueDate(dueDate);
		setIsDone(false);
	}

	/************ Accessors *************/
	public String getTaskDescription() {
		return description;
	}

	public String getTaskLocation() {
		return location;
	}

	public Date getTaskDueDate() {
		return dueDate;
	}
	
	public boolean getIsDone() {
		return isDone;
	}

	/************ Mutators *************/
	public void setDescription(String description) {
		this.description = description;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setDueDate(Date date) {
		this.dueDate = date;
	}
	
	public void setIsDone(boolean isDone) {
		this.isDone = isDone;
	}

	/********** Overriding methods ***********/
	public String toString() {
		return getTaskDescription();
	}

	public boolean equals(Object obj) {
		if (obj instanceof Task) {
			Task other = (Task) obj;
			boolean isDateSame = false;
			boolean isDescSame = false;
			boolean isLocationSame = false;
			if ((other.getTaskDueDate() == null && this.getTaskDueDate() == null) || (other.getTaskDueDate().equals(this.getTaskDueDate()))) {
				isDateSame = true;
			}
			if (other.getTaskDescription().equals(this.getTaskDescription())) {
				isDescSame = true;
			}
			if ((other.getTaskLocation() == null && this.getTaskLocation() == null) || (other.getTaskLocation().equals(this.getTaskLocation()))) {
				isLocationSame = true;
			}
			return isDateSame && isDescSame && isLocationSame;
		} else {
			return false;
		}
	}

	public int compareTo(Task that) {
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;

		if (this.isBefore(that)) {
			return BEFORE;
		}

		else if (this.isAfter(that)) {
			return AFTER;
		}

		else
		return EQUAL;
	}

	/********** Comparison methods ***********/
	public boolean isBefore(Task other) {
		return this.getTaskDueDate().isBefore(other.getTaskDueDate());
	}

	public boolean isAfter(Task other) {
		return this.getTaskDueDate().isAfter(other.getTaskDueDate());
	}
}
