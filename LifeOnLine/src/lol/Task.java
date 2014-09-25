package lol;

public class Task implements Comparable<Task>{

	/************ Attributes ***************/
	private String description;
	private String location;
	private Date dueDate;

	/************ Constructors *************/
	public Task(String description, String location, Date dueDate) {
		setDescription(description);
		setLocation(location);
		setDueDate(dueDate);
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

	/********** Overriding methods ***********/
	public String toString() {
		return getTaskDescription();
	}

	public boolean equals(Object obj) {
		if (obj instanceof Time) {
			Task other = (Task) obj;
			return other.getTaskDueDate().getDay() == this.getTaskDueDate().getDay()
			&& other.getTaskDueDate().getMonth() == this.getTaskDueDate().getMonth()
			&& other.getTaskDueDate().getYear4Digit() == this.getTaskDueDate().getYear4Digit()
			&& other.getTaskDueDate().getTime() == this.getTaskDueDate().getTime();
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
