//@author A0118886M

/**
 * This class represents a time of the day.
 */
package lol;

import java.text.DecimalFormat;

public class Time {

	/************ Attributes ***************/
	private int hour; // from 1 to 12 when initialized
	private int min; // from 0 to 59 when initialized
	private String ampm; // am or pm
	private String format24hr; // 4 digit 24hr format

	/************ Constructors *************/
	public Time() { // Uninitialized time
		setHour(Constants.LIMIT_MIN_HR - 1);
		setMin(Constants.LIMIT_MIN_MINUTE);
		setAmpm(Constants.STRING_AM);
		setFormat24hr(Constants.STRING_2400); // Time does not exist
	}

	public Time(int hour, int min, String ampm) { // e.g. 3.40pm
		setHour(hour);
		setMin(min);
		setAmpm(ampm);
		setFormat24hr(convertTo24hr(hour, min, ampm));
	}

	public Time(int hour, String ampm) { // e.g. 2am
		setHour(hour);
		setMin(Constants.LIMIT_MIN_MINUTE);
		setAmpm(ampm);
		setFormat24hr(convertTo24hr(this.hour, this.min, this.ampm));
		;
	}

	public Time(String format24hr) { // e.g. 2359
		setHour(getHour(format24hr));
		setMin(getMin(format24hr));
		setAmpm(getAmpm(format24hr));
		setFormat24hr(format24hr);
	}

	/************ Accessors *************/
	public int getHour() {
		return hour;
	}

	public int getMin() {
		return min;
	}

	public String getAmpm() {
		return ampm;
	}

	public String getFormat24hr() {
		return format24hr;
	}

	/************ Mutators *************/
	public void setHour(int hour) {
		this.hour = hour;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public void setAmpm(String ampm) {
		this.ampm = ampm;
	}

	public void setFormat24hr(String format24hr) {
		this.format24hr = format24hr;
	}

	/********** Overriding methods ***********/
	public String toString() { // e.g. 1.43pm
		DecimalFormat df = new DecimalFormat(Constants.FORMAT_2_DIGITS);
		return getHour() + Constants.DOT + df.format(getMin())
				+ getAmpm();
	}

	public boolean equals(Object obj) {
		if (obj instanceof Time) {
			Time other = (Time) obj;
			return other.getFormat24hr().equals(this.getFormat24hr());
		} else {
			return false;
		}
	}

	/*********** Comparison methods ***********/

	/**
	 * Checks whether a time is earlier than the parameter object
	 * 
	 * @param other
	 *            Time to be compared with
	 * @return true if "this" is earlier than "other", else false
	 */
	public boolean isBefore(Time other) {
		return Integer.parseInt(this.format24hr) < Integer
				.parseInt(other.format24hr);
	}

	/**
	 * Checks whether a time is later than the parameter object
	 * 
	 * @param other
	 *            Time to be compared with
	 * @return true if "this" is later than "other", else false
	 */
	public boolean isAfter(Time other) {
		return Integer.parseInt(this.format24hr) > Integer
				.parseInt(other.format24hr);
	}

	/************ Conversion methods *************/

	/**
	 * Convert a time from 12-hour format to 4-digit 24-format Example: 5.02pm
	 * to "1702"
	 * 
	 * @param hour
	 *            hour from 1 to 12
	 * @param min
	 *            minute from 0 to 59
	 * @param ampm
	 *            am or pm
	 * @return 4-digit 24-hour format string
	 */
	public String convertTo24hr(int hour, int min, String ampm) {
		DecimalFormat df = new DecimalFormat(Constants.FORMAT_2_DIGITS); // 2
																			// digits

		if (hour == Constants.LIMIT_MAX_12HR) {
			hour = Constants.LIMIT_MIN_HR - 1;
		}

		switch (ampm) {
		case Constants.STRING_AM:
			return df.format(hour) + df.format(min);

		case Constants.STRING_PM:
			return df.format(hour + Constants.LIMIT_MAX_12HR) + df.format(min);

		default:
			return Constants.STRING_0000;
		}
	}

	/**
	 * Given a 24-hour format time, returns the hour in 12-hour format Example:
	 * "2250" returns 10
	 * 
	 * @param format24hr
	 *            4-digit 24-hour format string
	 * @return hour in 12-hour format
	 */
	public int getHour(String format24hr) {
		// first 2 digits
		int hr = Integer.parseInt(format24hr.substring(0, 2));

		if (hr == Constants.LIMIT_MIN_HR - 1) {
			hr = Constants.LIMIT_MAX_12HR;
		} else if (hr > Constants.LIMIT_MAX_12HR) {
			hr -= Constants.LIMIT_MAX_12HR;
		}
		return hr;
	}

	/**
	 * Given a 24-hour format time, returns the minute Example: "2250" returns
	 * 50
	 * 
	 * @param format24hr
	 *            4-digit 24-hour format string
	 * @return minute
	 */
	public int getMin(String format24hr) {
		return Integer.parseInt(format24hr.substring(2));
	}

	/**
	 * Given a 24-hour format time, returns whether the time is am or pm in
	 * 12-hour format Example: "2250" returns pm, "0800" returns am
	 * 
	 * @param format24hr
	 *            4-digit 24-hour format string
	 * @return am or pm in 12-hour format
	 */
	public String getAmpm(String format24hr) {
		int hr = Integer.parseInt(format24hr.substring(0, 2));
		return (hr < 12) ? Constants.STRING_AM : Constants.STRING_PM;
	}

}
