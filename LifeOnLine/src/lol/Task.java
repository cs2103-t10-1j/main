package lol;

public class Task implements Comparable<Task> {

	/************ Attributes ***************/
	private String description;
	private String location;
	private Date dueDate;
	private Date endDate;
	private Time startTime;
	private Time endTime;
	private boolean isDone;
	private boolean isOverdue;
    private boolean alerted;
	/************ Constructors *************/
	public Task(String description, String location, Date dueDate) {
		setDescription(description);
		setLocation(location);
		setDueDate(dueDate);
		setEndDate(null);
		setStartTime(null);
		setEndTime(null);
		setIsDone(false);
		setIsOverdue(false);
		setAlerted(false);
	}

	public Task(String description, String location, Date dueDate,
			Time startTime, Time endTime) {
		setDescription(description);
		setLocation(location);
		setDueDate(dueDate);
		setEndDate(null);
		setStartTime(startTime);
		setEndTime(endTime);
		setIsDone(false);
		setIsOverdue(false);
		setAlerted(false);
	}
	
	public Task(String description, String location, Date dueDate, Date endDate) {
		setDescription(description);
		setLocation(location);
		setDueDate(dueDate);
		setEndDate(endDate);
		setStartTime(null);
		setEndTime(null);
		setIsDone(false);
		setIsOverdue(false);
		setAlerted(false);
	}

