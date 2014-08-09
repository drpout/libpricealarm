package com.github.andrefbsantos.libpricealarm;

import java.util.Timer;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

public class PriceHitAlarm extends Alarm {
	/**
	 *
	 */
	private static final long serialVersionUID = 7243560546256303827L;
	private double upperBound;
	private double lowerBound;

	public PriceHitAlarm(Exchange exchange, Pair pair, Timer timer, long period, Notify notify,
			double upperBound, double lowerBound) throws UpperBoundSmallerThanLowerBoundException {
		super(exchange, pair, timer, period, notify);
		if (upperBound <= lowerBound) {
			throw new UpperBoundSmallerThanLowerBoundException();
		} else {
			this.upperBound = upperBound;
			this.lowerBound = lowerBound;
		}
	}

	@Override
	public void run() {
		lastValue = getExchangeLastValue();
		if ((lowerBound != Double.NEGATIVE_INFINITY && lastValue <= lowerBound) || (upperBound != Double.POSITIVE_INFINITY && lastValue >= upperBound)) {
			doReset(notify.trigger());
		}
	}

	public double getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}
}
