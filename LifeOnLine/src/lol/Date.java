/**
 * This class represents a date with a day, month, year and time from 2010 to 2099.
 * 
 * @author Tania Chattopadhyay
 */

package lol;

public class Date {
	private static final int CURRENT_YEAR = 2014;

	/************ Attributes ***************/
	private int day; // from 1 to 31
	private int month; // from 1 to 12
	private String monthName; // Jan, Feb, ... Dec
	private int year4Digit; // 4 digit year e.g 2013
	private int year2Digit; // 10 to 99 for 2010 to 2099
	private Time time; // Time of the day

	/************ Constructors *************/
	public Date() { // Default value 1.1.2014
		setDay(1);
		setMonth(1);
		setMonthName(getMonthName(1));
		setYear4Digit(CURRENT_YEAR);
		setYear2Digit(CURRENT_YEAR % 100);
		setTime(new Time());
	}

	public Date(int day, int month, int year, Time time) { // e.g 2.3.14 5am or
															// 2.3.2014 5am
		setDay(day);
		setMonth(month);
		setMonthName(getMonthName(month));
		setYear4Digit(getYear4Digit(year));
		setYear2Digit(getYear2Digit(year));
		setTime(time);
	}

	public Date(int day, int month, Time time) { // e.g 6/7 8pm
		setDay(day);
		setMonth(month);
		setMonthName(getMonthName(month));
		setYear4Digit(CURRENT_YEAR);
		setYear2Digit(CURRENT_YEAR % 100);
		setTime(time);
	}

	public Date(int day, int month, int year) { // e.g 2.3.14 or 2.3.2014
		setDay(day);
		setMonth(month);
		setMonthName(getMonthName(month));
		setYear4Digit(getYear4Digit(year));
		setYear2Digit(getYear2Digit(year));
	}

	public Date(int day, int month) { // e.g 6/7
		setDay(day);
		setMonth(month);
		setMonthName(getMonthName(month));
		setYear4Digit(CURRENT_YEAR);
		setYear2Digit(CURRENT_YEAR % 100);
	}

	/************ Accessors *************/
	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
	}

	public String getMonthName() {
		return monthName;
	}

	public int getYear4Digit() {
		return year4Digit;
	}

	public int getYear2Digit() {
		return year2Digit;
	}

	public Time getTime() {
		return time;
	}

	/************ Mutators *************/
	public void setDay(int day) {
		this.day = day;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public void setYear4Digit(int year) {
		this.year4Digit = year;
	}

	public void setYear2Digit(int year) {
		this.year2Digit = year;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	/********** Overriding methods ***********/
	public String toString() { // e.g. 14 Nov 15 or 7 Dec
		if (getYear4Digit() == CURRENT_YEAR) {
			return getDay() + " " + getMonthName();
		} else {
			return getDay() + " " + getMonthName() + " " + getYear2Digit();
		}

	}

	public boolean equals(Object obj) {
		if (obj instanceof Date) {
			Date other = (Date) obj;
			return other.getDay() == this.getDay()
					&& other.getMonth() == this.getMonth()
					&& other.getYear4Digit() == this.getYear4Digit()
					&& other.getTime().equals(this.getTime());
		} else {
			return false;
		}
	}

	/*********** Comparison methods ***********/

	/**
	 * Checks whether a date is earlier than the parameter object
	 * 
	 * @param other
	 *            Date to be compared with
	 * @return true if "this" is earlier than "other", else false
	 */
	public boolean isBefore(Date other) {
		if (this.getYear4Digit() < other.getYear4Digit()) { // year is smaller
			return true;
		} else if (this.getYear4Digit() > other.getYear4Digit()) { // year is
																	// greater
			return false;
		} else { // same year
			if (this.getMonth() < other.getMonth()) { // month is smaller
				return true;
			} else if (this.getMonth() > other.getMonth()) { // month is greater
				return false;
			} else { // saame month
				if (this.getDay() < other.getDay()) { // day is smaller
					return true;
				} else if (this.getDay() > other.getDay()) { // day is greater
					return false;
				} else { // same day
					if (this.getTime().isBefore(other.getTime())) { // time is
																	// smaller
						return true;
					} else { // time is greater or equal
						return false;
					}
				}
			}
		}
	}

	/**
	 * Checks whether a date is later than the parameter object
	 * 
	 * @param other
	 *            Date to be compared with
	 * @return true if "this" is later than "other", else false
	 */
	public boolean isAfter(Date other) {
		return !equals(other) && !isBefore(other);
	}

	/************ Other methods *************/

	/**
	 * Returns first 3 letters of the month
	 * 
	 * @param month
	 *            number of the month
	 * @return month name
	 */
	public String getMonthName(int month) {
		String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" };
		if (month >= 1 && month <= 12) {
			return months[month - 1];
		} else {
			return months[0];
		}
	}

	/**
	 * Returns 4-digit year from 2010 to 2099
	 * 
	 * @param year
	 *            2-digit or 4-digit year
	 * @return 4-digit year
	 */
	private int getYear4Digit(int year) {
		if (year < 100) {
			return 2000 + year;
		} else {
			return year;
		}
	}

	/**
	 * Returns 2-digit year from 2010 to 2099
	 * 
	 * @param year
	 *            2-digit or 4-digit year
	 * @return 10 to 99 - 2-digit year
	 */
	private int getYear2Digit(int year) {
		if (year < 100) {
			return year;
		} else {
			return year - 2000;
		}
	}
}
