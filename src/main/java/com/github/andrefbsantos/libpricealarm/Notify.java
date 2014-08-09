package com.github.andrefbsantos.libpricealarm;

import java.io.Serializable;

public interface Notify extends Serializable {
	/**
	 * Triggers a notification (e.g. sound) for the alarm.
	 *
	 * @return true if alarm should be reset, false if it should be turned off
	 */
	public boolean trigger();

}
