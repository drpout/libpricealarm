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

	public PriceHitAlarm(long id, Exchange exchange, Pair pair, Timer timer, long period,
			Notify notify, double upperBound, double lowerBound)
					throws UpperBoundSmallerThanLowerBoundException {
		super(id, exchange, pair, timer, period, notify);
		if (upperBound <= lowerBound) {
			throw new UpperBoundSmallerThanLowerBoundException();
		} else {
			this.upperBound = upperBound;
			this.lowerBound = lowerBound;
		}
	}

	@Override
	public void run() {
		this.lastValue = this.getExchangeLastValue();
		if ((this.lowerBound != Double.NEGATIVE_INFINITY && this.lastValue <= this.lowerBound) || (this.upperBound != Double.POSITIVE_INFINITY && this.lastValue >= this.upperBound)) {
			this.doReset(this.notify.trigger());
		}
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
