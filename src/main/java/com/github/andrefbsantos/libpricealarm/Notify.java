package com.github.andrefbsantos.libpricealarm;

public interface Notify {
	/**
	 * Triggers a notification (e.g. sound) for the alarm.
	 * 
	 * @return true if alarm should be reset, false if it should be turned off
	 */
	public boolean trigger();

}
