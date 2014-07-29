package com.github.andrefbsantos.libpricealarm;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

public class PriceVarAlarm extends Alarm {
	private float variation;
	private int percent;
	
	public PriceVarAlarm(Exchange exchange, Pair pair, Timer timer, long period,
		Notify notify, float variation) {
		super(Exchange exchange, Pair pair, Timer timer, long period, Notify notify);
		this.variation = variation;
		this.percent = 0;
		updateLastValue();
	}
	
	public PriceVarAlarm(Exchange exchange, Pair pair, Timer timer, long period,
		Notify notify, int percent) {
		super(Exchange exchange, Pair pair, Timer timer, long period, Notify notify);
		this.percent = percent;
		updateLastValue();
		variation = lastValue * (percent*0.01);
	}
	
	public void run() {
		float newValue = exchange.getLastValue(pair);
		if(Math.abs(lastValue - newValue) >= variation)
			doReset(notify.trigger());
		lastValue = newValue;
		if(percent)
			variation = lastValue * (percent*0.01);
	}
}
