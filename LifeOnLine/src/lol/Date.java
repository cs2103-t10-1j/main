/**
 * This class represents a date with a day, month, year and time from 2010 to 2099.
 * 
 * @author Tania Chattopadhyay
 */

package lol;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Date {

	/************ Attributes ***************/
	private int day; // from 1 to 31
	private int month; // from 1 to 12
	private String monthName; // Jan, Feb, ... Dec
	private int year4Digit; // 4 digit year e.g 2013
	private int year2Digit; // 10 to 99 for 2010 to 2099
	private Time time; // Time of the day

	/************ Constructors *************/
	public Date() { // Default value 1 Jan of current year
		setDay(1);
		setMonth(1);
		setMonthName(getMonthName(1));
		setYear4Digit(getCurrentYear());
		setYear2Digit(getCurrentYear() % 100);
		setTime(new Time());
	}

	public Date(int day, int month, int year, Time time) { // e.g 2/3/14 5am or
															// 2/3/2014 5am
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

		int currYear = getCurrentYear();

		// date with day and month in current year
		Date newDate = new Date(day, month, currYear, time);

		// if date is more than 7 days before today, assume the date is in the
		// next year
		
		if (newDate.isBefore(getTodaysDate())
				&& newDate.getDateDifference(newDate, getTodaysDate()) > Constants.DAYS_OFFSET) {
			setYear4Digit(currYear + 1);
			setYear2Digit((currYear + 1) % 100);
		} else {
			setYear4Digit(currYear);
			setYear2Digit(currYear % 100);
		}

		setTime(time);
	}

	public Date(int day, int month, int year) { // e.g 2/3/14 or 2/3/2014
		setDay(day);
		setMonth(month);
		setMonthName(getMonthName(month));
		setYear4Digit(getYear4Digit(year));
		setYear2Digit(getYear2Digit(year));
		setTime(new Time());
	}

	public Date(int day, int month) { // e.g 6/7
		setDay(day);
		setMonth(month);
		setMonthName(getMonthName(month));

		int currYear = getCurrentYear();

		// date with day and month in current year
		Date newDate = new Date(day, month, currYear);

		// if date is more than 7 days before today, assume the date is in the
		// next year
		
		if (newDate.isBefore(getTodaysDate())
				&& newDate.getDateDifference(newDate, getTodaysDate()) > Constants.DAYS_OFFSET) {
			setYear4Digit(currYear + 1);
			setYear2Digit((currYear + 1) % 100);
		} else {
			setYear4Digit(currYear);
			setYear2Digit(currYear % 100);
		}

		setTime(new Time());
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

	public int getCurrentYear() {
		return getTodaysDate().getYear4Digit();
	}

	public int getCurrentMonth() {
		return getTodaysDate().getMonth();
	}

	public int getCurrentDay() {
		return getTodaysDate().getDay();
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
	public String toString() { // e.g. 7 Dec 2015
		return getDay() + " " + getMonthName() + " " + getYear4Digit();
	}

	public String toString2() { // e.g. 7/12/2015
		return getDay() + "/" + getMonth() + "/" + getYear4Digit();
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
	
	public static boolean equalDate(Date date1, Date date2){
		if(date1 == null && date2 != null){
			return false;
		}
		else if(date1 != null && date2 == null){
			return false;
		}
		else if(date1 == null && date2 == null){
			return true;
		}
		else{ 
			return date1.getDay() == date2.getDay()
					&& date1.getMonth() == date2.getMonth()
					&& date1.getYear4Digit() == date2.getYear4Digit();
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
	 * Returns the entire name of the month
	 * 
	 * @param month
	 *            number of the month
	 * @return month name
	 */
	public String getMonthNameLong(int month) {
		String[] months = { "January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November",
				"December" };
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

	/**
	 * Returns today's date
	 * 
	 * @return Date object containing today's date
	 */
	public Date getTodaysDate() {
		Calendar rightNow = Calendar.getInstance(); // Get the current date
		return new Date(rightNow.get(Calendar.DATE),
				rightNow.get(Calendar.MONTH) + 1, rightNow.get(Calendar.YEAR));
	}

	/**
	 * Returns number of days between 2 dates
	 * 
	 * @param earlierDate
	 *            earlier date
	 * @param laterDate
	 *            later date
	 * @return number of days laterDate is ahead of earlierDate. If earlierDate
	 *         is after laterDate, returns a negative number
	 */
	public int getDateDifference(Date earlierDate, Date laterDate) {
		Calendar earlier = new GregorianCalendar();
		Calendar later = new GregorianCalendar();

		earlier.set(earlierDate.getYear4Digit(), earlierDate.getMonth() - 1,
				earlierDate.getDay());
		later.set(laterDate.getYear4Digit(), laterDate.getMonth() - 1,
				laterDate.getDay());
		
		return (int) ((later.getTime().getTime() - earlier.getTime().getTime())
				/ (1000 * 60 * 60 * 24));
	}
}
