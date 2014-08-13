package com.github.andrefbsantos.libpricealarm;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

public abstract class Alarm implements Serializable {

	private static final long serialVersionUID = 438506410563236110L;
	private long id;
	private boolean on;
	private long period;
	private transient Exchange exchange;
	private String exchangeCode;
	private Pair pair;
	protected double lastValue;
	private Timestamp lastUpdateTimestamp;
	protected transient Notify notify;

	public Alarm(long id, Exchange exchange, Pair pair, long period, Notify notify) {
		this.id = id;
		this.exchange = exchange;
		this.exchangeCode = exchange.getClass().getSimpleName();
		this.pair = pair;
		this.on = true;
		this.period = period;
		this.notify = notify;
	}

	/**
	 * Runs the logic behind the alarm and triggers a notification if the
	 * alarm conditions are met.
	 *
	 * @return true if alarm should be reset, false if it should be turned off
	 */
	public abstract boolean run();

	public Exchange getExchange() {
		return this.exchange;
	}

	protected double getExchangeLastValue() {
		double lastValue = this.lastValue;
		try {
			lastValue = this.exchange.getLastValue(this.pair);
			if(this.lastUpdateTimestamp == null) {
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
	}

	public void toggle() {
		on = !on;
	}

	public void turnOff() {
		on = false;
	}

	public void turnOn() {
		on = true;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getExchangeCode() {
		return exchangeCode;
	}

	public Notify getNotify() {
		return notify;
	}

	public void setNotify(Notify notify) {
		this.notify = notify;
	}
}
