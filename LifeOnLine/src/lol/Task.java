package lol;

public class Task implements Comparable<Task>{

	/************ Attributes ***************/
	private String description;
	private String location;
	private Date dueDate;
	private Time startTime;
	private Time endTime;
	private boolean isDone;

	/************ Constructors *************/
	public Task(String description, String location, Date dueDate) {
		setDescription(description);
		setLocation(location);
		setDueDate(dueDate);
		setStartTime(null);
		setEndTime(null);
		setIsDone(false);
	}
	
	public Task(String description, String location, Date dueDate, Time startTime, Time endTime) {
		setDescription(description);
		setLocation(location);
		setDueDate(dueDate);
		setStartTime(startTime);
		setEndTime(endTime);
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
	
	public Time getStartTime() {
		return startTime;
	}
	
	public Time getEndTime() {
		return endTime;
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
	
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
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
			boolean isStartTimeSame = false;
			boolean isEndTimeSame = false;
			boolean isDescSame = false;
			boolean isLocationSame = false;

			if ((other.getStartTime() == null && this.getStartTime() == null) || (other.getStartTime().equals(this.getStartTime()))) {
				isStartTimeSame = true;
			}
			
			if ((other.getEndTime() == null && this.getEndTime() == null) || (other.getEndTime().equals(this.getEndTime()))) {
				isEndTimeSame = true;
			}
			
			if ((other.getTaskDueDate() == null && this.getTaskDueDate() == null) || (other.getTaskDueDate().equals(this.getTaskDueDate()))) {
				isDateSame = true;
			}

			if ((other.getTaskLocation() == null && this.getTaskLocation() == null) || (other.getTaskLocation().equals(this.getTaskLocation()))) {
				isLocationSame = true;
			}
			
			if (other.getTaskDescription().equals(this.getTaskDescription())) {
				isDescSame = true;
			}
			
			
			return isDateSame && isDescSame && isLocationSame && isStartTimeSame && isEndTimeSame;
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
		return this.getTaskDueDate().isBefore(other.getTaskDueDate()) || (this.getTaskDueDate().equals(other.getTaskDueDate()) && (this.getStartTime().isBefore(other.getStartTime())));
	}

	public boolean isAfter(Task other) {
		return this.getTaskDueDate().isAfter(other.getTaskDueDate()) || (this.getTaskDueDate().equals(other.getTaskDueDate()) && (this.getStartTime().isAfter(other.getStartTime())));
	}
}
