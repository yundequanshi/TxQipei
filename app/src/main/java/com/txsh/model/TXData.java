package com.txsh.model;

import com.google.gson.annotations.Expose;

public class TXData {
	@Expose
	private String date;
	@Expose
	private String day;
	@Expose
	private String hours;
	@Expose
	private String minutes;
	@Expose
	private String month;
	@Expose
	private String nanos;
	@Expose
	private String seconds;
	@Expose
	private String time;
	@Expose
	private String timezoneOffset;
	@Expose
	private String year;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	public String getMinutes() {
		return minutes;
	}
	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getNanos() {
		return nanos;
	}
	public void setNanos(String nanos) {
		this.nanos = nanos;
	}
	public String getSeconds() {
		return seconds;
	}
	public void setSeconds(String seconds) {
		this.seconds = seconds;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTimezoneOffset() {
		return timezoneOffset;
	}
	public void setTimezoneOffset(String timezoneOffset) {
		this.timezoneOffset = timezoneOffset;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	
	
}
