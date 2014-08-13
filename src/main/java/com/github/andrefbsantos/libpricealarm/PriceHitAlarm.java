package com.github.andrefbsantos.libpricealarm;

import com.github.andrefbsantos.libdynticker.core.Exchange;
import com.github.andrefbsantos.libdynticker.core.Pair;

public class PriceHitAlarm extends Alarm {

	private static final long serialVersionUID = 7243560546256303827L;
	private double upperBound;
	private double lowerBound;

	public PriceHitAlarm(long id, Exchange exchange, Pair pair, long period, Notify notify,
			double upperBound, double lowerBound) throws UpperBoundSmallerThanLowerBoundException {
		super(id, exchange, pair, period, notify);
		if(upperBound <= lowerBound) {
			throw new UpperBoundSmallerThanLowerBoundException();
		} else {
			this.upperBound = upperBound;
			this.lowerBound = lowerBound;
		}
	}

	@Override
	public boolean run() {
		lastValue = getExchangeLastValue();
		if((lowerBound != Double.NEGATIVE_INFINITY && lastValue <= lowerBound)
				|| (upperBound != Double.POSITIVE_INFINITY && lastValue >= upperBound)) {
			return notify.trigger();
		}
		return true;
	}

	public double getLowerBound() {
		return this.lowerBound;
	}

	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}

	public double getUpperBound() {
		return this.upperBound;
	}

	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}
}
