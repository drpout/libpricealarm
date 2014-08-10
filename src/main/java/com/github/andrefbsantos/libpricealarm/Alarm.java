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

	private long id;
	protected Exchange exchange;
	protected Pair pair;
	private boolean on;
	private transient Timer timer;
	private long period;
	private Timestamp lastUpdateTimestamp;
	protected double lastValue;
	protected Notify notify;

	public Alarm(long id, Exchange exchange, Pair pair, Timer timer, long period, Notify notify) {
		this.id = id;
		this.exchange = exchange;
		this.pair = pair;
		this.on = true;
		this.timer = timer;
		this.period = period;
		this.notify = notify;
		timer.schedule(this, period, period);
	}

	public void doReset(boolean reset) {
		if (!reset) {
			this.cancel();
			this.on = false;
		}
	}

	public Exchange getExchange() {
		return this.exchange;
	}

	protected double getExchangeLastValue() {
		double lastValue = this.lastValue;
		try {
			lastValue = this.exchange.getLastValue(this.pair);
			if (this.lastUpdateTimestamp == null) {
				this.lastUpdateTimestamp = new Timestamp(System.currentTimeMillis());
			} else {
				this.lastUpdateTimestamp.setTime(System.currentTimeMillis());
			}
		} catch (IOException e) {
		}
		return lastValue;
	}

	public double getLastValue() {
		return this.lastValue;
	}

	public Pair getPair() {
		return this.pair;
	}

	public long getPeriod() {
		return this.period;
	}

	public boolean isOn() {
		return this.on;
	}

	public void setPeriod(long period) {
		this.period = period;
		this.cancel();
		this.timer.schedule(this, period, period);
	}

	public void toggle() {
		if (this.on) {
			this.cancel();
			this.on = false;
		} else {
			this.timer.schedule(this, this.period, this.period);
			this.on = true;
		}
	}

	public void turnOff() {
		if (this.on) {
			this.cancel();
			this.on = false;
		}
	}

	public void turnOn() {
		if (!this.on) {
			this.timer.schedule(this, this.period, this.period);
			this.on = true;
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

	public Timer getTimer() {
		return this.timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public void resume() {
		if (this.on) {
			this.timer.schedule(this, this.period, this.period);
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
