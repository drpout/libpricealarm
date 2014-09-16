package mobi.boilr.libpricealarm;

import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskAlarmWrapper extends TimerTask implements Serializable {

	private static final long serialVersionUID = 1L;
	private Alarm alarm;
	private transient Timer timer;

	public TimerTaskAlarmWrapper(Alarm alarm, Timer timer) {
		this.alarm = alarm;
		this.timer = timer;
		timer.schedule(this, alarm.getPeriod(), alarm.getPeriod());
	}

	@Override
	public void run() {
		try {
			if(!alarm.run()) {
				cancel();
				alarm.turnOff();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setPeriod(long period) {
		alarm.setPeriod(period);
		cancel();
		timer.schedule(this, period, period);
	}

	public void turnOff() {
		if(alarm.isOn()) {
			cancel();
			alarm.turnOff();
		}
	}

	public void turnOn() {
		if(!alarm.isOn()) {
			timer.schedule(this, alarm.getPeriod(), alarm.getPeriod());
			alarm.turnOn();
		}
	}

	public void toggle() {
		if(alarm.isOn())
			cancel();
		else
			timer.schedule(this, alarm.getPeriod(), alarm.getPeriod());
		alarm.toggle();
	}

	public Alarm getAlarm() {
		return alarm;
	}

	public Timer getTimer() {
		return timer;
	}

}
