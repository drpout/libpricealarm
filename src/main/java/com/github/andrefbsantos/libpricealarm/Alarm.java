package com.github.andrefbsantos.libpricealarm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

public abstract class Alarm extends TimerTask implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 438506410563236110L;

	protected Exchange exchange;
	protected Pair pair;
	private boolean on;
	private transient Timer timer;
	private long period;
	private Timestamp lastUpdateTimestamp;
	protected double lastValue;
	protected Notify notify;

	public Alarm(Exchange exchange, Pair pair, Timer timer, long period, Notify notify) {
		this.exchange = exchange;
		this.pair = pair;
		on = true;
		this.timer = timer;
		this.period = period;
		this.notify = notify;
		timer.schedule(this, period, period);
	}

	public void doReset(boolean reset) {
		if (!reset) {
			cancel();
			on = false;
		}
	}

	public Exchange getExchange() {
		return exchange;
	}

	protected double getExchangeLastValue() {
		double lastValue = this.lastValue;
		try {
			lastValue = exchange.getLastValue(pair);
			if (lastUpdateTimestamp == null) {
				lastUpdateTimestamp = new Timestamp(System.currentTimeMillis());
			} else {
				lastUpdateTimestamp.setTime(System.currentTimeMillis());
			}
		} catch (IOException e) {
		}
		return lastValue;
	}

	public double getLastValue() {
		return lastValue;
	}

	public Pair getPair() {
		return pair;
	}

	public long getPeriod() {
		return period;
	}

	public boolean isOn() {
		return on;
	}

	public void setPeriod(long period) {
		this.period = period;
		cancel();
		timer.schedule(this, 0, period);
	}

	public void toggle() {
		if (on) {
			cancel();
			on = false;
		} else {
			timer.schedule(this, 0, period);
			on = true;
		}
	}

	public void turnOff() {
		if (on) {
			cancel();
			on = false;
		}
	}

	public void turnOn() {
		if (!on) {
			timer.schedule(this, 0, period);
			on = true;
		}
	}

	private void writeObject(ObjectOutputStream os) throws IOException, ClassNotFoundException {
		try {
			os.defaultWriteObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readObject(ObjectInputStream is) throws IOException, ClassNotFoundException {
		try {
			is.defaultReadObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
