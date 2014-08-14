package com.github.andrefbsantos.libpricealarm;

import java.util.Timer;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

public class PriceVarAlarm extends Alarm {
	/**
	 *
	 */
	private static final long serialVersionUID = -5424769817492896869L;
	private double variation;
	private int percent;

	public PriceVarAlarm(long id, Exchange exchange, Pair pair, Timer timer, long period,
			Notify notify, double variation) {
		super(id, exchange, pair, timer, period, notify);
		this.variation = variation;
		percent = 0;
		lastValue = getExchangeLastValue();
	}

	public PriceVarAlarm(long id, Exchange exchange, Pair pair, Timer timer, long period,
			Notify notify, int percent) {
		super(id, exchange, pair, timer, period, notify);
		this.percent = percent;
		lastValue = getExchangeLastValue();
		variation = lastValue * (percent * 0.01);
	}

	@Override
	public void run() {
		double newValue = getExchangeLastValue();
		if (Math.abs(lastValue - newValue) >= variation) {
			doReset(notify.trigger());
		}
		lastValue = newValue;
		if (percent > 0) {
			variation = lastValue * (percent * 0.01);
		}
	}

	public double getVariation() {
		return variation;
	}

	public void setVariation(double variation) {
		this.variation = variation;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public boolean isPercent() {
		return percent > 0;
	}
}
