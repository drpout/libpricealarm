package com.github.andrefbsantos.libpricealarm;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Alarm extends TimerTask {
	
	protected Exchange exchange;
	protected Pair pair;
	private boolean on;
	private Timer timer;
	private long period;
	protected float lastValue;
	protected Notify notify;

	public Alarm(Exchange exchange, Pair pair, Timer timer, long period, Notify notify) {
		this.exchange = exchange;
		this.pair = pair;
		this.on = true;
		this.timer = timer;
		this.period = period;
		this.notify = notify;
		timer.schedule(this, 0, period);
	}
	
	public void updateLastValue() {
		lastValue = exchange.getLastValue(pair);
	}
	
	public Exchange getExchange() {
		return exchange;
	}
	
	public Pair getPair() {
		return pair;
	}
	
	public boolean isOn() {
		return on;
	}
	
	public void turnOff() {
		if(on) {
			cancel();
			on = false;
		}
	}
	
	public void turnOn() {
		if(!on) {
			timer.schedule(this, 0, period);
			on = true;
		}
	}
	
	public void toggle() {
		if(on) {
			cancel();
			on = false;
		} else {
			timer.schedule(this, 0, period);
			on = true;
		}
	}
	
	public void doReset(boolean reset) {
		if(!reset) {
			cancel();
			on = false;
		}
	}
	
	public long getPeriod() {
		return period;
	}
	
	public void setPeriod(long period) {
		this.period = period;
		cancel();
		timer.schedule(this, 0, period);
	}
	
	public float getLastValue() {
		return lastValue;
	}
}