	public Task(String description, String location, Date dueDate, Date endDate, 
			Time startTime, Time endTime) {
		setDescription(description);
		setLocation(location);
		setDueDate(dueDate);
		setEndDate(endDate);
		setStartTime(startTime);
		setEndTime(endTime);
		setIsDone(false);
		setIsOverdue(false);
		setAlerted(false);
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
	
	public Date getEndDate() {
		return endDate;
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

	public boolean getIsOverdue() {
		return isOverdue;
	}
	
	public boolean getAlerted() {
		return alerted;
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

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public void setIsOverdue(boolean isOverdue) {
		this.isOverdue = isOverdue;
	}
	
	public void setAlerted(boolean alerted) {
		this.alerted = alerted;
	}

	/********** Overriding methods ***********/
	public String toString() {
		return getTaskDescription();
	}

	public boolean equals(Object obj) {
		if (obj instanceof Task) {
			Task other = (Task) obj;
			boolean isDateSame = false;
			boolean isEndDateSame = false;
			boolean isStartTimeSame = false;
			boolean isEndTimeSame = false;
			boolean isDescSame = false;
			boolean isLocationSame = false;

			if ((other.getStartTime() == null && this.getStartTime() == null)
					|| ((other.getStartTime() != null && this.getStartTime() != null) && (other
							.getStartTime().equals(this.getStartTime())))) {
				isStartTimeSame = true;
			}

			if ((other.getEndTime() == null && this.getEndTime() == null)
					|| ((other.getEndTime() != null && this.getEndTime() != null) && (other
							.getEndTime().equals(this.getEndTime())))) {
				isEndTimeSame = true;
			}

			if ((other.getTaskDueDate() == null && this.getTaskDueDate() == null)
					|| ((other.getTaskDueDate() != null && this
							.getTaskDueDate() != null) && (other
							.getTaskDueDate().equals(this.getTaskDueDate())))) {
				isDateSame = true;
			}
			
			if ((other.getEndDate() == null && this.getEndDate() == null)
					|| ((other.getEndDate() != null && this.getEndDate() != null) && (other
							.getEndDate().equals(this.getEndDate())))) {
				isEndDateSame = true;
			}

			if ((other.getTaskLocation() == null && this.getTaskLocation() == null)
					|| ((other.getTaskLocation() != null && this
							.getTaskLocation() != null) && (other
							.getTaskLocation().equals(this.getTaskLocation())))) {
				isLocationSame = true;
			}

			if ((other.getTaskDescription() == null && this
					.getTaskDescription() == null)
					|| ((other.getTaskDescription() != null && this
							.getTaskDescription() != null) && (other
							.getTaskDescription().equals(this
							.getTaskDescription())))) {
				isDescSame = true;
			}

			return isDateSame && isEndDateSame && isDescSame && isLocationSame
					&& isStartTimeSame && isEndTimeSame;
		} else {
			return false;
		}
	}

	public int compareTo(Task that) {
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;

		// Task with date will be higher priority than tasks without date
		if ((that.getTaskDueDate() == null) && (this.getTaskDueDate() != null)) {
			return BEFORE;
		}

		// Task without date will be lower priority than tasks with date
		else if ((this.getTaskDueDate() == null)
				&& (that.getTaskDueDate() != null)) {
			return AFTER;
		}

		// If all are tasks without date, they prioritize order in which user
		// entered
		else if ((this.getTaskDueDate() == null)
				&& (that.getTaskDueDate() == null)) {
			return AFTER;
		}

		else if ((that.getTaskDueDate() != null)
				&& (this.getTaskDueDate() != null)
				&& (this.getStartTime() != null)
				&& (that.getStartTime() != null) && (this.getEndTime() == null)
				&& (that.getEndTime() == null)
				&& this.getStartTime().equals(that.getStartTime())) {
			return EQUAL;
		}

		else if ((that.getTaskDueDate() != null)
				&& (this.getTaskDueDate() != null)
				&& (this.getStartTime() != null)
				&& (that.getStartTime() != null) && (this.getEndTime() == null)
				&& (that.getEndTime() != null)
				&& this.getStartTime().equals(that.getStartTime())) {
			return BEFORE;
		}

		else if ((that.getTaskDueDate() != null)
				&& (this.getTaskDueDate() != null)
				&& (this.getStartTime() != null)
				&& (that.getStartTime() != null) && (this.getEndTime() != null)
				&& (that.getEndTime() == null)
				&& this.getStartTime().equals(that.getStartTime())) {
			return AFTER;
		}

		// same date, null time -> equal priority
		else if (((this.getTaskDueDate() != null) && (that.getTaskDueDate() != null))
				&& (this.getTaskDueDate().equals(that.getTaskDueDate()))
				&& ((this.getStartTime() == null) && (that.getStartTime() == null))) {

			return EQUAL;
		}

		// same date, null time has lower priority
		else if (((this.getTaskDueDate() != null) && (that.getTaskDueDate() != null))
				&& (this.getTaskDueDate().equals(that.getTaskDueDate()))
				&& ((this.getStartTime() == null) && (that.getStartTime() != null))) {

			return AFTER;
		}

		// same date, non-null time has higher priorty
		else if (((this.getTaskDueDate() != null) && (that.getTaskDueDate() != null))
				&& (this.getTaskDueDate().equals(that.getTaskDueDate()))
				&& ((this.getStartTime() != null) && (that.getStartTime() == null))) {

			return BEFORE;
		}

		// non-null Date that occurs before has higher priority
		else if (this.isBefore(that)) {
			return BEFORE;
		}

		// non-null Date that occurs after has lower priority
		else if (this.isAfter(that)) {
			return AFTER;
		}
		
		// if one date has a date range then it has lower priority
		else if (this.getEndDate() == null && that.getEndDate() != null) {
			return BEFORE;
		}
		
		else if (this.getEndDate() != null && that.getEndDate() == null) {
			return AFTER;
		}
		
		// if both have end date then the one with earlier end date has higher priority
		else if (this.getEndDate().isBefore(that.getEndDate())) {
			return BEFORE;
		}
		
		else if (this.getEndDate().isAfter(that.getEndDate())) {
			return AFTER;
		}

		// both have same end date
		else {
			return EQUAL;
		}
	}

	/********** Comparison methods ***********/
	public boolean isBefore(Task other) {
		return this.getTaskDueDate().isBefore(other.getTaskDueDate())
				|| (this.getTaskDueDate().equals(other.getTaskDueDate()) && (this
						.getStartTime().isBefore(other.getStartTime())))
				|| (this.getTaskDueDate().equals(other.getTaskDueDate())
						&& (this.getStartTime().equals(other.getStartTime())) && (this
							.getEndTime().isBefore(other.getEndTime()))
							
							|| (this.getTaskDueDate().equals(other.getTaskDueDate()) &&
									this.getStartTime().equals(other.getStartTime()) &&
									this.getEndTime().equals(other.getEndTime()) &&
									this.getEndDate().isBefore(other.getEndDate())));
	}

	public boolean isAfter(Task other) {
		return this.getTaskDueDate().isAfter(other.getTaskDueDate())
				|| (this.getTaskDueDate().equals(other.getTaskDueDate()) && (this
						.getStartTime().isAfter(other.getStartTime())))
				|| (this.getTaskDueDate().equals(other.getTaskDueDate())
						&& (this.getStartTime().equals(other.getStartTime())) && (this
							.getEndTime().isAfter(other.getEndTime()))
							
							|| (this.getTaskDueDate().equals(other.getTaskDueDate()) &&
									this.getStartTime().equals(other.getStartTime()) &&
									this.getEndTime().equals(other.getEndTime()) &&
									this.getEndDate().isAfter(other.getEndDate())));
	}
}
