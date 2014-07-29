package com.github.andrefbsantos.libpricealarm;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

public class PriceHitAlarm extends Alarm {
	private float upperBound;
	private float lowerBound;
	
	public PriceHitAlarm(Exchange exchange, Pair pair, Timer timer, long period,
		Notify notify, float upperBound, float lowerBound) {
		super(Exchange exchange, Pair pair, Timer timer, long period, Notify notify);
		if(upperBound <= lowerBound)
			throw new UpperBoundSmallerThanLowerBoundException();
		else {
			this.upperBound = upperBound;
			this.lowerBound = lowerBound;
		}
	}
	
	public PriceHitAlarm(Exchange exchange, Pair pair, Timer timer, long period,
		Notify notify, float upperBound) {
		super(Exchange exchange, Pair pair, Timer timer, long period, Notify notify);
		this.upperBound = upperBound;
		this.lowerBound = Float.NEGATIVE_INFINITY;
	}
	
	public PriceHitAlarm(Exchange exchange, Pair pair, Timer timer, long period,
		Notify notify, float lowerBound) {
		super(Exchange exchange, Pair pair, Timer timer, long period, Notify notify);
		this.upperBound = Float.POSITIVE_INFINITY;
		this.lowerBound = lowerBound;
	}
	
	public void run() {
		updateLastValue();
		if((lowerBound != Float.NEGATIVE_INFINITY && lastValue <= lowerBound) ||
			(upperBound != Float.POSITIVE_INFINITY && lastValue >= upperBound))
			doReset(notify.trigger());
	}
}
