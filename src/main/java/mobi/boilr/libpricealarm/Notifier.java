package mobi.boilr.libpricealarm;

import java.io.Serializable;

public abstract class Notifier implements Serializable {
	private static final long serialVersionUID = -4036300151105944485L;

	/**
	 * Checks whether alarm should trigger and launches a notification if so.
	 * @return true if alarm should be reset, false if it should be turned off
	 */
	public boolean trigger(Alarm alarm) {
		if(alarm.isOn()) {
			return notify(alarm.getId());
		}
		else {
			return true;
		}
	}

	/**
	 * Shows/rings/vibrates a notification for the alarm.
	 * @return true if alarm should be reset, false if it should be turned off
	 */
	protected abstract boolean notify(int alarmID);
}
